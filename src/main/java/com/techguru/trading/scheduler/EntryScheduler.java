package com.techguru.trading.scheduler;

import static com.techguru.trading.constants.TechTradingConstants.KITE_API_URL;
import static com.techguru.trading.constants.TechTradingConstants.PROFIT_OFFSET;
import static com.techguru.trading.constants.TechTradingConstants.STOPLOSS_OFFSET;

import com.techguru.trading.constants.TechTradingConstants;
import com.techguru.trading.model.Candle;
import com.techguru.trading.model.Candle.CandlePosition;
import com.techguru.trading.model.Contract;
import com.techguru.trading.model.Entry;
import com.techguru.trading.model.Entry.EntryType;
import com.techguru.trading.service.CandleService;
import com.techguru.trading.service.ContractService;
import com.techguru.trading.service.EntryService;
import com.techguru.trading.util.Utils;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.DoubleStream;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EntryScheduler {

  private ContractService contractService;

  private CandleService candleService;

  private EntryService entryService;

  private JavaMailSender javaMailSender;

  @Value("${spring.mail.username}")
  private String toAddress;

  @Autowired
  public EntryScheduler(ContractService contractService,
      CandleService candleService, EntryService entryService, JavaMailSender javaMailSender) {
    this.contractService = contractService;
    this.candleService = candleService;
    this.entryService = entryService;
    this.javaMailSender = javaMailSender;
  }

  // Checks entry for active contracts every 15mins
  @Scheduled(cron = "2 0,15,30,45 9-21 ? * MON-FRI")
  public void checkEntry() {

    LocalDateTime localDateTime = LocalDateTime.now();

    //No entries on Thursday after 5pm
    if (DayOfWeek.THURSDAY.equals(localDateTime.getDayOfWeek()) && localDateTime.getHour() > 16) {
      return;
    }

    List<Contract> activeContracts = contractService.findActiveContracts();
    List<Entry> entries = new ArrayList<Entry>();
    activeContracts.forEach((contract) -> {

      String apiUrl = KITE_API_URL.replace("@#$%kiteChartId%$#@", contract.getKiteChartId())
          .replace("@#$%from%$#@",
              LocalDate.now().toString()).replace("@#$%to%$#@",
              LocalDate.now().toString());
      JSONObject response = Utils.getApiResponse(apiUrl);

      Candle candle = Utils.getKiteCandle(response, 2);

      if (candle == null) {
        return;
      }

      Optional<Candle> optionalFirstCandle = candleService
          .findFirstCandle(contract, LocalDate.now());
      if (!optionalFirstCandle.isPresent() && LocalDate.now().isEqual(candle.getTradeDate())) {
        Candle prevHACandle = candleService.findLastCandle(contract).orElseThrow();

        Double HAOpen = DoubleStream.of(prevHACandle.getOpen(), prevHACandle.getClose()).average()
            .getAsDouble();
        Double HAClose = DoubleStream
            .of(candle.getOpen(), candle.getClose(), candle.getHigh(), candle.getClose()).average()
            .getAsDouble();

        Double max = Double.max(HAOpen, HAClose);
        Double min = Double.min(HAOpen, HAClose);
        Double HAHigh = Utils.floorTwoDecimals(Double.max(candle.getHigh(), max));
        Double HALow = Utils.roundTwoDecimals(Double.min(candle.getLow(), min));

        Candle firstHACandle = Candle.builder().candlePosition(CandlePosition.FIRST)
            .contract(contract).tradeDate(candle.getTradeDate()).open(HAOpen).close(HAClose)
            .high(HAHigh).low(HALow).build();
        log.info("First candle - High:{}, Low:{}", firstHACandle.getHigh(), firstHACandle.getLow());
        candleService.addCandle(firstHACandle);
      }

      Candle firstCandle = candleService.findFirstCandle(contract, candle.getTradeDate())
          .orElseThrow();

      Double average = DoubleStream
          .of(candle.getOpen(), candle.getHigh(), candle.getLow(), candle.getClose()).average()
          .getAsDouble();
      Double buyClose = Utils.floorTwoDecimals(average);
      Double sellClose = Utils.roundTwoDecimals(average);

      log.info("Average: {}, Buy close: {}, Sell close: {}", average, buyClose, sellClose);

      Optional<Entry> optionalLastEntry = entryService.findLastEntry(contract, LocalDate.now());

      Candle liveCandle = Utils.getKiteCandle(response, 1);

      if (buyClose >= (firstCandle.getHigh() + TechTradingConstants.ENTRY_OFFSET)) {
        if ((!optionalLastEntry.isPresent()) || (optionalLastEntry.isPresent()
            && EntryType.SELL.equals(optionalLastEntry.get().getEntryType()))) {
          Double entryValue = liveCandle.getOpen();
          Double exitValue = Utils.roundTwoDecimals(entryValue + PROFIT_OFFSET);
          Double stopLoss = Utils.roundTwoDecimals(firstCandle.getLow() - STOPLOSS_OFFSET);
          entries.add(
              Entry.builder().contract(contract).entryType(EntryType.BUY).entryValue(entryValue)
                  .exitValue(exitValue).stopLoss(stopLoss).isActive(Boolean.FALSE)
                  .tradeDate(LocalDate.now()).createdAt(LocalDateTime.now())
                  .updatedAt(LocalDateTime.now()).build());
        }
      } else if (sellClose <= (firstCandle.getLow() - TechTradingConstants.ENTRY_OFFSET)) {
        if ((!optionalLastEntry.isPresent()) || (optionalLastEntry.isPresent()
            && EntryType.BUY.equals(optionalLastEntry.get().getEntryType()))) {
          Double entryValue = liveCandle.getOpen();
          Double exitValue = Utils.roundTwoDecimals(entryValue - PROFIT_OFFSET);
          Double stopLoss = Utils.roundTwoDecimals(firstCandle.getHigh() + STOPLOSS_OFFSET);
          entries.add(
              Entry.builder().contract(contract).entryType(EntryType.SELL).entryValue(entryValue)
                  .exitValue(exitValue).stopLoss(stopLoss).isActive(Boolean.FALSE)
                  .tradeDate(LocalDate.now()).createdAt(LocalDateTime.now())
                  .updatedAt(LocalDateTime.now()).build());
        }
      }
    });
    if (!entries.isEmpty()) {
      entryService.addEntry(entries);
      sendEmail(entries);
    }

    /*List<Contract> activeContracts = contractService.findActiveContracts();
    List<Entry> entries = new ArrayList<Entry>();
    LocalDate testDate = LocalDate.now().minusDays(3);
    log.info("test date {}", testDate.toString());
    activeContracts.forEach((contract) -> {
      try {

        String apiUrl = KITE_API_URL.replace("@#$%kiteChartId%$#@", contract.getKiteChartId())
            .replace("@#$%from%$#@",
                testDate.toString()).replace("@#$%to%$#@",
                testDate.toString());
        JSONObject response = Utils.getApiResponse(apiUrl);

        JSONObject data = response.getJSONObject("data");
        JSONArray candles = data.getJSONArray("candles");

        for (int i = 0; i < candles.length(); i++) {
          JSONArray candleArray = candles.getJSONArray(i);
          Candle candle = Utils.getKiteCandle(candleArray);
          LocalDateTime candleDateTime = Utils.getDateTime(candleArray.getString(0));

          Optional<Candle> optionalFirstCandle = candleService
              .findFirstCandle(contract, candle.getTradeDate());
          if (!optionalFirstCandle.isPresent() *//*&& LocalDate.now().isEqual(candle.getTradeDate())*//*) {
            Candle prevHACandle = candleService.findLastCandle(contract).orElseThrow();

            Double HAOpen = DoubleStream.of(prevHACandle.getOpen(), prevHACandle.getClose())
                .average()
                .getAsDouble();
            Double HAClose = DoubleStream
                .of(candle.getOpen(), candle.getClose(), candle.getHigh(), candle.getClose())
                .average()
                .getAsDouble();

            Double max = Double.max(HAOpen, HAClose);
            Double min = Double.min(HAOpen, HAClose);
            Double HAHigh = Double.max(candle.getHigh(), max);
            Double HALow = Double.min(candle.getLow(), min);

            Candle firstHACandle = Candle.builder().candlePosition(CandlePosition.FIRST)
                .contract(contract).tradeDate(candle.getTradeDate()).open(HAOpen).close(HAClose)
                .high(HAHigh).low(HALow).build();
            candleService.addCandle(firstHACandle);
          }

          Candle firstCandle = candleService.findFirstCandle(contract, candle.getTradeDate())
              .orElseThrow();

          Double average = DoubleStream
              .of(candle.getOpen(), candle.getHigh(), candle.getLow(), candle.getClose())
              .average()
              .getAsDouble();
          Double buyClose = Utils.floorTwoDecimals(average);
          Double sellClose = Utils.roundTwoDecimals(average);

          Optional<Entry> optionalLastEntry = entryService.findLastEntry(contract, LocalDate.now());

          if (buyClose >= (firstCandle.getHigh() + TechTradingConstants.ENTRY_OFFSET)) {
            if ((!optionalLastEntry.isPresent()) || (optionalLastEntry.isPresent()
                && EntryType.SELL.equals(optionalLastEntry.get().getEntryType()))) {
              Double entryValue = candle.getOpen();
              Double exitValue = Utils.roundTwoDecimals(entryValue + PROFIT_OFFSET);
              Double stopLoss = Utils.roundTwoDecimals(firstCandle.getLow() - STOPLOSS_OFFSET);
              Entry entry = Entry.builder().contract(contract).entryType(EntryType.BUY).entryValue(entryValue)
                      .exitValue(exitValue).stopLoss(stopLoss).isActive(Boolean.FALSE)
                      .tradeDate(LocalDate.now()).createdAt(LocalDateTime.now())
                      .updatedAt(LocalDateTime.now()).build();
              entryService.addEntry(entry);
              log.info("entry mail {},{},{},{},{},{}", entry.getEntryType(), entry.getContract().getId(),
                  candleDateTime, entry.getEntryValue(), entry.getExitValue(),
                  entry.getStopLoss());
            }
          } else if (sellClose <= (firstCandle.getLow() - TechTradingConstants.ENTRY_OFFSET)) {
            if ((!optionalLastEntry.isPresent()) || (optionalLastEntry.isPresent()
                && EntryType.BUY.equals(optionalLastEntry.get().getEntryType()))) {
              Double entryValue = candle.getOpen();
              Double exitValue = Utils.roundTwoDecimals(entryValue - PROFIT_OFFSET);
              Double stopLoss = Utils.roundTwoDecimals(firstCandle.getHigh() + STOPLOSS_OFFSET);
             Entry entry  =
                  Entry.builder().contract(contract).entryType(EntryType.SELL)
                      .entryValue(entryValue)
                      .exitValue(exitValue).stopLoss(stopLoss).isActive(Boolean.FALSE)
                      .tradeDate(LocalDate.now()).createdAt(LocalDateTime.now())
                      .updatedAt(LocalDateTime.now()).build();
              entryService.addEntry(entry);
              log.info("entry mail {},{},{},{},{},{}", entry.getEntryType(), entry.getContract().getId(),
                  candleDateTime, entry.getEntryValue(), entry.getExitValue(),
                  entry.getStopLoss());
            }
          }
        }
      } catch (Exception e) {
        log.error("Exception occured", e);
      }
    });*/
  }

  private void sendEmail(List<Entry> entries) {
    SimpleMailMessage msg = new SimpleMailMessage();

    msg.setTo(toAddress);
    msg.setBcc(TechTradingConstants.BCC_ADDRESS);

    entries.forEach((entry) -> {
      msg.setSubject(entry.getContract().getId() + " - " + entry.getEntryType() + " - Entry:"
          + entry.getEntryValue() + ", Exit: " + entry.getExitValue() + ", StopLoss: " + entry
          .getStopLoss());
      msg.setText("Happy Trading");
      javaMailSender.send(msg);
    });
  }
}
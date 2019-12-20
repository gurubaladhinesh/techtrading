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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.DoubleStream;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EntryScheduler {

  private ContractService contractService;

  private CandleService candleService;

  private EntryService entryService;

  @Autowired
  public EntryScheduler(ContractService contractService,
      CandleService candleService, EntryService entryService) {
    this.contractService = contractService;
    this.candleService = candleService;
    this.entryService = entryService;
  }

  // Checks entry for active contracts every 15mins
  @Scheduled(cron = "2 0,15,30,45 9-21 ? * MON-FRI")
  public void checkEntry() {

    List<Contract> activeContracts = contractService.findActiveContracts();
    List<Entry> entries = new ArrayList<Entry>();
    activeContracts.forEach((contract) -> {

      String apiUrl = KITE_API_URL.replace("@#$%kiteChartId%$#@", contract.getKiteChartId())
          .replace("@#$%from%$#@",
              LocalDate.now().minusWeeks(1).toString()).replace("@#$%to%$#@",
              LocalDate.now().toString());
      JSONObject response = Utils.getApiResponse(apiUrl);

      Candle candle = Utils.getKiteCandle(response, 2);

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
        Double HAHigh = Double.max(candle.getHigh(), max);
        Double HALow = Double.min(candle.getLow(), min);

        Candle firstHACandle = Candle.builder().candlePosition(CandlePosition.FIRST)
            .contract(contract).tradeDate(candle.getTradeDate()).open(HAOpen).close(HAClose)
            .high(HAHigh).low(HALow).build();
        candleService.addCandle(firstHACandle);
      }

      Double average = DoubleStream
          .of(candle.getOpen(), candle.getClose(), candle.getHigh(), candle.getClose()).average()
          .getAsDouble();
      Double buyClose = Utils.floorTwoDecimals(average);
      Double sellClose = Utils.roundTwoDecimals(average);

      Candle firstCandle = candleService.findFirstCandle(contract, LocalDate.now()).orElseThrow();
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
      Utils.sendEmail(entries);
    }
  }
}
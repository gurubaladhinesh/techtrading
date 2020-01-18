package com.techguru.trading.scheduler;

import com.techguru.trading.constants.TechTradingConstants;
import com.techguru.trading.model.Candle;
import com.techguru.trading.model.Candle.CandlePosition;
import com.techguru.trading.model.Contract;
import com.techguru.trading.service.CandleService;
import com.techguru.trading.service.ContractService;
import com.techguru.trading.util.Utils;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.DoubleStream;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class LastCandleScheduler {

  private CandleService candleService;

  private ContractService contractService;

  public LastCandleScheduler(CandleService candleService, ContractService contractService) {
    this.candleService = candleService;
    this.contractService = contractService;
  }

  @Scheduled(cron = "0 0 1 ? * TUE-SAT")
  public void addLastHeikinAshiCandle() {

    List<Contract> activeContracts = contractService.findActiveContracts();

    LocalDate date = LocalDate.now().minusDays(1);

    activeContracts.forEach((contract) -> {

          Optional<Candle> optionalCandle = candleService.findFirstCandle(contract, date);

          if (optionalCandle.isPresent()) {

            Candle prevCandle = candleService.findLastCandle(contract).orElseThrow();

            Double prevHAOpen = prevCandle.getOpen();
            Double prevHAClose = prevCandle.getClose();

            Double HAOpen = Double.valueOf(0.0);
            Double HAClose = Double.valueOf(0.0);

            String apiUrl =
                TechTradingConstants.KITE_API_URL
                    .replace("@#$%kiteChartId%$#@", contract.getKiteChartId())
                    .replace("@#$%from%$#@",
                        date.toString()).replace("@#$%to%$#@",
                    date.toString());

            JSONObject responseJson = Utils.getApiResponse(apiUrl);
            JSONObject data = responseJson.getJSONObject("data");
            JSONArray candles = data.getJSONArray("candles");

            for (int i = 0; i < candles.length(); i++) {
              JSONArray candle = candles.getJSONArray(i);

              LocalDateTime dateTime = Utils.getDateTime(candle.getString(0));
              Double open = candle.getDouble(1);
              Double high = candle.getDouble(2);
              Double low = candle.getDouble(3);
              Double close = candle.getDouble(4);

              HAOpen = DoubleStream.of(prevHAOpen, prevHAClose).average().getAsDouble();
              HAClose = DoubleStream.of(open, high, low, close).average().getAsDouble();

              prevHAOpen = HAOpen;
              prevHAClose = HAClose;
            }
            Candle candle = Candle.builder().candlePosition(CandlePosition.LAST).tradeDate(date)
                .contract(contract).open(Utils.roundTwoDecimals(HAOpen))
                .close(Utils.roundTwoDecimals(HAClose)).build();
            candleService.addCandle(candle);
          }
        }
    );
  }
}

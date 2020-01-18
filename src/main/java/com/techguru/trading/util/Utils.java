package com.techguru.trading.util;

import com.techguru.trading.constants.TechTradingConstants;
import com.techguru.trading.model.Candle;
import com.techguru.trading.model.Contract;
import com.techguru.trading.model.Entry;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.web.util.HtmlUtils;

@Component
@Slf4j
public class Utils implements TechTradingConstants {

  public static JSONObject getApiResponse(String apiUrl) {
    log.info("API url: {}", apiUrl);
    String response = StringUtils.EMPTY;
    try {
      URL url = new URL(apiUrl);
      HttpURLConnection con = (HttpURLConnection) url.openConnection();
      con.setRequestMethod("GET");
      con.setRequestProperty("User-Agent", "Mozilla/5.0");
      if (HttpURLConnection.HTTP_OK == con.getResponseCode()) {
        response = readResponse(con);
      }
    } catch (MalformedURLException e) {
      e.printStackTrace();
    } catch (ProtocolException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return new JSONObject(response);
  }

  private static String readResponse(HttpURLConnection con) throws IOException {
    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
    String inputLine;
    StringBuffer response = new StringBuffer();

    while ((inputLine = in.readLine()) != null) {
      response.append(inputLine);
    }
    in.close();
    return response.toString();
  }

  public static LocalDateTime getDateTime(String dateTime) {
    String pattern = "yyyy-MM-dd'T'HH:mm:ssZ";
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
    ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateTime, formatter);
    return zonedDateTime.toLocalDateTime();
  }

  public static Candle getKiteCandle(JSONObject response, int candlePositionFromLast) {
    JSONObject data = response.getJSONObject("data");
    JSONArray candles = data.getJSONArray("candles");
    if(candles.length()<2){
      return null;
    }

    JSONArray candle = candles.getJSONArray(candles.length() - candlePositionFromLast);

    LocalDateTime dateTime = getDateTime(candle.getString(0));
    Double open = candle.getDouble(1);
    Double high = candle.getDouble(2);
    Double low = candle.getDouble(3);
    Double close = candle.getDouble(4);

    return Candle.builder().tradeDate(dateTime.toLocalDate()).open(open).high(high).low(low)
        .close(close).build();
  }

  public static Candle getKiteCandle(JSONArray candle){
    LocalDateTime dateTime = getDateTime(candle.getString(0));
    Double open = candle.getDouble(1);
    Double high = candle.getDouble(2);
    Double low = candle.getDouble(3);
    Double close = candle.getDouble(4);

    return Candle.builder().tradeDate(dateTime.toLocalDate()).open(open).high(high).low(low)
        .close(close).build();
  }

  public static Double roundTwoDecimals(double value) {
    double scale = Math.pow(10, 2);
    return Math.round(value * scale) / scale;
  }

  public static Double floorTwoDecimals(double value) {
    double scale = Math.pow(10, 2);
    return Math.floor(value * scale) / scale;
  }

}

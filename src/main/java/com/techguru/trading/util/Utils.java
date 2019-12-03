package com.techguru.trading.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.techguru.trading.constants.TechTradingConstants;
import com.techguru.trading.model.Candle;
import com.techguru.trading.model.Contract;
import com.techguru.trading.model.Entry;
import com.techguru.trading.model.HeikinAshiCandle;

@Component
public class Utils implements TechTradingConstants {

	@Autowired
	public JavaMailSender javaMailSender;

	@Value("${spring.mail.username}")
	private String toAddress;

	public JSONObject getInvestingApiResponse(Contract contract) {

		String apiUrl = INVESTING_API_URL.replace("@#$%pairId%$#@", contract.getPairId());
		String response = StringUtils.EMPTY;

		try {
			URL url = new URL(apiUrl);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("User-Agent", "Mozilla/5.0");
			con.setRequestProperty("Referer", contract.getRefererUrl());
			con.setRequestProperty("Sec-Fetch-Mode", "cors");
			con.setRequestProperty("Sec-Fetch-Site", "same-origin");
			con.setRequestProperty("X-Requested-With", "XMLHttpRequest");
			con.setRequestProperty("Host", INVESTING_HOST);
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

	public JSONObject getKiteApiResponse(Contract contract) {

		String apiUrl = KITE_API_URL.replace("@#$%kiteChartId%$#@", contract.getKiteChartId());

		System.out.println("Kite API url::" + apiUrl);

		String response = StringUtils.EMPTY;

		try {
			URL url = new URL(apiUrl);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("User-Agent", "Mozilla/5.0");
			if (HttpURLConnection.HTTP_OK == con.getResponseCode()) {
				response = readResponse(con);
				System.out.println("response::" + response);
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

	public Candle getInvestingCandle(JSONObject jsonObject, int candlePositionFromLast) {
		JSONArray candlesArray = jsonObject.getJSONArray("candles");
		JSONArray candleArray = candlesArray.getJSONArray(candlesArray.length() - candlePositionFromLast);
		LocalDateTime candleDateTime = Instant.ofEpochMilli(candleArray.getLong(0)).atZone(ZoneId.of(ZONE_ID_INDIA))
				.toLocalDateTime();
		Candle candle = new Candle();
		candle.setTradeDate(candleDateTime.toLocalDate());
		candle.setOpen(candleArray.getDouble(1));
		candle.setHigh(candleArray.getDouble(2));
		candle.setLow(candleArray.getDouble(3));
		candle.setClose(candleArray.getDouble(4));
		return candle;
	}

	// get HeikinAshi candle from normal candle and last recoded HeikinAshi candle
	public HeikinAshiCandle getHeikinAshiCandle(HeikinAshiCandle heikinAshiCandle, Candle candle) {
		HeikinAshiCandle resultHeikinAshiCandle = new HeikinAshiCandle();
		resultHeikinAshiCandle.setClose(
				roundTwoDecimalPlaces((candle.getOpen() + candle.getHigh() + candle.getLow() + candle.getClose()) / 4));
		resultHeikinAshiCandle
				.setOpen(roundTwoDecimalPlaces((heikinAshiCandle.getOpen() + heikinAshiCandle.getClose()) / 2));
		resultHeikinAshiCandle.setHigh(roundTwoDecimalPlaces(Math
				.max(Math.max(candle.getHigh(), resultHeikinAshiCandle.getOpen()), resultHeikinAshiCandle.getClose())));
		resultHeikinAshiCandle.setLow(roundTwoDecimalPlaces(Math
				.min(Math.min(candle.getLow(), resultHeikinAshiCandle.getOpen()), resultHeikinAshiCandle.getClose())));

		resultHeikinAshiCandle.setContract(heikinAshiCandle.getContract());
		return resultHeikinAshiCandle;
	}

	// send email
	public void sendEmail(List<Entry> entries) {
		SimpleMailMessage msg = new SimpleMailMessage();

		msg.setTo(toAddress);
		msg.setBcc(BCC_ADDRESS);

		entries.forEach((entry) -> {
			msg.setSubject(entry.getContract().getId() + " - " + entry.getEntryType() + " - Entry:"
					+ entry.getEntryValue() + ", Exit: " + entry.getExitValue());
			msg.setText("Happy Trading");
			javaMailSender.send(msg);
		});

	}

	public LocalDateTime getInvestingLocalDateTimeFromResponse(JSONObject response, int candlePositionFromLast) {
		JSONArray candlesArray = response.getJSONArray("candles");
		JSONArray candleArray = candlesArray.getJSONArray(candlesArray.length() - candlePositionFromLast);
		return Instant.ofEpochMilli(candleArray.getLong(0)).atZone(ZoneId.of(ZONE_ID_INDIA)).toLocalDateTime();

	}

	public Double roundTwoDecimalPlaces(Double value) {
		double scale = Math.pow(10, 2);
		return Math.floor(value * scale) / scale;
	}

	public LocalDateTime getKiteLocalDateTimeFromResponse(JSONObject response, int candlePositionFromLast) {
		JSONObject data = (JSONObject) response.getJSONObject("data");
		JSONArray candlesArray = data.getJSONArray("candles");
		JSONArray candleArray = candlesArray.getJSONArray(candlesArray.length() - candlePositionFromLast);
		String dateTime = candleArray.getString(0);
		String pattern = "yyyy-MM-dd'T'HH:mm:ssZ";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateTime, formatter);
		return zonedDateTime.toLocalDateTime();
	}

	public Candle getKiteCandle(JSONObject jsonObject, int candlePositionFromLast) {
		JSONObject data = (JSONObject) jsonObject.getJSONObject("data");
		JSONArray candlesArray = data.getJSONArray("candles");
		JSONArray candleArray = candlesArray.getJSONArray(candlesArray.length() - candlePositionFromLast);
		LocalDateTime candleDateTime = getKiteLocalDateTimeFromResponse(jsonObject, candlePositionFromLast);
		Candle candle = new Candle();
		candle.setTradeDate(candleDateTime.toLocalDate());
		candle.setOpen(candleArray.getDouble(1));
		candle.setHigh(candleArray.getDouble(2));
		candle.setLow(candleArray.getDouble(3));
		candle.setClose(candleArray.getDouble(4));
		return candle;
	}

}

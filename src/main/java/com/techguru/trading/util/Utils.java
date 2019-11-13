package com.techguru.trading.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.techguru.trading.constants.TechTradingConstants;
import com.techguru.trading.model.Contract;
import com.techguru.trading.model.FirstCandle;

public class Utils implements TechTradingConstants {

	public FirstCandle getFirstCandle(Contract contract) {
		JSONObject responseJson = getInvestingApiResponse(contract);
		FirstCandle firstCandle = null;
		if (responseJson != null) {
			firstCandle = parseFirstCandleInvestingApiResponse(responseJson);
			firstCandle.setContract(contract);
		}
		return firstCandle;
	}

	private JSONObject getInvestingApiResponse(Contract contract) {

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

	private String readResponse(HttpURLConnection con) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		return response.toString();
	}

	private FirstCandle parseFirstCandleInvestingApiResponse(JSONObject jsonObject) {

		JSONArray candlesArray = jsonObject.getJSONArray("candles");
		JSONArray candle = candlesArray.getJSONArray(candlesArray.length() - 2);

		LocalDate firstCandleDate = Instant.ofEpochMilli(candle.getLong(0)).atZone(ZoneId.of(ZONE_ID_INDIA))
				.toLocalDate();
		FirstCandle firstCandle = new FirstCandle();
		firstCandle.setTradeDate(firstCandleDate);
		firstCandle.setHigh(candle.getDouble(2));
		firstCandle.setLow(candle.getDouble(3));
		return firstCandle;
	}
}

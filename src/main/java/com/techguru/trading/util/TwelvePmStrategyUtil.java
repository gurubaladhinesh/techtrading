package com.techguru.trading.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;

public class TwelvePmStrategyUtil {

  public static void main(String[] args) throws IOException {

    String apiUrl =
        "https://kitecharts-aws.zerodha.com/api/chart/55592455/60minute?from=" + LocalDate.now()
            .minusMonths(4).toString() + "&to=" + LocalDate.now().minusDays(1).toString();

    JSONObject response = Utils.getApiResponse(apiUrl);

    JSONObject data = response.getJSONObject("data");
    JSONArray candles = data.getJSONArray("candles");

    LocalDate currentDate = null;

    Workbook workbook = new XSSFWorkbook();
    Sheet ohlcSheet = workbook.createSheet("ohlc");

    String[] ohlcHeaders = {"Date", "Time", "Open", "High", "Low", "Close"};

    int ohlcCellCount = ohlcHeaders.length;
    int ohlcRowCount = 0;

    Row ohlcHeaderRow = ohlcSheet.createRow(ohlcRowCount++);
    for (int i = 0; i < ohlcCellCount; i++) {
      Cell cell = ohlcHeaderRow.createCell(i, CellType.STRING);
      cell.setCellValue(ohlcHeaders[i]);
    }

    for (int i = 0; i < candles.length(); i++) {
      JSONArray candle = candles.getJSONArray(i);

      LocalDateTime candleDateTime = Utils.getDateTime(candle.getString(0));
      double open = candle.getDouble(1);
      double high = candle.getDouble(2);
      double low = candle.getDouble(3);
      double close = candle.getDouble(4);

      Row ohlcRow = ohlcSheet.createRow(ohlcRowCount++);

      Cell ohlcCell;

      if (currentDate == null || !candleDateTime.toLocalDate().isEqual(currentDate)) {
        ohlcCell = ohlcRow.createCell(0, CellType.NUMERIC);
        ohlcCell.setCellValue(candleDateTime.toLocalDate());
      }
      currentDate = candleDateTime.toLocalDate();

      ohlcCell = ohlcRow.createCell(1, CellType.STRING);
      ohlcCell.setCellValue(candleDateTime.toLocalTime().toString());

      ohlcCell = ohlcRow.createCell(2, CellType.NUMERIC);
      ohlcCell.setCellValue(open);

      ohlcCell = ohlcRow.createCell(3, CellType.NUMERIC);
      ohlcCell.setCellValue(high);

      ohlcCell = ohlcRow.createCell(4, CellType.NUMERIC);
      ohlcCell.setCellValue(low);

      ohlcCell = ohlcRow.createCell(5, CellType.NUMERIC);
      ohlcCell.setCellValue(close);

    }
    try (FileOutputStream outputStream = new FileOutputStream(
        "/home/gurubaladhinesh/Desktop/12pmstrategy_january_contract.xlsx")) {
      workbook.write(outputStream);
    }
  }
}

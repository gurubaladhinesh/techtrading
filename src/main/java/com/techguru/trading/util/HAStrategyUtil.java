package com.techguru.trading.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.stream.DoubleStream;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;

public class HAStrategyUtil {

  public static void main(String[] args) throws IOException {

    String apiUrl =
        "https://kitecharts-aws.zerodha.com/api/chart/55592455/15minute?from=" + LocalDate.now()
            .minusMonths(4).toString() + "&to=" + LocalDate.now().minusDays(1).toString();

    JSONObject response = Utils.getApiResponse(apiUrl);

    JSONObject data = response.getJSONObject("data");
    JSONArray candles = data.getJSONArray("candles");

    double firstHAOpen = 185.15;

    LocalDate currentDate = null;

    Workbook workbook = new XSSFWorkbook();
    Sheet entrySheet = workbook.createSheet("entry");
    Sheet ohlcSheet = workbook.createSheet("ohlc");

    String[] ohlcHeaders = {"Date", "Time", "Open", "High", "Low", "Close"};
    String[] entryHeaders = {"Date", "FirstHAOpen", "FirstHAHigh", "FirstHALow", "FirstHAClose",
        "9:00AM",
        "9:15AM",
        "9:30AM",
        "9:45AM",

        "10:00AM",
        "10:15AM",
        "10:30AM",
        "10:45AM",

        "11:00AM",
        "11:15AM",
        "11:30AM",
        "11:45AM",

        "12:00PM",
        "12:15PM",
        "12:30PM",
        "12:45PM",

        "1:00PM",
        "1:15PM",
        "1:30PM",
        "1:45PM",

        "2:00PM",
        "2:15PM",
        "2:30PM",
        "2:45PM",

        "3:00PM",
        "3:15PM",
        "3:30PM",
        "3:45PM",

        "4:00PM",
        "4:15PM",
        "4:30PM",
        "4:45PM",

        "5:00PM",
        "5:15PM",
        "5:30PM",
        "5:45PM",

        "6:00PM",
        "6:15PM",
        "6:30PM",
        "6:45PM",

        "7:00PM",
        "7:15PM",
        "7:30PM",
        "7:45PM",

        "8:00PM",
        "8:15PM",
        "8:30PM",
        "8:45PM",

        "9:00PM",
        "9:15PM",
        "9:30PM",
        "9:45PM",

        "10:00PM",
        "10:15PM",
        "10:30PM",
        "10:45PM",

        "11:00PM",
        "11:15PM",
        "11:30PM",
        "11:45PM"
    };

    int ohlcCellCount = ohlcHeaders.length;
    int entryCellCount = entryHeaders.length;
    int ohlcRowCount = 0;
    int entryRowCount = 0;

    Row ohlcHeaderRow = ohlcSheet.createRow(ohlcRowCount++);
    for (int i = 0; i < ohlcCellCount; i++) {
      Cell cell = ohlcHeaderRow.createCell(i, CellType.STRING);
      cell.setCellValue(ohlcHeaders[i]);
    }

    Row entryHeaderRow = entrySheet.createRow(entryRowCount++);
    for (int i = 0; i < entryCellCount; i++) {
      Cell cell = entryHeaderRow.createCell(i, CellType.STRING);
      cell.setCellValue(entryHeaders[i]);
    }

    Row entryRow = null;
    int entryCurrentCellCount = 0;
    for (int i = 0; i < candles.length(); i++) {
      JSONArray candle = candles.getJSONArray(i);

      LocalDateTime candleDateTime = Utils.getDateTime(candle.getString(0));
      double open = candle.getDouble(1);
      double high = candle.getDouble(2);
      double low = candle.getDouble(3);
      double close = candle.getDouble(4);
      double haClose = DoubleStream.of(open, high, low, close).average().orElseThrow();

      Row ohlcRow = ohlcSheet.createRow(ohlcRowCount++);

      Cell ohlcCell;

      if (currentDate == null || !candleDateTime.toLocalDate().isEqual(currentDate)) {
        ohlcCell = ohlcRow.createCell(0, CellType.NUMERIC);
        ohlcCell.setCellValue(candleDateTime.toLocalDate());

        entryRow = entrySheet.createRow(entryRowCount++);
        Cell entryCell = entryRow.createCell(0, CellType.NUMERIC);//Date
        entryCell.setCellValue(candleDateTime.toLocalDate());

        entryCell = entryRow.createCell(1, CellType.NUMERIC);
        entryCell.setCellValue(Utils.roundTwoDecimals(firstHAOpen));

        entryCell = entryRow.createCell(2, CellType.NUMERIC);
        entryCell.setCellValue(Utils
            .roundTwoDecimals(DoubleStream.of(high, firstHAOpen, haClose).max().getAsDouble()));

        entryCell = entryRow.createCell(3, CellType.NUMERIC);
        entryCell.setCellValue(
            Utils.roundTwoDecimals(DoubleStream.of(low, firstHAOpen, haClose).min().getAsDouble()));

        entryCell = entryRow.createCell(4, CellType.NUMERIC);
        entryCell.setCellValue(Utils.roundTwoDecimals(haClose));

        if (candleDateTime.toLocalTime().isAfter(LocalTime.NOON)) { // opening at 5pm
          for (int j = 0; j < entryHeaders.length; j++) {
            if ("5:00PM".equals(entryHeaders[j])) {
              entryCurrentCellCount = j;
            }
          }
        } else {
          for (int j = 0; j < entryHeaders.length; j++) {
            if ("9:00AM".equals(entryHeaders[j])) {
              entryCurrentCellCount = j;
            }
          }
        }
      }
      currentDate = candleDateTime.toLocalDate();

      Cell entryCell = entryRow.createCell(entryCurrentCellCount++, CellType.NUMERIC);
      entryCell.setCellValue(haClose);
      firstHAOpen = DoubleStream.of(firstHAOpen, haClose).average().orElseThrow();

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
        "/home/gurubaladhinesh/Desktop/ha_strategy_january_contract.xlsx")) {
      workbook.write(outputStream);
    }
  }
}
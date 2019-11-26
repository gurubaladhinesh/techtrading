package com.techguru.trading.constants;

public interface TechTradingConstants {

	public static final Double PROFIT_OFFSET = 0.7;

	public static final String DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

	public static final String DATE_FORMAT = "yyyy-MM-dd";

	public static final String INVESTING_API_URL = "https://in.investing.com/common/modules/js_instrument_chart/api/data.php?pair_id=@#$%pairId%$#@"
			+ "&chart_type=candlestick&pair_interval=900&candle_count=20";

	public static final String INVESTING_HOST = "in.investing.com";
	
	public static final String ZONE_ID_INDIA = "Asia/Kolkata";
	
	public static final Double ENTRY_OFFSET = 0.1;
}

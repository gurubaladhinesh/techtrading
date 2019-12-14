package com.techguru.trading.constants;

import java.time.LocalDate;

public interface TechTradingConstants {

	public static final Double PROFIT_OFFSET = 0.5;

	public static final String DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm";

	public static final String DATE_FORMAT = "yyyy-MM-dd";

	public static final String INVESTING_API_URL = "https://in.investing.com/common/modules/js_instrument_chart/api/data.php?pair_id=@#$%pairId%$#@"
			+ "&chart_type=candlestick&pair_interval=900&candle_count=20";

	public static final String INVESTING_HOST = "in.investing.com";

	public static final String ZONE_ID_INDIA = "Asia/Kolkata";

	public static final Double ENTRY_OFFSET = 0.1;

	public static final String[] BCC_ADDRESS = { "gurubaladhinesh@gmail.com", "c.sasikumar@live.com" };

	public static final String KITE_API_URL = "https://kitecharts-aws.zerodha.com/api/chart/@#$%kiteChartId%$#@/15minute?from="
			+ LocalDate.now().minusWeeks(1) + "&to=" + LocalDate.now();
	
	public static final Double STOPLOSS_OFFSET = 0.2;
}

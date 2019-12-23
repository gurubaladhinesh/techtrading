package com.techguru.trading.constants;

import java.time.LocalDate;

public interface TechTradingConstants {

	public static final Double PROFIT_OFFSET = 0.7;

	public static final String DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm";

	public static final String DATE_FORMAT = "yyyy-MM-dd";

	public static final Double ENTRY_OFFSET = 0.1;

	public static final String[] BCC_ADDRESS = { "gurubaladhinesh@gmail.com", "c.sasikumar@live.com",
			"kavithakamaraj5@gmail.com" };

	public static final String KITE_API_URL = "https://kitecharts-aws.zerodha.com/api/chart/@#$%kiteChartId%$#@/15minute?from=@#$%from%$#@&to=@#$%to%$#@";

	public static final Double STOPLOSS_OFFSET = 0.2;
}

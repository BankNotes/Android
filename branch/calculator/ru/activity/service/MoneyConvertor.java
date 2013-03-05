package calculator.ru.activity.service;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public final class MoneyConvertor {

	public static String convertToMoneyFormat(double number) {
		NumberFormat numFormat = NumberFormat.getInstance();

		if (numFormat instanceof DecimalFormat) {
			((DecimalFormat) numFormat).setDecimalSeparatorAlwaysShown(true);
		}

		NumberFormat dFormat = (DecimalFormat) numFormat;
		dFormat.setMaximumFractionDigits(2);
		dFormat.setMinimumFractionDigits(2);

		return dFormat.format(number);

	}
	
	
}

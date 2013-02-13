package calculator.ru.activity;

import java.util.HashMap;

public class ResCalcItem extends HashMap<String, String>{

	
	private static final long serialVersionUID = 1L;
	public static final String ID_CALC="id_calc";
	public static final String SUM ="sum_of_loan";
	public static final String PERCENT = "percent";
	public static final String DATE="date_of_loan";
	public static final String QTY_PAYMENTS="qty_payments";
	public static final String CREDIT_TYPE="credit_type";
	public static final String CALC_TYPE="calc_type";
	
	public ResCalcItem(String idCalc, String sum, String percent, String date, String qtyPayments, String creditType, String calcType){
		super();
		super.put(ID_CALC, idCalc);
		super.put(SUM, sum);
		super.put(PERCENT, percent);
		super.put(DATE, date);
		super.put(QTY_PAYMENTS, qtyPayments);
		super.put(CREDIT_TYPE, creditType);
		super.put(CALC_TYPE, calcType);
	}

}

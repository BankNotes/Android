package calculator.ru.activity;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;
import calculator.ru.R;

public class Result extends Activity {

	double sumCred;
	int period;
	double percent;
	String beginDate;
	double comisPayment = 0;

	boolean payType;
	public static final String ID = "id";
	public static final String ID_CALC = "id_calc";
	public static final String PAY_ID = "pay_id";
	public static final String PAY = "pay";
	public static final String PAY_PERCENT = "pay_percent";
	public static final String PAY_FEE = "pay_fee";
	public static final String PAY_DATE = "pay_date";

	public static final String REMAIN = "remain";
	GridView resultGrid;

	ArrayAdapter<String> adapter;
	private static final Uri PAYMENT_URI = Uri
			.parse("content://calculator.ru.paymentscontentprovider/payments");
	private static final Uri IDATA_URI = Uri
			.parse("content://calculator.ru.inputdatacontentprovider/input_data");

	private static final Uri TOTALS_URI = Uri
			.parse("content://calculator.ru.totaldatacontentprovider/totals");

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.result);

		String[] projectionInputData = { "id", "period", "percent", "payType" };
		Cursor inputData = getContentResolver().query(IDATA_URI,
				projectionInputData, null, null, null);

		inputData.moveToLast();

		String[] idCalc = { inputData.getInt(0) + "" };
		period = inputData.getInt(1);
		percent = inputData.getDouble(2);
		inputData.close();

		CharSequence[] header = getResources().getTextArray(
				R.array.headerResult);
		List<String> headerList = new ArrayList<String>();

		for (int i = 0; i < 5; i++) {
			headerList.add(header[i].toString());
		}
		GridView headGrid = (GridView) findViewById(R.id.result_header);
		headGrid.setAdapter(new ArrayAdapter<String>(this, R.layout.item,
				R.id.tvText, headerList));
		headGrid.setNumColumns(5);
		headGrid.setVerticalSpacing(2);
		headGrid.setHorizontalSpacing(2);
		headGrid.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
		headGrid.setClickable(false);

		List<String> resList = new ArrayList<String>();

		String[] projection = { PAY_ID, PAY_DATE, PAY, PAY_PERCENT, PAY_FEE,
				REMAIN };

		Cursor payments = getContentResolver().query(PAYMENT_URI, projection,
				"id_calc=?", idCalc, PAY_ID);

		if (payments != null) {

			payments.moveToFirst();

			do {

				resList.add(payments.getString(1));

				for (int n = 2; n <= 5; n++) {
					resList.add(convertToMoneyFormat(payments.getDouble(n)));
				}

			} while (payments.moveToNext());
			payments.close();

		}

		String[] projectionTotal = { "sumPays", "sumPercentPays", "sumPayFee" };
		String selectionTotal = "id_calc=?";

		Cursor total = getContentResolver().query(TOTALS_URI, projectionTotal,
				selectionTotal, idCalc, null);
		total.moveToLast();

		double payout = total.getDouble(0);
		double payoutPercent = total.getDouble(1);
		double payoutFee = total.getDouble(2);
		total.close();

		for (int i = 0; i < 5; i++) {
			resList.add(" ");
		}
		resList.add(getResources().getText(R.string.total).toString());

		resList.add(convertToMoneyFormat(payout));

		resList.add(convertToMoneyFormat(payoutPercent));

		resList.add(convertToMoneyFormat(payoutFee));
		
		resList.add(" ");

		GridView resGrid = (GridView) findViewById(R.id.result_grid);

		resGrid.setAdapter(new ArrayAdapter<String>(this, R.layout.item,
				R.id.tvText, resList));

		resGrid.setNumColumns(5);
		resGrid.setVerticalSpacing(2);
		resGrid.setHorizontalSpacing(2);


		resGrid.setClickable(false);

		TextView textHead = (TextView) findViewById(R.id.textResult);
		textHead.setText(getResources().getString(R.string.sumCred) + ": "
				+ convertToMoneyFormat(payoutFee) + "\n"
				+ getResources().getString(R.string.percent) + ": "
				+ NumberFormat.getInstance(Locale.getDefault()).format(percent)
				+ "%");

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.result, menu);
		return true;
	}

	private String convertToMoneyFormat(double number) {
		NumberFormat numFormat = NumberFormat.getIntegerInstance(Locale
				.getDefault());
		return numFormat.format(number);
	}
}

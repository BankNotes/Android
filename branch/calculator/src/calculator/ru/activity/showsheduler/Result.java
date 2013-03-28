package calculator.ru.activity.showsheduler;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;
import calculator.dbase.DataSource;
import calculator.dbase.InputData;
import calculator.ru.R;
import calculator.ru.activity.service.MoneyConvertor;

public class Result extends Activity {

	// double sumCred;
	// int period;
	// double percent;
	// String beginDate;
	// double comisPayment = 0;
	//
	// boolean payType;
	// public static final String ID = "id";
	// public static final String ID_CALC = "id_calc";
	// public static final String PAY_ID = "pay_id";
	// public static final String PAY = "pay";
	// public static final String PAY_PERCENT = "pay_percent";
	// public static final String PAY_FEE = "pay_fee";
	// public static final String PAY_DATE = "pay_date";
	//
	// public static final String REMAIN = "remain";
	DataSource dSource;


	ArrayAdapter<String> adapter;

	// private static final Uri PAYMENT_URI = Uri
	// .parse("content://calculator.dbase.paymentscontentprovider/payments");
	// private static final Uri IDATA_URI = Uri
	// .parse("content://calculator.dbase.inputdatacontentprovider/input_data");
	//
	// private static final Uri TOTALS_URI = Uri
	// .parse("content://calculator.dbase.totalscontentprovider/totals");

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.result);
		dSource = new DataSource(this);

		dSource.open();

		TextView textHead = (TextView) findViewById(R.id.textResult);
		InputData iData = dSource.getLastInputData();

		// textHead.setText(getResources().getString(R.string.sumCred) + ": "
		// + MoneyConvertor.convertToMoneyFormat(payoutFee) + "\n"
		// + getResources().getString(R.string.percent) + ": "
		// + NumberFormat.getInstance(Locale.getDefault()).format(percent)
		// + "%");
		textHead.setText(getResources().getString(R.string.name_calc)
				+ ": "
				+ iData.getName()
				+ "\n"
				+ getResources().getString(R.string.sumCred)
				+ ": "
				+ MoneyConvertor.convertToMoneyFormat(iData.getSum())
				+ "\n"
				+ getResources().getString(R.string.percent)
				+ ": "
				+ NumberFormat.getInstance(Locale.getDefault()).format(
						iData.getPercent()) + "\n"
						+ getResources().getString(R.string.period)+": "
						+ iData.getPeriod() + " "+getResources().getString(R.string.month)+"\n"
				+ getResources().getString(R.string.date_of_issue) + ": "
				+ iData.getBeginDate());

		
		// String[] projectionInputData = { "id", "period", "percent", "payType"
		// };
		// Cursor inputData = getContentResolver().query(IDATA_URI,
		// projectionInputData, null, null, null);

		// inputData.moveToLast();
		//
		// String[] idCalc = { inputData.getInt(0) + "" };
		// String[] idCalc = { dSource.getLastIdCalc() + "" };
		// period = inputData.getInt(1);
		// percent = inputData.getDouble(2);
		// inputData.close();
/*
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
*/
		List<String> resList = new ArrayList<String>();

		// test data
		ArrayList<RowOfSheduller> table = dSource.getShedullerTable();
		for (RowOfSheduller row : table) {
			resList.add(row.getPayDate());
			resList.add(MoneyConvertor.convertToMoneyFormat(row.getPayment()));
			resList.add(MoneyConvertor.convertToMoneyFormat(row.getPayPercent()));
			resList.add(MoneyConvertor.convertToMoneyFormat(row.getPayFee()));
			resList.add(MoneyConvertor.convertToMoneyFormat(row.getRemain()));
		}
		
//		resList.add("1");
//		resList.add("2");
//		resList.add("3");
//		resList.add("4");
//		resList.add("5");
		
	
		GridView resultGrid = (GridView) findViewById(R.id.result_header);
		resultGrid.setAdapter(new ArrayAdapter<String>(this, R.layout.item,
				R.id.tvText, resList));
		resultGrid.setNumColumns(5);
		resultGrid.setVerticalSpacing(2);
		resultGrid.setHorizontalSpacing(2);
		resultGrid.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
		resultGrid.setClickable(false);
	}
	
		// String[] projection = { PAY_ID, PAY_DATE, PAY, PAY_PERCENT, PAY_FEE,
		// REMAIN };
		//
		// lastInputData.get
		// Cursor payments = getContentResolver().query(PAYMENT_URI, projection,
		// "id_calc=?", idCalc, PAY_ID);
		//
		// if (payments != null) {
		//
		// payments.moveToFirst();
		//
		// do {
		//
		// resList.add(payments.getString(1));
		//
		// for (int n = 2; n <= 5; n++) {
		// resList.add(MoneyConvertor.convertToMoneyFormat(payments
		// .getDouble(n)));
		// }
		//
		// } while (payments.moveToNext());
		// payments.close();
/*
		ArrayList<RowOfSheduller> table = dSource.getShedullerTable();
		for (RowOfSheduller row : table) {
			resList.add(row.getPayDate());
			resList.add(MoneyConvertor.convertToMoneyFormat(row.getPayment()));
			resList.add(MoneyConvertor.convertToMoneyFormat(row.getPayPercent()));
			resList.add(MoneyConvertor.convertToMoneyFormat(row.getPayFee()));
			resList.add(MoneyConvertor.convertToMoneyFormat(row.getRemain()));
		}

		
		// String[] projectionTotal = { "sumPays", "sumPercentPays", "sumPayFee"
		// };
		// String selectionTotal = "id_calc=?";
		//
		// Cursor total = getContentResolver().query(TOTALS_URI,
		// projectionTotal,
		// selectionTotal, idCalc, null);
		// total.moveToLast();

		RowOfSheduller totalRow = dSource.getTotalsSheduler();
		// double payout = total.getDouble(0);
		double payout = totalRow.getPayment();
		// double payoutPercent = total.getDouble(1);
		double payoutPercent = totalRow.getPayPercent();
		// double payoutFee = total.getDouble(2);
		double payoutFee = totalRow.getPayFee();
		// total.close();

		for (int i = 0; i < 5; i++) {
			resList.add(" ");
		}
		resList.add(getResources().getText(R.string.total).toString());

		resList.add(MoneyConvertor.convertToMoneyFormat(payout));

		resList.add(MoneyConvertor.convertToMoneyFormat(payoutPercent));

		resList.add(MoneyConvertor.convertToMoneyFormat(payoutFee));

		resList.add(" ");

		resList.clear();
		resList.add("first column");
		resList.add("second column");
		resList.add("third column");
		resList.add("forth column");
		resList.add("fith column");
		
		GridView resGrid = (GridView) findViewById(R.id.result_grid);

//		resGrid.setAdapter(new ArrayAdapter<String>(this, R.layout.item,
//				R.id.tvText, resList));
//		resGrid.setAdapter(new ArrayAdapter<String>(this, R.layout.item,
//				R.id.tvText, headerList));
//		resGrid.setNumColumns(5);
//		resGrid.setVerticalSpacing(2);
//		resGrid.setHorizontalSpacing(2);
//		resGrid.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
//		resGrid.setClickable(false);

			}
*/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.result, menu);
		return true;
	}
}

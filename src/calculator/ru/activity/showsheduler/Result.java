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

	DataSource dSource;

	ArrayAdapter<String> adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.result);
		dSource = new DataSource(this);

		dSource.open();

		// header text
		TextView textHead = (TextView) findViewById(R.id.textResult);
		InputData iData = dSource.getLastInputData();

		String calcType = "";
		if (iData.getCalcType().equals(InputData.BY_PAY)) {
			calcType = getResources().getString(R.string.byAmmountOfPayment);
		}
		if (iData.getCalcType().equals(InputData.BY_PROFIT)) {
			calcType = getResources().getString(R.string.byAmmountIncome);
		}
		if (iData.getCalcType().equals(InputData.BY_SUM)) {
			calcType = getResources().getString(R.string.bySumOfLoan);
		}
		
		String paymentType = "";
		if (iData.getPaymentType() == 1) {
			paymentType = getResources().getString(R.string.anuitent);
		} else {
			paymentType = getResources().getString(R.string.different);
		}
		
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
				+ getResources().getString(R.string.period) + ": "
				+ iData.getPeriod() + " "
				+ getResources().getString(R.string.month).toLowerCase() + "\n"
				+ getResources().getString(R.string.date_of_issue) + ": "
				+ iData.getBeginDate() + "\n"
				+ getResources().getString(R.string.calc_type_tv) + ": "
				+ calcType.toLowerCase() + "\n"
				+ getResources().getString(R.string.payment_type) + ": "
				+ paymentType.toLowerCase());

		// table header
		int numberColumns=6;
		CharSequence[] header = getResources().getTextArray(
				R.array.headerResult);
		List<String> headerList = new ArrayList<String>();

		for (int i = 0; i < 6; i++) {
			headerList.add(header[i].toString());
		}
		GridView headGrid = (GridView) findViewById(R.id.result_header);
		headGrid.setAdapter(new ArrayAdapter<String>(this, R.layout.item,
				R.id.tvText, headerList));
		headGrid.setNumColumns(numberColumns);
		headGrid.setVerticalSpacing(2);
		headGrid.setHorizontalSpacing(2);
		headGrid.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
		headGrid.setClickable(false);

		// this table
		List<String> resList = new ArrayList<String>();
		
		ArrayList<RowOfSheduller> table = dSource.getShedullerTable();
		for (RowOfSheduller row : table) {
			resList.add(row.getPayId() + "");
			resList.add(row.getPayDate());
			resList.add(MoneyConvertor.convertToMoneyFormat(row.getPayment()));
			resList.add(MoneyConvertor.convertToMoneyFormat(row.getPayPercent()));
			resList.add(MoneyConvertor.convertToMoneyFormat(row.getPayFee()));
			resList.add(MoneyConvertor.convertToMoneyFormat(row.getRemain()));
		}

		// adding spase
		for(int i=0; i<numberColumns; i++){
			resList.add("");
		}

		//totals row
		RowOfSheduller totalRow = dSource.getTotalsSheduler();
		resList.add(getResources().getString(R.string.total));
		resList.add("");
		resList.add(MoneyConvertor.convertToMoneyFormat( totalRow.getPayment()));
		resList.add(MoneyConvertor.convertToMoneyFormat(totalRow.getPayPercent()));
		resList.add(MoneyConvertor.convertToMoneyFormat(totalRow.getPayFee()));
		resList.add("");

		GridView resultGrid = (GridView) findViewById(R.id.result_grid);
		resultGrid.setAdapter(new ArrayAdapter<String>(this, R.layout.item,
				R.id.tvText, resList));
		resultGrid.setNumColumns(numberColumns);
		resultGrid.setVerticalSpacing(2);
		resultGrid.setHorizontalSpacing(2);
		resultGrid.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
		resultGrid.setClickable(false);
		
		
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.result, menu);
		return true;
	}
	
	public void onDestroy() {
		dSource.close();
		super.onDestroy();

	}
}

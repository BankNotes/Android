package calculator.ru.activity;

import java.util.ArrayList;

import android.app.ListActivity;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import calculator.ru.R;

public class ListOfCalcs extends ListActivity {

	private static final Uri LIST_URI = Uri
			.parse("content://calculator.ru.listofloansdataprovider/list_of_loan");

	private GridView listGrid;

	private CharSequence[] header;
	private int columns, width;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_of_calcs);
		/*
		 * this.header =
		 * getResources().getTextArray(R.array.header_list_of_calcs);
		 * List<String> headerList = new ArrayList<String>();
		 * 
		 * for (int i = 0; i < header.length; i++) {
		 * headerList.add(header[i].toString()); } GridView headGrid =
		 * (GridView) findViewById(R.id.list_header_grid);
		 * headGrid.setAdapter(new ArrayAdapter<String>(this, R.layout.item,
		 * R.id.tvText, headerList));
		 * 
		 * this.columns = header.length;
		 * 
		 * headGrid.setNumColumns(columns); Display display = ((WindowManager)
		 * getSystemService(WINDOW_SERVICE)) .getDefaultDisplay(); this.width =
		 * display.getWidth();
		 * 
		 * headGrid.setColumnWidth(width / columns);
		 * headGrid.setVerticalSpacing(5); headGrid.setHorizontalSpacing(5);
		 * headGrid.setStretchMode(GridView.NO_STRETCH);
		 * headGrid.setClickable(false);
		 * 
		 * TableLayout table = (TableLayout) findViewById(R.id.control_panel);
		 * table.setStretchAllColumns(true);
		 * 
		 * updateTable();
		 */
		ArrayList<ResCalcItem> list = new ArrayList<ResCalcItem>();
		list.add(new ResCalcItem("1", "1000000", "12", "12.01.2012", "50",
				"Annuitet", "By sum of loan"));
		list.add(new ResCalcItem("2", "1000000", "12", "12.01.2012", "50",
				"Annuitet", "By sum of loan"));
		list.add(new ResCalcItem("3", "1000000", "12", "12.01.2012", "50",
				"Annuitet", "By sum of loan"));
		list.add(new ResCalcItem("4", "1000000", "12", "12.01.2012", "50",
				"Annuitet", "By sum of loan"));
		list.add(new ResCalcItem("5", "1000000", "12", "12.01.2012", "50",
				"Annuitet", "By sum of loan"));
		list.add(new ResCalcItem("6", "1000000", "12", "12.01.2012", "50",
				"Annuitet", "By sum of loan"));
		list.add(new ResCalcItem("7", "1000000", "12", "12.01.2012", "50",
				"Annuitet", "By sum of loan"));
		list.add(new ResCalcItem("1", "1000000", "12", "12.01.2012", "50",
				"Annuitet", "By sum of loan"));
		list.add(new ResCalcItem("1", "1000000", "12", "12.01.2012", "50",
				"Annuitet", "By sum of loan"));
		list.add(new ResCalcItem("1", "1000000", "12", "12.01.2012", "50",
				"Annuitet", "By sum of loan"));
		list.add(new ResCalcItem("1", "1000000", "12", "12.01.2012", "50",
				"Annuitet", "By sum of loan"));
		list.add(new ResCalcItem("1", "1000000", "12", "12.01.2012", "50",
				"Annuitet", "By sum of loan"));

		ListAdapter adapter = new SimpleAdapter(this, list,
				R.layout.item_of_list, 
				new String[] { 
								ResCalcItem.SUM,
								ResCalcItem.PERCENT,
								ResCalcItem.DATE,
								ResCalcItem.QTY_PAYMENTS,
								ResCalcItem.CREDIT_TYPE,
								ResCalcItem.CALC_TYPE
							 }, 
				new int[] { 
								R.id.sum,
								R.id.percent,
								R.id.date_of_loan,
								R.id.period,
								R.id.credit_type,
								R.id.calc_type
							});
		setListAdapter(adapter);
	}

	public void refresh(View v) {
		if (v.getId() == R.id.refreshButton) {
			update();

		}
	}

	public void delete(View v) {
		if (v.getId() == R.id.delete) {

		}
	}

	private void update() {
		/*
		 * List<String> resList = new ArrayList<String>();
		 * 
		 * this.listGrid = (GridView) findViewById(R.id.list_grid); String[]
		 * projectionInputData = { "sum_of_loan", "percent", "date_of_loan",
		 * "qty_payments", "credit_type", "calc_type" }; Cursor calculations =
		 * getContentResolver().query(LIST_URI, projectionInputData, null, null,
		 * "id");
		 * 
		 * if (calculations.moveToFirst()) { calculations.moveToFirst(); do {
		 * resList.add(convertToMoneyFormat(calculations.getDouble(0)));
		 * resList.add(calculations.getDouble(1) + "");
		 * resList.add(calculations.getString(2));
		 * resList.add(calculations.getInt(3) + "");
		 * resList.add(calculations.getString(4));
		 * resList.add(calculations.getString(5)); } while
		 * (calculations.moveToNext()); } else { for (int i = 0; i <
		 * header.length; i++) { resList.add(" "); }
		 * 
		 * } calculations.close();
		 * 
		 * listGrid.setAdapter(new ArrayAdapter<String>(this, R.layout.item,
		 * R.id.tvText, resList)); listGrid.setNumColumns(columns);
		 * listGrid.setColumnWidth(width / columns);
		 * listGrid.setVerticalSpacing(5); listGrid.setHorizontalSpacing(5);
		 * listGrid.setStretchMode(GridView.NO_STRETCH); }
		 * 
		 * private String convertToMoneyFormat(double number) { NumberFormat
		 * numFormat = NumberFormat.getIntegerInstance(Locale .getDefault());
		 * return numFormat.format(number);
		 */

	}
}

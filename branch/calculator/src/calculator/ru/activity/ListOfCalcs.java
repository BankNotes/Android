package calculator.ru.activity;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import calculator.ru.R;

public class ListOfCalcs extends ListActivity {

	private static final Uri LIST_URI = Uri
			.parse("content://calculator.dbase.listofloancontentprovider/list_of_loan");

	private GridView listGrid;

	private CharSequence[] header;
	private int columns, width;
	private ContentResolver cResolver;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_of_calcs);
		this.cResolver = getContentResolver();

		String[] projection = { "id_calc", "sum_of_loan", "percent",
				"begin_date", "end_date", "qty_payments", "credit_type",
				"calc_type" };
		String selection = null;
		String[] selectionArgs = null;
		String sortOrder = "id_calc";
		
		Cursor cursor = cResolver.query(LIST_URI, projection, selection,
				selectionArgs, sortOrder);
		cursor.moveToFirst();
		
		
		ArrayList<ResCalcItem> list = new ArrayList<ResCalcItem>();
		while(cursor.moveToNext()){
			list.add(new ResCalcItem(cursor.getInt(0)+"", 
					cursor.getDouble(1)+"", cursor.getDouble(2)+"", cursor.getString(3), 
					cursor.getString(4), cursor.getInt(5)+"", cursor.getString(6), cursor.getString(7)));
		}
		
//		list.add(new ResCalcItem("1", "1000000", "12", "12.01.2012",
//				"14.12.2015", "50", "Annuitet", "By sum of loan"));

		ListAdapter adapter = new SimpleAdapter(this, list,
				R.layout.item_of_list, new String[] { ResCalcItem.SUM,
						ResCalcItem.PERCENT, ResCalcItem.BEGIN_DATE,
						ResCalcItem.END_DATE, ResCalcItem.QTY_PAYMENTS,
						ResCalcItem.CREDIT_TYPE, ResCalcItem.CALC_TYPE },
				new int[] { R.id.sum, R.id.percent, R.id.begin_date,
						R.id.end_date, R.id.period, R.id.credit_type,
						R.id.calc_type });
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

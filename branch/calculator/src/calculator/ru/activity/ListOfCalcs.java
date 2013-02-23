package calculator.ru.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;
import calculator.ru.R;
import calculator.ru.activity.item.ItemOfCalc;

public class ListOfCalcs extends Activity implements OnItemSelectedListener {

	final int ID = 0;
	final int SUM_OF_LOAN = 1;
	final int PERCENT = 2;
	final int BEGIN_DATE = 3;
	final int END_DATE = 4;
	final int PERIOD = 5;
	final int CREDIT_TYPE = 6;
	final int CALC_TYPE = 7;

	private static final Uri LIST_URI = Uri
			.parse("content://calculator.dbase.listofloancontentprovider/list_of_loan");
	private static final Uri PAYS_URI = Uri
			.parse("content://calculator.dbase.paymentscontentprovider/payments");

	private ContentResolver cResolver;
	private String[] projection = { "id_calc", "sum_of_loan", "percent",
			"begin_date", "end_date", "qty_payments", "credit_type",
			"calc_type" };
	private String selection = null;
	private String[] selectionArgs = null;
	private String sortOrder = "id_calc";

	private ArrayList<ItemOfCalc> arrayList = new ArrayList<ItemOfCalc>();
	MyListAdapter myAdapter;
	ListView lView;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_of_calcs);
		this.cResolver = getContentResolver();
		// this.arrayList = new ArrayList<ResCalcItem>();

		// arrayList.add(new ItemOfCalc(0, 3, 20000.0, 20.0, "12.09.2012",
		// "12.12.2012", "Annu", "By profit", false));
		updateList();

	}

	public void refresh(View v) {
		if (v.getId() == R.id.refreshButton) {
			updateList();
		}
	}

	public void delete(View v) {
		if (v.getId() == R.id.delete) {

			ArrayList<ItemOfCalc> selectedItems = myAdapter.getSelectedItems();
			
			ArrayList<String> ids = new ArrayList<String>();
			for (ItemOfCalc i : selectedItems) {
				ids.add(i.getId()+"");
			}
			
			String[] selectionArgs =(String[]) ids.toArray();
			cResolver.delete(LIST_URI, "id_calc=?", selectionArgs);
			cResolver.delete(PAYS_URI, "id_calc=?", selectionArgs);
		}
	}

	private void updateList() {
		Cursor cursor = cResolver.query(LIST_URI, projection, selection,
				selectionArgs, sortOrder);
		if (cursor != null) {
			cursor.moveToFirst();
			arrayList.clear();
			while (cursor.moveToNext()) {
				arrayList.add(new ItemOfCalc(cursor.getInt(ID), cursor
						.getInt(PERIOD), cursor.getDouble(SUM_OF_LOAN), cursor
						.getDouble(PERCENT), cursor.getString(BEGIN_DATE),
						cursor.getString(END_DATE), cursor
								.getString(CREDIT_TYPE), cursor
								.getString(CALC_TYPE), false));
			}
		}
		cursor.close();
		myAdapter = new MyListAdapter(this, arrayList);

		lView = (ListView) findViewById(R.id.my_list);
		lView.setAdapter(myAdapter);
		/*
		 * while (cursor.moveToNext()) { arrayList.add(new
		 * ResCalcItem(cursor.getInt(0) + "", MoneyConvertor
		 * .convertToMoneyFormat(cursor.getDouble(1)), cursor .getDouble(2) +
		 * "", cursor.getString(3), cursor .getString(4), cursor.getInt(5) + "",
		 * cursor.getString(6), cursor.getString(7))); } cursor.close(); adapter
		 * = new SimpleAdapter(this, arrayList, R.layout.item_of_list, new
		 * String[] { ResCalcItem.SUM, ResCalcItem.PERCENT,
		 * ResCalcItem.BEGIN_DATE, ResCalcItem.END_DATE,
		 * ResCalcItem.QTY_PAYMENTS, ResCalcItem.CREDIT_TYPE,
		 * ResCalcItem.CALC_TYPE }, new int[] { R.id.sum, R.id.percent,
		 * R.id.begin_date, R.id.end_date, R.id.period, R.id.credit_type,
		 * R.id.calc_type }); setListAdapter(adapter);
		 */
	}

	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {

	}

	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}
}

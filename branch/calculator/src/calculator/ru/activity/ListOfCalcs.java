package calculator.ru.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ListActivity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import calculator.ru.R;

public class ListOfCalcs extends ListActivity {

	private static final Uri LIST_URI = Uri
			.parse("content://calculator.dbase.listofloancontentprovider/list_of_loan");

	private ContentResolver cResolver;
	private String[] projection = { "id_calc", "sum_of_loan", "percent",
			"begin_date", "end_date", "qty_payments", "credit_type",
			"calc_type" };
	private ListAdapter adapter;
	private String selection = null;
	private String[] selectionArgs = null;
	private String sortOrder = "id_calc";
	private ArrayList<ResCalcItem> list;
	//private HashMap<Integer, Boolean> itemsOfList = new HashMap<Integer, Boolean>();

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_of_calcs);
		this.cResolver = getContentResolver();
		this.list = new ArrayList<ResCalcItem>();

		updateList();

	}

	public void refresh(View v) {
		if (v.getId() == R.id.refreshButton) {
			updateList();
		}
	}

	public void delete(View v) {
		if (v.getId() == R.id.delete) {

		}
	}

	private void updateList() {
		Cursor cursor = cResolver.query(LIST_URI, projection, selection,
				selectionArgs, sortOrder);
		cursor.moveToFirst();
		list.clear();
		while (cursor.moveToNext()) {
			list.add(new ResCalcItem(cursor.getInt(0) + "", MoneyConvertor
					.convertToMoneyFormat(cursor.getDouble(1)), cursor
					.getDouble(2) + "", cursor.getString(3), cursor
					.getString(4), cursor.getInt(5) + "", cursor.getString(6),
					cursor.getString(7)));
		}
		cursor.close();
		adapter = new SimpleAdapter(this, list, R.layout.item_of_list,
				new String[] { ResCalcItem.SUM, ResCalcItem.PERCENT,
						ResCalcItem.BEGIN_DATE, ResCalcItem.END_DATE,
						ResCalcItem.QTY_PAYMENTS, ResCalcItem.CREDIT_TYPE,
						ResCalcItem.CALC_TYPE }, new int[] { R.id.sum,
						R.id.percent, R.id.begin_date, R.id.end_date,
						R.id.period, R.id.credit_type, R.id.calc_type });
		setListAdapter(adapter);

	}
}

package calculator.ru.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import calculator.ru.R;
import calculator.ru.activity.item.ItemOfCalc;

public class ListOfCalcs extends Activity {

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
		
		updateList();

		Button refreshButton = (Button) findViewById(R.id.refreshButton);
		refreshButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				updateList();
			}
		});

		Button deleteButton = (Button) findViewById(R.id.delete_item_button);
		deleteButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				deleteItem();
			}
		});
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
		
	}

	private void deleteItem() {
		ArrayList<ItemOfCalc> selectedItems = myAdapter.getSelectedItems();

		ArrayList<String> ids = new ArrayList<String>();
		for (ItemOfCalc i : selectedItems) {
			ids.add(i.getId() + "");
		}

		String[] selectionArgs = ids.toArray(new String[ids.size()]);
		cResolver.delete(LIST_URI, "id_calc=?", selectionArgs);
		cResolver.delete(PAYS_URI, "id_calc=?", selectionArgs);
		updateList();

	}

}

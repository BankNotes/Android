package calculator.ru.activity.allcomputs;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import calculator.dbase.DataSource;
import calculator.ru.R;

public class ListOfCalcs extends Activity {

	private DataSource dSource;

	private ArrayList<ItemOfCalc> arrayListOfCalcs = new ArrayList<ItemOfCalc>();;

	MyListAdapter myAdapter;
	ListView lView;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_of_calcs);

		dSource = new DataSource(this);
		dSource.open();

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
		arrayListOfCalcs.clear();

		for (ItemOfCalc item : dSource.getListOfCalculations()) {
			arrayListOfCalcs.add(item);
		}

		myAdapter = new MyListAdapter(this, arrayListOfCalcs);
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
		dSource.deleteSelectedItemsOfList(selectionArgs);

		updateList();

	}

}

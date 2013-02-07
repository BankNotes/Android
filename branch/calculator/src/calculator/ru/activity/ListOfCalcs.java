package calculator.ru.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import calculator.ru.R;

public class ListOfCalcs extends Activity {

	private static final Uri IDATA_URI = Uri
			.parse("content://calculator.ru.inputdatacontentprovider/input_data");

	ArrayAdapter<String> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_of_calcs);

		CharSequence[] header = getResources().getTextArray(
				R.array.header_list_of_calcs);
		List<String> headerList = new ArrayList<String>();

		for (int i = 0; i < header.length; i++) {
			headerList.add(header[i].toString());
		}
		GridView headGrid = (GridView) findViewById(R.id.list_header_grid);
		headGrid.setAdapter(new ArrayAdapter<String>(this, R.layout.item,
				R.id.tvText, headerList));
		headGrid.setNumColumns(header.length);
		headGrid.setClickable(false);

		
		String[] projectionInputData = { "inputSum", "percent", "beginDate",
				"period", "payType" };
		Cursor calculations = getContentResolver().query(IDATA_URI,
				projectionInputData, null, null, "id");

		List<String> resList = new ArrayList<String>();

		if (calculations != null) {
			calculations.moveToFirst();
			do {
				resList.add(calculations.getDouble(0)+"");
				resList.add(calculations.getDouble(1)+"");
				resList.add(calculations.getString(2));
				resList.add(calculations.getInt(3)+"");
				resList.add(calculations.getInt(4)+"");

			} while (calculations.moveToNext());
		}
		calculations.close();
		
		GridView listGrid = (GridView) findViewById(R.id.list_grid);
		listGrid.setAdapter(new ArrayAdapter<String>(this, R.layout.item,
				R.id.tvText, resList));
		listGrid.setNumColumns(header.length);
		
		
	}
}

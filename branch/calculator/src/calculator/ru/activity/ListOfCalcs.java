package calculator.ru.activity;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import calculator.ru.R;

public class ListOfCalcs extends Activity {

	private static final Uri IDATA_URI = Uri
			.parse("content://calculator.ru.inputdatacontentprovider/input_data");
	
	private GridView listGrid;
	private List<String> resList;
	private CharSequence[] header;
	private int columns, width;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_of_calcs);

		this.header = getResources().getTextArray(
				R.array.header_list_of_calcs);
		List<String> headerList = new ArrayList<String>();

		for (int i = 0; i < header.length; i++) {
			headerList.add(header[i].toString());
		}
		GridView headGrid = (GridView) findViewById(R.id.list_header_grid);
		headGrid.setAdapter(new ArrayAdapter<String>(this, R.layout.item,
				R.id.tvText, headerList));

		
		this.columns = header.length;
		
		headGrid.setNumColumns(columns);
		Display display = ((WindowManager) getSystemService(WINDOW_SERVICE))
				.getDefaultDisplay();
		this.width = display.getWidth();
		
		
		headGrid.setColumnWidth(width / columns);
		headGrid.setVerticalSpacing(5);
		headGrid.setHorizontalSpacing(5);
		headGrid.setStretchMode(GridView.NO_STRETCH);
		headGrid.setClickable(false);

		this.resList = new ArrayList<String>();

		this.listGrid = (GridView) findViewById(R.id.list_grid);
		
		updateTable();
		
		

	}

	public void refresh(View v) {
		if(v.getId() == R.id.refreshButton) {
			updateTable();
			
			listGrid.setAdapter(new ArrayAdapter<String>(this, R.layout.item,
					R.id.tvText, resList));
			listGrid.setNumColumns(columns);
			listGrid.setColumnWidth(width / columns);
			listGrid.setVerticalSpacing(5);
			listGrid.setHorizontalSpacing(5);
			listGrid.setStretchMode(GridView.NO_STRETCH);
		}
	}
	
	private void updateTable(){
		String[] projectionInputData = { "inputSum", "percent", "beginDate",
				"period", "payType" };
		Cursor calculations = getContentResolver().query(IDATA_URI,
				projectionInputData, null, null, "id");
		

		if (calculations.moveToFirst()) {
			calculations.moveToFirst();
			do {
				resList.add(convertToMoneyFormat(calculations.getDouble(0)));
				resList.add(calculations.getDouble(1) + "");
				resList.add(calculations.getString(2));
				resList.add(calculations.getInt(3) + "");
				resList.add(calculations.getInt(4) + "");

			} while (calculations.moveToNext());
		} else {
			for (int i = 0; i < header.length; i++) {
				resList.add(" ");
			}

		}
		calculations.close();
	}
	
	private String convertToMoneyFormat(double number) {
		NumberFormat numFormat = NumberFormat.getIntegerInstance(Locale
				.getDefault());
		return numFormat.format(number);
	}
}

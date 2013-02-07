package calculator.ru.activity.comput;

import java.util.Calendar;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.InputFilter;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import calculator.ru.R;
import calculator.ru.activity.PriceInputFilter;
import calculator.ru.activity.Result;
import calculator.ru.payments.Payments;

public class CalcBySumLoan extends FragmentActivity {

	public final static String PERCENT = "percent";
	public final static String BEGIN_DATE = "beginDate";
	public final static String PERIOD = "period";
	public final static String PAYTYPE = "paytype";

	private int year;
	private int mth;
	private int day;

	static final int DATE_DIALOG_ID = 0;
	private Button dateButton;
	private RadioButton r1;
	private RadioButton r2;

	private EditText editTextSumCred;
	private EditText editTextPercent;
	private EditText editTextPeriod;
	private TextView textDatePayed;

	private static final Uri IDATA_URI = Uri
			.parse("content://calculator.ru.inputdatacontentprovider/input_data");

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.calc_by_sum_cred);

		editTextSumCred = (EditText) findViewById(R.id.sumCred);
		editTextSumCred
				.setFilters(new InputFilter[] { new PriceInputFilter() });
		editTextSumCred.getParent();

		editTextPercent = (EditText) findViewById(R.id.percent);
		
		editTextPeriod = (EditText) findViewById(R.id.period);

		textDatePayed = (TextView) findViewById(R.id.dateField);
		
		dateButton = (Button) findViewById(R.id.dateButton);

		Display myDisplay = getWindowManager().getDefaultDisplay();
		int textSize = myDisplay.getWidth() / 20;

		Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		mth = c.get(Calendar.MONTH) + 1;
		day = c.get(Calendar.DAY_OF_MONTH);

		updateDate();
		textDatePayed.setTextSize(textSize);

		dateButton = (Button) findViewById(R.id.dateButton);
		
		r1 = (RadioButton) findViewById(R.id.anuitent);
		r2 = (RadioButton) findViewById(R.id.different);
		r1.setChecked(true);
		r2.setChecked(false);

		//addListenerOnButton();

	}

	public void getDate(View v) {
	
//		Toast t = Toast.makeText(getApplicationContext(), "fuck", 100);
//		t.setGravity(Gravity.CENTER, 0, 0);
//		t.show();
//		DialogFragment dFragment = new DatePickerFragment();
//		dFragment.show(getSupportFragmentManager(), "datePicker");
	}
	
	
//	public void addListenerOnButton() {
//
//		dateButton = (Button) findViewById(R.id.dateButton);
//
//		dateButton.setKeyListener(new () {
//
//			public void onClick(View v) {
//
//				//showDialog(DATE_DIALOG_ID);
//				DialogFragment dFragment = new DatePickerFragment();
//				dFragment.show(getSupportFragmentManager(), "datePicker");
//			}
//
//		});
//
//	}
//	
/*
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			// set date picker as current date
			return new DatePickerDialog(this, datePickerListener, year, mth,
					day);
		}
		return null;
	}
*/
	/*
	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

		// when dialog box is closed, below method will be called.
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {
			year = selectedYear;
			mth = selectedMonth;
			day = selectedDay;

			// set selected date into textview
			updateDate();

		}
	};
*/
	private void updateDate() {
		textDatePayed.setText(pad(day) + "." + pad(mth + 1) + "." + year);
	}

	private String pad(int s) {
		if (s < 10)
			return "0" + s;
		else
			return "" + s;
	}

	private void showMessage(String message, String verifyedExpression) {
		if (verifyedExpression.equals("")) {
			Context context = getApplicationContext();
			Toast mesCred = Toast
					.makeText(context, message, Toast.LENGTH_SHORT);
			mesCred.setGravity(Gravity.CENTER, 0, 0);
			mesCred.show();

			return;
		}
	}

	public void calculate(View v) {
		if (v.getId() == R.id.calculateButton) {
			Intent intent = new Intent(this, Result.class);

			int paytype = 0;

			if (r1.isChecked())
				paytype = 0;
			if (r2.isChecked())
				paytype = 1;

			if (editTextSumCred.getText().toString().equals("")) {
				showMessage(getResources().getText(R.string.errMesSumCred)
						.toString(), editTextSumCred.getText().toString());
				return;
			}

			if (editTextPeriod.getText().toString().equals("")) {
				showMessage(getResources().getText(R.string.errMesNumPays)
						.toString(), editTextPeriod.getText().toString());
				return;
			}

			if (editTextPercent.getText().toString().equals("")) {
				showMessage(getResources().getText(R.string.errMesPercent)
						.toString(), editTextPercent.getText().toString());
				return;
			}

			double inputSum = Double.parseDouble(editTextSumCred.getText()
					.toString());
			double percent = Double.parseDouble(editTextPercent.getText()
					.toString());
			int period = Integer.parseInt(editTextPeriod.getText().toString());
			String beginDate = textDatePayed.getText().toString();

			ContentValues values = new ContentValues();

			values.put("inputSum", inputSum);
			values.put("percent", percent);
			values.put("period", period);
			values.put("payType", paytype);
			values.put("beginDate", beginDate);

			getContentResolver().insert(IDATA_URI, values);

			Cursor c = getContentResolver().query(IDATA_URI, null, null, null,
					null);
			c.moveToLast();
			int idCalc = c.getInt(0);
			c.close();

			Payments p = new Payments(inputSum, percent, period, beginDate,
					paytype, idCalc, getContentResolver());
			p.calculate();

			Context context = getApplicationContext();
			Toast mesCred = Toast.makeText(context,
					getResources().getText(R.string.mesCalculateOK).toString(),
					Toast.LENGTH_SHORT);
			mesCred.setGravity(Gravity.CENTER, 0, 0);
			mesCred.show();

			editTextPercent.setText("");
			editTextPeriod.setText("");
			editTextSumCred.setText("");
			startActivity(intent);

		}
	}

	
}

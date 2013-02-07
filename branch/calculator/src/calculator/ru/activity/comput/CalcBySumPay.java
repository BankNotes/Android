package calculator.ru.activity.comput;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import calculator.ru.R;
import calculator.ru.activity.PriceInputFilter;
import calculator.ru.activity.Result;
import calculator.ru.payments.Payments;

public class CalcBySumPay extends Activity {

	private int myYear;
	private int mth;
	private int day;

	static final int DATE_DIALOG_ID = 0;
	private RadioButton r1;
	private RadioButton r2;

	private EditText editTextSumPay;
	private EditText editTextPercent;
	private EditText editTextPeriod;
	private TextView textDatePayed;

	private static final Uri IDATA_URI = Uri
			.parse("content://calculator.ru.inputdatacontentprovider/input_data");

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.calc_by_sum_pay);

		editTextSumPay = (EditText) findViewById(R.id.sumPay);
		editTextSumPay.setFilters(new InputFilter[] { new PriceInputFilter() });
		editTextSumPay.getParent();

		editTextPercent = (EditText) findViewById(R.id.percent);
		
		editTextPeriod = (EditText) findViewById(R.id.period);

		textDatePayed = (TextView) findViewById(R.id.dateField);
		Display myDisplay = getWindowManager().getDefaultDisplay();
		int textFieldWidth = myDisplay.getWidth() / 20;
		textDatePayed.setTextSize(textFieldWidth);

		Calendar c = Calendar.getInstance();
		myYear = c.get(Calendar.YEAR);
		mth = c.get(Calendar.MONTH) + 1;
		day = c.get(Calendar.DAY_OF_MONTH);
		textDatePayed.setText(pad(day) + "." + pad(mth) + "." + myYear);

		r1 = (RadioButton) findViewById(R.id.anuitent);
		r2 = (RadioButton) findViewById(R.id.different);
		r1.setChecked(true);
		r2.setChecked(false);

	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, datePickerListener, myYear, mth,
					day);
		}
		return null;
	}

	public void getDate(View v) {
		if (v.getId() == R.id.dateButton) {
			showDialog(DATE_DIALOG_ID);
			updateDate();
		}

	}

	public void calculate(View v) {
		if (v.getId() == R.id.calculateButton) {
			Intent intent = new Intent(this, Result.class);

			int paytype = 0; // anuitent, else 0 - different

			if (r1.isChecked())
				paytype = 0;
			if (r2.isChecked())
				paytype = 1;

			if (editTextSumPay.getText().toString().equals("")) {
				showMessage(getResources().getText(R.string.errMesSumPay)
						.toString(), editTextSumPay.getText().toString());
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

			double inputSum = Double.parseDouble(editTextSumPay.getText()
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

			// ---
			Cursor c = getContentResolver().query(IDATA_URI, null, null, null,
					null);
			c.moveToLast();
			int idCalc = c.getInt(0);
			c.close();

			Payments p = new Payments(inputSum, paytype, percent, beginDate,
					period, idCalc, getContentResolver());
			p.calculate();

			Context context = getApplicationContext();
			Toast mesCred = Toast.makeText(context,
					getResources().getText(R.string.mesCalculateOK).toString(),
					Toast.LENGTH_SHORT);
			mesCred.setGravity(Gravity.CENTER, 0, 0);
			mesCred.show();

			editTextPercent.setText("");
			editTextPeriod.setText("");
			editTextSumPay.setText("");

			startActivity(intent);
		}
	}

	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {

			myYear = year;
			mth = monthOfYear;
			day = dayOfMonth;
			updateDate();
		}
	};

	private void updateDate() {
		textDatePayed.setText(pad(day) + "." + pad(mth + 1) + "." + myYear);
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

}
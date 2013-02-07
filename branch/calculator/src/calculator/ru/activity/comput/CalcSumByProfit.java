package calculator.ru.activity.comput;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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

public class CalcSumByProfit extends Activity {

	private int myYear;
	private int mth;
	private int day;
	private TextView dateTextView;
	static final int DATE_DIALOG_ID = 0;
	private RadioButton r1;
	private RadioButton r2;

	EditText editTextIncomAmmount;
	EditText editTextPercent;
	EditText editTextPeriod;
	TextView textDatePayed;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.calc_by_profit);

		editTextIncomAmmount = (EditText) findViewById(R.id.sumProfit);
		editTextIncomAmmount
				.setFilters(new InputFilter[] { new PriceInputFilter() });
		editTextIncomAmmount.getParent();

		editTextPercent = (EditText) findViewById(R.id.percent);


		editTextPeriod = (EditText) findViewById(R.id.period);

		textDatePayed = (TextView) findViewById(R.id.dateField);
		Display myDisplay = getWindowManager().getDefaultDisplay();
		int textFieldWidth = myDisplay.getWidth() / 20;
		textDatePayed.setTextSize(textFieldWidth);

		dateTextView = (TextView) findViewById(R.id.dateField);
		Calendar c = Calendar.getInstance();
		myYear = c.get(Calendar.YEAR);
		mth = c.get(Calendar.MONTH) + 1;
		day = c.get(Calendar.DAY_OF_MONTH);
		dateTextView.setText(pad(day) + "." + pad(mth) + "." + myYear);

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

			if (editTextIncomAmmount.getText().toString().equals("")) {
				showMessage(getResources().getText(R.string.errMesSumIncom)
						.toString(), editTextIncomAmmount.getText().toString());
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

			double inputSum = Double.parseDouble(editTextIncomAmmount.getText()
					.toString());
			double percent = Double.parseDouble(editTextPercent.getText()
					.toString());
			int period = Integer.parseInt(editTextPeriod.getText().toString());
			String beginDate = textDatePayed.getText().toString();

			Payments p = new Payments(inputSum, percent, period, beginDate,
					paytype, getContentResolver());
			p.calculate();

			Context context = getApplicationContext();
			Toast mesCred = Toast.makeText(context,
					getResources().getText(R.string.mesCalculateOK).toString(),
					Toast.LENGTH_SHORT);
			mesCred.setGravity(Gravity.CENTER, 0, 0);
			mesCred.show();

			editTextIncomAmmount.setText("");
			editTextPercent.setText("");
			editTextPeriod.setText("");
			startActivity(intent);
		}
	}

	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			// TODO Auto-generated method stub
			myYear = year;
			mth = monthOfYear;
			day = dayOfMonth;
			updateDate();
		}
	};

	private void updateDate() {
		dateTextView.setText(pad(day) + "." + pad(mth + 1) + "." + myYear);
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
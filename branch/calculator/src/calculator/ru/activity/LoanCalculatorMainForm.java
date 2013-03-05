package calculator.ru.activity;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import calculator.ru.R;
import calculator.ru.activity.comput.LoanComputer;

public class LoanCalculatorMainForm extends FragmentActivity implements
		AdapterView.OnItemSelectedListener {

	public final static String PERCENT = "percent";
	public final static String BEGIN_DATE = "beginDate";
	public final static String PERIOD = "period";
	public final static String PAYTYPE = "paytype";

	private int year;
	private int mth;
	private int day;

	private RadioButton r1;
	private RadioButton r2;

	private EditText editTextInputSum;
	private EditText editTextPercent;
	private EditText editTextPeriod;
	private static TextView textDatePayed;
	private CharSequence[] loanTypes, calcTypes;

	private int calcType = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calc_form);

		calcTypes = getResources().getStringArray(R.array.calcTypes);

		loanTypes = getResources().getTextArray(R.array.loanType);

		final Spinner spin = (Spinner) findViewById(R.id.spinner_calc_type);
		spin.setOnItemSelectedListener(this);
		ArrayAdapter<CharSequence> arrAdapter = new ArrayAdapter<CharSequence>(
				this, android.R.layout.simple_expandable_list_item_1, loanTypes);
		arrAdapter
				.setDropDownViewResource(android.R.layout.simple_expandable_list_item_1);
		spin.setAdapter(arrAdapter);

		editTextInputSum = (EditText) findViewById(R.id.inputSum);
		editTextInputSum.setHint(calcTypes[0]);
		editTextPeriod = (EditText) findViewById(R.id.period);
		editTextPercent = (EditText) findViewById(R.id.percent);
		textDatePayed = (TextView) findViewById(R.id.dateField);

		Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		mth = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);
		textDatePayed.setText(pad(day) + "." + pad(mth + 1) + "." + year);

		r1 = (RadioButton) findViewById(R.id.anuitent);
		r2 = (RadioButton) findViewById(R.id.different);
		r1.setChecked(true);
		r2.setChecked(false);

	}

	public void onItemSelected(AdapterView<?> parent, View v, int position,
			long id) {

		calcType = position;
		editTextInputSum.setHint(calcTypes[position]);
	}

	public void onNothingSelected(AdapterView<?> arg0) {

	}

	public void getDate(View v) {
		if (v.getId() == R.id.dateButton) {
			DialogFragment dFrag = new DatePickerFragment();
			dFrag.show(getSupportFragmentManager(), "datePicker");
		}

	}

	public void calculate(View v) {
		if (v.getId() == R.id.calculateButton) {

			if (editTextInputSum.getText().toString().equals("")) {
				showMessage(getResources().getText(R.string.errMesSum)
						.toString(), editTextInputSum.getText().toString());
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

			double inputSum = Double.parseDouble(editTextInputSum.getText()
					.toString());
			int period = Integer.parseInt(editTextPeriod.getText().toString());
			double percent = Double.parseDouble(editTextPercent.getText()
					.toString());
			String beginDate = textDatePayed.getText().toString();
			int payType = 0;

			if (r2.isChecked()) {
				payType = 1;
			}
			LoanComputer lCompute = new LoanComputer(inputSum, period, percent,
					beginDate, payType, this.getContentResolver(), this);

			switch (calcType) {
			case 0:
				lCompute.calcBySumOfLoan();
				break;
			case 1:
				lCompute.calcByPayment();
				break;
			case 2:
				lCompute.calcByProfit();
				break;
			}

			editTextInputSum.setText("");
			editTextPercent.setText("");
			editTextPeriod.setText("");

			Intent intent = new Intent(this, Result.class);
			startActivity(intent);
		}
	}

	static String pad(int s) {
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

	private static class DatePickerFragment extends DialogFragment implements
			DatePickerDialog.OnDateSetListener {

		final Calendar c = Calendar.getInstance();
		int yy = c.get(Calendar.YEAR);
		int mth = c.get(Calendar.MONTH);
		int dd = c.get(Calendar.DAY_OF_MONTH);

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return new DatePickerDialog(getActivity(), this, yy, mth, dd);
		}

		public void onDateSet(DatePicker view, int year, int month, int day) {
			yy = year;
			mth = month;
			dd = day;
			updateDate();

		}

		private void updateDate() {
			LoanCalculatorMainForm.textDatePayed.setText(pad(dd) + "."
					+ pad(mth + 1) + "." + yy);
		}

	}

}

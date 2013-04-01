package calculator.ru.activity.mainform;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import calculator.dbase.DataSource;
import calculator.dbase.InputData;
import calculator.ru.R;
import calculator.ru.activity.showsheduler.Result;

public class LoanCalculatorMainForm extends FragmentActivity implements
		AdapterView.OnItemSelectedListener {

	public final static String PERCENT = "percent";
	public final static String BEGIN_DATE = "beginDate";
	public final static String PERIOD = "period";
	public final static String PAYTYPE = "paytype";

	private int year;
	private int mth;
	private int day;

	private LinearLayout formCalc;
	private EditText editTextName;
	private LinearLayout spinerLayout;
	private EditText editTextInputSum;
	private EditText editTextPercent;
	private EditText editTextPeriod;
	private Button dateButton;

	private static TextView textDatePayed;
	private RadioButton yearRadButn;
	private RadioButton mthRadButn;
	private RadioButton annitetRadioButton;
	private RadioButton differentRadioButton;
	private Button calculateButtn;
	private CharSequence[] loanTypes, calcTypes;

	private int calcType;

	private DataSource dSource;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calc_form);

		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int size = metrics.widthPixels / 18;
		int paddingLeftAndRight = 20;

		formCalc = (LinearLayout) findViewById(R.id.form_calc);

		editTextName = (EditText) findViewById(R.id.name_calc_edit_txt);
		editTextName.setTextSize(size - 6);

		spinerLayout = (LinearLayout) findViewById(R.id.spiner_layout);

		LayoutParams lParams = (LayoutParams) formCalc.getLayoutParams();
		lParams.width = metrics.widthPixels - paddingLeftAndRight;

		calcTypes = getResources().getStringArray(R.array.calcTypes);

		LayoutParams spinParams = (LayoutParams) spinerLayout.getLayoutParams();
		spinParams.height = metrics.heightPixels / 11;

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
		editTextInputSum.setTextSize(size - 6);

		yearRadButn = (RadioButton) findViewById(R.id.year_rad_btn);
		yearRadButn.setChecked(true);
		yearRadButn.setTextSize(size - 6);

		mthRadButn = (RadioButton) findViewById(R.id.month_rad_btn);
		mthRadButn.setChecked(false);
		mthRadButn.setTextSize(size - 6);

		editTextPeriod = (EditText) findViewById(R.id.period);
		editTextPeriod.setTextSize(size - 6);

		editTextPercent = (EditText) findViewById(R.id.percent);
		editTextPercent.setTextSize(size - 6);

		textDatePayed = (TextView) findViewById(R.id.dateField);

		dateButton = (Button) findViewById(R.id.dateButton);
		dateButton.setTextSize(size - 4);

		Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		mth = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);
		textDatePayed.setText(pad(day) + "." + pad(mth + 1) + "." + year);
		textDatePayed.setTextSize(size);

		annitetRadioButton = (RadioButton) findViewById(R.id.anuitent);
		differentRadioButton = (RadioButton) findViewById(R.id.different);
		annitetRadioButton.setChecked(true);
		differentRadioButton.setChecked(false);

		calculateButtn = (Button) findViewById(R.id.calculateButton);
		calculateButtn.setTextSize(size - 4);

		dSource = new DataSource(this);
		dSource.open();
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

	public void onDestroy() {
		dSource.close();
		super.onDestroy();

	}

	public void onResume() {
		dSource.open();
		super.onResume();

	}

	public void calculate(View v) {
		if (v.getId() == R.id.calculateButton) {
			if (editTextName.getText().toString().equals(null)) {
				showMessage(getResources().getText(R.string.errMesNameCalc)
						.toString(), editTextName.getText().toString());
			}

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
			// if (yearRadButn.isChecked()) {
			// period *= 12;
			// }
			double percent = Double.parseDouble(editTextPercent.getText()
					.toString());
			String beginDate = textDatePayed.getText().toString();

			int payType = 1;

			if (differentRadioButton.isChecked()) {
				payType = 0;
			}
			String nameCalc = editTextName.getText().toString();
			/*
			 * LoanComputer lCompute = new LoanComputer(inputSum, period,
			 * percent, beginDate, payType, this.getContentResolver(), this,
			 * nameCalc);
			 * 
			 * switch (calcType) { case BY_SUM: lCompute.calcBySumOfLoan();
			 * break; case BY_PAY: lCompute.calcByPayment(); break; case
			 * BY_PROFIT: lCompute.calcByProfit(); break; }
			 */

			InputData iData = new InputData(inputSum, percent, beginDate,
					period, yearRadButn.isChecked(), calcType, payType,
					nameCalc);
			dSource.insertInputData(iData);

			editTextName.setText("");
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

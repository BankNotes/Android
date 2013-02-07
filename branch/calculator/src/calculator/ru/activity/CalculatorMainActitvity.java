package calculator.ru.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import calculator.ru.R;

public class CalculatorMainActitvity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calc_main);

	}

	public void clickButton(View v) {
		switch (v.getId()) {
		case R.id.showCalc:
			Intent i = new Intent(this, LoanCalculatorMainForm.class);

			startActivity(i);
			break;
		case R.id.showPrevCalc:

			break;
		default:
			break;
		}
	}
}

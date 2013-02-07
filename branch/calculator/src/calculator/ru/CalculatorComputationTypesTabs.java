// This class is not used!!!!

package calculator.ru;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TabHost;
import calculator.ru.R;
import calculator.ru.activity.comput.CalcBySumLoan;
import calculator.ru.activity.comput.CalcBySumPay;
import calculator.ru.activity.comput.CalcSumByProfit;

public class CalculatorComputationTypesTabs extends TabActivity {

	final String TABS_TAG_1 = "Tag 1";
	final String TABS_TAG_2 = "Tag 2";
	final String TABS_TAG_3 = "Tag 3";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calculator);

		TabHost tabHost = getTabHost();

		TabHost.TabSpec tabSpec;

		tabSpec = tabHost.newTabSpec(TABS_TAG_1);
		tabSpec.setContent(TabFactory);
		tabSpec.setIndicator(getResources().getText(R.string.bySumOfLoan));

		tabSpec.setContent(new Intent(this, CalcBySumLoan.class));
		tabHost.addTab(tabSpec);

		tabSpec = tabHost.newTabSpec("tag2");
		tabSpec.setIndicator(getResources()
				.getText(R.string.byAmmountOfPayment));
		tabSpec.setContent(new Intent(this, CalcBySumPay.class));
		tabHost.addTab(tabSpec);

		tabSpec = tabHost.newTabSpec("tag3");
		tabSpec.setIndicator(getResources().getText(R.string.byAmmountIncome));
		tabSpec.setContent(new Intent(this, CalcSumByProfit.class));
		tabHost.addTab(tabSpec);

	}

	TabHost.TabContentFactory TabFactory = new TabHost.TabContentFactory() {

		// @Override
		public View createTabContent(String tag) {

			if (tag == TABS_TAG_1) {
				return getLayoutInflater().inflate(R.layout.calc_by_sum_cred,
						null);
			} else if (tag == TABS_TAG_2) {
				return getLayoutInflater().inflate(R.layout.calc_by_sum_pay,
						null);
			} else if (tag == TABS_TAG_3) {
				return getLayoutInflater().inflate(R.layout.calc_by_profit,
						null);
			}

			return null;
		}
	};

}
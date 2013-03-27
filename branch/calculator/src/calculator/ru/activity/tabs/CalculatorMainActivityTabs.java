package calculator.ru.activity.tabs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import calculator.ru.R;
import calculator.ru.activity.allcomputs.ListOfCalcs;
import calculator.ru.activity.mainform.LoanCalculatorMainForm;

@SuppressWarnings("deprecation")
public class CalculatorMainActivityTabs extends TabActivity {

	final String TABS_TAG_1 = "Tag 1";
	final String TABS_TAG_2 = "Tag 2";
//	private DataSource dSource;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calculator);

		TabHost tabHost = getTabHost();

		TabHost.TabSpec tabSpec;

		tabSpec = tabHost.newTabSpec(TABS_TAG_1);

		tabSpec.setIndicator(getResources().getText(R.string.makeCalc));

		tabSpec.setContent(new Intent(this, LoanCalculatorMainForm.class));
		tabHost.addTab(tabSpec);

		tabSpec = tabHost.newTabSpec(TABS_TAG_2);
		tabSpec.setIndicator(getResources()
				.getText(R.string.viewCalcs));
		tabSpec.setContent(new Intent(this, ListOfCalcs.class));
		tabHost.addTab(tabSpec);

 
/*
		try {
			String dBasePath = getString(R.string.db_path)+"calc.db";
			File f = new File(dBasePath);
			if (f.exists()) {
				f.delete();
			}
			if (!f.exists()) {
				CopyDB(getBaseContext().getAssets().open("calc.db"),
						new FileOutputStream(dBasePath));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
		
//		dSource = new DataSource(this);
		
		
	}



	public void CopyDB(InputStream inputStream, OutputStream outputStream)
			throws IOException {
		// ---copy 1K bytes at a time---
		byte[] buffer = new byte[1024];
		int length;
		while ((length = inputStream.read(buffer)) > 0) {
			outputStream.write(buffer, 0, length);
		}
		inputStream.close();
		outputStream.close();
	}
}

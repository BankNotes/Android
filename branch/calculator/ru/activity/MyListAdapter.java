package calculator.ru.activity;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import calculator.ru.R;
import calculator.ru.activity.item.ItemOfCalc;
import calculator.ru.activity.service.MoneyConvertor;

public class MyListAdapter extends BaseAdapter {

	Context context;
	LayoutInflater lInflater;
	ArrayList<ItemOfCalc> items;

	private OnCheckedChangeListener checkListener = new OnCheckedChangeListener() {

		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			getItemOfCalc((Integer) buttonView.getTag()).setChecked(isChecked);

		}
	};

	public MyListAdapter(Context context, ArrayList<ItemOfCalc> items) {
		this.context = context;
		this.items = items;
		lInflater = (LayoutInflater) this.context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {

		return items.size();
	}

	public Object getItem(int position) {

		return items.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			view = lInflater.inflate(R.layout.item_of_list, parent, false);
		}

		ItemOfCalc i = getItemOfCalc(position);

		((TextView) view.findViewById(R.id.sum)).setText(MoneyConvertor
				.convertToMoneyFormat(i.getDoubleSum()));
		((TextView) view.findViewById(R.id.percent)).setText(i.getPercent());
		((TextView) view.findViewById(R.id.begin_date)).setText(i
				.getBeginDate());
		((TextView) view.findViewById(R.id.end_date)).setText(i.getEndDate());
		((TextView) view.findViewById(R.id.period)).setText(i.getPeriod());
		((TextView) view.findViewById(R.id.credit_type)).setText(i
				.getCreditType());
		((TextView) view.findViewById(R.id.calc_type)).setText(i.getCalcType());

		CheckBox chBox = (CheckBox) view.findViewById(R.id.chk_box);
		chBox.setOnCheckedChangeListener(checkListener);
		chBox.setTag(position);
		chBox.setChecked(i.isChecked());

		return view;
	}
	
	public ArrayList<ItemOfCalc> getSelectedItems() {
		ArrayList<ItemOfCalc> items = new ArrayList<ItemOfCalc>();
		for (ItemOfCalc it : this.items) {
			if (it.isChecked()) {
				items.add(it);
			}
			
		}
		return items;
	}
	
	

	ItemOfCalc getItemOfCalc(int position) {
		return (ItemOfCalc) getItem(position);
	}
	
	
}

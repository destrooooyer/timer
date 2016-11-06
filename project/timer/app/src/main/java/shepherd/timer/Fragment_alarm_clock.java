package shepherd.timer;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DESTR on 2016/10/26.
 */

public class Fragment_alarm_clock extends Fragment
{
	private Button testBtn;
	private Context context;
	private ListView listView;
	private ArrayList<alarmData> alarms;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState)
	{
		context = this.getActivity();
		View v = inflater.inflate(R.layout.fragment_alarm_clock, container, false);
		testBtn = (Button) v.findViewById(R.id.test_btn);
		testBtn.setOnClickListener(testBtnOnClickListener);
		listView = (ListView) v.findViewById(R.id.alarm_lv);

		alarms=new ArrayList<alarmData>();

		init();
		myAdapter adapter = new myAdapter(this.getActivity());
		listView.setAdapter(adapter);


		return v;
	}

	private void init()
	{
		alarms.add(new alarmData("123", 1));
		alarms.add(new alarmData("123", 2));
		alarms.add(new alarmData("1233", 1));
	}


	private View.OnClickListener testBtnOnClickListener = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			Alarm_util.setAlarm(context, 0, 0, 0, 0, "123", true, 0);
		}
	};

	public final class viewHolder
	{
		public TextView time;
		public TextView day;
		public Switch sw;
	}

	public class myAdapter extends BaseAdapter
	{

		private LayoutInflater myInflater;

		public myAdapter(Context context)
		{
			myInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount()
		{
			return alarms.size();
		}

		@Override
		public Object getItem(int position)
		{
			return null;
		}

		@Override
		public long getItemId(int position)
		{
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			viewHolder holder = new viewHolder();
			if (convertView == null)
			{
				convertView = myInflater.inflate(R.layout.alarm_list_item, null);
				holder.time = (TextView) convertView.findViewById(R.id.alarm_list_time_tv);
				holder.day = (TextView) convertView.findViewById(R.id.alarm_list_day_tv);
				holder.sw = (Switch) convertView.findViewById(R.id.alarm_list_switch);
				convertView.setTag(holder);

			}
			else
				holder=(viewHolder) convertView.getTag();


			holder.time.setText(alarms.get(position).time);
			holder.day.setText(String.valueOf(alarms.get(position).day));

			return convertView;
		}
	}

	public class alarmData
	{
		public String time;
		public int day;

		public alarmData(String time, int day)
		{
			this.time = time;
			this.day = day;
		}

	}

}

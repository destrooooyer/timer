package shepherd.timer;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by DESTR on 2016/10/26.
 */

public class Fragment_alarm_clock extends Fragment
{
	private Context context;
	private ListView listView;
	private ArrayList<AlarmData> alarms;
	private CoordinatorLayout coorLayout;
	private FloatingActionButton floatActBtn;
	private myAdapter adapter;
	private int clickedPosition;

	public Fragment_alarm_clock()
	{

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.fragment_alarm_clock, container, false);

		context = this.getActivity();
		adapter = new myAdapter(this.getActivity());
		coorLayout = (CoordinatorLayout) v.findViewById(R.id.alarm_coor_layout);

		listView = (ListView) v.findViewById(R.id.alarm_lv);
		listView.setOnItemClickListener(lvOnClickListener);
		listView.setOnItemLongClickListener(lvOnLongClickListener);

		floatActBtn = (FloatingActionButton) v.findViewById(R.id.alarm_act_button);
		floatActBtn.setOnClickListener(actBtnClickListener);


		alarms = new ArrayList<AlarmData>();
		try
		{
			File file = new File(context.getFilesDir(), "alarmData.xml");
			if (file.exists())
			{
				InputStream is = new FileInputStream(file);
				Xml_IO xmlIO = new Xml_IO();
				alarms = xmlIO.pullXML(is);
				is.close();
			}
			else
			{
				listView.setBackgroundColor(Color.BLUE);
				file.createNewFile();
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (XmlPullParserException e)
		{
			e.printStackTrace();
		}


		listView.setAdapter(adapter);

		return v;
	}


	public final class viewHolder
	{
		public TextView id;
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
				holder.id = (TextView) convertView.findViewById(R.id.alarm_list_id);
				convertView.setTag(holder);

			}
			else
				holder = (viewHolder) convertView.getTag();

			String temp = "";
			if (alarms.get(position).hour < 10)
				temp += '0';
			temp += String.valueOf(alarms.get(position).hour) + ":";
			if (alarms.get(position).minute < 10)
				temp += '0';
			temp += String.valueOf(alarms.get(position).minute);
			holder.time.setText(temp);

			temp = "";
			if (alarms.get(position).repeat)
			{
				temp += "循环：";
				String temp2[] = {"一", "二", "三", "四", "五", "六", "日"};
				for (int i = 0; i < 7; i++)
				{
					if (alarms.get(position).checkedDays[i])
						temp += temp2[i];
				}
			}
			else
				temp += "不循环";
			holder.day.setText(temp);
			holder.id.setText(String.valueOf(position + 1));
			if (alarms.get(position).on)
				holder.sw.setChecked(true);


			return convertView;
		}
	}

	AdapterView.OnItemLongClickListener lvOnLongClickListener = new AdapterView.OnItemLongClickListener()
	{
		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id)
		{
			Snackbar snackbar = Snackbar.make(coorLayout, "删除闹钟" + String.valueOf(position + 1), 2000);
			snackbar.getView().setBackgroundColor(Color.GRAY);
			snackbar.setAction("删除", new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					alarms.remove(position);

					adapter.notifyDataSetChanged();

					try
					{
						File file = new File(context.getFilesDir(), "alarmData.xml");
						if (!file.exists())
							file.createNewFile();
						OutputStream os = new FileOutputStream(file);
						Xml_IO xmlIO = new Xml_IO();
						xmlIO.putXML(os, alarms);
						os.flush();
						os.close();
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
				}
			});
			snackbar.show();
			return true;
		}
	};

	AdapterView.OnItemClickListener lvOnClickListener = new AdapterView.OnItemClickListener()
	{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id)
		{
			clickedPosition = position;
			Intent intent = new Intent();
			intent.setClass(getActivity(), Set_alarm_activity.class);
			startActivityForResult(intent, 23333);
		}
	};

	View.OnClickListener actBtnClickListener = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			Intent intent = new Intent();
			intent.setClass(getActivity(), Set_alarm_activity.class);
			startActivityForResult(intent, 233);
		}

	};

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == 233 && resultCode == 2333)
		{
			AlarmData alarmData = new AlarmData();
			alarmData.hour = data.getExtras().getInt("hour");
			alarmData.minute = data.getExtras().getInt("minute");
			alarmData.repeat = data.getExtras().getBoolean("repeat");
			alarmData.checkedDays[0] = data.getExtras().getBoolean("Mon");
			alarmData.checkedDays[1] = data.getExtras().getBoolean("Tue");
			alarmData.checkedDays[2] = data.getExtras().getBoolean("Wed");
			alarmData.checkedDays[3] = data.getExtras().getBoolean("Thu");
			alarmData.checkedDays[4] = data.getExtras().getBoolean("Fri");
			alarmData.checkedDays[5] = data.getExtras().getBoolean("Sat");
			alarmData.checkedDays[6] = data.getExtras().getBoolean("Sun");
			alarmData.on = false;
			alarms.add(alarmData);
			adapter.notifyDataSetChanged();

			try
			{
				File file = new File(context.getFilesDir(), "alarmData.xml");
				if (!file.exists())
					file.createNewFile();
				OutputStream os = new FileOutputStream(file);
				Xml_IO xmlIO = new Xml_IO();
				xmlIO.putXML(os, alarms);
				os.flush();
				os.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		else if (requestCode == 23333 && resultCode == 2333)
		{
			alarms.get(clickedPosition).hour = data.getExtras().getInt("hour");
			alarms.get(clickedPosition).minute = data.getExtras().getInt("minute");
			alarms.get(clickedPosition).repeat = data.getExtras().getBoolean("repeat");
			alarms.get(clickedPosition).checkedDays[0] = data.getExtras().getBoolean("Mon");
			alarms.get(clickedPosition).checkedDays[1] = data.getExtras().getBoolean("Tue");
			alarms.get(clickedPosition).checkedDays[2] = data.getExtras().getBoolean("Wed");
			alarms.get(clickedPosition).checkedDays[3] = data.getExtras().getBoolean("Thu");
			alarms.get(clickedPosition).checkedDays[4] = data.getExtras().getBoolean("Fri");
			alarms.get(clickedPosition).checkedDays[5] = data.getExtras().getBoolean("Sat");
			alarms.get(clickedPosition).checkedDays[6] = data.getExtras().getBoolean("Sun");
			adapter.notifyDataSetChanged();

			try
			{
				File file = new File(context.getFilesDir(), "alarmData.xml");
				if (!file.exists())
					file.createNewFile();
				OutputStream os = new FileOutputStream(file);
				Xml_IO xmlIO = new Xml_IO();
				xmlIO.putXML(os, alarms);
				os.flush();
				os.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

}

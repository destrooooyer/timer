package shepherd.timer;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
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
import java.util.Calendar;
import java.util.Random;

/**
 * Created by DESTR on 2016/10/26.
 */

public class Fragment_alarm_clock extends Fragment
{
	private Context context;
	private ListView listView;
	private ArrayList<Alarm_Data> alarms;
	private CoordinatorLayout coorLayout;
	private FloatingActionButton floatActBtn;
	private myAdapter adapter;
	private int clickedPosition;
	private ringHandler handler;
	private AdapterNotifyHandler adapterNotifyHandler;

	private Uri notification;
	private Ringtone r;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.fragment_alarm_clock, container, false);

		context = this.getActivity();		//alarm manager 需要的 context
		adapter = new myAdapter(this.getActivity());	//listview的adapter
		coorLayout = (CoordinatorLayout) v.findViewById(R.id.alarm_coor_layout);	//在这上显示snack bar

		listView = (ListView) v.findViewById(R.id.alarm_lv);
		listView.setOnItemClickListener(lvOnClickListener);
		listView.setOnItemLongClickListener(lvOnLongClickListener);

		floatActBtn = (FloatingActionButton) v.findViewById(R.id.alarm_act_button);
		floatActBtn.setOnClickListener(actBtnClickListener);


		alarms = new ArrayList<Alarm_Data>();
		listView.setAdapter(adapter);

		//铃声
		notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
		r = RingtoneManager.getRingtone(context, notification);

		handler = new ringHandler();	//响铃用的

		adapterNotifyHandler = new AdapterNotifyHandler();	//【找姿势在这】在其他线程更新ui的姿势。只能在ui线程更新listview，所以其他线程要send message给这个handler来更新
		new load().execute();	//在别的线程load xml


//		if (getActivity().getIntent().getStringExtra("msg") == "唤醒")
//			new ringAlarm(getActivity().getIntent().getIntExtra("id", -1)).execute();

		return v;
	}


	private class load extends AsyncTask<String, String, String>
	{

		@Override
		protected String doInBackground(String... params)
		{
			try
			{
				File file = new File(context.getFilesDir(), "alarmData.xml");
				if (file.exists())
				{
					InputStream is = new FileInputStream(file);
					Xml_IO xmlIO = new Xml_IO();
					alarms = xmlIO.pullXMLAlarm(is);
					is.close();
				}
				else
				{
					file.createNewFile();
				}
				//adapter.notifyDataSetChanged();
				adapterNotifyHandler.sendMessage(new Message());

				//receiver 收到之后，启动main activity带的intent里有msg，证明是被receiver启动的
				if (getActivity().getIntent().getStringExtra("msg") != null)
				{
					Message msg = new Message();
					Bundle bundle = new Bundle();
					bundle.putInt("id", getActivity().getIntent().getIntExtra("id", -1));
					msg.setData(bundle);
					handler.sendMessage(msg);	//处理闹钟
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


			return null;
		}
	}

	class AdapterNotifyHandler extends android.os.Handler
	{
		@Override
		public void handleMessage(Message msg)
		{
			adapter.notifyDataSetChanged();
		}
	}

	class ringHandler extends android.os.Handler
	{

		private void clearIntent()
		{
			getActivity().getIntent().removeExtra("id");
			getActivity().getIntent().removeExtra("msg");
			getActivity().getIntent().removeExtra("msg2");
		}

		@Override
		public void handleMessage(Message msg)
		{
			int id = msg.getData().getInt("id");
			clearIntent();		//把intent弄没，不然每次转到这个fragment的时候都会响闹钟，因为intent还在
			r.play();

			//【找姿势在这】dialog的使用姿势如下
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setTitle("闹钟响了");
			builder.setPositiveButton("确认", new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					r.stop();
				}
			});
			builder.create().show();

			for (int i = 0; i < alarms.size(); i++)
			{
				if (alarms.get(i).id == id)
				{
					if (alarms.get(i).repeat)		//重复的，重设闹钟
						Alarm_util.setAlarmClock(context, alarms.get(i));
					else							//非重复的，关闭
					{
						alarms.get(i).on = false;
						adapter.notifyDataSetChanged();
						new saveXml().execute();
					}
				}
			}
		}
	}

	public final class viewHolder	//便于存list view的item，内容与alarm_list_item.xml对应
	{
		public TextView id;
		public TextView time;
		public TextView day;
		public Switch sw;
	}

	//list view 的adapter
	public class myAdapter extends BaseAdapter
	{

		private LayoutInflater myInflater;

		public myAdapter(Context context)
		{
			myInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount()	//元素个数
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

			//给每个item的switch单独弄一个listener的姿势，回来看的话可供参考
			SwitchListener swListener = new SwitchListener(position);
			if (convertView == null)	//把listview的item里面的货存到viewholder里，这里有setTag和getTag的用法
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
				if (temp.equals("循环："))
					temp += "未设置周几响";
			}
			else
			{
				temp += "不循环：";

				Calendar calendarAlarm = Calendar.getInstance();
				Calendar calendarNow = Calendar.getInstance();
				calendarAlarm.set(calendarAlarm.get(Calendar.YEAR), calendarAlarm.get(Calendar.MONTH), calendarAlarm.get(Calendar.DAY_OF_MONTH), alarms.get(position).hour, alarms.get(position).minute, 0);
				calendarNow.set(calendarNow.get(Calendar.YEAR), calendarNow.get(Calendar.MONTH), calendarNow.get(Calendar.DAY_OF_MONTH), calendarNow.get(Calendar.HOUR_OF_DAY), calendarNow.get(Calendar.MINUTE), 0);
				if (calendarAlarm.getTimeInMillis() <= calendarNow.getTimeInMillis())
					temp += "明天";
				else
					temp += "今天";
			}
			holder.day.setText(temp);
			holder.id.setText(String.valueOf(position + 1));

			//【找姿势在这】防止setchecked触发onCheckedChangeListener的姿势
			holder.sw.setOnCheckedChangeListener(null);
			if (alarms.get(position).on)
				holder.sw.setChecked(true);
			else
				holder.sw.setChecked(false);
			holder.sw.setOnCheckedChangeListener(swListener);


			return convertView;
		}
	}

	//把操作文件相关的都弄成异步的，不然占着ui线程不是有毒？
	private class saveXml extends AsyncTask<String, String, String>
	{

		@Override
		protected String doInBackground(String... params)
		{
			try
			{
				File file = new File(context.getFilesDir(), "alarmData.xml");
				if (!file.exists())
					file.createNewFile();
				OutputStream os = new FileOutputStream(file);
				Xml_IO xmlIO = new Xml_IO();
				xmlIO.putXMLAlarm(os, alarms);
				os.flush();
				os.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			return null;
		}
	}


	//【找姿势在这】listview的item中的switch的listener，加了个position属性，这样就知道谁是谁了
	public class SwitchListener implements CompoundButton.OnCheckedChangeListener
	{
		private int position;

		public SwitchListener(int position)
		{
			this.position = position;
		}

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
		{
			if (buttonView.isChecked())
			{
				alarms.get(position).on = true;

				Snackbar snackbar = Snackbar.make(coorLayout, "闹钟" + String.valueOf(position + 1) + "已启动", 2000);
				snackbar.getView().setBackgroundColor(Color.GRAY);
				snackbar.show();
				Alarm_util.setAlarmClock(context, alarms.get(position));
			}
			else
			{
				alarms.get(position).on = false;

				Snackbar snackbar = Snackbar.make(coorLayout, "闹钟" + String.valueOf(position + 1) + "已关闭", 2000);
				snackbar.getView().setBackgroundColor(Color.GRAY);
				snackbar.show();
				Alarm_util.cancelAlarmClock(context, alarms.get(position));
			}
			new saveXml().execute();

		}
	}

	//【找姿势在这】注意listview里的货在触发LongClick的时候也会触发click，解决的姿势就是返回值
	//返回true不再触发click，返回false会触发click
	AdapterView.OnItemLongClickListener lvOnLongClickListener = new AdapterView.OnItemLongClickListener()
	{
		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id)
		{
			//【找姿势在这】使用snackbar的姿势
			Snackbar snackbar = Snackbar.make(coorLayout, "删除闹钟" + String.valueOf(position + 1), 2000);
			snackbar.getView().setBackgroundColor(Color.GRAY);
			snackbar.setAction("删除", new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					alarms.remove(position);

					adapter.notifyDataSetChanged();

					new saveXml().execute();
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
			startActivityForResult(intent, 23333);		//【找姿势在这】需要activity返回货的姿势，23333是request code
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

	//【找姿势在这】用startactivityforresult，那个activity结束之后会调用onactivityresult，intent是货，前面那俩参数如其名
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == 233 && resultCode == 2333)
		{
			Alarm_Data alarmData = new Alarm_Data();
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

			//这里产生id，都是10的倍数，因为每个闹钟都有周1-周日，对应id+0~6，不会重复
			Random rand = new Random();
			int randInt = rand.nextInt() / 10 * 10;
			while (randInt <= 0)
				randInt = rand.nextInt() / 10 * 10;
			while (true)
			{
				boolean flag = true;
				for (Alarm_Data item : alarms)
				{
					if (item.id == randInt)
					{
						flag = false;
						randInt = rand.nextInt() / 10 * 10;
						break;
					}
				}

				if (flag)
					break;
			}
			alarmData.id = randInt;

			alarms.add(alarmData);
			adapter.notifyDataSetChanged();

			new saveXml().execute();
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

			alarms.get(clickedPosition).on = false;
			adapter.notifyDataSetChanged();

			try
			{
				File file = new File(context.getFilesDir(), "alarmData.xml");
				if (!file.exists())
					file.createNewFile();
				OutputStream os = new FileOutputStream(file);
				Xml_IO xmlIO = new Xml_IO();
				xmlIO.putXMLAlarm(os, alarms);
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

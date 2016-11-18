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

import org.w3c.dom.Text;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

/**
 * Created by DESTR on 2016/10/26.
 */

public class Fragment_memorial extends Fragment
{
	private Context context;
	private ListView listView;
	private CoordinatorLayout coorLayout;
	private FloatingActionButton floatActBtn;
	private ArrayList<Memorial_Data> memorials;
	private int clickedPosition;
	private myAdapter adapter;
	private AdapterNotifyHandler adapterNotifyHandler;
	private ringHandler handler;

	private Uri notification;
	private Ringtone r;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.fragment_memorial, container, false);

		memorials = new ArrayList<>();
		context = getActivity();
		listView = (ListView) v.findViewById(R.id.memorial_lv);
		coorLayout = (CoordinatorLayout) v.findViewById(R.id.memorial_coor_layout);

		notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
		r = RingtoneManager.getRingtone(context, notification);

		listView.setOnItemClickListener(lvOnClickListener);
		listView.setOnItemLongClickListener(lvOnLongClickListener);

		floatActBtn = (FloatingActionButton) v.findViewById(R.id.memorial_act_button);
		floatActBtn.setOnClickListener(actBtnClickListener);

		adapterNotifyHandler = new AdapterNotifyHandler();
		handler = new ringHandler();

		adapter = new myAdapter(context);
		listView.setAdapter(adapter);

		new load().execute();

		return v;
	}

	AdapterView.OnItemLongClickListener lvOnLongClickListener = new AdapterView.OnItemLongClickListener()
	{
		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id)
		{
			Snackbar snackbar = Snackbar.make(coorLayout, "删除 " + String.valueOf(memorials.get(position).title), 2000);
			snackbar.getView().setBackgroundColor(Color.GRAY);
			snackbar.setAction("删除", new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					memorials.remove(position);

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
			intent.setClass(getActivity(), Set_memorial_day_activity.class);
			startActivityForResult(intent, 23333);
		}
	};

	View.OnClickListener actBtnClickListener = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			Intent intent = new Intent();
			intent.setClass(getActivity(), Set_memorial_day_activity.class);
			startActivityForResult(intent, 233);
		}

	};

	public final class viewHolder
	{
		public TextView title;
		public TextView date;
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
			return memorials.size();
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
			SwitchListener swListener = new SwitchListener(position);
			if (convertView == null)
			{
				convertView = myInflater.inflate(R.layout.memorial_list_item, null);
				holder.title = (TextView) convertView.findViewById(R.id.memorial_list_title_tv);
				holder.date = (TextView) convertView.findViewById(R.id.memorial_list_day_tv);
				holder.sw = (Switch) convertView.findViewById(R.id.memorial_list_switch);
				convertView.setTag(holder);

			}
			else
				holder = (viewHolder) convertView.getTag();

			holder.title.setText(memorials.get(position).title);
			String temp = "";
			temp += String.valueOf(memorials.get(position).year) + "年";
			temp += String.valueOf(memorials.get(position).month + 1) + "月";
			temp += String.valueOf(memorials.get(position).day) + "日";


			holder.date.setText(temp);

			holder.sw.setOnCheckedChangeListener(null);
			if (memorials.get(position).on)
				holder.sw.setChecked(true);
			else
				holder.sw.setChecked(false);
			holder.sw.setOnCheckedChangeListener(swListener);


			return convertView;
		}
	}

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
				memorials.get(position).on = true;

				Snackbar snackbar = Snackbar.make(coorLayout, String.valueOf(memorials.get(position).title) + " 提醒已启动", 2000);
				snackbar.getView().setBackgroundColor(Color.GRAY);
				snackbar.show();
				Alarm_util.setAlarmMemorial(context, memorials.get(position));
			}
			else
			{
				memorials.get(position).on = false;

				Snackbar snackbar = Snackbar.make(coorLayout, String.valueOf(memorials.get(position).title) + " 提醒已关闭", 2000);
				snackbar.getView().setBackgroundColor(Color.GRAY);
				snackbar.show();
				Alarm_util.cancelAlarmMemorial(context, memorials.get(position));
			}

			new saveXml().execute();

		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == 233 && resultCode == 2333)    //request code 233: new
		{
			Memorial_Data memorialData = new Memorial_Data();
			memorialData.year = data.getIntExtra("year", 0);
			memorialData.month = data.getIntExtra("month", 0);
			memorialData.day = data.getIntExtra("day", 0);
			memorialData.title = data.getStringExtra("title");
			memorialData.on = false;

			Random rand = new Random();
			int randInt = rand.nextInt();
			while (randInt <= 0)
				randInt = rand.nextInt() / 10 * 10 + 9;
			while (true)
			{
				boolean flag = true;
				for (Memorial_Data item : memorials)
				{
					if (item.id == randInt)
					{
						flag = false;
						randInt = rand.nextInt() / 10 * 10 + 9;
						break;
					}
				}

				if (flag)
					break;
			}
			memorialData.id = randInt;

			memorials.add(memorialData);
			adapter.notifyDataSetChanged();
			new saveXml().execute();
		}
		else if (requestCode == 23333 && resultCode == 2333)    //request code 23333: modify
		{
			memorials.get(clickedPosition).year = data.getIntExtra("year", 0);
			memorials.get(clickedPosition).month = data.getIntExtra("month", 0);
			memorials.get(clickedPosition).day = data.getIntExtra("day", 0);
			memorials.get(clickedPosition).title = data.getStringExtra("title");
			memorials.get(clickedPosition).on = false;

			adapter.notifyDataSetChanged();
			new saveXml().execute();
		}
	}

	private class load extends AsyncTask<String, String, String>
	{

		@Override
		protected String doInBackground(String... params)
		{
			try
			{
				File file = new File(context.getFilesDir(), "memorialData.xml");
				if (file.exists())
				{
					InputStream is = new FileInputStream(file);
					Xml_IO xmlIO = new Xml_IO();
					memorials = xmlIO.pullXMLMemorial(is);
					is.close();
				}
				else
				{
					file.createNewFile();
				}
				//adapter.notifyDataSetChanged();
				adapterNotifyHandler.sendMessage(new Message());

				if (getActivity().getIntent().getStringExtra("msg2") != null)
				{
					Message msg = new Message();
					Bundle bundle = new Bundle();
					bundle.putInt("id", getActivity().getIntent().getIntExtra("id", -1));
					msg.setData(bundle);
					handler.sendMessage(msg);
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
			clearIntent();

			int position = -1;
			for (int i = 0; i < memorials.size(); i++)
			{
				if (memorials.get(i).id == id)
				{
					Alarm_util.setAlarmMemorial(context, memorials.get(i));
					position = i;
				}
			}
			if (position != -1)
			{
				r.play();
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle(memorials.get(position).title);
				builder.setPositiveButton("确认", new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						r.stop();
					}
				});
				builder.create().show();

			}
		}
	}


	private class saveXml extends AsyncTask<String, String, String>
	{

		@Override
		protected String doInBackground(String... params)
		{
			try
			{
				File file = new File(context.getFilesDir(), "memorialData.xml");
				if (!file.exists())
					file.createNewFile();
				OutputStream os = new FileOutputStream(file);
				Xml_IO xmlIO = new Xml_IO();
				xmlIO.putXMLMemorial(os, memorials);
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

}

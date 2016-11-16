package shepherd.timer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.aigestudio.wheelpicker.WheelPicker;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by DESTR on 2016/10/26.
 */

public class Fragment_count_down extends Fragment
{
	private TickTockView countdown;
	private WheelPicker hour;
	private WheelPicker minute;
	private WheelPicker second;
	private Button pause;
	private Button start;
	private Calendar end;
	private Context context;

	private int hoursRemain;
	private int minutesRemain;
	private int secondsRemain;

	private boolean filter;

	private ArrayList<String> dataHour;
	private ArrayList<String> dataMinute;
	private ArrayList<String> dataSecond;


	private Uri notification;
	private Ringtone r;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.fragment_count_down, container, false);
		context = getActivity();

		notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
		r = RingtoneManager.getRingtone(context, notification);

		countdown = (TickTockView) v.findViewById(R.id.countdown);
		hour = (WheelPicker) v.findViewById(R.id.countdown_hour_picker);
		minute = (WheelPicker) v.findViewById(R.id.countdown_minute_picker);
		second = (WheelPicker) v.findViewById(R.id.countdown_second_picker);
		pause = (Button) v.findViewById(R.id.countdown_pause);
		start = (Button) v.findViewById(R.id.countdown_start);


		hour.setAtmospheric(true);
		hour.setSelectedItemTextColor(Color.rgb(0xff, 0x7f, 0x00));
		hour.setCyclic(true);

		minute.setAtmospheric(true);
		minute.setSelectedItemTextColor(Color.rgb(0xff, 0x7f, 0x00));
		minute.setCyclic(true);

		second.setAtmospheric(true);
		second.setSelectedItemTextColor(Color.rgb(0xff, 0x7f, 0x00));
		second.setCyclic(true);

		dataHour = new ArrayList<>();
		dataSecond = new ArrayList<>();
		dataMinute = new ArrayList<>();

		for (int i = 10; i < 24; i++)
			dataHour.add(String.valueOf(i));
		for (int i = 10; i < 60; i++)
		{
			dataMinute.add(String.valueOf(i));
			dataSecond.add(String.valueOf(i));
		}
		for (int i = 0; i < 10; i++)
		{
			dataHour.add("0" + String.valueOf(i));
			dataMinute.add("0" + String.valueOf(i));
			dataSecond.add("0" + String.valueOf(i));
		}

		filter = true;

		hour.setData(dataHour);
		minute.setData(dataMinute);
		second.setData(dataSecond);

		countdown.setOnTickListener(new TickTockView.OnTickListener()
		{
			@Override
			public String getText(long timeRemaining)
			{
				secondsRemain = (int) (timeRemaining / 1000) % 60;
				minutesRemain = (int) ((timeRemaining / (1000 * 60)) % 60);
				hoursRemain = (int) ((timeRemaining / (1000 * 60 * 60)) % 24);

				if (secondsRemain == 0 && minutesRemain == 0 && hoursRemain == 0)
				{
					if (filter)
					{
						r.play();
						AlertDialog.Builder builder = new AlertDialog.Builder(context);
						builder.setTitle("倒计时结束了");
						builder.setPositiveButton("确认", new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface dialog, int which)
							{
								r.stop();
							}
						});
						builder.create().show();
						filter = false;
					}
				}

				return String.format("%1$02d%4$s %2$02d%5$s %3$02d%6$s",
						hoursRemain, minutesRemain, secondsRemain, "h", "m", "s");

			}
		});

		filter = false;
		countdown.start(Calendar.getInstance());
		countdown.stop();
		filter = true;

		pause.setText("暂停");
		pause.setEnabled(false);

		pause.setOnClickListener(pauseListener);

		start.setText("开始");
		start.setOnClickListener(startListener);

		return v;
	}

	private View.OnClickListener pauseListener = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			Button btn = (Button) v;
			if (btn.getText() == "继续")
			{
				filter = true;
				btn.setText("暂停");
				end = Calendar.getInstance();
				end.add(Calendar.HOUR, hoursRemain);
				end.add(Calendar.MINUTE, minutesRemain);
				end.add(Calendar.SECOND, secondsRemain);

				countdown.start(end);
			}
			else
			{
				btn.setText("继续");
				countdown.stop();
			}
		}
	};

	private View.OnClickListener startListener = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			Button btn = (Button) v;
			if (btn.getText() == "开始")
			{
				filter = true;
				btn.setText("清零");
				Calendar end = Calendar.getInstance();
				end.add(Calendar.HOUR, Integer.parseInt(dataHour.get(hour.getCurrentItemPosition())));
				end.add(Calendar.MINUTE, Integer.parseInt(dataMinute.get(minute.getCurrentItemPosition())));
				end.add(Calendar.SECOND, Integer.parseInt(dataSecond.get(second.getCurrentItemPosition())));
				pause.setEnabled(true);
				countdown.start(end);
			}
			else
			{
				filter = false;
				countdown.start(Calendar.getInstance());
				btn.setText("开始");
				pause.setEnabled(false);
				pause.setText("暂停");
				countdown.stop();
				filter = true;
			}
		}
	};

}

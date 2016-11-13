package shepherd.timer;


import android.content.Intent;
import android.graphics.Color;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Locale;


/**
 * Created by DESTR on 2016/11/11.
 */

public class Set_alarm_activity extends AppCompatActivity
{
	Button confirmButton;
	Button cancelButton;
	Toolbar toolbar;
	TimePicker timePicker;
	TextView tvDays[];
	CheckBox checkBox;
	boolean checkedDays[];
	int defaultColor;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set_alarm);

		toolbar = (Toolbar) findViewById(R.id.set_alarm_toolbar);
		setSupportActionBar(toolbar);

		timePicker = (TimePicker) findViewById(R.id.set_alarm_time_picker);
		timePicker.setIs24HourView(true);

		tvDays = new TextView[7];
		tvDays[0] = (TextView) findViewById(R.id.set_alarm_Mon);
		tvDays[1] = (TextView) findViewById(R.id.set_alarm_Tue);
		tvDays[2] = (TextView) findViewById(R.id.set_alarm_Wed);
		tvDays[3] = (TextView) findViewById(R.id.set_alarm_Thu);
		tvDays[4] = (TextView) findViewById(R.id.set_alarm_Fri);
		tvDays[5] = (TextView) findViewById(R.id.set_alarm_Sat);
		tvDays[6] = (TextView) findViewById(R.id.set_alarm_Sun);
		for (int i = 0; i < 7; i++)
			tvDays[i].setOnClickListener(tvDaysOnClickListener);

		defaultColor = tvDays[0].getCurrentTextColor();

		checkedDays = new boolean[7];
		for (int i = 0; i < 7; i++)
			checkedDays[i] = false;

		checkBox = (CheckBox) findViewById(R.id.set_alarm_check);
		checkBox.setChecked(true);
		checkBox.setOnCheckedChangeListener(checkedChangeListener);

		confirmButton = (Button) findViewById(R.id.set_alarm_confirm);
		confirmButton.setOnClickListener(confirmOnClickListener);

		cancelButton = (Button) findViewById(R.id.set_alarm_cancel);
		cancelButton.setOnClickListener(cancelOnClickListener);

		Calendar calendar = Calendar.getInstance();
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 2;
		if (dayOfWeek == -1) dayOfWeek = 6;
		checkedDays[dayOfWeek] = true;
		tvDays[dayOfWeek].setTextColor(Color.rgb(0xFF, 0x7F, 0x00));
	}


	CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener()
	{
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
		{
			if (isChecked)
			{
				boolean flag = false;
				for (int i = 0; i < 7; i++)
					if (checkedDays[i] == true)
						flag = true;
				if (flag == false)
				{
					for (int i = 0; i < 7; i++)
					{
						checkedDays[i] = true;
						tvDays[i].setTextColor(Color.rgb(0xFF, 0x7F, 0x00));
					}
				}
			}
			else
			{
				for (int i = 0; i < 7; i++)
				{
					checkedDays[i] = false;
					tvDays[i].setTextColor(defaultColor);
				}

			}
		}
	};

	View.OnClickListener confirmOnClickListener = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			Intent result = new Intent();
			result.putExtra("repeat", checkBox.isChecked());
			result.putExtra("Mon", checkedDays[0]);
			result.putExtra("Tue", checkedDays[1]);
			result.putExtra("Wed", checkedDays[2]);
			result.putExtra("Thu", checkedDays[3]);
			result.putExtra("Fri", checkedDays[4]);
			result.putExtra("Sat", checkedDays[5]);
			result.putExtra("Sun", checkedDays[6]);
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
			{
				result.putExtra("hour", timePicker.getHour());
				result.putExtra("minute", timePicker.getMinute());
			}
			else
			{
				result.putExtra("hour", timePicker.getCurrentHour());
				result.putExtra("minute", timePicker.getCurrentMinute());
			}
			setResult(2333, result);
			finish();
		}
	};

	View.OnClickListener cancelOnClickListener = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			Intent result = new Intent();
			setResult(0, result);
			finish();
		}
	};

	View.OnClickListener tvDaysOnClickListener = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			for (int i = 0; i < 7; i++)
				if (v.equals(tvDays[i]))
				{
					if (checkedDays[i] == false)
					{
						checkedDays[i] = true;
						tvDays[i].setTextColor(Color.rgb(0xFF, 0x7F, 0x00));
						checkBox.setChecked(true);
					}
					else
					{
						checkedDays[i] = false;
						tvDays[i].setTextColor(defaultColor);

						boolean flag = true;
						for (int j = 0; j < 7; j++)
						{
							if (checkedDays[j] == true)
							{
								flag = false;
								break;
							}
						}
						if (flag == true)
							checkBox.setChecked(false);
					}
				}
		}
	};
}

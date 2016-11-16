package shepherd.timer;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import java.util.Calendar;

/**
 * Created by DESTR on 2016/11/2.
 */

public class Alarm_util
{
	/**
	 * @param context
	 * @param id      id，用来区分pendingintent
	 * @param day     周几响
	 * @param hour    时
	 * @param minute  分
	 * @param msg     提示信息
	 * @param vibrate 是否振动
	 * @param repeat  是否重复[0不重复|1按周重复|2按天重复]
	 */
	public static void setAlarm(Context context, int id, int day, int hour, int minute, String msg, boolean vibrate, int repeat)
	{
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent("myAlarmClock");
		intent.putExtra("vibrate", vibrate);
		intent.putExtra("id", id);
		PendingIntent pdi = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_CANCEL_CURRENT);

		Calendar calendar = Calendar.getInstance();
		//set hour and minute
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), hour, minute, 0);

		if (repeat == 0)
		{
			Calendar calendarNow = Calendar.getInstance();
			calendarNow.set(calendarNow.get(Calendar.YEAR), calendarNow.get(Calendar.MONTH), calendarNow.get(Calendar.DAY_OF_MONTH), calendarNow.get(Calendar.HOUR_OF_DAY), calendarNow.get(Calendar.MINUTE), 0);
			if (calendarNow.getTimeInMillis() >= calendar.getTimeInMillis())
				calendar.add(Calendar.DATE, 1);
		}
		else if (repeat == 1)
		{
			int dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
			dayOfWeek -= 2;
			if (dayOfWeek == -1)
				dayOfWeek = 6;

			int dayCount = dayOfWeek - day;
			if (dayCount < 0)
				dayCount += 7;
			if (dayCount != 0)
				calendar.add(Calendar.DATE, dayCount);
			else
			{
				Calendar calendarNow = Calendar.getInstance();
				calendarNow.set(calendarNow.get(Calendar.YEAR), calendarNow.get(Calendar.MONTH), calendarNow.get(Calendar.DAY_OF_MONTH), calendarNow.get(Calendar.HOUR_OF_DAY), calendarNow.get(Calendar.MINUTE), 0);
				if (calendarNow.getTimeInMillis() >= calendar.getTimeInMillis())
					calendar.add(Calendar.DATE, 7);
			}
		}

		//api level 19(?据说)之后，set和set repeat变成不精确的了（为了省电），而setwindow和set exact是精确的
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
		{
			am.setWindow(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 10000, pdi);    //windowLengthMillis = 10000
		}
		else
		{
			am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pdi);
		}
	}

	public static void cancelAlarm(Context context, int id)
	{
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent("myAlarmClock");
		PendingIntent pdi = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		am.cancel(pdi);
	}


	public static void setAlarmClock(Context context, Alarm_Data alarm)
	{
		if (alarm.on)
		{
			if (alarm.repeat)
			{
				for (int i = 0; i < 7; i++)
				{
					if (alarm.checkedDays[i])
						setAlarm(context, alarm.id + i, i, alarm.hour, alarm.minute, "", alarm.repeat, 1);
				}
			}
			else
			{
				setAlarm(context, alarm.id, -1, alarm.hour, alarm.minute, "", alarm.repeat, 0);
			}
		}
	}

	public static void cancelAlarmClock(Context context, Alarm_Data alarm)
	{
		if (alarm.on == false)
		{
			if (alarm.repeat)
			{
				for (int i = 0; i < 7; i++)
				{
					if (alarm.checkedDays[i])
						Alarm_util.cancelAlarm(context, alarm.id + i);
				}
			}
			else
			{
				Alarm_util.cancelAlarm(context, alarm.id);
			}
		}
	}

	public static void setAlarmMemorial(Context context,Memorial_Data memorialData)
	{

	}

	public static void cancelAlarmMemorial(Context context,Memorial_Data memorialData)
	{

	}
}

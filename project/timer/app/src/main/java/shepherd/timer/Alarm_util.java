package shepherd.timer;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import java.util.Calendar;
import java.util.concurrent.SynchronousQueue;

/**
 * Created by DESTR on 2016/11/2.
 */

public class Alarm_util
{
	/**
	 * @param context
	 * @param id      用来区分pendingintent
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
		PendingIntent pdi = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_CANCEL_CURRENT);

		//test - alarm go off in 5s
		Calendar calendar=Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.add(Calendar.SECOND,5);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
		{
			am.setWindow(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),0,pdi);
		}


	}
}

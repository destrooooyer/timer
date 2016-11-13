package shepherd.timer;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.WindowManager;

/**
 * Created by DESTR on 2016/11/2.
 */

public class My_receiver extends BroadcastReceiver
{
	@Override
	public void onReceive(Context context, Intent intent)
	{
		Intent alarm = new Intent(context, MainActivity.class);

		alarm.putExtra("msg", "唤醒");
		alarm.putExtra("id", intent.getIntExtra("id", -1));

		alarm.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(alarm);
	}
}

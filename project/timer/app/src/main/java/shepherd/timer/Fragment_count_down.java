package shepherd.timer;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;

/**
 * Created by DESTR on 2016/10/26.
 */

public class Fragment_count_down extends Fragment
{
	private TickTockView countdown;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.fragment_count_down, container, false);
		countdown=(TickTockView)v.findViewById(R.id.view_ticktock_countdown);
		countdown.setOnTickListener(new TickTockView.OnTickListener() {
			@Override
			public String getText(long timeRemaining) {
				int seconds = (int) (timeRemaining / 1000) % 60;
				int minutes = (int) ((timeRemaining / (1000 * 60)) % 60);
				int hours = (int) ((timeRemaining / (1000 * 60 * 60)) % 24);
				int days = (int) (timeRemaining / (1000 * 60 * 60 * 24));
				boolean hasDays = days > 0;
				return String.format("%1$02d%4$s %2$02d%5$s %3$02d%6$s",
						hasDays ? days : hours,
						hasDays ? hours : minutes,
						hasDays ? minutes : seconds,
						hasDays ? "d" : "h",
						hasDays ? "h" : "m",
						hasDays ? "m" : "s");
			}
		});

		Calendar end = Calendar.getInstance();
		end.add(Calendar.MINUTE, 4);
		end.add(Calendar.SECOND, 5);

		Calendar start = Calendar.getInstance();
		start.add(Calendar.MINUTE, -1);
		if (countdown != null) {
			countdown.start(start, end);
		}
		return v;
	}
}

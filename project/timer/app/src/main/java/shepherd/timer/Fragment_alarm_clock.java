package shepherd.timer;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by DESTR on 2016/10/26.
 */

public class Fragment_alarm_clock extends Fragment
{
	private Button testBtn;
	private Context context;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState)
	{
		context=this.getActivity();
		View v = inflater.inflate(R.layout.fragment_alarm_clock, container, false);
		testBtn = (Button) v.findViewById(R.id.test_btn);
		testBtn.setOnClickListener(testBtnOnClickListener);
		return v;
	}

	private View.OnClickListener testBtnOnClickListener=new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			Alarm_util.setAlarm(context,0,0,0,0,"123",true,0);
		}
	};
}

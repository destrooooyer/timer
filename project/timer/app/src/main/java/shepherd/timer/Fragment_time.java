package shepherd.timer;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Random;
import java.util.StringTokenizer;

/**
 * Created by DESTR on 2016/10/26.
 */

public class Fragment_time extends Fragment
{
	private TextView tvDate;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.fragment_time, container, false);

		tvDate=(TextView) v.findViewById(R.id.tv_date);
		HashMap<Integer,String> dayOfWeekT=new HashMap<>();
		dayOfWeekT.put(1,"一");
		dayOfWeekT.put(2,"二");
		dayOfWeekT.put(3,"三");
		dayOfWeekT.put(4,"四");
		dayOfWeekT.put(5,"五");
		dayOfWeekT.put(6,"六");
		dayOfWeekT.put(7,"日");
		tvDate.setText(String.valueOf(Calendar.getInstance().get(Calendar.YEAR))+"年"+
				String.valueOf(Calendar.getInstance().get(Calendar.MONTH)+1)+"月"+
				String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH))+"日\n周"+
				dayOfWeekT.get(Calendar.getInstance().get(Calendar.DAY_OF_WEEK)));

		return v;
	}
}

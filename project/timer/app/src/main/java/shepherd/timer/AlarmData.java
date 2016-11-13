package shepherd.timer;

/**
 * Created by DESTR on 2016/11/12.
 */

public class AlarmData
{
	public int hour;
	public int minute;
	public int id;
	public boolean repeat;
	public boolean checkedDays[];
	public boolean on;

	public AlarmData()
	{
		this.checkedDays = new boolean[7];
	}
}

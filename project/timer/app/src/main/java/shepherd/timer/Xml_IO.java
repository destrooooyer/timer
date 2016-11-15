package shepherd.timer;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by DESTR on 2016/11/12.
 */

public class Xml_IO
{
	public ArrayList<Alarm_Data> pullXMLAlarm(InputStream inputStream) throws XmlPullParserException, IOException
	{
		ArrayList<Alarm_Data> data = new ArrayList<>();
		Alarm_Data alarm = new Alarm_Data();
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(inputStream, "UTF8");

		int eventType = parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT)
		{
			if (eventType == XmlPullParser.START_TAG)
			{
				if (parser.getName().equals("alarm"))
				{
					alarm = new Alarm_Data();
				}
				else if (parser.getName().equals("repeat"))
				{
					eventType = parser.next();
					alarm.repeat = Boolean.parseBoolean(parser.getText());
				}
				else if (parser.getName().equals("hour"))
				{
					eventType = parser.next();
					alarm.hour = Integer.parseInt(parser.getText());
				}
				else if (parser.getName().equals("minute"))
				{
					eventType = parser.next();
					alarm.minute = Integer.parseInt(parser.getText());
				}
				else if (parser.getName().equals("Mon"))
				{
					eventType = parser.next();
					alarm.checkedDays[0] = Boolean.parseBoolean(parser.getText());
				}
				else if (parser.getName().equals("Tue"))
				{
					eventType = parser.next();
					alarm.checkedDays[1] = Boolean.parseBoolean(parser.getText());
				}
				else if (parser.getName().equals("Wed"))
				{
					eventType = parser.next();
					alarm.checkedDays[2] = Boolean.parseBoolean(parser.getText());
				}
				else if (parser.getName().equals("Thu"))
				{
					eventType = parser.next();
					alarm.checkedDays[3] = Boolean.parseBoolean(parser.getText());
				}
				else if (parser.getName().equals("Fri"))
				{
					eventType = parser.next();
					alarm.checkedDays[4] = Boolean.parseBoolean(parser.getText());
				}
				else if (parser.getName().equals("Sat"))
				{
					eventType = parser.next();
					alarm.checkedDays[5] = Boolean.parseBoolean(parser.getText());
				}
				else if (parser.getName().equals("Sun"))
				{
					eventType = parser.next();
					alarm.checkedDays[6] = Boolean.parseBoolean(parser.getText());
				}
				else if (parser.getName().equals("on"))
				{
					eventType = parser.next();
					alarm.on = Boolean.parseBoolean(parser.getText());
				}
				else if (parser.getName().equals("id"))
				{
					eventType = parser.next();
					alarm.id = Integer.parseInt(parser.getText());
				}
			}
			else if (eventType == XmlPullParser.END_TAG)
			{
				if (parser.getName().equals("alarm"))
				{
					data.add(alarm);
				}
			}
			eventType = parser.next();
		}


		return data;
	}

	public void putXMLAlarm(OutputStream os, ArrayList<Alarm_Data> alarmDatas) throws IOException
	{
		XmlSerializer serializer = Xml.newSerializer();
		serializer.setOutput(os, "UTF-8");
		serializer.startDocument("UTF-8", true);

		for (Alarm_Data alarmData : alarmDatas)
		{
			serializer.startTag(null, "alarm");

			serializer.startTag(null, "hour");
			serializer.text(String.valueOf(alarmData.hour));
			serializer.endTag(null, "hour");

			serializer.startTag(null, "minute");
			serializer.text(String.valueOf(alarmData.minute));
			serializer.endTag(null, "minute");

			serializer.startTag(null, "repeat");
			serializer.text(String.valueOf(alarmData.repeat));
			serializer.endTag(null, "repeat");

			serializer.startTag(null, "Mon");
			serializer.text(String.valueOf(alarmData.checkedDays[0]));
			serializer.endTag(null, "Mon");

			serializer.startTag(null, "Tue");
			serializer.text(String.valueOf(alarmData.checkedDays[1]));
			serializer.endTag(null, "Tue");

			serializer.startTag(null, "Wed");
			serializer.text(String.valueOf(alarmData.checkedDays[2]));
			serializer.endTag(null, "Wed");

			serializer.startTag(null, "Thu");
			serializer.text(String.valueOf(alarmData.checkedDays[3]));
			serializer.endTag(null, "Thu");

			serializer.startTag(null, "Fri");
			serializer.text(String.valueOf(alarmData.checkedDays[4]));
			serializer.endTag(null, "Fri");

			serializer.startTag(null, "Sat");
			serializer.text(String.valueOf(alarmData.checkedDays[5]));
			serializer.endTag(null, "Sat");

			serializer.startTag(null, "Sun");
			serializer.text(String.valueOf(alarmData.checkedDays[6]));
			serializer.endTag(null, "Sun");

			serializer.startTag(null, "Sun");
			serializer.text(String.valueOf(alarmData.checkedDays[6]));
			serializer.endTag(null, "Sun");

			serializer.startTag(null, "on");
			serializer.text(String.valueOf(alarmData.on));
			serializer.endTag(null, "on");

			serializer.startTag(null, "id");
			serializer.text(String.valueOf(alarmData.id));
			serializer.endTag(null, "id");

			serializer.endTag(null, "alarm");
		}
		serializer.endDocument();

	}

	public ArrayList<Memorial_Data> pullXMLMemorial(InputStream inputStream) throws XmlPullParserException, IOException
	{
		ArrayList<Memorial_Data> data = new ArrayList<>();
		Memorial_Data memorialData = new Memorial_Data();
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(inputStream, "UTF8");

		int eventType = parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT)
		{
			if (eventType == XmlPullParser.START_TAG)
			{
				if (parser.getName().equals("memorial"))
				{
					memorialData = new Memorial_Data();
				}
				else if (parser.getName().equals("year"))
				{
					eventType = parser.next();
					memorialData.year = Integer.parseInt(parser.getText());
				}
				else if (parser.getName().equals("month"))
				{
					eventType = parser.next();
					memorialData.month = Integer.parseInt(parser.getText());
				}
				else if (parser.getName().equals("day"))
				{
					eventType = parser.next();
					memorialData.day = Integer.parseInt(parser.getText());
				}
				else if (parser.getName().equals("on"))
				{
					eventType = parser.next();
					memorialData.on = Boolean.parseBoolean(parser.getText());
				}
				else if (parser.getName().equals("title"))
				{
					eventType = parser.next();
					memorialData.title = parser.getText();
				}
			}
			else if (eventType == XmlPullParser.END_TAG)
			{
				if (parser.getName().equals("memorial"))
				{
					data.add(memorialData);
				}
			}
			eventType = parser.next();
		}


		return data;
	}

	public void putXMLMemorial(OutputStream os, ArrayList<Memorial_Data> data) throws IOException
	{
		XmlSerializer serializer = Xml.newSerializer();
		serializer.setOutput(os, "UTF-8");
		serializer.startDocument("UTF-8", true);

		for (Memorial_Data memorialData : data)
		{
			serializer.startTag(null, "memorial");

			serializer.startTag(null, "year");
			serializer.text(String.valueOf(memorialData.year));
			serializer.endTag(null, "year");

			serializer.startTag(null, "month");
			serializer.text(String.valueOf(memorialData.month));
			serializer.endTag(null, "month");

			serializer.startTag(null, "day");
			serializer.text(String.valueOf(memorialData.day));
			serializer.endTag(null, "day");

			serializer.startTag(null, "on");
			serializer.text(String.valueOf(memorialData.on));
			serializer.endTag(null, "on");

			serializer.startTag(null, "title");
			serializer.text(String.valueOf(memorialData.title));
			serializer.endTag(null, "title");

			serializer.endTag(null, "memorial");
		}
		serializer.endDocument();

	}
}

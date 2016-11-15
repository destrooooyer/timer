package shepherd.timer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Date;

/**
 * Created by DESTR on 2016/11/15.
 */

public class Set_memorial_day_activity extends AppCompatActivity
{
	Toolbar toolbar;
	DatePicker datePicker;
	Button cancelButton;
	Button confirmButton;
	EditText title;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set_memorial_day);


		toolbar = (Toolbar) findViewById(R.id.set_memorial_toolbar);
		setSupportActionBar(toolbar);

		datePicker = (DatePicker) findViewById(R.id.set_memorial_datepicker);
		confirmButton = (Button) findViewById(R.id.set_memorial_confirm);
		cancelButton = (Button) findViewById(R.id.set_memorial_cancel);
		title=(EditText)findViewById(R.id.set_memorial_title);

		confirmButton.setOnClickListener(confirmOnClickListener);
		cancelButton.setOnClickListener(cancelOnClickListener);

	}

	View.OnClickListener confirmOnClickListener = new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{

			Intent result = new Intent();

			result.putExtra("year",datePicker.getYear());
			result.putExtra("month",datePicker.getMonth());
			result.putExtra("day",datePicker.getDayOfMonth());
			result.putExtra("title",title.getText().toString());


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

}

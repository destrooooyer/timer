package shepherd.timer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import static java.lang.Math.min;

/**
 * Created by DESTR on 2016/10/27.
 */

///略坑，暂时搁置用现成的->_->

public class My_analog_clock extends View
{
	Bitmap dial;
	Bitmap hourHand;
	Bitmap minuteHand;

	Paint paint;


	public My_analog_clock(Context context)
	{
		super(context);
		init();
	}

	public My_analog_clock(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init();
	}

	public My_analog_clock(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		init();
	}

	private void init()
	{
		dial = BitmapFactory.decodeResource(getResources(), R.drawable.dial);
		hourHand = BitmapFactory.decodeResource(getResources(), R.drawable.hour_hand);
		minuteHand = BitmapFactory.decodeResource(getResources(), R.drawable.minute_hand);
	}

	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);

		//dDial.setBounds(50 - dial.getWidth() / 2, 50 - dial.getHeight() / 2, 50 + dial.getWidth() / 2, 50 + dial.getHeight() / 2);
		//dDial.draw(canvas);
		//canvas.save();
		canvas.scale(0.5f, 0.5f);
		canvas.save();
		canvas.translate(getWidth() / 2 - dial.getWidth() / 4, getHeight() / 2 - dial.getHeight() / 4);
		canvas.drawBitmap(dial, 0, 0, paint);
		canvas.restore();
		//canvas.translate(getWidth() / 2 - minuteHand.getWidth() / 4, getHeight() / 2 - minuteHand.getHeight() / 4);
		canvas.drawBitmap(minuteHand, 0, 0, paint);
	}

}

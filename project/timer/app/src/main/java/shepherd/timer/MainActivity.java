package shepherd.timer;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
	private Toolbar toolbar;
	private DrawerLayout drawerLayout;
	private NavigationView navigationView;

	private ActionBarDrawerToggle drawerToggle;
	private FragmentManager fm;
	private FragmentTransaction transaction;

	private Fragment_alarm_clock fragment_alarm_clock;
	private Fragment_count_down fragment_count_down;
	private Fragment_time fragment_time;
	private Fragment_memorial fragment_memorial;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		toolbar = (Toolbar) findViewById(R.id.toolbar);
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		navigationView=(NavigationView) findViewById(R.id.nav_view);

		toolbar.setTitle(R.string.time);
		setSupportActionBar(toolbar);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		fm=getFragmentManager();
		transaction=fm.beginTransaction();

		drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open,R.string.close)
		{
			@Override
			public void onDrawerOpened(View drawerView)
			{
				super.onDrawerOpened(drawerView);
			}

			@Override
			public void onDrawerClosed(View drawerView)
			{
				super.onDrawerClosed(drawerView);
			}
		};
		drawerToggle.syncState();

		navigationView.setNavigationItemSelectedListener(this);

		fragment_alarm_clock=new Fragment_alarm_clock();
		fragment_count_down=new Fragment_count_down();
		fragment_memorial=new Fragment_memorial();
		fragment_time=new Fragment_time();

		transaction.replace(R.id.frame_layout,fragment_time);
		transaction.commit();


	}

	@SuppressWarnings("StatementWithEmptyBody")
	@Override
	public boolean onNavigationItemSelected(MenuItem item)
	{
		int id=item.getItemId();
		if(id==R.id.menu_time)
		{
			fm=getFragmentManager();
			transaction=fm.beginTransaction();
			transaction.replace(R.id.frame_layout,fragment_time);
			transaction.commit();
			toolbar.setTitle(R.string.time);
		}
		else if(id==R.id.menu_alarm_clock)
		{
			fm=getFragmentManager();
			transaction=fm.beginTransaction();
			transaction.replace(R.id.frame_layout,fragment_alarm_clock);
			transaction.commit();
			toolbar.setTitle(R.string.alarm_clock);
		}
		else if(id==R.id.menu_count_down)
		{
			fm=getFragmentManager();
			transaction=fm.beginTransaction();
			transaction.replace(R.id.frame_layout,fragment_count_down);
			transaction.commit();
			toolbar.setTitle(R.string.count_down);
		}
		else if(id==R.id.menu_memorial_day)
		{
			fm=getFragmentManager();
			transaction=fm.beginTransaction();
			transaction.replace(R.id.frame_layout,fragment_memorial);
			transaction.commit();
			toolbar.setTitle(R.string.memorial_day);
		}

		drawerLayout.closeDrawer(GravityCompat.START);
		return true;
	}

}

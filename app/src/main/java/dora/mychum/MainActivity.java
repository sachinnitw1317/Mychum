package dora.mychum;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import dora.mychum.Alarm.AlarmFragment;
import dora.mychum.Timetable.AttendanceFragment;
import dora.mychum.Timetable.ChangeTimeFragment;
import dora.mychum.Timetable.CoursesFragment;
import dora.mychum.SettingsFragment;
import dora.mychum.Timetable.TimetableFragment;


public class MainActivity extends ActionBarActivity
        implements TimetableFragment.OnFragmentInteractionListener, NavigationDrawerCallbacks{

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private Toolbar mToolbar;
    private Menu current_menu;
    Fragment frag = null;
    FragmentManager fm= getFragmentManager();
    Button go;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);
        go= (Button) findViewById(R.id.go);
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent I=new Intent(MainActivity.this,AndroidGPSTrackingActivity.class);
                startActivity(I);
            }
        });


        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.fragment_drawer);


        // Set up the drawer.
        mNavigationDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), mToolbar);
        // populate the navigation drawer
        mNavigationDrawerFragment.setUserData("Devendra Dora", "dev.tech24@gmail.com", BitmapFactory.decodeResource(getResources(), R.drawable.avatar));



    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the add_alarm content by replacing fragments




        switch (position){

            case 0:

                break;
            case 1:
                frag = CoursesFragment.newInstance("Alarm", "Test");
                break;
            case 2 :
               frag = TimetableFragment.newInstance("dev", "0");
                break;
            case 3 :
                frag = CoursesFragment.newInstance("dev", "dora");
                break;
            case 4:
                frag = AttendanceFragment.newInstance("dev", "dora");
                break;
            case 5:
                frag = AlarmFragment.newInstance("dev","alarm");
                break;
            case 6 :
                frag = SettingsFragment.newInstance("settings","dora");
                Toast.makeText(this, "Menu item selected -> " + position, Toast.LENGTH_SHORT).show();
                break;


            default:

        }

        if(frag != null)
            fm.beginTransaction().replace(R.id.container, frag).commit();

        }




    @Override
    public void onBackPressed() {
        if (mNavigationDrawerFragment.isDrawerOpen())
            mNavigationDrawerFragment.closeDrawer();
        else
            super.onBackPressed();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        current_menu = menu;
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            if(frag != null && frag instanceof TimetableFragment)
                getMenuInflater().inflate(R.menu.menu_timetable, menu);
            else
                getMenuInflater().inflate(R.menu.main, menu);
            return true;
        }
        if(frag != null && frag instanceof TimetableFragment)
        getMenuInflater().inflate(R.menu.menu_timetable, menu);
        else
            getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        switch (id)
        {
            case R.id.action_settings :
                return true;
            case R.id.bt_changeTime :
                frag = ChangeTimeFragment.newInstance("dev", "dora");
                //fm.beginTransaction().replace(R.id.container, frag).commit();
                //getMenuInflater().inflate(R.menu.menu_back, current_menu);
               // super.onCreateOptionsMenu(current_menu);



              // break;
            case R.id.bt_editTimetable :
                frag = TimetableFragment.newInstance("dev","1");
                getMenuInflater().inflate(R.menu.menu_back, current_menu);
                break;
            case R.id.bt_goBack :
                android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
                fm.popBackStack();
        }

        if(frag != null)
            fm.beginTransaction().replace(R.id.container, frag).commit();


        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onFragmentInteraction(Uri uri){

    }



}

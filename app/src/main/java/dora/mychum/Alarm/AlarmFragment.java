package dora.mychum.Alarm;

import android.app.AlarmManager;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import dora.mychum.R;

import static java.lang.Integer.parseInt;

/**
 * Created by G S ABHAY on 08-06-2015.
 */
public class AlarmFragment extends Fragment {


    public AlarmFragment() {
        super();
    }


    private PendingIntent pendingIntent[] = new PendingIntent[100];
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    TimePicker time;
    TextView tvalarms;
    CalendarView calView;
    EditText snoozeMin;
    DatePicker alarmdate;
    RadioButton snooze,math;
    CheckBox weekdays[] = new CheckBox[7];
    static int Alarm_Count = 0;
    CheckBox snoozeCheck;
    static boolean snoozeOn = true;
    public AlarmManager alarmManager;
    Intent myIntent;
    Button viewAlarms;
    AlarmDB db;
    int days[] = {R.id.day0,R.id.day1,R.id.day2,R.id.day3,R.id.day4,R.id.day5,R.id.day6};
    public static int snooze_min = 10;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_alarm, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        //super.onActivityCreated(savedInstanceState);
        View v = getView();
        Button buttonStart = (Button)v.findViewById(R.id.startalarm);
        Button buttonCancel = (Button)v.findViewById(R.id.cancelalarm);
        viewAlarms = (Button)v.findViewById(R.id.viewAlarms);
        time = (TimePicker) v.findViewById(R.id.timePicker);
        tvalarms = (TextView) v.findViewById(R.id.tvalarms);
        tvalarms.setText("No Alarms Scheduled...");
        // calView = (CalendarView) findViewById(R.id.calendarView)
        snoozeMin = (EditText) v.findViewById(R.id.snoozemin);
        alarmdate = (DatePicker) v.findViewById(R.id.alarmdate);
        snooze = (RadioButton) v.findViewById(R.id.radioButtonSnooze);
        math = (RadioButton) v.findViewById(R.id.radioButtonMath);
        snoozeCheck = (CheckBox) v.findViewById(R.id.checksnooze);
        snoozeOn = snoozeCheck.isChecked();
        db = new AlarmDB(getActivity());

        for(int i=0; i<7; i++)
            weekdays[i] = (CheckBox) v.findViewById(days[i]);
        alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

        viewAlarms.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View view) {

                Intent i = new Intent(getActivity(), AddAlarm.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(i);
            }
        });


        buttonStart.setOnClickListener(new Button.OnClickListener() {
            int alarm_count = 0;

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub


                if (snooze.isChecked())

                    myIntent = new Intent(getActivity(), SnoozeClass.class);
                else
                    myIntent = new Intent(getActivity(), MathClass.class);

                tvalarms.setText("");
//             for (int i=0; i<7; i++)
//                 if(weekdays[i].isChecked())
//                     setAlarmForDay(i);
                Alarm_Count =  db.getLastID() +1 ;

                setAlarmForDay(Alarm_Count);



            }});

        buttonCancel.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                for (int i=0; i<7; i++)
                    if(weekdays[i].isChecked())
                    {
                        alarmManager.cancel(pendingIntent[i]);
                        pendingIntent[i].cancel();
                        //addIntents();
                    }


                // Tell the user about what we did.
                //tvalarms.setText("No Alarms Scheduled...");
                Toast.makeText(getActivity(), "Cancelled Alarm + ", Toast.LENGTH_LONG).show();


            }});
    }

    public static AlarmFragment newInstance(String param1, String param2) {
        AlarmFragment fragment = new AlarmFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public  void setAlarmForDay(int day)
    {
        pendingIntent[day] = PendingIntent.getBroadcast(getActivity(), day, myIntent, 0);



        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int hour = time.getCurrentHour();
        int min = time.getCurrentMinute();
        calendar.set(alarmdate.getYear(), alarmdate.getMonth(), alarmdate.getDayOfMonth());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
        // calendar.set(Calendar.DAY_OF_WEEK, day);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        if(snoozeOn)
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    1000 * 60 * snooze_min, pendingIntent[day]);
        else
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    pendingIntent[day]);

        //Toast.makeText(AndroidAlarmService.this, "Start Alarm " + alarm_count + " at " + time.getCurrentHour() + " " +  time.getCurrentMinute() + " PM on " + calendar.toString() , Toast.LENGTH_LONG).show();
        Toast.makeText(getActivity(), "Set on " + hour + " " + min + " " + calendar.getTime().toString(), Toast.LENGTH_LONG).show();

        tvalarms.append("Alarm SCheduled at " + time.getCurrentHour() + " " + time.getCurrentMinute() + " Day " + day + "\n");
        Date date = calendar.getTime();
        String formattedDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(date);

        long k =  db.insertAlarm(day,hour,min,formattedDate,snoozeOn?1:0,snooze_min,snooze.isChecked()?0:1,1);
        if(k > 0)
            Toast.makeText(getActivity(), "Inserted ", Toast.LENGTH_LONG).show();
        // addIntents();
    }


    public  void changeSnooze(View v)
    {
        snooze_min = parseInt(String.valueOf(snoozeMin.getText()));
        Toast.makeText(getActivity(), "Snooze changed to " + snooze_min, Toast.LENGTH_SHORT).show();
    }
    public void addIntents()
    {
        tvalarms.setText("");
        for(int i=0; i<7; i++)
            if(pendingIntent[i] != null)
                tvalarms.append("PendingIntent " + i + "\n");
    }

}

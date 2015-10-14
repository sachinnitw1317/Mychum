package dora.mychum.Alarm;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static java.lang.Integer.parseInt;
import dora.mychum.R;
/**
 * Created by G S ABHAY on 03-06-2015.
 */
public class AddAlarm extends Activity {
    ListView listActive,listInActive ;
    AlarmDB db;


    ArrayList<ContentValues> CV = new ArrayList<ContentValues>();
    List<String> active = new ArrayList<String>();
    List<String> inactive = new ArrayList<String>();

    List<Integer> activeId = new ArrayList<Integer>();
    List<Integer> inactiveId = new ArrayList<Integer>();
    ArrayAdapter<String> adapterActive,adapterInActive;
    AlertDialog.Builder myAlert;
    AlertDialog alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_list);

        // Get ListView object from xml
        db = new AlarmDB(this);
        listActive = (ListView) findViewById(R.id.listActive);
        listInActive = (ListView) findViewById(R.id.listInActive);

        CV = db.getAllAlarms();


        for (int i = 0; i < CV.size(); i++) {
            ContentValues row = CV.get(i);
            Toast.makeText(AddAlarm.this, row.toString(), Toast.LENGTH_SHORT).show();

            StringBuffer S = new StringBuffer();

            S.append(row.get("ID") + " ");
            S.append(row.get("DATE") + " ");
            int alarmActive = row.getAsInteger("ACTIVE");




            if(alarmActive == 1)
            {
                active.add(S.toString());
                activeId.add(parseInt(String.valueOf(row.get("ID"))));
            }

            else
            {
                inactive.add(S.toString());
               inactiveId.add(parseInt(String.valueOf(row.get("ID"))));
            }


        }






       // inactive.add("NO Inactive Alarms");
        adapterActive = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_2, android.R.id.text1, active);
        adapterInActive = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_2, android.R.id.text1, inactive);


        // Assign adapter to ListView
        listActive.setAdapter(adapterActive);
        listInActive.setAdapter(adapterInActive);


        // ListView Item Click Listener
        listActive.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition = position;

                // ListView Clicked item value
                final String itemValue = (String) listActive.getItemAtPosition(position);


                final int pos = position;
                myAlert = new AlertDialog.Builder(AddAlarm.this);
                myAlert.setTitle("Delete this ALarm from the List ? \n ");
                LayoutInflater inflater = getLayoutInflater();
                View dialoglayout = inflater.inflate(R.layout.alarm_delete, null);

                myAlert.setView(dialoglayout);
                final RadioButton turnoff, delete;
                turnoff = (RadioButton) dialoglayout.findViewById(R.id.offAlarm);
                delete = (RadioButton) dialoglayout.findViewById(R.id.delAlarm);
                myAlert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {


                        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                        Intent myIntent = new Intent(AddAlarm.this, SnoozeClass.class);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(AddAlarm.this, activeId.get(pos), myIntent, 0);
                        alarmManager.cancel(pendingIntent);

                        if (delete.isChecked()) {
                            db.deleteAlarm(activeId.get(pos));
                            Toast.makeText(AddAlarm.this, "Deleted the Alarm", Toast.LENGTH_SHORT).show();
                            active.remove(pos);
                            adapterActive.notifyDataSetChanged();
                        }
                       else if (turnoff.isChecked()) {
                            int key = activeId.get(pos);
                            active.remove(pos);
                            inactiveId.add(key);
                            activeId.remove(pos);
                            inactive.add(itemValue);
                            db.updateActive(key,0);


                            adapterActive.notifyDataSetChanged();
                            adapterInActive.notifyDataSetChanged();
                            Toast.makeText(AddAlarm.this, "Turned off the Alarm", Toast.LENGTH_SHORT).show();

                        }


                    }
                });

                myAlert.setNegativeButton("No", new DialogInterface.OnClickListener()

                        {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        }

                );
                alert = myAlert.create();
                alert.show();
            }
        });


        listInActive.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                int itemPosition = position;

                // ListView Clicked item value
                final String itemValue = (String) listInActive.getItemAtPosition(position);


                final int pos = position;
                myAlert = new AlertDialog.Builder(AddAlarm.this);
                myAlert.setTitle("Turn On the ALarm? ");


                myAlert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {


                        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                        Intent myIntent = new Intent(AddAlarm.this, SnoozeClass.class);
                        int key = inactiveId.get(pos);
                        ContentValues row = db.getData(key);
                        int mode = parseInt(String.valueOf(row.get("SNOOZE_MODE")));
                        if (mode == 0)
                            myIntent = new Intent(AddAlarm.this, SnoozeClass.class);
                        else
                            myIntent = new Intent(AddAlarm.this, MathClass.class);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(AddAlarm.this, key, myIntent, 0);

                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(System.currentTimeMillis());
                        int hour = parseInt(String.valueOf(row.get("HOUR")));
                        int min = parseInt(String.valueOf(row.get("MINUTE")));
                        String dateString = String.valueOf(row.get("DATE"));
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                        Date date = new Date();
                        try {
                            date = dateFormat.parse(dateString);
                        } catch (ParseException e) {
                            // TODO Auto-generated catch block
                            Toast.makeText(AddAlarm.this, "Date Parsing Error", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }

                        calendar.set(date.getYear(), date.getMonth(), date.getDay());
                        calendar.set(Calendar.HOUR_OF_DAY, hour);
                        calendar.set(Calendar.MINUTE, min);
                        // calendar.set(Calendar.DAY_OF_WEEK, day);
                        calendar.set(Calendar.SECOND, 0);
                        calendar.set(Calendar.MILLISECOND, 0);
                        if (parseInt(String.valueOf(row.get("SNOOZE"))) == 1)
                            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                                    1000 * 60 * parseInt(String.valueOf(row.get("SNOOZE_COUNT"))), pendingIntent);
                        else
                            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                                    pendingIntent);

                        inactive.remove(pos);
                        activeId.add(key);
                        inactiveId.remove(pos);
                        active.add(itemValue);
                        db.updateActive(key, 1);


                        adapterActive.notifyDataSetChanged();
                        adapterInActive.notifyDataSetChanged();
                        Toast.makeText(AddAlarm.this, "Turned on the Alarm", Toast.LENGTH_SHORT).show();

                    }
                });

                myAlert.setNegativeButton("No", new DialogInterface.OnClickListener()

                        {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        }

                );
                alert = myAlert.create();
                alert.show();

            }

        });

    }
}
package dora.mychum.Timetable;


import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import dora.mychum.DatabaseHelper;
import dora.mychum.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CoursesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChangeTimeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String slotsCount = "slotsCount";
    SharedPreferences sharedpreferences;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View fragView;
    Activity activity;
    DatabaseHelper dbhelper_obj;
    SQLiteDatabase db_obj;
    LinearLayout layout;
    Button bt_time[][];


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CoursesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChangeTimeFragment newInstance(String param1, String param2) {
        ChangeTimeFragment fragment = new ChangeTimeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ChangeTimeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        dbhelper_obj= new DatabaseHelper(getActivity());
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         fragView =  inflater.inflate(R.layout.change_time, container, false);
        sharedpreferences = this.getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        layout = (LinearLayout) fragView.findViewById(R.id.layout_time);
        int slotCount = sharedpreferences.getInt(slotsCount,10);
        bt_time = new Button[slotCount][];
        for (int i=0; i<slotCount; i++)
        {
            bt_time[i] = new Button[2];
            TextView tv = new TextView(this.getActivity());
            tv.setText("Slot " + (i + 1) + " Duration : ");
            tv.setTypeface(Typeface.DEFAULT_BOLD);
            layout.addView(tv);

            bt_time[i][0] = new Button(this.getActivity());

            String times[];
            times = dbhelper_obj.getTime(i);
            if(times == null)
            {
                bt_time[i][0].setText("Start Time ( 8:00 )");
                dbhelper_obj.addTime(i,"8:00","9:00");
            }

            else
            {
                bt_time[i][0].setText("Start Time ( " + times[0] + " )");
            }



            bt_time[i][0].setTag(i + "," + 0);
            layout.addView(bt_time[i][0]);
             activity = this.getActivity();


            bt_time[i][0].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    displayTimeDialog("Start Time",view,0);

                }
            });
            bt_time[i][1] = new Button(this.getActivity());
            if(times == null)
                bt_time[i][1].setText("End Time ( 9:00 )");
            else
            {
                bt_time[i][1].setText("End Time ( " + times[1] + " )");
            }

            bt_time[i][1].setTag(i + "," + 1);
            bt_time[i][1].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    displayTimeDialog("End Time", view,1);



                }
            });

            layout.addView(bt_time[i][1]);


        }

        return fragView;
    }

    public void displayTimeDialog(final String type,final View view, final int time_type)
    {
        TimePickerDialog timeDialog;
        Calendar mcurrentTime = Calendar.getInstance();
        final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        final int minute = mcurrentTime.get(Calendar.MINUTE);

        timeDialog = new TimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener()
        {

            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                Button btn = (Button) fragView.findViewWithTag(view.getTag());
                String time = selectedHour + ":" + selectedMinute;

                String tag = (String) view.getTag();
                int slot_num = Integer.parseInt(tag.substring(0, tag.indexOf(',')));
                long res = dbhelper_obj.updateTime(slot_num, time, time_type);
                if (res < 0) {
                    Toast.makeText(activity, "Failed to update", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(activity, "Updated", Toast.LENGTH_SHORT).show();

                btn.setText(type +  " ( " + selectedHour + ":" + selectedMinute + " )" );

            }
        },hour,minute,true);
        timeDialog.setTitle("Select Time");
        timeDialog.show();


    }






}

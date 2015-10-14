package dora.mychum.Timetable;


import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import dora.mychum.DatabaseHelper;
import dora.mychum.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CoursesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String slotsCount = "slotsCount";
    SharedPreferences sharedpreferences;
    Button update;
    CheckBox cb_days[] = new CheckBox[7];
    EditText et_slots;
    int cb_ids[] = {R.id.cb_mon, R.id.cb_tue,R.id.cb_wed,R.id.cb_thu,R.id.cb_fri,R.id.cb_sat,R.id.cb_sun};
    String day_names[] = {"Mon","Tue","Wed","Thu","Fri","Sat","Sun"};
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    DatabaseHelper dbhelper_obj;
    SQLiteDatabase db_obj;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CoursesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public SettingsFragment() {
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
        View fragView =  inflater.inflate(R.layout.select_days, container, false);
        Toast.makeText(SettingsFragment.this.getActivity(), "Entered", Toast.LENGTH_SHORT).show();
        sharedpreferences = this.getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedpreferences.edit();
        for (int i=0; i<7; i++)
        {
            cb_days[i] = (CheckBox) fragView.findViewById(cb_ids[i]);
            String res = sharedpreferences.getString(day_names[i],null);
            if(res!= null && res.equals("true"))
                cb_days[i].setChecked(true);
            else
                cb_days[i].setChecked(false);
        }
        update = (Button) fragView.findViewById(R.id.bt_updateDays);
        et_slots = (EditText) fragView.findViewById(R.id.et_slotsCount);
        et_slots.setText(String.valueOf(sharedpreferences.getInt(slotsCount,10)));
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i=0; i<7; i++)
                {
                    String res = String.valueOf(cb_days[i].isChecked());
                    editor.putString(day_names[i],res);
                }
                editor.commit();
                editor.putInt(slotsCount, Integer.parseInt(et_slots.getText().toString()));
                editor.commit();
                Toast.makeText(SettingsFragment.this.getActivity(), "Updated", Toast.LENGTH_SHORT).show();
            }
        });

        return fragView;
    }




}

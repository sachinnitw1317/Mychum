package dora.mychum.Timetable;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import dora.mychum.DatabaseHelper;
import dora.mychum.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddCourseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddCourseFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    DatabaseHelper dbhelper_obj;
    SQLiteDatabase db_obj;
    Cursor cursor;
    EditText courseName, abbr, credits;
    Button addCourse;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CoursesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddCourseFragment newInstance(String param1, String param2) {
        AddCourseFragment fragment = new AddCourseFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public AddCourseFragment() {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragView =  inflater.inflate(R.layout.course_add, container, false);
        courseName = (EditText) fragView.findViewById(R.id.et_course);
        abbr = (EditText) fragView.findViewById(R.id.et_abbr);
        credits = (EditText) fragView.findViewById(R.id.et_credits);
        addCourse = (Button) fragView.findViewById(R.id.bt_addCourse);
        addCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cName,cAbbr;
                int cCredits;
                cName = courseName.getText().toString();
                cAbbr = abbr.getText().toString();
                if(cAbbr.isEmpty())
                    cAbbr = cName.substring(0,3);
                cCredits = Integer.parseInt(credits.getText().toString());
               long res =  dbhelper_obj.addCourse(cName,cAbbr,cCredits);
                if(res < 0)
                {
                    Toast.makeText(AddCourseFragment.this.getActivity(), "Failed to add Course", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(AddCourseFragment.this.getActivity(), "Added Course succesfully!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return fragView;
    }




}

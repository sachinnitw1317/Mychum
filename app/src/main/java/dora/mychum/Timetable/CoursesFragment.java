package dora.mychum.Timetable;


import android.app.FragmentManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import dora.mychum.DatabaseHelper;
import dora.mychum.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CoursesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CoursesFragment extends Fragment {
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
    ListView courseList;
    ImageButton addButton;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CoursesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CoursesFragment newInstance(String param1, String param2) {
        CoursesFragment fragment = new CoursesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public CoursesFragment() {
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
        View fragView =  inflater.inflate(R.layout.courses_fragment, container, false);
        courseList = (ListView) fragView.findViewById(R.id.lv_courses);
        addButton = (ImageButton) fragView.findViewById(R.id.Courses_addbutton);
        final FragmentManager fragManager = getFragmentManager();
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               AddCourseFragment addFrag = AddCourseFragment.newInstance("course1", "DS");

                fragManager.beginTransaction().replace(R.id.container, addFrag,null).addToBackStack(null).commit();
            }
        });
        String[] from = {"CNAME","TOT_CLASSES"};
        int[] to = {R.id.tv_cname,R.id.tv_tot_classes};
        cursor = dbhelper_obj.getAllData("Courses");

        SimpleCursorAdapter curAdapter = new SimpleCursorAdapter(getActivity(),R.layout.course_item_row,cursor,from,to);
        courseList.setAdapter(curAdapter);

            Log.d("VALUE",String.valueOf(courseList.getItemAtPosition(0)));
        courseList.invalidate();
        return fragView;
    }




}

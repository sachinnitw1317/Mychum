        package dora.mychum;

        import android.content.ContentValues;
        import android.content.Context;
        import android.database.Cursor;
        import android.database.DatabaseUtils;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;
        import android.util.Log;
        import java.util.ArrayList;
        import java.util.HashMap;

/**
 * Created by Devendra Dora on 6/4/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MyChum.db";
    public static final int    DATABASE_VERSION = 1;

    private HashMap hp;

    public DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub

        db.execSQL( "CREATE TABLE COURSES (" +
                        " CID INTEGER PRIMARY KEY,"+
                        " CNAME TEXT NOT NULL,"+
                        " CABBR TEXT ,"+
                        " CREDITS INTEGER DEFAULT 0,"+
                        " TOT_CLASSES INTEGER DEFAULT 0 )"
        );

        db.execSQL("CREATE TABLE TIMES (" +
                        "TIMEID INTEGER PRIMARY KEY," +
                        "START_TIME TEXT ," +
                        "END_TIME TEXT )"

        );

        db.execSQL( "CREATE TABLE TIMETABLE ("+
                        "DAY INTEGER NOT NULL,"+
                        "CID INTEGER NOT NULL,"+
                        "TIMEID INTEGER NOT NULL,"+

                        "FOREIGN KEY(CID)REFERENCES COURSES(CID) ON DELETE CASCADE," +
                        "FOREIGN KEY(TIMEID)REFERENCES TIMES(TIMEID) ON DELETE CASCADE)"
        );


        db.execSQL( "CREATE TABLE ATTENDANCE ("+
                        "CID INTEGER NOT NULL,"+
                        "ABSENT_DATE TEXT NOT NULL,"+
                        "FOREIGN KEY(CID) REFERENCES  COURSES(CID) ON DELETE CASCADE)"
        );

        db.execSQL("CREATE TABLE TASKS (" +
                        "TID INTEGER PRIMARY KEY," +
                        "DESC TEXT NOT NULL," +
                        "DATE TEXT NOT NULL ," +
                        "TIME TEXT NOT NULL )"
        );




        //db = this.getWritableDatabase();
        ContentValues cval = new ContentValues();
        cval.put("CNAME", "devcomputer");
        cval.put("CNAME", "Computer Networks");
        cval.put("CNAME", "DS");
        cval.put("CNAME", "DBMS");

        db.insert("COURSES", null, cval);
        Log.d("db created dora","d");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        // if (oldVersion == 1) {
        // }
        db.execSQL("DROP TABLE IF EXISTS COURSES");
        db.execSQL("DROP TABLE IF EXISTS TIMETABLE");
        db.execSQL("DROP TABLE IF EXISTS ATTENDANCE");
        db.execSQL("DROP TABLE IF EXISTS TASKS");
        db.execSQL("DROP TABLE IF EXISTS TIMES");
        onCreate(db);
    }

    //Course
    public long addCourse(String cname,String cAbbr, int cCredits){


        SQLiteDatabase db = this.getWritableDatabase();
      //  this.onUpgrade(db,0,1);
        ContentValues cval = new ContentValues();
        cval.put("CNAME", cname);
        cval.put("CABBR", cAbbr);
        cval.put("CREDITS", cCredits);
       return db.insert("COURSES",null,cval);


    }

    //Time
    public long addTime(int id, String sTime, String eTime)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cval = new ContentValues();
        cval.put("TIMEID", id);
        cval.put("START_TIME", sTime);
        cval.put("END_TIME", eTime);

        return db.insert("TIMES",null,cval);
    }

    public long updateTime(int id, String time, int type)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cval = new ContentValues();
        cval.put("TIMEID", id);
        if(type == 0) //Start time
            cval.put("START_TIME", time);
        else  //End Time
            cval.put("END_TIME", time);
       return  db.update("TIMES", cval, "TIMEID = ?", new String[]{Integer.toString(id)});
    }

    public String[] getTime(int id)
    {

        SQLiteDatabase db = this.getReadableDatabase();
      // this.onUpgrade(db,0,1);
        Cursor cur = db.rawQuery("select START_TIME, END_TIME  from TIMES WHERE TIMEID = " + id, null);
        if(cur.getCount() == 0)
            return  null;
        String[] times = new String[2];
        cur.moveToFirst();
        times[0]  = cur.getString(0);
        times[1] = cur.getString(1);
        return times;
    }


    public void deleteAllTimes()
    {
        SQLiteDatabase db = this.getWritableDatabase();
         db.execSQL("DELETE FROM  TIMES");
    }



    public Cursor getAllData(String tablename){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("select rowid _id, * from " + tablename, null);

        //Cursor cur = db.query(tablename,null,null,null,null,null,null);
        //  Log.d("devendra",cur.toString());
        return cur;
    }


//TimeTable
    public Cursor getTimetableCell(int slot_id, int day)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("select  CABBR from  TIMETABLE T ,COURSES C WHERE TIMEID = " +slot_id + " AND DAY = " + day  + " AND T.CID = C.CID" ,null);
        if(c == null || c.getCount() == 0)
            return null;
        return c;
    }

    public long addTimetableCell(int slot_id, int day, int  c_id)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cval = new ContentValues();
        cval.put("DAY", day);
        cval.put("CID", c_id);
        cval.put("TIMEID", slot_id);

        return db.insert("TIMETABLE",null,cval);
    }

    public long updateTimetableCell(int slot_id, int day, int  c_id)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cval = new ContentValues();
        cval.put("CID", c_id);
        return  db.update("TIMETABLE", cval, "TIMEID = ? AND DAY = ?", new String[]{Integer.toString(slot_id), Integer.toString(day)});
    }



    /*

    public boolean insertContact  (String name, String phone, String email, String street,String place)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("phone", phone);
        contentValues.put("email", email);
        contentValues.put("street", street);
        contentValues.put("place", place);
        db.insert("contacts", null, contentValues);
        return true;
    }

    public Cursor getData(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from contacts where id="+id+"", null );
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, CONTACTS_TABLE_NAME);
        return numRows;
    }

    public boolean updateContact (Integer id, String name, String phone, String email, String street,String place)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("phone", phone);
        contentValues.put("email", email);
        contentValues.put("street", street);
        contentValues.put("place", place);
        db.update("contacts", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deleteContact (Integer id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("contacts",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    public ArrayList<String> getAllCotacts()
    {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from contacts", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_NAME)));
            res.moveToNext();
        }
        return array_list;
    }
    */
}



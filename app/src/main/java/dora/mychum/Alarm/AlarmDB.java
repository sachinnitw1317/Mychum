package dora.mychum.Alarm;

/**
 * Created by G S ABHAY on 03-06-2015.
 */


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static java.lang.Integer.parseInt;

public class AlarmDB extends SQLiteOpenHelper {

    public static final String TABLE_NAME       = "ALARMS";
    public static final String COLUMN_ID        = "ID";
    public static final String COLUMN_HOUR      = "HOUR";
    public static final String COLUMN_MINUTE    = "MINUTE";
    public static final     String COLUMN_DATE      = "DATE";


    public static final String COLUMN_SNOOZE_ON      = "SNOOZE";
    public static final String COLUMN_SNOOZE_COUNT     = "SNOOZE_COUNT";
    public static final String COLUMN_SNOOZE_MODE      = "SNOOZE_MODE";
    public static final String COLUMN_ACTIVE      = "ACTIVE";
    private static final int DATABASE_VERSION   = 4;
    private static final String DATABASE_NAME   = "alarms.db";
    private static final String TABLE_CREATE    =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID        + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_HOUR      + " INTEGER NOT NULL, " +
                    COLUMN_MINUTE    + " INTEGER NOT NULL, " +
                    COLUMN_DATE      + " DATE, " +
                    COLUMN_SNOOZE_ON     + " INTEGER, " +
                    COLUMN_SNOOZE_COUNT      + " INTEGER,  " +
                    COLUMN_SNOOZE_MODE + " INTEGER,  " +
                     COLUMN_ACTIVE+ " INTEGER DEFAULT 0 );";
    private HashMap hp;

    public AlarmDB(Context context)
    {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub

        db.execSQL(
                TABLE_CREATE
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long insertAlarm  (int id, int hour, int minute, String date,int snooze_on , int snooze_count, int snooze_mode,int active)
    {
        SQLiteDatabase db = this.getWritableDatabase();
       // onUpgrade(db,DATABASE_VERSION,DATABASE_VERSION);
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID, id);
        contentValues.put(COLUMN_HOUR, hour);
        contentValues.put(COLUMN_MINUTE, minute);
        contentValues.put(COLUMN_DATE, date.toString());
        contentValues.put(COLUMN_SNOOZE_ON, snooze_on);
        contentValues.put(COLUMN_SNOOZE_COUNT, snooze_count);
        contentValues.put(COLUMN_SNOOZE_MODE, snooze_mode);
        contentValues.put(COLUMN_ACTIVE, active);

        return db.insert(TABLE_NAME, null, contentValues);

    }

    public int getLastID()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select MAX(ID) from " + TABLE_NAME, null);
        res.moveToFirst();
        if(res.isNull(0))
            return  0;
       return  parseInt(res.getString(0));
    }

    public ContentValues getData(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select * from " + TABLE_NAME + " where " + COLUMN_ID + "=" + id + "", null);
        ContentValues map;
        map = new ContentValues();
        if(res.moveToFirst())
        DatabaseUtils.cursorRowToContentValues(res, map);
        return map;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        return numRows;
    }

    public boolean updateAlarm (int id, int hour, int minute, Date date,int snooze_on , int snooze_count, int snooze_mode,int active)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_HOUR, hour);
        contentValues.put(COLUMN_MINUTE, minute);
        contentValues.put(COLUMN_DATE, date.toString());
        contentValues.put(COLUMN_SNOOZE_ON, snooze_on);
        contentValues.put(COLUMN_SNOOZE_COUNT, snooze_count);
        contentValues.put(COLUMN_SNOOZE_MODE, snooze_mode);
        contentValues.put(COLUMN_ACTIVE, active);

        db.update(TABLE_NAME, contentValues, "ID = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public int updateActive(int id, int active)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ACTIVE, active);
       return  db.update(TABLE_NAME, contentValues, "ID = ? ", new String[]{Integer.toString(id)});
       // return db.rawQuery("UPDATE " + TABLE_NAME + " SET " + COLUMN_ACTIVE + " = '" + active + "' WHERE " + COLUMN_ID + " = " + id, null);



    }


    public Integer deleteAlarm (Integer id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME,
                COLUMN_ID +" = ? ",
                new String[] { Integer.toString(id) });
    }

    public ArrayList<ContentValues> getAllAlarms()
    {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from " + TABLE_NAME, null );
        res.moveToFirst();


        ArrayList<ContentValues> retVal = new ArrayList<ContentValues>();
        ContentValues map;
        if(res.moveToFirst()) {
            do {
                map = new ContentValues();
                DatabaseUtils.cursorRowToContentValues(res, map);
                retVal.add(map);
            } while(res.moveToNext());
        }


        return retVal;
    }
}
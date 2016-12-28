package co.techovative.cmtplanner;

/**
 * Created by Analysis on 4/28/2016.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlannerSQLiteHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 3;
    // Database Name
    private static final String DATABASE_NAME = "PlannerDB";

    private static final String TABLE_PLANNER = "PLANNER";
    private static final String TABLE_APP_URL= "APP_URL";
    private static final String TABLE_COLOR_SCHEME = "COLOR_SCHEME";



    private static final String KEY_ID = "id";
    private static final String KEY_TOPIC = "topic";
    private static final String KEY_EVENT_ID = "eventId";
    private static final String KEY_LOCATION = "location";
    private static final String KEY_EVENT_TIME = "EventTime";
    private static final String KEY_CHAIRED_BY = "chairedBy";
    private static final String KEY_ATTENDED_BY = "attendedBy";
    private static final String KEY_SECTION = "Section";
    private static final String KEY_APP_URL = "App_Url";
    public static final String KEY_BORDER_COLOR = "BORDER_COLOR";
    public static final String KEY_BACKGR_COLOR = "BACKGR_COLOR";
    public static final String KEY_FIRST_ROW = "FIRST_ROW";
    public static final String KEY_SECOND_ROW = "SECOND_ROW";
    public static final String KEY_OTHER_ROW = "OTHER_ROW";


    public PlannerSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create book table
        String CREATE_PLANNER_TABLE = "CREATE TABLE PLANNER ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "eventId TEXT, "+
                "EventTime REAL, " +
                "topic TEXT, "+
                "location TEXT, " +
                "chairedBy TEXT, " +
                "attendedBy TEXT, " +
                "Section TEXT )";
        String CREATE_APP_URL_TABLE = "CREATE TABLE APP_URL( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "App_Url TEXT )";
        String CREATE_COLOR_SCHEME_TABLE = "CREATE TABLE COLOR_SCHEME( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "BORDER_COLOR REAL, " +
                "BACKGR_COLOR REAL, " +
                "FIRST_ROW REAL, " +
                "SECOND_ROW REAL  " +
                "OTHER_ROW REAL  )";

        db.execSQL(CREATE_PLANNER_TABLE);
        db.execSQL(CREATE_APP_URL_TABLE);
        db.execSQL(CREATE_COLOR_SCHEME_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older tables if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLANNER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_APP_URL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COLOR_SCHEME);
        // create fresh tables
        this.onCreate(db);
    }

    public void initializeValues(){
        Log.d("initializeValues", "initializing values");
//        this.updateAlertLevel(1.0f);
//        this.updateCOuntryCodes("PAK,US");
//        this.addContact("Ammad", "03335276003");
    }

    public boolean addPlanner(Planner feature){
        Log.d("addPlanner", feature.topic + " : " + feature.id + " : " + feature.eventTime );
        boolean duplicate = false;
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT * FROM " + TABLE_PLANNER + " WHERE " +  KEY_EVENT_ID + " LIKE '" + feature.id + "' ";
        int recordCount = db.rawQuery(sql, null).getCount();

        if(recordCount ==0)
        {
            ContentValues values = new ContentValues();
            values.put(KEY_EVENT_ID, feature.id); // get title
            values.put(KEY_TOPIC, feature.topic); // get author
            values.put(KEY_CHAIRED_BY, feature.chairedBy);
            values.put(KEY_LOCATION, feature.location);
            values.put(KEY_EVENT_TIME, feature.eventTime);
            values.put(KEY_ATTENDED_BY, feature.attendedBy);
            values.put(KEY_SECTION, feature.section);
            // 3. insert
            db.insert(TABLE_PLANNER, // table
                    null, //nullColumnHack
                    values); // key/value -> keys = column names/ values = column values
        }
        else
            duplicate = true;
        // 4. close
        db.close();
        return duplicate;
    }

    public void addPlanners(List<Planner> plans){
        Log.d("addPlanners", "Adding fresh planner list" );
        boolean duplicate = false;
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "DELETE FROM " + TABLE_PLANNER ;
        db.execSQL(sql);
        for (Planner feature :plans) {
//            sql = "SELECT * FROM " + TABLE_PLANNER + " WHERE " + KEY_EVENT_ID + " LIKE '" + feature.id + "' ";
            //int recordCount = db.rawQuery(sql, null).getCount();

            // if(recordCount ==0)
            //{
            ContentValues values = new ContentValues();
            values.put(KEY_EVENT_ID, feature.id); // get title
            values.put(KEY_TOPIC, feature.topic); // get author
            values.put(KEY_CHAIRED_BY, feature.chairedBy);
            values.put(KEY_LOCATION, feature.location);
            values.put(KEY_EVENT_TIME, feature.eventTime);
            values.put(KEY_ATTENDED_BY, feature.attendedBy);
            values.put(KEY_SECTION, feature.section);
            // 3. insert
            db.insert(TABLE_PLANNER, // table
                    null, //nullColumnHack
                    values); // key/value -> keys = column names/ values = column values
            //}
            //else
            //    duplicate = true;
        }
        // 4. close
        db.close();
//        return duplicate;
    }


    public List<Planner> getPlans() {

        List<Planner> features = new ArrayList<Planner>();

        String query = "SELECT  * FROM " + TABLE_PLANNER+ " order by " + KEY_EVENT_TIME + " ASC " ;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Planner feature = null;
        if (cursor.moveToFirst()) {
            do {

                feature = new Planner();
                feature.id = cursor.getString(1);
                feature.eventTime = cursor.getLong(2);
                feature.topic = cursor.getString(3);
                feature.location = cursor.getString(4);
                feature.chairedBy = cursor.getString(5);
                feature.attendedBy = cursor.getString(6);
                feature.section = cursor.getString(7);

                features.add(feature);

            } while (cursor.moveToNext());
        }

        Log.d("getPlans()", features.toString());
        return features;
    }
    public void updateApplUrl(String codes){
        Log.d("updateApplUrl", codes);
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT * FROM " + TABLE_APP_URL;
        int recordCount = db.rawQuery(sql, null).getCount();
//        db.close();
        if(recordCount < 1)
        // 2. create ContentValues to add key "column"/value
        {
            ContentValues values = new ContentValues();
            values.put(KEY_APP_URL, codes); // get title
            // 3. insert
            db.insert(KEY_APP_URL, // table
                    null, //nullColumnHack
                    values); // key/value -> keys = column names/ values = column values
        }else
        {
            sql = "UPDATE " + TABLE_APP_URL + " SET " + KEY_APP_URL + " = '" + codes +"'";
            db.execSQL(sql);
        }
        // 4. close
        db.close();
    }

    public String getAppUrl() {
        Log.d("getAppUrl()", "Begin .......");
        SQLiteDatabase db = this.getWritableDatabase();
        String codes = "" ;
        String sql = "SELECT *  FROM " + TABLE_APP_URL ;
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                codes = cursor.getString(1);

            } while (cursor.moveToNext());
        }
        db.close();
        Log.d("getAppUrl()", codes);
        return codes;

    }

    public void updateColorScheme(int color, String key){
        Log.d("updateColorScheme", key + " : " + color);
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT * FROM " + TABLE_COLOR_SCHEME;
        int recordCount = db.rawQuery(sql, null).getCount();
//        db.close();
        if(recordCount < 1)
        // 2. create ContentValues to add key "column"/value
        {
            ContentValues values = new ContentValues();
            values.put(key, color); // get title
            // 3. insert
            db.insert(TABLE_COLOR_SCHEME, // table
                    null, //nullColumnHack
                    values); // key/value -> keys = column names/ values = column values
        }else
        {
            sql = "UPDATE " + TABLE_COLOR_SCHEME + " SET " + key + " = " + color +" ";
            db.execSQL(sql);
        }
        // 4. close
        db.close();
    }

    public ColorScheme getColorScheme() {
        Log.d("getColorScheme()", "Begin .......");
        SQLiteDatabase db = this.getWritableDatabase();
        ColorScheme codes = null ;
        String sql = "SELECT *  FROM " + TABLE_COLOR_SCHEME ;
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                codes.borderColor = cursor.getInt(1);
                codes.backgrColor = cursor.getInt(2);
                codes.firstRow = cursor.getInt(3);
                codes.secondRow = cursor.getInt(4);
                codes.otherRow = cursor.getInt(5);
            } while (cursor.moveToNext());
        }
        db.close();
        Log.d("getAppUrl()", codes.borderColor + " : " + codes.backgrColor);
        return codes;

    }

}
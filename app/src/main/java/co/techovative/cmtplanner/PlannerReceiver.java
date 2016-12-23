package co.techovative.cmtplanner;

/**
 * Created by Analysis on 6/1/2016.
 */

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.telephony.SmsManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PlannerReceiver extends BroadcastReceiver {
    Planner earthquake = null;
    String geojsonString ;
    private Exception e=null;
    private PlannerSQLiteHelper earthquakeSQLiteHelper;
    private Context context;
    private static AlarmManager manager;
    private static int alarmInterval = 60000;
    private static PendingIntent pendingIntent;

    //public String appURL = "http://192.168.18.137/planner/pages/cmts.jsf";

    static void scheduleAlarm(Context ctxt)
    {
        Intent alarmIntent = new Intent(ctxt, PlannerReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(ctxt, 0, alarmIntent, 0);
        Log.d("AlarmReceiver", "Scheduling receiver ************************ ");
        manager = (AlarmManager)ctxt.getSystemService(Context.ALARM_SERVICE);
//        int interval = 60000; // 60 seconds
        cancelAlarm();
        manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), alarmInterval, pendingIntent);
//        Toast.makeText(this, "Alarm Set", Toast.LENGTH_SHORT).show();

    }
    public static  void cancelAlarm() {
        if (manager != null) {
            manager.cancel(pendingIntent);
//            Toast.makeText(this, "Alarm Canceled", Toast.LENGTH_SHORT).show();
        }

    }
    @Override
    public void onReceive(Context arg0, Intent arg1) {
        if(arg1 !=null && arg1.getAction()!=null) {
            Log.d("AlarmReceiver", "Intent is :: " + arg1.getAction());
            if (arg1.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
                scheduleAlarm(arg0);
            }
        }
//        else{
            // For our recurring task, we'll just display a message
            earthquakeSQLiteHelper = new PlannerSQLiteHelper(arg0);

    //        Toast.makeText(arg0, "I'm running", Toast.LENGTH_SHORT).show();
            Log.d("AlarmReceiver", "Alarm started..........................");
            context = arg0;
            new PollLatestPlanner().execute();
//        }
    }


    private class PollLatestPlanner extends AsyncTask<String, Void, Void> {
        List<Planner> plans = null;
        String geojsonString ;
        private Exception e=null;
        String appURL = earthquakeSQLiteHelper.getAppUrl();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            mProgressDialog = new ProgressDialog(MainActivity.this);
//            mProgressDialog.setTitle("Earthquake Monitoring");
//            mProgressDialog.setMessage("Refresshing...");
//            mProgressDialog.setIndeterminate(false);
//            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {


            System.out.println("Requested URL: " + appURL);
            StringBuilder sb = new StringBuilder();
            URLConnection urlConn = null;
            InputStreamReader in = null;
            try {
                java.net.URL url = new java.net.URL(appURL);
                urlConn = url.openConnection();
//                if (urlConn != null)
//                    System.out.println("Requested URL:1 ");
                urlConn.setReadTimeout(120 * 1000);
                if (urlConn != null && urlConn.getInputStream() != null) {
//                    System.out.println("Requested URL:2 ");
                    in = new InputStreamReader(urlConn.getInputStream(), Charset.defaultCharset());
                    BufferedReader bufferedReader = new BufferedReader(in);
                    if (bufferedReader != null) {
//                        System.out.println("Requested URL:3 ");
                        int cp;
                        while ((cp = bufferedReader.read()) != -1) {
                            sb.append((char) cp);
                        }
                        bufferedReader.close();
                    }
                }
                in.close();
                geojsonString = sb.toString();
            }catch (Exception e) {
                e.printStackTrace();
                this.e=e;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if(this.e == null) {
                Gson gson = new Gson();
                plans  = gson.fromJson(geojsonString, new TypeToken<List<Planner>>(){}.getType());

               earthquakeSQLiteHelper.addPlanners(plans);
            }else
            {
                Log.d("AlarmReceiver", "Error!" + e.getMessage());

            }

        }
    }
}
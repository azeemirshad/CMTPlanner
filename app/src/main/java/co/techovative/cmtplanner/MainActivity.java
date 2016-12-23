package co.techovative.cmtplanner;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TableLayout latestEventsTable;
    private ProgressDialog mProgressDialog;
    private PlannerSQLiteHelper plannerSQLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Intent localIntent = new Intent(MainActivity.this, ConfigurationActivity.class);
                MainActivity.this.startActivity(localIntent);
                //push from bottom to top
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
            }
        });

        plannerSQLiteHelper = new PlannerSQLiteHelper(this);
        latestEventsTable = (TableLayout)findViewById(R.id.cmtTable);
        if(plannerSQLiteHelper.getAppUrl().equals(""))
            plannerSQLiteHelper.updateApplUrl("http://192.168.1.200/planner/pages/cmts.jsf");
        PlannerReceiver.scheduleAlarm(this);
        Toast.makeText(this, "Alarm Set", Toast.LENGTH_SHORT).show();
//        new ShowLatestPlans().execute();
        final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                //Do something after 100ms
//                new ShowLatestPlans().execute();
//            }
//        }, 10000);

        // flag that should be set true if handler should stop
        final  boolean mStopHandler = false;

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // do your stuff - don't create a new runnable here!
                Log.d("Refresher", "Invoked once again..");
                new ShowLatestPlans().execute();
                if (!mStopHandler) {
                    handler.postDelayed(this, 60000);
                }
            }
        };

// start it with:
        handler.post(runnable);





    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private class ShowLatestPlans extends AsyncTask<String, Void, Void> {
        //        USGSEarthquake earthquake = null;
        String geojsonString ;
        List<Planner> features = null;
        private Exception e=null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(MainActivity.this);
            mProgressDialog.setTitle("DG EME - IHD/Cmts");
            mProgressDialog.setMessage("Refresshing...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {


//            System.out.println("Requested URL: " + usgsURL);
            StringBuilder sb = new StringBuilder();
//            URLConnection urlConn = null;
//            InputStreamReader in = null;
            try {
                features = plannerSQLiteHelper.getPlans();
//                java.net.URL url = new java.net.URL(usgsURL);
//                urlConn = url.openConnection();
//                if (urlConn != null)
//                    System.out.println("Requested URL:1 ");
//                            urlConn.setReadTimeout(120 * 1000);
//                    if (urlConn != null && urlConn.getInputStream() != null) {
//                        System.out.println("Requested URL:2 ");
//                        in = new InputStreamReader(urlConn.getInputStream(), Charset.defaultCharset());
//                        BufferedReader bufferedReader = new BufferedReader(in);
//                        if (bufferedReader != null) {
//                            System.out.println("Requested URL:3 ");
//                            int cp;
//                            while ((cp = bufferedReader.read()) != -1) {
//                                sb.append((char) cp);
//                            }
//                            bufferedReader.close();
//                        }
//                    }
//                    in.close();
//                    geojsonString = sb.toString();
            }catch (Exception e) {
                e.printStackTrace();
                this.e=e;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            System.out.println("Requested URL:4 ");
            mProgressDialog.dismiss();
            if(this.e == null) {
//                Gson gson = new Gson();
//                System.out.println("Requested URL:5 ");
//                earthquake = gson.fromJson(geojsonString, USGSEarthquake.class);
//                int alertLevel = earthquakeSQLiteHelper.getAlertLevel();
//                boolean duplicate = false;
//                for (Feature feature : earthquake.features) {
//                    if(feature.properties.mag < alertLevel){
//                        feature.status = "Ignored";
//                    }else
//                    {
//                        feature.status = "Sent";
//                    }
//                    duplicate = earthquakeSQLiteHelper.addEarthquake(feature);
//                    if(!duplicate){
//                        sendSMSToAllContacts(feature);
//                    }

//                }
                redrawTable();
            }else
            {
                Toast.makeText(getApplicationContext(), "Error!" + e.getMessage(), Toast.LENGTH_LONG).show();

            }

        }

        private void redrawTable() {
//
//            latestEventsTable.reremoveAllViews();
            while (latestEventsTable.getChildCount() > 1)
                latestEventsTable.removeView(latestEventsTable.getChildAt(latestEventsTable.getChildCount() - 1));
            System.out.println("Requested URL:6 ");
            SimpleDateFormat format  = new SimpleDateFormat("dd/MM/yy HH:mm");
            String temp;

            ArrayList<TableRow> localTableRows = new ArrayList<TableRow>();
            int i = 300;
            TableRow localTableRow = null;

            TextView col1 = null;
            TextView col2 = null;
            TextView col3 = null;
            TextView col4 = null;
            TextView col5 = null;
            TextView col6 = null;
            int paddingLeft = 3;
            int paddingTop = 5;
            int paddingRight = 3;
            int paddingBotton = 5;



            System.out.println("Earthquakes ::  " + features.size());
            for (Planner feature : features) {

                localTableRow = new TableRow(MainActivity.this);
                localTableRow.setId(100 + i);
                TableLayout.LayoutParams params = new TableLayout.LayoutParams(
                        TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.MATCH_PARENT);
                params.gravity = Gravity.FILL;
                params.setMargins(1,1,1,1);
                //params.rightMargin = 1;


                localTableRow.setLayoutParams(params);
                localTableRow.setGravity(Gravity.FILL);

                col1 = new TextView(MainActivity.this);
                col1.setId(300 + i);
                col1.setGravity(Gravity.CENTER);
                //col1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                col1.setPadding(paddingLeft, paddingTop, paddingRight, paddingBotton);
                //col1.setWidth(500);
                //col1.setTextColor(Color.parseColor("#ffffff"));
                col1.setTextAppearance(MainActivity.this, i==300 ? R.style.DateTextStyle3 :
                        i % 2 == 0 ? R.style.DateTextStyle1: R.style.DateTextStyle2);
                col1.setBackgroundColor(Color.BLACK);

                TableRow.LayoutParams rowparams = new TableRow.LayoutParams(
                        TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT);
                //rowparams.gravity = Gravity.FILL;
                rowparams.setMargins(1,0,1,0);



                col1.setLayoutParams(rowparams);
                // Create a TextView for column 2
                col2 = new TextView(MainActivity.this);
                col2.setId(300 + i);
                col2.setGravity(Gravity.CENTER);
//                col2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                col2.setPadding(paddingLeft, paddingTop, paddingRight, paddingBotton);
                //col2.setWidth(500);
//                col2.setTextColor(Color.parseColor("#ffffff"));
                col2.setTextAppearance(MainActivity.this, i==300 ? R.style.DateTextStyle3 :
                        i % 2 == 0 ? R.style.DateTextStyle1: R.style.DateTextStyle2);
                col2.setBackgroundColor(Color.BLACK);
                col2.setLayoutParams(rowparams);
//                col2.setLayoutParams(new TableRow.LayoutParams(
//                        200,
//                        TableRow.LayoutParams.WRAP_CONTENT));
                // Create a TextView for column 3
                col3 = new TextView(MainActivity.this);
                col3.setId(400 + i);
                col3.setGravity(Gravity.CENTER);
                col3.setPadding(paddingLeft, paddingTop, paddingRight, paddingBotton);
//                col3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
//                col3.setWidth(50);
//                col3.setTextColor(Color.parseColor("#ffffff"));
                col3.setTextAppearance(MainActivity.this, i==300 ? R.style.DateTextStyle3 :
                        i % 2 == 0 ? R.style.DateTextStyle1: R.style.DateTextStyle2);
                col3.setBackgroundColor(Color.BLACK);
                col3.setLayoutParams(rowparams);

                col4 = new TextView(MainActivity.this);
                col4.setId(400 + i);
                col4.setGravity(Gravity.CENTER);
                col4.setPadding(paddingLeft, paddingTop, paddingRight, paddingBotton);
//                col4.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
//                col3.setWidth(50);
//                col4.setTextColor(Color.parseColor("#ffffff"));
                col4.setTextAppearance(MainActivity.this, i==300 ? R.style.DateTextStyle3 :
                        i % 2 == 0 ? R.style.DateTextStyle1: R.style.DateTextStyle2);
                col4.setBackgroundColor(Color.BLACK);
                col4.setLayoutParams(rowparams);


                col5 = new TextView(MainActivity.this);
                col5.setId(400 + i);
                col5.setGravity(Gravity.CENTER);
                col5.setPadding(paddingLeft, 5, paddingRight, 5);
                col5.setTextAppearance(MainActivity.this, i==300 ? R.style.DateTextStyle3 :
                        i % 2 == 0 ? R.style.DateTextStyle1: R.style.DateTextStyle2);
                col5.setBackgroundColor(Color.BLACK);
                col5.setMaxWidth(200);
                col5.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                col5.setLayoutParams(rowparams);


                col6 = new TextView(MainActivity.this);
                col6.setId(400 + i);
                col6.setGravity(Gravity.CENTER);
                col6.setPadding(paddingLeft, paddingTop, paddingRight, paddingBotton);
//                col6.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
//                col3.setWidth(50);
//                col6.setTextColor(Color.parseColor("#ffffff"));
                col6.setTextAppearance(MainActivity.this, i==300 ? R.style.DateTextStyle3 :
                        i % 2 == 0 ? R.style.DateTextStyle1: R.style.DateTextStyle2);

                col6.setBackgroundColor(Color.BLACK);
                col6.setLayoutParams(rowparams);
//                col3.setLayoutParams(new TableRow.LayoutParams(
//                        70,
//                        TableRow.LayoutParams.WRAP_CONTENT));




                System.out.println("Row " + i + ":" + format.format(feature.eventTime) + " : " + feature.topic );
                col1.setText(String.valueOf(format.format(feature.eventTime)));
                col2.setText(feature.topic);

                col3.setText(feature.location );
                col4.setText(feature.chairedBy);
                col5.setText(feature.attendedBy);
                col6.setText(feature.section);


//                if ((i % 2) == 0) {
//                    //localTableRow.setBackgroundColor(Color.WHITE);
//                    localTableRow.setBackgroundResource(R.drawable.row_blue_bg);
//                } else {
//                    //localTableRow.setBackgroundColor(Color.LTGRAY);
//                    localTableRow.setBackgroundResource(R.drawable.row_red_bg);
//                }
                localTableRow.addView(col1);
                localTableRow.addView(col2);
                localTableRow.addView(col3);
                localTableRow.addView(col4);
                localTableRow.addView(col5);
                localTableRow.addView(col6);
                localTableRow.setLayoutParams(params);
                latestEventsTable.addView(localTableRow, params);
                i++;
            }







        }

    }

}

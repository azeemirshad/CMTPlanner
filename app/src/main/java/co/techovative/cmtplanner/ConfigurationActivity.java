package co.techovative.cmtplanner;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;

public class ConfigurationActivity extends AppCompatActivity {
    private EditText editApplUrl;

    private PlannerSQLiteHelper plannerSQLiteHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        editApplUrl = (EditText) findViewById(R.id.editApplUrl);
        plannerSQLiteHelper = new PlannerSQLiteHelper(this);
        editApplUrl.setText(plannerSQLiteHelper.getAppUrl());
    }



    public void onUpdateApplUrl(View v) {
        plannerSQLiteHelper.updateApplUrl(this.editApplUrl.getText().toString());

        Toast.makeText(this, "App Url updated", Toast.LENGTH_SHORT).show();
        finish();
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
    }


}

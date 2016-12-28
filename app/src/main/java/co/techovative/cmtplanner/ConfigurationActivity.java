package co.techovative.cmtplanner;


import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;


import java.util.ArrayList;
import java.util.Map;

public class ConfigurationActivity extends AppCompatActivity implements  View.OnClickListener{
    private EditText editApplUrl;
    private int currentBackgroundColor = R.color.colorAccent;
    private PlannerSQLiteHelper plannerSQLiteHelper;
    private Button selBorderColorBtn;
    private View borderColorView, backgrView, firstRowView, secondRowView, otherRowView, titleRowView;
    private ColorScheme colorScheme;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        editApplUrl = (EditText) findViewById(R.id.editApplUrl);
        plannerSQLiteHelper = new PlannerSQLiteHelper(this);
        editApplUrl.setText(plannerSQLiteHelper.getAppUrl());
        colorScheme = plannerSQLiteHelper.getColorScheme();
        borderColorView = findViewById(R.id.color_border_view);
        backgrView = findViewById(R.id.color_backgr_view);
        firstRowView = findViewById(R.id.color_first_row_view);
        secondRowView = findViewById(R.id.color_second_row_view);
        otherRowView = findViewById(R.id.color_other_row_view);
        titleRowView = findViewById(R.id.color_title_row_view);
        borderColorView.setBackgroundColor(colorScheme.borderColor);
        backgrView.setBackgroundColor(colorScheme.backgrColor);
        firstRowView.setBackgroundColor(colorScheme.firstRow);
        secondRowView.setBackgroundColor(colorScheme.secondRow);
        otherRowView.setBackgroundColor(colorScheme.otherRow);



        selBorderColorBtn = (Button) findViewById(R.id.btnBorderColor);
//        currentBackgroundColor = getResources().getColor(R.color.colorAccent);
//        changeBackgroundColor(currentBackgroundColor);

    }

    public void onApply(View v) {
        Toast.makeText(this, "Settings applied", Toast.LENGTH_SHORT).show();
        finish();
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
    }

    public void onRestoreDefault(View v) {
        plannerSQLiteHelper.updateColorScheme(getResources().getColor(R.color.colorAccent), PlannerSQLiteHelper.KEY_BORDER_COLOR);
        plannerSQLiteHelper.updateColorScheme(Color.BLACK, PlannerSQLiteHelper.KEY_BACKGR_COLOR);
        plannerSQLiteHelper.updateColorScheme(getResources().getColor(R.color.holo_red_dark), PlannerSQLiteHelper.KEY_FIRST_ROW);
        plannerSQLiteHelper.updateColorScheme(getResources().getColor(R.color.colorYellowLight), PlannerSQLiteHelper.KEY_SECOND_ROW);
        plannerSQLiteHelper.updateColorScheme(Color.WHITE, PlannerSQLiteHelper.KEY_OTHER_ROW);
        plannerSQLiteHelper.updateColorScheme(Color.WHITE, PlannerSQLiteHelper.KEY_TITLE_ROW);
        Toast.makeText(this, "Default settings restored", Toast.LENGTH_SHORT).show();
        finish();
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
    }

    public void onUpdateApplUrl(View v) {
        plannerSQLiteHelper.updateApplUrl(this.editApplUrl.getText().toString());

        Toast.makeText(this, "App Url updated", Toast.LENGTH_SHORT).show();

    }
    public void onChooseBorderColor(View V){
        Log.d("onChooseBorderColor", "Opening dialog");
        this.openColorPickerDialog(getResources().getString(R.string.title_choose_border_color), PlannerSQLiteHelper.KEY_BORDER_COLOR);
    }
    public void onChooseBackgrColor(View v){
        Log.d("onChooseBackgrColor", "Opening dialog");
        this.openColorPickerDialog(getResources().getString(R.string.title_choose_backgr_color), PlannerSQLiteHelper.KEY_BACKGR_COLOR);
    }
    public void onChooseTitleRowColor(View v){
        Log.d("onChooseTitleRowColor", "Opening dialog");
        this.openColorPickerDialog(getResources().getString(R.string.title_choose_title_row_color), PlannerSQLiteHelper.KEY_TITLE_ROW);
    }
    public void onChooseFirstRowColor(View v){
        Log.d("onChooseFirstRowColor", "Opening dialog");
        this.openColorPickerDialog(getResources().getString(R.string.title_choose_first_row_color), PlannerSQLiteHelper.KEY_FIRST_ROW);
    }
    public void onChooseSecondRowColor(View v){
        Log.d("onChooseSecondRowColor", "Opening dialog");
        this.openColorPickerDialog(getResources().getString(R.string.title_choose_second_row_color), PlannerSQLiteHelper.KEY_SECOND_ROW);
    }
    public void onChooseOtherRowColor(View v){
        Log.d("onChooseOtherRowColor", "Opening dialog");
        this.openColorPickerDialog(getResources().getString(R.string.title_choose_other_row_color), PlannerSQLiteHelper.KEY_OTHER_ROW);
    }

    private void openColorPickerDialog(String title, String key) {
        final String colorKey = key;
        //        int[] colors = {R.color.amber, R.color.blue, R.color.brown, R.color.blue_grey, R.color.cyan, R.color.deep_orange, R.color.deep_purple,
//                        R.color.green};
//        ColorPickerDialog colorPickerDialog = new ColorPickerDialog();
//        colorPickerDialog.initialize(
//                R.string.color_picker_default_title, colors, R.color.blue, 4, colors.length);
//        colorPickerDialog.show(getFragmentManager(), "Color Chooser");
//        LayoutInflater layoutInflater = LayoutInflater.from(this);
//        ColorPickerPalette colorPickerPalette = (ColorPickerPalette) layoutInflater
//                .inflate(R.layout.color_palette, null);
//        colorPickerPalette.init(colors.length, 4, this);
//        colorPickerPalette.drawPalette(colors, R.color.blue);
//        android.support.v7.app.AlertDialog alert = new android.support.v7.app.AlertDialog.Builder(this)
//                .setTitle(R.string.color_picker_default_title)
////                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
////                    @Override
////                    public void onClick(DialogInterface dialogInterface, int i) {
////                        Log.d("onClick" , "Clicked Yes ");
////                    }
////                })
////                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
////                    @Override
////                    public void onClick(DialogInterface dialogInterface, int i) {
////                        Log.d("onClick" , "Clicked No ");
////                    }
////                })
//                .setView(colorPickerPalette)
//                .create();
//        alert.show();


//        ColorChooserDialog dialog = new ColorChooserDialog(this);
//        dialog.setTitle("Choose Color");
//        dialog.setColorListener(new ColorListener() {
//            @Override
//            public void OnColorClick(View v, int color) {
//                //do whatever you want to with the values
//                Log.d("onClick" , "Clicked Yes ");
//
//                Log.d("onColorSelected" , "Selected color :: " +  getResources().getResourceName(color));
//                Snackbar.make(v, "Color: "+color, Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//                getResources().getResourceName(color);
//
//            }
//        });
//        //customize the dialog however you want
//        dialog.show();
        ColorPickerDialogBuilder

                .with(this, R.style.MyDialogTheme)
                .setTitle(title)
                .initialColor(currentBackgroundColor)
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) {
                        //toast( "onColorSelected: 0x" + Integer.toHexString(selectedColor));
                    }
                })
                .setPositiveButton("ok", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                        updateColor(selectedColor, colorKey);
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .build()
                .show();
    }

    private void updateColor(int selectedColor, String key) {
//        currentBackgroundColor = selectedColor;
//        root.setBackgroundColor(selectedColor);
//        selBorderColorBtn.setBackgroundColor(selectedColor);
        plannerSQLiteHelper.updateColorScheme(selectedColor, key);
        if(key.equals(PlannerSQLiteHelper.KEY_BORDER_COLOR))
            borderColorView.setBackgroundColor(selectedColor);
        else if(key.equals(PlannerSQLiteHelper.KEY_BACKGR_COLOR))
            backgrView.setBackgroundColor(selectedColor);
        else if(key.equals(PlannerSQLiteHelper.KEY_FIRST_ROW))
            firstRowView.setBackgroundColor(selectedColor);
        else if(key.equals(PlannerSQLiteHelper.KEY_SECOND_ROW))
            secondRowView.setBackgroundColor(selectedColor);
        else if(key.equals(PlannerSQLiteHelper.KEY_OTHER_ROW))
            otherRowView.setBackgroundColor(selectedColor);
        else if(key.equals(PlannerSQLiteHelper.KEY_TITLE_ROW))
            titleRowView.setBackgroundColor(selectedColor);
    }

    private void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onClick(View view) {

    }
}

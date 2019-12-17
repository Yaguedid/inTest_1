package com.example.intest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

public class Settings extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    private SharedPreferences userinfo;
    private SharedPreferences.Editor editor;

    Switch emp;
    Switch stud;
    String StudentOrEmployer;
    String initalizSettting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        userinfo=getSharedPreferences("userinfos", MODE_PRIVATE);
        editor=userinfo.edit();

        initalizSettting=userinfo.getString("StudentOrEmployer","nothing");
        Log.d("edddddd",initalizSettting+"");

        emp = (Switch) findViewById(R.id.amEmp_switch);
        stud = (Switch) findViewById(R.id.amStud_switch);
        if (emp != null) {
            emp.setOnCheckedChangeListener(this);
        }
        if (stud != null) {
            stud.setOnCheckedChangeListener(this);
        }

        if(initalizSettting.equals("Student")){
            emp.setChecked(false);
            stud.setChecked(true);
        }
        if(initalizSettting.equals("Employer")){
            emp.setChecked(true);
            stud.setChecked(false);
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (emp.equals(buttonView)) {
            if (buttonView.isChecked()) {
                stud.setChecked(false);
                StudentOrEmployer = "Employer";
            }
            if (!buttonView.isChecked()) {
                stud.setChecked(true);
                StudentOrEmployer = "Student";
            }
        } else if (stud.equals(buttonView)) {
            if (buttonView.isChecked()) {
                emp.setChecked(false);
                StudentOrEmployer = "Student";
            }
            if (!buttonView.isChecked()) {
                emp.setChecked(true);
                StudentOrEmployer = "Employer";
            }
        }

        Log.d("xxxxx", StudentOrEmployer);
        editor.putString("StudentOrEmployer",StudentOrEmployer);
        editor.apply();


    }








    public void  ApplySettings(View view)
    {
        startActivity(new Intent(Settings.this, WelcomeScreen.class));
    }
}

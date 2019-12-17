package com.example.intest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.intest.main.CustomViewPager;
import com.example.intest.main.SectionsPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class TabsHolder  extends AppCompatActivity {
    public static ViewPager viewPager;
    public static TabsHolder mVar;
    public static HashMap<String,String> chekMap =new HashMap<>();
    private String EmailUser,FisrtnameUser,LastNameUser,IdUser,PictureUser,StudentOrEmployer;
    private SharedPreferences userinfo;
    LinearLayout parentLinearLayout ;
    LinearLayout parentLinearLayoutExperience ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs_holder);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        viewPager = findViewById(R.id.view_pager);
        //viewPager.setPagingEnabled(true);
        viewPager.setAdapter(sectionsPagerAdapter);

        mVar=this;
        setSharedPreferences();

        chekMap.put("peron_name","false");
        chekMap.put("Peron_lastname","false");
        chekMap.put("Peron_email","false");
        chekMap.put("Peron_phone","false");
        chekMap.put("Peron_birth_date","false");
        chekMap.put("Peron_city","false");

        chekMap.put("education_domaine","false");
        chekMap.put("education_diplome","false");

        chekMap.put("skills_langue","false");
        chekMap.put("skills","false");

        chekMap.put("offre_type","false");
        chekMap.put("offre_period","false");
        //chekMap.put("offre_about","false");








        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
    }
        public void setState(Boolean b){

          //  viewPager.setPagingEnabled(b);
        }

        public static TabsHolder getInstance(){
            return mVar;
        }

        public void goToDashbord()
        {
            startActivity(new Intent(TabsHolder.this,DashbordStudent.class));
        }
        public void setSharedPreferences()
        {
            userinfo=getSharedPreferences("userinfos", MODE_PRIVATE);

            EmailUser=userinfo.getString("email",null);
            FisrtnameUser=userinfo.getString("firstname",null);
            LastNameUser=userinfo.getString("lastname",null);
            IdUser=userinfo.getString("id",null);
            PictureUser=userinfo.getString("picture",null);
            StudentOrEmployer=userinfo.getString("StudentOrEmployer",null);
        }

    public void stockInFireBase(String name,String lastname,String phone,String email,String city,String birthDate,String niveuEtude
            ,String domaineListString,String skillsListString,String langueListString,String offersListString,String PeriodListString)
    {
        FirebaseDatabase database;
        DatabaseReference myRef;
        database = FirebaseDatabase.getInstance();
        myRef=database.getReference("Users").child(IdUser).child("Date_Of_Birth");
        myRef.setValue(birthDate);
        myRef=database.getReference("Users").child(IdUser).child("Periode");
        myRef.setValue(PeriodListString);
        myRef=database.getReference("Users").child(IdUser).child("Phone");
        myRef.setValue(phone);
        myRef=database.getReference("Users").child(IdUser).child("City");
        myRef.setValue(city);
        myRef=database.getReference("Users").child(IdUser).child("Languages");
        myRef.setValue(langueListString);
        myRef=database.getReference("Users").child(IdUser).child("Skills");
        myRef.setValue(skillsListString);
        myRef=database.getReference("Users").child(IdUser).child("Diplomas");
        myRef.setValue(niveuEtude);
        myRef=database.getReference("Users").child(IdUser).child("Domaine");
        myRef.setValue(domaineListString);
        myRef=database.getReference("Users").child(IdUser).child("Type_Shearched_Offer");
        myRef.setValue(offersListString);
        myRef  = database.getReference("Users").child(IdUser).child("FirstName");
        myRef.setValue(name);
        myRef  = database.getReference("Users").child(IdUser).child("LastName");
        myRef.setValue(lastname);
        myRef  = database.getReference("Users").child(IdUser).child("Email");
        myRef.setValue(email);


    }

    public void onDelete(View v) {

        parentLinearLayout.removeView((View) v.getParent());
         Toast.makeText(mVar, "on delet", Toast.LENGTH_SHORT).show();
         tab_3.getInstance().parentLinearLayout=parentLinearLayout;
    }
    public void onDeleteExperienceFiled(View v) {
        parentLinearLayoutExperience.removeView((View) v.getParent());
        tab_3.getInstance().parentLinearLayoutExperience=parentLinearLayoutExperience;
    }
}

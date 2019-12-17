package com.example.intest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.InputStream;

public class info_employer extends AppCompatActivity implements View.OnFocusChangeListener {
    TextView fullNameView;
EditText firstNameView,LastNameView,EmailView,CompanyNameView,CompanyDescriptionView;
String firstNameString,LastNameString,EmailString,CompanyNameString,CompanyDescriptionString;
private String EmailUser,FisrtnameUser,LastNameUser,IdUser,PictureUser,StudentOrEmployer;
ImageView logoImage,userImage,descriptionEmpty,companyEmpty,emailEmpty,nomEmpty,prenomEmpty;
    private SharedPreferences userinfo;
    FirebaseDatabase database;
    DatabaseReference myRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emloyer_infos);

        userinfo=getSharedPreferences("userinfos", MODE_PRIVATE);

        EmailUser=userinfo.getString("email",null);
        FisrtnameUser=userinfo.getString("firstname",null);
        LastNameUser=userinfo.getString("lastname",null);
        IdUser=userinfo.getString("id",null);
        PictureUser=userinfo.getString("picture",null);
        database = FirebaseDatabase.getInstance();
        InstantiateViews();
    }
    public void InstantiateViews()
    {


        logoImage=findViewById(R.id.logoImage);
        descriptionEmpty=findViewById(R.id.descriptionEmpty);
        companyEmpty=findViewById(R.id.companyEmpty);
        emailEmpty=findViewById(R.id.emailEmpty);
        nomEmpty=findViewById(R.id.nomEmpty);
        prenomEmpty=findViewById(R.id.prenomEmpty);
        fullNameView=findViewById(R.id.fullname);
        fullNameView.setText(FisrtnameUser+" "+LastNameUser);
        firstNameView=findViewById(R.id.firstname);
        firstNameView.setOnFocusChangeListener(this);
        firstNameView.setText(FisrtnameUser);
        LastNameView=findViewById(R.id.LastName);
        LastNameView.setOnFocusChangeListener(this);
        LastNameView.setText(LastNameUser);
        EmailView=findViewById(R.id.email);
        EmailView.setOnFocusChangeListener(this);
        EmailView.setText(EmailUser);
        CompanyNameView=findViewById(R.id.Company_name);
        CompanyNameView.setOnFocusChangeListener(this);
        CompanyDescriptionView=findViewById(R.id.descriptionCompany);
       CompanyDescriptionView.setOnFocusChangeListener(this);

        logoImage=findViewById(R.id.logoImage);
        userImage=findViewById(R.id.UserImage);
        new info_employer.DownloadImageTask((ImageView)userImage)
                .execute(PictureUser);
    }
    public void yes(View view)
    {
        boolean isSomethingEmpty=false;
        firstNameString=firstNameView.getText().toString().trim();
        LastNameString=LastNameView.getText().toString().trim();
        EmailString=EmailView.getText().toString().trim();
        CompanyNameString=CompanyNameView.getText().toString().trim();
        CompanyDescriptionString=CompanyDescriptionView.getText().toString().trim();
        logoImage=findViewById(R.id.logoImage);


        if(firstNameString.equals(""))
        {

            prenomEmpty.setVisibility(View.VISIBLE);
            isSomethingEmpty=true;
        }
        if(LastNameString.equals(""))
        {

            nomEmpty.setVisibility(View.VISIBLE);
            isSomethingEmpty=true;
        }
        if(EmailString.equals(""))
        {

            emailEmpty.setVisibility(View.VISIBLE);

            isSomethingEmpty=true;
        }
        if(CompanyNameString.equals(""))
        {

            companyEmpty.setVisibility(View.VISIBLE);

            isSomethingEmpty=true;
        }
        if(CompanyDescriptionString.equals(""))
        {

            descriptionEmpty.setVisibility(View.VISIBLE);

            isSomethingEmpty=true;
        }
        if(!isSomethingEmpty)
        {
            Toast.makeText(info_employer.this,"all is good",Toast.LENGTH_SHORT).show();
            myRef  = database.getReference("Users").child(IdUser).child("FirstName");
            myRef.setValue(firstNameString);
            myRef  = database.getReference("Users").child(IdUser).child("LastName");
            myRef.setValue(LastNameString);
            myRef  = database.getReference("Users").child(IdUser).child("Email");
            myRef.setValue(EmailString);
            myRef  = database.getReference("Users").child(IdUser).child("CompanyName");
            myRef.setValue(CompanyNameString);
            myRef  = database.getReference("Users").child(IdUser).child("CompanyDescription");
            myRef.setValue(CompanyDescriptionString);
            startActivity(new Intent(info_employer.this, DashbordEmployer.class));
        }




    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
       switch (v.getId())
        {
            case R.id.firstname:
                if(hasFocus)
                {
                    prenomEmpty.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.LastName:
                if(hasFocus)
                {
                    nomEmpty.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.email:
                if(hasFocus)
                {
                    emailEmpty.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.Company_name:
                if(hasFocus)
                {
                    companyEmpty.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.descriptionCompany:
                if(hasFocus) {
                    descriptionEmpty.setVisibility(View.INVISIBLE);
                    break;
                }
    }




}
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}

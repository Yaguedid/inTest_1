package com.example.intest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.InputStream;

public class WelcomeScreen extends AppCompatActivity {
private String EmailUser,FisrtnameUser,LastNameUser,IdUser,PictureUser,StudentOrEmployer;
TextView welcomeText;
ImageView userImage;
    String FirstTime="";
    FirebaseDatabase database;
    DatabaseReference myRef;
private SharedPreferences userinfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_welcome);
setAdds();
        userinfo=getSharedPreferences("userinfos", MODE_PRIVATE);

            EmailUser=userinfo.getString("email",null);
            FisrtnameUser=userinfo.getString("firstname",null);
            LastNameUser=userinfo.getString("lastname",null);
            IdUser=userinfo.getString("id",null);
            PictureUser=userinfo.getString("picture",null);
            StudentOrEmployer=userinfo.getString("StudentOrEmployer",null);

       /********************firebase**************/
        database = FirebaseDatabase.getInstance();
        myRef  = database.getReference("Users").child(IdUser).child("FirstName");
        myRef.setValue(FisrtnameUser);
        myRef  = database.getReference("Users").child(IdUser).child("LastName");
        myRef.setValue(LastNameUser);
        myRef  = database.getReference("Users").child(IdUser).child("Email");
        myRef.setValue(EmailUser);
        myRef  = database.getReference("Users").child(IdUser).child("Picture");
        myRef.setValue(PictureUser);

        /**********************************/
        instantiateViews();
    }

    private void instantiateViews() {
        welcomeText=findViewById(R.id.welcometext);
        userImage=findViewById(R.id.userimg);
        welcomeText.setText(FisrtnameUser+" "+LastNameUser);
        new DownloadImageTask((ImageView)userImage)
                .execute(PictureUser);

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
    public void goToDashbord(View view)
    {
        if(StudentOrEmployer.equals("Employer"))
        startActivity(new Intent(WelcomeScreen.this, DashbordEmployer.class));
       else
        {
          FirstTime=userinfo.getString("FirstTime",null);
            if(FirstTime==null)
             startActivity(new Intent(WelcomeScreen.this,TabsHolder.class));
           else
            startActivity(new Intent(WelcomeScreen.this, DashbordStudent.class));
        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(WelcomeScreen.this,VeryFirstActivity.class));
    }
    private void setAdds() {
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdView mAdView = findViewById(R.id.adViewh);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }
}

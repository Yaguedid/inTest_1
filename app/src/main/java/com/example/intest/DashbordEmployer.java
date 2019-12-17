package com.example.intest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DashbordEmployer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private String EmailUser, FisrtnameUser, LastNameUser, IdUser, PictureUser;
    private SharedPreferences userinfo;
    FirebaseDatabase database;
    DatabaseReference myRef;
    Button PostAnOfferButton;

    List<String> ListToRemove=new ArrayList<>();
    private LayoutInflater mInflater;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashbord_user);




        userinfo=getSharedPreferences("userinfos", MODE_PRIVATE);
        EmailUser=userinfo.getString("email",null);
        FisrtnameUser=userinfo.getString("firstname",null);
        LastNameUser=userinfo.getString("lastname",null);
        IdUser=userinfo.getString("id",null);
        PictureUser=userinfo.getString("picture",null);
        instantiateViews();
        Intent intent=getIntent();
        ListToRemove=intent.getStringArrayListExtra("ListToRemove");
        database = FirebaseDatabase.getInstance();

        setAdds();
        setMyNavigatinBar();



    }

    private void setAdds() {
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private void instantiateViews() {
        PostAnOfferButton=findViewById(R.id.postOffer);
        TextView card_user_name =(TextView) findViewById(R.id.card_user_name);
        TextView card_user_name_2 =(TextView) findViewById(R.id.card_user_name_2);
        ImageView card_img =(ImageView) findViewById(R.id.card_img);

        card_user_name.setText(FisrtnameUser +" " + LastNameUser);
        card_user_name_2.setText("Welcome back "+FisrtnameUser +" let's find you a job !" );
        new DashbordEmployer.DownloadImageTask((ImageView)card_img)
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


    public void postAnOffer(View view)
    {
        startActivity(new Intent(DashbordEmployer.this, PostAnOffer.class));
    }
    public void SearchForOffers(View view)
    {
        startActivity(new Intent(DashbordEmployer.this, SharchForOffer.class));
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(DashbordEmployer.this,WelcomeScreen.class));
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.nav_profile:
                Toast.makeText(this, "profile", Toast.LENGTH_SHORT).show();

                break;
            case R.id.nav_home:
                Toast.makeText(this, "home", Toast.LENGTH_SHORT).show();

                break;
            case R.id.nav_contact:
                Toast.makeText(this, "contact us", Toast.LENGTH_SHORT).show();

                break;
            case R.id.nav_share:
                Toast.makeText(this, "share", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_email:
                Toast.makeText(this, "inbox", Toast.LENGTH_SHORT).show();
                if(ListToRemove!=null)
                {
                    if(ListToRemove.size()>0)
                    {
                        for(String d:ListToRemove)
                        {
                            myRef  = database.getReference("EmployersInbox").child(IdUser).child(d);
                            myRef.removeValue();
                        }

                    }
                    ListToRemove.clear();

                }
                startActivity(new Intent(DashbordEmployer.this, InboxForEmployers.class));
                break;
            case R.id.nav_setting:
                startActivity(new Intent(DashbordEmployer.this, Settings.class));
                break;

        }
        return true;
    }

    private void setMyNavigatinBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);


        ActionBarDrawerToggle toggle =new ActionBarDrawerToggle(this,drawer,toolbar,R.string.togle_open,R.string.togle_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView =(NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View hView =  navigationView.getHeaderView(0);
        TextView nav_user = (TextView)hView.findViewById(R.id.haeder_name);
        TextView nav_email = (TextView)hView.findViewById(R.id.header_email);
        ImageView imageView=(ImageView)hView.findViewById(R.id.navImageUser);

        nav_user.setText(FisrtnameUser + " " + LastNameUser);
        nav_email.setText(EmailUser);
        new DashbordEmployer.DownloadImageTask((ImageView)imageView)
                .execute(PictureUser);

    }
}

package com.example.intest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.intest.function.GetOffersIds;
import com.example.intest.function.MatchingJob;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class DashbordStudent extends AppCompatActivity implements MyRecyclerViewAdapterForListMatchingOffers.ItemClickListener,
        NavigationView.OnNavigationItemSelectedListener{
    private String EmailUser,FisrtnameUser,LastNameUser,IdUser,PictureUser;
    private SharedPreferences userinfo;
    Handler handler = new Handler();
    FirebaseDatabase database;
    MyRecyclerViewAdapterForListMatchingOffers adapter;
    DatabaseReference myRef;
    /************ filled with matching******/
    List<String> ListIdsDomaine = new ArrayList<>();
    List<String> ListIdsType = new ArrayList<>();
    List<String> ListIdsRequirements = new ArrayList<>();
    List<String> ListIdsSkills = new ArrayList<>();
    List<String> ListIdsCities = new ArrayList<>();
    List<String> ListIdsPeriods = new ArrayList<>();
    HashMap<String,String> MatchingJobsAndAverage=new HashMap<>();
    HashMap<String,String> MatchingJobsIdsAndTitles=new HashMap<>();
    /************ to get from firebase******/
    public List<String> domainList;
    public List<String> typeList;
    public List<String> reqList;
    public List<String> skillsList;
    public List<String> periodeList;
    public List<String> citiesList;
    HashMap<String,String> Cv=new HashMap<>();
    /*********************To show******************/
    List<String> OfferTitlesList ;
    List<String> OfferAverageList ;
    List<String> OfferIdsList;
    /***************************************************/

/*****************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashbord_student);
        setAdds();
        getInfosFromSharedPreferences();
        instantiateViews();
        setMyNavigatinBar();
        database = FirebaseDatabase.getInstance();
        getCvFromFirebase();
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
    public void getInfosFromSharedPreferences()
    {
        userinfo=getSharedPreferences("userinfos", MODE_PRIVATE);

        EmailUser=userinfo.getString("email",null);
        FisrtnameUser=userinfo.getString("firstname",null);
        LastNameUser=userinfo.getString("lastname",null);
        IdUser=userinfo.getString("id",null);
        PictureUser=userinfo.getString("picture",null);
    }


    /***********************************recycle settings***********************************/

    @Override
    public void onItemClick(View view, int position) {
        Intent intent =new Intent(DashbordStudent.this,ApplayAnOffre.class);
        intent.putExtra("offerId",OfferIdsList.get(position));
        intent.putExtra("averageMatching",OfferAverageList.get(position));
        startActivity(intent);

    }
    private void matchingJob()
    {
        MatchingJob m=new MatchingJob(ListIdsDomaine,ListIdsType,ListIdsRequirements,ListIdsSkills,ListIdsCities,ListIdsPeriods);
        m.matchingJob();
        MatchingJobsAndAverage=m.getMatchingJobsAndAverage();
        MatchingJobsIdsAndTitles=m.getMatchingJobsIdsAndTitles();
        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                if(MatchingJobsIdsAndTitles.size()<ListIdsDomaine.size())
                {
                    handler.post(this);

                }else
                {
                    OfferAverageList=new ArrayList<>(MatchingJobsAndAverage.values());
                    OfferTitlesList=new ArrayList<>(MatchingJobsIdsAndTitles.values());
                    OfferIdsList=new ArrayList<>(MatchingJobsIdsAndTitles.keySet());
                    setRecycle();
                }

            }
        });




    }
    private void getOffersIds()
    {
        GetOffersIds r= new GetOffersIds(domainList,skillsList,reqList,typeList,citiesList,periodeList);
        r.getOfferIdsd();
        ListIdsDomaine=r.getListIdsDomaine();
        ListIdsSkills=r.getListIdsSkills();
        ListIdsRequirements=r.getListIdsRequirements();
        ListIdsType=r.getListIdsType();
        ListIdsCities=r.getListIdsCities();
        ListIdsPeriods=r.getListIdsPeriods();

        Thread thread = new Thread() {
            @Override
            public void run() {

                if(ListIdsDomaine.size()==0 || ListIdsSkills.size()==0  || ListIdsRequirements.size()==0 || ListIdsType.size()==0|| ListIdsCities.size()==0|| ListIdsPeriods.size()==0)
                {
                    handler.post(this);

                }else
                {
                    matchingJob();
                }
            }
        };

        thread.start();
    }


    public void getCvFromFirebase()
    {
        myRef  = database.getReference("Users").child(IdUser);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                   Cv.put(child.getKey(),child.getValue().toString());
                }
                domainList = Arrays.asList(Cv.get("Domaine").split("\\r?\\n"));
                typeList = Arrays.asList(Cv.get("Type_Shearched_Offer").split("\\r?\\n"));
                reqList = Arrays.asList(Cv.get("Diplomas").split("\\r?\\n"));
                skillsList = Arrays.asList(Cv.get("Skills").split("\\r?\\n"));
                periodeList = Arrays.asList(Cv.get("Periode").split("\\r?\\n"));
                citiesList = Arrays.asList(Cv.get("City").split("\\r?\\n"));



               getOffersIds();

            }
            @Override
            public void onCancelled(DatabaseError error) {}
        });
    }

    public void setRecycle()
    {
        RecyclerView recyclerView = findViewById(R.id.errorRy);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyRecyclerViewAdapterForListMatchingOffers(this,OfferTitlesList,OfferAverageList);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_student, menu);
        return true;
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

                break;
            case R.id.nav_setting:
                Toast.makeText(this, "setting", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(DashbordStudent.this, Settings.class));
                break;
            case R.id.nav_cv:
                Toast.makeText(this, "cv", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(DashbordStudent.this,CV_Display.class);
                intent.putExtra("candidateId",IdUser);
                startActivity(intent);

                break;


        }
        return true;
    }

    @Override

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {

            case R.id.advanced_shearch:
                startActivity(new Intent(DashbordStudent.this, SharchForOffer.class));
                return true;

        }


        return super.onOptionsItemSelected(item);
    }

    private void instantiateViews() {
        TextView card_user_name =(TextView) findViewById(R.id.card_user_name);
        TextView card_user_name_2 =(TextView) findViewById(R.id.card_user_name_2);
        ImageView card_img =(ImageView) findViewById(R.id.card_img);

        card_user_name.setText(FisrtnameUser +" " + LastNameUser);
        card_user_name_2.setText("Welcome back "+FisrtnameUser +" let's find you a job !" );
        new DashbordStudent.DownloadImageTask((ImageView)card_img)
                .execute(PictureUser);
    }


    private void setMyNavigatinBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);


        ActionBarDrawerToggle toggle =new ActionBarDrawerToggle(this,drawer,toolbar,R.string.togle_open,R.string.togle_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View hView =  navigationView.getHeaderView(0);
        TextView nav_user = (TextView)hView.findViewById(R.id.haeder_name);
        TextView nav_email = (TextView)hView.findViewById(R.id.header_email);
        ImageView imageView=(ImageView)hView.findViewById(R.id.navImageUser);

        nav_user.setText(FisrtnameUser + " " + LastNameUser);
        nav_email.setText(EmailUser);
        new DashbordStudent.DownloadImageTask((ImageView)imageView)
                .execute(PictureUser);

    }
}

package com.example.intest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListOfMatchingOffers extends AppCompatActivity implements MyRecyclerViewAdapterForListMatchingOffers.ItemClickListener{
    MyRecyclerViewAdapterForListMatchingOffers adapter;
    HashMap<String,String> MatchingJobsAndAverage=new HashMap<>();
    HashMap<String,String> MatchingJobsIdsAndTitles=new HashMap<>();
    List<String> OfferTitlesList ;
    List<String> OfferAverageList ;
    List<String> OfferAverageListCopy ;
    List<String> OfferIdsList;
    List<String> ListToRemove=new ArrayList<>();
    FirebaseDatabase database;
    DatabaseReference myRef;
    Handler handler = new Handler();
    public List<String> PicturesList;
    public List<String> CitiesList;
    public List<String> PaidOrNotList;
    public List<String> StartsAtList;
    public List<String> ReleaseDateList;
    public List<String> PeriodeList;
    public List<String> OfferPostersList;
    public List<String> CompanyNameList;
    public static ListOfMatchingOffers mVariable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.matching_offers_recycle);
        mVariable=this;
        setAdds();
        Intent intent=getIntent();
        database = FirebaseDatabase.getInstance();
        PicturesList=new ArrayList<String>();
        CitiesList=new ArrayList<String>();
        PaidOrNotList=new ArrayList<String>();
        StartsAtList=new ArrayList<String>();
        ReleaseDateList=new ArrayList<String>();
        PeriodeList=new ArrayList<String>();
        OfferPostersList=new ArrayList<String>();
        CompanyNameList=new ArrayList<String>();
        if(intent!=null)
        {
            MatchingJobsAndAverage=(HashMap<String, String>)intent.getSerializableExtra("MatchingJobsAndAverage");
            MatchingJobsIdsAndTitles=(HashMap<String, String>)intent.getSerializableExtra("MatchingJobsIdsAndTitles");
            OfferAverageList=new ArrayList<>(MatchingJobsAndAverage.values());
            OfferTitlesList=new ArrayList<>(MatchingJobsIdsAndTitles.values());
            OfferIdsList=new ArrayList<>(MatchingJobsIdsAndTitles.keySet());
            threadToForceWaitForOfferPosters(OfferIdsList.size());
        }

      //  sort();

    }
    public void threadToForceWaitForOfferPosters(final int size)
    {
        getOfferPosters(OfferIdsList);
        Thread thread = new Thread() {
            @Override
            public void run() {

                if(OfferPostersList.size()<size)
                {
                    handler.post(this);

                }else
                {
                    threadToForceWaitForCompanyIcon(size);
                }

            }
        };

        thread.start();
    }
    public void threadToForceWaitForCompanyIcon(final int size)
    {
        getCompanyIcon(OfferPostersList);
        Thread thread = new Thread() {
            @Override
            public void run() {

                if(PicturesList.size()<size)
                {
                    handler.post(this);

                }else
                {
                    threadToForceWaitForCompanyName(size);
                }

            }
        };

        thread.start();
    }
    public void threadToForceWaitForCompanyName(final int size)
    {
        getOfferCompanyName(OfferPostersList);
        Thread thread = new Thread() {
            @Override
            public void run() {

                if(CompanyNameList.size()<size)
                {
                    handler.post(this);

                }else
                {
                    threadToForceWaitForOfferCity(size);
                }

            }
        };

        thread.start();
    }
    public void threadToForceWaitForOfferCity(final int size)
    {
        getOfferCity(OfferIdsList);
        Thread thread = new Thread() {
            @Override
            public void run() {

                if(CitiesList.size()<size)
                {
                    handler.post(this);

                }else
                {
                    threadToForceWaitForOfferPaidOrNot(size);
                }

            }
        };

        thread.start();
    }
    public void threadToForceWaitForOfferPaidOrNot(final int size)
    {
        getOfferPaidOrNot(OfferIdsList);
        Thread thread = new Thread() {
            @Override
            public void run() {

                if(PaidOrNotList.size()<size)
                {
                    handler.post(this);

                }else
                {
                    threadToForceWaitForOfferReleaseDate(size);
                }

            }
        };

        thread.start();
    }
    public void threadToForceWaitForOfferReleaseDate(final int size)
    {
        getOfferReleaseDate(OfferIdsList);
        Thread thread = new Thread() {
            @Override
            public void run() {

                if(ReleaseDateList.size()<size)
                {
                    handler.post(this);

                }else
                {
                    threadToForceWaitForOfferStartsAt(size);
                }

            }
        };

        thread.start();
    }
    public void threadToForceWaitForOfferStartsAt(final int size)
    {
        getOfferStartsAt(OfferIdsList);
        Thread thread = new Thread() {
            @Override
            public void run() {

                if(StartsAtList.size()<size)
                {
                    handler.post(this);

                }else
                {
                    threadToForceWaitForOfferPeriode(size);
                }

            }
        };

        thread.start();
    }
    public void threadToForceWaitForOfferPeriode(final int size)
    {
        getOfferPeriode(OfferIdsList);
        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                if(PeriodeList.size()<size)
                {
                    handler.post(this);

                }else
                {
                    setRecycle();
                }

            }
        });


    }
    public void getOfferPosters(List<String> ListOfIds)
    {
        for(String id:ListOfIds)
        {
            myRef=database.getReference("Offers").child(id).child("Poster Id");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String value = dataSnapshot.getValue(String.class);
                    OfferPostersList.add(value);
                }
                @Override
                public void onCancelled(DatabaseError error) {}
            });
        }
    }
    public void getCompanyIcon(List<String> ListOfIds)
    {
        for(String id:ListOfIds)
        {
            myRef=database.getReference("Users").child(id).child("CompanyLogo");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String value = dataSnapshot.getValue(String.class);
                    PicturesList.add(value);
                }
                @Override
                public void onCancelled(DatabaseError error) {}
            });
        }
    }
    public void getOfferCompanyName(List<String> ListOfIds)
    {
        for(String id:ListOfIds)
        {
            myRef=database.getReference("Users").child(id).child("CompanyName");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String value = dataSnapshot.getValue(String.class);
                    CompanyNameList.add(value);
                }
                @Override
                public void onCancelled(DatabaseError error) {}
            });
        }
    }
    public void getOfferCity(List<String> ListOfIds)
    {
        for(String id:ListOfIds)
        {
            myRef=database.getReference("Offers").child(id).child("City");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String value = dataSnapshot.getValue(String.class);
                    CitiesList.add(value);
                }
                @Override
                public void onCancelled(DatabaseError error) {}
            });
        }
    }
    public void getOfferPaidOrNot(List<String> ListOfIds)
    {
        for(String id:ListOfIds)
        {
            myRef=database.getReference("Offers").child(id).child("Paid");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String value = dataSnapshot.getValue(String.class);
                    PaidOrNotList.add(value);
                }
                @Override
                public void onCancelled(DatabaseError error) {}
            });
        }
    }
    public void getOfferReleaseDate(List<String> ListOfIds)
    {
        for(String id:ListOfIds)
        {
            myRef=database.getReference("Offers").child(id).child("DateRelease");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String value = dataSnapshot.getValue(String.class);
                    ReleaseDateList.add(value);
                }
                @Override
                public void onCancelled(DatabaseError error) {}
            });
        }
    }

    public void getOfferStartsAt(List<String> ListOfIds)
    {
        for(String id:ListOfIds)
        {
            myRef=database.getReference("Offers").child(id).child("StartDate");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String value = dataSnapshot.getValue(String.class);
                    StartsAtList.add(value);
                }
                @Override
                public void onCancelled(DatabaseError error) {}
            });
        }
    }
    public void getOfferPeriode(List<String> ListOfIds)
    {
        for(String id:ListOfIds)
        {
            myRef=database.getReference("Offers").child(id).child("Periode");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String value = dataSnapshot.getValue(String.class);
                    PeriodeList.add(value);
                }
                @Override
                public void onCancelled(DatabaseError error) {}
            });
        }
    }

    @Override
    public void onItemClick(View view, int position) {

        Intent intent =new Intent(ListOfMatchingOffers.this,ApplayAnOffre.class);
        intent.putExtra("offerId",OfferIdsList.get(position));
        intent.putExtra("averageMatching",OfferAverageList.get(position));
        startActivity(intent);
    }
    public void setRecycle()
    {
        RecyclerView recyclerView = findViewById(R.id.errorRy);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyRecyclerViewAdapterForListMatchingOffers(this,OfferTitlesList,OfferAverageList,PicturesList,CitiesList
                ,PaidOrNotList,StartsAtList,ReleaseDateList,PeriodeList,CompanyNameList);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

    }
    public void sort()
    {OfferAverageListCopy=new ArrayList<>(MatchingJobsAndAverage.values());
        double lastAv=0;
        int index=0;
        for (String average:OfferAverageListCopy)
        {
            double avInDouble=Double.valueOf(average.trim());
            if(avInDouble>=lastAv)
            {
                lastAv=avInDouble;
                index=OfferAverageList.indexOf(average);


                OfferAverageList.remove(index);
                OfferAverageList.add(0,average);

                OfferTitlesList.remove(index);
                OfferTitlesList.add(0,average);

                OfferIdsList.remove(index);
                OfferIdsList.add(0,average);
            }

        }
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
    public static ListOfMatchingOffers getInstance()
    {
        return mVariable;
    }



}

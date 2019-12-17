package com.example.intest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

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

public class InboxForEmployers extends AppCompatActivity implements MyRecyclerViewAdapterForMyInbox.ItemClickListener{
    MyRecyclerViewAdapterForMyInbox adapter;
    FirebaseDatabase database;
    DatabaseReference myRef;
    HashMap<String,String> MatchingCandidatesAndAverage=new HashMap<>();
    List<String> MatchingCandidatesListIds;
    List<String> AverageCandidatesList;
    Handler handler = new Handler();
    private String userId;
    private SharedPreferences userinfo;
    List<String> CandidatesFirstNames=new ArrayList<>();
    List<String> CandidatesLastNames=new ArrayList<>();
    List<String> CandidatesFullNames=new ArrayList<>();
    List<String> CandidatesPictures=new ArrayList<>();
    List<String> CandidatesCities=new ArrayList<>();
    List<String> CandidatesSchool=new ArrayList<>();
    List<String> CandidatesSchoolYear=new ArrayList<>();
    List<String> CandidatesDegree=new ArrayList<>();

    List<String> ListToRemove=new ArrayList<>();
    public static InboxForEmployers mVariable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.matching_candidates_recycle);
        setAdds();
        mVariable=this;
        userinfo=getSharedPreferences("userinfos", MODE_PRIVATE);
        userId=userinfo.getString("id",null);
        database = FirebaseDatabase.getInstance();

       getMatchingCandidatesIds();
    }
    public void setRecycle()
    {
        RecyclerView recyclerView = findViewById(R.id.errorRy);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyRecyclerViewAdapterForMyInbox(this,CandidatesFullNames,AverageCandidatesList,CandidatesPictures,CandidatesCities
        ,CandidatesSchool,CandidatesSchoolYear,CandidatesDegree);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

    }


    @Override
    public void onItemClick(View view, int position) {
        Intent intent=new Intent(InboxForEmployers.this, CV_Display.class);
        intent.putExtra("candidateId",MatchingCandidatesListIds.get(position));
           startActivity(intent);
    }


    public  void getMatchingCandidatesIds()
    {
        myRef=database.getReference("EmployersInbox").child(userId);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                   MatchingCandidatesAndAverage.put(child.getKey(),child.getValue().toString());
                }
                MatchingCandidatesListIds= new ArrayList<>(MatchingCandidatesAndAverage.keySet());
                AverageCandidatesList= new ArrayList<>(MatchingCandidatesAndAverage.values());
                threadToForceWaitForFirstName(MatchingCandidatesListIds.size());
            }
            @Override
            public void onCancelled(DatabaseError error) {}
        });
    }
    public void threadToForceWaitForFirstName(final int size)
    {
        getMatchingCandidatesFirstNames();
        Thread thread = new Thread() {
            @Override
            public void run() {

                if(CandidatesFirstNames.size()<size)
                {
                    handler.post(this);

                }else
                {
                    threadToForceWaitForLastName(size);
                }

            }
        };

        thread.start();
    }
    public void threadToForceWaitForLastName(final int size)
    {
        getMatchingCandidatesLastNames();
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if(CandidatesLastNames.size()<size)
                {
                    handler.post(this);

                }else
                {
                    threadToForceWaitForPictures(size);
                }

            }
        });

    }
    public void threadToForceWaitForPictures(final int size)
    {
        getMatchingCandidatesPictures();
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if(CandidatesPictures.size()<size)
                {
                    handler.post(this);

                }else
                {
                    threadToForceWaitForCities(size);
                }

            }
        });

    }
    public void threadToForceWaitForCities(final int size)
    {
        getMatchingCandidatesCities();
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if(CandidatesCities.size()<size)
                {
                    handler.post(this);

                }else
                {
                    threadToForceWaitForSchools(size);
                }

            }
        });

    }
    public void threadToForceWaitForSchools(final int size)
    {
        getMatchingCandidatesSchool();
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if(CandidatesSchool.size()<size)
                {
                    handler.post(this);

                }else
                {
                    threadToForceWaitForSchoolYear(size);
                }

            }
        });

    }
    public void threadToForceWaitForSchoolYear(final int size)
    {
        getMatchingCandidatesSchoolYear();
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if(CandidatesSchoolYear.size()<size)
                {
                    handler.post(this);

                }else
                {
                    threadToForceWaitForDegree(size);
                }

            }
        });

    }
    public void threadToForceWaitForDegree(final int size)
    {
        getMatchingCandidatesDegree();
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if(CandidatesDegree.size()<size)
                {
                    handler.post(this);

                }else
                {
                    showCompleteName();
                }

            }
        });

    }
    public void getMatchingCandidatesFirstNames()
    {
        for(String id:MatchingCandidatesListIds)
        {
            myRef=database.getReference("Users").child(id).child("FirstName");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String value = dataSnapshot.getValue(String.class);
                    CandidatesFirstNames.add(value);
                }
                @Override
                public void onCancelled(DatabaseError error) {}
            });
        }
    }
    public void getMatchingCandidatesLastNames()
    {
        for(String id:MatchingCandidatesListIds)
        {
            myRef=database.getReference("Users").child(id).child("LastName");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String value = dataSnapshot.getValue(String.class);
                    CandidatesLastNames.add(value);
                }
                @Override
                public void onCancelled(DatabaseError error) {}
            });
        }
    }
    public void getMatchingCandidatesPictures()
    {
        for(String id:MatchingCandidatesListIds)
        {
            myRef=database.getReference("Users").child(id).child("Picture");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String value = dataSnapshot.getValue(String.class);
                    CandidatesPictures.add(value);
                }
                @Override
                public void onCancelled(DatabaseError error) {}
            });
        }
    }
    public void getMatchingCandidatesCities()
    {
        for(String id:MatchingCandidatesListIds)
        {
            myRef=database.getReference("Users").child(id).child("City");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String value = dataSnapshot.getValue(String.class);
                    CandidatesCities.add(value);
                }
                @Override
                public void onCancelled(DatabaseError error) {}
            });
        }
    }
    public void getMatchingCandidatesSchool()
    {
        for(String id:MatchingCandidatesListIds)
        {
            myRef=database.getReference("Users").child(id).child("School");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String value = dataSnapshot.getValue(String.class);
                    CandidatesSchool.add(value);
                }
                @Override
                public void onCancelled(DatabaseError error) {}
            });
        }
    }
    public void getMatchingCandidatesSchoolYear()
    {
        for(String id:MatchingCandidatesListIds)
        {
            myRef=database.getReference("Users").child(id).child("SchoolYear");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String value = dataSnapshot.getValue(String.class);
                    CandidatesSchoolYear.add(value);
                }
                @Override
                public void onCancelled(DatabaseError error) {}
            });
        }
    }
    public void getMatchingCandidatesDegree()
    {
        for(String id:MatchingCandidatesListIds)
        {
            myRef=database.getReference("Users").child(id).child("Degree");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String value = dataSnapshot.getValue(String.class);
                    CandidatesDegree.add(value);
                }
                @Override
                public void onCancelled(DatabaseError error) {}
            });
        }
    }
    public void showCompleteName()
    {

        int size=CandidatesLastNames.size();
        for(int i=0;i<size;i++)
        {
            CandidatesFullNames.add(CandidatesLastNames.get(i)+" "+CandidatesFirstNames.get(i));
        }
        setRecycle();
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
    public static InboxForEmployers getInstance()
    {
        return mVariable;
    }
    public void addToListRemove(int position)
    {
        String id=MatchingCandidatesListIds.get(position);
        ListToRemove.add(id);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(InboxForEmployers.this,DashbordEmployer.class);
        intent.putStringArrayListExtra("ListToRemove",(ArrayList<String>)ListToRemove);
       startActivity(intent);
    }
}

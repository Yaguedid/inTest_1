package com.example.intest.function;

import android.content.Intent;

import com.example.intest.ListOfMatchingOffers;
import com.example.intest.SharchForOffer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class MatchingJob {


    List<String> ListIdsDomaineThis = new ArrayList<>();
    List<String> ListIdsTypeThis = new ArrayList<>();
    List<String> ListIdsRequirementsThis = new ArrayList<>();
    List<String> ListIdsSkillsThis = new ArrayList<>();
    List<String> ListIdsCitiesThis = new ArrayList<>();
    List<String> ListIdsPeriodsThis = new ArrayList<>();

    List<String> ListOfMatchingJobs = new ArrayList<>();
    List<Double> ListOfIndexes=new ArrayList<>();
    private final int maxMatchingPoints=20;
    private final int typeofOfferFactor=2;
    private final int skillsOfferFactor=1;
    private final int RequiOfferFactor=3;
    private final int CityOfferFactor=2;
    private final int PeriodOfferFactor=2;
    private HashMap<String,String> MatchingJobsAndAverage=new HashMap<>();
    private HashMap<String,String> MatchingJobsIdsAndTitles=new HashMap<>();
    FirebaseDatabase database;
    DatabaseReference offerSettingsRef,offerIdRef;

    public MatchingJob(List<String> listIdsDomaine, List<String> listIdsType, List<String> listIdsRequirements, List<String> listIdsSkills,
                       List<String> listIdsCity,List<String> listIdsPeriod ) {
        ListIdsDomaineThis = listIdsDomaine;
        ListIdsTypeThis = listIdsType;
        ListIdsRequirementsThis = listIdsRequirements;
        ListIdsSkillsThis = listIdsSkills;
        ListIdsCitiesThis=listIdsCity;
        ListIdsPeriodsThis=listIdsPeriod;
        database = FirebaseDatabase.getInstance();
    }

    public MatchingJob() {
    }

    public void matchingJob()
    {
        double matchingScore=0;

        for (String domain: ListIdsDomaineThis)
        {
            if(ListIdsRequirementsThis.contains(domain))
            {
                int occurrences = Collections.frequency(ListIdsRequirementsThis, domain);
                matchingScore+=RequiOfferFactor*occurrences;
            }
            if(ListIdsSkillsThis.contains(domain))
            {
                int occurrences = Collections.frequency(ListIdsSkillsThis, domain);
                matchingScore+=skillsOfferFactor*occurrences;
            }
            if(ListIdsTypeThis.contains(domain))
            {
                int occurrences = Collections.frequency(ListIdsTypeThis, domain);
                matchingScore+=typeofOfferFactor*occurrences;
            }
            if(ListIdsPeriodsThis.contains(domain))
            {
                int occurrences = Collections.frequency(ListIdsPeriodsThis, domain);
                matchingScore+=PeriodOfferFactor*occurrences;
            }
            if(ListIdsCitiesThis.contains(domain))
            {
                int occurrences = Collections.frequency(ListIdsCitiesThis, domain);
                matchingScore+=CityOfferFactor*occurrences;
            }
            ListOfIndexes.add(matchingScore);
            Collections.sort(ListOfIndexes, Collections.reverseOrder());
            ListOfMatchingJobs.add(domain);
            Double average=matchingScore/maxMatchingPoints;
            average=average*100;
            MatchingJobsAndAverage.put(domain,String.valueOf(average));
            if(matchingScore>=ListOfIndexes.get(0))
            {
                int index = ListOfMatchingJobs.indexOf(domain);
                ListOfMatchingJobs.remove(index);
                ListOfMatchingJobs.add(0, domain);
            }
            getOfferTitle(domain);

            matchingScore=0;
        }

    }
    public void getOfferTitle(final String id)
    {
        offerSettingsRef=database.getReference("Offers").child(id).child("Title");
        offerSettingsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                MatchingJobsIdsAndTitles.put(id,value);


            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        });

    }


    public HashMap<String, String> getMatchingJobsAndAverage() {
        return MatchingJobsAndAverage;
    }

    public HashMap<String, String> getMatchingJobsIdsAndTitles() {
        return MatchingJobsIdsAndTitles;
    }
}

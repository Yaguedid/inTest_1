package com.example.intest.function;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GetOffersIds {
    public List<String> domainListme=new ArrayList<>();
    public List<String> typeListme=new ArrayList<>();
    public List<String> reqListme=new ArrayList<>();
    public List<String> skillsListme=new ArrayList<>();
    public List<String> citiesListme=new ArrayList<>();
    public List<String> periodsListme=new ArrayList<>();

    List<String> ListIdsDomaine = new ArrayList<>();
    List<String> ListIdsType = new ArrayList<>();
    List<String> ListIdsRequirements = new ArrayList<>();
    List<String> ListIdsSkills = new ArrayList<>();
    List<String> ListIdsCities = new ArrayList<>();
    List<String> ListIdsPeriods = new ArrayList<>();

    FirebaseDatabase database;
    DatabaseReference offerSettingsRef;



    public GetOffersIds(){

   }
    public GetOffersIds(List<String> domainList, List<String> skillsList, List<String> reqList, List<String> typeList,
                        List<String> cityList,List<String> periodList){
        domainListme=domainList;
        reqListme=reqList;
        skillsListme=skillsList;
        typeListme=typeList;
        citiesListme=cityList;
        periodsListme=periodList;
        database = FirebaseDatabase.getInstance();

    }

    public void getOfferIdsd(){
        for(String domain:domainListme)
        {
            offerSettingsRef=database.getReference("Offer Domains").child(domain);
            offerSettingsRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot child: dataSnapshot.getChildren()) {
                        ListIdsDomaine.add(child.getValue().toString());


                    }

                }
                @Override
                public void onCancelled(DatabaseError error) {}
            });


        }

        /**************/
        /***Skills ***/
        for(String skill:skillsListme)
        {
            offerSettingsRef=database.getReference("Offer Skills").child(skill);
            offerSettingsRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot child: dataSnapshot.getChildren()) {
                        ListIdsSkills.add(child.getValue().toString());

                    }
                }
                @Override
                public void onCancelled(DatabaseError error) {}
            });

        }

        /**************/
        /***Requirements ***/

        for(String req:reqListme)
        {
            offerSettingsRef=database.getReference("Offer Requirements").child(req);
            offerSettingsRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot child: dataSnapshot.getChildren()) {
                        ListIdsRequirements.add(child.getValue().toString());

                    }
                }
                @Override
                public void onCancelled(DatabaseError error) {}
            });

        }
        /**************/
        /***offer Type ***/
        for(String type:typeListme)
        {
            offerSettingsRef=database.getReference("Offer Type").child(type);
            offerSettingsRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot child: dataSnapshot.getChildren()) {
                        ListIdsType.add(child.getValue().toString());

                    }

                }
                @Override
                public void onCancelled(DatabaseError error) {}
            });

        }
        for(String city:citiesListme)
        {
            offerSettingsRef=database.getReference("Offer City").child(city);
            offerSettingsRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot child: dataSnapshot.getChildren()) {
                        ListIdsCities.add(child.getValue().toString());

                    }

                }
                @Override
                public void onCancelled(DatabaseError error) {}
            });

        }
        for(String period:periodsListme)
        {
            offerSettingsRef=database.getReference("Offer Period").child(period);
            offerSettingsRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot child: dataSnapshot.getChildren()) {
                        ListIdsPeriods.add(child.getValue().toString());

                    }

                }
                @Override
                public void onCancelled(DatabaseError error) {}
            });

        }

    }

    public List<String> getListIdsDomaine() {
        return ListIdsDomaine;
    }

    public List<String> getListIdsType() {
        return ListIdsType;
    }

    public List<String> getListIdsRequirements() {
        return ListIdsRequirements;
    }

    public List<String> getListIdsSkills() {
        return ListIdsSkills;
    }

    public List<String> getListIdsCities() {
        return ListIdsCities;
    }

    public List<String> getListIdsPeriods() {
        return ListIdsPeriods;
    }
}

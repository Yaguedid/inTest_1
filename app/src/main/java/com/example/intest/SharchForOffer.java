package com.example.intest;
import com.example.intest.function.GetOffersIds;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.intest.function.MatchingJob;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class SharchForOffer extends AppCompatActivity {

    public List<Integer> mDomainItems = new ArrayList<>();
    public List<Integer> mTypeItems = new ArrayList<>();
    public List<Integer> mReqItems = new ArrayList<>();
    public List<Integer> mSkillsrItems = new ArrayList<>();
    public List<Integer> mCitiesItems = new ArrayList<>();
    public List<Integer> mPeriodsItems = new ArrayList<>();

    public List<String> domainList;
    public List<String> typeList;
    public List<String> reqList;
    public List<String> skillsList;
    public List<String> periodeList;
    public List<String> citiesList;
    public Boolean firstTime=true;
    Handler handler = new Handler();
    public CountDownLatch done;
    List<String> ListToRemove=new ArrayList<>();

    TextView domaineItemSelected,TypeItemSelected,requiremtnItemSelected,skillsItemSelected,cityItemSelected,periodeItemSelected;
    Button domaineItemsBtn,TypeItemsBtn,RequirementsItemsBtn,skillsItemsBtn,cityItmesBtn,periodeItemsBtn,poustulerBtn;
    String[] domaineListItems,TypeListItems,requirementListItems,skillsListItems,cityListItems,periodeListItems;


    private EditText TittleOffre;

    /******************/
    FirebaseDatabase database;
    DatabaseReference offerSettingsRef,offerIdRef;
    private SharedPreferences userinfo;
    public SharedPreferences sharedPreferences;
    public  SharedPreferences.Editor editors;
    private String offerid;
    private String EmailUser, FisrtnameUser, LastNameUser, IdUser, PictureUser;
    TextView FirstNameView, LastNameView,OfferTitleView,OfferDetailtsView;
    ImageView PictureView;
    List<String> ListIdsDomaine = new ArrayList<>();
    List<String> ListIdsType = new ArrayList<>();
    List<String> ListIdsRequirements = new ArrayList<>();
    List<String> ListIdsSkills = new ArrayList<>();
    List<String> ListIdsCities = new ArrayList<>();
    List<String> ListIdsPeriods = new ArrayList<>();
   HashMap<String,String> MatchingJobsAndAverage=new HashMap<>();
    HashMap<String,String> MatchingJobsIdsAndTitles=new HashMap<>();

    /************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sharch_for_offer);
setAdds();
        domainList=new ArrayList<>();
        reqList=new ArrayList<>();
        skillsList=new ArrayList<>();
        typeList=new ArrayList<>();
        periodeList=new ArrayList<>();
        citiesList=new ArrayList<>();
        database = FirebaseDatabase.getInstance();



/****************************get user info ******************/
        userinfo=getSharedPreferences("userinfos", MODE_PRIVATE);

        EmailUser=userinfo.getString("email",null);
        FisrtnameUser=userinfo.getString("firstname",null);
        LastNameUser=userinfo.getString("lastname",null);
        IdUser=userinfo.getString("id",null);
        PictureUser=userinfo.getString("picture",null);
        instanciateViews();
/*********************************************/
    }
    public void instanciateViews()
    {
        TextView card_user_name =(TextView) findViewById(R.id.card_user_name);
        TextView card_user_name_2 =(TextView) findViewById(R.id.card_user_name_2);
        ImageView card_img =(ImageView) findViewById(R.id.card_img);

        card_user_name.setText(FisrtnameUser +" " + LastNameUser);
        card_user_name_2.setText("Welcome back "+FisrtnameUser +" let's find you a job !" );
        new SharchForOffer.DownloadImageTask((ImageView)card_img)
                .execute(PictureUser);


        cityItemSelected=findViewById(R.id.id_TextVillePrefere);
        periodeItemSelected=findViewById(R.id.id_TextPeriodeStage);


        OfferTitleView=findViewById(R.id.id_TittleOffre);
        OfferDetailtsView=findViewById(R.id.offerBody);

        cityItmesBtn=findViewById(R.id.id_Ville_prefere);
        periodeItemsBtn=findViewById(R.id.id_periode_de_stage);

        /* +++ Domaine +++ */
        domaineItemsBtn = (Button) findViewById(R.id.id_Domain);
        domaineItemSelected = (TextView) findViewById(R.id.id_TextDomaine);
        domaineListItems = getResources().getStringArray(R.array.domaine_item);

        /* +++ Types +++ */
        TypeItemsBtn = (Button) findViewById(R.id.id_Type);
        TypeItemSelected = (TextView) findViewById(R.id.id_TextType);
        TypeListItems = getResources().getStringArray(R.array.type_item);

        /* +++ Requirement +++ */
        RequirementsItemsBtn = (Button) findViewById(R.id.id_Requiremments);
        requiremtnItemSelected = (TextView) findViewById(R.id.id_TextRequiremments);
        requirementListItems = getResources().getStringArray(R.array.requirement_item);

        /* +++ Skills +++ */
        skillsItemsBtn = (Button) findViewById(R.id.id_Skills);
        skillsItemSelected = (TextView) findViewById(R.id.id_TextSkills);
        skillsListItems = getResources().getStringArray(R.array.skills_item);
        cityListItems=getResources().getStringArray(R.array.city_item);
        periodeListItems=getResources().getStringArray(R.array.period_item);
        /* -- poustuler btn --*/

        poustulerBtn = (Button) findViewById(R.id.postule_Btn);


        /* -- Title offre --*/

        TittleOffre = (EditText) findViewById(R.id.id_TittleOffre);





        domaineItemsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showListItemes(domaineListItems,domaineItemSelected);



            }
        });

        TypeItemsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showTypeListItemes(TypeListItems,TypeItemSelected);



            }
        });



        RequirementsItemsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                showReqListItemes(requirementListItems,requiremtnItemSelected);

            }
        });

        skillsItemsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showSkillsListItemes(skillsListItems,skillsItemSelected);
            }
        });
        cityItmesBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            showCitiesListItemes(cityListItems,cityItemSelected);
        }
    });

        periodeItemsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showPeriodListItemes(periodeListItems,periodeItemSelected);
            }
        });


        poustulerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                postuler();


                Log.d("test ",""+domainList.size());
                Log.d("test ",""+typeList.size());
                Log.d("req ",""+reqList.size());
                Log.d("ski ",""+skillsList.size());

            }
        });
    }
    private void postuler(){


        if(domainList.isEmpty() || typeList.isEmpty() || reqList.isEmpty() || skillsList.isEmpty())
        {
            if (domainList.isEmpty()) {
                domaineItemSelected.setText("please select a choice !");
                domaineItemSelected.setTextColor(getResources().getColor(R.color.colorAccent));
            }
            if (typeList.isEmpty()) {
                TypeItemSelected.setText("please select a choice !");
                TypeItemSelected.setTextColor(getResources().getColor(R.color.colorAccent));
            }
            if (reqList.isEmpty()) {
                requiremtnItemSelected.setText("please select a choice !");
                requiremtnItemSelected.setTextColor(getResources().getColor(R.color.colorAccent));
            }
            if (skillsList.isEmpty()) {
                skillsItemSelected.setText("please select a choice !");
                skillsItemSelected.setTextColor(getResources().getColor(R.color.colorAccent));
            }
            if (citiesList.isEmpty()) {
                cityItemSelected.setText("please select a choice !");
                cityItemSelected.setTextColor(getResources().getColor(R.color.colorAccent));
            }
            if (periodeList.isEmpty()) {
                periodeItemSelected.setText("please select a choice !");
                periodeItemSelected.setTextColor(getResources().getColor(R.color.colorAccent));
            }

        }else
        {

            getOffersIds();

        }
    }

    private void showListItemes(final String[] itemsResTab, final TextView textView){

        final boolean[] checkedItems;

        checkedItems = new boolean[itemsResTab.length];

        domainList= new ArrayList<>();

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(SharchForOffer.this);

        mBuilder.setTitle(R.string.dialog_title);

        mBuilder.setMultiChoiceItems(itemsResTab, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {

                if(isChecked){
                    mDomainItems.add(position);
                }else{
                    mDomainItems.remove((Integer.valueOf(position)));
                }
            }
        });

        mBuilder.setCancelable(false);

        mBuilder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {

                String item = "";
                for (int i = 0; i < mDomainItems.size(); i++) {
                    if(! domainList.contains(itemsResTab[mDomainItems.get(i)] )) {
                        item = item + itemsResTab[mDomainItems.get(i)] + ", ";
                        domainList.add(itemsResTab[mDomainItems.get(i)]);

                    }
                }
                if(item.equals("")){
                    textView.setText("no items selected !");
                    textView.setTextColor(getResources().getColor(R.color.colorAccent));
                }else {
                    textView.setText(item);
                    textView.setTextColor(getResources().getColor(R.color.colorPrimary));}
            }
        });

        mBuilder.setNegativeButton(R.string.dismiss_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        mBuilder.setNeutralButton(R.string.clear_all_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                domainList.clear();
                for (int i = 0; i < checkedItems.length; i++) {
                    checkedItems[i] = false;}

                mDomainItems.clear();
                textView.setText("no items selected !");
                textView.setTextColor(getResources().getColor(R.color.colorAccent));


            }
        });

        AlertDialog mDialog = mBuilder.create();
        mDialog.show();

    }
    /* --------------------------------------------------------------------------------------------------------------------------------*/
    private void showReqListItemes(final String[] itemsResTab, final TextView textView){

        final boolean[] checkedReqItems;

        checkedReqItems = new boolean[itemsResTab.length];

        reqList= new ArrayList<>();
        //   reqList= new ArrayList<>();
        // skillsList= new ArrayList<>();



        AlertDialog.Builder mBuilder = new AlertDialog.Builder(SharchForOffer.this);

        mBuilder.setTitle(R.string.dialog_title);

        mBuilder.setMultiChoiceItems(itemsResTab, checkedReqItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {

                if(isChecked){
                    mReqItems.add(position);
                }else{
                    mReqItems.remove((Integer.valueOf(position)));
                }
            }
        });

        mBuilder.setCancelable(false);

        mBuilder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {

                String item = "";
                for (int i = 0; i < mReqItems.size(); i++) {
                    if(! reqList.contains(itemsResTab[mReqItems.get(i)] )) {
                        item = item + itemsResTab[mReqItems.get(i)] + ", ";
                        reqList.add(itemsResTab[mReqItems.get(i)]);

                    }
                }
                if(item.equals("")){
                    textView.setText("no items selected !");
                    textView.setTextColor(getResources().getColor(R.color.colorAccent));
                }else {
                    textView.setText(item);
                    textView.setTextColor(getResources().getColor(R.color.colorPrimary));}

            }
        });

        mBuilder.setNegativeButton(R.string.dismiss_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        mBuilder.setNeutralButton(R.string.clear_all_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                reqList.clear();
                for (int i = 0; i < checkedReqItems.length; i++) {
                    checkedReqItems[i] = false;}

                mReqItems.clear();

                textView.setText("no items selected !");
                textView.setTextColor(getResources().getColor(R.color.colorAccent));



            }
        });

        AlertDialog mDialog = mBuilder.create();
        mDialog.show();

    }
    /* --------------------------------------------------------------------------------------------------------------------------------*/
    private void showSkillsListItemes(final String[] itemsResTab, final TextView textView){

        final boolean[] checkedskillsItems;

        checkedskillsItems = new boolean[itemsResTab.length];

        skillsList= new ArrayList<>();
        //   reqList= new ArrayList<>();
        // skillsList= new ArrayList<>();



        AlertDialog.Builder mBuilder = new AlertDialog.Builder(SharchForOffer.this);

        mBuilder.setTitle(R.string.dialog_title);

        mBuilder.setMultiChoiceItems(itemsResTab, checkedskillsItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {

                if(isChecked){
                    mSkillsrItems.add(position);
                }else{
                    mSkillsrItems.remove((Integer.valueOf(position)));
                }
            }
        });

        mBuilder.setCancelable(false);

        mBuilder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {

                String item = "";
                for (int i = 0; i < mSkillsrItems.size(); i++) {
                    if(! skillsList.contains(itemsResTab[mSkillsrItems.get(i)] )) {
                        item = item + itemsResTab[mSkillsrItems.get(i)] + ", ";
                        skillsList.add(itemsResTab[mSkillsrItems.get(i)]);

                    }
                }


                if(item.equals("")){
                    textView.setText("no items selected !");
                    textView.setTextColor(getResources().getColor(R.color.colorAccent));
                }else {
                    textView.setText(item);
                    textView.setTextColor(getResources().getColor(R.color.colorPrimary));}


            }
        });

        mBuilder.setNegativeButton(R.string.dismiss_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        mBuilder.setNeutralButton(R.string.clear_all_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                skillsList.clear();
                for (int i = 0; i < checkedskillsItems.length; i++) {
                    checkedskillsItems[i] = false;}

                mSkillsrItems.clear();

                textView.setText("no items selected !");
                textView.setTextColor(getResources().getColor(R.color.colorAccent));





            }
        });

        AlertDialog mDialog = mBuilder.create();
        mDialog.show();

    }
    /* --------------------------------------------------------------------------------------------------------------------------------*/
    private void showTypeListItemes(final String[] itemsResTab, final TextView textView){

        final boolean[] checkedTypesItems;

        checkedTypesItems = new boolean[itemsResTab.length];

        typeList= new ArrayList<>();




        AlertDialog.Builder mBuilder = new AlertDialog.Builder(SharchForOffer.this);

        mBuilder.setTitle(R.string.dialog_title);

        mBuilder.setMultiChoiceItems(itemsResTab, checkedTypesItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {

                if(isChecked){
                    mTypeItems.add(position);
                }else{
                    mTypeItems.remove((Integer.valueOf(position)));
                }
            }
        });

        mBuilder.setCancelable(false);

        mBuilder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {

                String item = "";
                for (int i = 0; i < mTypeItems.size(); i++) {
                    if(! typeList.contains(itemsResTab[mTypeItems.get(i)] )) {
                        item = item + itemsResTab[mTypeItems.get(i)] + ", ";
                        typeList.add(itemsResTab[mTypeItems.get(i)]);

                    }
                }


                if(item.equals("")){
                    textView.setText("no items selected !");
                    textView.setTextColor(getResources().getColor(R.color.colorAccent));
                }else {
                    textView.setText(item);
                    textView.setTextColor(getResources().getColor(R.color.colorPrimary));}


            }
        });

        mBuilder.setNegativeButton(R.string.dismiss_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        mBuilder.setNeutralButton(R.string.clear_all_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                typeList.clear();
                for (int i = 0; i < checkedTypesItems.length; i++) {
                    checkedTypesItems[i] = false;}

                mTypeItems.clear();

                textView.setText("no items selected !");
                textView.setTextColor(getResources().getColor(R.color.colorAccent));





            }
        });

        AlertDialog mDialog = mBuilder.create();
        mDialog.show();

    }
    /*----------------------------------------------------------------------------------------------------------------*/
    private void showCitiesListItemes(final String[] itemsResTab, final TextView textView){

        final boolean[] checkedskillsItems;

        checkedskillsItems = new boolean[itemsResTab.length];

        citiesList= new ArrayList<>();
        //   reqList= new ArrayList<>();
        // skillsList= new ArrayList<>();



        AlertDialog.Builder mBuilder = new AlertDialog.Builder(SharchForOffer.this);

        mBuilder.setTitle(R.string.dialog_title);

        mBuilder.setMultiChoiceItems(itemsResTab, checkedskillsItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {

                if(isChecked){
                    mCitiesItems.add(position);
                }else{
                    mCitiesItems.remove((Integer.valueOf(position)));
                }
            }
        });

        mBuilder.setCancelable(false);

        mBuilder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {

                String item = "";
                for (int i = 0; i < mCitiesItems.size(); i++) {
                    if(! citiesList.contains(itemsResTab[mCitiesItems.get(i)] )) {
                        item = item + itemsResTab[mCitiesItems.get(i)] + ", ";
                        citiesList.add(itemsResTab[mCitiesItems.get(i)]);

                    }
                }


                if(item.equals("")){
                    textView.setText("no items selected !");
                    textView.setTextColor(getResources().getColor(R.color.colorAccent));
                }else {
                    textView.setText(item);
                    textView.setTextColor(getResources().getColor(R.color.colorPrimary));}


            }
        });

        mBuilder.setNegativeButton(R.string.dismiss_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        mBuilder.setNeutralButton(R.string.clear_all_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                citiesList.clear();
                for (int i = 0; i < checkedskillsItems.length; i++) {
                    checkedskillsItems[i] = false;}

                mCitiesItems.clear();

                textView.setText("no items selected !");
                textView.setTextColor(getResources().getColor(R.color.colorAccent));





            }
        });

        AlertDialog mDialog = mBuilder.create();
        mDialog.show();

    }
    /*----------------------------------------------------------------------------------------------------------------*/
    private void showPeriodListItemes(final String[] itemsResTab, final TextView textView){

        final boolean[] checkedskillsItems;

        checkedskillsItems = new boolean[itemsResTab.length];

        periodeList= new ArrayList<>();
        //   reqList= new ArrayList<>();
        // skillsList= new ArrayList<>();



        AlertDialog.Builder mBuilder = new AlertDialog.Builder(SharchForOffer.this);

        mBuilder.setTitle(R.string.dialog_title);

        mBuilder.setMultiChoiceItems(itemsResTab, checkedskillsItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {

                if(isChecked){
                    mPeriodsItems.add(position);
                }else{
                    mPeriodsItems.remove((Integer.valueOf(position)));
                }
            }
        });

        mBuilder.setCancelable(false);

        mBuilder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {

                String item = "";
                for (int i = 0; i < mPeriodsItems.size(); i++) {
                    if(! periodeList.contains(itemsResTab[mPeriodsItems.get(i)] )) {
                        item = item + itemsResTab[mPeriodsItems.get(i)] + ", ";
                        periodeList.add(itemsResTab[mPeriodsItems.get(i)]);

                    }
                }


                if(item.equals("")){
                    textView.setText("no items selected !");
                    textView.setTextColor(getResources().getColor(R.color.colorAccent));
                }else {
                    textView.setText(item);
                    textView.setTextColor(getResources().getColor(R.color.colorPrimary));}


            }
        });

        mBuilder.setNegativeButton(R.string.dismiss_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        mBuilder.setNeutralButton(R.string.clear_all_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                periodeList.clear();
                for (int i = 0; i < checkedskillsItems.length; i++) {
                    checkedskillsItems[i] = false;}

                mPeriodsItems.clear();

                textView.setText("no items selected !");
                textView.setTextColor(getResources().getColor(R.color.colorAccent));





            }
        });

        AlertDialog mDialog = mBuilder.create();
        mDialog.show();

    }

    /*----------------------------------------------------------------------------------------------------------------*/

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

    private void matchingJob()
    {
        MatchingJob m=new MatchingJob(ListIdsDomaine,ListIdsType,ListIdsRequirements,ListIdsSkills,ListIdsCities,ListIdsPeriods);
        m.matchingJob();
        MatchingJobsAndAverage=m.getMatchingJobsAndAverage();
        MatchingJobsIdsAndTitles=m.getMatchingJobsIdsAndTitles();
        Thread thread = new Thread() {
            @Override
            public void run() {

                if(MatchingJobsIdsAndTitles.size()<ListIdsDomaine.size())
                {
                    handler.post(this);

                }else
                {
                    Intent intent=new Intent(SharchForOffer.this,ListOfMatchingOffers.class);
                    intent.putExtra("MatchingJobsAndAverage",MatchingJobsAndAverage);
                    intent.putExtra("MatchingJobsIdsAndTitles",MatchingJobsIdsAndTitles);
                    startActivity(intent);
                }

            }
        };

        thread.start();



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

    private void setAdds() {
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdView mAdView = findViewById(R.id.adViewe);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

}
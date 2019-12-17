package com.example.intest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ApplayAnOffre extends AppCompatActivity {



    /**** Employer Detalils ****/


    TextView OffreTitle,OffreDescription,CompanyNameView,CityView,ReleaseDateView,StartDateView,PeriodeView,PaidOrNotView,CompanyDetails;
    ImageView logoCompanyView;



    /* offer id */
    String OffreId,userId,EmployerId,CompanyNameText;
     Uri uri;

    /* Databes Firebas */
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference offerRef,EmployerRef,appliedOffersRef;





    /* data base storage */
    private StorageReference mStorageRef;
    private Button uploadImag,applayButton;

    private int PICK_IMAGE_INTENT=2;
    private ProgressDialog progressDialog;
    public Map<String, String> map= new HashMap<>();
    public Map<String, String> Usermap= new HashMap<>();
    public Map<String, String> EmpMap= new HashMap<>();
    private SharedPreferences userinfo;
    String averageMatching;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applay_an_offre2);
       // setAdds();


         userinfo=getSharedPreferences("userinfos", MODE_PRIVATE);
         userId=userinfo.getString("id",null);
         Intent intent=getIntent();
         if(intent!=null)
         {
             OffreId=intent.getStringExtra("offerId");
             averageMatching=intent.getStringExtra("averageMatching");
         }
         instantiateviews();


        progressDialog =new ProgressDialog(this);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        getOffre(OffreId);


        uploadImag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent =new Intent(Intent.ACTION_PICK);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("application/pdf");
               // intent.setType("image/*");
                startActivityForResult(intent,PICK_IMAGE_INTENT);
            }
        });

        applayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadCv();
            }
        });


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            if (requestCode==PICK_IMAGE_INTENT && resultCode == RESULT_OK){
               uri=data.getData(); }



    }
    void uploadCv(){

        String reposetry = "offer_"+EmployerId;
        if(!(uri==null)) {

            if(accept()){
                    progressDialog.setMessage("uploading...");
                    progressDialog.show();
                    StorageReference filePath = mStorageRef.child(reposetry).child(userId);
                    filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();

                            /************ Start Dialoge ********** */


                            new AlertDialog.Builder(ApplayAnOffre.this)
                                    .setTitle("Post ")
                                    .setMessage("Your request is applied successfully !")
                                    .setCancelable(false)
                                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent =new Intent( ApplayAnOffre.this, DashbordStudent.class);
                                            startActivity(intent);
                                        }
                                    })

                                    // A null listener allows the button to dismiss the dialog and take no further action.
                                    //.setNegativeButton(android.R.string.no, null)
                                    .setIcon(R.drawable.chekicon)
                                    .show();
                        }
                    });
                /************ Store in firebase realtime storage ********** */

                appliedOffersRef=database.getReference("EmployersInbox").child(EmployerId).child(userId);
                appliedOffersRef.setValue(averageMatching);





            }else {
                Toast.makeText(ApplayAnOffre.this, "Only PDF format is accepted!",Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(ApplayAnOffre.this, "Please select a pdf !",Toast.LENGTH_LONG).show();
        }
    }
    public void getOffre(String offerId ) {
        offerRef = database.getReference().child("Offers").child(offerId) ;
        offerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    map.put(child.getKey(),child.getValue().toString());}

                String detail= map.get("Details");
                String id= map.get("Poster Id");
                String title= map.get("Title");
                String city=map.get("City");
                String startDate=map.get("StartDate");
                String releaseDate=map.get("DateRelease");
                String paidOrNot=map.get("Paid");
                String periode=map.get("Periode");
                CityView.setText(city);
                StartDateView.setText(startDate);
                ReleaseDateView.setText(releaseDate);
                if(paidOrNot.equals("yes"))
                PaidOrNotView.setText("payé");
                else
                    PaidOrNotView.setText("non payé");
                PeriodeView.setText(periode);
                CompanyNameText=map.get("Company_name");
                OffreTitle.setText(title);
                OffreDescription.setText(detail);
               // CompanyNameView.setText(CompanyNameText);
                EmployerId=id;
                getEmplyer(id);
                getInfosCompany(id);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void getInfosCompany(String id)
    {
        offerRef=database.getReference("Users").child(id);
        offerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    Usermap.put(child.getKey(),child.getValue().toString());}

                String CompanyName= Usermap.get("CompanyName");
                String CompanyDescription= Usermap.get("CompanyDescription");
                String CompanyLogo= Usermap.get("CompanyLogo");
                CompanyNameView.setText(CompanyName);
                CompanyDetails.setText(CompanyDescription);

                new DownloadImageTask((ImageView)logoCompanyView)
                        .execute(CompanyLogo);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getEmplyer(String employerId ) {

        EmployerRef = database.getReference().child("Users").child(employerId) ;

        EmployerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    EmpMap.put(child.getKey(),child.getValue().toString());}

                String firsName= EmpMap.get("FirstName");
                String lastName= EmpMap.get("LastName");
                String mail= EmpMap.get("Email");
                String urlPic= EmpMap.get("Picture");






            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


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
    public boolean accept() {
        String var=uri.toString();
            if (var.contains(".pdf")) {
                return true;
            }else {
                return false;}
    }

    public void instantiateviews()
    {

        CompanyNameView=findViewById(R.id.CompanyName);
        ReleaseDateView=findViewById(R.id.releaseDate);
        StartDateView=findViewById(R.id.startdate);
        PeriodeView=findViewById(R.id.periode);
        PaidOrNotView=findViewById(R.id.paidOrNot);
        CompanyDetails=findViewById(R.id.aboutCompany);
        CityView=findViewById(R.id.cityOffer);
        logoCompanyView=findViewById(R.id.Company_logo);
        /* offre details */

        OffreTitle =(TextView) findViewById(R.id.Emploer_OffreTittleId);
        OffreDescription =(TextView) findViewById(R.id.Employer_OfferBodyId);


        uploadImag =(Button) findViewById(R.id.UplodCv_BtnId);
        applayButton =(Button) findViewById(R.id.applay_Btn);

    }
    private void setAdds() {
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdView mAdView = findViewById(R.id.adViewi);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

}


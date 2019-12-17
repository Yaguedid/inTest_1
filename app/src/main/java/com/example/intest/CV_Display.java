package com.example.intest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.intest.download.DownloadTask;
import com.example.intest.function.SendSms;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;
import java.util.HashMap;

public class CV_Display extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference offerSettingsRef;
    private SharedPreferences userinfo;

    TextView FullNameView,BirthDayView,PhoneView,CityView,EmailView,LanguagesView,SkillsView,ProfileView,DiplomeView,
    DomaineView,OfferTypeView;
    ImageView imageUser;
    String candidateId,StudentOrEmployer,IdUser;
    HashMap<String,String> user=new HashMap<>();


    /* data base storage */
    private StorageReference mStorageRef;
    public  String CV_URL="";
    public  String offerId="Y18x8sASH_";
    public  String potulerId="PrCQ7QHDMl";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        getCvUrl(offerId,potulerId);

        userinfo=getSharedPreferences("userinfos", MODE_PRIVATE);
        StudentOrEmployer=userinfo.getString("StudentOrEmployer",null);
        IdUser=userinfo.getString("id",null);
        if(StudentOrEmployer.equals("Employer"))
        setContentView(R.layout.activity_cv__input);
        else
            setContentView(R.layout.activity_cv__input2);
        database = FirebaseDatabase.getInstance();
        Intent intent=getIntent();
      if(intent!=null)
        {
            candidateId=intent.getStringExtra("candidateId");
        }


        instantiateViews();
        getFromFireBase();


    }

    private void instantiateViews() {
        FullNameView=findViewById(R.id.fullNameUser);
        BirthDayView=findViewById(R.id.birthdayId);
        PhoneView=findViewById(R.id.phoneId);
        CityView=findViewById(R.id.cityId);
        EmailView=findViewById(R.id.emailId);
        LanguagesView=findViewById(R.id.languagesId);
        SkillsView=findViewById(R.id.skillsId);
       // ProfileView=findViewById(R.id.profileId);
       // DiplomeView=findViewById(R.id.dimplomeId);
        DomaineView=findViewById(R.id.genieId);
        OfferTypeView=findViewById(R.id.typeOffer);
        imageUser=findViewById(R.id.imageUser);

    }
    private void getFromFireBase()
    {
        offerSettingsRef=database.getReference("Users").child(candidateId);
        offerSettingsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    user.put(child.getKey(),child.getValue().toString());

                }
                setDataToViews();

            }
            @Override
            public void onCancelled(DatabaseError error) {}
        });

    }
    private void setDataToViews()
    {
        new CV_Display.DownloadImageTask((ImageView)imageUser)
                .execute(user.get("Picture"));
        FullNameView.setText(user.get("FirstName")+" "+user.get("LastName"));
        BirthDayView.setText(user.get("Date_Of_Birth"));
        PhoneView.setText(user.get("Phone"));
        CityView.setText(user.get("City"));
        EmailView.setText(user.get("Email"));
        EmailView.setMovementMethod(new ScrollingMovementMethod());
        LanguagesView.setText(user.get("Languages"));
        LanguagesView.setMovementMethod(new ScrollingMovementMethod());
        SkillsView.setText(user.get("Skills"));
        SkillsView.setMovementMethod(new ScrollingMovementMethod());
       // ProfileView.setText(user.get("Profil"));
        //ProfileView.setMovementMethod(new ScrollingMovementMethod());
        PhoneView.setMovementMethod(new ScrollingMovementMethod());
        //DiplomeView.setText(user.get("Diplomas"));
        //DomaineView.setText(user.get("Domaine"));
        OfferTypeView.setText(user.get("Type_Shearched_Offer"));





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

    public void yes(View view)
    {
        Intent intent=new Intent(CV_Display.this, SendSms.class);
        intent.putExtra("FullNameCandidate",user.get("FirstName")+" "+user.get("LastName"));
        intent.putExtra("OfferType",user.get("Type_Shearched_Offer"));
        intent.putExtra("PhoneNumber",user.get("Phone"));
        startActivity(intent);

    }
    public void no(View view)
    {

        offerSettingsRef=database.getReference("EmployersInbox").child(IdUser).child(candidateId);
        offerSettingsRef.removeValue();
        startActivity(new Intent(CV_Display.this,InboxForEmployers.class));


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if(StudentOrEmployer.equals("Employer"))
        inflater.inflate(R.menu.cv_menu_employer, menu);
        else
            inflater.inflate(R.menu.cv_menu_student, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(StudentOrEmployer.equals("Employer"))
        switch (item.getItemId()) {
            case R.id.download_cv_candidate:


                new DownloadTask(CV_Display.this,CV_URL,"redouan_eddafali");
                Toast.makeText(CV_Display.this, "download", Toast.LENGTH_SHORT).show();
                return true;


        }
        else
            switch (item.getItemId()) {
                case R.id.edit_cv:
                    Toast.makeText(CV_Display.this, "edit", Toast.LENGTH_SHORT).show();
                    return true;


            }
        return true;
    }



    public String getCvUrl(String offereID,String postulerID){


       StorageReference path=mStorageRef.child("offer_"+offereID).child(postulerID);
       path.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                CV_URL=uri.toString();
                Log.d("zbzbzb",CV_URL);


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

            }
        });

       return CV_URL;
    }
}

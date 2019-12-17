package com.example.intest;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;


public class tab_1 extends Fragment  implements TextWatcher {

    ImageView userImage;
    EditText userName;
    EditText userLastName;
    EditText userEmail;
    EditText userPhone;
    Spinner Daybirth;
    Spinner Monthbirth;
    Spinner Yearbirth;
    Spinner userCity;
    TextView nameFiled,lastnameFiled,mailFiled,phoneFiled,cityFiled,birthdayFiled;

    String name;
    String lastName;
    String email;
    String phone;
    String birthDate;
    String city="";

    Button chooseImage;
    private SharedPreferences userinfo;

    int PICK_IMAGE=3;
    Uri imgUri;

    String[] citysTable ;
    String[] daysTable ;
    String[] yearsTable ;
    String[] monthsTable ;


    Context context;

    public static tab_1  tab1_var;
    public static HashMap<String,String> VerfyInfo =new HashMap<>();
    private String EmailUser,FisrtnameUser,LastNameUser,IdUser,PictureUser,StudentOrEmployer;
    Boolean FILED_VERIFICATION=false;
    Boolean COLOR_VERIFICATION=false;


    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, final ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.pesonal_information, container, false);
        userinfo=TabsHolder.getInstance().getSharedPreferences("userinfos",TabsHolder.getInstance().MODE_PRIVATE);
        EmailUser=userinfo.getString("email",null);
        FisrtnameUser=userinfo.getString("firstname",null);
        LastNameUser=userinfo.getString("lastname",null);
        IdUser=userinfo.getString("id",null);
        PictureUser=userinfo.getString("picture",null);
        StudentOrEmployer=userinfo.getString("StudentOrEmployer",null);

        tab1_var=this;
        context=this.getActivity();

        userImage=(ImageView) root.findViewById(R.id.user_ImageId);
        userName=(EditText) root.findViewById(R.id.user_name);
        userLastName=(EditText) root.findViewById(R.id.user_lastName);
        userEmail=(EditText) root.findViewById(R.id.user_email);
        userPhone=(EditText) root.findViewById(R.id.user_phone);
        Daybirth=(Spinner) root.findViewById(R.id.day_birth_id);
        Monthbirth=(Spinner) root.findViewById(R.id.month_birth_id);
        Yearbirth=(Spinner) root.findViewById(R.id.year_birth_id);
        userCity=(Spinner) root.findViewById(R.id.user_city);

        nameFiled =(TextView) root.findViewById(R.id.naameFiled);
        lastnameFiled =(TextView) root.findViewById(R.id.lasnameFiled);
        mailFiled =(TextView) root.findViewById(R.id.emalFiled);
        phoneFiled =(TextView) root.findViewById(R.id.phoneFiled);
        cityFiled =(TextView) root.findViewById(R.id.cityFiled);
        birthdayFiled =(TextView) root.findViewById(R.id.birthdayFiled);

        citysTable =getResources().getStringArray(R.array.city);
        daysTable =getResources().getStringArray(R.array.day);
        yearsTable =getResources().getStringArray(R.array.years);
        monthsTable =getResources().getStringArray(R.array.months);


        ArrayAdapter arr_city = new ArrayAdapter(this.getActivity(),android.R.layout.simple_list_item_1,citysTable);
        arr_city.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userCity.setAdapter(arr_city);

        ArrayAdapter arr_day = new ArrayAdapter(this.getActivity(),android.R.layout.simple_list_item_1,daysTable);
        arr_day.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Daybirth.setAdapter(arr_day);

        ArrayAdapter arr_month = new ArrayAdapter(this.getActivity(),android.R.layout.simple_list_item_1,monthsTable);
        arr_month.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Monthbirth.setAdapter(arr_month);

        ArrayAdapter arr_year = new ArrayAdapter(this.getActivity(),android.R.layout.simple_list_item_1,yearsTable);
        arr_year.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Yearbirth.setAdapter(arr_year);

        initUserInfo();
        chooseImage =(Button)root.findViewById(R.id.chooseImg_id);



        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //get image from gallery
                /*Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);*/
            }
        });

        userName.addTextChangedListener(this);
        userLastName.addTextChangedListener(this);
        userPhone.addTextChangedListener(this);
        userEmail.addTextChangedListener(this);








        return root;
    }



    private void initUserInfo() {

        Picasso.with(getActivity()).load(PictureUser).into(userImage);
        userName.setText(FisrtnameUser);
        userLastName.setText(LastNameUser);
        userEmail.setText(EmailUser);

    }

        public  void chekInfo(){

            name =userName.getText().toString();
            lastName =userLastName.getText().toString();
            email =userEmail.getText().toString();
            phone =userPhone.getText().toString();

            city = userCity.getSelectedItem().toString();
            birthDate=Daybirth.getSelectedItem().toString()+"-"+ Monthbirth.getSelectedItem().toString()+"-"+Yearbirth.getSelectedItem().toString();

            if(!name.isEmpty())TabsHolder.getInstance().chekMap.put("peron_name","true");
            else TabsHolder.getInstance().chekMap.put("peron_name","false");
            if(!lastName.isEmpty())TabsHolder.getInstance().chekMap.put("Peron_lastname","true");
            else TabsHolder.getInstance().chekMap.put("Peron_lastname","false");
            if(!email.isEmpty())TabsHolder.getInstance().chekMap.put("Peron_email","true");
            else TabsHolder.getInstance().chekMap.put("Peron_email","false");
            if(!phone.isEmpty())TabsHolder.getInstance().chekMap.put("Peron_phone","true");
            else TabsHolder.getInstance().chekMap.put("Peron_phone","false");
            if(!birthDate.isEmpty())TabsHolder.getInstance().chekMap.put("Peron_birth_date","true");
            else TabsHolder.getInstance().chekMap.put("Peron_birth_date","false");
            if(!city.isEmpty())TabsHolder.getInstance().chekMap.put("Peron_city","true");
            else TabsHolder.getInstance().chekMap.put("Peron_city","false");


        }

        public static tab_1 getInstance(){
        return tab1_var;
        }


    @Override
    public void onResume() {
        super.onResume();
        if (FILED_VERIFICATION == true) {
            if (name.isEmpty())
                nameFiled.setTextColor(getResources().getColor(R.color.colorAccent));
            if (lastName.isEmpty())
                lastnameFiled.setTextColor(getResources().getColor(R.color.colorAccent));
            if (email.isEmpty())
                mailFiled.setTextColor(getResources().getColor(R.color.colorAccent));
            if (phone.isEmpty())
                phoneFiled.setTextColor(getResources().getColor(R.color.colorAccent));

            COLOR_VERIFICATION=true;
            FILED_VERIFICATION=false;

        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {


    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (COLOR_VERIFICATION == true) {
            if (name.isEmpty())
                nameFiled.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            if (lastName.isEmpty())
                lastnameFiled.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            if (email.isEmpty())
                mailFiled.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            if (phone.isEmpty())
                phoneFiled.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            COLOR_VERIFICATION=false;


        }
    }



    @Override
    public void afterTextChanged(Editable s) {



    }
}






/* -------------- ------------------------------- --------------------------- --------------------------- */


/* load image from phone

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==PICK_IMAGE && resultCode == RESULT_OK){

          if(data!=null){
            imgUri=data.getData();
            Picasso.with(getActivity()).load(imgUri).into(userImage);}
        }



    }*/



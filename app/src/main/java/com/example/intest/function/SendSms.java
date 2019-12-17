package com.example.intest.function;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.intest.InboxForEmployers;
import com.example.intest.R;

public class SendSms extends AppCompatActivity {
    final int SEND_SMS_REQUEST_CODE = 1;
    ProgressBar pb;
    String FullNameCandidate, OfferType, PhoneNumber, msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_sms);
        pb = findViewById(R.id.pb);
        Intent intent = getIntent();
        FullNameCandidate = intent.getStringExtra("FullNameCandidate");
        OfferType = intent.getStringExtra("OfferType");
        PhoneNumber = intent.getStringExtra("PhoneNumber");
        msg = "Bonjour Mr/Mme " + FullNameCandidate + "\n Vottre demande de " + OfferType + " a été prise .Veuillez nous visiter " +
                "lundi 10/12 pour négociation .\n Cordialement";
        if (checkPermission(Manifest.permission.SEND_SMS)) {
            Toast.makeText(SendSms.this, "Num = " + PhoneNumber, Toast.LENGTH_SHORT).show();
            Toast.makeText(SendSms.this, "Msg = " + msg, Toast.LENGTH_LONG).show();
            send();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, SEND_SMS_REQUEST_CODE);
        }

    }

    public void send() {
        if (checkPermission(Manifest.permission.SEND_SMS)) {

            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(PhoneNumber, null, msg, null, null);
            pb.setVisibility(View.INVISIBLE);
            startActivity(new Intent(SendSms.this, InboxForEmployers.class));
        }
    }

    public boolean checkPermission(String permission) {
        int check = ContextCompat.checkSelfPermission(this, permission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }



}
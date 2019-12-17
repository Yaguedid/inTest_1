package com.example.intest.navigation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.intest.R;

import org.w3c.dom.Text;

public class addText extends AppCompatActivity {

    private LinearLayout parentLinearLayout;
    int counter=0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_text);
        parentLinearLayout = (LinearLayout) findViewById(R.id.parent_linear_layout);

        Button btn =findViewById(R.id.getText);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text="";
                for(int i=0 ;i<=counter;i++){
                LinearLayout layout =(LinearLayout) parentLinearLayout.getChildAt(i);
                EditText editText = (EditText) layout.getChildAt(0);
               // Log.d("rooooooot",""+layout.getId());
                if(!editText.getText().toString().isEmpty()) text=text+ editText.getText().toString() +" + ";
            }
                Toast.makeText(addText.this, "text " + text, Toast.LENGTH_SHORT).show();

            }
        });

    }
    public void onAddField(View v) {

       if(counter <4){
        LayoutInflater inflater = (LayoutInflater) getSystemService (Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.filed_test, null);
        // Add the new row before the add field button.
        parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount() - 2);}
        counter++;
    }

    public void onDelete(View v) {
      if(counter>=1) {
          parentLinearLayout.removeView((View) v.getParent());
            counter--;}
    }
}

package com.example.intest;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;


public class tab_2 extends Fragment implements CompoundButton.OnCheckedChangeListener {


    CheckBox box1;
    CheckBox box2;
    CheckBox box3;
    CheckBox box4;
   public static int counter=0;

    String niveuEtude;

    Button select_geni;

    TextView itemsSelected;


    public List<Integer> mDomainItems = new ArrayList<>();
    public List<String> domainList=new ArrayList<>();;
    String[] domaineListItems;
    AlertDialog.Builder mBuilder ;


    Context context;
    public static tab_2  tab2_var;
    Boolean FILED_VERIFICATION=false;
    Boolean BOX_VERIFICATION=false;

    Boolean isCheckBoxCheked=false;
    Boolean isCheckInChekBoxesIsTrue=false;
    int numberOfCheckBoxesCheked=0;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, final ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.education, container, false);

        tab2_var=this;
        context=this.getActivity();
        mBuilder = new AlertDialog.Builder(this.getActivity());
        box1 =(CheckBox) root.findViewById(R.id.bacPlus_2);
        box2 =(CheckBox) root.findViewById(R.id.bacPlus_3);
        box3 =(CheckBox) root.findViewById(R.id.bacPlus_4);
        box4 =(CheckBox) root.findViewById(R.id.bacPlus_5);
        select_geni=(Button) root.findViewById(R.id.geni_id);
        itemsSelected=(TextView)root.findViewById(R.id.geni_selected_id);
        domaineListItems =getResources().getStringArray(R.array.domaine_item);



        select_geni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    showListItemes(domaineListItems,itemsSelected);



            }

        }
        );

        box1.setOnCheckedChangeListener(this);
        box2.setOnCheckedChangeListener(this);
        box3.setOnCheckedChangeListener(this);
        box4.setOnCheckedChangeListener(this);


        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        chekIfFiledEmpty();
        checkIfListEmpty(domainList,itemsSelected);
        if(FILED_VERIFICATION==true){
            select_geni.setBackgroundColor(getResources().getColor(R.color.Verifcation_Button));        }
    }

    private void showListItemes(final String[] itemsResTab, final TextView textView){

        final boolean[] checkedItems;

        checkedItems = new boolean[itemsResTab.length];

        domainList= new ArrayList<>();

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);

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
                        item = item + itemsResTab[mDomainItems.get(i)] + ",\n ";
                        domainList.add(itemsResTab[mDomainItems.get(i)]);
                        select_geni.setBackgroundColor(getResources().getColor(R.color.Verifcation_Button));


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

    private  void checkIfListEmpty(List<String> list, TextView textView){
        if(!list.isEmpty()){

            String item="";
            for(int i=0;i<list.size();i++){

                item=item+list.get(i) +",\n";
            }
            textView.setText(item);
            textView.setTextColor(getResources().getColor(R.color.colorPrimary));

        }

    }

    public void chekInfo(){
        numberOfCheckBoxesCheked=0;
        isCheckBoxCheked=false;
            if(box1.isChecked()){
                isCheckBoxCheked=true;
                numberOfCheckBoxesCheked++;
            }
            if(box2.isChecked()){
                isCheckBoxCheked=true;
                numberOfCheckBoxesCheked++;
            }
            if(box3.isChecked()){
                isCheckBoxCheked=true;
                numberOfCheckBoxesCheked++;
            }
            if(box4.isChecked()){
                isCheckBoxCheked=true;
                numberOfCheckBoxesCheked++;
            }

            if(isCheckBoxCheked && numberOfCheckBoxesCheked ==1) isCheckInChekBoxesIsTrue=true;
            else
                isCheckInChekBoxesIsTrue=false;
            if(isCheckInChekBoxesIsTrue){
                if(box1.isChecked())niveuEtude="BAC+2";
                if(box2.isChecked())niveuEtude="BAC+3";
                if(box3.isChecked())niveuEtude="BAC+4";
                if(box4.isChecked())niveuEtude="BAC+5";
                TabsHolder.getInstance().chekMap.put("education_diplome","true");

            }else {
                niveuEtude="";
                TabsHolder.getInstance().chekMap.put("education_diplome","false");
            }

            if(! domainList.isEmpty() )  TabsHolder.getInstance().chekMap.put("education_domaine","true");
            else  TabsHolder.getInstance().chekMap.put("education_domaine","false");

    }

    public static tab_2 getInstance(){
        return tab2_var;
    }

    private void chekIfFiledEmpty() {
        if(FILED_VERIFICATION==true) {

            itemsSelected.setText("no items selected !");
            itemsSelected.setTextColor(getResources().getColor(R.color.colorAccent));
            select_geni.setBackgroundColor(getResources().getColor(R.color.colorAccent));

            if (counter ==0 || counter >1)
            {box1.setTextColor(getResources().getColor(R.color.colorAccent));
              box2.setTextColor(getResources().getColor(R.color.colorAccent));
              box3.setTextColor(getResources().getColor(R.color.colorAccent));
              box4.setTextColor(getResources().getColor(R.color.colorAccent));}


            BOX_VERIFICATION=true;
            FILED_VERIFICATION=false;
        }else {
            select_geni.setBackgroundColor(getResources().getColor(R.color.Verifcation_Button));

        }


    }




    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


            box1.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            box2.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            box3.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            box4.setTextColor(getResources().getColor(R.color.colorPrimaryDark));


    }
}

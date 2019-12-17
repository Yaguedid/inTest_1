 package com.example.intest;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.intest.navigation.addText;

import java.util.ArrayList;
import java.util.List;

public class tab_3  extends Fragment {

    Button chooseSkills;
    Button chooseSoftSkills;

    TextView skillsSelected;
    TextView SoftSkillsSelectedLanguge;

    public List<Integer> mSkillsItems = new ArrayList<>();
    public List<String> skillsList =new ArrayList<>();
    String[] skillsListItems;
    AlertDialog.Builder skillBuilder ;


    public List<Integer> mSoftSkillsItems = new ArrayList<>();
    public List<String> SoftSkillsList=new ArrayList<>();
    String[] softSkillsListItems;
    AlertDialog.Builder SoftSkillsBuilder ;

    Context context;
    public static tab_3  tab3_var;

    Boolean FILED_VERIFICATION=false;

    public LinearLayout parentLinearLayout;
    public LinearLayout parentLinearLayoutExperience;


    int counter=0;
    int counter_experiences=0;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.skills, container, false);
        tab3_var=this;
        context=this.getActivity();
        skillBuilder = new AlertDialog.Builder(context);
        SoftSkillsBuilder = new AlertDialog.Builder(context);

        chooseSoftSkills=(Button)root.findViewById(R.id.langue_id);
        chooseSkills=(Button)root.findViewById(R.id.skiils_id);

        skillsSelected=(TextView) root.findViewById(R.id.skills_selected);
        SoftSkillsSelectedLanguge=(TextView) root.findViewById(R.id.langue_selected);

        skillsListItems =getResources().getStringArray(R.array.skills_item);
        softSkillsListItems =getResources().getStringArray(R.array.soft_skills);

        chekIfFiledEmpty();
        checkIfListEmpty(skillsList,skillsSelected);
        checkIfListEmpty(SoftSkillsList,SoftSkillsSelectedLanguge);

        chooseSkills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSkillsItemes(); }


        });

        chooseSoftSkills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { showLangugeItems(); }});



 /****************** ********************* Start get data langues & experience  **************************** ***************************/

        parentLinearLayout = (LinearLayout) root.findViewById(R.id.parent_linear_layout);
        parentLinearLayoutExperience = (LinearLayout) root.findViewById(R.id.parent_linear_layout_Experience);

        Button add_languge =root.findViewById(R.id.add_field_button);
        Button add_Experience =root.findViewById(R.id.add_Experience_button);
        Button delet_langue =root.findViewById(R.id.delete_firs_lang);
        Button delet_experience =root.findViewById(R.id.delete_Experience);

        add_languge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddField();
            }
        });
        add_Experience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddExperienceFiled();
            }
        });
        delet_langue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDelete(v);
            }
        });
        delet_experience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeleteExperienceFiled(v);
            }
        });

        Button btn =(Button) root.findViewById(R.id.getText);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLangueSelected();
            }
        });
        Button btn_exper =(Button) root.findViewById(R.id.getExperiences);
        btn_exper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getExperiencesSelected();
            }
        });

        /****************** ********************* End get data langues & experience  ************** *****************************************/
        return root;
    }


    @Override
    public void onResume() {
        super.onResume();


        chekIfFiledEmpty();
        checkIfListEmpty(skillsList,skillsSelected);
        checkIfListEmpty(SoftSkillsList,SoftSkillsSelectedLanguge);
    }

    private void showSkillsItemes(){
        final boolean[] checkedItems;

        checkedItems = new boolean[skillsListItems.length];

        skillsList= new ArrayList<>();



        skillBuilder.setTitle("Choisissez vos Compétances");

        skillBuilder.setMultiChoiceItems(skillsListItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {

                if(isChecked){
                    mSkillsItems.add(position);
                }else{
                    mSkillsItems.remove((Integer.valueOf(position)));
                }
            }
        });

        skillBuilder.setCancelable(false);

        skillBuilder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {

                String item = "";
                for (int i = 0; i < mSkillsItems.size(); i++) {
                    if(! skillsList.contains(skillsListItems[mSkillsItems.get(i)] )) {
                        item = item + skillsListItems[mSkillsItems.get(i)] + " + ";
                        skillsList.add(skillsListItems[mSkillsItems.get(i)]);

                    }
                }
                if(item.equals("")){
                    skillsSelected.setText("no items selected !");
                    skillsSelected.setTextColor(getResources().getColor(R.color.colorAccent));


                }else {
                    skillsSelected.setText(item);
                    skillsSelected.setTextColor(getResources().getColor(R.color.colorPrimary));
                    chooseSkills.setBackgroundColor(getResources().getColor(R.color.Verifcation_Button));
                }
            }
        });

        skillBuilder.setNegativeButton(R.string.dismiss_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        skillBuilder.setNeutralButton(R.string.clear_all_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                skillsList.clear();
                for (int i = 0; i < checkedItems.length; i++) {
                    checkedItems[i] = false;}

                mSkillsItems.clear();
                skillsSelected.setText("no items selected !");
                skillsSelected.setTextColor(getResources().getColor(R.color.colorAccent));


            }
        });

        AlertDialog mDialog = skillBuilder.create();
        mDialog.show();
    }

    public void showLangugeItems(){
        final boolean[] checkedLangItems;

        checkedLangItems = new boolean[softSkillsListItems.length];

        SoftSkillsList= new ArrayList<>();



        SoftSkillsBuilder.setTitle("Choisissez vos Soft-Skills");

        SoftSkillsBuilder.setMultiChoiceItems(softSkillsListItems, checkedLangItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {

                if(isChecked){
                    mSoftSkillsItems.add(position);
                }else{
                    mSoftSkillsItems.remove((Integer.valueOf(position)));
                }
            }
        });

        SoftSkillsBuilder.setCancelable(false);

        SoftSkillsBuilder.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {

                String item = "";
                for (int i = 0; i < mSoftSkillsItems.size(); i++) {
                    if(! SoftSkillsList.contains(softSkillsListItems[mSoftSkillsItems.get(i)] )) {
                        item = item + softSkillsListItems[mSoftSkillsItems.get(i)] + " + ";
                        SoftSkillsList.add(softSkillsListItems[mSoftSkillsItems.get(i)]);

                    }
                }
                if(item.equals("")){
                    SoftSkillsSelectedLanguge.setText("no items selected !");
                    SoftSkillsSelectedLanguge.setTextColor(getResources().getColor(R.color.colorAccent));
                }else {
                    SoftSkillsSelectedLanguge.setText(item);
                    SoftSkillsSelectedLanguge.setTextColor(getResources().getColor(R.color.colorPrimary));
                    chooseSoftSkills.setBackgroundColor(getResources().getColor(R.color.Verifcation_Button));}
            }
        });

        SoftSkillsBuilder.setNegativeButton(R.string.dismiss_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        SoftSkillsBuilder.setNeutralButton(R.string.clear_all_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                SoftSkillsList.clear();
                for (int i = 0; i < checkedLangItems.length; i++) {
                    checkedLangItems[i] = false;}

                mSoftSkillsItems.clear();
                SoftSkillsSelectedLanguge.setText("no items selected !");
                SoftSkillsSelectedLanguge.setTextColor(getResources().getColor(R.color.colorAccent));


            }
        });

        AlertDialog mDialog = SoftSkillsBuilder.create();
        mDialog.show();


    }



    private  void checkIfListEmpty(List<String> list, TextView textView){
        if(!list.isEmpty()){

            String item="";
            for(int i=0;i<list.size();i++){

                item=item+list.get(i) +" + ";
            }
            textView.setText(item);
            textView.setTextColor(getResources().getColor(R.color.colorPrimary));
        }}

    public void chekInfo(){

        if(! skillsList.isEmpty() )  TabsHolder.getInstance().chekMap.put("skills","true");
        else  TabsHolder.getInstance().chekMap.put("skills","false");

        if(! SoftSkillsList.isEmpty() )  TabsHolder.getInstance().chekMap.put("skills_langue","true");
        else TabsHolder.getInstance().chekMap.put("skills_langue","false");
    }

    public static tab_3 getInstance(){
        return tab3_var;
    }

    public void chekIfFiledEmpty() {
        if(FILED_VERIFICATION==true) {

            if (skillsList.isEmpty()) {
                skillsSelected.setText("no items selected !");
                skillsSelected.setTextColor(getResources().getColor(R.color.colorAccent));
                chooseSkills.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            }

            if (SoftSkillsList.isEmpty()) {
                SoftSkillsSelectedLanguge.setText("no items selected !");
                SoftSkillsSelectedLanguge.setTextColor(getResources().getColor(R.color.colorAccent));
                chooseSoftSkills.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            }

        }

    }











 /*********************************** Start Experinces and Langueges Handler  ***********************************/

  public void onAddField() {
        if(counter <4){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.field, null);
        parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount() - 1);
        TabsHolder.getInstance().parentLinearLayout=parentLinearLayout;
        // Add the new row before the add field but.addView(rowView, parentLinearLayout.getChildCount() - 1);
        counter++;}
    }
    public void onAddExperienceFiled() {
        if(counter_experiences<=4) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            final View rowView = inflater.inflate(R.layout.filed_exeriences, null);
            parentLinearLayoutExperience.addView(rowView, parentLinearLayoutExperience.getChildCount() - 1);
            TabsHolder.getInstance().parentLinearLayoutExperience = parentLinearLayoutExperience;
            counter_experiences++;
        }

    }
    public void onDelete(View v) {
       if(counter>=1){
           parentLinearLayout.removeView((View) v.getParent());
           counter--;
       }
    }
    public void onDeleteExperienceFiled(View v) {
        if(counter_experiences>=1){
            parentLinearLayoutExperience.removeView((View) v.getParent());
            counter_experiences--;
        }
    }


String langSelected="";
String niveuaLangselected="";


public  void getLangueSelected(){

       langSelected="";
       niveuaLangselected="";
    for(int i=0 ;i<=counter;i++) {
        CardView card = (CardView) parentLinearLayout.getChildAt(i);
        LinearLayout layout = (LinearLayout) card.getChildAt(0);
        Spinner us_lang = (Spinner) layout.getChildAt(0);
        Spinner us_niveau = (Spinner) layout.getChildAt(1);
        niveuaLangselected = niveuaLangselected + us_niveau.getSelectedItem().toString() + "\n";
        langSelected = langSelected + us_lang.getSelectedItem().toString() + "\n";
    }
    Toast.makeText(context, "langue " + langSelected, Toast.LENGTH_SHORT).show();
    Toast.makeText(context, "niveau " + niveuaLangselected, Toast.LENGTH_SHORT).show();
}



String Exper_title="";
String Exper_Company="";
String Exper_Start_Date="";
String Exper_End_Date="";

public void getExperiencesSelected(){

    Exper_title="";
    Exper_Company="";
    Exper_Start_Date="";
    Exper_End_Date="";
    for(int i=0 ;i<=counter_experiences;i++) {
        CardView card = (CardView) parentLinearLayoutExperience.getChildAt(i);
        LinearLayout layout = (LinearLayout) card.getChildAt(1);

        LinearLayout layout_title_and_company = (LinearLayout) layout.getChildAt(0);
        EditText title = (EditText) layout_title_and_company.getChildAt(0);
        EditText company = (EditText) layout_title_and_company.getChildAt(1);
        Exper_title = Exper_title + title.getText().toString() + "\n";
        Exper_Company = Exper_Company + company.getText().toString() + "\n";

        LinearLayout layout_Date = (LinearLayout) layout.getChildAt(1);
        EditText date_start = (EditText) layout_Date.getChildAt(0);
        EditText date_end = (EditText) layout_Date.getChildAt(1);
        Exper_Start_Date = Exper_Start_Date + date_start.getText().toString() + "\n";
        Exper_End_Date = Exper_End_Date + date_end.getText().toString() + "\n";
    }


    Log.d("title" ,  Exper_title);
    Log.d("company" ,  Exper_Company);
    Log.d("start" ,  Exper_Start_Date);
    Log.d("end" ,  Exper_End_Date);
}


/*********************************** End Experinces and Langueges Handler  ***********************************/

}

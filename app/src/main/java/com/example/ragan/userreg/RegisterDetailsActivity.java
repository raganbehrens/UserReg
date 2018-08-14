package com.example.ragan.userreg;

import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class RegisterDetailsActivity extends AppCompatActivity{

    private CheckBox chk_male;
    private CheckBox chk_female;
    private CheckBox chk_pref_male;
    private CheckBox chk_pref_female;

    private Spinner spn_religion;
    private Spinner spn_race;

    private TextView txt_reg_error;

    private EditText et_min_age;
    private EditText et_max_age;
    private EditText et_feet;
    private EditText et_inches;
    private EditText et_zipcode;

    private Button btn_complete;

    private String email;
    private String password;
    private String fName;
    private String lName;
    private String dob;
    private String gender;
    private String feet;
    private String inches;
    private String religion;
    private String race;
    private String min_age;
    private String max_age;
    private String zipcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_details);

        chk_male = findViewById(R.id.chk_male);
        chk_female = findViewById(R.id.chk_female);
        chk_pref_male = findViewById(R.id.chk_pref_male);
        chk_pref_female = findViewById(R.id.chk_pref_female);

        spn_religion = findViewById(R.id.spn_religion);
        spn_race = findViewById(R.id.spn_race);

        txt_reg_error = findViewById(R.id.txt_reg_error);

        et_min_age = findViewById(R.id.et_min_age);
        et_max_age = findViewById(R.id.et_max_age);
        et_feet = findViewById(R.id.et_feet);
        et_inches = findViewById(R.id.et_inches);
        et_zipcode = findViewById(R.id.et_zipcode);

        btn_complete = findViewById(R.id.btn_complete);

        Intent thisIntent = getIntent();
        email = thisIntent.getStringExtra("email");
        password = thisIntent.getStringExtra("password");
        fName = thisIntent.getStringExtra("fName");
        lName = thisIntent.getStringExtra("lName");
        dob = thisIntent.getStringExtra("dob");

        chk_female.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (chk_female.isChecked()) {
                    chk_male.setChecked(false);
                    gender = "Female";
                }
            }
        });

        chk_male.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (chk_male.isChecked()) {
                    chk_female.setChecked(false);
                    gender = "Male";
                }
            }
        });

        btn_complete.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                register();
            }


        });
    }

    public void register(){
        SharedPreferences sharedPref = getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();validateEntries();

        // If all values are valid, save info to shared preferences
        if(validateEntries()) {
            editor.putString(email, password);
            editor.putString(email+"fName", fName);
            editor.putString(email+"lName", lName);
            editor.putString(email+"dob", dob);
            editor.putString(email+"gender", gender);
            editor.putString(email+"feet", feet);
            editor.putString(email+"inches", inches);
            editor.putString(email+"religion", religion);
            editor.putString(email+"race", race);
            editor.putString(email+"zipcode", zipcode);
            if (chk_pref_male.isChecked()) editor.putBoolean(email+"prefMale", true);
            if (chk_pref_female.isChecked()) editor.putBoolean(email+"prefFemale", true);
            editor.putString(email+"minAge", min_age);
            editor.putString(email+"maxAge", max_age);
            editor.commit();

            Intent intent = new Intent(this, UserProfileActivity.class);
            intent.putExtra("email", email);
            startActivity(intent);
        }

    }

    public boolean validateEntries(){
        if (chk_male.isChecked()) gender = "Male";
        else if (chk_female.isChecked()) gender = "Female";
        else {
            showError("Please select your gender");
            return false;
        }

        if (!chk_pref_male.isChecked() && !chk_pref_female.isChecked()){
            showError("Please choose at least one preference");
            return false;
        }

        if (et_min_age.getText().toString().equals("") || et_max_age.getText().toString().equals(""))
        {
            showError("Please specify age preference");
            return false;
        }

        else {
            min_age = et_min_age.getText().toString();
            max_age = et_max_age.getText().toString();
        }

        if (et_feet.getText().toString().equals("") || et_inches.getText().toString().equals("")){
            showError("Please enter your height");
            return false;
        }

        else {
            feet = et_feet.getText().toString();
            inches = et_inches.getText().toString();
        }

        if (et_zipcode.getText().toString().length() < 5){
            showError("Please enter a valid zipcode");
            return false;
        }

        else {
            zipcode = et_zipcode.getText().toString();
        }

        if (spn_religion.getSelectedItemPosition() == 0) {
            spn_religion.setSelection(1);
            religion = spn_religion.getSelectedItem().toString();
        }

        if (spn_race.getSelectedItemPosition() == 0){
            spn_race.setSelection(1);
            race = spn_race.getSelectedItem().toString();
        }


        txt_reg_error.setText("Success");

        return true;
    }

    private void showError(String errorMessage){
        Toast.makeText(getApplicationContext(), errorMessage,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to quit registration?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        logout();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void logout(){
        Intent loginScreen = new Intent(this, MainActivity.class);
        loginScreen.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(loginScreen);
    }
}

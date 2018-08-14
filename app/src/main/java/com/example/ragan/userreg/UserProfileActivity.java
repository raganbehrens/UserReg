package com.example.ragan.userreg;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class UserProfileActivity extends AppCompatActivity {


    private static final int SELECT_PHOTO = 100;
    private ImageView chooseImage;

    private TextView mTxtName;
    private TextView mTxtGender;
    private TextView mTxtDob;
    private TextView mTxtReligion;
    private TextView mTxtRace;
    private TextView mTxtHeight;
    private TextView mTxtAgeRange;
    private TextView mZipcode;

    private CheckBox mChkMale;
    private CheckBox mChkFemale;


    //private TextView mTxtGender;
    //private TextView


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        chooseImage = findViewById(R.id.img_profile);
        Intent myIntent = getIntent();
        String accountEmail = myIntent.getStringExtra("email");
        mTxtName = (TextView) findViewById(R.id.txt_profile_name);
        mTxtGender = (TextView) findViewById(R.id.txt_profile_gender);
        mTxtDob = (TextView) findViewById(R.id.txt_profile_dob);
        mTxtReligion = findViewById(R.id.txt_profile_religion);
        mTxtRace = findViewById(R.id.txt_profile_race);
        mTxtHeight = findViewById(R.id.txt_profile_height);
        mTxtAgeRange = findViewById(R.id.txt_profile_age_range);
        mZipcode = findViewById(R.id.txt_profile_zip);

        mChkMale = findViewById(R.id.chk_profile_male);
        mChkFemale = findViewById(R.id.chk_profile_female);
        loadData(accountEmail);
    }

    private void loadData(String email){
        SharedPreferences sharedPref = getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String firstName = sharedPref.getString(email+"fName", "error");
        String lastName = sharedPref.getString(email+"lName", "error");
        String feet = sharedPref.getString(email+"feet", "error");
        String inches = sharedPref.getString(email+"inches", "error");
        String minAge = sharedPref.getString(email+"minAge", "error");
        String maxAge = sharedPref.getString(email+"maxAge", "error");
        String heightString = String.format(getString(R.string.height_string), feet, inches);
        String ageString = String.format(getString(R.string.age_string), minAge, maxAge);
        String zipcode = sharedPref.getString(email+"zipcode", "error");
        if (sharedPref.getBoolean(email+"prefMale", false)) mChkMale.setChecked(true);
        if (sharedPref.getBoolean(email+"prefFemale", false)) mChkFemale.setChecked(true);
        mTxtName.setText(firstName + " " + lastName);
        mTxtDob.setText(sharedPref.getString(email+ "dob", "error"));
        mTxtGender.setText(sharedPref.getString(email+"gender", "error"));
        mTxtReligion.setText(sharedPref.getString(email+"religion", "error"));
        mTxtRace.setText(sharedPref.getString(email+"race", "error"));
        mTxtHeight.setText(heightString);
        mTxtAgeRange.setText(ageString);
        mZipcode.setText(zipcode);

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to logout?")
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

    public void chooseImage(View v){
        openGallery();
    }

    private void openGallery(){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case SELECT_PHOTO:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = data.getData();
                    if(selectedImage !=null){
                        chooseImage.setImageURI(selectedImage);
                    }
                }
        }
    }
}

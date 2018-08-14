package com.example.ragan.userreg;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.TransitionManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

import java.util.regex.*;

public class MainActivity extends AppCompatActivity {

    // Buttons on view
    private Button btn_login;
    private Button btn_register;
    private Button btn_sign_in;
    private Button btn_sign_up;
    private Button btn_clear_prefs;

    // Textboxes on view
    private EditText et_email;
    private EditText et_password;
    private EditText et_confirm_password;
    private EditText et_first_name;
    private EditText et_last_name;
    private EditText et_dob;

    private boolean registering = false;
    private boolean validEmail = false;

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPref = getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        // Initialize textboxes
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        et_confirm_password = findViewById(R.id.et_confirm_password);
        et_first_name = findViewById(R.id.et_first_name);
        et_last_name = findViewById(R.id.et_last_name);
        et_dob = findViewById(R.id.et_dob);

        // Initialize buttons
        btn_sign_in = findViewById(R.id.btn_sign_in);
        btn_sign_up = findViewById(R.id.btn_sign_up);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_register = (Button) findViewById(R.id.btn_register);
        btn_clear_prefs = (Button) findViewById(R.id.button_clear_prefs);

        et_confirm_password.setVisibility(View.INVISIBLE);
        btn_sign_in.setTextColor(Color.CYAN);

        // Set listeners
        btn_login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                if (et_email.getText().toString().equals("")) showError("Please enter your email address");
                else login(et_email.getText().toString(), et_password.getText().toString());
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                register(et_email.getText().toString(), et_password.getText().toString());
            }
        });

        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (registering) {
                    registering = false;
                    btn_sign_in.setTextColor(Color.CYAN);
                    btn_sign_up.setTextColor(Color.GRAY);
                    hideKeyboard((EditText) getCurrentFocus());
                    startTransition();
                }
            }
        });

        btn_sign_up.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!registering){
                    registering = true;
                    btn_sign_up.setTextColor(Color.CYAN);
                    btn_sign_in.setTextColor(Color.GRAY);
                    startTransition();
                }
            }
        });

        btn_clear_prefs.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                SharedPreferences sharedPref = getSharedPreferences(
                        getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.clear();
                editor.commit();
            }
        });

        et_confirm_password.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                et_confirm_password.setTextColor(Color.WHITE);

            }
            public void afterTextChanged(Editable s) {

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        });

        et_email.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                et_email.setTextColor(Color.WHITE);
                Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(s.toString());
                if (!matcher.find()) {
                    //txt_error_text.setText("Please use a valid email address");
                    validEmail = false;
                }
                else {
                    //txt_error_text.setText("Valid email");
                    validEmail = true;
                }

            }
            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        });

        et_password.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                et_password.setTextColor(Color.WHITE);

            }
            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        });

        et_dob.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                setDate(v);
            }
        });

    }

    private void login(String email, String password){
        SharedPreferences sharedPref = getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        View focused = getCurrentFocus();
        hideKeyboard((EditText) focused);

        if (!sharedPref.contains(email)){
            et_email.setTextColor(Color.RED);
            showError("Account not recognized");

        }
        else if (sharedPref.contains(email) && sharedPref.getString(email, null).equals(password)){
            Intent intent = new Intent(this, UserProfileActivity.class);
            intent.putExtra("email", email);
            startActivity(intent);
        }
        else {
            et_password.setTextColor(Color.RED);
            showError("Invalid login");
        }
    }

    private void register(String email, String password){
        SharedPreferences sharedPref = getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        hideKeyboard((EditText) getCurrentFocus());
        if (!validEmail){

            et_email.setTextColor(Color.RED);
            showError("Invalid email address");
        }
        else if (sharedPref.contains(email)){
            et_email.setTextColor(Color.RED);
            showError("This account already exists");
        }
        else if (et_password.getText().toString().length() <=5){
            et_password.setTextColor(Color.RED);
            showError("Password must contain atleast 6 characters");
        }
        else if (!et_password.getText().toString().equals(et_confirm_password.getText().toString())) {
            et_confirm_password.setTextColor(Color.RED);
            showError("Passwords do not match");
        }
        else if (et_first_name.getText().toString().equals("")) {
            showError("Must specify first name");
        }
        else if (et_last_name.getText().toString().equals("")) {
            showError("Must specify last name");
        }
        else if (et_dob.getText().toString().equals("")) {
            showError("Must specify date of birth");
        }
        else {
            String fName = et_first_name.getText().toString();
            String lName = et_last_name.getText().toString();
            String dob = et_dob.getText().toString();

            Intent intent = new Intent(this, RegisterDetailsActivity.class);
            intent.putExtra("email", email);
            intent.putExtra("password", password);
            intent.putExtra("fName", fName);
            intent.putExtra("lName", lName);
            intent.putExtra("dob", dob);
            startActivity(intent);
            //AccountManager.getInstance();

            //AccountManager.registerAccount(this, email, password, fName, lName, dob);
        }
        /*
        if (!validEmail) txt_error_text.setText("Please enter a valid email address");
        //else if (sharedPref.contains(email)) txt_error_text.setText("An account with this email already exists");
        else {
            Intent intent = new Intent(this, RegisterActivity.class);
            intent.putExtra("email", email);
            intent.putExtra("password", password);
            startActivity(intent);

            txt_error_text.setText("Account Created");
            //editor.putString(email, password);
            //editor.commit();
            System.out.println("Committed");
        }*/
    }

    private void startTransition(){
        ConstraintLayout constraintLayout = (ConstraintLayout) findViewById(R.id.main);
        ConstraintSet constraintSet1 = new ConstraintSet();
        constraintSet1.clone(this, R.layout.activity_main);
        ConstraintSet constraintSet2 = new ConstraintSet();
        constraintSet2.clone(this, R.layout.activity_main_alt);
        if (registering) {
            TransitionManager.beginDelayedTransition(constraintLayout);
            constraintSet2.applyTo(constraintLayout);
        }
        else if (!registering){
            TransitionManager.beginDelayedTransition(constraintLayout);
            constraintSet1.applyTo(constraintLayout);
        }

    }

    private void showError(String errorMessage){
        Toast.makeText(getApplicationContext(), errorMessage,Toast.LENGTH_SHORT).show();
    }

    @SuppressWarnings("deprecation")
    private void setDate(View view) {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.et_dob = et_dob;
        newFragment.show(getSupportFragmentManager(), "datePicker");

    }

    protected void hideKeyboard(EditText input) {
        //input.setInputType(0);
        if (input != null) {
            input.clearFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
        }
    }
}

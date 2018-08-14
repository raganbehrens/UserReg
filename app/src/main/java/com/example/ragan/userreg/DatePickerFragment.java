package com.example.ragan.userreg;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    public EditText et_dob;


    public void init(EditText dobBox){

    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        DatePickerDialog dialogDatePicker = new DatePickerDialog(getActivity(),android.R.style.Theme_Holo_Dialog,this, year,month, day);
        //dialogDatePicker.getDatePicker().setSpinnersShown(true);
        return dialogDatePicker;
        //return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day)
    {
        et_dob.setText(month + "-" + day + "-" + year);
        // Do something with the date chosen by the user
    }
}
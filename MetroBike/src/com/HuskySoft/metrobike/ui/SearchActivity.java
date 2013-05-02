package com.HuskySoft.metrobike.ui;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TimePicker;

import com.HuskySoft.metrobike.R;

public class SearchActivity extends Activity {

	public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
		
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current time as the default values for the picker
			final Calendar calendar = Calendar.getInstance();
			int hour = calendar.get(Calendar.HOUR_OF_DAY);
			int minute = calendar.get(Calendar.MINUTE);
			
			// Create a new instance of TimePickerDialog and return it
			TimePickerDialog tpd = new TimePickerDialog(getActivity(), this, hour, minute, true);
			// Set the name of the dialog box
			tpd.setTitle(R.string.dialogbox_time);
			return tpd;
		}

		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			// Do something with the time chosen by the user
			EditText timeEditText = (EditText) getActivity().findViewById(R.id.editTextTime);
			
			// Formatting string be displayer
			String hourString = "";
			if (hourOfDay < 10) hourString += "0";
			hourString += hourOfDay;
			
			String minuteString = "";
			if (minute < 10) minuteString += "0";
			minuteString += minute;
			
			timeEditText.setText(hourString + ":" + minuteString);
		}
	}
	
	public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current date as the default date in the picker
			final Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);
			
			// Create a new instance of DatePickerDialog and return it
			DatePickerDialog dpd = new DatePickerDialog(getActivity(), this, year, month, day);
			// Set the name of the dialog box
			dpd.setTitle(R.string.dialogbox_date);
			return dpd;
		}

		public void onDateSet(DatePicker view, int year, int month, int day) {
			// Do something with the time chosen by the user
			EditText dateEditText = (EditText) getActivity().findViewById(R.id.editTextDate);
			
			// Formatting string be displayer
			String monthString = "";
			// system month starts from 0
			month++;
			if (month < 10) monthString += "0";
			monthString += month;
			
			String dayString = "";
			if (day < 10) dayString += "0";
			dayString += day;
			
			dateEditText.setText(monthString + "/" + dayString + "/" + year);
		}
	}
	
	private RadioButton departAtButton;
	private RadioButton leaveNowButton;
	private EditText dateEditText;
	private EditText timeEditText;
	private Button historyButton;
	private Button findButton;
	
	private final Calendar calendar = Calendar.getInstance();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		departAtButton = (RadioButton) findViewById(R.id.radioButtonDepartAt);
		leaveNowButton = (RadioButton) findViewById(R.id.radioButtonLeaveNow);
		dateEditText = (EditText) findViewById(R.id.editTextDate);
		timeEditText = (EditText) findViewById(R.id.editTextTime);
		historyButton = (Button) findViewById(R.id.buttonHistory);
		findButton = (Button) findViewById(R.id.buttonFind);
		
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		String hourString = "";
		if (hour < 10) hourString += "0";
		hourString += hour;
		
		int minute = calendar.get(Calendar.MINUTE);
		String minuteString = "";
		if (minute < 10) minuteString += "0";
		minuteString += minute;	
		
        int year = calendar.get(Calendar.YEAR);
        
        int month = calendar.get(Calendar.MONTH);
        month++;
		String monthString = "";
		if (month < 10) monthString += "0";
		monthString += month;	
		
        int day = calendar.get(Calendar.DAY_OF_MONTH);
		String dayString = "";
		if (day < 10) dayString += "0";
		dayString += day;	
		
		
		dateEditText.setText(monthString + "/" + dayString + "/" + year);
		timeEditText.setText(hourString + ":" + minuteString);
		
		leaveNowButton.setOnClickListener(new OnClickListener() {
		    public void onClick(View v) {
		    	dateEditText.setEnabled(false);
		    	timeEditText.setEnabled(false);
		    }
		});
		
		departAtButton.setOnClickListener(new OnClickListener() {
		    public void onClick(View v) {
		    	dateEditText.setEnabled(true);
		    	timeEditText.setEnabled(true);
		    	dateEditText.requestFocus();
		    }
		});

		dateEditText.setOnClickListener(new OnClickListener() {
		    public void onClick(View v) {
		    	showDatePickerDialog(v);
		    }
		});
		
		timeEditText.setOnClickListener(new OnClickListener() {
		    public void onClick(View v) {
		    	showTimePickerDialog(v);
		    }
		});
		
		findButton.setOnClickListener(new OnClickListener() {
		    public void onClick(View v) {
		    	// to be modified later, currently just directly call ResultsActivity
		    	Intent intent = new Intent(v.getContext(), ResultsActivity.class);
		    	startActivity(intent);
		    }
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_search, menu);
		return true;
	}
	
	public void showDatePickerDialog(View v) {
	    DialogFragment dpf = new DatePickerFragment();
	    dpf.show(getFragmentManager(), "datePicker");
	}
	
	public void showTimePickerDialog(View v) {
	    DialogFragment tpf = new TimePickerFragment();
	    tpf.show(getFragmentManager(), "timePicker");
	}
	
	
	
}

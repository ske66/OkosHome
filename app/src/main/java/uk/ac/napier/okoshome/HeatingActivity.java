package uk.ac.napier.okoshome;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class HeatingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heating);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        final Switch heatingOn = (Switch) findViewById(R.id.switch1);
        final Switch autoHeatingOn = (Switch) findViewById(R.id.switch2);
        final Switch timeModeOn = (Switch) findViewById(R.id.switch3);
        final Switch tempModeOn = (Switch) findViewById(R.id.switch4);

        final Button startTime = (Button) findViewById(R.id.btnStartTime);
        final Button endTime = (Button) findViewById(R.id.btnEndTime);
        final Button confirmTemp = (Button) findViewById(R.id.btnConfirmTemp);

        final EditText startTemp = (EditText) findViewById(R.id.txtStartTemp);
        final EditText endTemp = (EditText) findViewById(R.id.txtEndTemp);

        final CardView modeCard = (CardView) findViewById(R.id.Mode);
        final CardView timeCard = (CardView) findViewById(R.id.Times);
        final CardView tempCard = (CardView) findViewById(R.id.Temps);

        modeCard.setCardBackgroundColor(Color.rgb(240, 240, 240));
        timeCard.setCardBackgroundColor(Color.rgb(240, 240, 240));
        tempCard.setCardBackgroundColor(Color.rgb(240,240,240));


        startTime.setEnabled(false);
        endTime.setEnabled(false);
        startTemp.setEnabled(false);
        endTemp.setEnabled(false);
        timeModeOn.setEnabled(false);
        tempModeOn.setEnabled(false);
        confirmTemp.setEnabled(false);

        heatingOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (heatingOn.isChecked()) {
                    autoHeatingOn.setChecked(false);
                    timeModeOn.setChecked(false);
                    tempModeOn.setChecked(false);
                    timeModeOn.setEnabled(false);
                    tempModeOn.setEnabled(false);
                    startTime.setEnabled(false);
                    endTime.setEnabled(false);
                    startTemp.setEnabled(false);
                    endTemp.setEnabled(false);
                    confirmTemp.setEnabled(false);
                    timeCard.setCardBackgroundColor(Color.rgb(240,240,240));
                    modeCard.setCardBackgroundColor(Color.rgb(240, 240, 240));
                    tempCard.setCardBackgroundColor(Color.rgb(240,240,240));
                } else {
                    modeCard.setCardBackgroundColor(Color.rgb(240, 240, 240));
                    timeCard.setCardBackgroundColor(Color.rgb(240,240,240));
                    tempCard.setCardBackgroundColor(Color.rgb(240,240,240));
                    startTime.setEnabled(false);
                    endTime.setEnabled(false);
                    startTemp.setEnabled(false);
                    endTemp.setEnabled(false);
                }
            }
        });

        autoHeatingOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (autoHeatingOn.isChecked()) {
                    heatingOn.setChecked(false);
                    timeModeOn.setChecked(true);
                    timeModeOn.setEnabled(true);
                    tempModeOn.setEnabled(true);
                    startTime.setEnabled(true);
                    endTime.setEnabled(true);
                    startTemp.setEnabled(false);
                    endTemp.setEnabled(false);
                    confirmTemp.setEnabled(false);
                    modeCard.setCardBackgroundColor(Color.rgb(255, 255, 255));
                    timeCard.setCardBackgroundColor(Color.rgb(255, 255, 255));
                    tempCard.setCardBackgroundColor(Color.rgb(240,240,240));

                } else {
                    timeModeOn.setChecked(false);
                    tempModeOn.setChecked(false);
                    timeModeOn.setChecked(false);
                    timeModeOn.setEnabled(false);
                    tempModeOn.setEnabled(false);
                    startTime.setEnabled(false);
                    endTime.setEnabled(false);
                    startTemp.setEnabled(false);
                    endTemp.setEnabled(false);
                    confirmTemp.setEnabled(false);
                    modeCard.setCardBackgroundColor(Color.rgb(240, 240, 240));
                    timeCard.setCardBackgroundColor(Color.rgb(240, 240, 240));
                    tempCard.setCardBackgroundColor(Color.rgb(240,240,240));
                }
            }
        });

        timeModeOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (timeModeOn.isChecked()) {
                    tempModeOn.setChecked(false);
                    startTime.setEnabled(true);
                    endTime.setEnabled(true);
                    startTemp.setEnabled(false);
                    endTemp.setEnabled(false);
                    timeModeOn.setEnabled(true);
                    tempModeOn.setEnabled(true);
                    confirmTemp.setEnabled(false);
                    timeCard.setCardBackgroundColor(Color.rgb(255, 255, 255));
                    tempCard.setCardBackgroundColor(Color.rgb(240,240,240));
                } else {
                    tempModeOn.setChecked(true);
                    startTime.setEnabled(false);
                    endTime.setEnabled(false);
                    startTemp.setEnabled(true);
                    endTemp.setEnabled(true);
                    timeModeOn.setEnabled(true);
                    tempModeOn.setEnabled(true);
                    confirmTemp.setEnabled(true);
                    timeCard.setCardBackgroundColor(Color.rgb(240, 240, 240));
                    tempCard.setCardBackgroundColor(Color.rgb(255,255,255));
                }
            }
        });

        tempModeOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tempModeOn.isChecked())
                {
                    timeModeOn.setChecked(false);
                    startTime.setEnabled(false);
                    endTime.setEnabled(false);
                    startTemp.setEnabled(true);
                    endTemp.setEnabled(true);
                    confirmTemp.setEnabled(true);
                    timeCard.setCardBackgroundColor(Color.rgb(240,240,240));
                    tempCard.setCardBackgroundColor(Color.rgb(255,255,255));
                }
                else
                {
                    timeModeOn.setChecked(true);
                    startTime.setEnabled(true);
                    endTime.setEnabled(true);
                    startTemp.setEnabled(false);
                    endTemp.setEnabled(false);
                    confirmTemp.setEnabled(false);
                    timeCard.setCardBackgroundColor(Color.rgb(255,255,255));
                    tempCard.setCardBackgroundColor(Color.rgb(240,240,240));
                }

            }
        });


        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HeatingActivity.this);
                builder.setTitle("Time Picker");
                builder.setIcon(R.mipmap.ic_launcher);
                Calendar calendar = Calendar.getInstance();
                TimePickerDialog timePickerDialog = new TimePickerDialog(HeatingActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        String startTimeHour;
                        String startTimeMinute;
                        String timeOfDay;

                        if (hourOfDay < 10)
                        {
                            startTimeHour = "0" + Integer.toString(hourOfDay);
                            startTimeMinute = Integer.toString(minute);
                        }
                        else
                        {
                            startTimeHour = Integer.toString(hourOfDay);
                            startTimeMinute = Integer.toString(minute);
                        }

                        if (hourOfDay > 11)
                        {
                            timeOfDay = "PM";
                        }
                        else
                        {
                            timeOfDay = "AM";
                        }

                        String StartTime = startTimeHour + ":" + startTimeMinute + " " + timeOfDay;

                        Toast.makeText(HeatingActivity.this, StartTime, Toast.LENGTH_SHORT).show();

                        startTime.setText(StartTime);
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
                timePickerDialog.show();
            }
        });

        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HeatingActivity.this);
                builder.setTitle("Time Picker");
                builder.setIcon(R.mipmap.ic_launcher);
                Calendar calendar = Calendar.getInstance();
                TimePickerDialog timePickerDialog = new TimePickerDialog(HeatingActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        String endTimeHour;
                        String endTimeMinute;
                        String timeOfDay;

                        if (hourOfDay < 10)
                        {
                             endTimeHour = "0" + Integer.toString(hourOfDay);
                             endTimeMinute = Integer.toString(minute);
                        }
                        else
                        {
                            endTimeHour = Integer.toString(hourOfDay);
                            endTimeMinute = Integer.toString(minute);
                        }

                        if (hourOfDay > 11)
                        {
                            timeOfDay = "PM";
                        }
                        else
                        {
                            timeOfDay = "AM";
                        }

                        String EndTime = endTimeHour + ":" + endTimeMinute + " " + timeOfDay;

                        Toast.makeText(HeatingActivity.this,EndTime, Toast.LENGTH_SHORT).show();

                        endTime.setText(EndTime);
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
                timePickerDialog.show();
            }
        });

        confirmTemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String startTemperature =  startTemp.getText().toString();
                String endTemperature = endTemp.getText().toString();
            }
        });


    }
}

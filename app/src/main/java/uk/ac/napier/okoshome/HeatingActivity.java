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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class HeatingActivity extends AppCompatActivity {

    MqttAndroidClient mqttAndroidClient;

    final String serverUri = "tcp://94.174.54.253:1883";


    String clientId = "ExampleAndroidClient";
    final String publishTopic = "exampleAndroidPublishTopic";
    final String username = "mark";
    final String password = "sensing";


    SimpleDateFormat sdf = new SimpleDateFormat("HHmm");
    String currentTime = sdf.format(new Date());




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

        boolean Heating;
        boolean AutoHeating;
        boolean TimedHeating;
        boolean TempHeating;


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

                publishMessage("Heating: ON");

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
                    timeCard.setCardBackgroundColor(Color.rgb(240, 240, 240));
                    modeCard.setCardBackgroundColor(Color.rgb(240, 240, 240));
                    tempCard.setCardBackgroundColor(Color.rgb(240, 240, 240));

                } else {
                    publishMessage("Heating: OFF");
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

            //if the time chosen is == currentTime, then turn on heating

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

            //if the time chosen to finish is == currentTime then turn off heating

        });

        confirmTemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String startTemperature =  startTemp.getText().toString();
                String endTemperature = endTemp.getText().toString();
            }
        });



        clientId = clientId + System.currentTimeMillis();

        mqttAndroidClient = new MqttAndroidClient(getApplicationContext(), serverUri, clientId);
        mqttAndroidClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {

            }

            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                Log.w("Debug", new String(message.getPayload()));
                subscribeToTopic("livingroom/okoshome/#");

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });

        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setUserName(username);
        mqttConnectOptions.setPassword(password.toCharArray());
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setCleanSession(false);


        try {
            mqttAndroidClient.connect(mqttConnectOptions, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                    disconnectedBufferOptions.setBufferEnabled(true);
                    disconnectedBufferOptions.setBufferSize(100);
                    disconnectedBufferOptions.setPersistBuffer(false);
                    disconnectedBufferOptions.setDeleteOldestMessages(false);
                    mqttAndroidClient.setBufferOpts(disconnectedBufferOptions);
                    subscribeToTopic("livingroom/okoshome/#");

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                }
            });
        } catch (MqttException ex) {
            ex.printStackTrace();
        }


    }


    public void subscribeToTopic(String sTopic) {
        try {
            mqttAndroidClient.subscribe(sTopic, 1, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.w("debug", "subscriber");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.w("debug", "cannot subscribe");
                }
            });

        } catch (MqttException ex) {
            System.err.println("Exception whilst subscribing!");
            ex.printStackTrace();
        }
    }

    public void publishMessage(String a){

        try{
            MqttMessage message = new MqttMessage();
            message.setPayload(a.getBytes());
            mqttAndroidClient.publish(publishTopic, message);

        }catch (MqttException e) {
            System.err.println("Error Publish: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

package uk.ac.napier.okoshome;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TimePicker;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class LightingActivity extends AppCompatActivity {

    MqttAndroidClient mqttAndroidClient;

    final String serverUri = "tcp://94.174.54.253:1883";


    String clientId = "ExampleAndroidClient";
    final String username = "mark";
    final String password = "sensing";

    String startingTime = "12:00";
    String endingTime = "13:00";

    String startingTime2 = "12:00";
    String endingTime2 = "13:00";

    boolean timeMode = false;
    boolean timeMode2 = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lighting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final CardView modeCard = (CardView) findViewById(R.id.Mode);
        final CardView timeCard = (CardView) findViewById(R.id.Times);
        final CardView timeCard2 = (CardView) findViewById(R.id.Times2);

        final Switch lightsOn = (Switch) findViewById(R.id.switch1);
        final Switch autoLightsOn = (Switch) findViewById(R.id.switch2);
        final Switch timeModeOn = (Switch) findViewById(R.id.switch3);
        final Switch proxModeOn = (Switch) findViewById(R.id.switch4);

        final Switch lightsOn2 = (Switch) findViewById(R.id.switch5);
        final Switch autoLightsOn2 = (Switch) findViewById(R.id.switch6);

        final Button startTime = (Button) findViewById(R.id.btnStartTime);
        final Button endTime = (Button) findViewById(R.id.btnEndTime);

        final Button startTime2 = (Button) findViewById(R.id.btnStartTime2);
        final Button endTime2 = (Button) findViewById(R.id.btnEndTime2);

        modeCard.setCardBackgroundColor(Color.rgb(240, 240, 240));
        timeCard.setCardBackgroundColor(Color.rgb(240, 240, 240));
        timeCard2.setCardBackgroundColor(Color.rgb(240,240,240));

        timeModeOn.setEnabled(false);
        proxModeOn.setEnabled(false);
        startTime.setEnabled(false);
        endTime.setEnabled(false);

        startTime2.setEnabled(false);
        endTime2.setEnabled(false);


        lightsOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (lightsOn.isChecked())
                {
                    publishMessage("N", "hall/pir");
                    publishMessage("F", "proxMode");

                    timeMode = false;

                    startTime.setEnabled(false);
                    endTime.setEnabled(false);
                    autoLightsOn.setChecked(false);
                    timeModeOn.setEnabled(false);
                    timeModeOn.setChecked(false);
                    proxModeOn.setEnabled(false);
                    proxModeOn.setChecked(false);

                    timeCard.setCardBackgroundColor(Color.rgb(240, 240, 240));
                    modeCard.setCardBackgroundColor(Color.rgb(240, 240, 240));

                }
                else
                {
                    publishMessage("F", "hall/pir");
                    publishMessage("F", "proxMode");

                    startTime.setEnabled(false);
                    endTime.setEnabled(false);
                    timeModeOn.setEnabled(false);
                    timeModeOn.setChecked(false);
                    proxModeOn.setEnabled(false);
                    proxModeOn.setChecked(false);

                    timeCard.setCardBackgroundColor(Color.rgb(240, 240, 240));
                    modeCard.setCardBackgroundColor(Color.rgb(240, 240, 240));

                }
            }
        });

        autoLightsOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (autoLightsOn.isChecked())
                {
                    timeMode = true;

                    publishMessage("F", "hall/pir");
                    publishMessage("F", "proxMode");

                    startTime.setEnabled(true);
                    endTime.setEnabled(true);
                    lightsOn.setChecked(false);
                    timeModeOn.setEnabled(true);
                    timeModeOn.setChecked(true);
                    proxModeOn.setEnabled(true);
                    proxModeOn.setChecked(false);

                    timeCard.setCardBackgroundColor(Color.rgb(255, 255, 255));
                    modeCard.setCardBackgroundColor(Color.rgb(255, 255, 255));

                }
                else
                {
                    timeMode = false;

                    publishMessage("F", "hall/pir");
                    publishMessage("F", "proxMode");

                    startTime.setEnabled(false);
                    endTime.setEnabled(false);
                    timeModeOn.setEnabled(false);
                    proxModeOn.setEnabled(false);
                    timeModeOn.setChecked(false);
                    proxModeOn.setChecked(false);

                    timeCard.setCardBackgroundColor(Color.rgb(240, 240, 240));
                    modeCard.setCardBackgroundColor(Color.rgb(240, 240, 240));

                }
            }
        });


        timeModeOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (timeModeOn.isChecked())
                {
                    timeMode = true;

                    publishMessage("F", "proxMode");
                    startTime.setEnabled(true);
                    endTime.setEnabled(true);
                    proxModeOn.setChecked(false);

                    timeCard.setCardBackgroundColor(Color.rgb(255, 255, 255));

                }
                else
                {
                    timeMode = false;

                    publishMessage("F", "hall/pir");
                    publishMessage("N", "proxMode");
                    startTime.setEnabled(false);
                    endTime.setEnabled(false);
                    proxModeOn.setChecked(true);

                    timeCard.setCardBackgroundColor(Color.rgb(240, 240, 240));
                }
            }
        });


        proxModeOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (proxModeOn.isChecked())
                {
                    timeMode = false;

                    publishMessage("F", "hall/pir");
                    publishMessage("N", "proxMode");
                    startTime.setEnabled(false);
                    endTime.setEnabled(false);
                    timeModeOn.setChecked(false);
                    timeCard.setCardBackgroundColor(Color.rgb(240, 240, 240));

                }
                else
                {
                    timeMode = true;

                    publishMessage("F", "hall/pir");
                    publishMessage("F", "proxMode");

                    startTime.setEnabled(false);
                    endTime.setEnabled(false);
                    timeModeOn.setChecked(true);
                    timeCard.setCardBackgroundColor(Color.rgb(255, 255, 255));

                }
            }
        });



        //DO ONE FOR hall/pir, proxMode is only for hall/pir




        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LightingActivity.this);
                builder.setTitle("Time Picker");
                builder.setIcon(R.mipmap.ic_launcher);
                final Calendar calendar = Calendar.getInstance();
                TimePickerDialog timePickerDialog = new TimePickerDialog(LightingActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        String startTimeHour;
                        String startTimeMinute;

                        if (hourOfDay < 10) {
                            startTimeHour = "0" + Integer.toString(hourOfDay);
                            startTimeMinute = Integer.toString(minute);
                        } else {
                            startTimeHour = Integer.toString(hourOfDay);
                            startTimeMinute = Integer.toString(minute);
                        }

                        String StartTime = startTimeHour + ":" + startTimeMinute;

                        startTime.setText(StartTime);

                        startingTime = StartTime;

                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
                timePickerDialog.show();

            }

            //if the time chosen is == currentTime, then turn on heating

        });

        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(LightingActivity.this);
                builder.setTitle("Time Picker");
                builder.setIcon(R.mipmap.ic_launcher);
                Calendar calendar = Calendar.getInstance();
                TimePickerDialog timePickerDialog = new TimePickerDialog(LightingActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        String endTimeHour;
                        String endTimeMinute;
                        String timeOfDay;

                        if (hourOfDay < 10) {
                            endTimeHour = "0" + Integer.toString(hourOfDay);
                            endTimeMinute = Integer.toString(minute);
                        } else {
                            endTimeHour = Integer.toString(hourOfDay);
                            endTimeMinute = Integer.toString(minute);
                        }

                        String EndTime = endTimeHour + ":" + endTimeMinute;

                        endTime.setText(EndTime);

                        endingTime = EndTime;


                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
                timePickerDialog.show();
            }
        });






        startTime2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LightingActivity.this);
                builder.setTitle("Time Picker");
                builder.setIcon(R.mipmap.ic_launcher);
                final Calendar calendar = Calendar.getInstance();
                TimePickerDialog timePickerDialog = new TimePickerDialog(LightingActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        String startTimeHour;
                        String startTimeMinute;

                        if (hourOfDay < 10) {
                            startTimeHour = "0" + Integer.toString(hourOfDay);
                            startTimeMinute = Integer.toString(minute);
                        } else {
                            startTimeHour = Integer.toString(hourOfDay);
                            startTimeMinute = Integer.toString(minute);
                        }

                        String StartTime = startTimeHour + ":" + startTimeMinute;

                        startTime2.setText(StartTime);

                        startingTime2 = StartTime;

                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
                timePickerDialog.show();

            }

            //if the time chosen is == currentTime, then turn on heating

        });

        endTime2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(LightingActivity.this);
                builder.setTitle("Time Picker");
                builder.setIcon(R.mipmap.ic_launcher);
                Calendar calendar = Calendar.getInstance();
                TimePickerDialog timePickerDialog = new TimePickerDialog(LightingActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        String endTimeHour;
                        String endTimeMinute;
                        String timeOfDay;

                        if (hourOfDay < 10) {
                            endTimeHour = "0" + Integer.toString(hourOfDay);
                            endTimeMinute = Integer.toString(minute);
                        } else {
                            endTimeHour = Integer.toString(hourOfDay);
                            endTimeMinute = Integer.toString(minute);
                        }

                        String EndTime = endTimeHour + ":" + endTimeMinute;

                        endTime2.setText(EndTime);

                        endingTime2 = EndTime;


                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
                timePickerDialog.show();
            }
        });




        //get current time
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(20000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Calendar now = Calendar.getInstance();
                                SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                                String time = format.format(now.getTime());


                                if (timeMode == true)
                                {
                                    if (time.equals(startingTime))
                                    {
                                        publishMessage("N", "hall/pir");
                                    }
                                    else if (time.equals(endingTime))
                                    {
                                        publishMessage("F", "hall/pir");
                                    }

                                }


                                if (timeMode2 == true)
                                {
                                    if (time.equals(startingTime2))
                                    {
                                        publishMessage("N", "livingroom/lights");
                                    }
                                    else if (time.equals(endingTime2))
                                    {
                                        publishMessage("F", "livingroom/lights");
                                    }
                                }
                            }
                        });
                    }
                } catch (InterruptedException e) {

                }
            }
        };
        t.start();



        lightsOn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (lightsOn2.isChecked())
                {
                    publishMessage("N", "livingroom/lights");

                    timeMode2 = false;

                    startTime2.setEnabled(false);
                    endTime2.setEnabled(false);
                    autoLightsOn2.setChecked(false);

                    timeCard2.setCardBackgroundColor(Color.rgb(240, 240, 240));

                }
                else
                {
                    publishMessage("F", "livingroom/lights");

                    timeMode2 = false;

                    startTime2.setEnabled(false);
                    endTime2.setEnabled(false);
                    autoLightsOn2.setChecked(false);

                    timeCard2.setCardBackgroundColor(Color.rgb(240, 240, 240));

                }
            }
        });

        autoLightsOn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (autoLightsOn2.isChecked())
                {
                    publishMessage("F", "livingroom/lights");

                    timeMode2 = true;
                    startTime2.setEnabled(true);
                    endTime2.setEnabled(true);

                    timeCard2.setCardBackgroundColor(Color.rgb(255, 255, 255));

                }
                else
                {
                    publishMessage("F", "livingroom/lights");

                    timeMode2 = false;
                    startTime2.setEnabled(false);
                    endTime2.setEnabled(false);

                    timeCard2.setCardBackgroundColor(Color.rgb(240, 240, 240));

                }

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
                subscribeToTopic("okoshome");

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
                    subscribeToTopic("okoshome");

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

    public void publishMessage(String pMessage, String pTopic){

        try{
            MqttMessage message = new MqttMessage();
            message.setPayload(pMessage.getBytes());
            mqttAndroidClient.publish(pTopic, message);

        }catch (MqttException e) {
            System.err.println("Error Publish: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

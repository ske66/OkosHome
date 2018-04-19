package uk.ac.napier.okoshome;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

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


public class AirActivity extends AppCompatActivity {



    MqttAndroidClient mqttAndroidClient;

    final String serverUri = "tcp://94.174.54.253:1883";

    String clientId = "ExampleAndroidClient";
    final String username = "mark";
    final String password = "sensing";
    int Hum = 20;
    int Dust = 30;
    String startHumidity = "30";
    String endHumidity = "45";
    String startDusting = "30";
    String endDusting = "40";
    boolean humMode = false;
    boolean dustMode = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_air);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final TextView txtHumidity = (TextView) findViewById(R.id.txtHumidity);
        final TextView txtDust = (TextView) findViewById(R.id.txtDust);

        final Button confirmDust = (Button) findViewById(R.id.btnConfirmDust);
        final Button confirmHum = (Button) findViewById(R.id.btnConfirmHum);

        final Switch filteringOn = (Switch) findViewById(R.id.switch1);
        final Switch autoFilteringOn = (Switch) findViewById(R.id.switch2);
        final Switch dustModeOn = (Switch) findViewById(R.id.switch3);
        final Switch humModeOn = (Switch) findViewById(R.id.switch4);

        final EditText startHum = (EditText) findViewById(R.id.txtStartHum);
        final EditText endHum = (EditText) findViewById(R.id.txtEndHum);
        final EditText startDust = (EditText) findViewById(R.id.txtStartDust);
        final EditText endDust = (EditText) findViewById(R.id.txtEndDust);

        final CardView modeCard = (CardView) findViewById(R.id.Mode);
        final CardView dustCard = (CardView) findViewById(R.id.Dust);
        final CardView humCard = (CardView) findViewById(R.id.Hum);

        modeCard.setCardBackgroundColor(Color.rgb(240, 240, 240));
        dustCard.setCardBackgroundColor(Color.rgb(240, 240, 240));
        humCard.setCardBackgroundColor(Color.rgb(240, 240, 240));


        startHum.setEnabled(false);
        endHum.setEnabled(false);
        dustModeOn.setEnabled(false);
        humModeOn.setEnabled(false);
        confirmHum.setEnabled(false);


        filteringOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (filteringOn.isChecked())
                {
                    dustMode = false;
                    humMode = false;

                    publishMessage("N", "livingroom/filter");
                    publishMessage("ON", "cmnd/livingroom/dehumidifier/Power");

                    autoFilteringOn.setChecked(false);
                    dustModeOn.setChecked(false);
                    dustModeOn.setEnabled(false);
                    humModeOn.setEnabled(false);
                    humModeOn.setChecked(false);
                    startDust.setEnabled(false);
                    endDust.setEnabled(false);
                    confirmDust.setEnabled(false);
                    startHum.setEnabled(false);
                    endHum.setEnabled(false);
                    confirmHum.setEnabled(false);

                    dustCard.setCardBackgroundColor(Color.rgb(240, 240, 240));
                    modeCard.setCardBackgroundColor(Color.rgb(240, 240, 240));
                    humCard.setCardBackgroundColor(Color.rgb(240, 240, 240));

                }
                else
                {
                    publishMessage("F", "livingroom/filter");
                    publishMessage("OFF", "cmnd/livingroom/dehumidifier/Power");


                    autoFilteringOn.setChecked(false);
                    dustModeOn.setChecked(false);
                    humModeOn.setChecked(false);
                    startDust.setEnabled(false);
                    endDust.setEnabled(false);
                    startHum.setEnabled(false);
                    endHum.setEnabled(false);
                    confirmHum.setEnabled(false);

                    dustCard.setCardBackgroundColor(Color.rgb(240, 240, 240));
                    modeCard.setCardBackgroundColor(Color.rgb(240, 240, 240));
                    humCard.setCardBackgroundColor(Color.rgb(240, 240, 240));
                }
            }
        });

        autoFilteringOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (autoFilteringOn.isChecked())
                {
                    publishMessage("F", "livingroom/filter");
                    publishMessage("OFF", "cmnd/livingroom/dehumidifier/Power");

                    dustMode = true;

                    filteringOn.setChecked(false);
                    dustModeOn.setEnabled(true);
                    humModeOn.setEnabled(true);

                    dustCard.setCardBackgroundColor(Color.rgb(240, 240, 240));
                    humCard.setCardBackgroundColor(Color.rgb(240, 240, 240));
                    modeCard.setCardBackgroundColor(Color.rgb(255, 255, 255));

                }
                else
                {

                    publishMessage("F", "livingroom/filter");
                    publishMessage("OFF", "cmnd/livingroom/dehumidifier/Power");

                    dustMode = false;

                    dustModeOn.setChecked(false);
                    humModeOn.setChecked(false);
                    dustModeOn.setEnabled(false);
                    humModeOn.setEnabled(false);
                    startDust.setEnabled(false);
                    endDust.setEnabled(false);
                    startHum.setEnabled(false);

                    modeCard.setCardBackgroundColor(Color.rgb(240,240,240));
                    dustCard.setCardBackgroundColor(Color.rgb(240, 240, 240));
                    humCard.setCardBackgroundColor(Color.rgb(240,240,240));
                }
            }
        });


        dustModeOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (dustModeOn.isChecked())
                {
                    publishMessage("F", "livingroom/filter");

                    dustMode = true;

                    startDust.setEnabled(true);
                    endDust.setEnabled(true);
                    confirmDust.setEnabled(true);

                    dustCard.setCardBackgroundColor(Color.rgb(255, 255, 255));
                }
                else
                {
                    publishMessage("F", "livingroom/filter");

                    dustMode = false;

                    dustModeOn.setChecked(false);
                    startDust.setEnabled(false);
                    endDust.setEnabled(false);

                    dustCard.setCardBackgroundColor(Color.rgb(240, 240, 240));
                }

            }
        });

        humModeOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (humModeOn.isChecked())
                {
                    publishMessage("OFF", "cmnd/livingroom/dehumidifier/Power");

                    startHum.setEnabled(true);
                    endHum.setEnabled(true);
                    confirmHum.setEnabled(true);


                    humCard.setCardBackgroundColor(Color.rgb(255, 255, 255));
                }

                else
                {
                    humMode = false;
                    publishMessage("OFF", "cmnd/livingroom/dehumidifier/Power");


                    humModeOn.setChecked(false);
                    startHum.setEnabled(false);
                    endHum.setEnabled(false);
                    confirmHum.setEnabled(false);

                    humCard.setCardBackgroundColor(Color.rgb(240, 240, 240));
                }
            }
        });


        confirmHum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Integer.parseInt(startHum.getText().toString()) < Integer.parseInt(endHum.getText().toString()))
                {
                    Toast.makeText(AirActivity.this, "Starting Humidity cannot be lower than end humidity!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    startHumidity = startHum.getText().toString();
                    endHumidity = endHum.getText().toString();
                }
            }
        });

        confirmDust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Integer.parseInt(startDust.getText().toString()) < Integer.parseInt(endDust.getText().toString()))
                {
                    Toast.makeText(AirActivity.this, "Starting Dust amount cannot be lower than end Dust amount", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    startDusting = startDust.getText().toString();
                    endDusting = endDust.getText().toString();
                }
            }
        });


        clientId = clientId + System.currentTimeMillis();

        mqttAndroidClient = new MqttAndroidClient(getApplicationContext(), serverUri, clientId);
        mqttAndroidClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {

                subscribeToTopic("okoshome");

            }

            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                Log.w("Debug", new String(message.getPayload()));
                subscribeToTopic("okoshome");

                if (message.toString().contains("RH:")) {
                    String InstructionValue = message.toString().replace("RH:   ", "");
                    Integer.parseInt(InstructionValue);

                    Hum = Integer.parseInt(InstructionValue);

                    if (humMode == true) {
                        if (Hum <= Integer.parseInt(startHumidity) && Hum > Integer.parseInt(endHumidity)) {
                            publishMessage("ON", "cmnd/livingroom/dehumidifier/Power");
                        } else {
                            publishMessage("OFF", "cmnd/livingroom/dehumidifier/Power");
                        }
                    }

                }

                if (message.toString().contains("DUST:")) {
                    String InstructionValue = message.toString().replace("DUST:   ", "");
                    Integer.parseInt(InstructionValue);

                    Dust = Integer.parseInt(InstructionValue);

                    if (dustMode == true) {
                        if (Dust <= Integer.parseInt(startDusting) && Dust > Integer.parseInt(endDusting)) {
                            publishMessage("N", "livingroom/filter");
                        } else {
                            publishMessage("F", "livingroom/filter");
                        }
                    }
                }
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

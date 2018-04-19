package uk.ac.napier.okoshome;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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
import org.eclipse.paho.client.mqttv3.MqttTopic;

import java.io.UnsupportedEncodingException;

    public class MainActivity extends AppCompatActivity {

        MqttAndroidClient mqttAndroidClient;

        final String serverUri = "tcp://94.174.54.253:1883";

        String clientId = "ExampleAndroidClient";
        final String username = "mark";
        final String password = "sensing";


        int currentTemperature;
        int currentLights = 2;
        int humidificationLevel = 22;

        String currentLock = "Locked";

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            //TextViews
            final TextView txtTemperature = (TextView) findViewById(R.id.txtTemperature);
            final TextView txtLighting = (TextView) findViewById(R.id.txtLighting);
            final TextView txtLock = (TextView) findViewById(R.id.txtLock);
            final TextView txtAir = (TextView) findViewById(R.id.txtAir);

            //CardViews
          final  CardView heatingCard = (CardView) findViewById(R.id.HeatingCard);
            final CardView lightingCard = (CardView) findViewById(R.id.LightingCard);
           final CardView lockCard = (CardView) findViewById(R.id.LockCard);
            final CardView airCard = (CardView) findViewById(R.id.AirCard);


            txtTemperature.setText("Temp: " + currentTemperature + "c");
            txtLighting.setText("Lights on: " + currentLights);
            txtLock.setText(currentLock);
            txtAir.setText("Humidity: " + humidificationLevel + "%");

            heatingCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, HeatingActivity.class);
                    startActivity(intent);
                }
            });

            lightingCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, LightingActivity.class);
                    startActivity(intent);
                }
            });

            airCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, AirActivity.class);
                    startActivity(intent);
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


                    if (message.toString().contains("TEMP:"))
                    {
                        String InstructionValue = message.toString().replace("TEMP:   ", "");
                        txtTemperature.setText("Temp: " + InstructionValue + "c");

                        if (Integer.parseInt(InstructionValue) > 5 && Integer.parseInt(InstructionValue) <=30 ) {
                            heatingCard.setCardBackgroundColor(Color.parseColor("#ffffff"));
                        }
                        if (Integer.parseInt(InstructionValue) > 30 && Integer.parseInt(InstructionValue) < 45) {
                            heatingCard.setCardBackgroundColor(Color.parseColor("#ffd9b3"));
                        }
                        if (Integer.parseInt(InstructionValue) >= 45 && Integer.parseInt(InstructionValue) < 70) {
                            heatingCard.setCardBackgroundColor(Color.parseColor("#ffb3b3"));
                        }

                        if (Integer.parseInt(InstructionValue) > 70)
                        {
                            Toast.makeText(MainActivity.this, "This place is too toasty, Fire services have been notified", Toast.LENGTH_SHORT).show();
                        }

                    }
                  else if (message.toString().contains("LIGHT:"))
                    {
                        String InstructionValue = message.toString().replace("LIGHT:   ", "");
                        txtLighting.setText("Lights On: " + InstructionValue);

                        if (Integer.parseInt(InstructionValue) >= 0 && Integer.parseInt(InstructionValue) <= 6) {
                            lightingCard.setCardBackgroundColor(Color.parseColor("#ffffff"));
                        }
                        if (Integer.parseInt(InstructionValue) > 6 && Integer.parseInt(InstructionValue) < 10) {
                            lightingCard.setCardBackgroundColor(Color.parseColor("#ffd9b3"));
                        }
                        if (Integer.parseInt(InstructionValue) >= 10) {
                            lightingCard.setCardBackgroundColor(Color.parseColor("#ffb3b3"));
                        }
                    }


                   else if (message.toString().contains("RH:"))
                    {
                        String InstructionValue = message.toString().replace("RH:   ", "");
                        txtAir.setText("Humidity: " + InstructionValue  + "%");
                    }

                    else if (message.toString().contains("DOOR:"))
                    {
                        String InstructionValue = message.toString().replace("DOOR:   ", "");
                        txtLock.setText("Door: " + InstructionValue);

                        //Lock warning validation
                        if (InstructionValue.equals("Unlocked")) {
                            lockCard.setCardBackgroundColor(Color.parseColor("#ffb3b3"));
                        }
                        else if (InstructionValue.equals("Locked")){
                            lockCard.setCardBackgroundColor(Color.parseColor("#ffffff"));
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
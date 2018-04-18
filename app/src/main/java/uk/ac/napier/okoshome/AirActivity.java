package uk.ac.napier.okoshome;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;


public class AirActivity extends AppCompatActivity {



    MqttAndroidClient mqttAndroidClient;

    final String serverUri = "tcp://94.174.54.253:1883";

    String clientId = "ExampleAndroidClient";
    final String username = "mark";
    final String password = "sensing";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_air);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final TextView txtHumidity = (TextView) findViewById(R.id.txtHumidity);
        final TextView txtDust = (TextView) findViewById(R.id.txtDust);
        final TextView txtCO = (TextView) findViewById(R.id.txtCO);


        clientId = clientId + System.currentTimeMillis();

        mqttAndroidClient = new MqttAndroidClient(getApplicationContext(), serverUri, clientId);
        mqttAndroidClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {

                subscribeToTopic("livingroom/okoshome/#");

            }

            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                Log.w("Debug", new String(message.getPayload()));


                if (message.toString().contains("RH:")) {
                    String InstructionValue = message.toString().replace("RH:   ", "");
                    txtHumidity.setText("Humidity Level: " + InstructionValue + "%");

                } else if (message.toString().contains("DUST:")) {
                    String InstructionValue = message.toString().replace("DUST:   ", "");
                    txtDust.setText("Dust Level: " + InstructionValue + "%");

                } else if (message.toString().contains("CO:")) {
                    String InstructionValue = message.toString().replace("CO:   ", "");
                    txtCO.setText("CO Level: " + InstructionValue + "%");
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

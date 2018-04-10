package uk.ac.napier.okoshome;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;

import helpers.MQTTHelper;

public class MainActivity extends AppCompatActivity {

    int currentTemperature = 25;
    int currentLights = 2;
    int carbonMonoxideLevel = 5;
    int humidificationLevel = 22;
    int airPurificationLevel = 90;

    String currentLock = "locked";

    MQTTHelper mqttHelper;

    MqttAndroidClient mqttAndroidClient;

    TextView dataReceived;

    Button btnPublish;

    final String publishMessage = "Hello World!";
    final String publishTopic = "ExampleAndroidClient";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        dataReceived = (TextView) findViewById(R.id.dataReceived);
        btnPublish = (Button) findViewById(R.id.btnPublish);

        startMqtt();


        //TextViews
        TextView txtTemperature = (TextView) findViewById(R.id.txtTemperature);
        TextView txtLighting = (TextView) findViewById(R.id.txtLighting);
        TextView txtLock = (TextView) findViewById(R.id.txtLock);
        TextView txtAir = (TextView) findViewById(R.id.txtAir);

        //CardViews
        CardView heatingCard = (CardView) findViewById(R.id.HeatingCard);
        CardView lightingCard = (CardView) findViewById(R.id.LightingCard);
        CardView lockCard = (CardView) findViewById(R.id.LockCard);
        CardView airCard = (CardView) findViewById(R.id.AirCard);


        txtTemperature.setText("Temp: " + currentTemperature + "c");
        txtLighting.setText("Lights on: " + currentLights);
        txtLock.setText(currentLock);
        txtAir.setText("CO Level: " + carbonMonoxideLevel + "%");


        btnPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publishMessage();
            }
        });

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

        //Temperature warning validation
        if (currentTemperature > 30 && currentTemperature < 45) {
            heatingCard.setCardBackgroundColor(Color.parseColor("#ffd9b3"));
        } else if (currentTemperature >= 45) {
            heatingCard.setCardBackgroundColor(Color.parseColor("#ffb3b3"));
        }

        //Lighting warning validation
        if (currentLights > 6 && currentLights < 10) {
            lightingCard.setCardBackgroundColor(Color.parseColor("#ffd9b3"));
        } else if (currentLights >= 10) {
            lightingCard.setCardBackgroundColor(Color.parseColor("#ffb3b3"));
        }

        //Lock warning validation
        if (currentLock == "Unlocked") {
            lockCard.setCardBackgroundColor(Color.parseColor("#ffb3b3"));
        }


        //Carbon Monoxide Validation
        if (carbonMonoxideLevel > 10 && humidificationLevel < 70 && airPurificationLevel < 80) {
            airCard.setCardBackgroundColor(Color.parseColor("#ffb3b3"));
            txtAir.setText("CO Level: " + carbonMonoxideLevel + "%");

            //turn off heating

            //Alexa warn user that Humidity levels are high
        }

        if (carbonMonoxideLevel > 10 && humidificationLevel > 70 && airPurificationLevel < 80) {
            airCard.setCardBackgroundColor(Color.parseColor("#ffb3b3"));
            txtAir.setText("CO Level: " + carbonMonoxideLevel + "%");

            //turn off heating

            //Alexa warn user that Humidity levels are high
        }

        if (carbonMonoxideLevel > 10 && humidificationLevel > 70 && airPurificationLevel >= 80) {
            airCard.setCardBackgroundColor(Color.parseColor("#ffb3b3"));
            txtAir.setText("CO Level: " + carbonMonoxideLevel + "%");

            //turn off heating

            //Alexa warn user that Humidity levels are high
        }

        if (carbonMonoxideLevel > 10 && humidificationLevel < 70 && airPurificationLevel >= 80) {
            airCard.setCardBackgroundColor(Color.parseColor("#ffb3b3"));
            txtAir.setText("CO Level: " + carbonMonoxideLevel + "%");

            //turn off heating

            //Alexa warn user that Humidity levels are high
        }


        //Humidity Validation
        if (carbonMonoxideLevel <= 10 && humidificationLevel >= 70 && airPurificationLevel >= 80) {
            airCard.setCardBackgroundColor(Color.parseColor("#ffb3b3"));
            txtAir.setText("Humidity: " + humidificationLevel + "%");

            //turn off heating

            //Alexa warn user that Humidity levels are high
        }

        if (carbonMonoxideLevel <= 10 && humidificationLevel >= 70 && airPurificationLevel < 80) {
            airCard.setCardBackgroundColor(Color.parseColor("#ffb3b3"));
            txtAir.setText("Humidity: " + humidificationLevel + "%");

            //turn off heating
        }


        //Air Purification Validation
        if (carbonMonoxideLevel <= 10 && humidificationLevel < 70 && airPurificationLevel < 80) {
            airCard.setCardBackgroundColor(Color.parseColor("#ffb3b3"));
            txtAir.setText("Air Purity: " + airPurificationLevel + "%");

            //turn off heating
        }

    }

    private void startMqtt() {
        mqttHelper = new MQTTHelper(getApplicationContext());
        mqttHelper.mqttAndroidClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean b, String s) {
                Log.w("Debug", "Connected");
            }

            @Override
            public void connectionLost(Throwable throwable) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                Log.w("Debug",mqttMessage.toString());
                dataReceived.setText(mqttMessage.toString());
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });
    }

    public void publishMessage() {

        try {
            MqttMessage message = new MqttMessage();
            message.setPayload(publishMessage.getBytes());
           mqttAndroidClient.publish(publishTopic, message);
           if(!mqttAndroidClient.isConnected()){

           }
        }
        catch (MqttException e){
            System.err.println("Error Publishing: " + e.getMessage());
            e.printStackTrace();
        }


    }m

}


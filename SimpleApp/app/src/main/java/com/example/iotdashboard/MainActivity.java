package com.example.iotdashboard;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.angads25.toggle.interfaces.OnToggledListener;
import com.github.angads25.toggle.model.ToggleableView;
import com.github.angads25.toggle.widget.LabeledSwitch;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.nio.charset.Charset;

public class MainActivity extends AppCompatActivity {
    MQTTHelper mqttHelper;
    TextView txtTemp, txtHumid;
    LabeledSwitch button1, button2;
    SeekBar seekBar;
    TextView value;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        setContentView(R.layout.activity_main);

        txtTemp = findViewById(R.id.txtTemp);
        txtHumid = findViewById(R.id.txtHumid);

        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);

        seekBar = findViewById(R.id.seekBar);
        value = findViewById(R.id.seekBarProgress);

        button1.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(ToggleableView toggleableView, boolean isOn) {
                if(isOn == true){
                    sendDataMQTT("quang_cao2002/feeds/button1", "1");
                }else{
                    sendDataMQTT("quang_cao2002/feeds/button1", "0");
                }
            }
        });

        button2.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(ToggleableView toggleableView, boolean isOn) {
                if(isOn == true){
                    sendDataMQTT("quang_cao2002/feeds/button2", "1");
                }else{
                    sendDataMQTT("quang_cao2002/feeds/button2", "0");
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            int progressChangedValue = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = progress;
                value.setText("Changing operating cycle: " + progress + "sec");
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
//                Toast.makeText(MainActivity.this, "New operating cycle: " + progressChangedValue + "sec", Toast.LENGTH_SHORT).show();
                sendDataMQTT("quang_cao2002/feeds/sending-freq",Integer.toString(progressChangedValue));
//                Log.d("TEST",Integer.toString(progressChangedValue));
            }
        });
        seekBar.setMax(100);
        startMQTT();
    }

    public void sendDataMQTT(String topic, String value){
        MqttMessage msg = new MqttMessage();
        msg.setId(1234);
        msg.setQos(0);
        msg.setRetained(false);

        byte[] b = value.getBytes(Charset.forName("UTF-8"));
        msg.setPayload(b);

        try {
            mqttHelper.mqttAndroidClient.publish(topic, msg);
        }catch (MqttException e){

        }
    }
    public void startMQTT(){
        mqttHelper = new MQTTHelper(this);
        mqttHelper.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {

            }

            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                Log.d("TEST", topic + "***" + message.toString());
                if (topic.contains("sensor1")){
                    txtTemp.setText("Temp: " + message.toString() + "°C");
                }
                else if (topic.contains("sensor2")) {
                    txtHumid.setText("Humid: " + message.toString() + "%");
                }
                else if (topic.contains("button1")){
                    if (message.toString().equals("1")){
                        button1.setOn(true);
                    }
                    else{
                        button1.setOn(false);
                    }
                }
                else if (topic.contains("button2")){
                    if (message.toString().equals("1")){
                        button2.setOn(true);
                    }
                    else{
                        button2.setOn(false);
                    }
                }
                else if (topic.contains("sending-freq")){
                    seekBar.setProgress(Integer.parseInt(message.toString()));
//                    Toast.makeText(MainActivity.this, "New operating cycle: " + message.toString() + "sec", Toast.LENGTH_SHORT).show();
                }
                else if (topic.contains("error-detect")){
                    Toast.makeText(MainActivity.this, "Error:" + message.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }
}
package com.example.breakthecode;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.health.connect.datatypes.units.Power;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.BatteryManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {
    private Button main_BTN_click;
    private TextInputEditText main_TXT_password;
    private final int MAX_BRIGHTNESS = 255;
    private int currentBrightness;
    private AudioManager audio;
    private BatteryManager battery;
    private int batteryPercentage;
    private int currentVolume;
    private int maxVolume;
    private String enteredPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        findviews();
        main_BTN_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
               currentVolume= audio.getStreamVolume(AudioManager.STREAM_MUSIC);
               maxVolume = audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
               battery=  (BatteryManager) getSystemService(Context.BATTERY_SERVICE);
                batteryPercentage= battery.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
               enteredPassword = main_TXT_password.getText().toString();
                try {
                    currentBrightness = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
                } catch (Settings.SettingNotFoundException e) {
                    e.printStackTrace();
                }
                if ((currentBrightness == MAX_BRIGHTNESS) && (enteredPassword.equals(String.valueOf(batteryPercentage)) && (currentVolume == maxVolume) && isConnectedToWifi()))  {
                    Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                else{
                        Toast.makeText(MainActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                    }
                
            }
        });
    }

    private void findviews() {
        main_BTN_click=findViewById(R.id.main_BTN_click);
        main_TXT_password=findViewById(R.id.main_TXT_password);
    }

    private boolean isConnectedToWifi() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
            return capabilities != null && capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI);
        }
        return false;
    }
}
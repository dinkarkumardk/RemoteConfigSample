package com.dinkar.remoteconfigsample;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

public class MainActivity extends AppCompatActivity {
    FirebaseRemoteConfig mFirebaseRemoteConfig;
    public static final String TAG = MainActivity.class.getSimpleName();
    public static final String IMAGE_URL_KEY = "image_url";
    ImageView imageView;
    Button fetch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.image);
        fetch = findViewById(R.id.button);
        fetch.setOnClickListener(v -> fetchRemoteConfig());
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(0) // setting this to zero only for development mode so that I can check the changes immediately, default value is 12 hour
                .build();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
        mFirebaseRemoteConfig.setDefaultsAsync(R.xml.config_defaults);
        setImageView();
    }

    private void fetchRemoteConfig() {
        mFirebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        boolean updated = task.getResult();
                        Log.d(TAG, "Config params fetched: " + updated);
                        Toast.makeText(MainActivity.this, "Fetch succeeded",
                                Toast.LENGTH_SHORT).show();
                        setImageView();
                    } else {
                        Toast.makeText(MainActivity.this, "Fetch failed",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setImageView() {
        Glide.with(this).load(mFirebaseRemoteConfig.getString(IMAGE_URL_KEY)).into(imageView);
    }
}
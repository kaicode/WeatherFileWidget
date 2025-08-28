package io.kaicode.weatherwidget;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // For v1, simply redirect to the WeatherFile page
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://weatherfile.com/location?loc_id=GBR00002"));
        startActivity(intent);
        finish();
    }
}


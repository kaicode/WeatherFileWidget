package io.kaicode.weatherwidget.worker;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import io.kaicode.weatherwidget.widget.WindWidgetProvider;

public class WidgetUpdateService extends Service {
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Trigger widget update
        WindWidgetProvider.requestUpdate(this);
        stopSelf(startId);
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}


package io.kaicode.weatherwidget;

import android.app.Application;

import io.kaicode.weatherwidget.worker.WidgetScheduler;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        WidgetScheduler.schedule(this);
    }
}


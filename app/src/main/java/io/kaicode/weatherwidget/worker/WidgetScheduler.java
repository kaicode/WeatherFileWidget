package io.kaicode.weatherwidget.worker;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

public class WidgetScheduler {
    private static final String UNIQUE_WORK = "widget-update";

    public static void schedule(@NonNull Context context) {
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
        PeriodicWorkRequest request = new PeriodicWorkRequest.Builder(WidgetWorker.class, 30, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .build();
        WorkManager.getInstance(context)
                .enqueueUniquePeriodicWork(UNIQUE_WORK, ExistingPeriodicWorkPolicy.UPDATE, request);
    }
}


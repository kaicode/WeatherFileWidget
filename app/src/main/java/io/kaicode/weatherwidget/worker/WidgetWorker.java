package io.kaicode.weatherwidget.worker;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import io.kaicode.weatherwidget.R;
import io.kaicode.weatherwidget.net.WeatherFileClient;
import io.kaicode.weatherwidget.render.DialRenderer;
import io.kaicode.weatherwidget.widget.WindWidgetProvider;

public class WidgetWorker extends Worker {
    public WidgetWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        Context context = getApplicationContext();
        AppWidgetManager mgr = AppWidgetManager.getInstance(context);
        int[] ids = mgr.getAppWidgetIds(new ComponentName(context, WindWidgetProvider.class));
        if (ids == null || ids.length == 0) return Result.success();
        try {
            WeatherFileClient client = new WeatherFileClient();
            WeatherFileClient.Latest latest = client.fetchLatest();
            int size = 512;
            Bitmap dirBmp = DialRenderer.renderDirectionDial(size, latest.windDirectionDeg);
            Bitmap spdBmp = DialRenderer.renderSpeedDial(size, latest.windSpeedMs);
            for (int id : ids) {
                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_wind);
                views.setImageViewBitmap(R.id.image_direction, dirBmp);
                views.setImageViewBitmap(R.id.image_speed, spdBmp);
                views.setTextViewText(R.id.text_speed, String.format("%.1f m/s", latest.windSpeedMs));
                views.setTextViewText(R.id.text_direction, latest.windDirectionDeg + "\u00B0");
                views.setTextViewText(R.id.text_updated, latest.time);
                mgr.updateAppWidget(id, views);
            }
            return Result.success();
        } catch (Exception e) {
            return Result.retry();
        }
    }
}


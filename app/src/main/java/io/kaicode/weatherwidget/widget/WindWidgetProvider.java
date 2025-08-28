package io.kaicode.weatherwidget.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.RemoteViews;

import io.kaicode.weatherwidget.MainActivity;
import io.kaicode.weatherwidget.R;
import io.kaicode.weatherwidget.net.WeatherFileClient;
import io.kaicode.weatherwidget.render.DialRenderer;

public class WindWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateWidget(context, appWidgetManager, appWidgetId);
        }
    }

    public static void requestUpdate(Context context) {
        AppWidgetManager mgr = AppWidgetManager.getInstance(context);
        int[] ids = mgr.getAppWidgetIds(new ComponentName(context, WindWidgetProvider.class));
        if (ids != null && ids.length > 0) {
            for (int id : ids) {
                updateWidget(context, mgr, id);
            }
        }
    }

    private static void updateWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_wind);

        // Tap opens WeatherFile page
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://weatherfile.com/location?loc_id=GBR00002"));
        PendingIntent pi = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        views.setOnClickPendingIntent(R.id.image_speed, pi);
        views.setOnClickPendingIntent(R.id.image_direction, pi);

        // Network on background thread (simple thread for v1)
        new Thread(() -> {
            try {
                WeatherFileClient client = new WeatherFileClient();
                WeatherFileClient.Latest latest = client.fetchLatest();
                int size = 512; // generate crisp bitmaps
                Bitmap dirBmp = DialRenderer.renderDirectionDial(size, latest.windDirectionDeg);
                Bitmap spdBmp = DialRenderer.renderSpeedDial(size, latest.windSpeedMs);

                views.setImageViewBitmap(R.id.image_direction, dirBmp);
                views.setImageViewBitmap(R.id.image_speed, spdBmp);
                views.setTextViewText(R.id.text_speed, String.format("%.1f m/s", latest.windSpeedMs));
                views.setTextViewText(R.id.text_direction, latest.windDirectionDeg + "\u00B0");
                views.setTextViewText(R.id.text_updated, latest.time);
                appWidgetManager.updateAppWidget(appWidgetId, views);
            } catch (Exception e) {
                // Leave placeholders
                appWidgetManager.updateAppWidget(appWidgetId, views);
            }
        }).start();
    }
}


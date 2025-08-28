package io.kaicode.weatherwidget.render;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

public class DialRenderer {

    public static Bitmap renderDirectionDial(int sizePx, int degrees) {
        Bitmap bmp = Bitmap.createBitmap(sizePx, sizePx, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);

        Paint ring = new Paint(Paint.ANTI_ALIAS_FLAG);
        ring.setStyle(Paint.Style.STROKE);
        ring.setStrokeWidth(sizePx * 0.04f);
        ring.setColor(Color.WHITE);

        Paint fill = new Paint(Paint.ANTI_ALIAS_FLAG);
        fill.setStyle(Paint.Style.FILL);
        fill.setColor(Color.parseColor("#223344"));

        float cx = sizePx / 2f;
        float cy = sizePx / 2f;
        float r = sizePx * 0.45f;
        c.drawCircle(cx, cy, r, fill);
        c.drawCircle(cx, cy, r, ring);

        // ticks every 30 degrees
        Paint tick = new Paint(Paint.ANTI_ALIAS_FLAG);
        tick.setColor(Color.WHITE);
        tick.setStrokeWidth(sizePx * 0.01f);
        for (int d = 0; d < 360; d += 30) {
            double rad = Math.toRadians(d - 90);
            float x1 = cx + (float) Math.cos(rad) * (r * 0.8f);
            float y1 = cy + (float) Math.sin(rad) * (r * 0.8f);
            float x2 = cx + (float) Math.cos(rad) * (r * 0.95f);
            float y2 = cy + (float) Math.sin(rad) * (r * 0.95f);
            c.drawLine(x1, y1, x2, y2, tick);
        }

        // pointer arrow
        Paint arrow = new Paint(Paint.ANTI_ALIAS_FLAG);
        arrow.setStyle(Paint.Style.FILL);
        arrow.setColor(Color.parseColor("#00C8FF"));
        Path path = new Path();
        float arrowLen = r * 0.85f;
        float arrowWidth = r * 0.12f;
        double rad = Math.toRadians(degrees - 90);
        float tipX = cx + (float) Math.cos(rad) * arrowLen;
        float tipY = cy + (float) Math.sin(rad) * arrowLen;
        double leftRad = rad + Math.PI / 2;
        double rightRad = rad - Math.PI / 2;
        float baseX1 = cx + (float) Math.cos(leftRad) * arrowWidth;
        float baseY1 = cy + (float) Math.sin(leftRad) * arrowWidth;
        float baseX2 = cx + (float) Math.cos(rightRad) * arrowWidth;
        float baseY2 = cy + (float) Math.sin(rightRad) * arrowWidth;
        path.moveTo(tipX, tipY);
        path.lineTo(baseX1, baseY1);
        path.lineTo(baseX2, baseY2);
        path.close();
        c.drawPath(path, arrow);

        return bmp;
    }

    public static Bitmap renderSpeedDial(int sizePx, double speedMs) {
        Bitmap bmp = Bitmap.createBitmap(sizePx, sizePx, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);

        Paint ring = new Paint(Paint.ANTI_ALIAS_FLAG);
        ring.setStyle(Paint.Style.STROKE);
        ring.setStrokeWidth(sizePx * 0.04f);
        ring.setColor(Color.WHITE);

        Paint fill = new Paint(Paint.ANTI_ALIAS_FLAG);
        fill.setStyle(Paint.Style.FILL);
        fill.setColor(Color.parseColor("#223344"));

        float cx = sizePx / 2f;
        float cy = sizePx / 2f;
        float r = sizePx * 0.45f;
        c.drawCircle(cx, cy, r, fill);
        c.drawCircle(cx, cy, r, ring);

        // scale 0..30 m/s
        float max = 30f;
        float ratio = (float) Math.max(0, Math.min(max, speedMs)) / max;
        float sweep = 270f * ratio;

        Paint arcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        arcPaint.setStyle(Paint.Style.STROKE);
        arcPaint.setStrokeWidth(sizePx * 0.06f);
        arcPaint.setColor(Color.parseColor("#00E676"));
        RectF oval = new RectF(cx - r * 0.8f, cy - r * 0.8f, cx + r * 0.8f, cy + r * 0.8f);
        c.drawArc(oval, 135, sweep, false, arcPaint);

        return bmp;
    }
}


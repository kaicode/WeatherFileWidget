package io.kaicode.weatherwidget.net;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherFileClient {
    private static final String TAG = "WeatherFileClient";
    private static final String BASE = "https://weatherfile.com";
    private static final String VERSION = "V03";
    private static final String LOC_ID = "GBR00002"; // fixed for v1

    public static class Latest {
        public final double windSpeedMs; // wsc in m/s
        public final int windDirectionDeg; // wdc degrees
        public final String time; // HH:mm:ss
        public final String date; // yyyy-MM-dd
        public Latest(double windSpeedMs, int windDirectionDeg, String time, String date) {
            this.windSpeedMs = windSpeedMs;
            this.windDirectionDeg = windDirectionDeg;
            this.time = time;
            this.date = date;
        }
    }

    public Latest fetchLatest() throws Exception {
        String urlStr = BASE + "/" + VERSION + "/loc/" + LOC_ID + "/latest.json";
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(10000);
        conn.setRequestProperty("wf-tkn", "PUBLIC");
        int code = conn.getResponseCode();
        if (code != 200) throw new RuntimeException("HTTP " + code);
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) sb.append(line);
        br.close();
        String body = sb.toString();
        JSONObject root = new JSONObject(body);
        if (!"ok".equals(root.optString("status"))) {
            throw new RuntimeException("Status not ok");
        }
        JSONObject data = root.getJSONObject("data");
        double wsc = data.optDouble("wsc", Double.NaN);
        int wdc = data.optInt("wdc", 0);
        String time = data.optString("time", "");
        String date = data.optString("date", "");
        if (Double.isNaN(wsc)) throw new RuntimeException("Missing wsc");
        return new Latest(wsc, wdc, time, date);
    }
}


package com.panneau.home;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;

public class MainActivity extends Activity {

    private static final String PKG_HA = "io.homeassistant.companion.android";
    private static final String PKG_SPOTIFY = "com.spotify.music";

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER);
        root.setBackgroundColor(Color.parseColor("#0B0F14"));
        int pad = dp(24);
        root.setPadding(pad, pad, pad, pad);

        TextClock clock = new TextClock(this);
        clock.setFormat24Hour("HH:mm");
        clock.setFormat12Hour("HH:mm");
        clock.setTextSize(72);
        clock.setTextColor(Color.WHITE);
        clock.setGravity(Gravity.CENTER);
        root.addView(clock, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        TextClock date = new TextClock(this);
        date.setFormat24Hour("EEEE d MMMM");
        date.setFormat12Hour("EEEE d MMMM");
        date.setTextSize(20);
        date.setTextColor(Color.parseColor("#8B97A5"));
        date.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams dlp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dlp.bottomMargin = dp(32);
        root.addView(date, dlp);

        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams rlp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f);
        root.addView(row, rlp);

        row.addView(tile("\uD83C\uDFE0\nHome Assistant", "#18BCF2", PKG_HA));
        row.addView(tile("\uD83C\uDFB5\nSpotify", "#1DB954", PKG_SPOTIFY));

        TextView gear = new TextView(this);
        gear.setText("\u2699");
        gear.setTextSize(22);
        gear.setTextColor(Color.parseColor("#3A4452"));
        gear.setGravity(Gravity.END);
        gear.setOnLongClickListener(v -> {
            startActivity(new Intent(Settings.ACTION_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            return true;
        });
        root.addView(gear, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        setContentView(root);
    }

    private TextView tile(String text, String color, String pkg) {
        TextView t = new TextView(this);
        t.setText(text);
        t.setTextSize(26);
        t.setTextColor(Color.WHITE);
        t.setGravity(Gravity.CENTER);
        GradientDrawable g = new GradientDrawable();
        g.setColor(Color.parseColor(color));
        g.setCornerRadius(dp(28));
        t.setBackground(g);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.MATCH_PARENT, 1f);
        lp.setMargins(dp(10), dp(10), dp(10), dp(10));
        t.setLayoutParams(lp);
        t.setOnClickListener(v -> open(pkg));
        return t;
    }

    private void open(String pkg) {
        Intent i = getPackageManager().getLaunchIntentForPackage(pkg);
        if (i == null) {
            i = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + pkg));
        }
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try { startActivity(i); } catch (Exception ignored) { }
    }

    private int dp(int v) {
        return (int) (v * getResources().getDisplayMetrics().density);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    @Override
    public void onBackPressed() { }
}

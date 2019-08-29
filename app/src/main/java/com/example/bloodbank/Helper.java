package com.example.bloodbank;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Helper {

    public static String FEED = "feed";
    public static String USERS = "users";
    public static String BLOOD_BANK = "bloodBank";
    public static String BLOOD_GROUP = "bloodGroup";
    public static String UID = "uid";
    //    public static long THREE_MOUNTH=Long.parseLong(7889237999);
    public static long THREE_MOUNTH = Long.parseLong("7776000000");

    public static String Name = "name";


    public static void showToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();

    }

    public static void call(Context context, String num) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", num, null));
        context.startActivity(intent);
    }

    public static void message(Context context, String num) {
        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
        smsIntent.setType("vnd.android-dir/mms-sms");
        smsIntent.putExtra("address", num);
        context.startActivity(smsIntent);
    }

    public static void setNotificationTopic(String topicName, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("topic", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("topic", topicName);
        editor.apply();
    }

    public static void clearTopic(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("topic", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public static String getBloodGroup(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("topic", 0);
        return sharedPreferences.getString("topic", "none");
    }

    public static String getNotificationTopic(Context context) {
        String demoTopic = getBloodGroup(context);

        switch (demoTopic) {
            case "A+":
                return "aPlus";
            case "A-":
                return "aMinus";
            case "B-":
                return "bMinus";
            case "B+":
                return "bPlus";
            case "O+":
                return "oPlus";
            case "O-":
                return "oMinus";
            case "AB+":
                return "abPlus";
            case "AB-":
                return "abMinus";
            default:
                return "none";
        }
    }
}

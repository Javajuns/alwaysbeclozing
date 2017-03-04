package com.google.firebase.quickstart.fcm;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class ActionHelper {

    public static void startCustomActivity(String className, Bundle extras, Context context){
        Class cls = null;
        try {
            cls = Class.forName(className);
        }catch(ClassNotFoundException e){
            //means you made a wrong input in firebase console
        }
        Intent i = new Intent(context, cls);
        i.putExtras(extras);
        context.startActivity(i);
    }

    public static void startDialing(String phoneNumber, Context context, boolean createNewTask) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        if (createNewTask) intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.parse("tel:"+phoneNumber));
        context.startActivity(intent);
    }

    public static void startCalling(String phoneNumber, Context context) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.parse("tel:"+phoneNumber));
        context.startActivity(intent);
    }
}

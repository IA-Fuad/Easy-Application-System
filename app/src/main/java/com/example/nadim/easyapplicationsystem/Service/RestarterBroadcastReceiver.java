package com.example.nadim.easyapplicationsystem.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class RestarterBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("recieve", "ok");
        context.startService(new Intent(context, MyService.class));
    }
}

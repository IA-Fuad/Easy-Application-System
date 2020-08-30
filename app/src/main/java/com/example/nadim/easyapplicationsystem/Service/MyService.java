package com.example.nadim.easyapplicationsystem.Service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.nadim.easyapplicationsystem.Notification;
import com.example.nadim.easyapplicationsystem.R;
import com.example.nadim.easyapplicationsystem.Student_Home_Page;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MyService extends Service {

    public static MyService Single;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public static int cnt = 0;
    public static ArrayList<String> applicationID;
    private JSONObject msg;
    private Map<String, Boolean> mp;
    private NotificationManager notificationManager;
    private String name = "Application", CHANNEL_ID = "x";
    String description = "";
    private long start, end;
    private DatabaseReference databaseReference;

    private Socket mSocket;

    {
        try {
            mSocket = IO.socket("http://139.59.47.82:9000");
        } catch (URISyntaxException e) {
        }
    }


    //socket connection status
    public boolean socket_status = false;


    public MyService(){

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

       // mp = new HashMap<>();
       // FirebaseApp.initializeApp(this);
        sharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        editor = sharedPreferences.edit();

        applicationID = new ArrayList<>();
        Single = this;

        firebaseNotification();

       // runSocket();
        Log.d("StartService", "Started from Service");

        //restart Service when it killed
        return START_STICKY;
    }

    public void startTime(){
        start = System.currentTimeMillis();
    }


    private void firebaseNotification(){


        databaseReference.child(sharedPreferences.getString("id", null)).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Log.d("fdata", "added");
                String applicationId = dataSnapshot.getKey();
                String status = (String) dataSnapshot.getValue();

                notification(applicationId, status);
                applicationID.add(applicationId);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void runSocket() {

        if (!socket_status) {
            mSocket.connect();
            //mSocket.on("join", onNewMessage);
        }
        Log.d("StartService", "Started from Service");
        mSocket.emit("join", sharedPreferences.getString("id", ""));
        mSocket.on("real_response", onNewMessage);
    }

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            // callBroad(args.toString());

            applicationID = new ArrayList<>();
            msg = (JSONObject) args[0];
            sharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
            editor = sharedPreferences.edit();
            String id = "";

            try {
                id = msg.getString("appl_id");
                // applicationID.add(id);
                Log.d("notification", msg.getString("appl_id"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.d("aid", id);

            if(!sharedPreferences.contains(id)){

                editor.putString(id, "ok").apply();

                Gson gson = new Gson();
                String appid;

                if (sharedPreferences.contains("appl_id")){
                    appid = sharedPreferences.getString("appl_id", null);
                    Type type = new TypeToken<ArrayList<String>>() {}.getType();
                    Log.d("showapp",gson.fromJson(appid,type).toString());
                    applicationID = gson.fromJson(appid, type);
                }


                appid = gson.toJson(applicationID);
                editor.putString("appl_id",appid);
                editor.apply();
               // notification();
            }


            //   Toast.makeText(MyService.this, args[0].toString(), Toast.LENGTH_SHORT).show();

        }

    };

    private void notification(String appl_id, String status){


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
                //channel.setDescription(description);
                // Register the channel with the system; you can't change the importance
                // or other notification behaviors after this
                channel.enableLights(true);
                channel.setLightColor(Color.BLUE);
                channel.enableVibration(true);
                //AudioAttributes audioAttributes = new AudioAttributes();
                //channel.setSound( RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

                notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.createNotificationChannel(channel);
            }


            String msg;
            if (status == "1") msg = "Accepted";
            else msg = "Rejected";

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent i = new Intent(getApplicationContext(), Notification.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, i, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);

        builder.setSmallIcon(R.drawable.new_notification);
        builder.setContentTitle(getString(R.string.app_name));
        builder.setContentText(status);

        builder.setContentIntent(pendingIntent);
        builder.setSound(alarmSound);
        builder.setAutoCancel(true);
        if (notificationManager == null){
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        notificationManager.notify(new Random().nextInt(), builder.build());
       // editor.remove(id);
    }

/*    public void callBroad(String data) {
        //SocketApp playforever = SocketApp.getInstance();
        Intent intent = new Intent();
        intent.putExtra("message", data);
        intent.setAction("socketiodemo.NOTIFICATION");
      //  playforever.context.sendBroadcast(intent);
    }*/

    @Override
    public void onDestroy() {
        super.onDestroy();
       // stopSelf();
        Log.d("myservice", "onDestroy: ");
        sendBroadcast(new Intent(this, RestarterBroadcastReceiver.class));
        Single = null;
        socket_status = false;
        mp.clear();

    }

    @Override
    public void onCreate() {
        super.onCreate();
        databaseReference = FirebaseDatabase.getInstance().getReference();
    //    startForeground(1, new android.app.Notification());
        Single = this;
        mSocket.connect();
        sharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
    }



}

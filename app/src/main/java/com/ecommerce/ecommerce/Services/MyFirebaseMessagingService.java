package com.ecommerce.ecommerce.Services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.ecommerce.ecommerce.R;
import com.ecommerce.ecommerce.activity.ProductDetailActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String title = remoteMessage.getNotification().getTitle();
        String body = remoteMessage.getNotification().getBody();
        Map<String,String> extraData = remoteMessage.getData();
        String category = extraData.get("category");
        Log.d("Notifyyy1","Success");
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,"TAC")
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.ic_launcher_background);
        Intent intent;
        if(category.equals("Chat"))
        {
//            startActivity(intent);
            //        Toast.makeText(this,"NNNNN",Toast.LENGTH_SHORT).show();
            intent = new Intent(getApplicationContext(), ProductDetailActivity.class);
            intent.putExtra("routineId",extraData.get("routineId"));
            intent.putExtra("category","Instructor");
            PendingIntent pendingIntent = PendingIntent.getActivity(this,10,intent,PendingIntent.FLAG_ONE_SHOT);
            notificationBuilder.setContentIntent(pendingIntent);
        }
        else{
            Log.d("Notifyyy5","Success");
//            Toast.makeText(this,"NNNNN",Toast.LENGTH_SHORT).show();
        }


        NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        int id = (int)System.currentTimeMillis();

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            NotificationChannel channel = new NotificationChannel("TAC","demo",NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(id,notificationBuilder.build());
    }



}

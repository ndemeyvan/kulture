package cm.studio.devbee.communitymarket;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import com.google.rpc.BadRequestOrBuilder;

public class notification_receiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager =(NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        Intent repeating_intent = new Intent( context , Accueil.class);
        repeating_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,100,repeating_intent,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher_logo_two)
                .setContentIntent(pendingIntent)
                .setContentTitle("Open Market")
                //.setContentText(" De bonnes deal t'attendent sur Open Market , fais y un tour .")
                .setAutoCancel(true)
                .setPriority(2)
                .setWhen(System.currentTimeMillis())
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(" De bonnes deal t'attendent sur Open Market , fais y un tour ."));
        notificationManager.notify(100, notificationBuilder.build());
    }
}

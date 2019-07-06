package cm.studio.devbee.communitymarket;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.Random;

import cm.studio.devbee.communitymarket.messagerie.MessageActivity;
import io.grpc.internal.SharedResourceHolder;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private final String ADMIN_CHANNEL_ID ="admin_channel";
    private String title;
    private String message;
    private String id;
    private String viens_de_detail;
    private String id_recepteur;
    private String image_en_vente;
    private String id_expediteur;
    private String image_user;
    private FirebaseAuth firebaseAuth;

    private static FirebaseFirestore firebaseFirestore;
    private String current_id;

    /*remoteMessage.getData()!=null && remoteMessage.getData().size() > 0*/
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        firebaseAuth=FirebaseAuth.getInstance ();
        current_id=firebaseAuth.getCurrentUser ().getUid ();

        if (remoteMessage.getData()!=null && remoteMessage.getData().size() > 0) {
            final Intent intent = new Intent(this, MessageActivity.class);
            NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            int notificationID = new Random().nextInt(9000);
            title = remoteMessage.getData().get("title");
            message = remoteMessage.getData().get("message");
            id = remoteMessage.getData().get("id");
            id_recepteur = remoteMessage.getData().get("id_recepteur");
            viens_de_detail = remoteMessage.getData().get("viens_de_detail");
            image_en_vente = remoteMessage.getData().get("image_en_vente");
            id_expediteur=remoteMessage.getData ().get ( "id_expediteur" );
            intent.putExtra ("id_recepteur",id_recepteur  );
            intent.putExtra ("image_en_vente",image_en_vente  );
            intent.putExtra ("viens_de_detail","faux"  );
            intent.putExtra ( "id de l'utilisateur",id );
      /*
        Apps targeting SDK 26 or above (Android O) must implement notification channels and add its notifications
        to at least one of them. Therefore, confirm if version is Oreo or higher, then setup notification channel
      */
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                setupChannels(notificationManager);
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this , 0, intent,
                    PendingIntent.FLAG_ONE_SHOT);
            Bitmap largeIcon = BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher_logo_two);
            Uri notificationSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, ADMIN_CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_launcher_logo_two)
                    .setLargeIcon(largeIcon)
                    .setContentTitle(title)
                    .setContentText(message)
                    .addAction(R.drawable.ic_message_flooat_icon, "repondre", pendingIntent)
                    .setAutoCancel(true)
                    .setSound(notificationSoundUri)
                    .setContentIntent(pendingIntent);
            //Set notification color to match your app color template
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                notificationBuilder.setColor(getResources().getColor(R.color.colorPrimaryDark));
            }
            notificationManager.notify(notificationID, notificationBuilder.build());
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupChannels(NotificationManager notificationManager){
        CharSequence adminChannelName = "New notification";
        String adminChannelDescription = "Device to devie notification";
        NotificationChannel adminChannel;
        adminChannel = new NotificationChannel(ADMIN_CHANNEL_ID, adminChannelName, NotificationManager.IMPORTANCE_HIGH);
        adminChannel.setDescription(adminChannelDescription);
        adminChannel.enableLights(true);
        adminChannel.setLightColor(Color.RED);
        adminChannel.enableVibration(true);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(adminChannel);
        }
    }
}

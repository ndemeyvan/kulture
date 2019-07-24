package cm.studio.devbee.communitymarket;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class notification_receiver extends BroadcastReceiver {
    private FirebaseAuth firebaseAuth;
    private String current_id;
    private FirebaseFirestore firebaseFirestore;


    @Override
    public void onReceive(final Context context, Intent intent) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            firebaseAuth=FirebaseAuth.getInstance ();
            current_id=firebaseAuth.getCurrentUser ().getUid ();
            firebaseFirestore=FirebaseFirestore.getInstance();
            firebaseFirestore.collection ( "mes donnees utilisateur" ).document (current_id).get ().addOnCompleteListener ( new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful ()){
                        if (task.getResult ().exists ()){
                            String nom_user = task.getResult ().getString ("user_name");
                            String prenomuser =task.getResult ().getString ("user_prenom");

                            //////////////////////////
                            Intent repeating_intent = new Intent( context , Accueil.class);
                            NotificationManager notificationManager =(NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
                            repeating_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,100,repeating_intent,PendingIntent.FLAG_UPDATE_CURRENT);
                            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                                    .setSmallIcon(android.R.color.transparent)
                                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                                            R.drawable.shoes))
                                    .setContentTitle(" hello " + nom_user + " " + prenomuser)
                                    //.setContentText(" De bonnes deal t'attendent sur Open Market , fais y un tour .")
                                    .setAutoCancel(true)
                                    .setPriority(2)
                                    .setWhen(System.currentTimeMillis())
                                    .setStyle(new NotificationCompat.BigTextStyle()
                                            .bigText(" De bons deal t'attendent sur Open Market , fais y un tour ."))
                                    .setContentIntent(pendingIntent);
                            notificationManager.notify(100, notificationBuilder.build());
                            //////////////////

                        }
                    }else{

                    }
                }
            } );

        } else {
            // No user is signed in
        }

    }
}

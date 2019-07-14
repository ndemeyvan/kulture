package cm.studio.devbee.communitymarket;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import androidx.annotation.Nullable;
import cm.studio.devbee.communitymarket.messagerie.ChatMessageActivity;
import cm.studio.devbee.communitymarket.profile.ParametrePorfilActivity;

import static cm.studio.devbee.communitymarket.App.CHANNEL_ID;


public class notification_service extends Service {
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private String current_user_id;
    private Intent intent;
    private String contenu;
    private String nom_du_notifieur;
    private String lien_de_limag_du_notifieur;
    private String lien_du_produit;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate ();

        Toast.makeText ( this,"service lance",Toast.LENGTH_LONG ).show ();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final DocumentReference docRef = firebaseFirestore.collection ( "mes donnees utilisateur" ).document (current_user_id);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@android.support.annotation.Nullable final DocumentSnapshot snapshot,
                                @android.support.annotation.Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }
                if (snapshot != null && snapshot.exists()) {
                    Toast.makeText ( getApplicationContext (),"new change",Toast.LENGTH_LONG ).show ();
                    firebaseFirestore=FirebaseFirestore.getInstance ();
                    firebaseFirestore.collection ( "mes donnees utilisateur" ).document (current_user_id).get ().addOnCompleteListener ( new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful ()){
                                if (task.getResult ().exists ()){
                                    String message= task.getResult ().getString ( "message" );
                                    contenu = task.getResult ().getString ( "message_du_notifieur" );
                                    nom_du_notifieur = task.getResult ().getString ( "nom_du_notifieur" );
                                    lien_de_limag_du_notifieur = task.getResult ().getString ( "lien_de_limag_du_notifieur" );
                                    lien_du_produit=task.getResult ().getString ( "lien_du_produit" );

                                    if (message.equals ( "non lu" )){

                                        try {
                                            sendNotif ( message ,nom_du_notifieur ,lien_du_produit);
                                        } catch (IOException e1) {
                                            e1.printStackTrace ();
                                        }

                                    }else{
                                         }
                                }else {

                                }
                            }else{
                            }
                        }
                    } );
                } else {
                }
            }
        });



        //do heavy work on a background thread
        //stopSelf();

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy ();
        Toast.makeText ( this,"service arrete",Toast.LENGTH_LONG ).show ();

    }

    public void sendNotif(String message,String nom_du_notifieur,String image_produit) throws IOException {
        Intent notificationIntent = new Intent( this, ChatMessageActivity.class);
        InputStream in;
        URL url = new URL (image_produit);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoInput(true);
        connection.connect();
        in = connection.getInputStream();
        Bitmap myBitmap = BitmapFactory.decodeStream(in);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        Uri notificationSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(nom_du_notifieur)
                .setContentText(message)
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher_logo_two)
                .setLargeIcon ( myBitmap )
                .setSound(notificationSoundUri)
                .setContentIntent(pendingIntent)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(message))
                .build();
        startForeground(1, notification);
    }


}

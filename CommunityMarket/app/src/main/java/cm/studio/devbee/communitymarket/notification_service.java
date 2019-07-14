package cm.studio.devbee.communitymarket;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
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

import androidx.annotation.Nullable;
import cm.studio.devbee.communitymarket.profile.ParametrePorfilActivity;

public class notification_service extends Service {
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private String current_user_id;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate ();
        firebaseAuth=FirebaseAuth.getInstance ();
        current_user_id=firebaseAuth.getCurrentUser ().getUid ();
        final DocumentReference docRef = firebaseFirestore.collection ( "mes donnees utilisateur" ).document (current_user_id);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@android.support.annotation.Nullable DocumentSnapshot snapshot,
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
                                    if (message.equals ( "non lu" )){


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
        Toast.makeText ( this,"service lance",Toast.LENGTH_LONG ).show ();
    }

    @Override
    public void onDestroy() {
        super.onDestroy ();
        Toast.makeText ( this,"service arrete",Toast.LENGTH_LONG ).show ();

    }

}

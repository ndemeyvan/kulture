package cm.studio.devbee.communitymarket;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceIdService;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private FirebaseAuth firebaseAuth;
    private String current_id;
    private static final String TAG = "mFirebaseIIDService";
    private  String SUBSCRIBE_TO ;
   // = "userABC"

    @Override
    public void onTokenRefresh() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            firebaseAuth=FirebaseAuth.getInstance ();
            current_id=firebaseAuth.getCurrentUser ().getUid ();
            String token = FirebaseInstanceId.getInstance().getToken();
            FirebaseMessaging.getInstance().subscribeToTopic(SUBSCRIBE_TO);
        } else {
            // No user is signed in
        }
        SUBSCRIBE_TO=current_id;
        /*
          This method is invoked whenever the token refreshes
          OPTIONAL: If you want to send messages to this application instance
          or manage this apps subscriptions on the server side,
          you can send this token to your server.
        */


        // Once the token is generated, subscribe to topic with the userId

    }
}
package cm.studio.devbee.communitymarket.profile;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import javax.annotation.Nullable;

import cm.studio.devbee.communitymarket.Accueil;
import cm.studio.devbee.communitymarket.R;
import cm.studio.devbee.communitymarket.gridView_post.GridViewAdapter;
import cm.studio.devbee.communitymarket.gridView_post.ModelGridView;
import cm.studio.devbee.communitymarket.notification_utils.Model_notification;
import cm.studio.devbee.communitymarket.notification_utils.NotificationAdapter;

public class NotificationActivity extends AppCompatActivity {
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private String current_user_id;
    private static RecyclerView chaussuresRecyclerView;
    private static NotificationAdapter categoriesAdaptechaussures;
    private TextView text_empty;
    private Toolbar detail_image_post_toolbar;
    private ProgressBar notification_progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        current_user_id=firebaseAuth.getCurrentUser().getUid();
        detail_image_post_toolbar=findViewById(R.id.toolbar_notification);
        notification_progress=findViewById ( R.id.notification_progress );
        setSupportActionBar(detail_image_post_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        text_empty=findViewById ( R.id.text_empty );
        detail_image_post_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity ( new Intent ( getApplicationContext (),Accueil.class ).setFlags ( Intent.FLAG_ACTIVITY_CLEAR_TOP ) );
                finish ();
            }
        });
        firebaseFirestore.collection ( "mes donnees utilisateur" ).document (current_user_id).collection ( "mes notification" ).addSnapshotListener(NotificationActivity.this,new EventListener<QuerySnapshot> () {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    int i = queryDocumentSnapshots.size();

                } else {
                    text_empty.setVisibility ( View.VISIBLE );
                }
            }
        });
        DocumentReference user = firebaseFirestore.collection("mes donnees utilisateur" ).document(current_user_id);
        user.update("has_notification", "false")
                .addOnSuccessListener(NotificationActivity.this,new OnSuccessListener<Void> () {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                })
                .addOnFailureListener(NotificationActivity.this,new OnFailureListener () {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });

        chaussureRecyclerView();

    }

    @Override
    protected void onStart() {
        super.onStart();
        categoriesAdaptechaussures.startListening();
    }

    public void chaussureRecyclerView(){
        Query firstQuery =firebaseFirestore.collection ( "mes donnees utilisateur" ).document (current_user_id).collection ( "mes notification" ).orderBy ( "date_du_like",Query.Direction.DESCENDING );
        FirestoreRecyclerOptions<Model_notification> options = new FirestoreRecyclerOptions.Builder<Model_notification>()
                .setQuery(firstQuery, Model_notification.class)
                .build();
        categoriesAdaptechaussures  = new NotificationAdapter(options,NotificationActivity.this);
        chaussuresRecyclerView = findViewById(R.id.notification_recycler);
        chaussuresRecyclerView.setHasFixedSize(true);
        chaussuresRecyclerView.setLayoutManager(new LinearLayoutManager(NotificationActivity.this,LinearLayoutManager.VERTICAL,false));
        chaussuresRecyclerView.setAdapter(categoriesAdaptechaussures);
        notification_progress.setVisibility ( View.INVISIBLE );
    }
    public void userstatus(String status){
        DocumentReference user = firebaseFirestore.collection("mes donnees utilisateur" ).document(current_user_id);
        user.update("status", status)
                .addOnSuccessListener(NotificationActivity.this,new OnSuccessListener<Void> () {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                })
                .addOnFailureListener(NotificationActivity.this,new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }



    @Override
    public void onResume() {
        super.onResume ();
        userstatus("online");
    }

    @Override
    public void onPause() {
        super.onPause ();
        userstatus("offline");

    }



    @Override
    public void onBackPressed() {
        finish();
    }
}

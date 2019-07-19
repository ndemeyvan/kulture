package cm.studio.devbee.communitymarket.messagerie;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import cm.studio.devbee.communitymarket.Accueil;
import cm.studio.devbee.communitymarket.MyFirebaseMessagingService;
import cm.studio.devbee.communitymarket.R;
import cm.studio.devbee.communitymarket.utilForChat.DiplayAllChat;
import cm.studio.devbee.communitymarket.utilForChat.List_chat_adapter;

import static android.view.View.VISIBLE;

public class ChatMessageActivity extends AppCompatActivity {
    private  static FirebaseAuth firebaseAuth;
    private  static String current_user;
    private static FirebaseFirestore firebaseFirestore;
    private static RecyclerView contatc_recyclerview;
    // private GroupAdapter groupAdapter;
    private static Toolbar message_toolbar;
    private String image_profil;
    private String status;
    private  TextView conversation_count;
    private ProgressBar chat_progress;
    private DiplayAllChat diplayAllChat;
    List_chat_adapter list_chat_adapter;
    List<DiplayAllChat> diplayAllChatList;
    String valeur;
    private String ouvert;
    private String viens_de_detail;
    // private  static  CircleImageView online_status_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_chat_message );
        firebaseAuth=FirebaseAuth.getInstance ();
        message_toolbar=findViewById ( R.id.message_toolbar );
        setSupportActionBar (message_toolbar);
        getSupportActionBar ().setTitle ( "Discussions" );
        valeur=getIntent ().getStringExtra ( "viens" );
        ouvert=getIntent ().getStringExtra ( "ouvert" );
        viens_de_detail=getIntent ().getStringExtra ( "viens_de_detail" );
        contatc_recyclerview=findViewById ( R.id.contatc_recyclerview );
        current_user=firebaseAuth.getCurrentUser ().getUid ();
        contatc_recyclerview.setLayoutManager ( new LinearLayoutManager ( getApplicationContext () ) );
        diplayAllChatList=new ArrayList<> (  );
        list_chat_adapter=new List_chat_adapter (diplayAllChatList,ChatMessageActivity.this);
        firebaseFirestore=FirebaseFirestore.getInstance ();
        contatc_recyclerview.setAdapter (list_chat_adapter  );
        conversation_count=findViewById(R.id.conversation_count);
        chat_progress=findViewById(R.id.chat_progress);
        chat_progress.setVisibility(VISIBLE);
        //online_status_image=findViewById ( R.id.online_status_image );
        recuperation ();
        getSupportActionBar ().setDisplayHomeAsUpEnabled ( true );
        message_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity ( new Intent ( getApplicationContext (),Accueil.class ).setFlags ( Intent.FLAG_ACTIVITY_CLEAR_TOP ) );
                finish ();

            }
        });

        DocumentReference user = firebaseFirestore.collection("mes donnees utilisateur" ).document(current_user);
        user.update("message", "lu")
                .addOnSuccessListener(new OnSuccessListener<Void> () {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });

        firebaseFirestore.collection ( "dernier_message" ).document (current_user).collection ( "contacts"  ).addSnapshotListener ( this,new EventListener<QuerySnapshot> () {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (queryDocumentSnapshots.isEmpty ()){
                    conversation_count.setVisibility(VISIBLE);
                    chat_progress.setVisibility(View.INVISIBLE);
                }
            }
        } );

        stopService(new Intent(ChatMessageActivity.this,MyFirebaseMessagingService.class));


    }
    @Override
    public void onBackPressed() {
             finish ();
    }





    public  void recuperation(){
        Query firstQuery =firebaseFirestore.collection ( "dernier_message" )
                .document (current_user).collection ( "contacts" ).orderBy ( "temps",Query.Direction.DESCENDING );
        firstQuery.addSnapshotListener( ChatMessageActivity.this,new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for (DocumentChange doc:queryDocumentSnapshots.getDocumentChanges()){
                    if (doc.getType()==DocumentChange.Type.ADDED){
                        DiplayAllChat model=doc.getDocument().toObject(DiplayAllChat.class);
                        diplayAllChatList.add ( model );
                        list_chat_adapter.notifyDataSetChanged ();
                        //groupAdapter.add(new ContactItem ( model ) );
                        //groupAdapter.notifyDataSetChanged();
                    }
                }

            }
        });
        AnimationDrawable animationDrawableOne = (AnimationDrawable) message_toolbar.getBackground();
        animationDrawableOne.setEnterFadeDuration(2000);
        animationDrawableOne.setExitFadeDuration(4000);
        animationDrawableOne.start();
        chat_progress.setVisibility(View.INVISIBLE);
    }


    public void userstatus(String status){
        DocumentReference user = firebaseFirestore.collection("mes donnees utilisateur" ).document(current_user);
        user.update("status", status)
                .addOnSuccessListener(this,new OnSuccessListener<Void> () {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                })
                .addOnFailureListener(new OnFailureListener () {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });

        DocumentReference link = firebaseFirestore.collection("mes donnees utilisateur" ).document(current_user);
        link.update("message", "lu")
                .addOnSuccessListener(this,new OnSuccessListener<Void> () {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume ();
        userstatus("online");


    }

    @Override
    protected void onPause() {
        super.onPause ();
        userstatus("offline");
    }




}

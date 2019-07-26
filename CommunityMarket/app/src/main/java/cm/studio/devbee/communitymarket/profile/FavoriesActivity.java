package cm.studio.devbee.communitymarket.profile;

import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
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

import cm.studio.devbee.communitymarket.R;
import cm.studio.devbee.communitymarket.gridView_post.ModelGridView;
public class FavoriesActivity extends AppCompatActivity {
    private  static RecyclerView favorite_recycler;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private String current_user;
    private static FavoriteAdapter favories_adapter;
    private Toolbar favorite_toolbar;
    TextView text_empty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_favories );
        firebaseAuth=FirebaseAuth.getInstance ();
        firebaseFirestore=FirebaseFirestore.getInstance ();
        current_user=firebaseAuth.getCurrentUser ().getUid ();
        favorite_toolbar=findViewById ( R.id.favorite_toolbar );
        setSupportActionBar ( favorite_toolbar );
        getSupportActionBar ().setDisplayHomeAsUpEnabled ( true );
        favorite_toolbar.setNavigationOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                finish ();
            }
        } );
        favorie ();
        AnimationDrawable animationDrawableOne = (AnimationDrawable) favorite_toolbar.getBackground();
        animationDrawableOne.setEnterFadeDuration(2000);
        animationDrawableOne.setExitFadeDuration(4000);
        animationDrawableOne.start();
        text_empty=findViewById ( R.id.text_empty );
        firebaseFirestore.collection ( "mes donnees utilisateur" ).document (current_user).collection ( "mes favories" ).addSnapshotListener(FavoriesActivity.this,new EventListener<QuerySnapshot> () {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    int i = queryDocumentSnapshots.size();

                } else {
                    text_empty.setVisibility ( View.VISIBLE );
                }
            }
        });
    }

    public void favorie(){
        Query firstQuery =firebaseFirestore.collection ( "mes donnees utilisateur" ).document (current_user).collection ( "mes favories" ).orderBy ( "prix_du_produit",Query.Direction.ASCENDING );
        FirestoreRecyclerOptions<ModelGridView> options = new FirestoreRecyclerOptions.Builder<ModelGridView>()
                .setQuery(firstQuery, ModelGridView.class)
                .build();
        favories_adapter  = new FavoriteAdapter (options,this);
        favorite_recycler = findViewById(R.id.favorite_recycler);
        favorite_recycler.setHasFixedSize(true);
        favorite_recycler.setLayoutManager(new LinearLayoutManager (this,LinearLayoutManager.VERTICAL,false));
        favorite_recycler.setAdapter(favories_adapter);

    }

    @Override
    protected void onStart() {
        super.onStart ();
        favories_adapter.startListening ();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed ();
        finish ();
    }

    public void userstatus(String status){
        DocumentReference user = firebaseFirestore.collection("mes donnees utilisateur" ).document(current_user);
        user.update("status", status)
                .addOnSuccessListener(new OnSuccessListener<Void> () {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                })
                .addOnFailureListener(new OnFailureListener () {
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
}

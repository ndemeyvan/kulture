package cm.studio.devbee.communitymarket.search;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import cm.studio.devbee.communitymarket.R;
import cm.studio.devbee.communitymarket.gridView_post.ModelGridView;
import cm.studio.devbee.communitymarket.utilsForUserApp.UserAdapter;

public class SearchActivity extends AppCompatActivity {
    private EditText search_edit_text;
    private RecyclerView search_recyclerview;
    private FirebaseFirestore db;
    private static FirebaseAuth firebaseAuth;
    private static String current_user;
    private List<ModelGridView> listUsers;
    private List<Seach_user_model> listUserstwo;

    private UserAdapter searchAdapter;
    private Search_user_adapter searchAdaptertwo;

    private Toolbar toolbar_search;
    private ProgressBar search_progress;
    private ImageButton search_button;
    private Spinner recherche;
    private String choix;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_search );
        search_edit_text=findViewById ( R.id.search_edit_text );
        search_recyclerview=findViewById ( R.id.search_recyclerview );
        listUsers = new ArrayList<>();
        searchAdapter= new UserAdapter ( listUsers,SearchActivity.this );
        searchAdaptertwo= new Search_user_adapter ( listUserstwo,SearchActivity.this);

        search_recyclerview.setLayoutManager ( new LinearLayoutManager ( getApplicationContext (),LinearLayoutManager.VERTICAL,false ) );
        search_recyclerview.setAdapter ( searchAdapter );
        db = FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance ();
        current_user=firebaseAuth.getCurrentUser ().getUid ();
        toolbar_search=findViewById(R.id.toolbar_search);
        search_progress=findViewById(R.id.search_progress);
        search_progress.setVisibility(View.INVISIBLE);
        search_button=findViewById ( R.id.search_button );
        setSupportActionBar(toolbar_search);
        getSupportActionBar().setTitle("search");
        getSupportActionBar ().setDisplayHomeAsUpEnabled ( true );
        recherche=findViewById ( R.id.recherche );
        toolbar_search.setNavigationOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                //startActivity ( new Intent ( getApplicationContext (),Accueil.class ).setFlags ( Intent.FLAG_ACTIVITY_CLEAR_TOP ) );
                finish ();
            }
        } );

        search_button.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                recherche.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(final AdapterView<?> parent, View view, final int position, long id) {
                        choix=parent.getItemAtPosition(position).toString();
                        if (choix.equals ( "articles" )){
                            searchOne ();
                        }else{
                            searchTwo ();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        toas ( "choisir une categories" );
                    }
                });
                searchAdapter.getFilter ().filter (  search_edit_text.getText ().toString () );
            }
        } );

        AnimationDrawable animationDrawableOne = (AnimationDrawable) toolbar_search.getBackground();
        animationDrawableOne.setEnterFadeDuration(2000);
        animationDrawableOne.setExitFadeDuration(4000);
        animationDrawableOne.start();

        ///////////////
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed ();
        finish ();
    }

    private void searchOne() {
        db.collection ( "publication" ).document ("categories").collection (choix ).addSnapshotListener ( SearchActivity.this, new EventListener<QuerySnapshot> () {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges () ){
                    if (doc.getType ()==DocumentChange.Type.ADDED){
                        ModelGridView modelGridView = doc.getDocument ().toObject ( ModelGridView.class );
                        listUsers.add ( modelGridView);
                        searchAdapter.notifyDataSetChanged ();
                    }
                }
            }
        } );
    }
    private void searchTwo() {
        db.collection ( "mes donnees utilisateur" ).addSnapshotListener ( this, new EventListener<QuerySnapshot> () {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges () ){
                    if (doc.getType ()==DocumentChange.Type.ADDED){
                        Seach_user_model modelGridView = doc.getDocument ().toObject ( Seach_user_model.class );
                        if (!modelGridView.getId_utilisateur ().equals ( current_user )){
                            listUserstwo.add ( modelGridView);
                        }
                        searchAdaptertwo.notifyDataSetChanged ();
                    }
                }
            }
        } );
    }

    public void userstatus(String status){
        DocumentReference user = db.collection("mes donnees utilisateur" ).document(current_user);
        user.update("status", status)
                .addOnSuccessListener(SearchActivity.this,new OnSuccessListener<Void> () {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                })
                .addOnFailureListener(SearchActivity.this,new OnFailureListener() {
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

    public void toas(String s){
        Toast.makeText ( getApplicationContext (), s, Toast.LENGTH_SHORT ).show ();
    }


}
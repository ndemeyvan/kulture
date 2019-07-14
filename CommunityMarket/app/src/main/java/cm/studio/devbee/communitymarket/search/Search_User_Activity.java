package cm.studio.devbee.communitymarket.search;

import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

import cm.studio.devbee.communitymarket.R;
import cm.studio.devbee.communitymarket.gridView_post.ModelGridView;
import cm.studio.devbee.communitymarket.utilsForUserApp.UserAdapter;

public class Search_User_Activity extends AppCompatActivity {
    private EditText search_edit_text;
    private RecyclerView search_recyclerview;
    private FirebaseFirestore db;
    private static FirebaseAuth firebaseAuth;
    private static String current_user;
    Toolbar toolbarSearch;
    private Search_user_adapter searchAdapter;
    private ImageView search_button;
    private ProgressBar search_progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_search );
        search_edit_text=findViewById ( R.id.search_edit_text );
        search_recyclerview=findViewById ( R.id.search_recyclerview );
        setSupportActionBar(toolbarSearch);
        search_recyclerview.setLayoutManager ( new LinearLayoutManager ( getApplicationContext (),LinearLayoutManager.VERTICAL,false ) );
        db = FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance ();
        current_user=firebaseAuth.getCurrentUser ().getUid ();
        search_button=findViewById(R.id.search_button);
        search_progress=findViewById(R.id.search_progress);
        search_progress.setVisibility(View.INVISIBLE);
        getSupportActionBar().setTitle("search");
        getSupportActionBar ().setDisplayHomeAsUpEnabled ( true );
        toolbarSearch.setNavigationOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                //startActivity ( new Intent ( getApplicationContext (),Accueil.class ).setFlags ( Intent.FLAG_ACTIVITY_CLEAR_TOP ) );
                finish ();
            }
        } );
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_progress.setVisibility(View.VISIBLE);
                if (search_edit_text.getText().toString().toLowerCase().equals ( "" )){
                    Toast.makeText ( Search_User_Activity.this,"search empty",Toast.LENGTH_LONG ).show ();
                    search_progress.setVisibility(View.INVISIBLE);
                }else{
                    search(search_edit_text.getText().toString().toLowerCase());
                }
            }
        });

        AnimationDrawable animationDrawableOne = (AnimationDrawable) toolbarSearch.getBackground();
        animationDrawableOne.setEnterFadeDuration(2000);
        animationDrawableOne.setExitFadeDuration(4000);
        animationDrawableOne.start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed ();
        finish ();
    }

    private void search(final String s) {
        Query firstQuery =db.collection ( "publication" ).document ("categories").collection ("nouveaux" ).orderBy( "decription_du_produit").startAt(s).endAt(s+"\uf8ff");
        FirestoreRecyclerOptions<Seach_user_model> options = new FirestoreRecyclerOptions.Builder<Seach_user_model>()
                .setQuery(firstQuery, Seach_user_model.class)
                .build();
        searchAdapter  = new Search_user_adapter (options,Search_User_Activity.this);
        search_recyclerview = findViewById(R.id.search_recyclerview);
        search_recyclerview.setHasFixedSize(true);
        search_recyclerview.setLayoutManager(new LinearLayoutManager(Search_User_Activity.this,LinearLayoutManager.VERTICAL,false));
        search_recyclerview.setAdapter(searchAdapter);
        searchAdapter.startListening ();
        search_progress.setVisibility(View.INVISIBLE);
    }


}

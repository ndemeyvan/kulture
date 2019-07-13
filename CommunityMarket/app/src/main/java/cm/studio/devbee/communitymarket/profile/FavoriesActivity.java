package cm.studio.devbee.communitymarket.profile;

import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import cm.studio.devbee.communitymarket.R;
import cm.studio.devbee.communitymarket.gridView_post.GridViewAdapter;
import cm.studio.devbee.communitymarket.gridView_post.ModelGridView;
import cm.studio.devbee.communitymarket.utilsForPostPrincipal.PrincipalAdapte;
import cm.studio.devbee.communitymarket.utilsForPostPrincipal.PrincipalModel;

public class FavoriesActivity extends AppCompatActivity {
    private  static RecyclerView favorite_recycler;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private String current_user;
    private static FavoriteAdapter favories_adapter;
    private Toolbar favorite_toolbar;

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
}

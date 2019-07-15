
package cm.studio.devbee.communitymarket.profile;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import cm.studio.devbee.communitymarket.Accueil;
import cm.studio.devbee.communitymarket.R;
import cm.studio.devbee.communitymarket.gridView_post.ModelGridView;
import cm.studio.devbee.communitymarket.utilsForPostPrincipal.PrincipalAdapte;
import cm.studio.devbee.communitymarket.utilsForPostPrincipal.PrincipalModel;
import cm.studio.devbee.communitymarket.utilsForVendeur.ProfilAdapteur;
import cm.studio.devbee.communitymarket.utilsForVendeur.VendeurAdapteur;

public class VenteUtilisateurActivity extends AppCompatActivity {
    private static RecyclerView Recycler;
    private static android.support.v7.widget.Toolbar profil_toolbar;
    private static FirebaseAuth mAuth;
    private static FirebaseFirestore firebaseFirestore;
    private static String current_user_id;
    private static ProfilAdapteur gridViewAdapter;
    private TextView vente_presente;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_vente_utilisateur );
        profil_toolbar=findViewById(R.id.profil_toolbar);
        Recycler=findViewById(R.id.profilRecycler);
        setSupportActionBar(profil_toolbar);
        mAuth=FirebaseAuth.getInstance (  );
        vente_presente=findViewById ( R.id.vente_presente );
        vente_presente.setVisibility ( View.INVISIBLE );
        getSupportActionBar ().setDisplayHomeAsUpEnabled ( true );
        firebaseFirestore=FirebaseFirestore.getInstance ();
        profil_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity ( new Intent ( getApplicationContext (),ProfileActivity.class ).setFlags ( Intent.FLAG_ACTIVITY_CLEAR_TOP ) );
                finish ();
            }
        });
       recyclerprofil();
        firebaseFirestore.collection ( "publication" ).document ("post utilisateur").collection ( current_user_id ).addSnapshotListener ( this,new EventListener<QuerySnapshot> () {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (!queryDocumentSnapshots.isEmpty ()){

                }else{
                    vente_presente.setVisibility ( View.VISIBLE );
                }
            }
        } );

        firebaseFirestore.collection ( "publication" ).document ("post utilisateur").collection ( current_user_id ).addSnapshotListener ( this,new EventListener<QuerySnapshot> () {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (!queryDocumentSnapshots.isEmpty ()){
                    int i=queryDocumentSnapshots.size ();
                    getSupportActionBar ().setTitle ( i +" "+getString(R.string.article_en_vente));

                }else{
                    getSupportActionBar ().setTitle ( getString(R.string.no_article));
                }
            }
        } );

        AnimationDrawable animationDrawableOne = (AnimationDrawable) profil_toolbar.getBackground();
        animationDrawableOne.setEnterFadeDuration(2000);
        animationDrawableOne.setExitFadeDuration(4000);
        animationDrawableOne.start();


    }

    @Override
    protected void onStart() {
        super.onStart ();
        gridViewAdapter.startListening ();
    }

    public void recyclerprofil(){
        current_user_id=mAuth.getCurrentUser ().getUid ();
        Query firstQuery =firebaseFirestore.collection ( "publication" ).document ("post utilisateur").collection ( current_user_id ).orderBy ( "prix_du_produit",Query.Direction.DESCENDING );
        FirestoreRecyclerOptions<ModelGridView> options = new FirestoreRecyclerOptions.Builder<ModelGridView>()
                .setQuery(firstQuery, ModelGridView.class)
                .build();
        gridViewAdapter  = new ProfilAdapteur (options,this);
        Recycler = findViewById(R.id.profilRecycler);
        Recycler.setHasFixedSize(true);
        Recycler.setLayoutManager(new LinearLayoutManager (this,LinearLayoutManager.VERTICAL,false));
        Recycler.setAdapter(gridViewAdapter);

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed ();
        finish ();
    }
}

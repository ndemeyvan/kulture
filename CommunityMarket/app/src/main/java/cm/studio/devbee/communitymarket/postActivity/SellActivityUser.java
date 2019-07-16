package cm.studio.devbee.communitymarket.postActivity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import cm.studio.devbee.communitymarket.R;
import cm.studio.devbee.communitymarket.gridView_post.ModelGridView;
import cm.studio.devbee.communitymarket.profile.FavoriesActivity;
import cm.studio.devbee.communitymarket.utilsForPostPrincipal.PrincipalAdapte;
import cm.studio.devbee.communitymarket.utilsForPostPrincipal.PrincipalModel;
import cm.studio.devbee.communitymarket.utilsForVendeur.ProfilAdapteur;
import cm.studio.devbee.communitymarket.utilsForVendeur.VendeurAdapteur;

public class SellActivityUser extends AppCompatActivity {
    private static RecyclerView vendeur_recyclerView;
    private static VendeurAdapteur gridViewAdapter;
    private static FirebaseFirestore firebaseFirestore;
    String current_user_id;
    private static Toolbar vendeur_toolbar;
    TextView vente_presente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_user);
        vendeur_toolbar=findViewById(R.id.vendeur_toolbar);
        setSupportActionBar(vendeur_toolbar);
        getSupportActionBar ().setDisplayHomeAsUpEnabled ( true );
        vente_presente=findViewById ( R.id.vente_presente );
        current_user_id =getIntent().getExtras().getString("id de l'utilisateur");
        firebaseFirestore=FirebaseFirestore.getInstance();
        vendeur_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        vendeur_produit();
        vente_presente.setVisibility ( View.INVISIBLE );
        firebaseFirestore.collection ( "publication" ).document ("post utilisateur").collection ( current_user_id ).addSnapshotListener(SellActivityUser.this,new EventListener<QuerySnapshot> () {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    int i = queryDocumentSnapshots.size();

                } else {
                    vente_presente.setVisibility ( View.VISIBLE );
                }
            }
        });
        nom();
    }
    public void vendeur_produit(){
        Query firstQuery =firebaseFirestore.collection ( "publication" ).document ("post utilisateur").collection ( current_user_id ).orderBy ( "prix_du_produit",Query.Direction.DESCENDING );
        FirestoreRecyclerOptions<ModelGridView> options = new FirestoreRecyclerOptions.Builder<ModelGridView>()
                .setQuery(firstQuery, ModelGridView.class)
                .build();
        gridViewAdapter  = new VendeurAdapteur (options,SellActivityUser.this);
        vendeur_recyclerView = findViewById(R.id.vendeur_recyclerView);
        vendeur_recyclerView.setHasFixedSize(true);
        vendeur_recyclerView.setLayoutManager(new LinearLayoutManager (SellActivityUser.this,LinearLayoutManager.VERTICAL,false));
        vendeur_recyclerView.setAdapter(gridViewAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart ();
        gridViewAdapter.startListening ();
    }

    public void nom(){
        firebaseFirestore.collection("mes donnees utilisateur").document(current_user_id).get().addOnCompleteListener(SellActivityUser.this,new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult ().exists ()){
                        String prenom=task.getResult ().getString ( "user_prenom" );
                        String name_user= task.getResult ().getString ( "user_name" );
                        getSupportActionBar().setTitle(name_user +" " + prenom);
                    }
                }else {
                    String error=task.getException().getMessage();
                    Toast.makeText(getApplicationContext(),error,Toast.LENGTH_LONG).show();

                }
            }
        });
    }
}

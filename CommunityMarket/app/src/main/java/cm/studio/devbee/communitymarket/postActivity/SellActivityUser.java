package cm.studio.devbee.communitymarket.postActivity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

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
import cm.studio.devbee.communitymarket.utilsForVendeur.ProfilAdapteur;
import cm.studio.devbee.communitymarket.utilsForVendeur.VendeurAdapteur;

public class SellActivityUser extends AppCompatActivity {
    String iddupost;
    private static RecyclerView vendeur_recyclerView;
    private static VendeurAdapteur gridViewAdapter;
    private static List<ModelGridView> modelGridViewList;
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
        iddupost =getIntent().getExtras().getString("id du post");
        current_user_id =getIntent().getExtras().getString("id de l'utilisateur");
        vendeur_recyclerView=findViewById(R.id.vendeur_recyclerView);
        firebaseFirestore=FirebaseFirestore.getInstance();
        modelGridViewList=new ArrayList<>();
        vendeur_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        gridViewAdapter=new VendeurAdapteur(modelGridViewList,SellActivityUser.this);
        vendeur_recyclerView.setAdapter(gridViewAdapter);
        vendeur_recyclerView.setLayoutManager(new GridLayoutManager (SellActivityUser.this,2));
        vendeur_produit();
        vente_presente.setVisibility ( View.INVISIBLE );
        nom();
    }
    public void vendeur_produit(){
        Query firstQuery =firebaseFirestore.collection ( "publication" ).document ("post utilisateur").collection ( current_user_id ).orderBy ( "prix_du_produit",Query.Direction.DESCENDING );
        firstQuery.addSnapshotListener(SellActivityUser.this,new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for (DocumentChange doc:queryDocumentSnapshots.getDocumentChanges()){
                    if (doc.getType()==DocumentChange.Type.ADDED){
                        String idupost=doc.getDocument ().getId ();
                        ModelGridView modelGridView =doc.getDocument().toObject(ModelGridView.class).withId ( idupost );
                        modelGridViewList.add(modelGridView);
                        gridViewAdapter.notifyDataSetChanged();
                    }
                }

            }
        });

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

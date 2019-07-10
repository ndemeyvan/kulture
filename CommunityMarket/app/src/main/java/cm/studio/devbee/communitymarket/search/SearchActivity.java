package cm.studio.devbee.communitymarket.search;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;
import cm.studio.devbee.communitymarket.R;
import cm.studio.devbee.communitymarket.gridView_post.ModelGridView;
import cm.studio.devbee.communitymarket.utilsForUserApp.UserAdapter;

public class SearchActivity extends AppCompatActivity {
    private EditText search_edit_text;
    private RecyclerView search_recyclerview;
    private FirebaseFirestore db;
    private static FirebaseAuth firebaseAuth;
    private static String current_user;
    Toolbar toolbarSearch;
    private List<ModelGridView> listUsers;
    private UserAdapter searchAdapter;
    private ImageView search_button;
    private Toolbar toolbar_search;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_search );
        search_edit_text=findViewById ( R.id.search_edit_text );
        search_recyclerview=findViewById ( R.id.search_recyclerview );
        listUsers = new ArrayList<>();
        setSupportActionBar(toolbarSearch);
        search_recyclerview.setLayoutManager ( new LinearLayoutManager ( getApplicationContext (),LinearLayoutManager.VERTICAL,false ) );
        db = FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance ();
        current_user=firebaseAuth.getCurrentUser ().getUid ();
        search_button=findViewById(R.id.search_button);
        toolbar_search=findViewById(R.id.toolbar_search);
        setSupportActionBar(toolbar_search);
        getSupportActionBar().setTitle("search");
        getSupportActionBar ().setDisplayHomeAsUpEnabled ( true );
        toolbar_search.setNavigationOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                //startActivity ( new Intent ( getApplicationContext (),Accueil.class ).setFlags ( Intent.FLAG_ACTIVITY_CLEAR_TOP ) );
                finish ();
            }
        } );
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search(search_edit_text.getText().toString().toLowerCase());
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed ();
        finish ();
    }

    private void search(final String s) {
        db.collection ( "publication" ).document ("categories").collection ("nouveaux" ).orderBy( "decription_du_produit").startAt(s).endAt(s+"\uf8ff").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        listUsers.clear ();
                        for (DocumentSnapshot doc :task.getResult()) {
                            listUsers.clear ();
                            ModelGridView searchModel = new ModelGridView(doc.getString("nom_du_produit"),doc.getString("image_du_produit"),doc.getString("prix_du_produit"),doc.getString("user_profil_image"),doc.getString("utilisateur"),doc.getString("categories"),doc.getString("decription_du_produit"),doc.getString("date_de_publication"),doc.getString("user_image"),doc.getString("like"),doc.getString("post_id"));
                            if (!current_user.equals ( searchModel.getUtilisateur() )) {
                                listUsers.add ( searchModel );
                            }
                        }
                        searchAdapter = new UserAdapter ( listUsers, SearchActivity.this ,s);
                        search_recyclerview.setAdapter ( searchAdapter );
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

}
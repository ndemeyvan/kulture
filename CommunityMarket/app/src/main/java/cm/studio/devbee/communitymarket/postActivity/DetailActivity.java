package cm.studio.devbee.communitymarket.postActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import cm.studio.devbee.communitymarket.Accueil;
import cm.studio.devbee.communitymarket.R;
import cm.studio.devbee.communitymarket.commentaires.Commentaire_Adapter;
import cm.studio.devbee.communitymarket.commentaires.Commentaires_Model;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class DetailActivity extends AppCompatActivity implements RewardedVideoAdListener {
    private  static String iddupost;
    private static FirebaseFirestore firebaseFirestore;
    private static ImageView detail_image_post;
    private static TextView detail_post_titre_produit;
    private static CircleImageView detail_profil_image;
    private static ImageButton vendeur_button;
    private static TextView detail_user_name;
    private static TextView detail_prix_produit;
    private static TextView detail_description;
    private static TextView date_de_publication;
    private static FirebaseAuth firebaseAuth;
    private static String current_user_id;
    private static String utilisateur_actuel;
    private static AsyncTask asyncTask;
    private static ProgressBar detail_progress;
    private static ImageButton supprime_detail_button;
    private static String lien_image;
    private  static Toolbar toolbarDetail;
    private RewardedVideoAd mad;
    String categories;
    String prenom;
    String name_user;
    CircleImageView post_detail_currentuser_img;
    Button post_detail_add_comment_btn;
    EditText post_detail_comment;
    String comment;
    RecyclerView rv_comment;
    List<Commentaires_Model> commentaires_modelList;
    Commentaire_Adapter commentaire_adapter;
    TextView comment_empty_text;
    android.support.v7.widget.Toolbar detail_image_post_toolbar;
    ProgressBar add_progressbar;
    String titre_produit;
    String prix_produit;


    private static WeakReference<DetailActivity> detailActivityWeakReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_principal_);
        detail_image_post_toolbar=findViewById(R.id.detail_image_post_toolbar);
        setSupportActionBar(detail_image_post_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        detail_image_post_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity ( new Intent ( getApplicationContext (),Accueil.class ).setFlags ( Intent.FLAG_ACTIVITY_CLEAR_TOP ) );
                finish ();
            }
        });
        firebaseAuth=FirebaseAuth.getInstance ();
        utilisateur_actuel=firebaseAuth.getCurrentUser ().getUid ();
        firebaseFirestore=FirebaseFirestore.getInstance();
        iddupost =getIntent().getExtras().getString("id du post");
        current_user_id =getIntent().getExtras().getString("id de l'utilisateur");
        categories=getIntent().getExtras().getString("id_categories");
        add_progressbar=findViewById(R.id.add_progressbar);
        detail_image_post=findViewById(R.id.detail_image_post);
        detail_post_titre_produit=findViewById(R.id.detail_titre_produit);
        detail_prix_produit=findViewById(R.id.detail_prix_produit);
        detail_profil_image=findViewById(R.id.detail_image_du_profil);
        vendeur_button=findViewById(R.id.vendeur_button);
        post_detail_currentuser_img=findViewById(R.id.post_detail_currentuser_img);
        rv_comment=findViewById(R.id.rv_comment);
        commentaires_modelList=new ArrayList<>();
        commentaire_adapter=new Commentaire_Adapter(commentaires_modelList,DetailActivity.this);
        rv_comment.setAdapter(commentaire_adapter);
        rv_comment.setLayoutManager(new LinearLayoutManager(DetailActivity.this,LinearLayoutManager.VERTICAL,false));
        post_detail_add_comment_btn=findViewById(R.id.post_detail_add_comment_btn);
        //detail_user_name=findViewById(R.id.detail_user_name);
        detail_description=findViewById(R.id.detail_description);
        date_de_publication=findViewById(R.id.date_de_publication);
        firebaseAuth=FirebaseAuth.getInstance();
        detail_progress=findViewById ( R.id.detail_progress );
        supprime_detail_button=findViewById ( R.id.supprime_detail_button );
        detailActivityWeakReference=new WeakReference<>(this);
        vendeur_button.setEnabled(true);
        asyncTask=new AsyncTask();
        asyncTask.execute();
        add_progressbar.setVisibility(INVISIBLE);
        comment_empty_text=findViewById(R.id.comment_empty_text);
        firebaseFirestore.collection ( "publication" ).document ("categories").collection ("nouveaux" ).document (iddupost).addSnapshotListener ( this,new EventListener<DocumentSnapshot> () {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (!documentSnapshot.exists ()){
                    Toast.makeText ( DetailActivity.this, getString(R.string.vente_retire), Toast.LENGTH_SHORT ).show ();

                }else {

                }
            }
        } );
        ///////ads"ca-app-pub-3940256099942544~3347511713
        ////my id : ca-app-pub-4353172129870258~6890094527
        MobileAds.initialize(this,"ca-app-pub-4353172129870258~6890094527");
        mad=MobileAds.getRewardedVideoAdInstance(this);
        mad.setRewardedVideoAdListener(this);
        loadRewardedVideo();
        if (mad.isLoaded()) {
            mad.show();
        }
        donne();
        detail_progress.setVisibility ( View.VISIBLE );
        post_detail_comment=findViewById(R.id.post_detail_comment);
        post_detail_add_comment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                post_detail_add_comment_btn.setVisibility(INVISIBLE);
                add_progressbar.setVisibility(VISIBLE);
                comment = post_detail_comment.getText().toString();
                if (!TextUtils.isEmpty ( comment )){
                    Date date=new Date();
                    SimpleDateFormat sdf= new SimpleDateFormat("d/MM/y H:mm:ss");
                    final String date_avec_seconde=sdf.format(date);
                    Calendar calendar=Calendar.getInstance ();
                    SimpleDateFormat currentDate=new SimpleDateFormat (" dd MMM yyyy" );
                    String saveCurrentDate=currentDate.format ( calendar.getTime () );
                    final Map <String,Object> user_comment = new HashMap ();
                    user_comment.put ( "contenu",comment );
                    user_comment.put ( "heure",saveCurrentDate );
                    user_comment.put ( "id_user",utilisateur_actuel );
////////////recuperation des donnerr du vendeur
                    firebaseFirestore.collection ( "mes donnees utilisateur" ).document (utilisateur_actuel).get ().addOnCompleteListener ( new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful ()){
                                if (task.getResult ().exists ()){
                                    String image_profil_user =task.getResult ().getString ("user_profil_image");
                                    Picasso.with ( getApplicationContext() ).load ( image_profil_user ).into ( post_detail_currentuser_img );

                                }
                            }else{

                            }
                        }
                    } );
//////////////////////// recuperation de la categories du post
                    firebaseFirestore.collection ( "publication" ).document ("categories").collection ( "nouveaux" ).document (iddupost).get().addOnCompleteListener(DetailActivity.this,new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()){
                                if (task.getResult ().exists ()){
                                     categories = task.getResult ().getString ( "categories" );
                                }
                            }else {
                                String error=task.getException().getMessage();

                            }
                        }
                    });
////////////////////////////// see if the comment is go
                    firebaseFirestore.collection ( "publication" ).document ("categories").collection ("nouveaux" ).document (iddupost).collection("commentaires").add(user_comment).addOnCompleteListener(DetailActivity.this,new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            Toast.makeText(DetailActivity.this,"votre commentaire a ete envoyer",Toast.LENGTH_LONG).show();
                            add_progressbar.setVisibility(INVISIBLE);
                            post_detail_add_comment_btn.setVisibility(View.VISIBLE);
                            post_detail_comment.setText("");
                            comment_empty_text.setVisibility(INVISIBLE);
                        }
                    });

                }else{

                    Toast.makeText(DetailActivity.this,"votre conenu est vide ",Toast.LENGTH_LONG).show();
                    add_progressbar.setVisibility(View.INVISIBLE);
                    post_detail_add_comment_btn.setVisibility(VISIBLE);

                }

            }
        });
//////////////////////////////
        comment_empty_text.setVisibility(INVISIBLE);
        firebaseFirestore.collection ( "publication" ).document ("categories").collection ("nouveaux" ).document (iddupost).collection("commentaires").addSnapshotListener ( this,new EventListener<QuerySnapshot> () {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (queryDocumentSnapshots.isEmpty ()){
                    comment_empty_text.setVisibility(VISIBLE);

                }
            }
        } );

        commentaire();

    }
////envoi du commentaire
    public void commentaire(){
        Query firstQuery =firebaseFirestore.collection ( "publication" ).document ("categories").collection ( "nouveaux" ).document (iddupost).collection("commentaires").orderBy ( "heure",Query.Direction.ASCENDING );
        // .limit(3);
        firstQuery.addSnapshotListener(DetailActivity.this,new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for (DocumentChange doc:queryDocumentSnapshots.getDocumentChanges()){

                    if (doc.getType()==DocumentChange.Type.ADDED){
                        Commentaires_Model principalAdaptemodel =doc.getDocument().toObject(Commentaires_Model.class);
                        commentaires_modelList.add(principalAdaptemodel);
                        commentaire_adapter.notifyDataSetChanged();
                    }
                }

            }
        });
    }
///ads
    public void loadRewardedVideo(){
        if (!mad.isLoaded()){
            // ca-app-pub-3940256099942544/5224354917
            // my pub id : ca-app-pub-4353172129870258/9670857450
            mad.loadAd("ca-app-pub-4353172129870258/9670857450",new AdRequest.Builder().build());
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        /*Intent gotohome=new Intent(getApplicationContext(),Accueil.class);
        startActivity(gotohome);*/
        finish();
    }

    public void nomEtImageProfil(){
        firebaseFirestore.collection("mes donnees utilisateur").document(current_user_id).get().addOnCompleteListener(DetailActivity.this,new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult ().exists ()){
                         prenom=task.getResult ().getString ( "user_prenom" );
                         name_user= task.getResult ().getString ( "user_name" );
                        String image_user=task.getResult ().getString ( "user_profil_image" );
                       // detail_user_name.setText(name_user+" "+prenom);
                        Picasso.with(getApplicationContext()).load(image_user).into(detail_profil_image);
                        detail_profil_image.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_transition_animation));
                        //detail_user_name.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_transition_animation));

                    }
                }else {
                    String error=task.getException().getMessage();

                }
            }
        });
    }

    public void donne(){
        firebaseFirestore.collection ( "mes donnees utilisateur" ).document (utilisateur_actuel).get ().addOnCompleteListener ( DetailActivity.this,new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful ()){
                    if (task.getResult ().exists ()){
                        String image_profil_user =task.getResult ().getString ("user_profil_image");
                        Picasso.with ( getApplicationContext() ).load ( image_profil_user ).into ( post_detail_currentuser_img );

                    }
                }else{

                }
            }
        } );
    }
    public void supprime(){
        if (current_user_id.equals ( utilisateur_actuel )){
           //supprime_detail_button.setText ( "supprimer de cette categories ?");
            vendeur_button.setVisibility ( INVISIBLE );
            supprime_detail_button.setEnabled ( true );
            supprime_detail_button.setVisibility ( View.VISIBLE );
            supprime_detail_button.setOnClickListener ( new View.OnClickListener () {
                @Override
                public void onClick(View v) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DetailActivity.this);
                        alertDialogBuilder.setMessage(getString(R.string.voulez_vous_supprimer));
                                alertDialogBuilder.setPositiveButton("oui",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface arg0, int arg1) {
                                                firebaseFirestore.collection ( "publication" ).document ("categories").collection ("nouveaux" ).document (iddupost).get ().addOnCompleteListener ( DetailActivity.this,new OnCompleteListener<DocumentSnapshot> () {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        if (task.getResult ().exists ()){
                                                            if (task.isSuccessful ()){
                                                                detail_progress.setVisibility ( View.VISIBLE );
                                                                firebaseFirestore.collection ( "publication" ).document ("categories").collection ( categories ).document (iddupost).delete ();
                                                                firebaseFirestore.collection ( "publication" ).document ("categories").collection ( "nouveaux" ).document (iddupost).delete ();
                                                                firebaseFirestore.collection ( "publication" ).document ("post utilisateur").collection ( current_user_id ).document(iddupost).delete ();
                                                                // Intent gtohome=new Intent ( getApplicationContext (),Accueil.class );
                                                                Toast.makeText ( getApplicationContext (),getString(R.string.supprimer_desnvx),Toast.LENGTH_LONG ).show ();
                                                                //startActivity ( gtohome );
                                                                finish ();

                                                            }else {
                                                                detail_progress.setVisibility ( INVISIBLE );
                                                                String error=task.getException ().getMessage ();
                                                                Toast.makeText ( getApplicationContext (),error,Toast.LENGTH_LONG ).show ();

                                                            }
                                                        }else {

                                                        }
                                                    }
                                                } );
                                            }
                                        });

                        alertDialogBuilder.setNegativeButton("non",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                               dialog.cancel();

                            }
                        });

                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();

                }
            } );
        }else{
            vendeur_button.setVisibility ( View.VISIBLE );
            supprime_detail_button.setVisibility ( INVISIBLE );
        }


    }

    @Override
    public void onRewardedVideoAdLoaded() {
        Toast.makeText(getApplicationContext(),getString(R.string.wait),Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRewardedVideoAdOpened() {
        Toast.makeText(getApplicationContext(),getString(R.string.see_video),Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRewardedVideoStarted() {
        Toast.makeText(getApplicationContext(),getString(R.string.see_video),Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRewardedVideoAdClosed() {
        Toast.makeText(getApplicationContext(),getString(R.string.see_video),Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
       //  mad.destroy(getApplicationContext());
        Toast.makeText(getApplicationContext(),getString(R.string.video_seen_thank),Toast.LENGTH_LONG).show();
        //Toast.makeText(getApplicationContext(),getString(R.string.wait),Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {
        Toast.makeText(getApplicationContext(),"si une video publicitaire ce charge regarder la pour soutenir lappli svp.",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRewardedVideoCompleted() {
        Toast.makeText(getApplicationContext(),getString(R.string.video_seen_thank),Toast.LENGTH_LONG).show();
    }


    public class AsyncTask extends android.os.AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute ();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            firebaseFirestore.collection ( "publication" ).document ("categories").collection ( "nouveaux" ).document (iddupost).get().addOnCompleteListener(DetailActivity.this,new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()){
                        if (task.getResult ().exists ()){
                            String postLike= task.getResult ().getString ( "user_name" );
                            String image_user=task.getResult ().getString ( "user_profil_image" );
                            String titreDuProduit=task.getResult().getString("nom_du_produit");
                            titre_produit=titreDuProduit;
                            String description= task.getResult ().getString ( "decription_du_produit" );
                            String imageduproduit=task.getResult ().getString ( "image_du_produit" );
                            String prixduproduit= task.getResult ().getString ( "prix_du_produit" );
                            prix_produit=prixduproduit;

                            final String datedepublication=task.getResult ().getString ( "date_de_publication" );
                            getSupportActionBar().setTitle(titreDuProduit);
                            detail_post_titre_produit.setText(titreDuProduit);
                            detail_prix_produit.setText(prixduproduit);
                            firebaseFirestore.collection("mes donnees utilisateur").document(current_user_id).get().addOnCompleteListener(DetailActivity.this,new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()){
                                        if (task.getResult ().exists ()){
                                            prenom=task.getResult ().getString ( "user_prenom" );
                                            name_user= task.getResult ().getString ( "user_name" );
                                            String image_user=task.getResult ().getString ( "user_profil_image" );
                                            date_de_publication.setText(datedepublication + "  |  " + name_user+" "+prenom);
                                            Picasso.with(getApplicationContext()).load(image_user).into(detail_profil_image);
                                            detail_profil_image.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_transition_animation));
                                            date_de_publication.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_transition_animation));

                                            //detail_user_name.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_transition_animation));

                                        }
                                    }else {
                                        String error=task.getException().getMessage();

                                    }
                                }
                            });

                            lien_image=imageduproduit;
//                            getSupportActionBar().setTitle(titreDuProduit);
                            Picasso.with(getApplicationContext()).load(imageduproduit).into(detail_image_post);
                            detail_post_titre_produit.setEnabled ( true );
                            detail_prix_produit.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_transition_animation));
                            date_de_publication.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_transition_animation));
                            detail_image_post.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_transition_animation));
                            detail_description.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_transition_animation));
                            supprime_detail_button.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_transition_animation));
                            vendeur_button.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_transition_animation));
                            detail_description.setText(description);
                            detail_progress.setVisibility ( INVISIBLE );

                        }
                    }else {
                        String error=task.getException().getMessage();

                    }
                }
            });
            vendeurActivity();
            nomEtImageProfil();
            supprime ();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute ( aVoid );

        }
    }

    public void vendeurActivity(){

        vendeur_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent vendeur=new Intent(getApplicationContext(),UserGeneralPresentation.class);
                vendeur.putExtra("id du post",iddupost);
                vendeur.putExtra("id de l'utilisateur",current_user_id);
                vendeur.putExtra("image_en_vente",lien_image);
                vendeur.putExtra("titre_produit",titre_produit);
                vendeur.putExtra("prix_produit",prix_produit);
                startActivity(vendeur);
                //finish();
            }
        });
    }

    public void userstatus(String status){
        DocumentReference user = firebaseFirestore.collection("mes donnees utilisateur" ).document(current_user_id);
        user.update("status", status)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
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
    protected void onDestroy() {
        asyncTask.cancel(true);
        super.onDestroy();
        asyncTask.cancel(true);
        iddupost=null;
        firebaseFirestore=null;
        detail_image_post=null;
         detail_post_titre_produit=null;
        detail_profil_image=null;
        vendeur_button=null;
        detail_prix_produit=null;
         detail_description=null;
        date_de_publication=null;
         firebaseAuth=null;
         current_user_id=null;
        utilisateur_actuel=null;
        supprime_detail_button=null;
        detail_progress=null;

    }
}

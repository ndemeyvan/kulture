package cm.studio.devbee.communitymarket.postActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;

import javax.annotation.Nullable;

import cm.studio.devbee.communitymarket.R;
import cm.studio.devbee.communitymarket.profile.ProfileActivity;
import de.hdodenhof.circleimageview.CircleImageView;

public class  DetailActivityFour extends AppCompatActivity {

    private  static String iddupost;
    private static FirebaseFirestore firebaseFirestore;
    private static ImageView detail_image_post;
    private static TextView detail_post_titre_produit;
    private static CircleImageView detail_profil_image;

    private static TextView detail_user_name;
    private static TextView detail_prix_produit;
    private static TextView detail_description;
    private static TextView date_de_publication;
    private static FirebaseAuth firebaseAuth;
    private static String current_user_id;
    private static String utilisateur_actuel;
    private static AsyncTask asyncTask;
    private static FloatingActionButton supprime_detail_button;
    private  static Toolbar toolbarDetail;
    private static String lien_image;
    String prenom;
    String name_user;
    String categories;

    private static WeakReference<DetailActivityFour> detailActivityFourWeakReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_four);
        firebaseAuth=FirebaseAuth.getInstance ();
        utilisateur_actuel=firebaseAuth.getCurrentUser ().getUid ();
        firebaseFirestore=FirebaseFirestore.getInstance();
        iddupost =getIntent().getExtras().getString("id du post");
        current_user_id =getIntent().getExtras().getString("id de l'utilisateur");
        lien_image =getIntent().getExtras().getString("image_en_vente");
        categories=getIntent().getExtras().getString("id_categories");
        detail_image_post=findViewById(R.id.detail_image_post);
        detail_post_titre_produit=findViewById(R.id.detail_prix_produit );
        detail_prix_produit=findViewById(R.id.detail_prix_produit);
        detail_profil_image=findViewById(R.id.detail_image_du_profil);
        //detail_user_name=findViewById(R.id.detail_user_name);
        detail_description=findViewById(R.id.detail_description);
        date_de_publication=findViewById(R.id.date_de_publication);
        firebaseAuth=FirebaseAuth.getInstance();

        toolbarDetail=findViewById(R.id.toolbarDetail);
        supprime_detail_button=findViewById ( R.id.supprime_detail_button );
        detailActivityFourWeakReference=new WeakReference<>(this);
        toolbarDetail=findViewById(R.id.toolbarDetail);
        setSupportActionBar(toolbarDetail);
        getSupportActionBar ().setDisplayHomeAsUpEnabled ( true );

        toolbarDetail.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity ( new Intent ( getApplicationContext (),ProfileActivity.class ).setFlags ( Intent.FLAG_ACTIVITY_CLEAR_TOP ) );
                finish ();

            }
        });

        asyncTask= new AsyncTask ();
        asyncTask.execute();
        firebaseFirestore.collection ( "publication" ).document ("post utilisateur").collection (current_user_id ).document (iddupost).addSnapshotListener ( this,new EventListener<DocumentSnapshot> () {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (!documentSnapshot.exists ()){
                    Toast.makeText ( DetailActivityFour.this, getString(R.string.vente_retire), Toast.LENGTH_SHORT ).show ();

                }else {

                }
            }
        } );

    }
   /* public void nomEtImageProfil(){
        firebaseFirestore.collection("mes donnees utilisateur").document(current_user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot> () {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult ().exists ()){
                        String prenom=task.getResult ().getString ( "user_prenom" );
                        String name_user= task.getResult ().getString ( "user_name" );
                        String image_user=task.getResult ().getString ( "user_profil_image" );
                        //detail_user_name.setText(name_user+" "+prenom);
                        Picasso.with(getApplicationContext()).load(image_user).into(detail_profil_image);
                        detail_profil_image.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_transition_animation));
                        detail_user_name.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_transition_animation));
                    }
                }else {
                    String error=task.getException().getMessage();

                }
            }
        });
    }*/
    @SuppressLint("RestrictedApi")
    public void supprime(){
        if (current_user_id.equals ( utilisateur_actuel )){
            supprime_detail_button.setVisibility ( View.VISIBLE );
            supprime_detail_button.setEnabled ( true );
            //supprime_detail_button.setText ( "supprimer de votre profil ?");
            supprime_detail_button.setVisibility ( View.VISIBLE );
            supprime_detail_button.setOnClickListener ( new View.OnClickListener () {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DetailActivityFour.this);
                    alertDialogBuilder.setMessage(getString(R.string.voulez_vous_supprimer));
                    alertDialogBuilder.setPositiveButton("oui",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    firebaseFirestore.collection ( "publication" ).document ("post utilisateur").collection ( current_user_id ).document (iddupost).get ().addOnCompleteListener ( DetailActivityFour.this,new OnCompleteListener<DocumentSnapshot> () {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.getResult ().exists ()){
                                                if (task.isSuccessful ()){
                                                    firebaseFirestore.collection ( "publication" ).document ("categories").collection ( categories ).document (iddupost).delete ();
                                                    firebaseFirestore.collection ( "publication" ).document ("categories").collection ( "nouveaux" ).document (iddupost).delete ();
                                                    firebaseFirestore.collection ( "publication" ).document ("post utilisateur").collection ( current_user_id ).document(iddupost).delete ();
                                                    finish ();
                                                }else {
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
                    ////////////////////////////////////////

                    /////////////////////////////////////////////////
                }
            } );
        }else{
            supprime_detail_button.setVisibility ( View.INVISIBLE );
        }
    }


    public class AsyncTask extends android.os.AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute ();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            firebaseFirestore.collection ( "publication" ).document ("post utilisateur").collection ( utilisateur_actuel ).document (iddupost).get().addOnCompleteListener(DetailActivityFour.this,new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()){
                        if (task.getResult ().exists ()){
                            String postLike= task.getResult ().getString ( "user_name" );
                            String image_user=task.getResult ().getString ( "user_profil_image" );
                            String titreDuProduit=task.getResult().getString("nom_du_produit");
                            String description= task.getResult ().getString ( "decription_du_produit" );
                            String imageduproduit=task.getResult ().getString ( "image_du_produit" );
                            String prixduproduit= task.getResult ().getString ( "prix_du_produit" );
                            final String datedepublication=task.getResult ().getString ( "date_de_publication" );
                            detail_post_titre_produit.setText(titreDuProduit);
                            detail_prix_produit.setText(prixduproduit);
                            detail_description.setText(description);
                            firebaseFirestore.collection("mes donnees utilisateur").document(current_user_id).get().addOnCompleteListener(DetailActivityFour.this,new OnCompleteListener<DocumentSnapshot>() {
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
                                            //detail_user_name.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_transition_animation));

                                        }
                                    }else {
                                        String error=task.getException().getMessage();

                                    }
                                }
                            });
                           // date_de_publication.setText(datedepublication + "  |  " + name_user+" "+prenom);
                            lien_image=imageduproduit;
                            Picasso.with(getApplicationContext()).load(imageduproduit).into(detail_image_post);
                            getSupportActionBar().setTitle(titreDuProduit);
                            detail_prix_produit.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_transition_animation));
                            date_de_publication.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_transition_animation));
                            detail_image_post.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_transition_animation));
                            detail_description.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_transition_animation));
                        }
                    }else {
                        String error=task.getException().getMessage();

                    }
                }
            });
            //nomEtImageProfil();
            supprime ();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute ( aVoid );

        }
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
        detail_prix_produit=null;
        detail_description=null;
        date_de_publication=null;
        firebaseAuth=null;
        current_user_id=null;
        utilisateur_actuel=null;
        supprime_detail_button=null;


    }

}
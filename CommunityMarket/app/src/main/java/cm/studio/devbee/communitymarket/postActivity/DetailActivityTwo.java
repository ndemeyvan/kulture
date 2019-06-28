package cm.studio.devbee.communitymarket.postActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
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
import com.squareup.picasso.Transformation;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;

import cm.studio.devbee.communitymarket.R;
import cm.studio.devbee.communitymarket.commentaires.Commentaire_Adapter;
import cm.studio.devbee.communitymarket.commentaires.Commentaires_Model;
import cm.studio.devbee.communitymarket.messagerie.MessageActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class DetailActivityTwo extends AppCompatActivity implements RewardedVideoAdListener {
    private  static String iddupost;
    private static FirebaseFirestore firebaseFirestore;
    private static ImageView detail_image_post;
    private static TextView detail_post_titre_produit;
    private static ImageView detail_profil_image;
    private static FloatingActionButton vendeur_button;
    private static TextView detail_user_name;
    private static TextView detail_prix_produit;
    private static TextView detail_description;
    private static TextView date_de_publication;
    private static FirebaseAuth firebaseAuth;
    private static String current_user_id;
    private static String utilisateur_actuel;
    private static AsyncTask asyncTask;
    private static ProgressBar detail_progress;
    private static FloatingActionButton supprime_detail_button;
    private static  String  categories;
    private static  String lien_image;
    private static RewardedVideoAd mad;
    private static WeakReference<DetailActivityTwo> detailActivityTwoWeakReference;
    private static FloatingActionButton voir_les_commentaire_btn;
    private static String image_user;
    private static String prenom;
    private static String name_user;
    private static CircleImageView post_detail_currentuser_img;
    private static String comment;
    private static List<Commentaires_Model> commentaires_modelList;
    private static Commentaire_Adapter commentaire_adapter;
    private static android.support.v7.widget.Toolbar detail_image_post_toolbar;
    private static String titre_produit;
    private static Dialog myDialog;
    private static String prix_produit;
    private String user_mail;
    private String residence_user;
    private String derniere_conection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_detail_two );
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
        detail_image_post=findViewById(R.id.detail_image_post);
        showPopup();

        detail_post_titre_produit=findViewById(R.id.detail_prix_produit );
        detail_prix_produit=findViewById(R.id.detail_prix_produit);
        detail_profil_image=findViewById(R.id.detail_image_du_profil);
        vendeur_button=findViewById(R.id.vendeur_button);
        vendeur_button.setEnabled ( true );
        //detail_user_name=findViewById(R.id.detail_user_name);
        detail_description=findViewById(R.id.detail_description);
        date_de_publication=findViewById(R.id.date_de_publication);
        firebaseAuth=FirebaseAuth.getInstance();
        detail_progress=findViewById ( R.id.detail_progress );
        supprime_detail_button=findViewById ( R.id.supprime_detail_button );
        asyncTask=new AsyncTask ();
        asyncTask.execute();
        detailActivityTwoWeakReference=new WeakReference<>(this);
        post_detail_currentuser_img=findViewById(R.id.post_detail_user_image);
        asyncTask=new AsyncTask ();
        asyncTask.execute();
        firebaseFirestore.collection ( "publication" ).document ("categories").collection (categories ).document (iddupost).addSnapshotListener ( this,new EventListener<DocumentSnapshot> () {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (!documentSnapshot.exists ()){
                    Toast.makeText ( DetailActivityTwo.this, getString(R.string.vente_retire), Toast.LENGTH_SHORT ).show ();

                }else {

                }
            }
        } );
        MobileAds.initialize(this,"ca-app-pub-4353172129870258~6890094527");
        mad=MobileAds.getRewardedVideoAdInstance(this);
        mad.setRewardedVideoAdListener(this);
        loadRewardedVideo();
        if (mad.isLoaded()) {
            mad.show();
        }
        detail_progress.setVisibility ( View.VISIBLE );
        voir_les_commentaire_btn=findViewById(R.id.voir_les_commentaire_btn);
        voir_les_commentaire_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               comment_init();
            }
        });

    }


    public void showPopup() {
        myDialog=new Dialog(this);
        myDialog.setContentView(R.layout.load_pop_pup);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.setCancelable(false);
        myDialog.show();
    }


    public void commentaire(){
        Query firstQuery =firebaseFirestore.collection ( "publication" ).document ("categories").collection ( categories ).document (iddupost).collection("commentaires");
        firstQuery.addSnapshotListener(DetailActivityTwo.this,new EventListener<QuerySnapshot>() {
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

    public void nomEtImageProfil() {
        firebaseFirestore.collection ( "mes donnees utilisateur" ).document ( current_user_id ).get ().addOnCompleteListener ( DetailActivityTwo.this, new OnCompleteListener<DocumentSnapshot> () {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful ()) {
                    if (task.getResult ().exists ()) {
                        prenom = task.getResult ().getString ( "user_prenom" );
                        name_user = task.getResult ().getString ( "user_name" );
                        image_user = task.getResult ().getString ( "user_profil_image" );
                        user_mail  =task.getResult ().getString ("user_mail");
                        residence_user  =task.getResult ().getString ("user_residence");
                        derniere_conection  =task.getResult ().getString ("derniere_conection");
                        // detail_user_name.setText(name_user+" "+prenom);
                        Picasso.with ( getApplicationContext () ).load ( image_user ).into ( detail_profil_image );
                        detail_profil_image.setAnimation ( AnimationUtils.loadAnimation ( getApplicationContext (), R.anim.fade_transition_animation ) );
                        //detail_user_name.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_transition_animation));

                    }
                } else {
                    String error = task.getException ().getMessage ();

                }
            }
        } );
    }

    public void loadRewardedVideo(){
        if (!mad.isLoaded()){
            // ca-app-pub-3940256099942544/5224354917
            // my pub id : ca-app-pub-4353172129870258/9670857450
            mad.loadAd("ca-app-pub-4353172129870258/9670857450",new AdRequest.Builder().build());
        }
    }




    @SuppressLint("RestrictedApi")
    public void supprime(){
        if (current_user_id.equals ( utilisateur_actuel )){
            vendeur_button.setVisibility ( INVISIBLE );
            supprime_detail_button.setEnabled ( true );
            supprime_detail_button.setVisibility ( View.VISIBLE );
            supprime_detail_button.setOnClickListener ( new View.OnClickListener () {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DetailActivityTwo.this);
                    alertDialogBuilder.setMessage(getString(R.string.voulez_vous_supprimer));
                    alertDialogBuilder.setPositiveButton("oui",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    firebaseFirestore.collection ( "publication" ).document ("categories").collection (categories).document (iddupost).get ().addOnCompleteListener ( DetailActivityTwo.this,new OnCompleteListener<DocumentSnapshot> () {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.getResult ().exists ()){
                                                if (task.isSuccessful ()){
                                                    detail_progress.setVisibility ( View.VISIBLE );
                                                    firebaseFirestore.collection ( "publication" ).document ("categories").collection ( categories ).document (iddupost).delete ();
                                                    firebaseFirestore.collection ( "publication" ).document ("categories").collection ( "nouveaux" ).document (iddupost).delete ();
                                                    firebaseFirestore.collection ( "publication" ).document ("post utilisateur").collection ( current_user_id ).document(iddupost).delete ();
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
       // mad.destroy(getApplicationContext());
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



    public void comment_init(){
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(DetailActivityTwo.this);
        View parientView = getLayoutInflater().inflate(R.layout.bottom_sheet_layout,null);
        bottomSheetDialog.setContentView(parientView);
        final BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from((View)parientView.getParent());
        //bottomSheetBehavior.setPeekHeight((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,100,getResources().getDisplayMetrics()));
        bottomSheetBehavior.setPeekHeight(410);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
        bottomSheetDialog.show();
        final CircleImageView post_detail_user_image= parientView.findViewById(R.id.post_detail_user_image);
        final ProgressBar progressBarBottom_sheet= parientView.findViewById(R.id.progressBarBottom_sheet);
        final ProgressBar progressBar3= parientView.findViewById(R.id.progressBar3);
        progressBarBottom_sheet.setVisibility(VISIBLE);
        progressBar3.setVisibility(INVISIBLE);
        firebaseFirestore.collection("mes donnees utilisateur").document(utilisateur_actuel).get().addOnCompleteListener(DetailActivityTwo.this,new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult ().exists ()){
                        String user_image= task.getResult ().getString ( "user_profil_image" );
                        Picasso.with(DetailActivityTwo.this).load(user_image).into(post_detail_user_image);
                        progressBarBottom_sheet.setVisibility(INVISIBLE);

                    }
                }else {
                    String error=task.getException().getMessage();

                }
            }
        });
        ImageView close_bottom_sheet=parientView.findViewById(R.id.close_bottom_sheet);
        final EditText post_detail_comment=parientView.findViewById(R.id.post_detail_comment);
        final Button post_detail_add_comment_btn=parientView.findViewById(R.id.post_detail_add_comment_btn);
        final TextView comment_empty_text=parientView.findViewById(R.id.comment_empty_text);
        RecyclerView rv_comment=parientView.findViewById(R.id.rv_comment);
        commentaires_modelList=new ArrayList<>();
        commentaire_adapter=new Commentaire_Adapter(commentaires_modelList,DetailActivityTwo.this);
        rv_comment.setAdapter(commentaire_adapter);
        rv_comment.setLayoutManager(new LinearLayoutManager(DetailActivityTwo.this,LinearLayoutManager.VERTICAL,false));
        firebaseFirestore.collection ( "publication" ).document ("categories").collection (categories ).document (iddupost).collection("commentaires").addSnapshotListener ( DetailActivityTwo.this,new EventListener<QuerySnapshot> () {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (queryDocumentSnapshots.isEmpty ()){
                    comment_empty_text.setVisibility(VISIBLE);
                }
            }
        } );

        close_bottom_sheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });

        post_detail_add_comment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!post_detail_comment.getText().toString().equals("")){
                    post_detail_add_comment_btn.setVisibility(INVISIBLE);
                    progressBar3.setVisibility(VISIBLE);

                    Date date=new Date();
                    SimpleDateFormat sdf= new SimpleDateFormat("d/MM/y H:mm:ss");
                    Calendar calendar=Calendar.getInstance ();
                    SimpleDateFormat currentDate=new SimpleDateFormat (" dd MMM yyyy" );
                    String saveCurrentDate=currentDate.format ( calendar.getTime () );
                    final Map<String,Object> user_comment = new HashMap();
                    comment = post_detail_comment.getText().toString();
                    user_comment.put ( "contenu",comment );
                    user_comment.put ( "heure",saveCurrentDate );
                    user_comment.put ( "id_user",utilisateur_actuel );
                    firebaseFirestore.collection ( "publication" ).document ("categories").collection (categories ).document (iddupost).collection("commentaires").add(user_comment).addOnSuccessListener(DetailActivityTwo.this, new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            String id_commentaire = documentReference.getId();
                            firebaseFirestore.collection ( "publication" ).document ("categories").collection ("nouveaux" ).document (iddupost).collection("commentaires").document(id_commentaire).set(user_comment).addOnSuccessListener(DetailActivityTwo.this,new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                }
                            });

                            firebaseFirestore.collection ( "publication" ).document ("post utilisateur").collection ( current_user_id ).document(iddupost).collection("commentaires").document(id_commentaire).set(user_comment).addOnSuccessListener(DetailActivityTwo.this, new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    post_detail_comment.setText("");
                                    comment_empty_text.setVisibility(INVISIBLE);
                                    progressBar3.setVisibility(INVISIBLE);
                                    post_detail_add_comment_btn.setVisibility(VISIBLE);
                                }
                            });
                        }
                    });
                }else{
                    Toast.makeText(getApplicationContext(),"vide",Toast.LENGTH_LONG).show();
                }
            }
        });

        commentaire();

    }


    public class AsyncTask extends android.os.AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute ();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            firebaseFirestore.collection ( "publication" ).document ("categories").collection ( categories ).document (iddupost).get().addOnCompleteListener(DetailActivityTwo.this,new OnCompleteListener<DocumentSnapshot>() {
                @SuppressLint("RestrictedApi")
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()){
                        if (task.getResult ().exists ()){
                            String titreDuProduit=task.getResult().getString("nom_du_produit");
                            titre_produit=titreDuProduit;
                            String description= task.getResult ().getString ( "decription_du_produit" );
                            String imageduproduit=task.getResult ().getString ( "image_du_produit" );
                            String prixduproduit= task.getResult ().getString ( "prix_du_produit" );
                            prix_produit=prixduproduit;
                            final String datedepublication=task.getResult ().getString ( "date_de_publication" );
                             image_user=task.getResult ().getString ( "user_profil_image" );
                            getSupportActionBar().setTitle(titreDuProduit);
                            detail_post_titre_produit.setText(titreDuProduit);
                            detail_prix_produit.setText(prixduproduit);
                            detail_description.setText(description);
                            firebaseFirestore.collection("mes donnees utilisateur").document(current_user_id).get().addOnCompleteListener(DetailActivityTwo.this,new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()){
                                        if (task.getResult ().exists ()){
                                            prenom=task.getResult ().getString ( "user_prenom" );
                                            name_user= task.getResult ().getString ( "user_name" );
                                            String image_user=task.getResult ().getString ( "user_profil_image" );
                                            date_de_publication.setText(datedepublication + "  |  " + name_user+" "+prenom);
                                            Picasso.with ( getApplicationContext () ).load ( image_user ).transform(new CircleTransform ()).into ( detail_profil_image );
                                            date_de_publication.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_transition_animation));
                                            detail_profil_image.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_transition_animation));

                                            //detail_user_name.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_transition_animation));

                                        }
                                    }else {
                                        String error=task.getException().getMessage();

                                    }
                                }
                            });
                            lien_image=imageduproduit;
                            Picasso.with(getApplicationContext()).load(image_user).into(detail_profil_image);
                            Picasso.with(getApplicationContext()).load(imageduproduit).into(detail_image_post);
                            detail_prix_produit.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_transition_animation));
                            date_de_publication.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_transition_animation));
                            detail_image_post.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_transition_animation));
                            detail_description.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_transition_animation));
                            detail_progress.setVisibility ( INVISIBLE );
                            myDialog.dismiss();

                        }

                    }else {


                    }
                }
            });
            supprime ();
            vendeurActivity();
            nomEtImageProfil();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute ( aVoid );

        }
    }

    public void vendeurActivity() {
        detail_profil_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(DetailActivityTwo.this);
                View parientView = getLayoutInflater().inflate(R.layout.activity_user_general_presentation,null);
                bottomSheetDialog.setContentView(parientView);
                final BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from((View)parientView.getParent());
                //bottomSheetBehavior.setPeekHeight((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,100,getResources().getDisplayMetrics()));
                bottomSheetBehavior.setPeekHeight(410);
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
                bottomSheetDialog.show();
                ImageView ic_bottom_sheet_up = parientView.findViewById(R.id.ic_bottom_sheet_up);
                ic_bottom_sheet_up.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
                });
                ImageView close_bottom_sheet= parientView.findViewById(R.id.close_bottom_sheet);
                close_bottom_sheet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                    }
                });
                final CircleImageView generalImageProfilUser= parientView.findViewById(R.id.generalImageProfilUser);
                final TextView general_user_name= parientView.findViewById(R.id.general_user_name);
                final TextView detail_user= parientView.findViewById(R.id.detail_user);
                final TextView general_residence= parientView.findViewById(R.id.general_residence);
                final TextView general_last_view= parientView.findViewById(R.id.general_last_view);
                Picasso.with(getApplicationContext()).load(image_user).into(generalImageProfilUser);
                general_residence.setText(residence_user);
                general_last_view.setText(derniere_conection);
                general_user_name.setText(name_user+" " + prenom);
                detail_user.setText(user_mail);
                final TextView total_vente= parientView.findViewById(R.id.total_vente);
                Button general_voir_ventes = parientView.findViewById(R.id.general_voir_ventes);
                Button general_button_message = parientView .findViewById(R.id.general_button_message);
                general_button_message.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent gotoMessage = new Intent(getApplicationContext(), MessageActivity.class);
                        gotoMessage.putExtra("id du post", iddupost);
                        gotoMessage.putExtra("id de l'utilisateur", current_user_id);
                        gotoMessage.putExtra("id_categories", categories);
                        gotoMessage.putExtra("image_en_vente", lien_image);
                        Map<String, String> donnees_utilisateur = new HashMap<>();
                        donnees_utilisateur.put("image_en_vente", lien_image);
                        donnees_utilisateur.put("titre_produit", titre_produit);
                        donnees_utilisateur.put("prix_produit", prix_produit);
                        firebaseFirestore.collection("sell_image").document(current_user_id).collection(utilisateur_actuel).document(current_user_id).set(donnees_utilisateur).addOnCompleteListener(DetailActivityTwo.this,new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        });
                        firebaseFirestore.collection("sell_image").document(utilisateur_actuel).collection(current_user_id).document(utilisateur_actuel).set(donnees_utilisateur).addOnCompleteListener(DetailActivityTwo.this,new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        });
                        DocumentReference user = firebaseFirestore.collection("sell_image").document(current_user_id).collection(utilisateur_actuel).document(current_user_id);
                        user.update("image_en_vente", lien_image)
                                .addOnSuccessListener(DetailActivityTwo.this,new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                    }
                                });
                        startActivity(gotoMessage);
                        //finish();

                    }
                });

                general_voir_ventes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent gotoDetail = new Intent(getApplicationContext(), SellActivityUser.class);
                        gotoDetail.putExtra("id du post", iddupost);
                        gotoDetail.putExtra("id de l'utilisateur", current_user_id);
                        gotoDetail.putExtra("id_categories", categories);
                        gotoDetail.putExtra ( "image_en_vente", lien_image );
                        gotoDetail.putExtra ( "prix_produit", prix_produit );
                        startActivity(gotoDetail);
                        //finish();
                    }
                });
                firebaseFirestore.collection("publication").document("post utilisateur").collection(current_user_id).addSnapshotListener(DetailActivityTwo.this,new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            int i = queryDocumentSnapshots.size();
                            total_vente.setText(i + "");

                        } else {
                            total_vente.setText(0 + "");
                        }
                    }
                });

            }
        });
        vendeur_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoMessage = new Intent(getApplicationContext(), MessageActivity.class);
                gotoMessage.putExtra("id du post", iddupost);
                gotoMessage.putExtra("id de l'utilisateur", current_user_id);
                gotoMessage.putExtra("id_categories", categories);
                gotoMessage.putExtra("image_en_vente", lien_image);
                Map<String, String> donnees_utilisateur = new HashMap<>();
                donnees_utilisateur.put("image_en_vente", lien_image);
                donnees_utilisateur.put("titre_produit", titre_produit);
                donnees_utilisateur.put("prix_produit", prix_produit);
                firebaseFirestore.collection("sell_image").document(current_user_id).collection(utilisateur_actuel).document(current_user_id).set(donnees_utilisateur).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
                firebaseFirestore.collection("sell_image").document(utilisateur_actuel).collection(current_user_id).document(utilisateur_actuel).set(donnees_utilisateur).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
                DocumentReference user = firebaseFirestore.collection("sell_image").document(current_user_id).collection(utilisateur_actuel).document(current_user_id);
                user.update("image_en_vente", lien_image)
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
                startActivity(gotoMessage);
               /* Intent vendeur = new Intent ( getApplicationContext (), MessageActivity.class );
                vendeur.putExtra ( "id du post", iddupost );
                vendeur.putExtra ( "id de l'utilisateur", current_user_id );
                vendeur.putExtra ( "image_en_vente", lien_image );
                vendeur.putExtra ( "titre_produit", titre_produit );
                vendeur.putExtra ( "prix_produit", prix_produit );
                startActivity ( vendeur );*/
            }
        });

        //finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
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
                    }});
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
        detail_user_name=null;
        detail_prix_produit=null;
        detail_description=null;
        date_de_publication=null;
        firebaseAuth=null;
        current_user_id=null;
        categories=null;

    }

    public class CircleTransform implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min ( source.getWidth (), source.getHeight () );

            int x = (source.getWidth () - size) / 2;
            int y = (source.getHeight () - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap ( source, x, y, size, size );
            if (squaredBitmap != source) {
                source.recycle ();
            }

            Bitmap bitmap = Bitmap.createBitmap ( size, size, source.getConfig () );

            Canvas canvas = new Canvas ( bitmap );
            Paint paint = new Paint ();
            BitmapShader shader = new BitmapShader ( squaredBitmap,
                    BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP );
            paint.setShader ( shader );
            paint.setAntiAlias ( true );

            float r = size / 2f;
            canvas.drawCircle ( r, r, r, paint );

            squaredBitmap.recycle ();
            return bitmap;
        }

        @Override
        public String key() {
            return "circle";
        }


    }
}

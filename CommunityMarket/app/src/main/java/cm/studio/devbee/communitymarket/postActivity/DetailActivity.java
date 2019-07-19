package cm.studio.devbee.communitymarket.postActivity;

import android.Manifest;
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
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
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
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

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
import cm.studio.devbee.communitymarket.MySingleton;
import cm.studio.devbee.communitymarket.R;
import cm.studio.devbee.communitymarket.commentaires.Commentaire_Adapter;
import cm.studio.devbee.communitymarket.commentaires.Commentaires_Model;
import cm.studio.devbee.communitymarket.messagerie.MessageActivity;
import cm.studio.devbee.communitymarket.profile.ParametrePorfilActivity;
import cm.studio.devbee.communitymarket.profile.ProfileActivity;
import de.hdodenhof.circleimageview.CircleImageView;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static cm.studio.devbee.communitymarket.messagerie.MessageActivity.user_id_message;

public class DetailActivity extends AppCompatActivity implements RewardedVideoAdListener {
    private static String iddupost;
    private static FirebaseFirestore firebaseFirestore;
    private static ImageView detail_image_post;
    // private static TextView detail_post_titre_produit;
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
    private static String lien_image;
    private static Toolbar toolbarDetail;
    private RewardedVideoAd mad;
    private Dialog myDialog;
    private boolean suppBool=false;
    private boolean venteBool=false;
    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key="+"AAAAfR8BveM:APA91bEgCOmnLz5LKrc4ms8qvCBYqAUwbXpoMswSYyuQJT0cg3FpLSvH-S_nAiaCARSdeolPbGpxTX5nHVm5AP6tI7N9sCYEL4IUkR_eF4lYZXN4oeXhWtCKavTHIaA8pH6eklL4yBO5";
    final private String contentType = "application/json";
    final String TAG = "NOTIFICATION TAG";
    String categories;
    String prenom;
    String name_user;
    CircleImageView post_detail_currentuser_img;
    String comment;
    List<Commentaires_Model> commentaires_modelList;
    Commentaire_Adapter commentaire_adapter;
    TextView comment_empty_text;
    android.support.v7.widget.Toolbar detail_image_post_toolbar;
    ProgressBar add_progressbar;
    String titre_produit;
    String prix_produit;
    private static FloatingActionButton voir_les_commentaire_btn;
    private String image_user;
    private String user_mail;
    private String residence_user;
    private String derniere_conection;
    private String titreDuProduit;
    private String prixduproduit;
    private String description;
    private String imageduproduit;
    private String datedepublication;
    private Menu menu;
    private boolean is_exist=false;
    private static  TextView detail_titre_vente;
    private String is_master;
    private Button is_master_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_test );
        detail_image_post_toolbar = findViewById ( R.id.detail_image_post_toolbar );
        setSupportActionBar ( detail_image_post_toolbar );
       // toolbar_layout=findViewById ( R.id.toolbar_layout );
        getSupportActionBar ().setDisplayHomeAsUpEnabled ( true );
        detail_image_post_toolbar.setNavigationOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                //startActivity ( new Intent ( getApplicationContext (),Accueil.class ).setFlags ( Intent.FLAG_ACTIVITY_CLEAR_TOP ) );
                finish ();
            }
        } );
        showPopup();
        detail_titre_vente=findViewById ( R.id.detail_titre_vente );
        firebaseAuth = FirebaseAuth.getInstance ();
        utilisateur_actuel = firebaseAuth.getCurrentUser ().getUid ();
        firebaseFirestore = FirebaseFirestore.getInstance ();
        iddupost = getIntent ().getExtras ().getString ( "id du post" );
        current_user_id = getIntent ().getExtras ().getString ( "id de l'utilisateur" );
        categories = getIntent ().getExtras ().getString ( "id_categories" );
        detail_image_post = findViewById ( R.id.detail_image_post );
        //detail_post_titre_produit=findViewById(R.id.detail_prix_produit );
        detail_prix_produit = findViewById ( R.id.detail_prix_produit );
        detail_profil_image = findViewById ( R.id.detail_image_du_profil );
        vendeur_button = findViewById ( R.id.vendeur_button );
        post_detail_currentuser_img = findViewById ( R.id.post_detail_user_image);
        is_master_button=findViewById(R.id.is_master_button);
        voir_les_commentaire_btn=findViewById(R.id.voir_les_commentaire_btn);
        //detail_user_name=findViewById(R.id.detail_user_name);
        detail_description = findViewById ( R.id.detail_description );
        date_de_publication = findViewById ( R.id.date_de_publication );
        firebaseAuth = FirebaseAuth.getInstance ();
        detail_progress = findViewById ( R.id.detail_progress );
        supprime_detail_button = findViewById ( R.id.supprime_detail_button );
        vendeur_button.setEnabled ( true );
        asyncTask = new AsyncTask ();
        asyncTask.execute ();
        add_progressbar=findViewById(R.id.add_progressbar);
        add_progressbar.setVisibility ( INVISIBLE );
        comment_empty_text = findViewById ( R.id.comment_empty_text );
        firebaseFirestore.collection ( "publication" ).document ( "categories" ).collection ( "nouveaux" ).document ( iddupost ).addSnapshotListener ( this, new EventListener<DocumentSnapshot> () {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (!documentSnapshot.exists ()) {
                    Toast.makeText ( DetailActivity.this, getString ( R.string.vente_retire ), Toast.LENGTH_SHORT ).show ();
                    myDialog.dismiss ();
                    finish ();
                } else {

                }
            }
        } );
        ///////ads"ca-app-pub-3940256099942544~3347511713
        ////my id : ca-app-pub-4353172129870258~6890094527
        MobileAds.initialize ( this, "ca-app-pub-4353172129870258~6890094527" );
        mad = MobileAds.getRewardedVideoAdInstance ( this );
        mad.setRewardedVideoAdListener ( this );
        loadRewardedVideo ();
        if (mad.isLoaded ()) {
            mad.show ();
        }
        donne ();
        detail_progress.setVisibility ( View.VISIBLE );
        voir_les_commentaire_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment_init();
            }
        });
        showcase_supp_button ();
        showcase_vente_button ();

    }
    public void showcase_supp_button(){
        if (suppBool==true){
            ShowcaseConfig config = new ShowcaseConfig();
            //config.setDelay(500); // half second between each showcase view
            MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(DetailActivity.this, String.valueOf(10));
            sequence.setConfig(config);
            sequence.addSequenceItem(detail_profil_image, "cliquez sur l'image de profil pour en savoir plus sur un vendeurs si le produit n'est pas poste par vous. \" ok \" pour continuer", "ok");
            sequence.addSequenceItem(voir_les_commentaire_btn, "laisser ou voir les commentaires. \" ok \" pour continuer\"", "ok");
            sequence.addSequenceItem(detail_prix_produit, "ici apparait le prix.\" ok \" pour continuer\"", "ok");
            sequence.addSequenceItem(supprime_detail_button, "cliquez ici pour supprimmer un votre article de la vente.\" ok \" pour continuer\"", "ok");
            sequence.addSequenceItem(date_de_publication, "la date et le non du vendeur juste en bas ,c'est tout,fait de bons deal :).\" ok \" pour continuer\"", "ok");
            sequence.start();

        }

    }

    public void showcase_vente_button(){
        if (venteBool==true){
            new MaterialShowcaseView.Builder(this)
                    .setTarget(vendeur_button)
                    .setDismissText("ok")
                    .setContentText("cliquez ici ecrire au vendeur.\" ok \" pour continuer")
                    .singleUse(String.valueOf(90)) // provide a unique ID used to ensure it is only shown once
                    .show();
        }

    }

    public void comment_init(){
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(DetailActivity.this);
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
        firebaseFirestore.collection("mes donnees utilisateur").document(utilisateur_actuel).get().addOnCompleteListener(DetailActivity.this,new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult ().exists ()){
                        String user_image= task.getResult ().getString ( "user_profil_image" );
                        Picasso.with(DetailActivity.this).load(user_image).into(post_detail_user_image);
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
        commentaire_adapter=new Commentaire_Adapter(commentaires_modelList,DetailActivity.this);
        rv_comment.setAdapter(commentaire_adapter);
        rv_comment.setLayoutManager(new LinearLayoutManager(DetailActivity.this,LinearLayoutManager.VERTICAL,false));
        firebaseFirestore.collection ( "publication" ).document ("categories").collection (categories ).document (iddupost).collection("commentaires").addSnapshotListener ( DetailActivity.this,new EventListener<QuerySnapshot> () {
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
                    ////////
                    Date date=new Date();
                    Calendar calendarOne=Calendar.getInstance ();
                    SimpleDateFormat currentDateOne=new SimpleDateFormat (" dd MMM yyyy hh:mm" );
                    String saveCurrentDateOne=currentDateOne.format ( calendarOne.getTime () );
                    String randomKey=saveCurrentDateOne;
                    final Map <String,Object> notification_map = new HashMap ();
                    notification_map.put ( "nom_du_produit",titreDuProduit );
                    notification_map.put ( "decription_du_produit",description );
                    notification_map.put ( "prix_du_produit",prix_produit );
                    notification_map.put ( "date_du_like",randomKey );
                    notification_map.put ( "image_du_produit",lien_image);
                    notification_map.put("categories",categories);
                    notification_map.put("id_de_utilisateur",utilisateur_actuel);
                    notification_map.put("id_du_post",iddupost);
                    notification_map.put("post_id",iddupost);
                    notification_map.put("action","commantaire");
                    notification_map.put("commantaire",post_detail_comment.getText().toString());
                    notification_map.put("is_new_notification","true");

                    /////
                    firebaseFirestore.collection("mes donnees utilisateur").document(current_user_id).get().addOnCompleteListener(DetailActivity.this,new OnCompleteListener<DocumentSnapshot> () {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()){
                                if (task.getResult ().exists ()){
                                    String prenom=task.getResult ().getString ( "user_prenom" );
                                    String name_user= task.getResult ().getString ( "user_name" );
                                    String image_user=task.getResult ().getString ( "user_profil_image" );
                                        if (!current_user_id.equals ( utilisateur_actuel )){
                                            /////////////
                                            String TOPIC = "/topics/"+user_id_message; //topic has to match what the receiver subscribed to
                                            JSONObject notification = new JSONObject();
                                            JSONObject notifcationBody = new JSONObject ();
                                            try {
                                                notifcationBody.put("title", "nouvelle reaction");
                                                notifcationBody.put("message",name_user +" "+prenom +" reagi sur votre post") ;
                                                notifcationBody.put("id", user_id_message);
                                                notifcationBody.put ( "viens_de_detail","faux" );
                                                notifcationBody.put ( "id_recepteur",user_id_message );
                                                notifcationBody.put ( "image_en_vente",lien_image );
                                                notification.put("to", TOPIC);
                                                notification.put("categories_name", categories);
                                                notification.put("data", notifcationBody);
                                                notifcationBody.put ( "viens_de_detail","vrai" );

                                            } catch (JSONException e) {
                                                Log.e(TAG, "onCreate: " + e.getMessage() );
                                            }
                                            //sendNotification(notification);
                                            /////////////
                                        }


                                    ////end test noti

                                    //////////////////////////////

                                    if (!utilisateur_actuel.equals ( current_user_id )){
                                        firebaseFirestore.collection ( "mes donnees utilisateur" ).document (current_user_id).collection ( "mes notification" ).document(iddupost).set(notification_map).addOnSuccessListener(DetailActivity.this,new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void documentReference) {

                                            }

                                        }).addOnFailureListener ( DetailActivity.this, new OnFailureListener () {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                            }
                                        } );

                                        DocumentReference user = firebaseFirestore.collection("mes donnees utilisateur" ).document(current_user_id);
                                        user.update("has_notification", "true")
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

                                    ///////////////////////////
                                }
                            }else {
                                String error=task.getException().getMessage();
                                Toast.makeText ( getApplicationContext (), error, Toast.LENGTH_LONG ).show ();

                            }
                        }
                    });



                    post_detail_add_comment_btn.setVisibility(INVISIBLE);
                    progressBar3.setVisibility(VISIBLE);
                    SimpleDateFormat sdf= new SimpleDateFormat("d/MM/y H:mm:ss");
                    Calendar calendar=Calendar.getInstance ();
                    SimpleDateFormat currentDate=new SimpleDateFormat (" dd MMM yyyy H:mm" );
                    String saveCurrentDate=currentDate.format ( calendar.getTime () );
                    final Map<String,Object> user_comment = new HashMap();
                    comment = post_detail_comment.getText().toString();
                    user_comment.put ( "contenu",comment );
                    user_comment.put ( "heure",saveCurrentDate );
                    user_comment.put ( "id_user",utilisateur_actuel );
                    firebaseFirestore.collection ( "publication" ).document ("categories").collection (categories ).document (iddupost).collection("commentaires").add(user_comment).addOnSuccessListener(DetailActivity.this, new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            String id_commentaire = documentReference.getId();
                            firebaseFirestore.collection ( "publication" ).document ("categories").collection ("nouveaux" ).document (iddupost).collection("commentaires").document(id_commentaire).set(user_comment).addOnSuccessListener(DetailActivity.this,new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                }
                            });

                            firebaseFirestore.collection ( "publication" ).document ("post utilisateur").collection ( current_user_id ).document(iddupost).collection("commentaires").document(id_commentaire).set(user_comment).addOnSuccessListener(DetailActivity.this, new OnSuccessListener<Void>() {
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

    private void sendNotification(final JSONObject notification) {
        firebaseFirestore.collection("mes donnees utilisateur" ).document(current_user_id).get ().addOnCompleteListener ( DetailActivity.this, new OnCompleteListener<DocumentSnapshot> () {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful ()){
                    if (task.getResult ().exists ()){
                        String status=task.getResult ().getString ( "status" );
                        if (!status.equals ( "online" )){
                            ///////////////////////////
                            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest (FCM_API, notification,
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            Log.i(TAG, "onResponse: " + response.toString());
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Toast.makeText(DetailActivity.this, "Request error", Toast.LENGTH_LONG).show();
                                            Log.i(TAG, "onErrorResponse: Didn't work");
                                        }
                                    }){
                                @Override
                                public Map<String, String> getHeaders() throws AuthFailureError {
                                    Map<String, String> params = new HashMap<>();
                                    params.put("Authorization", serverKey);
                                    params.put("Content-Type", contentType);
                                    return params;
                                }
                            };
                            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
                            /////////////////////////////////
                        }
                    }
                }
            }
        } );


    }


    ////envoi du commentaire
    public void commentaire() {
        Query firstQuery = firebaseFirestore.collection ( "publication" ).document ( "categories" ).collection ( "nouveaux" ).document ( iddupost ).collection ( "commentaires" ).orderBy ( "heure",Query.Direction.ASCENDING );
        // .limit(3);
        firstQuery.addSnapshotListener ( DetailActivity.this, new EventListener<QuerySnapshot> () {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges ()) {
                    if (doc.getType () == DocumentChange.Type.ADDED) {
                        Commentaires_Model principalAdaptemodel = doc.getDocument ().toObject ( Commentaires_Model.class );
                        commentaires_modelList.add ( principalAdaptemodel );
                        commentaire_adapter.notifyDataSetChanged ();
                    }
                }

            }
        } );
    }

    ///ads
    public void loadRewardedVideo() {
        if (!mad.isLoaded ()) {
            // ca-app-pub-3940256099942544/5224354917
            // my pub id : ca-app-pub-4353172129870258/9670857450
            mad.loadAd ( "ca-app-pub-4353172129870258/9670857450", new AdRequest.Builder ().build () );
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed ();
        /*Intent gotohome=new Intent(getApplicationContext(),Accueil.class);
        startActivity(gotohome);*/
        finish ();
    }

    public void showPopup() {
        myDialog=new Dialog(this);
        myDialog.setContentView(R.layout.load_pop_pup);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.setCancelable(false);
        myDialog.show();
    }

    public void nomEtImageProfil() {
        firebaseFirestore.collection ( "mes donnees utilisateur" ).document ( current_user_id ).get ().addOnCompleteListener ( DetailActivity.this, new OnCompleteListener<DocumentSnapshot> () {
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

    public void donne() {
        firebaseFirestore.collection ( "mes donnees utilisateur" ).document ( utilisateur_actuel ).get ().addOnCompleteListener ( DetailActivity.this, new OnCompleteListener<DocumentSnapshot> () {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful ()) {
                    if (task.getResult ().exists ()) {
                        String image_profil_user = task.getResult ().getString ( "user_profil_image" );
                        Picasso.with ( getApplicationContext () ).load ( image_profil_user ).into ( post_detail_currentuser_img );

                    }
                } else {

                }
            }
        } );
    }

    @SuppressLint("RestrictedApi")
    public void supprime() {
        firebaseFirestore.collection ( "mes donnees utilisateur" ).document (utilisateur_actuel).get ().addOnCompleteListener ( DetailActivity.this,new OnCompleteListener<DocumentSnapshot>() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful ()){
                    if (task.getResult ().exists ()){
                        is_master= task.getResult ().getString ( "is_master" );
                        if (is_master.equals("true")){
                            is_master_button.setVisibility(VISIBLE);
                            is_master_button.setOnClickListener ( new View.OnClickListener () {
                                @Override
                                public void onClick(View v) {
                                   AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder ( DetailActivity.this );
                                    alertDialogBuilder.setMessage ( getString ( R.string.voulez_vous_supprimer ) );
                                    alertDialogBuilder.setPositiveButton ( "oui",
                                            new DialogInterface.OnClickListener () {
                                                @Override
                                                public void onClick(DialogInterface arg0, int arg1) {
                                                    showPopup();
                                                    firebaseFirestore.collection ( "publication" ).document ( "categories" ).collection ( categories ).document ( iddupost ).delete ();
                                                    firebaseFirestore.collection ( "publication" ).document ( "categories" ).collection ( "nouveaux" ).document ( iddupost ).delete ();
                                                    firebaseFirestore.collection ( "publication" ).document ( "post utilisateur" ).collection ( current_user_id ).document ( iddupost ).delete ();
                                                    firebaseFirestore.collection ( "mes donnees utilisateur" ).document (current_user_id).collection ( "mes notification" ).document ( iddupost ).delete ();                                                   myDialog.dismiss();

                                                }
                                            } );
                                    alertDialogBuilder.setNegativeButton ( "non", new DialogInterface.OnClickListener () {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel ();

                                        }
                                    } );
                                    AlertDialog alertDialog = alertDialogBuilder.create ();
                                    alertDialog.show ();
                                }
                            } );
                        }
                    }else {
                    }
                }else{
                }
            }
        } );


        if (current_user_id.equals ( utilisateur_actuel )) {
            //supprime_detail_button.setText ( "supprimer de cette categories ?");
            vendeur_button.setVisibility ( INVISIBLE );
            supprime_detail_button.setEnabled ( true );
            suppBool=true;
            supprime_detail_button.setVisibility ( View.VISIBLE );
            supprime_detail_button.setOnClickListener ( new View.OnClickListener () {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder ( DetailActivity.this );
                    alertDialogBuilder.setMessage ( getString ( R.string.voulez_vous_supprimer ) );
                    alertDialogBuilder.setPositiveButton ( "oui",
                            new DialogInterface.OnClickListener () {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    showPopup();
                                    firebaseFirestore.collection ( "mes donnees utilisateur" ).document (utilisateur_actuel).collection ( "mes notification" ).document ( iddupost ).get ().addOnCompleteListener ( DetailActivity.this, new OnCompleteListener<DocumentSnapshot> () {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful ()){
                                                if (task.getResult ().exists ()){
                                                    firebaseFirestore.collection ( "mes donnees utilisateur" ).document (utilisateur_actuel).collection ( "mes favories" ).document ( iddupost ).delete ();
                                                }
                                            }
                                        }
                                    } );
                                    firebaseFirestore.collection ( "publication" ).document ( "categories" ).collection ( "nouveaux" ).document ( iddupost ).get ().addOnCompleteListener ( DetailActivity.this, new OnCompleteListener<DocumentSnapshot> () {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.getResult ().exists ()) {
                                                if (task.isSuccessful ()) {
                                                    detail_progress.setVisibility ( View.VISIBLE );
                                                    firebaseFirestore.collection ( "publication" ).document ( "categories" ).collection ( categories ).document ( iddupost ).delete ();
                                                    firebaseFirestore.collection ( "publication" ).document ( "categories" ).collection ( "nouveaux" ).document ( iddupost ).delete ();
                                                    firebaseFirestore.collection ( "publication" ).document ( "post utilisateur" ).collection ( current_user_id ).document ( iddupost ).delete ();
                                                    firebaseFirestore.collection ( "mes donnees utilisateur" ).document (current_user_id).collection ( "mes notification" ).document ( iddupost ).delete ();                                                   myDialog.dismiss();
                                                    finish ();

                                                } else {
                                                    detail_progress.setVisibility ( INVISIBLE );
                                                    String error = task.getException ().getMessage ();
                                                    Toast.makeText ( getApplicationContext (), error, Toast.LENGTH_LONG ).show ();

                                                }
                                            } else {

                                            }
                                        }
                                    } );
                                }
                            } );

                    alertDialogBuilder.setNegativeButton ( "non", new DialogInterface.OnClickListener () {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel ();

                        }
                    } );

                    AlertDialog alertDialog = alertDialogBuilder.create ();
                    alertDialog.show ();

                }
            } );
        } else {
            vendeur_button.setVisibility ( View.VISIBLE );
            venteBool=true;
            supprime_detail_button.setVisibility ( INVISIBLE );
        }


    }

    @Override
    public void onRewardedVideoAdLoaded() {
       // Toast.makeText ( getApplicationContext (), getString ( R.string.wait ), Toast.LENGTH_LONG ).show ();
    }

    @Override
    public void onRewardedVideoAdOpened() {
      //  Toast.makeText ( getApplicationContext (), getString ( R.string.see_video ), Toast.LENGTH_LONG ).show ();
    }

    @Override
    public void onRewardedVideoStarted() {
       // Toast.makeText ( getApplicationContext (), getString ( R.string.see_video ), Toast.LENGTH_LONG ).show ();
    }

    @Override
    public void onRewardedVideoAdClosed() {
        //Toast.makeText ( getApplicationContext (), getString ( R.string.see_video ), Toast.LENGTH_LONG ).show ();
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        //  mad.destroy(getApplicationContext());
        //Toast.makeText ( getApplicationContext (), getString ( R.string.video_seen_thank ), Toast.LENGTH_LONG ).show ();
        //Toast.makeText(getApplicationContext(),getString(R.string.wait),Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {
       // Toast.makeText ( getApplicationContext (), "si une video publicitaire ce charge regarder la pour soutenir lappli svp.", Toast.LENGTH_LONG ).show ();
    }

    @Override
    public void onRewardedVideoCompleted() {
        //Toast.makeText ( getApplicationContext (), getString ( R.string.video_seen_thank ), Toast.LENGTH_LONG ).show ();
    }


    public class AsyncTask extends android.os.AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute ();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            firebaseFirestore.collection ( "publication" ).document ( "categories" ).collection ( "nouveaux" ).document ( iddupost ).get ().addOnCompleteListener ( DetailActivity.this, new OnCompleteListener<DocumentSnapshot> () {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful ()) {
                        if (task.getResult ().exists ()) {
                            String postLike = task.getResult ().getString ( "user_name" );
                            String image_user = task.getResult ().getString ( "user_profil_image" );
                             titreDuProduit = task.getResult ().getString ( "nom_du_produit" );
                            titre_produit = titreDuProduit;
                             description = task.getResult ().getString ( "decription_du_produit" );
                             imageduproduit = task.getResult ().getString ( "image_du_produit" );
                             prixduproduit = task.getResult ().getString ( "prix_du_produit" );
                            prix_produit = prixduproduit;
                            detail_titre_vente.setText ( titreDuProduit );
                            getSupportActionBar ().setTitle ( titreDuProduit );
                            datedepublication = task.getResult ().getString ( "date_de_publication" );
                            detail_image_post_toolbar.setTitle ( titreDuProduit );
                            // detail_post_titre_produit.setText(titreDuProduit);
                            detail_prix_produit.setText ( prixduproduit );
                            firebaseFirestore.collection ( "mes donnees utilisateur" ).document ( current_user_id ).get ().addOnCompleteListener ( DetailActivity.this, new OnCompleteListener<DocumentSnapshot> () {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful ()) {
                                        if (task.getResult ().exists ()) {
                                            prenom = task.getResult ().getString ( "user_prenom" );
                                            name_user = task.getResult ().getString ( "user_name" );
                                            String image_user = task.getResult ().getString ( "user_profil_image" );
                                            date_de_publication.setText ( datedepublication + "  |  " + name_user + " " + prenom );
                                            Picasso.with ( getApplicationContext () ).load ( image_user ).transform(new CircleTransform ()).into ( detail_profil_image );
                                            detail_profil_image.setAnimation ( AnimationUtils.loadAnimation ( getApplicationContext (), R.anim.fade_transition_animation ) );
                                            date_de_publication.setAnimation ( AnimationUtils.loadAnimation ( getApplicationContext (), R.anim.fade_transition_animation ) );
                                            //detail_user_name.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_transition_animation));

                                        }
                                    } else {
                                        String error = task.getException ().getMessage ();

                                    }
                                }
                            } );

                            lien_image = imageduproduit;
//                            getSupportActionBar().setTitle(titreDuProduit);
                            Picasso.with ( getApplicationContext () ).load ( imageduproduit ).into ( detail_image_post );
                            //detail_post_titre_produit.setEnabled ( true );
                            detail_prix_produit.setAnimation ( AnimationUtils.loadAnimation ( getApplicationContext (), R.anim.fade_transition_animation ) );
                            date_de_publication.setAnimation ( AnimationUtils.loadAnimation ( getApplicationContext (), R.anim.fade_transition_animation ) );
                            detail_image_post.setAnimation ( AnimationUtils.loadAnimation ( getApplicationContext (), R.anim.fade_transition_animation ) );
                            detail_description.setAnimation ( AnimationUtils.loadAnimation ( getApplicationContext (), R.anim.fade_transition_animation ) );
                            detail_description.setText ( description );
                            detail_progress.setVisibility ( INVISIBLE );
                            myDialog.dismiss();

                        }
                    } else {
                        String error = task.getException ().getMessage ();

                    }
                }
            } );
            vendeurActivity ();
            nomEtImageProfil ();
            supprime ();
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
                        if (current_user_id.equals ( utilisateur_actuel )){
                            Intent gotoProfil= new Intent ( getApplicationContext (),ProfileActivity.class );
                            startActivity ( gotoProfil );
                        }else {
                            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(DetailActivity.this);
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
                                    gotoMessage.putExtra ( "viens_de_detail","vrai" );
                                    Map<String, String> donnees_utilisateur = new HashMap<>();
                                    donnees_utilisateur.put("image_en_vente", lien_image);
                                    donnees_utilisateur.put("titre_produit", titre_produit);
                                    donnees_utilisateur.put("prix_produit", prix_produit);
                                    firebaseFirestore.collection("sell_image").document(current_user_id).collection(utilisateur_actuel).document(current_user_id).set(donnees_utilisateur).addOnCompleteListener(DetailActivity.this,new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                        }
                                    });
                                    firebaseFirestore.collection("sell_image").document(utilisateur_actuel).collection(current_user_id).document(utilisateur_actuel).set(donnees_utilisateur).addOnCompleteListener(DetailActivity.this,new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                        }
                                    });
                                    DocumentReference user = firebaseFirestore.collection("sell_image").document(current_user_id).collection(utilisateur_actuel).document(current_user_id);
                                    user.update("image_en_vente", lien_image)
                                            .addOnSuccessListener(DetailActivity.this,new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                }
                                            })
                                            .addOnFailureListener(DetailActivity.this,new OnFailureListener() {
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
                            firebaseFirestore.collection("publication").document("post utilisateur").collection(current_user_id).addSnapshotListener(DetailActivity.this,new EventListener<QuerySnapshot>() {
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
                gotoMessage.putExtra ( "id_recepteur",current_user_id );
                gotoMessage.putExtra ( "viens_de_detail","vrai" );
                gotoMessage.putExtra ( "viens_de_notification","faux" );
                gotoMessage.putExtra ( "contenu","" );
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
                        .addOnSuccessListener(DetailActivity.this,new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                            }
                        })
                        .addOnFailureListener(DetailActivity.this,new OnFailureListener() {
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

    public void userstatus(String status) {
        DocumentReference user = firebaseFirestore.collection ( "mes donnees utilisateur" ).document ( utilisateur_actuel );
        user.update ( "status", status )
                .addOnSuccessListener ( DetailActivity.this,new OnSuccessListener<Void> () {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                } )
                .addOnFailureListener (DetailActivity.this, new OnFailureListener () {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                } );
    }
    ////menu
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater ().inflate ( R.menu.detail_menu, menu );
        this.menu = menu;
        firebaseFirestore.collection ( "mes donnees utilisateur" ).document (utilisateur_actuel).collection ( "mes favories" ).document(iddupost).get ().addOnCompleteListener ( DetailActivity.this,new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful ()){
                    if (!task.getResult ().exists ()){
                        menu.getItem(0).setIcon(ContextCompat.getDrawable(getApplicationContext (), R.drawable.ic_like));
                    }else{
                        is_exist=true;
                        menu.getItem(0).setIcon(ContextCompat.getDrawable(getApplicationContext (), R.mipmap.ic_like_accent));
                    }

                }else{

                }
            }
        } );
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId ();
        if (id == R.id.ic_like) {
            if (!utilisateur_actuel.equals ( current_user_id )){
                ////////
                Date date=new Date();
                SimpleDateFormat sdf= new SimpleDateFormat("d/MM/y H:mm:ss");
                final String date_avec_seconde=sdf.format(date);
                Calendar calendar=Calendar.getInstance ();
                SimpleDateFormat currentDate=new SimpleDateFormat (" dd MMM yyyy hh:mm" );
                String saveCurrentDate=currentDate.format ( calendar.getTime () );
                String randomKey=saveCurrentDate;
                final Map <String,Object> notification_map = new HashMap ();
                notification_map.put ( "nom_du_produit",titreDuProduit );
                notification_map.put ( "decription_du_produit",description );
                notification_map.put ( "prix_du_produit",prix_produit );
                notification_map.put ( "date_du_like",randomKey );
                notification_map.put ( "image_du_produit",lien_image);
                notification_map.put("categories",categories);
                notification_map.put("id_de_utilisateur",utilisateur_actuel);
                notification_map.put("id_du_post",iddupost);
                notification_map.put("post_id",iddupost);
                notification_map.put("action","a liker");
                notification_map.put("commantaire","");
                notification_map.put("is_new_notification","true");
                /////

                final Map <String,Object> user_post = new HashMap ();
                user_post.put ( "nom_du_produit",titreDuProduit );
                user_post.put ( "decription_du_produit",description );
                user_post.put ( "prix_du_produit",prix_produit );
                user_post.put ( "date_de_publication",datedepublication );
                user_post.put ( "utilisateur",current_user_id );
                user_post.put ( "image_du_produit",lien_image);
                user_post.put ( "dete-en-seconde",datedepublication );
                user_post.put("search",description);
                user_post.put("categories",categories);
                user_post.put("a_liker",utilisateur_actuel);
                user_post.put("id_du_favorie",iddupost);
                user_post.put("post_id",iddupost);
                if (is_exist==false){
                    firebaseFirestore.collection ( "mes donnees utilisateur" ).document (utilisateur_actuel).collection ( "mes favories" ).document(iddupost).set(user_post).addOnSuccessListener(DetailActivity.this,new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void documentReference) {
                            is_exist=true;
                            menu.getItem(0).setIcon(ContextCompat.getDrawable(getApplicationContext (), R.mipmap.ic_like_accent));
                            Toast.makeText ( getApplicationContext (),"ajouter a vos favories",Toast.LENGTH_LONG ).show ();
                            //////////////////////////////
                            firebaseFirestore.collection ( "mes donnees utilisateur" ).document (current_user_id).collection ( "mes notification" ).document(iddupost).set(notification_map).addOnSuccessListener(DetailActivity.this,new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void documentReference) {

                                }

                            }).addOnFailureListener ( DetailActivity.this, new OnFailureListener () {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                }
                            } );

                            DocumentReference user = firebaseFirestore.collection("mes donnees utilisateur" ).document(current_user_id);
                            user.update("has_notification", "true")
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
                            ///////////////////////////
                        }

                    }).addOnFailureListener ( DetailActivity.this, new OnFailureListener () {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText ( getApplicationContext (),"error , try later please",Toast.LENGTH_LONG ).show ();
                        }
                    } );

                }else {
                    is_exist=false;
                    menu.getItem(0).setIcon(ContextCompat.getDrawable(getApplicationContext (), R.drawable.ic_like));
                    firebaseFirestore.collection ( "mes donnees utilisateur" ).document (utilisateur_actuel).collection ( "mes favories" ).document(iddupost).delete ();
                }

            }else {
                Toast.makeText ( getApplicationContext (),"vous ne pouvez pas ajouter votre propre article aux favories",Toast.LENGTH_LONG ).show ();
            }

            return true;
        }else if (id==R.id.ic_call){
            firebaseFirestore.collection ( "mes donnees utilisateur" ).document (current_user_id).get ().addOnCompleteListener ( DetailActivity.this,new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful ()){
                        if (task.getResult ().exists ()){
                            String telephone_user =task.getResult ().getString ("user_telephone");
                            String uri = "tel:"+telephone_user ;
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse(uri));
                            startActivity(intent);
                        }
                    }else{

                    }
                }
            } );
            return true;
        }

        return super.onOptionsItemSelected ( item );
    }
    ////menu


    @Override
    public void onResume() {
        super.onResume ();
        userstatus ( "online" );
    }

    @Override
    public void onPause() {
        super.onPause ();
        userstatus ( "offline" );

    }


    @Override
    protected void onDestroy() {
        asyncTask.cancel ( true );
        super.onDestroy ();
        asyncTask.cancel ( true );
        iddupost = null;
        firebaseFirestore = null;
        detail_image_post = null;
        //detail_post_titre_produit=null;
        detail_profil_image = null;
        vendeur_button = null;
        detail_prix_produit = null;
        detail_description = null;
        date_de_publication = null;
        firebaseAuth = null;
        current_user_id = null;
        utilisateur_actuel = null;
        supprime_detail_button = null;
        detail_progress = null;

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

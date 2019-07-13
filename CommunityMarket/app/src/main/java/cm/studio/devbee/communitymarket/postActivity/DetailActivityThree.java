package cm.studio.devbee.communitymarket.postActivity;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
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

import cm.studio.devbee.communitymarket.MySingleton;
import cm.studio.devbee.communitymarket.R;
import cm.studio.devbee.communitymarket.commentaires.Commentaire_Adapter;
import cm.studio.devbee.communitymarket.commentaires.Commentaires_Model;
import cm.studio.devbee.communitymarket.messagerie.MessageActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static cm.studio.devbee.communitymarket.messagerie.MessageActivity.user_id_message;

public class DetailActivityThree extends AppCompatActivity {
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
    private  static Toolbar toolbarDetail;
    private  static String lien_image;
    private  static String current_user;
    private static  String  categories;
    FloatingActionButton voir_les_commentaire_btn;
    private static List<Commentaires_Model> commentaires_modelList;
    private static Commentaire_Adapter commentaire_adapter;
    String prenom;
    String name_user;
    private Dialog myDialog;
    private static String titre_produit;
    private static String prix_produit;
    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key="+"AAAAfR8BveM:APA91bEgCOmnLz5LKrc4ms8qvCBYqAUwbXpoMswSYyuQJT0cg3FpLSvH-S_nAiaCARSdeolPbGpxTX5nHVm5AP6tI7N9sCYEL4IUkR_eF4lYZXN4oeXhWtCKavTHIaA8pH6eklL4yBO5";
    final private String contentType = "application/json";
    final String TAG = "NOTIFICATION TAG";
    private static WeakReference<DetailActivityThree> detailActivityThreeWeakReference;
    private String titreDuProduit;
    private String prixduproduit;
    private String image_user;
    private String user_mail;
    private String residence_user;
    private String derniere_conection;
    private String description;
    private String imageduproduit;
    private String datedepublication;
    private Menu menu;
    private boolean is_exist=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_activty_three);
        firebaseAuth=FirebaseAuth.getInstance ();
        utilisateur_actuel=firebaseAuth.getCurrentUser ().getUid ();
        firebaseFirestore=FirebaseFirestore.getInstance();
        iddupost =getIntent().getExtras().getString("id du post");
        categories=getIntent().getExtras().getString("id_categories");
        current_user_id =getIntent().getExtras().getString("id de l'utilisateur");
        lien_image =getIntent().getExtras().getString("image_en_vente");
        detail_image_post=findViewById(R.id.detail_image_post);
        detail_post_titre_produit=findViewById(R.id.detail_prix_produit );
        detail_prix_produit=findViewById(R.id.detail_prix_produit);
        detail_profil_image=findViewById(R.id.detail_image_du_profil);
        //detail_user_name=findViewById(R.id.detail_user_name);
        detail_description=findViewById(R.id.detail_description);
        date_de_publication=findViewById(R.id.date_de_publication);
        firebaseAuth=FirebaseAuth.getInstance();
        toolbarDetail=findViewById(R.id.toolbarDetail);
        setSupportActionBar(toolbarDetail);
        getSupportActionBar ().setDisplayHomeAsUpEnabled ( true );
        toolbarDetail.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity ( new Intent ( getApplicationContext (),Accueil.class ).setFlags ( Intent.FLAG_ACTIVITY_CLEAR_TOP ) );
                finish ();

            }
        });
        detailActivityThreeWeakReference=new WeakReference<>(this);
        vendeur_button=findViewById(R.id.vendeur_button);
        vendeur_button.setEnabled ( false );
        asyncTask=new AsyncTask();
        asyncTask.execute();
        firebaseFirestore.collection ( "publication" ).document ("post utilisateur").collection (current_user_id ).document (iddupost).addSnapshotListener ( this,new EventListener<DocumentSnapshot> () {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (!documentSnapshot.exists ()){
                    Toast.makeText ( DetailActivityThree.this, getString(R.string.voulez_vous_supprimer), Toast.LENGTH_SHORT ).show ();

                }else {

                }
            }
        } );
        showPopup();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed ();
        finish ();
    }

    public class AsyncTask extends android.os.AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute ();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            firebaseFirestore.collection ( "publication" ).document ("post utilisateur").collection ( current_user_id ).document (iddupost).get().addOnCompleteListener(DetailActivityThree.this,new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()){
                        if (task.getResult ().exists ()){
                            String postLike= task.getResult ().getString ( "user_name" );
                             image_user=task.getResult ().getString ( "user_profil_image" );
                            ////sockelerie a faire after
                             titreDuProduit=task.getResult().getString("nom_du_produit");
                             description= task.getResult ().getString ( "decription_du_produit" );
                             imageduproduit=task.getResult ().getString ( "image_du_produit" );
                             prixduproduit= task.getResult ().getString ( "prix_du_produit" );
                              datedepublication=task.getResult ().getString ( "date_de_publication" );
                                detail_post_titre_produit.setText(titreDuProduit);
                                detail_prix_produit.setText(prixduproduit);
                                detail_description.setText(description);
                            lien_image=imageduproduit;
                            vendeur_button.setEnabled ( true );
                            getSupportActionBar().setTitle(titreDuProduit);
                            firebaseFirestore.collection("mes donnees utilisateur").document(current_user_id).get().addOnCompleteListener(DetailActivityThree.this,new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()){
                                        if (task.getResult ().exists ()){
                                            prenom=task.getResult ().getString ( "user_prenom" );
                                            name_user= task.getResult ().getString ( "user_name" );
                                            String image_user=task.getResult ().getString ( "user_profil_image" );
                                            date_de_publication.setText(datedepublication + "  |  " + name_user+" "+prenom);
                                            Picasso.with(getApplicationContext()).load(image_user).transform(new CircleTransform()).into(detail_profil_image);
                                            date_de_publication.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_transition_animation));
                                            detail_profil_image.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_transition_animation));

                                        }
                                    }else {
                                        String error=task.getException().getMessage();

                                    }
                                }
                            });
                            Picasso.with(getApplicationContext()).load(imageduproduit).into(detail_image_post);
                            detail_prix_produit.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_transition_animation));
                            date_de_publication.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_transition_animation));
                            detail_image_post.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_transition_animation));
                            detail_description.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_transition_animation));
                            myDialog.dismiss();
                        }
                    }else {
                        String error=task.getException().getMessage();

                    }
                }
            });
            vendeurActivity();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute ( aVoid );

        }
    }
    public void comment_init(){
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(DetailActivityThree.this);
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
        firebaseFirestore.collection("mes donnees utilisateur").document(utilisateur_actuel).get().addOnCompleteListener(DetailActivityThree.this,new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult ().exists ()){
                        String user_image= task.getResult ().getString ( "user_profil_image" );
                        Picasso.with(DetailActivityThree.this).load(user_image).into(post_detail_user_image);
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
        commentaire_adapter=new Commentaire_Adapter(commentaires_modelList,DetailActivityThree.this);
        rv_comment.setAdapter(commentaire_adapter);
        rv_comment.setLayoutManager(new LinearLayoutManager(DetailActivityThree.this,LinearLayoutManager.VERTICAL,false));
        firebaseFirestore.collection ( "publication" ).document ("categories").collection (categories ).document (iddupost).collection("commentaires").addSnapshotListener ( DetailActivityThree.this,new EventListener<QuerySnapshot> () {
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
                    firebaseFirestore.collection("mes donnees utilisateur").document(current_user_id).get().addOnCompleteListener(DetailActivityThree.this,new OnCompleteListener<DocumentSnapshot> () {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()){
                                if (task.getResult ().exists ()){
                                    String prenom=task.getResult ().getString ( "user_prenom" );
                                    String name_user= task.getResult ().getString ( "user_name" );
                                    String image_user=task.getResult ().getString ( "user_profil_image" );
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
                                        notifcationBody.put ( "viens_de_detail","vrai" );

                                        notification.put("to", TOPIC);
                                        notification.put("data", notifcationBody);
                                    } catch (JSONException e) {
                                        Log.e(TAG, "onCreate: " + e.getMessage() );
                                    }
                                    sendNotification(notification);

                                    ////end test noti
                                }
                            }else {
                                String error=task.getException().getMessage();
                                Toast.makeText ( getApplicationContext (), error, Toast.LENGTH_LONG ).show ();

                            }
                        }
                    });

                    post_detail_add_comment_btn.setVisibility(INVISIBLE);
                    progressBar3.setVisibility(VISIBLE);
                    Date date=new Date();
                    SimpleDateFormat sdf= new SimpleDateFormat("d/MM/y H:mm:ss");
                    Calendar calendar=Calendar.getInstance ();
                    SimpleDateFormat currentDate=new SimpleDateFormat (" dd MMM yyyy" );
                    String saveCurrentDate=currentDate.format ( calendar.getTime () );
                    final Map<String,Object> user_comment = new HashMap();
                    String comment = post_detail_comment.getText().toString();
                    user_comment.put ( "contenu",comment );
                    user_comment.put ( "heure",saveCurrentDate );
                    user_comment.put ( "id_user",utilisateur_actuel );
                    firebaseFirestore.collection ( "publication" ).document ("categories").collection (categories ).document (iddupost).collection("commentaires").add(user_comment).addOnSuccessListener(DetailActivityThree.this, new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            String id_commentaire = documentReference.getId();
                            firebaseFirestore.collection ( "publication" ).document ("categories").collection ("nouveaux" ).document (iddupost).collection("commentaires").document(id_commentaire).set(user_comment).addOnSuccessListener(DetailActivityThree.this,new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                }
                            });

                            firebaseFirestore.collection ( "publication" ).document ("post utilisateur").collection ( current_user_id ).document(iddupost).collection("commentaires").document(id_commentaire).set(user_comment).addOnSuccessListener(DetailActivityThree.this, new OnSuccessListener<Void>() {
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

    private void sendNotification(JSONObject notification) {
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
                        Toast.makeText(DetailActivityThree.this, "Request error", Toast.LENGTH_LONG).show();
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
    }

    public void commentaire(){
        Query firstQuery =firebaseFirestore.collection ( "publication" ).document ("categories").collection ( categories ).document (iddupost).collection("commentaires").orderBy ( "heure",Query.Direction.ASCENDING );
        firstQuery.addSnapshotListener(DetailActivityThree.this,new EventListener<QuerySnapshot>() {
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

    public void vendeurActivity(){
        vendeur_button.setOnClickListener(new View.OnClickListener() {
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
                //finish();

            }
        });
    }

    ////menu
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater ().inflate ( R.menu.detail_menu, menu );
        this.menu = menu;
        firebaseFirestore.collection ( "mes donnees utilisateur" ).document (utilisateur_actuel).collection ( "mes favories" ).document(iddupost).get ().addOnCompleteListener ( DetailActivityThree.this,new OnCompleteListener<DocumentSnapshot>() {
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
                firebaseFirestore.collection ( "mes donnees utilisateur" ).document (utilisateur_actuel).collection ( "mes favories" ).document(iddupost).set(user_post).addOnSuccessListener(DetailActivityThree.this,new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void documentReference) {
                        menu.getItem(0).setIcon(ContextCompat.getDrawable(getApplicationContext (), R.mipmap.ic_like_accent));
                        Toast.makeText ( getApplicationContext (),"ajouter a vos favories",Toast.LENGTH_LONG ).show ();
                    }

                }).addOnFailureListener ( DetailActivityThree.this, new OnFailureListener () {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText ( getApplicationContext (),"error , try later please",Toast.LENGTH_LONG ).show ();
                    }
                } );

            }else {
                menu.getItem(0).setIcon(ContextCompat.getDrawable(getApplicationContext (), R.drawable.ic_like));
                firebaseFirestore.collection ( "mes donnees utilisateur" ).document (utilisateur_actuel).collection ( "mes favories" ).document(iddupost).delete ();
            }

            return true;
        }else if (id==R.id.ic_call){
            firebaseFirestore.collection ( "mes donnees utilisateur" ).document (current_user_id).get ().addOnCompleteListener ( DetailActivityThree.this,new OnCompleteListener<DocumentSnapshot>() {
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


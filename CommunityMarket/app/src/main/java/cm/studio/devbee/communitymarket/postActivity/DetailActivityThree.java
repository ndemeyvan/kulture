package cm.studio.devbee.communitymarket.postActivity;


import android.app.Dialog;
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
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
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

    private static WeakReference<DetailActivityThree> detailActivityThreeWeakReference;
    private String titreDuProduit;
    private String prixduproduit;

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
                            String image_user=task.getResult ().getString ( "user_profil_image" );
                            ////sockelerie a faire after
                             titreDuProduit=task.getResult().getString("nom_du_produit");
                            String description= task.getResult ().getString ( "decription_du_produit" );
                            String imageduproduit=task.getResult ().getString ( "image_du_produit" );
                             prixduproduit= task.getResult ().getString ( "prix_du_produit" );
                            final String datedepublication=task.getResult ().getString ( "date_de_publication" );
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


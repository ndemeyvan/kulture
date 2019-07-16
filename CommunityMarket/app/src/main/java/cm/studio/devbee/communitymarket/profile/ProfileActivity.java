package cm.studio.devbee.communitymarket.profile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import java.lang.ref.WeakReference;
import javax.annotation.Nullable;
import cm.studio.devbee.communitymarket.R;

public class ProfileActivity extends AppCompatActivity {
    private static FirebaseAuth mAuth;
    private static FirebaseFirestore firebaseFirestore;
    private static String current_user_id;
    private static TextView user_name;
    private static TextView telephone;
    private static TextView residence;
    private static TextView email;
    private static TextView operation;
    private static ImageView profilImage;
    private static ProgressBar progressBar;
    private static android.support.v7.widget.Toolbar profil_toolbar;
    private WeakReference<ProfileActivity> profileActivityWeakReference;
    private static AsyncTask asyncTask;
    private static Button vente_button;
    TextView total_vente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        profil_toolbar=findViewById(R.id.profil_de_la_toolbar);
        setSupportActionBar(profil_toolbar);
        user_name=findViewById ( R.id.user_name );
        progressBar=findViewById(R.id.progressBar);
        mAuth=FirebaseAuth.getInstance();
        telephone=findViewById(R.id.profil_user_phone);
        residence=findViewById(R.id.profil_user_residence);
        email=findViewById(R.id.profil_user_email);
        firebaseFirestore=FirebaseFirestore.getInstance ();
        profilImage=findViewById(R.id.circleImageView_profil);
        progressBar.setVisibility(View.VISIBLE);
        profileActivityWeakReference=new WeakReference<>(this);
        asyncTask=new AsyncTask();
        vente_button=findViewById ( R.id.vente_button );
        total_vente=findViewById(R.id.total_vente);
        getSupportActionBar ().setDisplayHomeAsUpEnabled ( true );
        profil_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity ( new Intent ( getApplicationContext (),Accueil.class ).setFlags ( Intent.FLAG_ACTIVITY_CLEAR_TOP ) );
                finish ();
            }
        });
        asyncTask.execute();
        vente_button.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                Intent vendre=new Intent ( ProfileActivity.this ,VenteUtilisateurActivity.class);
                startActivity ( vendre );
                //finish ();
            }
        } );
        current_user_id=mAuth.getCurrentUser ().getUid ();
        firebaseFirestore.collection("publication").document("post utilisateur").collection(current_user_id).addSnapshotListener(ProfileActivity.this,new EventListener<QuerySnapshot>() {
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

        AnimationDrawable animationDrawableOne = (AnimationDrawable) profil_toolbar.getBackground();
        animationDrawableOne.setEnterFadeDuration(2000);
        animationDrawableOne.setExitFadeDuration(4000);
        animationDrawableOne.start();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed ();
        /*Intent gotoHome =new Intent ( ProfileActivity.this,Accueil.class );
        startActivity ( gotoHome );*/
        finish ();
    }

    public void recupererDonne(){
        current_user_id=mAuth.getCurrentUser ().getUid ();
        firebaseFirestore.collection("publication").document("post utilisateur").collection(current_user_id).addSnapshotListener(ProfileActivity.this,new EventListener<QuerySnapshot>() {
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
        firebaseFirestore.collection ( "mes donnees utilisateur" ).document (current_user_id).get ().addOnCompleteListener ( ProfileActivity.this,new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful ()){
                    if (task.getResult ().exists ()){
                        String nom_user = task.getResult ().getString ("user_name");
                        String prenomuser =task.getResult ().getString ("user_prenom");
                        String telephone_user =task.getResult ().getString ("user_telephone");
                        String residence_user  =task.getResult ().getString ("user_residence");
                        String image_profil_user =task.getResult ().getString ("user_profil_image");
                        String email_user =task.getResult ().getString ("user_mail");
                        telephone.setText ( telephone_user );
                        residence.setText ( residence_user );
                        email.setText ( email_user );
                        getSupportActionBar().setTitle( nom_user + " " + prenomuser);
                        user_name.setText ( nom_user + " " + prenomuser );
                        user_name.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_transition_animation));
                        email.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_transition_animation));
                        total_vente.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_transition_animation));
                        residence.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_transition_animation));
                        telephone.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_transition_animation));
                        Picasso.with ( getApplicationContext() ).load ( image_profil_user ).transform(new CircleTransform()).into ( profilImage );
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }else{

                }
            }
        } );
    }



    public class CircleTransform implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }

            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap,
                    BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);

            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);

            squaredBitmap.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return "circle";
        }


    }
    public class AsyncTask extends android.os.AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {

            super.onPreExecute ();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            recupererDonne();
           // recyclerprofil();
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
        mAuth=null;
        firebaseFirestore=null;
        current_user_id=null;
        user_name=null;
        telephone=null;
        residence=null;
        email=null;
        operation=null;
        profilImage=null;
        progressBar=null;
        profil_toolbar=null;

    }

    public void userstatus(final String status){
        firebaseFirestore.collection ( "mes donnees utilisateur" ).document (current_user_id).get ().addOnCompleteListener (ProfileActivity.this, new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (!task.isSuccessful ()){
                    DocumentReference user = firebaseFirestore.collection("mes donnees utilisateur" ).document(current_user_id);
                    user.update("status", status)
                            .addOnSuccessListener(new OnSuccessListener<Void> () {
                                @Override
                                public void onSuccess(Void aVoid) {
                                }
                            })
                            .addOnFailureListener(new OnFailureListener () {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                }
                            });
                }
            }
        } );
    }

    @Override
    protected void onResume() {
        super.onResume ();
        userstatus("online");
    }

    @Override
    protected void onPause() {
        super.onPause ();
        userstatus("offline");

    }
}

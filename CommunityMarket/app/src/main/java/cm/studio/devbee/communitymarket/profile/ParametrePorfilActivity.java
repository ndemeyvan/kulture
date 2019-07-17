package cm.studio.devbee.communitymarket.profile;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import cm.studio.devbee.communitymarket.Accueil;
import cm.studio.devbee.communitymarket.R;
import cm.studio.devbee.communitymarket.postActivity.DetailActivity;
import cm.studio.devbee.communitymarket.postActivity.PostActivityFinal;
import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

import static android.view.View.VISIBLE;

public class ParametrePorfilActivity extends AppCompatActivity {
    private static EditText nom;
    private static EditText premon;
    private static EditText telephone;
    private static Spinner residence;
    private static EditText email;
    private  static  EditText edit_quartier;
    private static Button button_enregister;
    private static Uri mImageUri;
    private static ImageButton imageButton;
    private static CircleImageView parametreImage;
    private static FirebaseAuth mAuth;
    private static StorageReference storageReference;
    private static FirebaseFirestore firebaseFirestore;
    private static String current_user_id;
    private static ProgressBar parametre_progressbar;
    private static AsyncTask asyncTask;
    private static boolean ischange=false;
    private Toolbar toolbar_parametre;
    byte[] final_image;
    private  String user_residence;
    Dialog myDialog;
    private String quartier;
    String est_maitre ="faux";
    private static WeakReference<ParametrePorfilActivity> parametrePorfilActivityWeakReference;
    private String is_master;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_parametre_porfil );
        toolbar_parametre=findViewById(R.id.toolbar_parametre);
        setSupportActionBar(toolbar_parametre);
        nom=findViewById ( R.id.param_nom );
        premon=findViewById ( R.id.param_premon );
        telephone=findViewById ( R.id.param_telephone );
        residence=findViewById ( R.id.param_residence );
        email=findViewById ( R.id.param_mail );
        imageButton=findViewById(R.id.imageButton);
        button_enregister=findViewById ( R.id.param_button );
        parametreImage=findViewById ( R.id.parametre_image );
        edit_quartier=findViewById(R.id.edit_quartier);
        mAuth=FirebaseAuth.getInstance ();
        storageReference=FirebaseStorage.getInstance ().getReference ();
        firebaseFirestore=FirebaseFirestore.getInstance ();
        current_user_id=mAuth.getCurrentUser ().getUid ();
        firebaseFirestore.collection ( "mes donnees utilisateur" ).document (current_user_id).get ().addOnCompleteListener ( ParametrePorfilActivity.this,new OnCompleteListener<DocumentSnapshot>() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful ()){
                    if (task.getResult ().exists ()){
                        String pop_up= task.getResult ().getString ( "user_residence" );
                        String nom_user = task.getResult ().getString ("user_name");
                        if (pop_up.equals ( "..." )){
                            getSupportActionBar().setTitle("Welcome " + nom_user);
                        }else{
                            getSupportActionBar().setTitle("");

                            getSupportActionBar ().setDisplayHomeAsUpEnabled ( true );
                            toolbar_parametre.setNavigationOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    finish ();
                                }
                            });
                        }
                    }else {

                    }
                }else{


                }
            }
        } );

        parametre_progressbar=findViewById ( R.id.parametre_progressbar );
        imageButton=findViewById(R.id.imageButton);
        parametrePorfilActivityWeakReference=new WeakReference<>(this);
        asyncTask=new AsyncTask();
        asyncTask.execute();
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    try {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 555);
                        CropImage.activity()
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .start(ParametrePorfilActivity.this);

                    }catch (Exception e){
                        e.printStackTrace ();
                    }
                } else {
                    CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .start(ParametrePorfilActivity.this);
                }
            }
        });

        parametreImage.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    try {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 555);
                        CropImage.activity()
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .start(ParametrePorfilActivity.this);

                    }catch (Exception e){
                        e.printStackTrace ();
                    }
                } else {
                    CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .start(ParametrePorfilActivity.this);
                }
            }
        } );
        /*ConstraintLayout constraintLayout=findViewById(R.id.layout);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();*/
        Toast.makeText ( getApplicationContext (), getString(R.string.renplir_tous),Toast.LENGTH_LONG ).show ();
        residence.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                user_residence=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        AnimationDrawable animationDrawableOne = (AnimationDrawable) toolbar_parametre.getBackground();
        animationDrawableOne.setEnterFadeDuration(2000);
        animationDrawableOne.setExitFadeDuration(4000);
        animationDrawableOne.start();

    }
    public void showPopup() {
        myDialog=new Dialog(this);
        myDialog.setContentView(R.layout.load_pop_pup);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.setCancelable(false);
        myDialog.show();
    }



    public void getuserdata(){
        button_enregister.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {

                parametre_progressbar.setVisibility ( View.VISIBLE );
                final String user_name = nom.getText ().toString ();
                final String user_premon = premon.getText ().toString ();
                final String user_telephone = telephone.getText ().toString ();
                final String user_email = email.getText ().toString ();
                quartier =edit_quartier.getText().toString();
                /////////// envoi des fichier dans la base de donnee
                if (ischange) {
                    if (!TextUtils.isEmpty ( user_name ) && !TextUtils.isEmpty ( user_telephone ) && !TextUtils.isEmpty ( user_premon ) && !TextUtils.isEmpty ( user_residence ) && mImageUri != null && !TextUtils.isEmpty ( user_email )&& !TextUtils.isEmpty ( quartier )) {
                        parametre_progressbar.setVisibility ( View.VISIBLE );
                        showPopup();
                        final StorageReference image_de_profil = storageReference.child ( "image_de_profil" ).child ( current_user_id + " .jpg" );
                        UploadTask uploadTask = image_de_profil.putBytes(final_image);
                        Task<Uri> urlTask = uploadTask.continueWithTask ( new Continuation<UploadTask.TaskSnapshot, Task<Uri>> () {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if (!task.isSuccessful ()) {
                                    throw task.getException ();
                                }
                                // Continue with the task to get the download URL
                                return image_de_profil.getDownloadUrl ();
                            }
                        } ).addOnCompleteListener ( new OnCompleteListener<Uri> () {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful ()) {
                                    stockage ( task, user_name, user_premon, user_telephone, user_residence +" | "+ quartier, user_email );
                                } else {
                                    String error = task.getException ().getMessage ();
                                   // Toast.makeText ( getApplicationContext (), error, Toast.LENGTH_LONG ).show ();
                                    parametre_progressbar.setVisibility ( View.INVISIBLE );
                                }
                            }
                        } );
                        ////////fin de l'nvoie
                    } else {
                         myDialog.dismiss();

                        Toast.makeText ( getApplicationContext (), getString(R.string.renplir_tous), Toast.LENGTH_LONG ).show ();
                        parametre_progressbar.setVisibility ( View.INVISIBLE );
                    }
                }else{

                    stockage ( null, user_name, user_premon, user_telephone, user_residence +" | "+ quartier, user_email );

                }
            }
        });
    }

    public void stockage(@NonNull Task<Uri> task,String user_name,String user_premon,String user_telephone,String user_residence,String user_email ){
        Uri downloadUri;
        if (task!=null){
            downloadUri = task.getResult ();
        }else{
            downloadUri=mImageUri;
        }


        //////////////////////
        firebaseFirestore.collection ( "mes donnees utilisateur" ).document (current_user_id).get ().addOnCompleteListener ( ParametrePorfilActivity.this,new OnCompleteListener<DocumentSnapshot>() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful ()){
                    if (task.getResult ().exists ()){
                        is_master= task.getResult ().getString ( "is_master" );

                    }else {
                    }
                }else{
                }
            }
        } );

        ///////////////
        Calendar calendar=Calendar.getInstance ();
        SimpleDateFormat currentDate=new SimpleDateFormat (" dd MMM yyyy" );
        String saveCurrentDate=currentDate.format ( calendar.getTime () );
        String randomKey=saveCurrentDate;
        Map<String, String> donnees_utilisateur = new HashMap<> ();
        donnees_utilisateur.put ( "user_name",user_name);
        donnees_utilisateur.put ( "user_prenom",user_premon);
        donnees_utilisateur.put ( "user_telephone", user_telephone );
        donnees_utilisateur.put ( "user_residence", user_residence );
        donnees_utilisateur.put ( "user_mail",user_email);
        donnees_utilisateur.put ( "user_profil_image",downloadUri.toString ());
        donnees_utilisateur.put ( "id_utilisateur", current_user_id);
        donnees_utilisateur.put ( "status","online" );
        donnees_utilisateur.put ( "search",user_name.toLowerCase());
        donnees_utilisateur.put ( "message","lu" );
        donnees_utilisateur.put ( "derniere_conection",randomKey);
        donnees_utilisateur.put ( "has_notification","false");
        donnees_utilisateur.put ( "is_master","faux");

        firebaseFirestore.collection ( "mes donnees utilisateur" ).document ( current_user_id ).set ( donnees_utilisateur ).addOnCompleteListener ( ParametrePorfilActivity.this,new OnCompleteListener<Void> () {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful ()) {
                    Intent intent = new Intent ( getApplicationContext (), Accueil.class );
                    startActivity ( intent );
                    finish ();
                    Toast.makeText ( getApplicationContext (), getString(R.string.param_compte_enregister), Toast.LENGTH_LONG ).show ();
                } else {
                    String error = task.getException ().getMessage ();
                    Toast.makeText ( getApplicationContext (), error, Toast.LENGTH_LONG ).show ();
                    parametre_progressbar.setVisibility ( View.INVISIBLE );
                }
            }
        } );
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed ();
        Intent parametre=new Intent(getApplicationContext(),Accueil.class);
        startActivity(parametre);
        finish ();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult ( requestCode, resultCode, data );
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mImageUri = result.getUri();
                File actualImage = new File(mImageUri.getPath());
                try{
                    Bitmap compressedImage = new Compressor(this)
                            .setMaxWidth(250)
                            .setMaxHeight(250)
                            .setQuality(40)
                            .compressToBitmap(actualImage);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    compressedImage.compress(Bitmap.CompressFormat.JPEG, 40, baos);
                     final_image = baos.toByteArray();
                }catch (Exception e){

                }

                parametreImage.setImageURI ( mImageUri );
                ischange=true;
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
    public void userstatus(final String status){
        firebaseFirestore.collection ( "mes donnees utilisateur" ).document (current_user_id).get ().addOnCompleteListener (ParametrePorfilActivity.this, new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (!task.isSuccessful ()){
                    DocumentReference user = firebaseFirestore.collection("mes donnees utilisateur" ).document(current_user_id);
                    user.update("status", status)
                            .addOnSuccessListener(ParametrePorfilActivity.this,new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                }
                            })
                            .addOnFailureListener(ParametrePorfilActivity.this,new OnFailureListener() {
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


    public class AsyncTask extends android.os.AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute ();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            getuserdata ();
           // recuperation ();
            firebaseFirestore.collection ( "mes donnees utilisateur" ).document (current_user_id).get ().addOnCompleteListener ( ParametrePorfilActivity.this,new OnCompleteListener<DocumentSnapshot> () {
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
                            nom.setText ( nom_user );
                            premon.setText ( prenomuser );
                            edit_quartier.setText(residence_user);
                            telephone.setText ( telephone_user );
                            email.setText ( email_user );
                            mImageUri=Uri.parse ( image_profil_user );
                            parametre_progressbar.setVisibility ( View.INVISIBLE );
                            Picasso.with ( getApplicationContext()).load ( image_profil_user ).into ( parametreImage );
                        }
                    }else{

                    }
                }
            } );
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
        nom=null;
        premon=null;
        telephone=null;
       residence=null;
        email=null;
        button_enregister=null;
        mImageUri=null;
        parametreImage=null;
        mAuth=null;
       storageReference=null;
       firebaseFirestore=null;
       current_user_id=null;
        parametre_progressbar=null;

    }
}

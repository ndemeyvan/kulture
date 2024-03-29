package cm.studio.devbee.communitymarket;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.TwitterAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.lang.ref.WeakReference;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;



public class ChoiceActivity extends AppCompatActivity {
        private static GoogleSignInClient mGoogleSignInClient;
        private static SignInButton gotoLogin;
        private static ImageView devant;
        private static WeakReference<ChoiceActivity> choiceActivityWeakReference;
        private static LoginButton facebook_button;
        private static FirebaseAuth firebaseAuth;
        private static FirebaseFirestore firebaseFirestore;
        private static   CallbackManager callbackManager;
        private static ImageView image_de_choix;
        int RC_SIGN_IN = 0;
        private Dialog myDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        //Twitter.initialize(this);
        setContentView ( R.layout.activity_choice );
        gotoLogin=findViewById ( R.id.gotoLogin );
        firebaseAuth=FirebaseAuth.getInstance();
        facebook_button=findViewById(R.id.facebook_button);
        //image_de_choix=findViewById(R.id.image_de_choix);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("537391119843-njqfai8ka55g6doo947bnklqcap3ih8c.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        choiceActivityWeakReference=new WeakReference<>(this);
        login ();
        printkey();
        firebaseFirestore=FirebaseFirestore.getInstance();
       /* ConstraintLayout constraintLayout=findViewById(R.id.layout);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();*/
        callbackManager=CallbackManager.Factory.create();
        facebook_button.setReadPermissions("email");
        facebook_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                facebookSignIn();
            }
        });
        firebaseAuth=FirebaseAuth.getInstance();
        /*if (firebaseAuth.getCurrentUser() != null) {
            // User is signed in (getCurrentUser() will be null if not signed in)
            Intent intent = new Intent(getApplicationContext(), Accueil.class);
            startActivity(intent);
            finish();
        }*/

        if(CheckNetwork.isInternetAvailable(getApplicationContext ())){//returns true if internet available{

        }
        else{

            //Toast.makeText(getApplicationContext (),getString(R.string.tost_erreur_de_connexion),Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(),NoConnecTionActivity.class);
            startActivity(intent);
            finish();
        }

        //image_de_choix.animate().scaleX(2).scaleY(2).setDuration(2000).start();
    }




    private void facebookSignIn() {
        facebook_button.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccesToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

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

    private void handleFacebookAccesToken(AccessToken loginResult) {
        showPopup();
       // Toast.makeText(getApplicationContext(),getString(R.string.bienvenu),Toast.LENGTH_LONG).show();
        //oast.makeText(getApplicationContext(),getString(R.string.redirection),Toast.LENGTH_LONG).show();
        AuthCredential authCredential=FacebookAuthProvider.getCredential(loginResult.getToken());
        firebaseAuth.signInWithCredential(authCredential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                 final FirebaseUser user = authResult.getUser();
                firebaseFirestore.collection ( "mes donnees utilisateur" ).document (user.getUid()).get ().addOnCompleteListener ( ChoiceActivity.this,new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful ()){
                            if (!task.getResult ().exists ()){
                                Map<String, String> donnees_utilisateur = new HashMap<>();
                                Calendar calendar=Calendar.getInstance ();
                                SimpleDateFormat currentDate=new SimpleDateFormat (" dd MMM yyyy" );
                                String saveCurrentDate=currentDate.format ( calendar.getTime () );
                                String randomKey=saveCurrentDate;
                                donnees_utilisateur.put ( "user_name",user.getDisplayName());
                                donnees_utilisateur.put ( "user_prenom","");
                                donnees_utilisateur.put ( "user_telephone", "");
                                donnees_utilisateur.put ( "user_residence", "");
                                donnees_utilisateur.put ( "user_mail","");
                                donnees_utilisateur.put ( "user_profil_image", String.valueOf(user.getPhotoUrl()));
                                donnees_utilisateur.put ( "id_utilisateur", user.getUid());
                                donnees_utilisateur.put ( "status","online" );
                                donnees_utilisateur.put ( "search",user.getDisplayName().toLowerCase());
                                donnees_utilisateur.put ( "message","lu" );
                                donnees_utilisateur.put ( "derniere_conection",randomKey);
                                donnees_utilisateur.put ( "has_notification","false");
                                donnees_utilisateur.put ( "is_master","false");
                                donnees_utilisateur.put ( "is_on_main","false");

                                firebaseFirestore.collection ( "mes donnees utilisateur" ).document ( user.getUid()).set ( donnees_utilisateur ).addOnCompleteListener ( ChoiceActivity.this,new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful ()) {
                                            myDialog.dismiss();
                                            Intent intent = new Intent ( getApplicationContext (), Accueil.class );
                                            startActivity ( intent );
                                            finish ();
                                            Toast.makeText ( getApplicationContext (), getString(R.string.param_compte_enregister), Toast.LENGTH_LONG ).show ();
                                        } else {
                                            myDialog.dismiss();
                                            String error = task.getException ().getMessage ();
                                            Toast.makeText ( getApplicationContext (), "ce compte existe deja ", Toast.LENGTH_LONG ).show ();
                                            Intent intent = new Intent ( getApplicationContext (), Accueil.class );
                                            startActivity ( intent );
                                            finish ();
                                        }
                                    }
                                } );
                            }else {
                                myDialog.dismiss();
                                Intent gotoparam=new Intent(getApplicationContext(),Accueil.class);
                                startActivity ( gotoparam );
                                finish();
                            }
                        }else{


                        }
                    }
                } );

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                myDialog.dismiss();
                Toast.makeText(getApplicationContext(),"connexion error",Toast.LENGTH_LONG).show();
            }
        });
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {

            }
        }
    }
    //client secret xQuv99V0tWim5kGXzRsINGcw
    private void printkey() {
        try {
            PackageInfo info= getPackageManager().getPackageInfo("cm.studio.devbee.communitymarket",PackageManager.GET_SIGNATURES);
            for (Signature signature:info.signatures){
                try {
                    MessageDigest messageDigest=MessageDigest.getInstance("SHA");
                    messageDigest.update(signature.toByteArray());
                    Log.e("key",Base64.encodeToString(messageDigest.digest(),Base64.DEFAULT));
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void login(){
        gotoLogin.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);

            }
        } );
    }

    public void firebaseAuthWithGoogle(final GoogleSignInAccount acct){
        Log.d("id_user_google", "firebaseAuthWithGoogle:" + acct.getId());
        AuthCredential credential =GoogleAuthProvider.getCredential ( acct.getIdToken (),null );
        Log.e("Credentials", String.format("Provider %s signin method %s", credential.getProvider(), credential.getSignInMethod()));
        firebaseAuth.signInWithCredential ( credential ).addOnCompleteListener ( this, new OnCompleteListener<AuthResult> () {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful ()){
                        showPopup();
                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser ();
                        final String personName = firebaseUser.getDisplayName();
                        final String personFamilyName = "";
                        final String personId = firebaseUser.getUid();
                        final Uri personPhoto = acct.getPhotoUrl();
                        firebaseFirestore.collection ( "mes donnees utilisateur" ).document (personId).get ().addOnCompleteListener ( ChoiceActivity.this,new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful ()){
                                    if (!task.getResult ().exists ()){
                                        Map<String, String> donnees_utilisateur = new HashMap<>();
                                        Calendar calendar=Calendar.getInstance ();
                                        SimpleDateFormat currentDate=new SimpleDateFormat (" dd MMM yyyy" );
                                        String saveCurrentDate=currentDate.format ( calendar.getTime () );
                                        String randomKey=saveCurrentDate;
                                        donnees_utilisateur.put ( "user_name",personName);
                                        donnees_utilisateur.put ( "user_prenom",personFamilyName);
                                        donnees_utilisateur.put ( "user_telephone", "" );
                                        donnees_utilisateur.put ( "user_residence", "");
                                        donnees_utilisateur.put ( "user_mail","");
                                        donnees_utilisateur.put ( "user_profil_image", String.valueOf(personPhoto));
                                        donnees_utilisateur.put ( "id_utilisateur", personId);
                                        donnees_utilisateur.put ( "status","online" );
                                        donnees_utilisateur.put ( "search",personFamilyName);
                                        donnees_utilisateur.put ( "message","lu" );
                                        donnees_utilisateur.put ( "derniere_conection",randomKey);
                                        donnees_utilisateur.put ( "has_notification","false");
                                        donnees_utilisateur.put ( "is_master","false");
                                        donnees_utilisateur.put ( "is_on_main","false");
                                        firebaseFirestore.collection ( "mes donnees utilisateur" ).document ( personId).set ( donnees_utilisateur ).addOnCompleteListener ( ChoiceActivity.this,new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful ()) {
                                                    Intent intent = new Intent ( getApplicationContext (), Accueil.class );
                                                    startActivity ( intent );
                                                    finish ();
                                                    Toast.makeText ( getApplicationContext (), getString(R.string.param_compte_enregister), Toast.LENGTH_LONG ).show ();
                                                } else {
                                                    String error = task.getException ().getMessage ();
                                                    Toast.makeText ( getApplicationContext (), "ce compte existe deja ", Toast.LENGTH_LONG ).show ();
                                                    Intent intent = new Intent ( getApplicationContext (), Accueil.class );
                                                    startActivity ( intent );
                                                    finish ();
                                                }
                                            }
                                        } );
                                    }else {
                                       // Toast.makeText ( getApplicationContext (),"connexion",Toast.LENGTH_LONG ).show ();
                                        Intent gotoparam=new Intent(getApplicationContext(),Accueil.class);
                                        startActivity ( gotoparam );
                                        finish();
                                    }
                                }else{

                                    Toast.makeText(getApplicationContext(),"connexion error",Toast.LENGTH_LONG).show();

                                }
                            }
                        } );
                }else{
                    Toast.makeText(getApplicationContext(),"connexion error",Toast.LENGTH_LONG).show();

                }
            }
        } ).addOnFailureListener ( new OnFailureListener () {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"connexion error",Toast.LENGTH_LONG).show();

            }
        } );
    }

    @Override
    protected void onStart() {
        super.onStart ();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gotoLogin=null;

        devant=null;
    }
}

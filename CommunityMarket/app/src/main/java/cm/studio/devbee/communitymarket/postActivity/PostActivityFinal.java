package cm.studio.devbee.communitymarket.postActivity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import cm.studio.devbee.communitymarket.Accueil;
import cm.studio.devbee.communitymarket.R;
import id.zelory.compressor.Compressor;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

public class PostActivityFinal extends AppCompatActivity implements RewardedVideoAdListener {
    private static  final int MAX_LENGTH =100;
    private static Toolbar postfinaltoolbar;
    private static EditText nomProduit;
    private static EditText descriptionProduit;
    private static EditText prixPorduit;
    private static ImageView imageProduit;
    private static String categoryName ,nom_du_produit,decription_du_produit,prix_du_produit,saveCurrentTime,saveCurrentDate;
    private static Button vendreButton;

    private static Uri mImageUri;
    private static String randomKey;
    private static String current_user_id;
    private static FirebaseFirestore firebaseFirestore;
    private static StorageReference storageReference;
    private static FirebaseAuth firebaseAuth;
    private static Bitmap compressedImageFile;
    private static AsyncTask asyncTask;
    private static WeakReference<PostActivityFinal> postActivityWeakReference;
    private static FloatingActionButton post_new_button;
    private RewardedVideoAd mad;
    byte[] final_image;
    String id_document;
    private Dialog myDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_post_final );
        firebaseAuth=FirebaseAuth.getInstance ();
        storageReference=FirebaseStorage.getInstance ().getReference ();
        firebaseFirestore=FirebaseFirestore.getInstance ();
        current_user_id=firebaseAuth.getCurrentUser ().getUid ();
        postfinaltoolbar=findViewById ( R.id.final_toolbar );
        setSupportActionBar(postfinaltoolbar);
        getSupportActionBar ().setDisplayHomeAsUpEnabled ( true );
        postfinaltoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity ( new Intent ( getApplicationContext (),PostActivity.class ).setFlags ( Intent.FLAG_ACTIVITY_CLEAR_TOP ) );
                finish ();
            }
        });

        ShowcaseConfig config = new ShowcaseConfig();
        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(PostActivityFinal.this, String.valueOf(10));
        sequence.setConfig(config);
        sequence.addSequenceItem(postfinaltoolbar, "cliquer sur l'icone pour choisir une image. \" ok \" pour continuer", "ok");
        sequence.addSequenceItem(imageProduit, " la prevusualisation apparait ici ,ensuite scroller vers le bas pour continuer. \" ok \" pour continuer\"", "ok");
        //sequence.addSequenceItem(imageProduit, " enfin ,remplisser les valeurs propre a votre vente et vendez le. \" ok \" pour continuer\"", "ok");
        sequence.start();
        imageProduit=findViewById ( R.id.imageProduit );
        nomProduit=findViewById ( R.id.post_product_name );
        post_new_button=findViewById ( R.id.post_new_button );
        descriptionProduit=findViewById ( R.id.post_product_description );
        prixPorduit=findViewById ( R.id.post_production_prix );
        vendreButton=findViewById ( R.id.post_button );
        setSupportActionBar ( postfinaltoolbar );
        categoryName=getIntent ().getExtras ().get ( "categoryName" ).toString ();
        Toast.makeText ( getApplicationContext(),categoryName,Toast.LENGTH_LONG ).show ();
        asyncTask=new AsyncTask();
        asyncTask.execute();
        ///////ads"ca-app-pub-3940256099942544~3347511713
        ////my id : ca-app-pub-4353172129870258~6890094527
        MobileAds.initialize(this,"ca-app-pub-4353172129870258~6890094527");
        mad=MobileAds.getRewardedVideoAdInstance(this);
        mad.setRewardedVideoAdListener(this);
        loadRewardedVideo();
        /*ConstraintLayout constraintLayout=findViewById(R.id.layout);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();*/
        //ads
        if (mad.isLoaded()) {
            mad.show();
        }
        //Toast.makeText(getApplicationContext(),getString(R.string.post_remplisser_les_info),Toast.LENGTH_LONG).show();
        postActivityWeakReference=new WeakReference<>(this);

    }

    public void loadRewardedVideo(){
        if (!mad.isLoaded()){
            // ca-app-pub-3940256099942544/5224354917
            // my pub id : ca-app-pub-4353172129870258/9670857450
            mad.loadAd("ca-app-pub-4353172129870258/9670857450",new AdRequest.Builder().build());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater ().inflate ( R.menu.menu_post_article, menu );
        return true;
    }

    public void showPopup() {
        myDialog=new Dialog(this);
        myDialog.setContentView(R.layout.load_pop_pup);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.setCancelable(false);
        myDialog.show();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId ();
        if (id == R.id.send_article) {
            vendreButton.setEnabled ( false );
            if (TextUtils.isEmpty ( nom_du_produit )&&TextUtils.isEmpty ( decription_du_produit )&&TextUtils.isEmpty ( prix_du_produit )&&mImageUri==null){
                Toast.makeText ( getApplicationContext(),getString(R.string.renplir_tous),Toast.LENGTH_LONG ).show ();

            }else{
                showPopup();
                prendreDonnerDevente ();
                loadRewardedVideo();
                if (mad.isLoaded()) {
                    Toast.makeText ( getApplicationContext(),"Regarder cette publiczter pendant le traitement de votre vente , vous pouvez la fermer si vous voulez",Toast.LENGTH_LONG ).show ();
                    mad.show();
                }
            }
            return true;
        }
        if (id == R.id.add_image) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                try {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 555);
                    CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .start(PostActivityFinal.this);
                }catch (Exception e){
                    e.printStackTrace ();
                }
            } else {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(PostActivityFinal.this);
            }
            return true;
        }
        return super.onOptionsItemSelected ( item );
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
                            .setQuality(95)
                            .compressToBitmap(actualImage);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    compressedImage.compress(Bitmap.CompressFormat.JPEG, 95, baos);
                    final_image = baos.toByteArray();
                }catch (Exception e){

                }

                imageProduit.setImageURI ( mImageUri );
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    public void prendreDonner(){
        vendreButton.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {

                vendreButton.setEnabled ( false );
                prendreDonnerDevente ();
            }
        } );
    }
    public void prendreDonnerDevente(){
        nom_du_produit=nomProduit.getText ().toString ();
        decription_du_produit=descriptionProduit.getText ().toString ();
        prix_du_produit=(prixPorduit.getText ().toString ()+" fcfa");
        if (!TextUtils.isEmpty ( nom_du_produit )&&!TextUtils.isEmpty ( decription_du_produit )&&!TextUtils.isEmpty ( prix_du_produit )&&mImageUri!=null){
            stocker();
        }else{
            Toast.makeText ( getApplicationContext(),getString(R.string.renplir_tous),Toast.LENGTH_LONG ).show ();
        }
    }
    public void stocker(){
                            Date date=new Date();
                            SimpleDateFormat sdf= new SimpleDateFormat("d/MM/y H:mm:ss");
                            final String date_avec_seconde=sdf.format(date);
                            Calendar calendar=Calendar.getInstance ();
                            SimpleDateFormat currentDate=new SimpleDateFormat (" dd MMM yyyy" );
                            saveCurrentDate=currentDate.format ( calendar.getTime () );
                            randomKey=saveCurrentDate;
                            String random =random ();
                            final StorageReference image_product_post=storageReference.child ( "image_des_produits" ).child ( random+".jpg" );
                            UploadTask uploadTask =image_product_post.putBytes(final_image);
                            Task<Uri> urlTask = uploadTask.continueWithTask( new Continuation<UploadTask.TaskSnapshot, Task<Uri>> () {
                                @Override
                                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                    if (!task.isSuccessful()) {
                                        throw task.getException();
                                    }
                                    // Continue with the task to get the download URL
                                    return image_product_post.getDownloadUrl();
                                }
                            }).addOnCompleteListener(new OnCompleteListener<Uri> () {
                                @Override
                                public void onComplete(@NonNull final Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        Uri downloadUri = task.getResult();
                                        //String download= taskSnapshot.getUploadSessionUri().toString();
                                        final Map <String,Object> user_post = new HashMap ();
                                        user_post.put ( "nom_du_produit",nom_du_produit );
                                        user_post.put ( "decription_du_produit",decription_du_produit );
                                        user_post.put ( "prix_du_produit",prix_du_produit );
                                        user_post.put ( "date_de_publication",randomKey );
                                        user_post.put ( "utilisateur",current_user_id );
                                        user_post.put ( "image_du_produit",downloadUri.toString() );
                                        user_post.put ( "dete-en-seconde",date_avec_seconde );
                                        user_post.put("categories",categoryName);
                                        firebaseFirestore.collection ( "publication" ).document ("categories").collection ( categoryName ).add(user_post).addOnSuccessListener(PostActivityFinal.this,new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                id_document =documentReference.getId();

                                                firebaseFirestore.collection ( "publication" ).document ("categories").collection ( "nouveaux" ).document(id_document).set(user_post).addOnSuccessListener(PostActivityFinal.this,new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {

                                                    }
                                                });

                                                firebaseFirestore.collection ( "publication" ).document ("post utilisateur").collection ( current_user_id ).document(id_document).set(user_post).addOnSuccessListener(PostActivityFinal.this,new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {

                                                        Intent gotoRecherche=new Intent(getApplicationContext(),Accueil.class);
                                                        startActivity(gotoRecherche);
                                                        finish();
                                                        Toast.makeText(getApplicationContext(),"envoie effectuer",Toast.LENGTH_LONG).show();
                                                        myDialog.dismiss();

                                                    }
                                                });

                                            }

                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                                String error = task.getException().getMessage();
                                                Toast.makeText(getApplicationContext(),error,Toast.LENGTH_LONG).show();

                                            }
                                        });

                                    } else {

                                    }
                                }
                            });
                     }




    public static String random(){
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(MAX_LENGTH);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }

    @Override
    public void onRewardedVideoAdLoaded() {
        Toast.makeText(getApplicationContext(),getString(R.string.wait),Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRewardedVideoAdOpened() {

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
        //mad.destroy(getApplicationContext());
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

    public void userstatus(String status){
        DocumentReference user = firebaseFirestore.collection("mes donnees utilisateur" ).document(current_user_id);
        user.update("status", status)
                .addOnSuccessListener(PostActivityFinal.this,new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                })
                .addOnFailureListener(PostActivityFinal.this,new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }

    @Override
    public void onResume() {
        mad.resume(this);
        super.onResume ();
        userstatus("online");

    }

    @Override
    public void onPause() {
        mad.pause(this);
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
            //setimage ();
            prendreDonner ();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute ( aVoid );

        }
    }
    @Override
    public void onBackPressed() {
            super.onBackPressed ();
            Intent intent=new Intent ( getApplicationContext (),PostActivity.class );
            startActivity ( intent );
            finish ();
    }

    @Override
    protected void onDestroy() {
            asyncTask.cancel(true);
            super.onDestroy();
            asyncTask.cancel(true);
            postfinaltoolbar=null;;
            nomProduit=null;;
            descriptionProduit=null;;
            prixPorduit=null;;
            imageProduit=null;;
            categoryName=null;
            nom_du_produit=null;
            decription_du_produit=null;
            prix_du_produit=null;
            saveCurrentTime=null;
            saveCurrentDate=null;;
            vendreButton=null;;
            mImageUri=null;;
            randomKey=null;;
            current_user_id=null;
            firebaseFirestore=null;;
            storageReference=null;
            firebaseAuth=null;
            compressedImageFile=null;
            mad.destroy(this);
    }
}

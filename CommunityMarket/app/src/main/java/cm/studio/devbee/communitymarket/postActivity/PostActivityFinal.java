package cm.studio.devbee.communitymarket.postActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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
import com.microsoft.projectoxford.vision.VisionServiceClient;
import com.microsoft.projectoxford.vision.VisionServiceRestClient;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import java.io.File;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import cm.studio.devbee.communitymarket.Accueil;
import cm.studio.devbee.communitymarket.R;
import id.zelory.compressor.Compressor;
import android.app.ProgressDialog;
import android.widget.TextView;
import com.google.gson.Gson;
import com.microsoft.projectoxford.vision.contract.AnalysisResult;
import com.microsoft.projectoxford.vision.contract.Caption;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.concurrent.Future;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

public class PostActivityFinal extends AppCompatActivity implements RewardedVideoAdListener {
    private static  final int MAX_LENGTH =100;
    private static Toolbar final_toolbar;
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
    public VisionServiceClient visionServiceClient ;
    private String apilink="https://westcentralus.api.cognitive.microsoft.com/vision/v1.0/analyze?visualFeatures=Categories,Tags,Description&language=en";
    //private String apilink="https://openmarket.cognitiveservices.azure.com/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_post_final );
        firebaseAuth=FirebaseAuth.getInstance ();
        storageReference=FirebaseStorage.getInstance ().getReference ();
        firebaseFirestore=FirebaseFirestore.getInstance ();
        current_user_id=firebaseAuth.getCurrentUser ().getUid ();
        final_toolbar=findViewById ( R.id.final_toolbar );
        setSupportActionBar(final_toolbar);
        getSupportActionBar ().setDisplayHomeAsUpEnabled ( true );
        final_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotopost = new Intent ( PostActivityFinal.this,PostActivity.class );
                startActivity ( gotopost );
                finish ();
            }
        });
        //mcrosoft site key : 05222f7bfd274f8bb1f6ac44a6a1d493
        visionServiceClient = new VisionServiceRestClient ("551f0ff04fd7410da44382c9fb282c0b",apilink);
        imageProduit=findViewById ( R.id.imageProduit );
        nomProduit=findViewById ( R.id.post_product_name );
        post_new_button=findViewById ( R.id.post_new_button );
        descriptionProduit=findViewById ( R.id.post_product_description );
        prixPorduit=findViewById ( R.id.post_production_prix );
        vendreButton=findViewById ( R.id.post_button );
        categoryName=getIntent ().getExtras ().get ( "categoryName" ).toString ();
       // Toast.makeText ( getApplicationContext(),categoryName,Toast.LENGTH_LONG ).show ();
        ///////ads"ca-app-pub-3940256099942544~3347511713
        ////my id : ca-app-pub-4353172129870258~6890094527
        MobileAds.initialize(this,"ca-app-pub-4353172129870258~6890094527");
        mad=MobileAds.getRewardedVideoAdInstance(this);
        mad.setRewardedVideoAdListener(this);
        loadRewardedVideo();
        vendreButton.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                prendreDonnerDevente ();
            }
        } );
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
        //ads
        if (mad.isLoaded()) {
            mad.show();
        }
        //Toast.makeText(getApplicationContext(),getString(R.string.post_remplisser_les_info),Toast.LENGTH_LONG).show();
        postActivityWeakReference=new WeakReference<>(this);
        //Convert image to stream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final ByteArrayInputStream inputStream = new ByteArrayInputStream (outputStream.toByteArray());

        AnimationDrawable animationDrawableOne = (AnimationDrawable) final_toolbar.getBackground();
        animationDrawableOne.setEnterFadeDuration(2000);
        animationDrawableOne.setExitFadeDuration(4000);
        animationDrawableOne.start();
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

            if (TextUtils.isEmpty ( nom_du_produit )&&TextUtils.isEmpty ( decription_du_produit )&&TextUtils.isEmpty ( prix_du_produit )&&mImageUri==null){
                Toast.makeText ( getApplicationContext(),getString(R.string.renplir_tous),Toast.LENGTH_LONG ).show ();

            }else{
                showPopup();
                prendreDonnerDevente ();
                loadRewardedVideo();
                if (mad.isLoaded()) {
                    Toast.makeText ( getApplicationContext(),"Regarder cette publicter pendant le traitement de votre vente , vous pouvez la fermer si vous voulez",Toast.LENGTH_LONG ).show ();
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
                final File actualImage = new File(mImageUri.getPath());


                //visionTask.execute(inputStream);
                ///// debut de reconnaissance
               /* final AsyncTask<Void,Void,Void> asyncTask = new AsyncTask<Void, Void, Void> () {
                    @Override
                    protected Void doInBackground(Void... voids) {
                        Future <HttpResponse<String>> response = Unirest.post("https://microsoft-azure-microsoft-computer-vision-v1.p.rapidapi.com/analyze?visualfeatures=Categories%2CTags%2CColor%2CFaces%2CDescription")
                                .header("X-RapidAPI-Host", "microsoft-azure-microsoft-computer-vision-v1.p.rapidapi.com")
                                .header("X-RapidAPI-Key", "abe6f6383dmsh1c25b01602566e9p1c0d73jsn1e685cb9c0a1")
                                .field("image",actualImage)
                                .asStringAsync ( new Callback<String> () {
                                    @Override
                                    public void completed(HttpResponse<String> httpResponse) {
                                        Log.e("json_data",""+httpResponse.getBody ());
                                    }

                                    @Override
                                    public void failed(UnirestException e) {
                                        Log.e("json_data",""+e.getMessage ());
                                    }

                                    @Override
                                    public void cancelled() {

                                    }
                                } );
                        return null;
                    }

                };
                asyncTask.execute (  );*/

                //////fin de reconnaissance
                try{
                    Bitmap compressedImage = new Compressor(this)
                            .setMaxWidth(250)
                            .setMaxHeight(250)
                            .setQuality(95)
                            .compressToBitmap(actualImage);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    compressedImage.compress(Bitmap.CompressFormat.JPEG, 95, baos);
                    byte[] imageBytes =baos.toByteArray();
                    final ByteArrayInputStream inputStream = new ByteArrayInputStream(baos.toByteArray());

                    /*//debut
                    final String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                    //sending image to server
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, apilink,null,
                            new Response.Listener<JSONObject>(){

                        @Override
                        public void onResponse(JSONObject s) {
                            if(s.equals("true")){
                                Toast.makeText(PostActivityFinal.this, "Uploaded Successful", Toast.LENGTH_LONG).show();
                            }
                            else{
                                Toast.makeText(PostActivityFinal.this, "Some error occurred!", Toast.LENGTH_LONG).show();
                            }
                            try {
                                Log.i("result_vision",s.toString(2));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(PostActivityFinal.this, "Some error occurred -> "+volleyError, Toast.LENGTH_LONG).show();
                            Log.i("vision",volleyError.getMessage());
                        }
                    }) {

                        //adding parameters to send
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> parameters = new HashMap<String, String>();
                            parameters.put("image", imageString);
                            return parameters;
                        }

                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String,String> headers = new HashMap<>();
                            headers.put("Content-Type","application/octet-stream");
                            headers.put("Ocp-Apim-Subscription-Key","1971503e18984bbbb8b1d2cb1675ae5f");
                            return headers != null ? headers : super.getHeaders();
                        }
                    };

                    RequestQueue rQueue = Volley.newRequestQueue(PostActivityFinal.this);
                    rQueue.add(request);
                    ////fin*/
                    AsyncTask<InputStream,String,String> visionTask = new AsyncTask<InputStream, String, String>() {
                        ProgressDialog mDialog = new ProgressDialog(PostActivityFinal.this);
                        @Override
                        protected String doInBackground(InputStream... params) {
                            try{
                                publishProgress("Recognizing....");
                                String[] features = {"Description"};
                                String[] details = {};

                                AnalysisResult result = visionServiceClient.analyzeImage(params[0],features,details);

                                String strResult = new Gson().toJson(result);
                                return strResult;

                            } catch (Exception e) {
                                return null;
                            }
                        }

                        @Override
                        protected void onPreExecute() {
                            mDialog.show();
                        }

                        @Override
                        protected void onPostExecute(String s) {
                            mDialog.dismiss();

                           // AnalysisResult result = new Gson().fromJson(s,AnalysisResult.class);
                            StringBuilder stringBuilder = new StringBuilder();
                            try {
                                AnalysisResult result = new Gson().fromJson(s, AnalysisResult.class);
                                for (Caption caption: result.description.captions) {
                                    stringBuilder.append(caption.text);
                                }
                            } catch (Exception e) {
                                stringBuilder.append(e.getCause());
                            }
                            descriptionProduit.setText(stringBuilder);

                        }

                        @Override
                        protected void onProgressUpdate(String... values) {
                            mDialog.setMessage(values[0]);
                        }
                    };

                    visionTask.execute(inputStream);
                    final_image = baos.toByteArray();
                }catch (Exception e){

                }

                imageProduit.setImageURI ( mImageUri );
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }


    public void prendreDonnerDevente(){
        nom_du_produit=nomProduit.getText ().toString ();
        decription_du_produit=descriptionProduit.getText ().toString ();
        prix_du_produit=(prixPorduit.getText ().toString ()+" fcfa");
        if (!TextUtils.isEmpty ( nom_du_produit )&&!TextUtils.isEmpty ( decription_du_produit )&&!TextUtils.isEmpty ( prix_du_produit )&&mImageUri!=null){
            showPopup();
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
                            SimpleDateFormat currentDate=new SimpleDateFormat (" dd MMM yyyy H:mm:ss" );
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
                                        user_post.put("search",decription_du_produit);
                                        user_post.put("categories",categoryName);
                                        user_post.put("priority",randomKey);
                                        DocumentReference post_reference =firebaseFirestore.collection ( "publication" ).document ("categories").collection ( categoryName ).document();
                                        final String post_id = post_reference.getId();
                                        user_post.put("post_id",post_id);
                                        post_reference.set(user_post).addOnSuccessListener(PostActivityFinal.this,new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void documentReference) {
                                               firebaseFirestore.collection ( "publication" ).document ("categories").collection ( "nouveaux" ).document(post_id).set(user_post).addOnSuccessListener(PostActivityFinal.this,null);

                                                firebaseFirestore.collection ( "publication" ).document ("post utilisateur").collection ( current_user_id ).document(post_id).set(user_post).addOnSuccessListener(PostActivityFinal.this,new OnSuccessListener<Void>() {
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
       //  Toast.makeText(getApplicationContext(),getString(R.string.wait),Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {
      //  Toast.makeText(getApplicationContext(),getString(R.string.see_video),Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRewardedVideoAdClosed() {
        //Toast.makeText(getApplicationContext(),getString(R.string.see_video),Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        //mad.destroy(getApplicationContext());
        //Toast.makeText(getApplicationContext(),getString(R.string.video_seen_thank),Toast.LENGTH_LONG).show();
        //Toast.makeText(getApplicationContext(),getString(R.string.wait),Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {
        //Toast.makeText(getApplicationContext(),"si une video publicitaire ce charge regarder la pour soutenir lappli svp.",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRewardedVideoCompleted() {
        //Toast.makeText(getApplicationContext(),getString(R.string.video_seen_thank),Toast.LENGTH_LONG).show();
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






    @Override
    public void onBackPressed() {
            super.onBackPressed ();
            Intent intent=new Intent ( getApplicationContext (),PostActivity.class );
            startActivity ( intent );
            finish ();
    }

    @Override
    protected void onDestroy() {
            super.onDestroy();
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

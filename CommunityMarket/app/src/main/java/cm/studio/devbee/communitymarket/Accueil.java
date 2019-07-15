package cm.studio.devbee.communitymarket;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
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
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.microsoft.projectoxford.vision.VisionServiceClient;
import com.microsoft.projectoxford.vision.VisionServiceRestClient;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import cm.studio.devbee.communitymarket.Fragments.AccesoireFragment;
import cm.studio.devbee.communitymarket.Fragments.ChaussureFragment;
import cm.studio.devbee.communitymarket.Fragments.ChemiseFragment;
import cm.studio.devbee.communitymarket.Fragments.CulloteFragment;
import cm.studio.devbee.communitymarket.Fragments.HomeFragment;
import cm.studio.devbee.communitymarket.Fragments.JupesFragment;
import cm.studio.devbee.communitymarket.Fragments.LingerieFragment;
import cm.studio.devbee.communitymarket.Fragments.LocationFragment;
import cm.studio.devbee.communitymarket.Fragments.PantalonFragment;
import cm.studio.devbee.communitymarket.Fragments.PullFragment;
import cm.studio.devbee.communitymarket.Fragments.RobeFragment;
import cm.studio.devbee.communitymarket.Fragments.TshirtFragment;
import cm.studio.devbee.communitymarket.a_propos.AproposActivity;
import cm.studio.devbee.communitymarket.messagerie.ChatMessageActivity;
import cm.studio.devbee.communitymarket.messagerie.MessageActivity;
import cm.studio.devbee.communitymarket.postActivity.PostActivity;
import cm.studio.devbee.communitymarket.profile.FavoriesActivity;
import cm.studio.devbee.communitymarket.profile.ParametrePorfilActivity;
import cm.studio.devbee.communitymarket.profile.ProfileActivity;
import cm.studio.devbee.communitymarket.search.ChoiceSearchActivity;
import cm.studio.devbee.communitymarket.search.SearchActivity;
import de.hdodenhof.circleimageview.CircleImageView;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

public class Accueil extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, RewardedVideoAdListener  {
        private FirebaseAuth mAuth;
        private FirebaseFirestore firebaseFirestore;
        private String current_user_id;
        private ImageView acceuille_image;
        private TextView drawer_user_name;
        private FloatingActionButton content_floating_action_btn;
        private TabLayout tabLayout;
        private ViewPager tabsviewpager;
        private static AsyncTask asyncTask;
        private static WeakReference<Accueil> accueilWeakReference;
        private Menu menu;
        private AdView mAdView;
        private String name;
        private Dialog myDialog;
        private  ImageView image_user;
        private String nom_user;
        private CircleImageView notification_enable;

    private String contenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //"..."
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_accueil );
        Toolbar toolbar =findViewById ( R.id.detail_image_post_toolbar );
        setSupportActionBar ( toolbar );
        NavigationView navigationView =findViewById ( R.id.nav_view );
        tabLayout=findViewById(R.id.tabslayout);
        tabsviewpager=findViewById(R.id.tabsview);
        setupViewPager(tabsviewpager);
        tabLayout.setupWithViewPager(tabsviewpager);
        mAuth=FirebaseAuth.getInstance ();
        image_user=findViewById(R.id.image_user);
        notification_enable=findViewById(R.id.notification_enable);
        content_floating_action_btn=findViewById ( R.id.content_floating_action_btn );
        current_user_id=mAuth.getCurrentUser ().getUid ();
        firebaseFirestore=FirebaseFirestore.getInstance ();
        accueilWeakReference=new WeakReference<>(this);
        acceuille_image=navigationView.getHeaderView(0).findViewById(R.id.acceuille_image);
        drawer_user_name=navigationView.getHeaderView(0).findViewById(R.id.drawer_user_name);
        DrawerLayout drawer =findViewById ( R.id.drawer_layout );
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        FirebaseMessaging.getInstance().subscribeToTopic(current_user_id);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle (
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close );
        drawer.addDrawerListener ( toggle );
        toggle.syncState ();
        navigationView.setNavigationItemSelectedListener ( this );
        Calendar calendar=Calendar.getInstance ();
        SimpleDateFormat currentDate=new SimpleDateFormat (" dd MMM yyyy" );
        String saveCurrentDate=currentDate.format ( calendar.getTime () );
        String randomKey=saveCurrentDate;
        DocumentReference user = firebaseFirestore.collection("mes donnees utilisateur" ).document(current_user_id);
        user.update("derniere_conection", randomKey)
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

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);
        // my id ca-app-pub-4353172129870258~6890094527
        // leur id ca-app-pub-3940256099942544~3347511713
        MobileAds.initialize(this, "ca-app-pub-4353172129870258~6890094527");
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        name= (String) drawer_user_name.getText();
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {

                //Toast.makeText(getApplicationContext(), name +" "+ getString(R.string.soutenir),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {

            }

            @Override
            public void onAdOpened() {
                //Toast.makeText(getApplicationContext(),getString(R.string.soutiens_effectif)+" " + name,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the user is about to return
                // to the app after tapping on an ad.
            }
        });




        myDialog = new Dialog (this);
        asyncTask=new AsyncTask();
        asyncTask.execute();
        firebaseFirestore.collection ( "mes donnees utilisateur" ).document (current_user_id).get ().addOnCompleteListener ( Accueil.this,new OnCompleteListener<DocumentSnapshot>() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful ()){
                    if (task.getResult ().exists ()){
                        String pop_up= task.getResult ().getString ( "user_residence" );
                        if (pop_up.equals ( "..." )){
                            content_floating_action_btn.setVisibility(View.INVISIBLE);
                            ShowPopup ();
                        }else{
                            ShowcaseConfig config = new ShowcaseConfig();
                            MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(Accueil.this, String.valueOf(1));
                            sequence.setConfig(config);
                            sequence.addSequenceItem(content_floating_action_btn, "Hello cliquez ici pour ajouter une vente. \" ok \" pour continuer", "ok");
                            sequence.start();
                        }
                    }else {

                    }
                }else{


                }
            }
        } );

        recup();
        vaTopost ();
        showPopup ();

        AnimationDrawable animationDrawableOne = (AnimationDrawable) toolbar.getBackground();
        animationDrawableOne.setEnterFadeDuration(2000);
        animationDrawableOne.setExitFadeDuration(4000);
        animationDrawableOne.start();

        AnimationDrawable animationDrawableTwo = (AnimationDrawable) tabLayout.getBackground();
        animationDrawableTwo.setEnterFadeDuration(2000);
        animationDrawableTwo.setExitFadeDuration(4000);
        animationDrawableTwo.start();
    }



    @Override
    protected void onRestart() {
        super.onRestart ();

    }

    public void exit_pop_up(){
        Button button_pop_up;
        Button plus_tard_pop_up;
        myDialog.setContentView(R.layout.exit_pop_up);
        button_pop_up=myDialog.findViewById ( R.id.non_button);
        plus_tard_pop_up=myDialog.findViewById ( R.id.exit_button);
        plus_tard_pop_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              finish();
            }
        });
        button_pop_up.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
               myDialog.dismiss();
            }
        } );
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable (Color.TRANSPARENT));
        myDialog.setCancelable(false);
        myDialog.show();
    }

    public void setupViewPager(ViewPager viewPager){
        TabsAdapter tabsAdapter=new TabsAdapter(getSupportFragmentManager());
        tabsAdapter.addFragment(new HomeFragment(),getString(R.string.decouvrir).toLowerCase());
        tabsAdapter.addFragment(new TshirtFragment(),getString(R.string.T_shirts).toLowerCase());
        tabsAdapter.addFragment(new PullFragment () ,getString(R.string.Pulls));
        tabsAdapter.addFragment(new JupesFragment () ,getString(R.string.jupes));
        tabsAdapter.addFragment(new ChaussureFragment (),getString(R.string.chaussures));
        tabsAdapter.addFragment(new PantalonFragment (),getString(R.string.pantalons));
        tabsAdapter.addFragment(new CulloteFragment (),getString(R.string.cullotes));
        tabsAdapter.addFragment(new ChemiseFragment (),getString(R.string.chemises));
        tabsAdapter.addFragment(new RobeFragment (),getString(R.string.robes));
        tabsAdapter.addFragment(new AccesoireFragment (),getString(R.string.accessoire));
        tabsAdapter.addFragment(new LingerieFragment (),"lingeries");
        tabsAdapter.addFragment(new LocationFragment(),getString(R.string.location));
        viewPager.setAdapter(tabsAdapter);

    }
    public void showPopup() {
        myDialog=new Dialog(this);
        myDialog.setContentView(R.layout.load_pop_pup);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.setCancelable(false);
        myDialog.show();
    }
    public void recup(){
            firebaseFirestore.collection ( "mes donnees utilisateur" ).document (current_user_id).get ().addOnCompleteListener ( Accueil.this,new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful ()){
                        if (task.getResult ().exists ()){
                            String  image_profil_user =task.getResult ().getString ("user_profil_image");
                             nom_user = task.getResult ().getString ("user_name");
                            String prenomuser =task.getResult ().getString ("user_prenom");
                            getSupportActionBar ().setTitle ( "hello " + prenomuser );
                            drawer_user_name.setText ( nom_user + " " + prenomuser);
                            Picasso.with(Accueil.this).load(image_profil_user).into(image_user);
                            Picasso.with ( getApplicationContext()).load ( image_profil_user ).transform(new CircleTransform()).into ( acceuille_image );
                           // Picasso.with ( getApplicationContext()).load ( image_profil_user ).placeholder(R.drawable.boy).into ( profilbacck_image );
                        }
                    }else{


                    }
                }
            } );
    }

    public void ShowPopup() {
        Button button_pop_up;
        Button plus_tard_pop_up;
        myDialog.setContentView(R.layout.custum_pop_up);
        button_pop_up=myDialog.findViewById ( R.id.non_button);
        plus_tard_pop_up=myDialog.findViewById ( R.id.exit_button);
        plus_tard_pop_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
       button_pop_up.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                Intent gotoparam = new Intent ( Accueil.this,ParametrePorfilActivity.class );
                startActivity ( gotoparam );
                finish ();
            }
        } );
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable (Color.TRANSPARENT));
        myDialog.setCancelable(false);
        myDialog.show();
    }





    @Override
    public void onBackPressed() {
            DrawerLayout drawer = (DrawerLayout) findViewById ( R.id.drawer_layout );
            if (drawer.isDrawerOpen ( GravityCompat.START )) {
                drawer.closeDrawer ( GravityCompat.START );
            } else {
                //super.onBackPressed ();
                exit_pop_up();
            }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
            getMenuInflater ().inflate ( R.menu.accueil, menu );
            this.menu = menu;
        final DocumentReference docRef = firebaseFirestore.collection ( "mes donnees utilisateur" ).document (current_user_id);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }
                if (snapshot != null && snapshot.exists()) {
                    firebaseFirestore=FirebaseFirestore.getInstance ();
                    firebaseFirestore.collection ( "mes donnees utilisateur" ).document (current_user_id).get ().addOnCompleteListener ( new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful ()){
                                if (task.getResult ().exists ()){
                                    String message= task.getResult ().getString ( "message" );
                                    myDialog.dismiss ();
                                    contenu = task.getResult ().getString ( "message_du_notifieur" );
                                    String notification = task.getResult().getString("has_notification");
                                    if (notification.equals("true")){
                                        notification_enable.setVisibility(View.VISIBLE);
                                    }else{
                                        notification_enable.setVisibility(View.INVISIBLE);
                                    }
                                    if (message.equals ( "non lu" )){
                                        menu.getItem(1).setIcon(ContextCompat.getDrawable(getApplicationContext (), R.drawable.message_lu));
                                    }else{
                                        myDialog.dismiss ();
                                        menu.getItem(1).setIcon(ContextCompat.getDrawable(getApplicationContext (), R.drawable.ic_message_float_icon));
                                    }
                                }else {
                                    Intent gotoparam=new Intent(getApplicationContext(),ParametrePorfilActivity.class);
                                    startActivity ( gotoparam );
                                    finish();
                                }
                            }else{
                            }
                        }
                    } );
                } else {
                }
            }
        });
            return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId ();
            if (id == R.id.search) {
                Intent gogotoSearch = new Intent(getApplicationContext(),ChoiceSearchActivity.class);
                startActivity(gogotoSearch);
                //finish ();
                return true;
            }else if (id == R.id.message) {
                Intent message=new Intent(getApplicationContext(),ChatMessageActivity.class);
                message.putExtra ( "viens","acceuil" );
                message.putExtra ( "viens_de_detail","faux" );
                startActivity(message);
                //finish ();
                return true;
            }else if (id ==R.id.favories){
                Intent message=new Intent(getApplicationContext(),FavoriesActivity.class);
                startActivity(message);
            }

            return super.onOptionsItemSelected ( item );
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
            int id = item.getItemId ();

            if (id == R.id.ic_user) {
                Intent intent = new Intent ( getApplicationContext(),ProfileActivity.class );
                startActivity ( intent );
                //finish ();
            } else if (id == R.id.ic_logout) {
                userstatus("offline");
                stopService(new Intent(Accueil.this,MyFirebaseMessagingService.class));

                mAuth.getInstance().signOut();
                Intent intenttwo = new Intent ( getApplicationContext(),ChoiceActivity.class ).setFlags ( Intent.FLAG_ACTIVITY_CLEAR_TOP );
                startActivity ( intenttwo );
                finish ();
            }else if (id == R.id.setting) {
                Intent parametre=new Intent(getApplicationContext(),ParametrePorfilActivity.class);
                startActivity(parametre);
                //finish ();
            }
            else if (id == R.id.nous_contacter) {
                Intent nous_contacter=new Intent(getApplicationContext(),AproposActivity.class);
                startActivity(nous_contacter);
                finish ();
            }else if(id==R.id.ic_message){
                Intent message=new Intent(getApplicationContext(),ChatMessageActivity.class);
                message.putExtra ( "viens","acceuil" );
                message.putExtra ( "viens_de_detail","faux" );
                startActivity(message);
                //finish ();
            }else if (id ==R.id.favories){
                Intent message=new Intent(getApplicationContext(),FavoriesActivity.class);
                startActivity(message);
            }
            DrawerLayout drawer = (DrawerLayout) findViewById ( R.id.drawer_layout );
            drawer.closeDrawer ( GravityCompat.START );
            return true;
            //nous_contacter
    }

    @Override
    protected void onStart() {
            super.onStart();


    }
    public void vaTopost(){
            content_floating_action_btn.setOnClickListener ( new View.OnClickListener () {
                @Override
                public void onClick(View v) {
                    Intent vaTopost =new Intent ( getApplicationContext(),PostActivity.class );
                    startActivity ( vaTopost );
                    finish ();
                }
            } );
    }
    public void userstatus(String status){
            DocumentReference user = firebaseFirestore.collection("mes donnees utilisateur" ).document(current_user_id);
            user.update("status", status)
                    .addOnSuccessListener(Accueil.this,new OnSuccessListener<Void> () {
                        @Override
                        public void onSuccess(Void aVoid) {
                        }
                    })
                    .addOnFailureListener(Accueil.this,new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });
    }



    @Override
    public void onResume() {
            super.onResume ();
            vaTopost ();
           userstatus("online");
    }

    @Override
    public void onPause() {
            super.onPause ();
            userstatus("offline");

    }

    @Override
    public void onRewardedVideoAdLoaded() {

    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {

    }

    @Override
    public void onRewarded(RewardItem rewardItem) {

    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {

    }

    @Override
    public void onRewardedVideoCompleted() {

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
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute ( aVoid );

            }
    }

    @Override
    protected void onDestroy() {
            userstatus("offline");
            asyncTask.cancel(true);
            super.onDestroy();
            userstatus("offline");
            recup();
            asyncTask.cancel(true);
            mAuth=null;
            firebaseFirestore=null;
            content_floating_action_btn=null;
            tabLayout=null;
            tabsviewpager=null;
            accueilWeakReference=null;
    }
}


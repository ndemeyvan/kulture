package cm.studio.devbee.communitymarket;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.google.firebase.firestore.FirebaseFirestore;
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
import cm.studio.devbee.communitymarket.postActivity.PostActivity;
import cm.studio.devbee.communitymarket.profile.ParametrePorfilActivity;
import cm.studio.devbee.communitymarket.profile.ProfileActivity;
import cm.studio.devbee.communitymarket.search.SearchActivity;
import de.hdodenhof.circleimageview.CircleImageView;

public class Accueil extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, RewardedVideoAdListener {
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
        String name;
        Dialog myDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //"..."
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_accueil );
        Toolbar toolbar =findViewById ( R.id.toolbar );
        setSupportActionBar ( toolbar );
        NavigationView navigationView =findViewById ( R.id.nav_view );
        tabLayout=findViewById(R.id.tabslayout);
        tabsviewpager=findViewById(R.id.tabsview);
        setupViewPager(tabsviewpager);
        tabLayout.setupWithViewPager(tabsviewpager);
        mAuth=FirebaseAuth.getInstance ();
        current_user_id=mAuth.getCurrentUser ().getUid ();
        firebaseFirestore=FirebaseFirestore.getInstance ();
        accueilWeakReference=new WeakReference<>(this);
        acceuille_image=navigationView.getHeaderView(0).findViewById(R.id.acceuille_image);
        drawer_user_name=navigationView.getHeaderView(0).findViewById(R.id.drawer_user_name);
        DrawerLayout drawer =findViewById ( R.id.drawer_layout );
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
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

                Toast.makeText(getApplicationContext(), name +" "+ getString(R.string.soutenir),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {

            }

            @Override
            public void onAdOpened() {
                Toast.makeText(getApplicationContext(),getString(R.string.soutiens_effectif)+" " + name,Toast.LENGTH_LONG).show();
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

        firebaseFirestore.collection ( "mes donnees utilisateur" ).document (current_user_id).get ().addOnCompleteListener ( new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful ()){
                    if (task.getResult ().exists ()){
                        String message= task.getResult ().getString ( "message" );
                        if (message.equals ( "non_lu" )){
                            menu.getItem(1).setIcon(ContextCompat.getDrawable(getApplicationContext (), R.drawable.message_lu));
                        }else{

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
        myDialog = new Dialog (this);
        asyncTask=new AsyncTask();
        asyncTask.execute();

        firebaseFirestore.collection ( "mes donnees utilisateur" ).document (current_user_id).get ().addOnCompleteListener ( new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful ()){
                    if (task.getResult ().exists ()){
                        String pop_up= task.getResult ().getString ( "user_residence" );
                        if (pop_up.equals ( "..." )){
                            ShowPopup ();
                        }else{

                        }
                    }else {

                    }
                }else{


                }
            }
        } );

    }



    @Override
    protected void onRestart() {
        super.onRestart ();

    }

    public void setupViewPager(ViewPager viewPager){
        TabsAdapter tabsAdapter=new TabsAdapter(getSupportFragmentManager());
        tabsAdapter.addFragment(new HomeFragment(),getString(R.string.decouvrir));
        tabsAdapter.addFragment(new TshirtFragment(),getString(R.string.T_shirts));
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
    public void recup(){
            firebaseFirestore.collection ( "mes donnees utilisateur" ).document (current_user_id).get ().addOnCompleteListener ( Accueil.this,new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful ()){
                        if (task.getResult ().exists ()){
                            String  image_profil_user =task.getResult ().getString ("user_profil_image");
                            String nom_user = task.getResult ().getString ("user_name");
                            String prenomuser =task.getResult ().getString ("user_prenom");
                            drawer_user_name.setText ( nom_user + " " + prenomuser);
                            Picasso.with ( getApplicationContext()).load ( image_profil_user ).transform(new CircleTransform()).into ( acceuille_image );
                           // Picasso.with ( getApplicationContext()).load ( image_profil_user ).placeholder(R.drawable.boy).into ( profilbacck_image );
                        }
                    }else{


                    }
                }
            } );
    }

    public void ShowPopup() {
        TextView text_pop_up;
        CircleImageView image_pop_up;
        ImageView close_image;
        Button button_pop_up;
        Button plus_tard_pop_up;
        myDialog.setContentView(R.layout.custum_pop_up);
        close_image=myDialog.findViewById ( R.id.close_image );
        image_pop_up=myDialog.findViewById ( R.id.image_pop_up );
        button_pop_up=myDialog.findViewById ( R.id.button_pop_up );
        text_pop_up=myDialog.findViewById ( R.id.text_pop_up );
        plus_tard_pop_up=myDialog.findViewById ( R.id.plus_tard_pop_up );
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
        myDialog.show();
    }





    @Override
    public void onBackPressed() {
            DrawerLayout drawer = (DrawerLayout) findViewById ( R.id.drawer_layout );
            if (drawer.isDrawerOpen ( GravityCompat.START )) {
                drawer.closeDrawer ( GravityCompat.START );
            } else {
                super.onBackPressed ();
            }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater ().inflate ( R.menu.accueil, menu );
            this.menu = menu;
            return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId ();
            if (id == R.id.search) {
                Intent gogotoSearch = new Intent(getApplicationContext(),SearchActivity.class);
                startActivity(gogotoSearch);
                //finish ();
                return true;
            }else if (id == R.id.message) {
                Intent gogotoSearch = new Intent(getApplicationContext(),ChatMessageActivity.class);
                startActivity(gogotoSearch);
                //finish ();
                return true;
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
                mAuth.getInstance().signOut();
                Intent intenttwo = new Intent ( getApplicationContext(),ChoiceActivity.class ).setFlags ( Intent.FLAG_ACTIVITY_CLEAR_TOP );
                startActivity ( intenttwo );
                finish ();
            }else if (id == R.id.setting) {
                Intent parametre=new Intent(getApplicationContext(),ParametrePorfilActivity.class);
                startActivity(parametre);
                finish ();
            }
            else if (id == R.id.nous_contacter) {
                Intent nous_contacter=new Intent(getApplicationContext(),AproposActivity.class);
                startActivity(nous_contacter);
               // finish ();
            }else if(id==R.id.ic_message){
                Intent message=new Intent(getApplicationContext(),ChatMessageActivity.class);
                startActivity(message);
                //finish ();
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
            content_floating_action_btn=findViewById ( R.id.content_floating_action_btn );
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
                recup();
                vaTopost ();

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


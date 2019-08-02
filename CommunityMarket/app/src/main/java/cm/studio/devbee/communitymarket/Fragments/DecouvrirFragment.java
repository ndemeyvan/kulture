package cm.studio.devbee.communitymarket.Fragments;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.Random;

import cm.studio.devbee.communitymarket.Home_Adapter.HomeAdapter;
import cm.studio.devbee.communitymarket.PublicityActivity;
import cm.studio.devbee.communitymarket.R;
import cm.studio.devbee.communitymarket.gridView_post.ModelGridView;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class DecouvrirFragment extends Fragment implements RewardedVideoAdListener {
    private static FirebaseAuth firebaseAuth;
    private static FirebaseFirestore firebaseFirestore;
    private static ImageView imageOne,imageTwo,imageThree,imageFour;
    private static TextView img1,img2,img3,img4;
    private static ProgressBar content_progresbar;
    private static ViewFlipper viewFlipper;
    private static RecyclerView chaussureRecyclerView;
    private static DocumentSnapshot lastVisible;
    private static View v;
    private  static WeakReference<ModeFragment> homeFragmentWeakReference;
    private RecyclerView principalRecyclerView;
    private static  String current_user;
    private AdView mAdView;
    private RewardedVideoAd mad;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference notebookRef = db.collection("publication").document("categories").collection("nouveaux");
    private InterstitialAd mInterstitialAd;
    String a_charger;
    TextView textChausure;
    private Dialog myDialog;
    ViewFlipper viewFlippertwo;
    ImageView pubImageTwo,pubImage;
    ImageView pubImageThree;
    ImageView pubImageFour;
    TextView pubImageTextTwo;
    TextView pubImageTextThree;
    TextView pubImageTextFour;
    private TextView textView16;
    private HomeAdapter adapter;
    private HomeAdapter adapterTwo;

    public DecouvrirFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_decouvrir, container, false);
        pubImageTextTwo=v.findViewById ( R.id.pubImageTextTwo );
        pubImageTextThree=v.findViewById ( R.id.pubImageTextThree );
        pubImageTextFour=v.findViewById ( R.id.pubImageTextFour );
        pubImage=v.findViewById ( R.id.pubImage );
        ////////nvx
        viewFlippertwo=v.findViewById ( R.id.viewFlippertwo);
        pubImageTwo=v.findViewById ( R.id.pubImageTwo );
        pubImageThree=v.findViewById ( R.id.pubImageThree);
        pubImageFour=v.findViewById ( R.id.pubImageFour );
       //////////////slider
        imageOne=v.findViewById(R.id.imageSlideOne);
        imageTwo=v.findViewById(R.id.imageSlideTwo);
        imageThree=v.findViewById(R.id.imageSlideThree);
        imageFour=v.findViewById(R.id.imageSlideFour);
        img1=v.findViewById(R.id.img1);
        img2=v.findViewById(R.id.img2);
        img3=v.findViewById(R.id.img3);
        img4=v.findViewById(R.id.img4);
        textChausure=v.findViewById(R.id.textChausure);
        ///////fin slider
        textView16=v.findViewById ( R.id.textView16 );
        content_progresbar=v.findViewById ( R.id.content_progresbar );
        viewFlipper=v.findViewById(R.id.viewFlipper);
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        uptdate();
        showPopup ();
        firebaseAuth=FirebaseAuth.getInstance ();
        current_user=firebaseAuth.getCurrentUser ().getUid ();
        Query firstQuery =notebookRef.orderBy ( "priority",Query.Direction.DESCENDING );
        FirestoreRecyclerOptions<ModelGridView> options = new FirestoreRecyclerOptions.Builder<ModelGridView>()
                .setQuery(firstQuery, ModelGridView.class)
                .build();
        adapter  = new HomeAdapter (options,getActivity());
        principalRecyclerView = v.findViewById(R.id.principal_recyclerView);
        principalRecyclerView.setHasFixedSize(true);
        principalRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        principalRecyclerView.setAdapter(adapter);
        firebaseFirestore.collection ( "mes donnees utilisateur" ).document (current_user).get ().addOnCompleteListener ( getActivity (),new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful ()){
                    if (task.getResult ().exists ()){
                        String [] liste = {" , " + getString(R.string.il_ya_ceci)," , "+getString(R.string.fait_ton_choix)," , " + getString(R.string.ici_on_vend_la_sap)," , "+getString(R.string.s_habiller_est_un_art_équilibre)," , " + getString(R.string.heureuxdetevoir)};
                        int i ;
                        Random ran = new Random ();
                        i= ran.nextInt(5);
                        String nom_user = task.getResult ().getString ("user_name");
                        textView16.setText (nom_user+" "+liste[i]);

                    }
                }else{


                }
            }
        } );

        int i ;
        Random ran = new Random();
        i= ran.nextInt(9);
        String [] liste = {"vetements","Chaussures","Accessoires","Pantalons","Bijoux","Afritudes","Montres","Polos","Sous_vetement"};
        a_charger = liste[i];
        String text = liste[i];
        textChausure.setText(  text.replaceAll("(?!^)([A-Z])", " $1").toLowerCase ());
        Query queryuery =firebaseFirestore.collection ( "publication" ).document ("categories").collection ("Mode" ).document("dans").collection ( a_charger ).orderBy ( "priority",Query.Direction.DESCENDING );
        FirestoreRecyclerOptions<ModelGridView> optionsTwo = new FirestoreRecyclerOptions.Builder<ModelGridView>()
                .setQuery(queryuery, ModelGridView.class)
                .build();
        adapterTwo  = new HomeAdapter (optionsTwo,getActivity());
        chaussureRecyclerView = v.findViewById(R.id.chaussureRecyclerView);
        chaussureRecyclerView.setHasFixedSize(true);
        chaussureRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        chaussureRecyclerView.setAdapter(adapterTwo);

        // my id ca-app-pub-4353172129870258~6890094527
        // leur id ca-app-pub-3940256099942544~3347511713
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-4353172129870258~6890094527");
        mAdView = v.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener () {
            @Override
            public void onAdLoaded() {
               // Toast.makeText(getApplicationContext(),  "appuyer sur la banniere publictaire pour encourager l'application" ,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {

            }

            @Override
            public void onAdOpened() {
               // Toast.makeText(getApplicationContext(),"merci pour votre soutient",Toast.LENGTH_LONG).show();
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
        //////////////////interticiel mob
        //my id ca-app-pub-4353172129870258~6890094527
        //my key ca-app-pub-4353172129870258/5670006458

        // pour eux ca-app-pub-3940256099942544~3347511713
        //  pour eux ca-app-pub-3940256099942544/1033173712
        MobileAds.initialize(getApplicationContext(),"ca-app-pub-4353172129870258~6890094527");
        mInterstitialAd = new InterstitialAd(getApplicationContext());
        mInterstitialAd.setAdUnitId("ca-app-pub-4353172129870258/5670006458");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.show();
        if (!mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                //  Toast.makeText(getApplicationContext(),  "appuyer sur la banniere publictaire pour encourager l'application" ,Toast.LENGTH_LONG).show();

            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                //Toast.makeText(getApplicationContext(),"merci pour votre soutient",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the interstitial ad is closed.
            }
        });
        ///////ads"ca-app-pub-3940256099942544~3347511713
        ////my id : ca-app-pub-4353172129870258~6890094527
        MobileAds.initialize(getActivity(),"ca-app-pub-4353172129870258~6890094527");
        mad=MobileAds.getRewardedVideoAdInstance(getActivity());
        mad.setRewardedVideoAdListener(this);
        loadRewardedVideo();

        return v ;
    }
    public void loadRewardedVideo(){
        if (!mad.isLoaded()){
            // ca-app-pub-3940256099942544/5224354917
            // my pub id : ca-app-pub-4353172129870258/9670857450
            mad.loadAd("ca-app-pub-4353172129870258/9670857450",new AdRequest.Builder().build());
        }
    }
    public void uptdate(){
        DocumentReference user = firebaseFirestore.collection("sliders").document("images");
        user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    viewFlipper.setOutAnimation(getActivity(),android.R.anim.slide_out_right);
                    viewFlipper.setInAnimation(getActivity(),android.R.anim.slide_in_left);
                    content_progresbar.setVisibility ( View.INVISIBLE );
                    //image_one
                    firebaseFirestore.collection("slider_home_one").document("imageOne").get().addOnCompleteListener(getActivity (),new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()){
                                if (task.getResult().exists()){
                                    final String iinageOne= task.getResult ().getString ( "iinageOne" );
                                    final String contact=task.getResult ().getString ( "contact" );
                                    final String desc=task.getResult().getString("desc");
                                    final String imageThree= task.getResult ().getString ( "imageThree" );
                                    final String imageTwo=task.getResult ().getString ( "imageTwo" );
                                    final String lieu= task.getResult ().getString ( "lieu" );
                                    final String name=task.getResult ().getString ( "name" );
                                    Picasso.with(getActivity()).load(iinageOne).into(imageOne);
                                    myDialog.dismiss ();
                                    imageOne.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent gotoPublicity=new Intent(getActivity(),PublicityActivity.class);
                                            gotoPublicity.putExtra("imageOne",iinageOne);
                                            gotoPublicity.putExtra("contact",contact);
                                            gotoPublicity.putExtra("desc",desc);
                                            gotoPublicity.putExtra("imageThree",imageThree);
                                            gotoPublicity.putExtra("imageTwo",imageTwo);
                                            gotoPublicity.putExtra("lieu",lieu);
                                            gotoPublicity.putExtra("name",name);
                                            startActivity(gotoPublicity);
                                        }
                                    });
                                }
                            }
                        }
                    });

                    //image_two
                    firebaseFirestore.collection("slider_home_one").document("imageTwo").get().addOnCompleteListener(getActivity (),new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()){
                                if (task.getResult().exists()){
                                    final String iinageOne= task.getResult ().getString ( "iinageOne" );
                                    final String contact=task.getResult ().getString ( "contact" );
                                    final String desc=task.getResult().getString("desc");
                                    final String imageThree= task.getResult ().getString ( "imageThree" );
                                    final String imageTwoo=task.getResult ().getString ( "imageTwo" );
                                    final String lieu= task.getResult ().getString ( "lieu" );
                                    final String name=task.getResult ().getString ( "name" );
                                    Picasso.with(getActivity()).load(iinageOne).into(imageTwo);
                                    imageTwo.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent gotoPublicity=new Intent(getActivity(),PublicityActivity.class);
                                            gotoPublicity.putExtra("imageOne",iinageOne);
                                            gotoPublicity.putExtra("contact",contact);
                                            gotoPublicity.putExtra("desc",desc);
                                            gotoPublicity.putExtra("imageThree",imageThree);
                                            gotoPublicity.putExtra("imageTwo",imageTwoo);
                                            gotoPublicity.putExtra("lieu",lieu);
                                            gotoPublicity.putExtra("name",name);
                                            startActivity(gotoPublicity);
                                        }
                                    });
                                }
                            }
                        }
                    });

                    //image_three
                    firebaseFirestore.collection("slider_home_one").document("imageThree").get().addOnCompleteListener(getActivity (),new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()){
                                if (task.getResult().exists()){
                                    final String iinageOne= task.getResult ().getString ( "iinageOne" );
                                    final String contact=task.getResult ().getString ( "contact" );
                                    final String desc=task.getResult().getString("desc");
                                    final String imageThreee= task.getResult ().getString ( "imageThree" );
                                    final String imageTwoo=task.getResult ().getString ( "imageTwo" );
                                    final String lieu= task.getResult ().getString ( "lieu" );
                                    final String name=task.getResult ().getString ( "name" );
                                    Picasso.with(getActivity()).load(iinageOne).into(imageThree);
                                    imageThree.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent gotoPublicity=new Intent(getActivity(),PublicityActivity.class);
                                            gotoPublicity.putExtra("imageOne",iinageOne);
                                            gotoPublicity.putExtra("contact",contact);
                                            gotoPublicity.putExtra("desc",desc);
                                            gotoPublicity.putExtra("imageThree",imageThreee);
                                            gotoPublicity.putExtra("imageTwo",imageTwoo);
                                            gotoPublicity.putExtra("lieu",lieu);
                                            gotoPublicity.putExtra("name",name);
                                            startActivity(gotoPublicity);
                                        }
                                    });
                                }
                            }
                        }
                    });

                    //image_four
                    firebaseFirestore.collection("slider_home_one").document("imageFour").get().addOnCompleteListener(getActivity (),new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()){
                                if (task.getResult().exists()){
                                    final String imageOne= task.getResult ().getString ( "iinageOne" );
                                    final String contact=task.getResult ().getString ( "contact" );
                                    final String desc=task.getResult().getString("desc");
                                    final String imageThreee= task.getResult ().getString ( "imageThree" );
                                    final String imageTwoo=task.getResult ().getString ( "imageTwo" );
                                    final String lieu= task.getResult ().getString ( "lieu" );
                                    final String name=task.getResult ().getString ( "name" );
                                    Picasso.with(getActivity()).load(imageOne).into(imageFour);
                                    imageFour.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent gotoPublicity=new Intent(getActivity(),PublicityActivity.class);
                                            gotoPublicity.putExtra("imageOne",imageOne);
                                            gotoPublicity.putExtra("contact",contact);
                                            gotoPublicity.putExtra("desc",desc);
                                            gotoPublicity.putExtra("imageThree",imageThreee);
                                            gotoPublicity.putExtra("imageTwo",imageTwoo);
                                            gotoPublicity.putExtra("lieu",lieu);
                                            gotoPublicity.putExtra("name",name);
                                            startActivity(gotoPublicity);
                                        }
                                    });
                                }
                            }
                        }
                    });



                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                // Toast.makeText(getActivity(),getString(R.string.erreur_slider),Toast.LENGTH_LONG).show();
            }
        });

    }


    public void showPopup() {
        myDialog=new Dialog(getContext ());
        myDialog.setContentView(R.layout.load_pop_pup);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable (Color.TRANSPARENT));
        myDialog.setCancelable(false);
        myDialog.show();
    }

    @Override
    public void onStart() {
        super.onStart ();
        adapter.startListening ();
        adapterTwo.startListening ();
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
}

package cm.studio.devbee.communitymarket.Fragments;


import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import com.github.xizzhu.simpletooltip.ToolTip;
import com.github.xizzhu.simpletooltip.ToolTipView;
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

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Nullable;

import cm.studio.devbee.communitymarket.Accueil;
import cm.studio.devbee.communitymarket.PublicityActivity;
import cm.studio.devbee.communitymarket.R;
import cm.studio.devbee.communitymarket.postActivity.DetailActivity;
import cm.studio.devbee.communitymarket.profile.ParametrePorfilActivity;
import cm.studio.devbee.communitymarket.utilsForNouveautes.CategoriesAdapteNouveaux;
import cm.studio.devbee.communitymarket.utilsForNouveautes.CategoriesModelNouveaux;
import cm.studio.devbee.communitymarket.utilsForPostPrincipal.PrincipalAdapte;
import cm.studio.devbee.communitymarket.utilsForPostPrincipal.PrincipalModel;
import cm.studio.devbee.communitymarket.utilsForUserApp.UserAdapter;
import cm.studio.devbee.communitymarket.utilsForUserApp.UserModel;
import cm.studio.devbee.communitymarket.welcome.CursorActivity;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements RewardedVideoAdListener {
    private static FirebaseAuth firebaseAuth;
    private static FirebaseFirestore firebaseFirestore;
    private static ImageView imageOne,imageTwo,imageThree,imageFour;
    private static TextView img1,img2,img3,img4;
    private static ProgressBar content_progresbar;
    private static RecyclerView content_recyclerView;
    private static CategoriesAdapteNouveaux categoriesAdapte;
    private static List<CategoriesModelNouveaux> categoriesModelList;
    private static ViewFlipper viewFlipper;
    private static CategoriesAdapteNouveaux categoriesAdapteNouveaux;
    private static RecyclerView nouveauxRecyclerView;
    private static RecyclerView chaussureRecyclerView;
    private static RecyclerView jupesRecyclerView;
    private static List<CategoriesModelNouveaux> categoriesAdapteChaussureList;
    private static DocumentSnapshot lastVisible;
    private static ImageView imagePubFixe;
    private static TextView imagePubText;
    private static View v;
    private  static WeakReference<HomeFragment> homeFragmentWeakReference;
    private static List<PrincipalModel> principalModelList;
    private static PrincipalAdapte principalAdapte;
    private RecyclerView principalRecyclerView;
    private static  String current_user;
    private static AlertDialog.Builder alertDialogBuilder;
    private static AlertDialog alertDialog;
    private AdView mAdView;
    private AdView mAdViewTwo;
    private RewardedVideoAd mad;
    private InterstitialAd mInterstitialAd;
    String a_charger;
    TextView textChausure;
    ViewFlipper viewFlippertwo;
    ImageView pubImageTwo,pubImage;
    ImageView pubImageThree;
    ImageView pubImageFour;
    TextView pubImageTextTwo;
    TextView pubImageTextThree;
    TextView pubImageTextFour;
    boolean isfirstload =true;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_home, container, false);
        pubImageTextTwo=v.findViewById ( R.id.pubImageTextTwo );
        pubImageTextThree=v.findViewById ( R.id.pubImageTextThree );
        pubImageTextFour=v.findViewById ( R.id.pubImageTextFour );
        pubImage=v.findViewById ( R.id.pubImage );
        ////////nvx
        principalRecyclerView=v.findViewById(R.id.principal_recyclerView);
        principalModelList=new ArrayList<>();
        viewFlippertwo=v.findViewById ( R.id.viewFlippertwo);
        pubImageTwo=v.findViewById ( R.id.pubImageTwo );
        pubImageThree=v.findViewById ( R.id.pubImageThree);
        pubImageFour=v.findViewById ( R.id.pubImageFour );
        //
        principalAdapte=new PrincipalAdapte(principalModelList,getActivity());
        principalRecyclerView.setAdapter(principalAdapte);
        principalRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        //////nvx
        ///chaussure
        categoriesAdapteChaussureList=new ArrayList<>(  );
        chaussureRecyclerView=v.findViewById ( R.id.chaussureRecyclerView );
        categoriesAdapteNouveaux=new CategoriesAdapteNouveaux (categoriesAdapteChaussureList,getActivity());
        chaussureRecyclerView.setAdapter ( categoriesAdapteNouveaux );
        chaussureRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
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
        content_progresbar=v.findViewById ( R.id.content_progresbar );
        imagePubFixe=v.findViewById(R.id.pubImage);
        imagePubText=v.findViewById(R.id.pubImageText);
        viewFlipper=v.findViewById(R.id.viewFlipper);
        firebaseFirestore=FirebaseFirestore.getInstance();
        AsyncTask asyncTask=new AsyncTask ();
        asyncTask.execute (  );
        homeFragmentWeakReference=new WeakReference<>(this);
        firebaseAuth=FirebaseAuth.getInstance();
        current_user=firebaseAuth.getCurrentUser().getUid();

        /* alertDialogBuilder = new AlertDialog.Builder(getActivity());
         alertDialogBuilder.setMessage("chargement ...");
         alertDialog = alertDialogBuilder.create();
         alertDialog.setCancelable ( false );
         alertDialog.show();
        alertDialog.cancel();*/
        Toast.makeText(getApplicationContext(),  "appuyer sur la banniere publictaire pour encourager l'application" ,Toast.LENGTH_LONG).show();

        ConstraintLayout constraintLayout=v.findViewById(R.id.layout);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        // my id ca-app-pub-4353172129870258~6890094527
        // leur id ca-app-pub-3940256099942544~3347511713
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-4353172129870258~6890094527");
        mAdView = v.findViewById(R.id.adView);
        mAdViewTwo=v.findViewById(R.id.adViewTwo);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdViewTwo.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                Toast.makeText(getApplicationContext(),  "appuyer sur la banniere publictaire pour encourager l'application" ,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {

            }

            @Override
            public void onAdOpened() {
                Toast.makeText(getApplicationContext(),"merci pour votre soutient",Toast.LENGTH_LONG).show();
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
       // mInterstitialAd.show();
        if (!mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                Toast.makeText(getApplicationContext(),  "appuyer sur la banniere publictaire pour encourager l'application" ,Toast.LENGTH_LONG).show();

            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                Toast.makeText(getApplicationContext(),"merci pour votre soutient",Toast.LENGTH_LONG).show();
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
        mAdView. setAnimation ( AnimationUtils. loadAnimation (getActivity(), R.anim.fade_transition_animation));
        mAdViewTwo. setAnimation ( AnimationUtils. loadAnimation (getActivity(), R.anim.fade_transition_animation));


        ///////ads"ca-app-pub-3940256099942544~3347511713
        ////my id : ca-app-pub-4353172129870258~6890094527
        MobileAds.initialize(getActivity(),"ca-app-pub-4353172129870258~6890094527");
        mad=MobileAds.getRewardedVideoAdInstance(getActivity());
        mad.setRewardedVideoAdListener(this);
        loadRewardedVideo();

         return v;
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
                    firebaseFirestore.collection("slider_home_one").document("imageOne").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()){
                                if (task.getResult().exists()){
                                    final String iinageOne= task.getResult ().getString ( "iinageOne" );
                                    final String contact=task.getResult ().getString ( "contact" );
                                    final String desc=task.getResult().getString("desc");
                                    final String imageThree= task.getResult ().getString ( "imageThre" );
                                    final String imageTwo=task.getResult ().getString ( "imageTwo" );
                                    final String lieu= task.getResult ().getString ( "lieu" );
                                    final String name=task.getResult ().getString ( "name" );
                                    Picasso.with(getActivity()).load(iinageOne).into(imageOne);
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
                    firebaseFirestore.collection("slider_home_one").document("imageTwo").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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
                                    Picasso.with(getActivity()).load(imageTwoo).into(imageTwo);
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
                    firebaseFirestore.collection("slider_home_one").document("imageThree").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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
                                    Picasso.with(getActivity()).load(imageThreee).into(imageThree);
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
                    firebaseFirestore.collection("slider_home_one").document("imageFour").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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

                Toast.makeText(getActivity(),getString(R.string.erreur_slider),Toast.LENGTH_LONG).show();
            }
        });

    }
    public void imagePub(){
        DocumentReference user = firebaseFirestore.collection("sliders").document("image_two");
        user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    viewFlippertwo.setOutAnimation(getActivity(),android.R.anim.slide_in_left);
                    viewFlippertwo.setInAnimation(getActivity(),android.R.anim.slide_out_right);
                    DocumentSnapshot doc =task.getResult();
                    StringBuilder imagePub=new StringBuilder("");
                    imagePub.append(doc.get("pubImage"));
                    imagePubText.setText(imagePub.toString());
                    String lien = imagePubText.getText().toString();
                    Picasso.with(getActivity()).load(lien).into(pubImage);
                    //////////image deux
                    StringBuilder image2=new StringBuilder("");
                    image2.append(doc.get("pubImageTwo"));
                    pubImageTextTwo.setText(image2.toString());
                    String lien2 = pubImageTextTwo.getText().toString();
                    Picasso.with(getActivity()).load(lien2).into(pubImageTwo);
                    //////image trois
                    StringBuilder image3=new StringBuilder("");
                    image3.append(doc.get("pubImageThree"));
                    pubImageTextThree.setText(image3.toString());
                    String lien3 = pubImageTextThree.getText().toString();
                    Picasso.with(getActivity()).load(lien3).into(pubImageThree);
                    //////////image quatre
                    StringBuilder image4=new StringBuilder("");
                    image4.append(doc.get("pubImageFour"));
                    pubImageTextFour.setText(image4.toString());
                    String lien4 = pubImageTextFour.getText().toString();
                    Picasso.with(getActivity()).load(lien4).into(pubImageFour);



                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

 public void nouveautes(){
        Query firstQuery =firebaseFirestore.collection ( "publication" ).document ("categories").collection ( "nouveaux" ).orderBy ( "prix_du_produit",Query.Direction.ASCENDING );
               // .limit(3);
        firstQuery.addSnapshotListener(getActivity(),new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for (DocumentChange doc:queryDocumentSnapshots.getDocumentChanges()){

                    if (doc.getType()==DocumentChange.Type.ADDED){
                        String idupost=doc.getDocument ().getId ();
                        PrincipalModel principalAdaptemodel =doc.getDocument().toObject(PrincipalModel.class).withId ( idupost );
                        if (isfirstload){
                            principalModelList.add(principalAdaptemodel);

                        }else{
                            principalModelList.add(0,principalAdaptemodel);

                        }
                        principalAdapte.notifyDataSetChanged();
                    }
                }
                isfirstload=false;
            }
        });
    }



   public void chaussuresRecycler(){
       int i ;
       Random ran = new Random();
       i= ran.nextInt(11);;
       String [] liste = {"Chaussures","jupes","accessoires","Cullotes","Pantalons","T-shirts","Chemises","robe","pull","lingeries","location"};
       a_charger = liste[i];
       String text = liste[i];
       textChausure.setText(  text.replaceAll("(?!^)([A-Z])", " $1").toUpperCase());
        Query firstQuery =firebaseFirestore.collection ( "publication" ).document ("categories").collection ( a_charger ).orderBy ( "prix_du_produit",Query.Direction.ASCENDING );
        firstQuery.addSnapshotListener(getActivity(),new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for (DocumentChange doc:queryDocumentSnapshots.getDocumentChanges()){
                    if (doc.getType()==DocumentChange.Type.ADDED){
                        String idupost=doc.getDocument ().getId ();
                        CategoriesModelNouveaux categoriesModelChaussure =doc.getDocument().toObject(CategoriesModelNouveaux.class).withId ( idupost );
                        if (isfirstload){
                            categoriesAdapteChaussureList.add(categoriesModelChaussure);
                        }else{
                            categoriesAdapteChaussureList.add(0,categoriesModelChaussure);
                        }
                        categoriesAdapteNouveaux.notifyDataSetChanged();

                    }
                }
                isfirstload=false;

            }
        });

    }

    public void userstatus(String status){
        DocumentReference user = firebaseFirestore.collection("mes donnees utilisateur" ).document(current_user);
        user.update("status", status)
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
    }


    @Override
    public void onResume() {
        super.onResume ();
        loadRewardedVideo();
        mInterstitialAd.show();
        if (!mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
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

    public class AsyncTask extends android.os.AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute ();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            uptdate();
            nouveautes();
            chaussuresRecycler ();
            imagePub();
            imagePub();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute ( aVoid );

        }
    }

    @Override
    public void onDestroy() {
        userstatus ( "false" );
        super.onDestroy();
        userstatus ( "false" );
        firebaseFirestore=null;
        imageFour=null;
        imageThree=null;
        imageTwo=null;
        imageOne=null;
        img1=null;
        img2=null;
        img3=null;
        img4=null;
        content_progresbar=null;
        content_recyclerView=null;
        categoriesAdapte=null;
        categoriesModelList=null;
         categoriesAdapteNouveaux=null;
         nouveauxRecyclerView=null;
         chaussureRecyclerView=null;
         jupesRecyclerView=null;
         categoriesAdapteChaussureList=null;
        lastVisible=null;
        imagePubFixe=null;
        imagePubText=null;
        ///robe
        v=null;
        firebaseAuth=null;


    }
}
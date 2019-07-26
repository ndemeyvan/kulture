package cm.studio.devbee.communitymarket.Fragments;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.ref.WeakReference;

import cm.studio.devbee.communitymarket.Fragments.mode.AccesoireFragment;
import cm.studio.devbee.communitymarket.Fragments.mode.AfritudeFragment;
import cm.studio.devbee.communitymarket.Fragments.mode.BijouxFragment;
import cm.studio.devbee.communitymarket.Fragments.mode.ChaussureFragment;
import cm.studio.devbee.communitymarket.Fragments.mode.JupesFragment;
import cm.studio.devbee.communitymarket.Fragments.mode.MontreFragment;
import cm.studio.devbee.communitymarket.Fragments.mode.PantalonFragment;
import cm.studio.devbee.communitymarket.Fragments.mode.PoloFragment;
import cm.studio.devbee.communitymarket.Fragments.mode.Sous_vetement;
import cm.studio.devbee.communitymarket.Fragments.mode.Vetementragment;
import cm.studio.devbee.communitymarket.Mes_tabs_Adapter.ModeTabs_Adapter;
import cm.studio.devbee.communitymarket.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ModeFragment extends Fragment  {
    //implements RewardedVideoAdListener
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
    private TabLayout tabLayout;
    private ViewPager tabsviewpager;
    private TextView textView16;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference notebookRef = db.collection("publication").document("categories").collection("nouveaux");
    private String viens_detail;

    public ModeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_home, container, false);
        /*pubImageTextTwo=v.findViewById ( R.id.pubImageTextTwo );
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
        AsyncTask asyncTask=new AsyncTask ();
        asyncTask.execute (  );
        homeFragmentWeakReference=new WeakReference<>(this);
        firebaseAuth=FirebaseAuth.getInstance();*/
        /*current_user=firebaseAuth.getCurrentUser().getUid();

        /* alertDialogBuilder = new AlertDialog.Builder(getActivity());
         alertDialogBuilder.setMessage("chargement ...");
         alertDialog = alertDialogBuilder.create();
         alertDialog.setCancelable ( false );
         alertDialog.show();
        alertDialog.cancel();*/
        //Toast.makeText(getApplicationContext(),  "appuyer sur la banniere publictaire pour encourager l'application" ,Toast.LENGTH_LONG).show();



        // my id ca-app-pub-4353172129870258~6890094527
        // leur id ca-app-pub-3940256099942544~3347511713
       /* MobileAds.initialize(getApplicationContext(), "ca-app-pub-4353172129870258~6890094527");
        mAdView = v.findViewById(R.id.adView);
        mAdViewTwo=v.findViewById(R.id.adViewTwo);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdViewTwo.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
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
       // mInterstitialAd.show();
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
        mAdView. setAnimation ( AnimationUtils. loadAnimation (getActivity(), R.anim.fade_transition_animation));
        mAdViewTwo. setAnimation ( AnimationUtils. loadAnimation (getActivity(), R.anim.fade_transition_animation));

        ///////ads"ca-app-pub-3940256099942544~3347511713
        ////my id : ca-app-pub-4353172129870258~6890094527
        MobileAds.initialize(getActivity(),"ca-app-pub-4353172129870258~6890094527");
        mad=MobileAds.getRewardedVideoAdInstance(getActivity());
        mad.setRewardedVideoAdListener(this);
        loadRewardedVideo();
        nouveautes();*/
        tabLayout=v.findViewById(R.id.tabLayoutMode);
        tabsviewpager=v.findViewById(R.id.tabsviewMode);
        setupViewPager(tabsviewpager);
        tabLayout.setupWithViewPager(tabsviewpager);
        return v;
    }
    /*public void loadRewardedVideo(){
        if (!mad.isLoaded()){
            // ca-app-pub-3940256099942544/5224354917
            // my pub id : ca-app-pub-4353172129870258/9670857450
            mad.loadAd("ca-app-pub-4353172129870258/9670857450",new AdRequest.Builder().build());
        }
    }*/



    /*public void uptdate(){
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



    public void imagePub(){
        DocumentReference user_two = firebaseFirestore.collection("sliders").document("images");
        user_two.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    viewFlippertwo.setOutAnimation(getActivity(),android.R.anim.slide_out_right);
                    viewFlippertwo.setInAnimation(getActivity(),android.R.anim.slide_in_left);
                    //image_one
                    firebaseFirestore.collection("slider_home_two").document("imageOne").get().addOnCompleteListener(getActivity (),new OnCompleteListener<DocumentSnapshot>() {
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
                                    Picasso.with(getActivity()).load(iinageOne).into(pubImage);
                                    pubImage.setOnClickListener(new View.OnClickListener() {
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
                    firebaseFirestore.collection("slider_home_two").document("imageTwo").get().addOnCompleteListener(getActivity (),new OnCompleteListener<DocumentSnapshot>() {
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
                                    Picasso.with(getActivity()).load(iinageOne).into(pubImageTwo);
                                    pubImageTwo.setOnClickListener(new View.OnClickListener() {
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
                    firebaseFirestore.collection("slider_home_two").document("imageThree").get().addOnCompleteListener(getActivity (),new OnCompleteListener<DocumentSnapshot>() {
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
                                    Picasso.with(getActivity()).load(iinageOne).into(pubImageThree);
                                    pubImageThree.setOnClickListener(new View.OnClickListener() {
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
                    firebaseFirestore.collection("slider_home_two").document("imageFour").get().addOnCompleteListener(getActivity (),new OnCompleteListener<DocumentSnapshot>() {
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
                                    Picasso.with(getActivity()).load(imageOne).into(pubImageFour);
                                    pubImageFour.setOnClickListener(new View.OnClickListener() {
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

                //Toast.makeText(getActivity(),getString(R.string.erreur_slider),Toast.LENGTH_LONG).show();
            }
        });
    }*/

 /*public void nouveautes(){
         Query firstQuery =notebookRef.orderBy ( "priority",Query.Direction.DESCENDING );
         FirestoreRecyclerOptions<PrincipalModel> options = new FirestoreRecyclerOptions.Builder<PrincipalModel>()
                 .setQuery(firstQuery, PrincipalModel.class)
                 .build();
         adapter  = new PrincipalAdapte(options,getActivity());
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
                         Random ran = new Random();
                         i= ran.nextInt(5);
                         String nom_user = task.getResult ().getString ("user_name");
                         textView16.setText (nom_user+" "+liste[i]);

                     }
                 }else{


                 }
             }
         } );
 }*/

   /* @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
        categoriesAdapte.startListening ();
    }*/



    /*/public void chaussuresRecycler(){
       int i ;
       Random ran = new Random();
       i= ran.nextInt(11);
       String [] liste = {"Chaussures","jupes","accessoires","Cullotes","Pantalons","T-shirts","Chemises","robe","pull","lingeries","location"};
       a_charger = liste[i];
       String text = liste[i];
       textChausure.setText(  text.replaceAll("(?!^)([A-Z])", " $1").toUpperCase());
        Query queryuery =firebaseFirestore.collection ( "publication" ).document ("categories").collection ( a_charger ).orderBy ( "priority",Query.Direction.DESCENDING );
        FirestoreRecyclerOptions<CategoriesModelNouveaux> options = new FirestoreRecyclerOptions.Builder<CategoriesModelNouveaux>()
                .setQuery(queryuery, CategoriesModelNouveaux.class)
                .build();
        categoriesAdapte  = new CategoriesAdapteNouveaux (options,getActivity());
        chaussureRecyclerView = v.findViewById(R.id.chaussureRecyclerView);
        chaussureRecyclerView.setHasFixedSize(true);
        chaussureRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        chaussureRecyclerView.setAdapter(categoriesAdapte);
    }*/




    /*@Override
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

   /* public class AsyncTask extends android.os.AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute ();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            uptdate();
            chaussuresRecycler ();
            imagePub();
            imagePub();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute ( aVoid );

        }
    }*/

    public void setupViewPager(ViewPager viewPager){
        ModeTabs_Adapter tabsAdapter=new ModeTabs_Adapter(getActivity().getSupportFragmentManager());
        tabsAdapter.addFragment(new Vetementragment(),"Vetements");
        tabsAdapter.addFragment(new ChaussureFragment(),"Chaussures");
        tabsAdapter.addFragment(new AccesoireFragment(),"Accessoires");
        tabsAdapter.addFragment(new PantalonFragment(),"Pantalons");
        tabsAdapter.addFragment(new BijouxFragment() ,"Bijouxs");
        tabsAdapter.addFragment(new JupesFragment() ,"Afritudes");
        tabsAdapter.addFragment(new MontreFragment(),"Montres");
        tabsAdapter.addFragment(new PoloFragment(),"Polos");
        tabsAdapter.addFragment(new Sous_vetement(),"Sous vetements");
        viewPager.setAdapter(tabsAdapter);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        firebaseFirestore=null;
        content_progresbar=null;
         chaussureRecyclerView=null;
        lastVisible=null;

        ///robe
        v=null;
        firebaseAuth=null;


    }
}

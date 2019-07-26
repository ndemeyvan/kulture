package cm.studio.devbee.communitymarket.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;

import cm.studio.devbee.communitymarket.PublicityActivity;
import cm.studio.devbee.communitymarket.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DecouvrirFragment extends Fragment {
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
    private TextView textView16;
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
        imagePub();
        return v ;
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
    }

}

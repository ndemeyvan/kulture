package cm.studio.devbee.communitymarket.Fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
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

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import cm.studio.devbee.communitymarket.PublicityActivity;
import cm.studio.devbee.communitymarket.R;
import cm.studio.devbee.communitymarket.gridView_post.GridViewAdapter;
import cm.studio.devbee.communitymarket.gridView_post.ModelGridView;

/**
 * A simple {@link Fragment} subclass.
 */
public class LingerieFragment extends Fragment {
    private static FirebaseFirestore firebaseFirestore;
    private static View v;
    private static RecyclerView lingerie_shirt;
    private static GridViewAdapter categoriesAdaptelingeries;
    private static List<ModelGridView> categoriesModelTshirtList;
    private static ProgressDialog progressDialog;
    private AsyncTask asyncTask;
    private static FirebaseAuth firebaseAuth;
    String curent_user;
    ViewFlipper viewFlippertwo;
    ImageView pubImageTwo,pubImage;
    ImageView pubImageThree;
    ImageView pubImageFour;
    TextView imagePubText;
    TextView pubImageTextTwo;
    TextView pubImageTextThree;
    TextView pubImageTextFour;



    public LingerieFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate ( R.layout.fragment_lingerie, container, false );
        pubImageTextTwo=v.findViewById ( R.id.pubImageTextTwo );
        pubImageTextThree=v.findViewById ( R.id.pubImageTextThree );
        pubImageTextFour=v.findViewById ( R.id.pubImageTextFour );
        pubImage=v.findViewById ( R.id.pubImage );
        viewFlippertwo=v.findViewById ( R.id.viewFlippertwo);
        pubImageTwo=v.findViewById ( R.id.pubImageTwo );
        pubImageThree=v.findViewById ( R.id.pubImageThree);
        pubImageFour=v.findViewById ( R.id.pubImageFour );
        imagePubText=v.findViewById ( R.id.imagePubText );
       asyncTask=new AsyncTask ();
        asyncTask.execute (  );
        firebaseAuth=FirebaseAuth.getInstance ();
        curent_user=firebaseAuth.getCurrentUser ().getUid ();
        ConstraintLayout constraintLayout=v.findViewById(R.id.layout);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();
        return v;
    }
    public void userstatus(String status){
        DocumentReference user = firebaseFirestore.collection("mes donnees utilisateur" ).document(curent_user);
        user.update("status", status)
                .addOnSuccessListener(new OnSuccessListener<Void> () {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                })
                .addOnFailureListener(new OnFailureListener () {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume ();
        userstatus("online");
    }

    @Override
    public void onPause() {
        super.onPause ();
        userstatus("offline");
    }

    public void tshirtRecyclerView(){

        Query firstQuery =firebaseFirestore.collection ( "publication" ).document ("categories").collection ( "lingeries" ).orderBy ( "prix_du_produit",Query.Direction.ASCENDING );
        FirestoreRecyclerOptions<ModelGridView> options = new FirestoreRecyclerOptions.Builder<ModelGridView>()
                .setQuery(firstQuery, ModelGridView.class)
                .build();
        categoriesAdaptelingeries  = new GridViewAdapter (options,getActivity());
        lingerie_shirt = v.findViewById(R.id.lingerie_shirt);
        lingerie_shirt.setHasFixedSize(true);
        lingerie_shirt.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        lingerie_shirt.setAdapter(categoriesAdaptelingeries);


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

                Toast.makeText(getActivity(),getString(R.string.erreur_slider),Toast.LENGTH_LONG).show();
            }
        });
    }


    public class AsyncTask extends android.os.AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {

            super.onPreExecute ();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            tshirtRecyclerView();
            imagePub ();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute ( aVoid );

        }
    }

    @Override
    public void onDestroy() {
        asyncTask.cancel(true);
        super.onDestroy();
        asyncTask.cancel(true);
        firebaseFirestore=null;
        lingerie_shirt=null;
        v=null;
        categoriesAdaptelingeries=null;
        progressDialog=null;
    }

}

package cm.studio.devbee.communitymarket.Fragments.mode;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.List;

import cm.studio.devbee.communitymarket.PublicityActivity;
import cm.studio.devbee.communitymarket.R;
import cm.studio.devbee.communitymarket.gridView_post.GridViewAdapter;
import cm.studio.devbee.communitymarket.gridView_post.ModelGridView;

/**
 * A simple {@link Fragment} subclass.
 */
public class MontreFragment extends Fragment {
    private  static View v;
    private static RecyclerView culloteRecyclerView;
    private static FirebaseFirestore firebaseFirestore;
    private static ProgressDialog progressDialog;
    private static AsyncTask asyncTask;
    private static GridViewAdapter categoriesAdaptecullote;
    private static List<ModelGridView> categoriesModelculloteList;
    private static WeakReference<MontreFragment> culloteFragmentWeakReference;
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

    public MontreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate ( R.layout.fragment_cullote, container, false );
        pubImageTextTwo=v.findViewById ( R.id.pubImageTextTwo );
        pubImageTextThree=v.findViewById ( R.id.pubImageTextThree );
        pubImageTextFour=v.findViewById ( R.id.pubImageTextFour );
        pubImage=v.findViewById ( R.id.pubImage );
        viewFlippertwo=v.findViewById ( R.id.viewFlippertwo);
        pubImageTwo=v.findViewById ( R.id.pubImageTwo );
        pubImageThree=v.findViewById ( R.id.pubImageThree);
        pubImageFour=v.findViewById ( R.id.pubImageFour );
        imagePubText=v.findViewById ( R.id.imagePubText );
        firebaseFirestore=FirebaseFirestore.getInstance ();
        asyncTask=new AsyncTask ();
        asyncTask.execute();
        culloteFragmentWeakReference=new WeakReference<> ( this );
        firebaseAuth=FirebaseAuth.getInstance ();
        curent_user=firebaseAuth.getCurrentUser ().getUid ();

        getActivity ().runOnUiThread
                (new Runnable() {
                    @Override
                    public void run() {
                        RecyclerView ();
                        imagePub ();
                    }
                });
        return v;
    }


    public void RecyclerView(){
        Query firstQuery =firebaseFirestore.collection ( "publication" ).document ("categories").collection ( "Cullotes" ).orderBy ( "priority",Query.Direction.DESCENDING );
        FirestoreRecyclerOptions<ModelGridView> options = new FirestoreRecyclerOptions.Builder<ModelGridView>()
                .setQuery(firstQuery, ModelGridView.class)
                .build();
        categoriesAdaptecullote  = new GridViewAdapter (options,getActivity());
        culloteRecyclerView = v.findViewById(R.id.culloteRecyclerView);
        culloteRecyclerView.setHasFixedSize(true);
        culloteRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        culloteRecyclerView.setAdapter(categoriesAdaptecullote);
    }

    @Override
    public void onStart() {
        super.onStart();
        categoriesAdaptecullote.startListening();
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
                    firebaseFirestore.collection("slider_cullotes").document("imageOne").get().addOnCompleteListener(getActivity (),new OnCompleteListener<DocumentSnapshot>() {
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
                    firebaseFirestore.collection("slider_cullotes").document("imageTwo").get().addOnCompleteListener(getActivity (),new OnCompleteListener<DocumentSnapshot>() {
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
                    firebaseFirestore.collection("slider_cullotes").document("imageThree").get().addOnCompleteListener(getActivity (),new OnCompleteListener<DocumentSnapshot>() {
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
                    firebaseFirestore.collection("slider_cullotes").document("imageFour").get().addOnCompleteListener(getActivity (),new OnCompleteListener<DocumentSnapshot>() {
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
    public  class AsyncTask extends android.os.AsyncTask<Void, Void, Void> {
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
    public void onDestroy() {
        asyncTask.cancel(true);
        super.onDestroy();
        asyncTask.cancel(true);
        culloteRecyclerView=null;
        firebaseFirestore=null;
        progressDialog=null;
        categoriesAdaptecullote=null;
        categoriesModelculloteList=null;
    }


}

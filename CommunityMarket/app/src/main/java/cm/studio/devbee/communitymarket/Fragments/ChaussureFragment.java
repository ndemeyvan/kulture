package cm.studio.devbee.communitymarket.Fragments;


import android.app.ProgressDialog;
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
import java.util.List;

import javax.annotation.Nullable;

import cm.studio.devbee.communitymarket.R;
import cm.studio.devbee.communitymarket.gridView_post.GridViewAdapter;
import cm.studio.devbee.communitymarket.gridView_post.ModelGridView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChaussureFragment extends Fragment {
    private static FirebaseFirestore firebaseFirestore;
    private static View v;
    private static RecyclerView chaussuresRecyclerView;
    private static GridViewAdapter categoriesAdaptechaussures;
    private static List<ModelGridView> categoriesModelchaussuresList;
    private static ImageView imagePubchaussures;
    private static TextView textPubchaussures;
    private static ProgressDialog progressDialog;
    private static WeakReference<ChaussureFragment> chaussuresFragmentWeakReference;
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

    public ChaussureFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate ( R.layout.fragment_chaussure, container, false );
        pubImageTextTwo=v.findViewById ( R.id.pubImageTextTwo );
        pubImageTextThree=v.findViewById ( R.id.pubImageTextThree );
        pubImageTextFour=v.findViewById ( R.id.pubImageTextFour );
        pubImage=v.findViewById ( R.id.pubImage );
        viewFlippertwo=v.findViewById ( R.id.viewFlippertwo);
        pubImageTwo=v.findViewById ( R.id.pubImageTwo );
        pubImageThree=v.findViewById ( R.id.pubImageThree);
        pubImageFour=v.findViewById ( R.id.pubImageFour );
        imagePubText=v.findViewById ( R.id.imagePubText );
        //
        chaussuresFragmentWeakReference=new WeakReference<> ( this );
        chaussuresRecyclerView=v.findViewById ( R.id.chaussureRecyclerView );
        firebaseFirestore=FirebaseFirestore.getInstance();
        categoriesModelchaussuresList=new ArrayList<> (  );
        categoriesAdaptechaussures=new GridViewAdapter (categoriesModelchaussuresList,getActivity());
        chaussuresRecyclerView.setAdapter ( categoriesAdaptechaussures );
        chaussuresRecyclerView.setLayoutManager(new LinearLayoutManager (getActivity(),LinearLayoutManager.VERTICAL,false));
        chaussuresRecyclerView=v.findViewById ( R.id.chaussureRecyclerView );
        asyncTask=new AsyncTask ();
        asyncTask.execute (  );
        firebaseAuth=FirebaseAuth.getInstance ();
        curent_user=firebaseAuth.getCurrentUser ().getUid ();
        ConstraintLayout constraintLayout=v.findViewById(R.id.layout);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();
        imagePub();

        return v;
    }
    public void imagePub(){
        DocumentReference user = firebaseFirestore.collection("sliders").document("image_chaussures");
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

    public void userstatus(String status){
        DocumentReference user = firebaseFirestore.collection("mes donnees utilisateur" ).document(curent_user);
        user.update("status", status)
                .addOnSuccessListener(new OnSuccessListener<Void> () {
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
        userstatus("online");
    }

    @Override
    public void onPause() {
        super.onPause ();
        userstatus("offline");
    }

    public void chaussureRecyclerView(){
        Query firstQuery =firebaseFirestore.collection ( "publication" ).document ("categories").collection ( "Chaussures" ).orderBy ( "prix_du_produit",Query.Direction.ASCENDING );
        firstQuery.addSnapshotListener(getActivity (),new EventListener<QuerySnapshot> () {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                for (DocumentChange doc:queryDocumentSnapshots.getDocumentChanges()){
                    if (doc.getType()==DocumentChange.Type.ADDED){
                        String idupost=doc.getDocument ().getId ();
                        ModelGridView categoriesModelTshirt =doc.getDocument().toObject(ModelGridView.class).withId ( idupost );
                        categoriesModelchaussuresList.add(categoriesModelTshirt);
                        categoriesAdaptechaussures.notifyDataSetChanged();
                    }
                }

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
           chaussureRecyclerView ();
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
        chaussuresRecyclerView=null;
        v=null;
        categoriesAdaptechaussures=null;
        categoriesModelchaussuresList=null;
        imagePubchaussures=null;
        textPubchaussures=null;
        progressDialog=null;
    }

}

//Chaussures
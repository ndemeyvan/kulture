package cm.studio.devbee.communitymarket.utilsForPostPrincipal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import cm.studio.devbee.communitymarket.R;
import cm.studio.devbee.communitymarket.postActivity.DetailActivity;

public class PrincipalAdapte extends FirestoreRecyclerAdapter<PrincipalModel,PrincipalAdapte.ViewHolder> {
    Context context;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    public PrincipalAdapte(@NonNull FirestoreRecyclerOptions<PrincipalModel> options,Context context) {
        super(options);
        this.context=context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v=LayoutInflater.from ( viewGroup.getContext () ).inflate (R.layout.item_layout_principal ,viewGroup,false);
        viewGroup.getContext();
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        return new ViewHolder ( v );
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position, @NonNull PrincipalModel model) {
        String desc =model.getDecription_du_produit();
        String nvxPrix=model.getPrix_du_produit();
        String imageproduit=model.getImage_du_produit ();
        final String nom_id=model.getUtilisateur ();
        Log.i("nom",model.getUtilisateur ());
        String tempsdepub=model.getDate_de_publication ();
        String produinom=model.getNom_du_produit ();
        final String postId=model.getPost_id();
        final String current_user=firebaseAuth.getCurrentUser ().getUid ();
        final String idDuPost=model.getPost_id();
        final String categorie =model.getCategories();
        viewHolder.imageproduitxi ( imageproduit );
        viewHolder.setNom ( desc );
        viewHolder.setPrix(nvxPrix);
        viewHolder.temps ( tempsdepub );
        viewHolder . card_principal . setAnimation ( AnimationUtils. loadAnimation (context, R . anim . fade_transition_animation));
        viewHolder.card_principal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoDetail =new Intent(context,DetailActivity.class);
                gotoDetail.putExtra("id du post",postId);
                gotoDetail.putExtra("id de l'utilisateur",nom_id);
                gotoDetail.putExtra("id_categories",categorie);
                context.startActivity(gotoDetail);
                //((Activity)context).finish();

            }
        });
       firebaseFirestore.collection ( "publication" ).document ("categories").collection ( categorie ).document (postId).collection ( "commentaires" ).addSnapshotListener ( (Activity) context,new EventListener<QuerySnapshot> () {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (!queryDocumentSnapshots.isEmpty ()){
                    int i=queryDocumentSnapshots.size ();
                    viewHolder.comment_number.setText ( i+"" );

                }else{
                    viewHolder.comment_number.setText ( "0" );
                }
            }
        } );
        firebaseFirestore.collection("mes donnees utilisateur").document(nom_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult ().exists ()){
                        String name_user= task.getResult ().getString ( "user_name" );
                        String image_user=task.getResult ().getString ( "user_profil_image" );
                        viewHolder.setuserData ( name_user,image_user );
                    }
                }else {
                    String error=task.getException().getMessage();
                    Toast.makeText(context,error,Toast.LENGTH_LONG).show();
                }
            }
        });
    }



    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView description;
        TextView prix;
        ImageView imageDuproduit;
        TextView nom_utilisateur;
        ImageView profil_utilisateur;
        TextView temps_de_la_pub;
        ImageView like;
        TextView likeCount;
        TextView categorieChoice;
        ProgressBar principal_progress;
        //ConstraintLayout nouveaux_container;
        CardView card_principal;
        TextView comment_number;
        ImageView image_comment;
        TextView text_prix;
        public ViewHolder(@NonNull View itemView) {
            super ( itemView );
            description=itemView.findViewById ( R.id.post_user_description );
            prix=itemView.findViewById(R.id.postUser_prix);
            imageDuproduit=itemView.findViewById(R.id.postImage);
            nom_utilisateur=itemView.findViewById ( R.id.post_user_prenom );
            temps_de_la_pub=itemView.findViewById ( R.id.post_userTemps );
            profil_utilisateur=itemView.findViewById ( R.id.postImageUtilisateur );
            principal_progress=itemView.findViewById ( R.id.principal_progress);
            //nouveaux_container=itemView.findViewById ( R.id.nouveaux_container);
            card_principal=itemView.findViewById ( R.id.nouveaux_container );
            comment_number=itemView.findViewById ( R.id.comment_number );
            image_comment=itemView.findViewById ( R.id.image_comment );
            text_prix=itemView.findViewById ( R.id.text_prix );
            text_prix.setVisibility ( View.INVISIBLE );
            image_comment.setVisibility ( View.INVISIBLE );
            comment_number.setVisibility ( View.INVISIBLE );
        }
        public void setNom(final String desc){
            description.setText(desc);
        }
        public void categorie(String categ){
            categorieChoice.setText(categ);
        }
        public void setPrix(String nouveauxPrix){
            prix.setText(nouveauxPrix);
        }
        public void temps(String postTemps){
           temps_de_la_pub.setText(postTemps);
        }
        public void likexa(int lelike){
            likeCount.setText(lelike+"");
        }
        public void imageproduitxi(String image){

            Picasso.with(context).load(image).into (imageDuproduit );
        }
        public void setuserData(String name,String image){
            nom_utilisateur.setText ( name );
            Picasso.with(context).load(image).transform(new CircleTransform()).into (profil_utilisateur );
            principal_progress.setVisibility ( View.INVISIBLE );
            comment_number=itemView.findViewById ( R.id.comment_number );
            image_comment=itemView.findViewById ( R.id.image_comment );
            text_prix=itemView.findViewById ( R.id.text_prix );
            text_prix.setVisibility ( View.VISIBLE );
            image_comment.setVisibility (  View.VISIBLE  );
            comment_number.setVisibility (  View.VISIBLE  );
        }

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

}

package cm.studio.devbee.communitymarket.profile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
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

import java.util.List;

import javax.annotation.Nullable;

import cm.studio.devbee.communitymarket.R;
import cm.studio.devbee.communitymarket.gridView_post.GridViewAdapter;
import cm.studio.devbee.communitymarket.gridView_post.ModelGridView;
import cm.studio.devbee.communitymarket.postActivity.DetailActivityThree;
import cm.studio.devbee.communitymarket.postActivity.DetailActivityTwo;

public class FavoriteAdapter extends FirestoreRecyclerAdapter<ModelGridView,FavoriteAdapter.ViewHolder> {
    List<ModelGridView> modelGridViewList;
    Context context;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    public FavoriteAdapter(@NonNull FirestoreRecyclerOptions<ModelGridView> options,Context  context) {
        super ( options );
        this.context=context;

    }

    @Override
    protected void onBindViewHolder(@NonNull final FavoriteAdapter.ViewHolder viewHolder, int position, @NonNull ModelGridView model) {
        String produit_image =model.getImage_du_produit();
        String nom=model.getNom_du_produit();
        String desc =model.getDecription_du_produit();
        String prix_produit=model.getPrix_du_produit();
        String tempsdepub=model.getDate_de_publication ();
        final String nom_utilisateur=model.getUtilisateur();
        final String idDuPost=model.getPost_id();
        final String categorie=model.getCategories();
        //viewHolder.setCatrogies_name(categorie);
        viewHolder.prix_produit(prix_produit);
        viewHolder.image_produit(produit_image);
        //viewHolder.nom_produit(nom);
        viewHolder.post_user_description.setText ( desc );
        viewHolder.post_userTemps.setText ( tempsdepub );
        // viewHolder.setUser(nom_utilisateur);
        firebaseFirestore.collection ( "publication" ).document ("categories").collection ( categorie ).document (idDuPost).collection ( "commentaires" ).addSnapshotListener ( (Activity) context,new EventListener<QuerySnapshot> () {
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
        viewHolder.profil_container.setAnimation ( AnimationUtils.loadAnimation ( context,R.anim.fade_transition_animation ) );
        viewHolder.profil_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoDetail =new Intent(context,DetailActivityThree.class);
                gotoDetail.putExtra("id du post",idDuPost);
                gotoDetail.putExtra("id de l'utilisateur",nom_utilisateur);
                gotoDetail.putExtra("id_categories",categorie);
                context.startActivity(gotoDetail);

            }
        });
        firebaseFirestore.collection("mes donnees utilisateur").document(nom_utilisateur).get().addOnCompleteListener((Activity) context,new OnCompleteListener<DocumentSnapshot> () {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult ().exists ()){
                        String image_user=task.getResult ().getString ( "user_profil_image" );
                        String user_nom=task.getResult ().getString ( "user_name" );
                        String user_prenom=task.getResult ().getString ( "user_prenom" );
                        viewHolder.nom_user.setText(user_nom+" "+user_prenom);
                        if (firebaseAuth.getCurrentUser().getUid()==nom_utilisateur){
                            viewHolder.nom_user.setText(" ");
                        }
                        viewHolder.profil_post ( image_user );
                    }
                }else {
                    String error=task.getException().getMessage();
                    Toast.makeText(context,error,Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @NonNull
    @Override
    public FavoriteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        View v=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_post_layout_vendeur,viewGroup,false);
        return new ViewHolder ( v );
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView produit;
        ImageView post_image_profil;
        //TextView post_titre_produit_description;
        TextView prix_post;
        // TextView catrogies_name;
        TextView nom_user;
        CardView profil_container;
        TextView post_user_description;
        ProgressBar principal_progress;
        TextView post_userTemps;
        TextView comment_number;
        ImageView image_comment;
        TextView text_prix;
        public ViewHolder(@NonNull View itemView) {
            super ( itemView );
            produit=itemView.findViewById(R.id.post_image_vendeur );
            prix_post=itemView.findViewById(R.id.prix_postl_vendeur );
            post_image_profil=itemView.findViewById ( R.id.profil_vendeur );
            nom_user=itemView.findViewById(R.id.nom_user);
            profil_container=itemView.findViewById ( R.id.profil_container );
            post_user_description=itemView.findViewById ( R.id.post_user_description );
            principal_progress=itemView.findViewById ( R.id.principal_progress );
            post_userTemps=itemView.findViewById ( R.id.post_userTemps );
            comment_number=itemView.findViewById ( R.id.comment_number );
            image_comment=itemView.findViewById ( R.id.image_comment );
            text_prix=itemView.findViewById ( R.id.text_prix );
            text_prix.setVisibility ( View.INVISIBLE );
            image_comment.setVisibility ( View.INVISIBLE );
            comment_number.setVisibility ( View.INVISIBLE );
        }

        public void image_produit(String image){
            Picasso.with(context).load(image).into (produit );
        }
        /*public void nom_produit(String nom){
             post_titre_produit_description.setText(nom);
         }*/
        public void prix_produit(String prix){
            prix_post.setText(prix);
        }
        public void profil_post(String profil){
            Picasso.with(context).load(profil).transform(new CircleTransform ()).into (post_image_profil );
            principal_progress.setVisibility ( View.INVISIBLE  );
            text_prix.setVisibility ( View.VISIBLE );
            image_comment.setVisibility (  View.VISIBLE  );
            comment_number.setVisibility (  View.VISIBLE  );
        }
       /* public void setCatrogies_name(String cat){
            catrogies_name.setText(cat);
        }*/
       /* public void setUser(String user){
            nom_user.setText(user);
        }*/

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

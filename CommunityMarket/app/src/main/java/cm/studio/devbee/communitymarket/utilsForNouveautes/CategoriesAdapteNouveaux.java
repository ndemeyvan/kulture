package cm.studio.devbee.communitymarket.utilsForNouveautes;

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
import com.google.firebase.firestore.DocumentReference;
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

import cm.studio.devbee.communitymarket.Accueil;
import cm.studio.devbee.communitymarket.R;
import cm.studio.devbee.communitymarket.postActivity.DetailActivity;
import cm.studio.devbee.communitymarket.postActivity.DetailActivityTwo;
import cm.studio.devbee.communitymarket.postActivity.PostActivityFinal;
import cm.studio.devbee.communitymarket.utilsForUserApp.UserAdapter;
import de.hdodenhof.circleimageview.CircleImageView;

public class  CategoriesAdapteNouveaux extends FirestoreRecyclerAdapter<CategoriesModelNouveaux,CategoriesAdapteNouveaux.ViewHolder> {

    Context context;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    public CategoriesAdapteNouveaux(@NonNull FirestoreRecyclerOptions<CategoriesModelNouveaux> options,Context context) {
        super ( options );
        this.context=context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v=LayoutInflater.from ( viewGroup.getContext () ).inflate (R.layout.item_nouveautes ,viewGroup,false);
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        return new ViewHolder ( v );
    }


    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i, @NonNull CategoriesModelNouveaux model) {
        String desc =model.getDecription_du_produit();
        String nvxPrix=model.getPrix_du_produit();
        final String imageproduit=model.getImage_du_produit ();
        final String nom_id=model.getUtilisateur ();
        String tempsdepub=model.getDate_de_publication ();
        String produinom=model.getNom_du_produit ();
        final String current_user=firebaseAuth.getCurrentUser ().getUid ();
        final String idDuPost=model.getPost_id ();
        final String categorie=model.getCategories();
        viewHolder.categorie(categorie);
        viewHolder.imageproduitxi ( imageproduit );
        viewHolder.setNom ( desc );
        viewHolder.setPrix(nvxPrix);
        viewHolder.temps ( tempsdepub );
        viewHolder.nomproduit ( produinom );
        viewHolder.container. setAnimation ( AnimationUtils. loadAnimation (context, R.anim.fade_transition_animation));
        viewHolder.card_nvx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoDetail =new Intent(context,DetailActivityTwo.class);
                gotoDetail.putExtra("id du post",idDuPost);
                gotoDetail.putExtra("id de l'utilisateur",nom_id);
                gotoDetail.putExtra("id_categories",categorie);
                gotoDetail.putExtra("image_en_vente",imageproduit);
                context.startActivity(gotoDetail);
                //((Activity)context).finish();

            }
        });
        firebaseFirestore.collection("mes donnees utilisateur").document(nom_id).get().addOnCompleteListener((Activity) context,new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult ().exists ()){
                        String name_user= task.getResult ().getString ( "user_name" );
                        String image_user=task.getResult ().getString ( "user_profil_image" );
                        String user_prenom=task.getResult ().getString ( "user_prenom" );

                        viewHolder.setuserData ( name_user,user_prenom,image_user );
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
        TextView liker;
        ImageView imageDuproduit;
        TextView nom_utilisateur;
        ImageView profil_utilisateur;
        TextView temps_de_la_pub;
        ImageView image_profil;
        TextView nouveaux_tire;
        ImageView like;
        TextView likeCount;
        TextView categorieChoice;
        ConstraintLayout container;
        CardView card_nvx;
        ProgressBar nouveaux_progress;
        public ViewHolder(@NonNull View itemView) {
            super ( itemView );
            description=itemView.findViewById ( R.id.nouveaux_description_du_produit );
            prix=itemView.findViewById(R.id.nouveaux_prix);
            nouveaux_tire=itemView.findViewById ( R.id.nouveaux_tire );
            image_profil=itemView.findViewById ( R.id.nouveaux_image_profile );
            liker=itemView.findViewById(R.id.nouveaux_nombre_de_like);
            imageDuproduit=itemView.findViewById(R.id.nouveaute_image);
            container=itemView.findViewById ( R.id.container );
            nom_utilisateur=itemView.findViewById ( R.id.nouveaux_user_name );
            temps_de_la_pub=itemView.findViewById ( R.id.nouveaux_temps );
            like =itemView.findViewById ( R.id.like_image );
            card_nvx=itemView.findViewById ( R.id.card_nvx );
            profil_utilisateur=itemView.findViewById ( R.id.nouveaux_image_profile );
            likeCount=itemView.findViewById ( R.id.nouveaux_nombre_de_like );
            categorieChoice=itemView.findViewById(R.id.categorieChoice);
            nouveaux_progress=itemView.findViewById ( R.id.nouveaux_progress);
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
        public void setuserData(String name,String prenom,String image){
            nom_utilisateur.setText ( name +" "+prenom);
            Picasso.with(context).load(image).transform(new CircleTransform()).into (profil_utilisateur );
            nouveaux_progress.setVisibility ( View.INVISIBLE );
        }
        public void nomproduit(String produitnom){
            nouveaux_tire.setText ( produitnom );
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

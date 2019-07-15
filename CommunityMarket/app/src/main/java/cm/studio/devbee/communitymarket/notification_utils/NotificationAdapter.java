package cm.studio.devbee.communitymarket.notification_utils;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import cm.studio.devbee.communitymarket.R;
import cm.studio.devbee.communitymarket.utilForChat.ModelChat;
import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationAdapter extends FirestoreRecyclerAdapter<Model_notification,NotificationAdapter.ViewHolder> {

    Context context;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public NotificationAdapter(@NonNull FirestoreRecyclerOptions<Model_notification> options,Context context) {
        super(options);
        this.context=context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_notification,viewGroup,false);
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        return new ViewHolder(v);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, int position, @NonNull Model_notification model) {
        String le_temps_de_la_notification = model.getDate_du_like();
        holder.temps_de_la_notification.setText(le_temps_de_la_notification);
        String id_du_profil_qui_notifie = model.getId_de_utilisateur();
        String action =model.getAction();
        String commentaire =model.getCommantaire();
        String image_du_produit =model.getImage_du_produit();
        Picasso.with(context).load(image_du_produit).into(holder.image_du_produit);
        if (action.equals("commantaire")){
            holder.text_des_commentaires.setVisibility(View.VISIBLE);
            holder.image_des_commentaire.setVisibility(View.VISIBLE);
            holder.image_des_like.setVisibility(View.INVISIBLE);
            holder.text_des_likes.setVisibility(View.INVISIBLE);
            holder.text_des_commentaires.setText(commentaire);
        }else{
            holder.image_des_like.setVisibility(View.VISIBLE);
            holder.image_des_commentaire.setVisibility(View.INVISIBLE);
            holder.text_des_commentaires.setVisibility(View.INVISIBLE);
            holder.text_des_likes.setVisibility(View.VISIBLE);
        }
        firebaseFirestore.collection("mes donnees utilisateur").document(id_du_profil_qui_notifie).get().addOnCompleteListener((Activity) context,new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult ().exists ()){
                        String name_user= task.getResult ().getString ( "user_name" );
                        String user_prenom= task.getResult ().getString ( "user_prenom" );
                        String image_user=task.getResult ().getString ( "user_profil_image" );
                      holder.nom_du_profil.setText(name_user + " " + user_prenom);
                        Picasso.with(context).load(image_user).into(holder.image_du_profil);
                    }
                }else {
                    String error=task.getException().getMessage();
                    Toast.makeText(context,error,Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image_du_produit;
        ImageView image_des_like;
        CircleImageView image_du_profil;
        ImageView image_des_commentaire;
        TextView nom_du_profil;
        TextView text_des_likes;
        TextView text_des_commentaires;
        TextView temps_de_la_notification;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image_du_produit=itemView.findViewById(R.id.image_du_produit);
            image_des_like=itemView.findViewById(R.id.image_des_like);
            image_du_profil=itemView.findViewById(R.id.image_du_profil);
            image_des_commentaire=itemView.findViewById(R.id.image_des_commentaire);
            nom_du_profil=itemView.findViewById(R.id.nom_du_profil);
            text_des_likes=itemView.findViewById(R.id.text_des_likes);
            text_des_commentaires=itemView.findViewById(R.id.text_des_commentaires);
            temps_de_la_notification=itemView.findViewById(R.id.temps_de_la_notification);


        }
    }




}

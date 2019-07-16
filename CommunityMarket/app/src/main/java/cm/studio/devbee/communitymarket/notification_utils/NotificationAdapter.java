package cm.studio.devbee.communitymarket.notification_utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import cm.studio.devbee.communitymarket.R;
import cm.studio.devbee.communitymarket.messagerie.MessageActivity;
import cm.studio.devbee.communitymarket.postActivity.DetailActivityTwo;
import cm.studio.devbee.communitymarket.utilForChat.ModelChat;
import de.hdodenhof.circleimageview.CircleImageView;
import pl.droidsonroids.gif.GifImageView;

import static com.facebook.FacebookSdk.getApplicationContext;

public class NotificationAdapter extends FirestoreRecyclerAdapter<Model_notification,NotificationAdapter.ViewHolder> {

    Context context;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private String current_user;
    private String titreDuProduit;
    private String prixduproduit;
    private String name_user;
    private String user_prenom;
    private String action_faite;

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
        current_user=firebaseAuth.getCurrentUser ().getUid ();
        return new ViewHolder(v);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, int position, @NonNull Model_notification model) {
        String le_temps_de_la_notification = model.getDate_du_like();
        holder.temps_de_la_notification.setText(le_temps_de_la_notification);
        final String id_du_profil_qui_notifie = model.getId_de_utilisateur();
        String action =model.getAction();
        String commentaire ="<< "+model.getCommantaire()+" >>";
        final String image_du_produit =model.getImage_du_produit();
        final String categories =model.getCategories ();
        final String id_du_post=model.getId_du_post ();
        Picasso.with(context).load(image_du_produit).into(holder.image_du_produit);
        if (action.equals("commantaire")){
            holder.text_des_commentaires.setVisibility(View.VISIBLE);
            holder.image_des_commentaire.setVisibility(View.VISIBLE);
            holder.image_des_like.setVisibility(View.INVISIBLE);
            holder.text_des_likes.setVisibility(View.INVISIBLE);
            holder.text_des_commentaires.setText(commentaire);
            action_faite=" vous avez commenté";
        }else{
            holder.image_des_like.setVisibility(View.VISIBLE);
            holder.image_des_commentaire.setVisibility(View.INVISIBLE);
            holder.text_des_commentaires.setVisibility(View.INVISIBLE);
            holder.text_des_likes.setVisibility(View.VISIBLE);
            action_faite=" vous avez ajouter à vos favories";
            commentaire="";
        }
        firebaseFirestore.collection("mes donnees utilisateur").document(id_du_profil_qui_notifie).get().addOnCompleteListener((Activity) context,new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult ().exists ()){
                         name_user= task.getResult ().getString ( "user_name" );
                         user_prenom= task.getResult ().getString ( "user_prenom" );
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

        firebaseFirestore.collection ( "mes donnees utilisateur" ).document (current_user).collection ( "mes notification" ).document(id_du_post).get ().addOnCompleteListener ( (Activity) context,new OnCompleteListener<DocumentSnapshot> () {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful ()){
                    if (task.getResult ().exists ()){
                        String is_new_notification = task.getResult ().getString ( "is_new_notification" );
                        if (is_new_notification.equals ( "true" )){
                            holder.notification_enable.setVisibility ( View.VISIBLE );
                        }else{
                            holder.notification_enable.setVisibility ( View.INVISIBLE );
                        }
                    }
                }
            }
        } );

        holder . cardview_nnotification . setAnimation ( AnimationUtils. loadAnimation (context, R.anim.fade_transition_animation));
        holder.image_du_produit.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent ( context,DetailActivityTwo.class );
                intent.putExtra ( "id du post",id_du_post );
                intent.putExtra ( "id de l'utilisateur",current_user );
                intent.putExtra ( "id_categories",categories );
                context.startActivity ( intent );
            }
        } );

        final String finalCommentaire = commentaire;
        holder.image_lancerç_la_reponse.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                current_user=firebaseAuth.getCurrentUser ().getUid ();
                firebaseFirestore.collection ( "publication" ).document ( "categories" ).collection ( "nouveaux" ).document ( id_du_post ).get ().addOnCompleteListener ( (Activity) context,new OnCompleteListener<DocumentSnapshot> () {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        titreDuProduit = task.getResult ().getString ( "nom_du_produit" );
                        prixduproduit = task.getResult ().getString ( "prix_du_produit" );

                    }
                } );
                Map<String, String> donnees_utilisateur = new HashMap<> ();
                donnees_utilisateur.put("image_en_vente", image_du_produit);
                donnees_utilisateur.put("titre_produit", titreDuProduit);
                donnees_utilisateur.put("prix_produit", prixduproduit);

                firebaseFirestore.collection("sell_image").document(id_du_profil_qui_notifie).collection(current_user).document(current_user).set(donnees_utilisateur).addOnCompleteListener((Activity) context,new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
                firebaseFirestore.collection("sell_image").document(current_user).collection(id_du_profil_qui_notifie).document(current_user).set(donnees_utilisateur).addOnCompleteListener((Activity) context,new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
                DocumentReference user = firebaseFirestore.collection("sell_image").document(id_du_profil_qui_notifie).collection(current_user).document(id_du_profil_qui_notifie);
                user.update("image_en_vente", image_du_produit)
                        .addOnSuccessListener((Activity) context,new OnSuccessListener<Void> () {
                            @Override
                            public void onSuccess(Void aVoid) {
                            }
                        })
                        .addOnFailureListener((Activity) context,new OnFailureListener () {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        });

                Intent gotoMessage = new Intent(context, MessageActivity.class);
                gotoMessage.putExtra("id du post", id_du_post);
                gotoMessage.putExtra("id de l'utilisateur", id_du_profil_qui_notifie);
                gotoMessage.putExtra("id_categories", categories);
                gotoMessage.putExtra("image_en_vente", image_du_produit);
                gotoMessage.putExtra ( "id_recepteur",current_user );
                gotoMessage.putExtra ( "viens_de_detail","vrai" );
                gotoMessage.putExtra ( "viens_de_notification","vrai" );
                String mon_post_test="ma publication";
                String parler_davantage ="pouvons nous en parler d'avantage ?";
                String je_vous_ferais_un_bon_prix=" je vous ferais un bon prix promis .";
                gotoMessage.putExtra ( "contenu",name_user + " " + user_prenom + " : "+action_faite+" "+" "+mon_post_test+" :"+  " " + finalCommentaire +" \n"+parler_davantage + "\n" +je_vous_ferais_un_bon_prix);
                context.startActivity ( gotoMessage );

            }
        } );
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image_du_produit;
        GifImageView image_des_like;
        CircleImageView image_du_profil;
        ImageView image_des_commentaire;
        TextView nom_du_profil;
        TextView text_des_likes;
        TextView text_des_commentaires;
        TextView temps_de_la_notification;
        ConstraintLayout notification_container;
        ImageButton image_lancerç_la_reponse;
        CardView cardview_nnotification;
        CircleImageView notification_enable;
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
            notification_container=itemView.findViewById ( R.id.notification_container );
            image_lancerç_la_reponse=itemView.findViewById ( R.id.image_lancerç_la_reponse );
            notification_enable=itemView.findViewById ( R.id.notification_enable );
            cardview_nnotification=itemView.findViewById ( R.id.cardview_nnotification );


        }
    }




}

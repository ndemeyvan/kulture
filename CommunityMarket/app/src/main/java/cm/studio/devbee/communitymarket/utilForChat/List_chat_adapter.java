package cm.studio.devbee.communitymarket.utilForChat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import java.util.List;
import cm.studio.devbee.communitymarket.R;
import cm.studio.devbee.communitymarket.messagerie.MessageActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.facebook.FacebookSdk.getApplicationContext;

public class List_chat_adapter extends RecyclerView.Adapter<List_chat_adapter.ViewHolder> {
    List<DiplayAllChat> diplayAllChatList;
    Context context;
    String current_user;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    public List_chat_adapter(List<DiplayAllChat> diplayAllChatList, Context context) {
        this.diplayAllChatList = diplayAllChatList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        firebaseAuth =FirebaseAuth.getInstance ();
        firebaseFirestore=FirebaseFirestore.getInstance ();
        View v= LayoutInflater.from ( viewGroup.getContext () ).inflate ( R.layout.item_contact_chat,viewGroup,false );
        return new ViewHolder ( v );
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final String id_expediteur_lien =diplayAllChatList.get ( i ).getId_expediteur ();
        final String id_recepteu_lien =diplayAllChatList.get ( i ).getId_recepteur ();
        String image_profil=diplayAllChatList.get ( i ).getImage_profil ();
        final CircleImageView profil=viewHolder.itemView.findViewById ( R.id.chat_message_image_profil );
        String nom = diplayAllChatList.get ( i ).getNom_utilisateur ();
        current_user=firebaseAuth.getCurrentUser ().getUid ();
        String temps=diplayAllChatList.get ( i ).getTemps ();
        viewHolder.temps.setText ( temps );
        ConstraintLayout chat_container=viewHolder.itemView.findViewById ( R.id.chat_container );
        chat_container.setAnimation ( AnimationUtils.loadAnimation ( getApplicationContext (),R.anim.fade_transition_animation ) );
        String dernier_message = diplayAllChatList.get ( i ).getDernier_message ();
        viewHolder.lats_message.setText ( dernier_message );
        viewHolder.nom_utilisateur.setText ( nom );
        String lu=diplayAllChatList.get ( i ).getLu ();
        Picasso.with ( context ).load ( image_profil ).into ( profil );
        chat_container.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                Intent chat =new Intent ( getApplicationContext (),MessageActivity.class );
                chat.putExtra ( "id de l'utilisateur" ,id_recepteu_lien);
                chat.putExtra ( "id_recepteur" ,id_expediteur_lien);
                chat.putExtra ( "viens_de_detail","vrai" );
                chat.putExtra ( "viens_de_notification","faux" );
                chat.putExtra ( "contenu","" );
                chat.putExtra("id du post","");

                context.startActivity ( chat );
                ((Activity) context).finish ();
            }
        } );
        if (current_user.equals ( id_expediteur_lien )){
            firebaseFirestore.collection("mes donnees utilisateur").document(id_recepteu_lien).get().addOnCompleteListener( (Activity) context,new OnCompleteListener<DocumentSnapshot> () {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()){
                        if (task.getResult ().exists ()){
                            String prenom=task.getResult ().getString ( "user_prenom" );
                            String name_user= task.getResult ().getString ( "user_name" );
                            String lien_profil_contact =task.getResult ().getString ( "user_profil_image" );
                            String status =task.getResult ().getString ( "status" );
                                if (status.equals ( "online" )){
                                    viewHolder.online.setVisibility ( View.VISIBLE );
                                    viewHolder.offline.setVisibility ( View.INVISIBLE );

                                }else {
                                    viewHolder.online.setVisibility ( View.INVISIBLE );
                                    viewHolder.offline.setVisibility ( View.VISIBLE );

                                }
                            Picasso.with ( context ).load ( lien_profil_contact ).into ( profil );
                            viewHolder.nom_utilisateur.setText (name_user +" "+ prenom);
                        }
                    }else {
                        String error=task.getException().getMessage();
                        Toast.makeText ( getApplicationContext (), error, Toast.LENGTH_LONG ).show ();
                    }
                }
            });
        }else{
            viewHolder.nom_utilisateur.setText ( nom );
            Picasso.with ( context ).load ( image_profil ).into ( profil );

        }

        if (lu.equals ( "non lu" )){
            viewHolder.new_message_image.setVisibility ( View.VISIBLE );
        }

    }

    @Override
    public int getItemCount() {
        return diplayAllChatList.size ();
    }
    TextView lats_message;
    TextView nom_utilisateur;
    TextView lu_non_lu;
    TextView id_recepteur;
    CircleImageView online ;
    CircleImageView offline;
    TextView temps;
    ConstraintLayout chat_container;
    CircleImageView profi;
    CircleImageView new_message_image;
    public class ViewHolder extends  RecyclerView.ViewHolder{
        final TextView lats_message=itemView.findViewById ( R.id.chat_last_message );
        final TextView nom_utilisateur=itemView.findViewById ( R.id.chat_user_name );
        final TextView lu_non_lu=itemView.findViewById ( R.id.lu_non );
        TextView id_recepteur=itemView.findViewById(R.id.id_recepteur);
        final CircleImageView online =itemView.findViewById ( R.id.online );
        final CircleImageView offline=itemView.findViewById ( R.id.offline );
        final TextView temps=itemView.findViewById ( R.id.chat_temps );
        final CircleImageView profil=itemView.findViewById ( R.id.chat_message_image_profil );
        final CircleImageView new_message_image=itemView.findViewById ( R.id.new_message_image );

        public ViewHolder(@NonNull View itemView) {
            super ( itemView );
            chat_container=itemView.findViewById ( R.id.chat_container );
        }
    }
}

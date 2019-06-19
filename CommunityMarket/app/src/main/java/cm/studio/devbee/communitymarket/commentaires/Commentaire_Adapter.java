package cm.studio.devbee.communitymarket.commentaires;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import cm.studio.devbee.communitymarket.postActivity.UserGeneralPresentation;
import cm.studio.devbee.communitymarket.profile.ProfileActivity;
import de.hdodenhof.circleimageview.CircleImageView;

public class Commentaire_Adapter extends RecyclerView.Adapter<Commentaire_Adapter.ViewHolder> {

    List<Commentaires_Model> commentaires_modelList;
    Context context;
    String statut;

    String current_user;
   String id;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    public Commentaire_Adapter(List<Commentaires_Model> commentaires_modelList, Context context) {
        this.commentaires_modelList = commentaires_modelList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_commentaire,viewGroup,false);
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
         final String get_id=commentaires_modelList.get(i).getId_user();
        String get_content=commentaires_modelList.get(i).getContenu();
        String get_temps=commentaires_modelList.get(i).getHeure();
        current_user=firebaseAuth.getCurrentUser().getUid();
        firebaseFirestore.collection("mes donnees utilisateur").document(get_id).get().addOnCompleteListener((Activity) context,new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult ().exists ()){
                        statut= task.getResult ().getString ( "status" );
                        String name_user= task.getResult ().getString ( "user_name" );
                        String image_user=task.getResult ().getString ( "user_profil_image" );
                        viewHolder.setuserData ( name_user,image_user );

                       /* if (statut.equals("online")){
                            viewHolder.online.setVisibility(View.VISIBLE);
                            viewHolder.offline.setVisibility(View.INVISIBLE);
                        }else {
                            viewHolder.online.setVisibility(View.INVISIBLE);
                            viewHolder.offline.setVisibility(View.VISIBLE);
                        }*/
                    }
                }else {
                    String error=task.getException().getMessage();
                    Toast.makeText(context,error,Toast.LENGTH_LONG).show();
                }
            }
        });
        viewHolder.setcomment(get_content);
        viewHolder.set_temps(get_temps);
        viewHolder.profil_comment_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!get_id.equals(current_user)) {
                    Intent gotoprofil_user=new Intent(context,UserGeneralPresentation.class);
                    gotoprofil_user.putExtra("id de l'utilisateur",get_id);
                    context.startActivity(gotoprofil_user);

                }else{
                    Intent gotoprofil_usertwo=new Intent(context,ProfileActivity.class);
                    //gotoprofil_user.putExtra("id de l'utilisateur",get_id);
                    context.startActivity(gotoprofil_usertwo);
                }


            }
        });

    }

    @Override
    public int getItemCount() {
        return commentaires_modelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView profil_comment_user;
        TextView profil_user_name;
        TextView profil_comment_last_message;
        TextView temps_commentaire;
         CircleImageView online;
        CircleImageView offline;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profil_comment_user=itemView.findViewById(R.id.chat_message_image_profil);
            profil_user_name=itemView.findViewById(R.id.chat_user_name);
            profil_comment_last_message=itemView.findViewById(R.id.chat_last_message);
            temps_commentaire=itemView.findViewById(R.id.chat_temps);
            online=itemView.findViewById(R.id.online);
            offline=itemView.findViewById(R.id.offline);
        }
        public void setuserData(String name,String image){
            profil_user_name.setText ( name );
            Picasso.with(context).load(image).into (profil_comment_user );
        }
        public void setcomment(String comment){
            profil_comment_last_message.setText(comment);
        }
        public void set_temps(String temps){
            temps_commentaire.setText(temps);
        }

    }
}

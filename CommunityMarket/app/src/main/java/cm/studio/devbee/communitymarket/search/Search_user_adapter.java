package cm.studio.devbee.communitymarket.search;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

import cm.studio.devbee.communitymarket.R;
import cm.studio.devbee.communitymarket.gridView_post.ModelGridView;
import cm.studio.devbee.communitymarket.postActivity.SellActivityUser;
import de.hdodenhof.circleimageview.CircleImageView;

public class Search_user_adapter extends FirestoreRecyclerAdapter<Seach_user_model,Search_user_adapter.ViewHolder> {
    List<Seach_user_model> modelGridViewList;
    Context context;
    String current_user_id;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private String residence;
    private String nom_user;
    private String prenom;
    private String description;
    private String image_profil;
    private String id_utilisateur;

    public Search_user_adapter(@NonNull FirestoreRecyclerOptions<Seach_user_model> options,Context context) {
        super ( options );
        this.context=context;
    }

    @Override
    protected void onBindViewHolder(@NonNull final Search_user_adapter.ViewHolder viewHolder, int position, @NonNull Seach_user_model model) {
        firebaseFirestore.collection ( "mes donnees utilisateur" ).document ( current_user_id ).get ().addOnCompleteListener ( new OnCompleteListener<DocumentSnapshot> () {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.getResult ().exists ()){
                     residence= task.getResult ().getString ( "user_residence" );
                     nom_user = task.getResult ().getString ("user_name");
                     prenom = task.getResult ().getString ("user_prenom");
                     description = task.getResult ().getString ("user_mail");
                     image_profil = task.getResult ().getString ("user_profil_image");
                     id_utilisateur = task.getResult ().getString ("id_utilisateur");
                    viewHolder.user_description.setText ( description );
                    viewHolder.user_location.setText ( residence );
                    viewHolder.user_name.setText ( nom_user +" " + prenom );
                    Picasso.with ( context ).load ( image_profil ).into ( viewHolder.parametre_image );
                }
            }
        } );
        viewHolder.user_container.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                Intent gotosell = new Intent ( context,SellActivityUser.class );
                gotosell.putExtra (  "id de l'utilisateur",id_utilisateur);
                context.startActivity ( gotosell );
            }
        } );

    }

    @NonNull
    @Override
    public Search_user_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        current_user_id=firebaseAuth.getCurrentUser ().getUid ();
        View v=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_user_layout,viewGroup,false);
        return new ViewHolder ( v );
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView parametre_image;
        TextView user_name;
        TextView user_description;
        TextView user_location;
        ConstraintLayout user_container;
        public ViewHolder(@NonNull View itemView) {
            super ( itemView );
            parametre_image=itemView.findViewById ( R.id.parametre_image );
            user_name=itemView.findViewById ( R.id.user_name );
            user_description=itemView.findViewById ( R.id.user_description );
            user_location=itemView.findViewById ( R.id.user_location );
            user_container=itemView.findViewById ( R.id.user_container );
        }
    }
}

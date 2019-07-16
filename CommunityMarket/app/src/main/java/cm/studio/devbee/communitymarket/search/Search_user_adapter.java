package cm.studio.devbee.communitymarket.search;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
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

import java.util.ArrayList;
import java.util.List;

import cm.studio.devbee.communitymarket.R;
import cm.studio.devbee.communitymarket.gridView_post.ModelGridView;
import cm.studio.devbee.communitymarket.postActivity.SellActivityUser;
import de.hdodenhof.circleimageview.CircleImageView;

public class Search_user_adapter extends RecyclerView.Adapter<Search_user_adapter.ViewHolder>implements Filterable {
    List<Seach_user_model> modelGridViewList;
    List<Seach_user_model>modelGridViewListTwoFiltered;

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


    public Search_user_adapter(List<Seach_user_model> modelGridViewList, Context context) {
        this.modelGridViewList = modelGridViewList;
        this.context = context;
        this.modelGridViewListTwoFiltered = modelGridViewList;

    }

    public Search_user_adapter(List<Seach_user_model> modelGridViewList, Context context, String prenom) {
        this.modelGridViewList = modelGridViewList;
        this.context = context;
        this.prenom = prenom;
        this.modelGridViewListTwoFiltered = modelGridViewList;

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

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
         nom_user = modelGridViewListTwoFiltered.get ( i ).getUser_name ();
        prenom=modelGridViewListTwoFiltered.get ( i ).getUser_prenom ();
        final String id =modelGridViewListTwoFiltered.get ( i ).getId_utilisateur ();
        viewHolder.user_name.setText ( nom_user +" " + prenom );
        firebaseFirestore.collection ( "mes donnees utilisateur" ).document ( id ).get ().addOnCompleteListener ( new OnCompleteListener<DocumentSnapshot> () {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.getResult ().exists ()){
                    residence= task.getResult ().getString ( "user_residence" );
                    //nom_user = task.getResult ().getString ("user_name");
                   // prenom = task.getResult ().getString ("user_prenom");
                    description = task.getResult ().getString ("user_mail");
                    image_profil = task.getResult ().getString ("user_profil_image");
                    viewHolder.user_description.setText ( description );
                    viewHolder.user_location.setText ( residence );
                    Picasso.with ( context ).load ( image_profil ).into ( viewHolder.parametre_image );
                }
            }
        } );
        viewHolder.user_container.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                Intent gotosell = new Intent ( context,SellActivityUser.class );
                gotosell.putExtra (  "id de l'utilisateur",id);
                context.startActivity ( gotosell );
            }
        } );

    }


    @Override
    public int getItemCount() {
        return modelGridViewListTwoFiltered.size ();
    }
    @Override
    public Filter getFilter() {
        return new Filter () {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String key =constraint.toString ();
                if (key.isEmpty ()){
                    modelGridViewListTwoFiltered=modelGridViewList;
                }else {
                    List<Seach_user_model> isfiltered = new ArrayList<> (  );
                    for (Seach_user_model row : modelGridViewList){
                        if (row.getUser_name ().toLowerCase ().contains ( key.toLowerCase () )){
                            isfiltered.add ( row );
                        }
                    }
                    modelGridViewListTwoFiltered=isfiltered;
                }
                FilterResults filterResults = new FilterResults ();
                filterResults.values=modelGridViewListTwoFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                modelGridViewListTwoFiltered= (List<Seach_user_model>)results.values;
                notifyDataSetChanged ();
            }
        };
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

package cm.studio.devbee.communitymarket.utilForPost;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import cm.studio.devbee.communitymarket.R;
import cm.studio.devbee.communitymarket.postActivity.PostActivity;
import cm.studio.devbee.communitymarket.postActivity.PostActivityFinal;

public class CategoriesAdaptePost extends RecyclerView.Adapter<CategoriesAdaptePost.ViewHolder> {
   public  List<CategoriesModelPost> categoriesModelList;
   public  Context context;

    public CategoriesAdaptePost(List<CategoriesModelPost> categoriesModelList, Context context) {
        this.categoriesModelList = categoriesModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v=LayoutInflater.from ( viewGroup.getContext () ).inflate (R.layout.item_post_categorie ,viewGroup,false);
        return new ViewHolder ( v );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        //viewHolder.categories_text.setText ( categoriesModelList.get ( i ).getPost_titre_categories () );
        final String desc =categoriesModelList.get ( i).getPost_titre_categories ();
        viewHolder.choix_des_categories_container.setAnimation ( AnimationUtils.loadAnimation ( context,R.anim.fade_scale_animation ) );
        viewHolder.image_categories.setImageResource ( categoriesModelList.get ( i ).getPost_image_categories () );
        viewHolder.setNom ( desc );
        viewHolder.choix_des_categories_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent categoryIntent=new Intent ( context,PostActivityFinal.class );
                categoryIntent.putExtra ( "categoryName",desc );
                context.startActivity(categoryIntent);
               ((Activity)context).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoriesModelList.size ();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image_categories;
        TextView categories_text;
        ConstraintLayout choix_des_categories_container;
        public ViewHolder(@NonNull View itemView) {
            super ( itemView );
            image_categories=itemView.findViewById ( R.id.post_cat_image );
            categories_text=itemView.findViewById ( R.id.post_cat_name );
            choix_des_categories_container=itemView.findViewById ( R.id.choix_des_categories_container );
        }
        public void setNom(final String name){
            categories_text.setText ( name );

        }

    }



}

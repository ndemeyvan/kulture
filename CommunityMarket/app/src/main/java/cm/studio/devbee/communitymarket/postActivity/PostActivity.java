package cm.studio.devbee.communitymarket.postActivity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import cm.studio.devbee.communitymarket.Accueil;
import cm.studio.devbee.communitymarket.R;
import cm.studio.devbee.communitymarket.utilForPost.CategoriesAdaptePost;
import cm.studio.devbee.communitymarket.utilForPost.CategoriesModelPost;


public class PostActivity extends AppCompatActivity {
    public static android.support.v7.widget.Toolbar toolbar_post;
    public static RecyclerView post_cat_recycler;
    public static CategoriesAdaptePost categoriesAdaptePost;
    public static List<CategoriesModelPost> categoriesModelPostList;
    public static WeakReference<PostActivity> postActivityWeakReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_post );
        toolbar_post=findViewById ( R.id.post_cat_toolbar );
        setSupportActionBar ( toolbar_post );
        getSupportActionBar ().setTitle ( getString(R.string.choix_de_categorie));
        getSupportActionBar ().setDisplayHomeAsUpEnabled ( true );
        toolbar_post.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PostActivity.this,Accueil.class);
                startActivity (i);
                finish ();
            }
        });
        post_cat_recycler=findViewById ( R.id.post_cat_recycler );
        categoriesModelPostList=new ArrayList<> (  );
        categoriesModelPostList.add ( new CategoriesModelPost ( "Mode",R.drawable.shoes ) );
        categoriesModelPostList.add ( new CategoriesModelPost ( "Phone et Tablette",R.drawable.jupes ) );
        categoriesModelPostList.add ( new CategoriesModelPost ( "Jeux et console",R.drawable.pantalons ) );
        categoriesModelPostList.add ( new CategoriesModelPost ( "Electronique",R.drawable.accessoires ) );
        categoriesModelPostList.add ( new CategoriesModelPost ( "Informatique",R.drawable.cullotes ) );
        categoriesModelPostList.add ( new CategoriesModelPost ( "Beaute",R.drawable.pantalons ) );

        postActivityWeakReference=new WeakReference<>(this);
        categoriesAdaptePost=new CategoriesAdaptePost (categoriesModelPostList,PostActivity.this);
        post_cat_recycler.setAdapter ( categoriesAdaptePost );
        post_cat_recycler.setLayoutManager ( new LinearLayoutManager ( getApplicationContext(),LinearLayoutManager.VERTICAL ,false) );
        AnimationDrawable animationDrawableOne = (AnimationDrawable) toolbar_post.getBackground();
        animationDrawableOne.setEnterFadeDuration(2000);
        animationDrawableOne.setExitFadeDuration(4000);
        animationDrawableOne.start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed ();
        Intent intent=new Intent ( getApplicationContext (),Accueil.class );
        startActivity ( intent );
        finish ();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        toolbar_post=null;
        post_cat_recycler=null;
        categoriesAdaptePost=null;
        categoriesModelPostList=null;
    }


}

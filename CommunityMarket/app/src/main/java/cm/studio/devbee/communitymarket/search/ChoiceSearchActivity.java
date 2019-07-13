package cm.studio.devbee.communitymarket.search;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import cm.studio.devbee.communitymarket.R;

public class ChoiceSearchActivity extends AppCompatActivity {
    CardView card_utilisateur;
    CardView card_article;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_choice_search );
        card_utilisateur=findViewById ( R.id.card_utilisateur );
        card_article=findViewById ( R.id.card_article );
        toolbar=findViewById ( R.id.toolbar );
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("what do you want");
        getSupportActionBar ().setDisplayHomeAsUpEnabled ( true );
        toolbar.setNavigationOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                //startActivity ( new Intent ( getApplicationContext (),Accueil.class ).setFlags ( Intent.FLAG_ACTIVITY_CLEAR_TOP ) );
                finish ();
            }
        } );
        card_article.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent ( ChoiceSearchActivity.this,SearchActivity.class );
                startActivity ( intent );
            }
        } );
        card_utilisateur.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {

            }
        } );
        AnimationDrawable animationDrawableOne = (AnimationDrawable) toolbar.getBackground();
        animationDrawableOne.setEnterFadeDuration(2000);
        animationDrawableOne.setExitFadeDuration(4000);
        animationDrawableOne.start();
    }
}

package cm.studio.devbee.communitymarket;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.squareup.picasso.Picasso;

import cm.studio.devbee.communitymarket.profile.ParametrePorfilActivity;

public class PublicityActivity extends AppCompatActivity {
    String imageOne;
    String imageTwo;
    String imageThree;
    String lieu;
    String contact;
    String name;
    String desc;
    private static ImageView imageOnee,imageTwoo,imageThreee;
    private static TextView img1,img2,img3,img4;
    TextView publicity_entreprise_name;
    TextView publicity_description;
    TextView publicity_location_entreprise;
    TextView publicity_contact;
    Button publicity_call_button;
    String telephone;
    Dialog myDialog;
    private static ViewFlipper viewFlipper;
    Toolbar rise_toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publicity);
        rise_toolbar=findViewById(R.id.toolbarDetail);
        setSupportActionBar(rise_toolbar);
        getSupportActionBar ().setDisplayHomeAsUpEnabled ( true );
        rise_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity ( new Intent ( getApplicationContext (),Accueil.class ).setFlags ( Intent.FLAG_ACTIVITY_CLEAR_TOP ) );
                finish ();

            }
        });
        viewFlipper=findViewById(R.id.viewFlipper);
        viewFlipper.setOutAnimation(getApplicationContext(),android.R.anim.slide_out_right);
        viewFlipper.setInAnimation(getApplicationContext(),android.R.anim.slide_in_left);
        ShowPopup();
        imageOne =getIntent().getExtras().getString("imageOne");
        imageTwo =getIntent().getExtras().getString("imageTwo");
        imageThree =getIntent().getExtras().getString("imageThree");
        lieu =getIntent().getExtras().getString("lieu");
        contact =getIntent().getExtras().getString("contact");
        name =getIntent().getExtras().getString("name");
        desc =getIntent().getExtras().getString("desc");
        imageOnee=findViewById(R.id.imageSlideOne);
        Picasso.with(getApplicationContext()).load(imageOne).into(imageOnee);
        imageTwoo=findViewById(R.id.imageSlideTwo);
        Picasso.with(getApplicationContext()).load(imageTwo).into(imageTwoo);
        imageThreee=findViewById(R.id.imageSlideThree);
        Picasso.with(getApplicationContext()).load(imageThree).into(imageThreee);
        img1=findViewById(R.id.img1);
        img2=findViewById(R.id.img2);
        img3=findViewById(R.id.img3);
        img4=findViewById(R.id.img4);
        publicity_entreprise_name=findViewById(R.id.publicity_entreprise_name);
        publicity_description=findViewById(R.id.publicity_description);
        publicity_location_entreprise=findViewById(R.id.publicity_location_entreprise);
        publicity_contact=findViewById(R.id.publicity_contact);
        publicity_call_button=findViewById(R.id.publicity_call_button);

        publicity_entreprise_name.setText(name);
        publicity_description.setText(desc);
        publicity_location_entreprise.setText(lieu);
        publicity_contact.setText(contact);
        telephone=contact;
        myDialog.dismiss();
        publicity_call_button.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                String uri = "tel:"+telephone ;
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(uri));
                startActivity(intent);
            }
         } );
        }

    public void ShowPopup(){
        myDialog=new Dialog(this);
        myDialog.setContentView(R.layout.load_pop_pup);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.setCancelable(false);
        myDialog.show();
    }
}

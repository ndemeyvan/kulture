package cm.studio.devbee.communitymarket.messagerie;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cm.studio.devbee.communitymarket.MyFirebaseMessagingService;
import cm.studio.devbee.communitymarket.MySingleton;
import cm.studio.devbee.communitymarket.R;
import cm.studio.devbee.communitymarket.utilForChat.ChatAdapter;
import cm.studio.devbee.communitymarket.utilForChat.DiplayAllChat;
import cm.studio.devbee.communitymarket.utilForChat.ModelChat;
import de.hdodenhof.circleimageview.CircleImageView;
public class MessageActivity extends AppCompatActivity {
    private static CircleImageView user_message_image;
    private static TextView user_name;
    private static String randomKey;
    private static Intent intent ;
    public static  String user_id_message;
    private static String current_user;
    private static FirebaseFirestore firebaseFirestore;
    private static FirebaseAuth firebaseAuth;
    private static Toolbar mesage_toolbar;
    private static ImageButton send_button;
    private static EditText message_user_send;
    private static RecyclerView message_recyclerview;
    private static String lien_profil_contact;
    private static DiplayAllChat contact;
    private static String nom_utilisateur;
    private static String saveCurrentDate;
    private static ChatAdapter chatAdapter;
    private static List<ModelChat> modelChatList;
    private static  CircleImageView online_status;
    private static CircleImageView offline_status;
    private static DatabaseReference reference;
    private static ImageView image_de_discutions;
    private static  String lien_image;
    private static  String image;
    private static ProgressBar message_progressbar;
    private static String id_recepteur;
    private static  ImageView image_de_fond;
    private  static  TextView prix_produit;
    private static  TextView titre_produit;
    private static  boolean is_open=false;
    private String viens_detail;
    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key="+"AAAAfR8BveM:APA91bEgCOmnLz5LKrc4ms8qvCBYqAUwbXpoMswSYyuQJT0cg3FpLSvH-S_nAiaCARSdeolPbGpxTX5nHVm5AP6tI7N9sCYEL4IUkR_eF4lYZXN4oeXhWtCKavTHIaA8pH6eklL4yBO5";
    final private String contentType = "application/json";
    final String TAG = "NOTIFICATION TAG";
    String NOTIFICATION_TITLE;
    String NOTIFICATION_MESSAGE;
    String TOPIC;
    private String prenom;
    private String name_user;
    private String mon_preom;
    private String mon_nom;
    private String lien_image_profil;
    private String image_du_produit;
    private String lien_du_produit;
    private String viens_de_notification;
    private String contenu;
    private String iddupost;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_message );
        user_message_image=findViewById ( R.id.image_user_message );
        user_name=findViewById ( R.id.user_messag_name );
        firebaseAuth=FirebaseAuth.getInstance ();
        mesage_toolbar=findViewById ( R.id.message_toolbar );
        setSupportActionBar ( mesage_toolbar );
        message_progressbar=findViewById(R.id.message_progressbar);
        getSupportActionBar ().setDisplayHomeAsUpEnabled ( true );
        intent=getIntent (  );
        titre_produit=findViewById(R.id.titre_produit);
        modelChatList=new ArrayList<> (  );
        image_de_fond=findViewById(R.id.image_de_fond);
        firebaseFirestore=FirebaseFirestore.getInstance();
        current_user=firebaseAuth.getCurrentUser ().getUid ();
        iddupost =getIntent().getExtras().getString("id du post");
        user_id_message=intent.getStringExtra ( "id de l'utilisateur" );
        lien_image=intent.getStringExtra ( "image_en_vente" );
        id_recepteur=intent.getStringExtra ( "id_recepteur" );
        viens_detail=getIntent ().getStringExtra ( "viens_de_detail" );
        viens_de_notification=getIntent ().getStringExtra ( "viens_de_notification" );
        contenu=getIntent ().getStringExtra ( "contenu" );
        send_button=findViewById ( R.id.image_lancerç_la_reponse );
        message_user_send=findViewById ( R.id.user_message_to_send );
        message_recyclerview=findViewById ( R.id.message_recyclerView );
        image_de_discutions=findViewById ( R.id.image_de_discutions );
        prix_produit=findViewById(R.id.prix_produit);
        message_recyclerview.setHasFixedSize ( true );
        //image_en_fond=findViewById ( R.id.image_en_fond );
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager ( MessageActivity.this );
        linearLayoutManager.setStackFromEnd ( true );
        message_recyclerview.setLayoutManager ( linearLayoutManager );
        nomEtImageProfil ();
        online_status=findViewById ( R.id.online_status_image );
        offline_status=findViewById ( R.id.offline_status_image );
        if (viens_de_notification.equals ( "vrai" )){
            message_user_send.setText ( contenu );
        }
        mesage_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotochatlist=new Intent(getApplicationContext(),ChatMessageActivity.class);
                if (viens_detail.equals ( "vrai" )){
                    gotochatlist.putExtra ( "viens","" );
                    gotochatlist.putExtra ( "ouvert","ouvert" );
                    gotochatlist.putExtra ( "viens_de_detail","vrai" );
                    startActivity(gotochatlist);
                    finish();
                }else{
                    gotochatlist.putExtra ( "viens", "acceuil" );
                    gotochatlist.putExtra ( "viens_de_detail","faux" );
                    gotochatlist.putExtra ( "ouvert","ouvert" );
                    startActivity(gotochatlist);
                    finish();
                }

            }
        });

        send_button.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                send_button.setAnimation ( AnimationUtils.loadAnimation ( getApplicationContext (),R.anim.fade_transition_animation ) );
                final String message =message_user_send.getText ().toString ();
                if(!TextUtils.isEmpty ( message )){
                    sendmessage (current_user,user_id_message,message);
                    DocumentReference user = firebaseFirestore.collection("mes donnees utilisateur" ).document(user_id_message);
                    user.update("message_du_notifieur", message)
                            .addOnSuccessListener(new OnSuccessListener<Void> () {
                                @Override
                                public void onSuccess(Void aVoid) {
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                }
                            });



                }else{
                    Toast.makeText ( getApplicationContext (),getString(R.string.message_vide),Toast.LENGTH_LONG ).show ();
                }
                message_user_send.setText ( "" );


                /////test noti
                firebaseFirestore.collection("mes donnees utilisateur").document(current_user).get().addOnCompleteListener(MessageActivity.this,new OnCompleteListener<DocumentSnapshot> () {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            if (task.getResult ().exists ()){
                                String prenom=task.getResult ().getString ( "user_prenom" );
                                String name_user= task.getResult ().getString ( "user_name" );
                                String image_user=task.getResult ().getString ( "user_profil_image" );
                                lien_profil_contact =task.getResult ().getString ( "user_profil_image" );
                                nom_utilisateur=task.getResult ().getString ( "user_name" );
                                String status=task.getResult ().getString ( "status" );
                                TOPIC = "/topics/"+user_id_message; //topic has to match what the receiver subscribed to
                                NOTIFICATION_MESSAGE =message;
                                JSONObject notification = new JSONObject();
                                JSONObject notifcationBody = new JSONObject ();
                                try {
                                    notifcationBody.put("title", name_user +" "+prenom);
                                    notifcationBody.put("message", NOTIFICATION_MESSAGE);
                                    notifcationBody.put("id", user_id_message);
                                    notifcationBody.put ( "viens_de_detail","faux" );
                                    notifcationBody.put ( "id_recepteur",user_id_message );
                                    notifcationBody.put ( "image_en_vente",lien_image );
                                    notifcationBody.put ( "id_expediteur" ,current_user);
                                    notification.put("to", TOPIC);
                                    notification.put("data", notifcationBody);
                                    notifcationBody.put ( "viens_de_detail","faux" );
                                } catch (JSONException e) {
                                }
                                sendNotification(notification);

                                ////end test noti
                            }
                        }else {
                            String error=task.getException().getMessage();
                            Toast.makeText ( getApplicationContext (), error, Toast.LENGTH_LONG ).show ();

                        }
                    }
                });

            }
        } );



        Calendar calendar=Calendar.getInstance ();
        SimpleDateFormat currentDate=new SimpleDateFormat (" dd MMM yyyy" );
        String saveCurrentDate=currentDate.format ( calendar.getTime () );
        String randomKey=saveCurrentDate;
        DocumentReference user = firebaseFirestore.collection("mes donnees utilisateur" ).document(current_user);
        user.update("derniere_conection", randomKey)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });

        firebaseFirestore.collection ( "sell_image" ).document ( user_id_message ).collection ( current_user ).document (user_id_message).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult ().exists ()){
                        image= task.getResult ().getString ( "image_en_vente" );
                        Picasso.with ( MessageActivity.this ).load ( image ).into ( image_de_discutions );
                    }else{

                    }
                }else {
                    String error=task.getException().getMessage();
                }
            }
        });

        firebaseFirestore.collection ( "sell_image" ).document ( current_user ).collection ( user_id_message ).document (current_user).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult ().exists ()){
                        image= task.getResult ().getString ( "image_en_vente" );
                        String prix = task.getResult ().getString ( "prix_produit" );
                        String titre = task.getResult ().getString ( "titre_produit" );
                        prix_produit.setText(prix);
                        titre_produit.setText(titre);
                        prix_produit.setAnimation ( AnimationUtils.loadAnimation ( getApplicationContext (),R.anim.fade_transition_animation ) );
                        titre_produit.setAnimation ( AnimationUtils.loadAnimation ( getApplicationContext (),R.anim.fade_transition_animation ) );
                        Picasso.with ( MessageActivity.this ).load ( image ).into ( image_de_discutions );
                        Picasso.with ( MessageActivity.this ).load ( image ).into (   image_de_fond );

                        image_de_discutions.setAnimation ( AnimationUtils.loadAnimation ( getApplicationContext (),R.anim.fade_transition_animation ) );
                    }else{

                    }
                }else {
                    String error=task.getException().getMessage();
                }
            }
        });

        userstatus("online");
        //Toast.makeText ( getApplicationContext (),"l'image qui porte sur la vente apparait en haut",Toast.LENGTH_LONG ).show ();
        AnimationDrawable animationDrawableOne = (AnimationDrawable) mesage_toolbar.getBackground();
        animationDrawableOne.setEnterFadeDuration(2000);
        animationDrawableOne.setExitFadeDuration(4000);
        animationDrawableOne.start();
        stopService(new Intent(MessageActivity.this,MyFirebaseMessagingService.class));

        DocumentReference userTwo = firebaseFirestore.collection ( "mes donnees utilisateur" ).document (current_user).collection ( "mes notification" ).document(iddupost);
        userTwo.update("is_new_notification", "false")
                .addOnSuccessListener(new OnSuccessListener<Void> () {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                })
                .addOnFailureListener(new OnFailureListener () {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }

    private void sendNotification(JSONObject notification) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest (FCM_API, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "onResponse: " + response.toString());

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MessageActivity.this, "Request error", Toast.LENGTH_LONG).show();
                        Log.i(TAG, "onErrorResponse: Didn't work");
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }





    public void userstatus(String status){
        DocumentReference user = firebaseFirestore.collection("mes donnees utilisateur" ).document(current_user);
        user.update("status", status)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });

    }

    @Override
    protected void onResume() {
        super.onResume ();
        userstatus("online");
    }

    @Override
    protected void onPause() {
        super.onPause ();
        userstatus("offline");
        Calendar calendar=Calendar.getInstance ();
        SimpleDateFormat currentDate=new SimpleDateFormat (" dd MMM yyyy" );
        String saveCurrentDate=currentDate.format ( calendar.getTime () );
        String randomKey=saveCurrentDate;
        DocumentReference user = firebaseFirestore.collection("mes donnees utilisateur" ).document(current_user);
        user.update("derniere_conection", randomKey)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }

    @Override
    protected void onDestroy() {
        userstatus("offline");
        super.onDestroy ();
        userstatus("offline");
        Calendar calendar=Calendar.getInstance ();
        SimpleDateFormat currentDate=new SimpleDateFormat (" dd MMM yyyy" );
        String saveCurrentDate=currentDate.format ( calendar.getTime () );
        String randomKey=saveCurrentDate;
        DocumentReference user = firebaseFirestore.collection("mes donnees utilisateur" ).document(current_user);
        user.update("derniere_conection", randomKey)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });

    }
    public void sendmessage(final String expediteur, final String recepteur, final String message){
        DatabaseReference reference =FirebaseDatabase.getInstance ().getReference ();
        final long milli;
        milli=SystemClock.currentThreadTimeMillis ();
        Date date=new Date();
        SimpleDateFormat sdf= new SimpleDateFormat("d/MM H:mm");
        final String date_avec_seconde=sdf.format(date);
        Calendar calendrier=Calendar.getInstance ();
        SimpleDateFormat currentDateOne=new SimpleDateFormat (" d MMM H:mm" );
        saveCurrentDate=currentDateOne.format ( calendrier.getTime () );
        randomKey=saveCurrentDate;
        final HashMap<String,Object> mesageMap = new HashMap<> (  );
        mesageMap.put ( "expediteur",expediteur );
        mesageMap.put ( "recepteur",recepteur );
        mesageMap.put ( "message",message );
        mesageMap.put ( "milli",milli);
        mesageMap.put ( "temps_d_envoi",randomKey );
        reference.child ( "Chats" ).push ().setValue ( mesageMap );
        ///////////////////////////////////////////
        Calendar calendar=Calendar.getInstance ();
        SimpleDateFormat currentDate=new SimpleDateFormat (" dd MMM yyyy" );
        saveCurrentDate=currentDate.format ( calendar.getTime () );
        randomKey=saveCurrentDate;


        contact =new DiplayAllChat (  );
        contact.setId_recepteur ( recepteur );
        contact.setId_expediteur ( expediteur );
        contact.setImage_profil (lien_profil_contact );
        contact.setTemps ( randomKey );
        contact.setTempsMilli ( String.valueOf ( milli ) );
        contact.setNom_utilisateur (nom_utilisateur );
        contact.setDernier_message ( message );
        contact.setLu ( "lu" );
        firebaseFirestore.collection ( "dernier_message" )
                .document (expediteur).collection ( "contacts" )
                .document (recepteur)
                .set ( contact ).addOnSuccessListener ( new OnSuccessListener<Void> () {
            @Override
            public void onSuccess(Void aVoid) {
                contact =new DiplayAllChat (  );
                contact.setId_recepteur ( expediteur );
                contact.setId_expediteur ( recepteur );
                contact.setImage_profil (lien_profil_contact );
                contact.setTemps ( randomKey );
                contact.setTempsMilli ( String.valueOf ( milli ) );
                contact.setNom_utilisateur (nom_utilisateur );
                contact.setDernier_message ( message );
                contact.setLu ( "non lu" );
                firebaseFirestore.collection ( "dernier_message" )
                        .document (recepteur).collection ( "contacts" )
                        .document (expediteur)
                        .set ( contact );
            }
        } );


        ///////////////////////////////////////////////////
        DocumentReference read_or_not = firebaseFirestore.collection("dernier_message" ).document (recepteur).collection("contacts").document (expediteur);
        read_or_not.update("lu", "non lu")
                .addOnSuccessListener(new OnSuccessListener<Void> () {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });

        DocumentReference read_or_not_two = firebaseFirestore.collection("dernier_message" ).document (expediteur).collection("contacts").document (recepteur);
        read_or_not_two.update("lu", "lu")
                .addOnSuccessListener(new OnSuccessListener<Void> () {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });

        DocumentReference user = firebaseFirestore.collection("mes donnees utilisateur" ).document(recepteur);
        user.update("message", "non lu")
                .addOnSuccessListener(new OnSuccessListener<Void> () {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
        user.update("message_du_notifieur", "non lu")
                .addOnSuccessListener(new OnSuccessListener<Void> () {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }

    @Override
    public void onBackPressed() {
        userstatus("offline");
        Intent gotochatlist=new Intent(getApplicationContext(),ChatMessageActivity.class);
        if (viens_detail.equals ( "vrai" )){
            startActivity ( new Intent ( getApplicationContext (),ChatMessageActivity.class ).setFlags ( Intent.FLAG_ACTIVITY_CLEAR_TOP ).putExtra ( "viens","" ).putExtra ( "ouvert","ouvert" ).putExtra ( "viens_de_detail","vrai" ));
            finish();
        }else{
            startActivity ( new Intent ( getApplicationContext (),ChatMessageActivity.class ).setFlags ( Intent.FLAG_ACTIVITY_CLEAR_TOP ).putExtra ( "viens", "acceuil" ).putExtra ( "ouvert","ouvert" ).putExtra ( "viens_de_detail","faux" ) );
            finish();
        }

    }

    public void readMessage(final String monId, final String sonID, final String imageYrl){
        // modelChatList.clear();
        modelChatList=new ArrayList<> (  );
        reference=FirebaseDatabase.getInstance ().getReference ("Chats");
        reference.addValueEventListener ( new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                modelChatList.clear ();
                for (DataSnapshot snapshot:dataSnapshot.getChildren ()){
                    ModelChat chat = snapshot.getValue (ModelChat.class);
                    if (chat.getRecepteur ().equals ( monId )&&chat.getExpediteur ().equals ( sonID)||chat.getRecepteur ().equals ( sonID )&&chat.getExpediteur ().equals ( monId )){
                        modelChatList.add ( chat );
                    }
                    chatAdapter=new ChatAdapter (getApplicationContext (),modelChatList,imageYrl,true);
                    message_recyclerview.setAdapter ( chatAdapter );
                    chatAdapter.notifyDataSetChanged();
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
        if (current_user.equals(id_recepteur)){
            /*DocumentReference user = firebaseFirestore.collection("dernier_message" ).document (user_id_message).collection("contacts").document (current_user);
            user.update("lu", "lu")
                    .addOnSuccessListener(new OnSuccessListener<Void> () {
                        @Override
                        public void onSuccess(Void aVoid) {
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });*/

            DocumentReference usertwo = firebaseFirestore.collection("dernier_message" ).document (current_user).collection("contacts").document (user_id_message);
            usertwo.update("lu", "lu")
                    .addOnSuccessListener(new OnSuccessListener<Void> () {
                        @Override
                        public void onSuccess(Void aVoid) {
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });
        }else{
            DocumentReference usertwo = firebaseFirestore.collection("dernier_message" ).document (current_user).collection("contacts").document (user_id_message);
            usertwo.update("lu", "lu")
                    .addOnSuccessListener(new OnSuccessListener<Void> () {
                        @Override
                        public void onSuccess(Void aVoid) {
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });
        }
        message_progressbar.setVisibility(View.INVISIBLE);



    }


    public void nomEtImageProfil(){
        firebaseFirestore.collection("mes donnees utilisateur").document(user_id_message).get().addOnCompleteListener(MessageActivity.this,new OnCompleteListener<DocumentSnapshot> () {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult ().exists ()){
                        prenom=task.getResult ().getString ( "user_prenom" );
                        name_user= task.getResult ().getString ( "user_name" );
                        String image_user=task.getResult ().getString ( "user_profil_image" );
                        lien_profil_contact =task.getResult ().getString ( "user_profil_image" );
                        nom_utilisateur=task.getResult ().getString ( "user_name" );
                        String status=task.getResult ().getString ( "status" );
                        Log.e ("keytwo",status);
                        if (status.equals ( "online" )){
                            online_status.setVisibility ( View.VISIBLE );
                            offline_status.setVisibility ( View.INVISIBLE );
                        }else {
                            online_status.setVisibility ( View.INVISIBLE );
                            offline_status.setVisibility ( View.VISIBLE );
                        }
                        readMessage ( current_user,user_id_message,lien_profil_contact );
                        user_name.setText(name_user+" "+prenom);
                        user_name.setAnimation ( AnimationUtils.loadAnimation ( getApplicationContext (),R.anim.fade_transition_animation ) );
//                        Picasso.with(getApplicationContext()).load(image_user).into(image_en_fond);
                        Picasso.with(getApplicationContext()).load(image_user).into(user_message_image);
                        user_message_image.setAnimation ( AnimationUtils.loadAnimation ( getApplicationContext (),R.anim.fade_transition_animation ) );

                    }
                }else {
                    String error=task.getException().getMessage();
                    Toast.makeText ( getApplicationContext (), error, Toast.LENGTH_LONG ).show ();

                }
            }
        });

    }



}




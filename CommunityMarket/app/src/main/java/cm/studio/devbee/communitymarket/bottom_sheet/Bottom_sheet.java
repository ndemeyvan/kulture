package cm.studio.devbee.communitymarket.bottom_sheet;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import cm.studio.devbee.communitymarket.R;
import cm.studio.devbee.communitymarket.commentaires.Commentaires_Model;
import cm.studio.devbee.communitymarket.postActivity.DetailActivityTwo;
import de.hdodenhof.circleimageview.CircleImageView;

public class Bottom_sheet extends BottomSheetDialogFragment {
    private static FirebaseFirestore firebaseFirestore;
    private BottomSheet_listener mListener;
    private Button post_detail_add_comment_btn;
    private EditText post_detail_comment;
    private CircleImageView post_detail_currentuser_img;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.bottom_sheet_layout,container,false);
        firebaseFirestore=FirebaseFirestore.getInstance();
        post_detail_add_comment_btn=v.findViewById(R.id.post_detail_add_comment_btn);
        post_detail_comment=v.findViewById(R.id.post_detail_comment);
        post_detail_currentuser_img=v.findViewById(R.id.post_detail_currentuser_img);
        final String commentaire = post_detail_comment.getText().toString();
        post_detail_add_comment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.bottom_sheet_listener(commentaire);
                dismiss();
            }
        });
        return v;
    }

    public interface  BottomSheet_listener{
        void bottom_sheet_listener(String message );
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mListener = (BottomSheet_listener) context;
        }catch (Exception e){

        }
    }
}

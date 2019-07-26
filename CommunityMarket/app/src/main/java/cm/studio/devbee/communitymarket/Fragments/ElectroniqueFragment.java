package cm.studio.devbee.communitymarket.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cm.studio.devbee.communitymarket.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ElectroniqueFragment extends Fragment {


    public ElectroniqueFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_electronique, container, false);
    }

}

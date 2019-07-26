package cm.studio.devbee.communitymarket.Fragments.informatique;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cm.studio.devbee.communitymarket.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrdinateurFragment extends Fragment {


    public OrdinateurFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_ordinateur, container, false);

        return v;

    }

}

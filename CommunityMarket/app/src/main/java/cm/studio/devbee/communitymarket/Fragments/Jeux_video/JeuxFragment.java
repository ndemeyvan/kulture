package cm.studio.devbee.communitymarket.Fragments.Jeux_video;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cm.studio.devbee.communitymarket.Fragments.Telephone_tablette.AccessoirePhoneFragment;
import cm.studio.devbee.communitymarket.Fragments.informatique.OrdinateurFragment;
import cm.studio.devbee.communitymarket.Fragments.informatique.StockageFragment;
import cm.studio.devbee.communitymarket.Mes_tabs_Adapter.Informatique_Tabs_Adapter;
import cm.studio.devbee.communitymarket.Mes_tabs_Adapter.Jeux_Tabs_Adapter;
import cm.studio.devbee.communitymarket.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class JeuxFragment extends Fragment {


    public JeuxFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_jeux, container, false);

        return v;
    }



}

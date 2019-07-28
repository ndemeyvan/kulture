package cm.studio.devbee.communitymarket.Fragments;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cm.studio.devbee.communitymarket.Fragments.Telephone_tablette.AccessoirePhoneFragment;
import cm.studio.devbee.communitymarket.Fragments.Telephone_tablette.TabletFragment;
import cm.studio.devbee.communitymarket.Fragments.Telephone_tablette.TelephoneFragment;
import cm.studio.devbee.communitymarket.Fragments.mode.AccesoireFragment;
import cm.studio.devbee.communitymarket.Fragments.mode.BijouxFragment;
import cm.studio.devbee.communitymarket.Fragments.mode.ChaussureFragment;
import cm.studio.devbee.communitymarket.Fragments.mode.JupesFragment;
import cm.studio.devbee.communitymarket.Fragments.mode.MontreFragment;
import cm.studio.devbee.communitymarket.Fragments.mode.PantalonFragment;
import cm.studio.devbee.communitymarket.Fragments.mode.PoloFragment;
import cm.studio.devbee.communitymarket.Fragments.mode.Sous_vetement;
import cm.studio.devbee.communitymarket.Fragments.mode.Vetementragment;
import cm.studio.devbee.communitymarket.Mes_tabs_Adapter.ModeTabs_Adapter;
import cm.studio.devbee.communitymarket.Mes_tabs_Adapter.Telephone_tab_adapter;
import cm.studio.devbee.communitymarket.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Tablette_TabFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager tabsviewpager;
    public Tablette_TabFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tablette__tab, container, false);
        tabLayout=v.findViewById(R.id.tabLayoutPhone);
        tabsviewpager=v.findViewById(R.id.tabsviewPhone);
        setupViewPager(tabsviewpager);
        tabLayout.setupWithViewPager(tabsviewpager);
        return v;
    }

    public void setupViewPager(ViewPager viewPager){
        Telephone_tab_adapter tabsAdapter=new Telephone_tab_adapter(getActivity().getSupportFragmentManager());
        tabsAdapter.addFragment(new TelephoneFragment(),"Telephones");
        tabsAdapter.addFragment(new TabletFragment(),"Tablette");
        tabsAdapter.addFragment(new AccessoirePhoneFragment(),"Accessoires");

        viewPager.setAdapter(tabsAdapter);

    }

}

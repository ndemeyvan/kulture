package cm.studio.devbee.communitymarket.Fragments;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cm.studio.devbee.communitymarket.Fragments.Electronique.Audio;
import cm.studio.devbee.communitymarket.Fragments.Electronique.Photo_CameraFragment;
import cm.studio.devbee.communitymarket.Fragments.Electronique.TelevisonFragment;
import cm.studio.devbee.communitymarket.Fragments.Telephone_tablette.AccessoirePhoneFragment;
import cm.studio.devbee.communitymarket.Fragments.informatique.OrdinateurFragment;
import cm.studio.devbee.communitymarket.Fragments.informatique.StockageFragment;
import cm.studio.devbee.communitymarket.Mes_tabs_Adapter.Electronique_tabs_Adapter;
import cm.studio.devbee.communitymarket.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class InformatiqueFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager tabsviewpager;


    public InformatiqueFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_informatique, container, false);
        tabLayout=v.findViewById(R.id.tabLayoutElect);
        tabsviewpager=v.findViewById(R.id.tabsviewElect);
        setupViewPager(tabsviewpager);
        tabLayout.setupWithViewPager(tabsviewpager);
        return v;
    }
    public void setupViewPager(ViewPager viewPager){
        Electronique_tabs_Adapter tabsAdapter=new Electronique_tabs_Adapter(getActivity().getSupportFragmentManager());
        tabsAdapter.addFragment(new OrdinateurFragment(),"Ordinateur");
        tabsAdapter.addFragment(new AccessoirePhoneFragment(),"App.Photo/camera");
        tabsAdapter.addFragment(new StockageFragment(),"Audio");
        viewPager.setAdapter(tabsAdapter);

    }
}

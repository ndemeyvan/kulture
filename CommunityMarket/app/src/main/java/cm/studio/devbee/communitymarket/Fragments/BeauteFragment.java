package cm.studio.devbee.communitymarket.Fragments;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cm.studio.devbee.communitymarket.Fragments.Beaute.LaitDeBeauteFragment;
import cm.studio.devbee.communitymarket.Fragments.Beaute.MaquillageFragment;
import cm.studio.devbee.communitymarket.Fragments.Beaute.ParfumFragment;
import cm.studio.devbee.communitymarket.Fragments.Electronique.Audio;
import cm.studio.devbee.communitymarket.Fragments.Electronique.Photo_CameraFragment;
import cm.studio.devbee.communitymarket.Fragments.Electronique.TelevisonFragment;
import cm.studio.devbee.communitymarket.Mes_tabs_Adapter.Electronique_tabs_Adapter;
import cm.studio.devbee.communitymarket.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BeauteFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager tabsviewpager;

    public BeauteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_beaute, container, false);
        tabLayout=v.findViewById(R.id.tabLayoutBeaute);
        tabsviewpager=v.findViewById(R.id.tabsviewBeaute);
        setupViewPager(tabsviewpager);
        tabLayout.setupWithViewPager(tabsviewpager);
        return v;
    }
    public void setupViewPager(ViewPager viewPager){
        Electronique_tabs_Adapter tabsAdapter=new Electronique_tabs_Adapter(getActivity().getSupportFragmentManager());
        tabsAdapter.addFragment(new ParfumFragment(),"Parfum");
        tabsAdapter.addFragment(new LaitDeBeauteFragment(),"Lait de beaute");
        tabsAdapter.addFragment(new MaquillageFragment(),"Maquillage");

        viewPager.setAdapter(tabsAdapter);

    }

}

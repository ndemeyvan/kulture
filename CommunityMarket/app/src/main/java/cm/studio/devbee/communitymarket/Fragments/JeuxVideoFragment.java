package cm.studio.devbee.communitymarket.Fragments;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cm.studio.devbee.communitymarket.Fragments.Jeux_video.Accssoire_Jeux_Fragment;
import cm.studio.devbee.communitymarket.Fragments.Jeux_video.ConsoleFragment;
import cm.studio.devbee.communitymarket.Fragments.Jeux_video.JeuxFragment;
import cm.studio.devbee.communitymarket.Mes_tabs_Adapter.Jeux_Tabs_Adapter;
import cm.studio.devbee.communitymarket.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class JeuxVideoFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager tabsviewpager;
    public JeuxVideoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_jeux_video, container, false);
        tabLayout=v.findViewById(R.id.tabLayoutJeux);
        tabsviewpager=v.findViewById(R.id.tabsviewJeux);
        setupViewPager(tabsviewpager);
        tabLayout.setupWithViewPager(tabsviewpager);
        return v;
    }

    public void setupViewPager(ViewPager viewPager){
        Jeux_Tabs_Adapter tabsAdapter=new Jeux_Tabs_Adapter(getActivity().getSupportFragmentManager());
        tabsAdapter.addFragment(new ConsoleFragment(),"Consoles");
        tabsAdapter.addFragment(new JeuxFragment(),"Jeux");
        tabsAdapter.addFragment(new Accssoire_Jeux_Fragment (),"Accessoires");

        viewPager.setAdapter(tabsAdapter);

    }

}

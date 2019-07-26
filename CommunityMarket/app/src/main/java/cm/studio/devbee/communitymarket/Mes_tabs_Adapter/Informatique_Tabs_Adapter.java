package cm.studio.devbee.communitymarket.Mes_tabs_Adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import cm.studio.devbee.communitymarket.Fragments.informatique.OrdinateurFragment;

public class Informatique_Tabs_Adapter extends FragmentPagerAdapter {
    List fragmentList =new ArrayList();
    List fragmentTitleList=new ArrayList();

    public void addFragment(Fragment fragment, String title){
        fragmentList.add(fragment);
        fragmentTitleList.add(title);
    }
    public Informatique_Tabs_Adapter(FragmentManager fm) {
        super(fm);
    }
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return (CharSequence) fragmentTitleList.get(position);
    }


    @Override
    public Fragment getItem(int i) {

        switch (i){
            case 0:
                OrdinateurFragment home =new OrdinateurFragment();
                return home;
        }
        return (Fragment) fragmentList.get(i);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

}


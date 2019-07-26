package cm.studio.devbee.communitymarket.Mes_tabs_Adapter;

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


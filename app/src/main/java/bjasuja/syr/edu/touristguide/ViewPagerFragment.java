package bjasuja.syr.edu.touristguide;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Button;

import com.eftimoff.viewpagertransformers.AccordionTransformer;
import com.eftimoff.viewpagertransformers.BackgroundToForegroundTransformer;
import com.eftimoff.viewpagertransformers.CubeInTransformer;
import com.eftimoff.viewpagertransformers.CubeOutTransformer;
import com.eftimoff.viewpagertransformers.ParallaxPageTransformer;
import com.eftimoff.viewpagertransformers.RotateUpTransformer;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.pollfish.constants.Position;
import com.pollfish.main.PollFish;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jp.wasabeef.recyclerview.animators.FadeInAnimator;
import jp.wasabeef.recyclerview.animators.FlipInBottomXAnimator;

public class ViewPagerFragment extends Fragment {
    static int pageno=0,movieno;
    private TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter adapter;

    static String s="https://maps.googleapis.com/maps/api/place/nearbysearch/json?key=AIzaSyC1bDJ9vVGjOwemAvVQyvLz0HPyv-kLDoI&radius=20000&types=restaurant";
    static String s2="https://maps.googleapis.com/maps/api/place/nearbysearch/json?key=AIzaSyC1bDJ9vVGjOwemAvVQyvLz0HPyv-kLDoI&radius=20000&types=store";
    static String s3="https://maps.googleapis.com/maps/api/place/nearbysearch/json?key=AIzaSyC1bDJ9vVGjOwemAvVQyvLz0HPyv-kLDoI&radius=20000&types=movie_theater";
    static String s4="https://maps.googleapis.com/maps/api/place/nearbysearch/json?key=AIzaSyC1bDJ9vVGjOwemAvVQyvLz0HPyv-kLDoI&radius=20000&types=museum";
    static String s5="https://maps.googleapis.com/maps/api/place/nearbysearch/json?key=AIzaSyC1bDJ9vVGjOwemAvVQyvLz0HPyv-kLDoI&radius=20000&types=lodging";
    static String s6="https://maps.googleapis.com/maps/api/place/nearbysearch/json?key=AIzaSyC1bDJ9vVGjOwemAvVQyvLz0HPyv-kLDoI&radius=20000&types=gas_station";
    static String s7="https://maps.googleapis.com/maps/api/place/nearbysearch/json?key=AIzaSyC1bDJ9vVGjOwemAvVQyvLz0HPyv-kLDoI&radius=20000&types=atm";

    RecyclerviewFragment masterFragment;
    RecyclerviewFragment2 masterFragment2;
    Recyclerviewfragment3 masterFragment3;
    RecyclerviewFragment4 masterFragment4;
    RecyclerviewFragment5 masterFragment5;
    RecyclerviewFragment6 masterFragment6;
    RecyclerviewFragment7 masterFragment7;

    private int[] tabIcons = {
            R.drawable.ic_restaurant_menu_black_24dp,
            R.drawable.ic_shopping_basket_black_24dp,
            R.drawable.ic_local_movies_black_24dp,
            R.drawable.ic_location_city_black_24dp,
            R.drawable.ic_hotel_black_24dp,
            R.drawable.ic_local_gas_station_black_24dp,
            R.drawable.ic_local_atm_black_24dp
    };

    public ViewPagerFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_view_pager, container, false);;

        viewPager = (ViewPager) v.findViewById(R.id.viewpager);

        setupViewPager(viewPager);

        tabLayout = (TabLayout) v.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        pageno = viewPager.getCurrentItem();

        final ViewPager.OnPageChangeListener pageChangeListener =new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d("pageno",String.valueOf(position));
                pageno=position;

            }

            @Override
            public void onPageSelected(int position) {
                pageno=position;


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };

        viewPager.addOnPageChangeListener( pageChangeListener);
        viewPager.post(new Runnable()
        {
            @Override
            public void run()
            {
                pageChangeListener .onPageSelected(viewPager.getCurrentItem()+1);
            }
        });

        setupTabIcons();

        return v;

    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
        tabLayout.getTabAt(4).setIcon(tabIcons[4]);
        tabLayout.getTabAt(5).setIcon(tabIcons[5]);
        tabLayout.getTabAt(6).setIcon(tabIcons[6]);
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getChildFragmentManager());
        masterFragment=new RecyclerviewFragment();
        masterFragment2=new RecyclerviewFragment2();
        masterFragment3=new Recyclerviewfragment3();
        masterFragment4=new RecyclerviewFragment4();
        masterFragment5=new RecyclerviewFragment5();
        masterFragment6=new RecyclerviewFragment6();
        masterFragment7=new RecyclerviewFragment7();
        adapter.addFragment(masterFragment,"Eating");
        adapter.addFragment(masterFragment2,"Shopping");
        adapter.addFragment(masterFragment3,"Entertainmnet");
        adapter.addFragment(masterFragment4,"Historic Places");
        adapter.addFragment(masterFragment5,"Lodging");
        adapter.addFragment(masterFragment6,"Utilities");
        adapter.addFragment(masterFragment7,"ATM");
        viewPager.setAdapter(adapter);
        viewPager.setPageTransformer(true, new CubeOutTransformer());
    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount()
        {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {

            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((MainActivity)getActivity()). actionBarDrawerToggle.syncState();
        ((MainActivity)getActivity()).drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }
}

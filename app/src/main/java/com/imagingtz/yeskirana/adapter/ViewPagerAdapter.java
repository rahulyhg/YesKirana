package com.imagingtz.yeskirana.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.imagingtz.yeskirana.fragment.ProductFragment;


/**
 * Created by Mishra on 1/16/2016.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private String[] tabsName = {"All", "New Products"};

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);

    }

    @Override
    public Fragment getItem(int position) {
        return ProductFragment.newInstance(position);
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return tabsName[position];
    }

    @Override
    public int getCount() {
        return 2;
    }

}


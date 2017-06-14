package com.sleepaiden.alphebetsong.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.sleepaiden.alphebetsong.models.AlphebetPage;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @Setter
    @Getter
    private List<AlphebetPage> data;

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return FirstPageFragment.newInstance();
        } else {
            return PlaceholderFragment.newInstance(data.get(position - 1));
        }
    }

    @Override
    public int getCount() {
        return data.size() + 1;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "";
        } else {
            return data.get(position - 1).getWord();
        }
    }
}

package com.newlag.tvapp.Adapters;

import android.content.Context;
import android.content.res.Resources;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.newlag.tvapp.PageFragment;
import com.newlag.tvapp.R;

public class PagerAdapter extends FragmentPagerAdapter {

    private static final int PAGE_COUNT = 2;

    private Context context;

    public PagerAdapter(@NonNull FragmentManager fm, int behavior, Context context) {
        super(fm, behavior);
        this.context = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return PageFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        Resources res = context.getResources();
        switch (position) {
            case 0:
                return res.getString(R.string.channels_title);
            case 1:
                return res.getString(R.string.favourite_title);
        }
        return null;
    }
}

package com.lefatechs.smarthome.Controller;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.lefatechs.smarthome.R;
import com.lefatechs.smarthome.View.Fragment.Engergy.SearchDayFragment;
import com.lefatechs.smarthome.View.Fragment.Engergy.SearchMonthFragment;
import com.lefatechs.smarthome.View.Fragment.Engergy.SearchYearFragment;

public class SearchAdapter extends FragmentPagerAdapter {
    private Context mContext;

    public SearchAdapter(FragmentManager fragmentManager, Context mContext) {
        super(fragmentManager,FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new SearchDayFragment();
            case 1: return new SearchMonthFragment();
            default: return new SearchYearFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String day = mContext.getResources().getString(R.string.label_day);
        String month = mContext.getResources().getString(R.string.label_month);
        String year = mContext.getResources().getString(R.string.label_year);
        switch (position){
            case 0: return day;
            case 1: return month;
            default: return year;
        }
    }
}

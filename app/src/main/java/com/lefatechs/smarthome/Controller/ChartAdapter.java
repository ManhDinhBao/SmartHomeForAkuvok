package com.lefatechs.smarthome.Controller;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.lefatechs.smarthome.R;
import com.lefatechs.smarthome.View.Fragment.Engergy.ElectricChartFragment;
import com.lefatechs.smarthome.View.Fragment.Engergy.WaterChartFragment;

public class ChartAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public ChartAdapter(FragmentManager fragmentManager, Context mContext) {
        super(fragmentManager,FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position==0){
            return new ElectricChartFragment();
        }
        else{
            return new WaterChartFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String water = mContext.getResources().getString(R.string.label_water);
        String electric = mContext.getResources().getString(R.string.label_electric);
        if (position==0){
            return electric;
        }
        else {
            return water;
        }
    }
}

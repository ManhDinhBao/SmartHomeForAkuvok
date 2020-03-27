package com.example.smarthome.Fragment.Notification;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class NotifyImageAdapter extends FragmentPagerAdapter{
    private ArrayList<String> mListAttachAddress;

    public NotifyImageAdapter(FragmentManager fragmentManager, ArrayList<String> listAttachAddress) {
        super(fragmentManager,FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.mListAttachAddress = listAttachAddress;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        String address = mListAttachAddress.get(position);
        return NotifyImageFragment.newInstance(address);
    }

    @Override
    public int getCount() {
        return mListAttachAddress.size();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}

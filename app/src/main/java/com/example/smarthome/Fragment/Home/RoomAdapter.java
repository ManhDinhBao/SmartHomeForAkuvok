package com.example.smarthome.Fragment.Home;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.smarthome.Model.Room.Room;
import com.google.gson.Gson;

import java.util.ArrayList;

public class RoomAdapter extends FragmentPagerAdapter {

    private ArrayList<Room> mListRooms;

    public RoomAdapter(FragmentManager fragmentManager, ArrayList<Room> mListRooms) {
        super(fragmentManager,FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.mListRooms = mListRooms;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Room room=mListRooms.get(position);
        return RoomFragment.newInstance(room.getId());
    }

    @Override
    public int getCount() {
        return mListRooms.size();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mListRooms.get(position).getName();
    }
}

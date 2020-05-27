package com.lefatechs.smarthome.Controller;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.lefatechs.smarthome.View.Fragment.Notification.AttachmentFragment;

import java.util.ArrayList;

public class AttachmentAdapter extends FragmentPagerAdapter {

    private ArrayList<String> mListAttachment;

    public AttachmentAdapter(FragmentManager fragmentManager, ArrayList<String> listAttachment) {
        super(fragmentManager,FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.mListAttachment = listAttachment;
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        String attachmentName=mListAttachment.get(position);
        return AttachmentFragment.newInstance(attachmentName);
    }

    @Override
    public int getCount() {
        return mListAttachment.size();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return  POSITION_NONE;
    }

}

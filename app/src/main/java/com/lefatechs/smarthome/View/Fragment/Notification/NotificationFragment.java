package com.lefatechs.smarthome.View.Fragment.Notification;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.lefatechs.smarthome.Controller.AttachmentAdapter;
import com.lefatechs.smarthome.Controller.MessageAdapter;
import com.lefatechs.smarthome.Custom.Global;
import com.lefatechs.smarthome.Model.Message.Message;
import com.lefatechs.smarthome.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NotificationFragment extends Fragment implements MessageAdapter.OnItemClickListener{

    private ImageView mIvBackGround;
    private MessageAdapter mMessageAdapter;
    private RecyclerView mRvMessage;
    private ViewPager sVpMessage;
    private TabLayout mTlMessage;
    private ArrayList<Message> mListMessage;
    private AttachmentAdapter attachmentAdapter;
    public final static String PREF_BACKGROUND = "background";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        sVpMessage = view.findViewById(R.id.viewPager_test_Content);
        mTlMessage = view.findViewById(R.id.tabLayout_notificationFragment_ContentList);
        mIvBackGround = view.findViewById(R.id.imageView_notificationFragment_backGround);
        mListMessage = Global.GetMessage();
        mRvMessage = view.findViewById(R.id.recycleView_notificationFragment_listNotification);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(RecyclerView.VERTICAL);

        mMessageAdapter = new MessageAdapter(getActivity(), mListMessage);
        mMessageAdapter.setOnItemClickListener(NotificationFragment.this);

        mRvMessage.setHasFixedSize(true);
        mRvMessage.setLayoutManager(layoutManager);
        mRvMessage.setAdapter(mMessageAdapter);

        attachmentAdapter = new AttachmentAdapter(getChildFragmentManager(), mListMessage.get(0).getAttachAddress());
        sVpMessage.setAdapter(attachmentAdapter);
        mTlMessage.setupWithViewPager(sVpMessage, true);

        ArrayList<String> listAttachment = mListMessage.get(0).getAttachAddress();
        attachmentAdapter = new AttachmentAdapter(getChildFragmentManager(),listAttachment);

        sVpMessage.setAdapter(attachmentAdapter);
        attachmentAdapter.notifyDataSetChanged();
        mTlMessage.setupWithViewPager(sVpMessage,true);

        return  view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //Set background
        SharedPreferences sharedConfig = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());
        String backgroundValue = sharedConfig.getString(PREF_BACKGROUND, "");
        switch (backgroundValue) {
            case "2":
                Picasso.get().load(R.drawable.home_background2).fit().into(mIvBackGround);
                break;
            case "3":
                Picasso.get().load(R.drawable.home_background3).fit().into(mIvBackGround);
                break;
            case "4":
                Picasso.get().load(R.drawable.home_background4).fit().into(mIvBackGround);
                break;
            default:
                Picasso.get().load(R.drawable.home_background).fit().into(mIvBackGround);
                break;
        }

    }

    @Override
    public void onItemClick(int position) {
        int backStackEntry = getChildFragmentManager().getBackStackEntryCount();
        if (backStackEntry > 0) {
            for (int i = 0; i < backStackEntry; i++) {
                getChildFragmentManager().popBackStackImmediate();
            }
        }

        if (getChildFragmentManager().getFragments() != null && getChildFragmentManager().getFragments().size() > 0) {
            for (int i = 0; i < getChildFragmentManager().getFragments().size(); i++) {
                Fragment mFragment = getChildFragmentManager().getFragments().get(i);
                if (mFragment != null) {
                    getChildFragmentManager().beginTransaction().remove(mFragment).commit();
                }
            }
        }

        ArrayList<String> listAttachment = mListMessage.get(position).getAttachAddress();
        attachmentAdapter = new AttachmentAdapter(getChildFragmentManager(),listAttachment);

        sVpMessage.setAdapter(attachmentAdapter);
        attachmentAdapter.notifyDataSetChanged();
        mTlMessage.setupWithViewPager(sVpMessage,true);
    }

}

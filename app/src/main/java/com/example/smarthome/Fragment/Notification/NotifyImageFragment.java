package com.example.smarthome.Fragment.Notification;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.smarthome.R;
import com.squareup.picasso.Picasso;

public class NotifyImageFragment extends Fragment {

    private String mImageAddress;
    private ImageView mIvNotifyContent;

    public static NotifyImageFragment newInstance(String mAttachAddress) {
        NotifyImageFragment myFragment = new NotifyImageFragment();

        Bundle args = new Bundle();
        args.putString("ADDRESS", mAttachAddress);
        myFragment.setArguments(args);

        return myFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mImageAddress = getArguments().getString("ADDRESS");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final View view = inflater.inflate(R.layout.fragment_room, container, false);

        mIvNotifyContent = view.findViewById(R.id.imageView_Notification_Detail);
        Picasso.get().load(mImageAddress).fit().into(mIvNotifyContent);

        return  view;
    }
}

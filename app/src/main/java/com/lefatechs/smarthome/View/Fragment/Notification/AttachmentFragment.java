package com.lefatechs.smarthome.View.Fragment.Notification;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.lefatechs.smarthome.R;
import com.squareup.picasso.Picasso;

public class AttachmentFragment extends Fragment {

    private ImageView mIvAttachment;
    private String mAttachmentName;
    private float mScaleFactor = 1.0f;
    private ScaleGestureDetector scaleGestureDetector;


    public static AttachmentFragment newInstance(String attachment) {
        AttachmentFragment myFragment = new AttachmentFragment();
        Bundle args = new Bundle();
        args.putString("ATTACHMENT_NAME", attachment);
        myFragment.setArguments(args);
        return myFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_notification_detail, container, false);
        mIvAttachment = view.findViewById(R.id.imageView_Notification_Detail);
        scaleGestureDetector = new ScaleGestureDetector(getActivity(), new ScaleListener());

        mAttachmentName = getArguments().getString("ATTACHMENT_NAME");

        String uri = "@drawable/"+mAttachmentName;

        int imageResource = getResources().getIdentifier(uri, null, getActivity().getPackageName());

//        Drawable res = ContextCompat.getDrawable(getActivity(), imageResource);
//        mIvAttachment.setImageDrawable(res);

        Picasso.get().load(imageResource).fit().into(mIvAttachment);

        mIvAttachment.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                scaleGestureDetector.onTouchEvent(motionEvent);
                return true;
            }
        });

        return view;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            mScaleFactor *= scaleGestureDetector.getScaleFactor();
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f));
            mIvAttachment.setScaleX(mScaleFactor);
            mIvAttachment.setScaleY(mScaleFactor);
            return true;
        }
    }
}

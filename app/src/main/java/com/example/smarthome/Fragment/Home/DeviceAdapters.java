package com.example.smarthome.Fragment.Home;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smarthome.Model.Device.Device;
import com.example.smarthome.Model.Point.Point;
import com.example.smarthome.R;
import com.example.smarthome.Utils.Comm;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DeviceAdapters extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private ArrayList<Device> mLstDevices;
    private OnItemClickListener mListenerClick;
    private OnStopTrackingTouch mListenerTracking;
    private OnTouchListener mListenerTouch;

    public DeviceAdapters(Context context, ArrayList<Device> itemList) {
        mContext = context;
        mLstDevices = itemList;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);

        void onItemClick(int position, int buttonPosition);
    }

    public interface OnStopTrackingTouch {
        void onStopTrackingTouch(int position, int progress);
    }

    public interface OnTouchListener {
        void onTouch(MotionEvent motionEvent);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListenerClick = listener;
    }

    public void onStopTrackingTouch(OnStopTrackingTouch listener) {
        mListenerTracking = listener;
    }

    public void onTouch(OnTouchListener listener) {
        mListenerTouch = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case 1:
                View view1 = layoutInflater.inflate(R.layout.item_control_seekbar, parent, false);
                return new ViewHolderSeekBar(view1);
            case 2:
                View view2 = layoutInflater.inflate(R.layout.item_control_curtain, parent, false);
                return new ViewHolderCurtain(view2);
            default:
                View view = layoutInflater.inflate(R.layout.item_control, parent, false);
                return new ViewHolderNormal(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Device device = mLstDevices.get(position);
        //View type 1 = ViewHolderSeekBar
        //Only dim light and AC have this view type
        if (device.getDeviceViewType() == 1) {
            ((ViewHolderSeekBar) holder).mTvDeviceName.setText(device.getName());

            //Set device item background
            if (!device.isPower()) {
                ((ViewHolderSeekBar) holder).mCardView.setElevation(0);
                int color = Build.VERSION.SDK_INT < 23 ? mContext.getResources().getColor(R.color.transparent80) : mContext.getResources().getColor(R.color.transparent80, null);
                ((ViewHolderSeekBar) holder).mCardView.setBackgroundColor(color);
                ((ViewHolderSeekBar) holder).mSbDeviceRange.setEnabled(false);
            } else {
                ((ViewHolderSeekBar) holder).mCardView.setElevation(5);
                int color = Build.VERSION.SDK_INT < 23 ? mContext.getResources().getColor(R.color.white) : mContext.getResources().getColor(R.color.white, null);
                ((ViewHolderSeekBar) holder).mCardView.setBackgroundColor(color);
                ((ViewHolderSeekBar) holder).mSbDeviceRange.setEnabled(true);

            }

            //Set device icon and value
            switch (device.getType().getID()) {
                case Comm.DEVICE_TYPE_LIGHT:
                    ((ViewHolderSeekBar) holder).mSbDeviceRange.setMax(100);
                    if (!device.isPower()) {
                        ((ViewHolderSeekBar) holder).mIvDeviceIcon.setImageResource(R.drawable.light_off_64px);
                        ((ViewHolderSeekBar) holder).mTvDeviceStatus.setText(mContext.getResources().getString(R.string.label_device_power_off));
                    } else {
                        ((ViewHolderSeekBar) holder).mIvDeviceIcon.setImageResource(R.drawable.light_on_64px);
                        ((ViewHolderSeekBar) holder).mTvDeviceStatus.setText(mContext.getResources().getString(R.string.label_device_power_on) + "(" + device.getCurrentValue() + "%)");
                    }
                    break;
                case Comm.DEVICE_TYPE_AC:
                    ((ViewHolderSeekBar) holder).mSbDeviceRange.setMax(35);
                    if (!device.isPower()) {
                        ((ViewHolderSeekBar) holder).mIvDeviceIcon.setImageResource(R.drawable.ac_off_64px);
                        ((ViewHolderSeekBar) holder).mTvDeviceStatus.setText(mContext.getResources().getString(R.string.label_device_power_off));
                    } else {
                        ((ViewHolderSeekBar) holder).mIvDeviceIcon.setImageResource(R.drawable.ac_on_64px);
                        ((ViewHolderSeekBar) holder).mTvDeviceStatus.setText(mContext.getResources().getString(R.string.label_device_power_on) + "(" + device.getCurrentValue() + "°C)");
                    }
                    break;
            }

            //Set progress value in first time load
            ((ViewHolderSeekBar) holder).mSbDeviceRange.setProgress(Integer.parseInt(device.getCurrentValue()));

            //View type 2 = ViewHolderCurtain
            // OnlyCurtain have this view type
        } else if (device.getDeviceViewType() == 2) {
            ((ViewHolderCurtain) holder).mTvDeviceName.setText(device.getName());
            String jsonCurtainMode = null;
            String stopMode = "0", openMode = "1", closeMode = "2";
            for (Point point : device.getPoints()) {
                if (point.getAlias().matches("mode")) {
                    jsonCurtainMode = point.getCharacter().getMap();
                }
            }

            //If curtain mode no error, get mode value
            //Else use default mode value
            if (jsonCurtainMode != null) {
                try {
                    JSONObject objCurtainMode = new JSONObject(jsonCurtainMode);
                    stopMode = objCurtainMode.getString("STOP");
                    openMode = objCurtainMode.getString("OPEN");
                    closeMode = objCurtainMode.getString("CLOSE");

                } catch (JSONException e) {
                    e.printStackTrace();
                    stopMode = "0";
                    openMode = "1";
                    closeMode = "2";
                }
            }

            //Set curtain status and icon
            if (device.getCurrentValue().matches(stopMode)) {
                ((ViewHolderCurtain) holder).mTvDeviceStatus.setText(mContext.getResources().getString(R.string.label_stop));
                ((ViewHolderCurtain) holder).mIvDeviceIcon.setImageResource(R.drawable.curtain_stop_64px);
            }
            if (device.getCurrentValue().matches(closeMode)) {
                ((ViewHolderCurtain) holder).mTvDeviceStatus.setText(mContext.getResources().getString(R.string.label_close));
                ((ViewHolderCurtain) holder).mIvDeviceIcon.setImageResource(R.drawable.curtain_full_close_64px);
            }
            if (device.getCurrentValue().matches(openMode)) {
                ((ViewHolderCurtain) holder).mTvDeviceStatus.setText(mContext.getResources().getString(R.string.label_open));
                ((ViewHolderCurtain) holder).mIvDeviceIcon.setImageResource(R.drawable.curtain_full_open_64px);
            }

            //View type 3 = ViewHolderNormal
        } else {
            ((ViewHolderNormal) holder).mTvDeviceName.setText(device.getName());

            //Set curtain status and icon
            if (!device.isPower()) {
                ((ViewHolderNormal) holder).mCardView.setElevation(0);
                ((ViewHolderNormal) holder).mIvDeviceIcon.setImageResource(R.drawable.light_off_64px);
                int color = Build.VERSION.SDK_INT < 23 ? mContext.getResources().getColor(R.color.transparent80) : mContext.getResources().getColor(R.color.white, null);
                ((ViewHolderNormal) holder).mCardView.setBackgroundColor(color);
                ((ViewHolderNormal) holder).mTvDeviceStatus.setText(mContext.getResources().getString(R.string.label_device_power_off));

            } else {
                ((ViewHolderNormal) holder).mCardView.setElevation(3);
                ((ViewHolderNormal) holder).mIvDeviceIcon.setImageResource(R.drawable.light_on_64px);
                int color = Build.VERSION.SDK_INT < 23 ? mContext.getResources().getColor(R.color.white) : mContext.getResources().getColor(R.color.white, null);
                ((ViewHolderNormal) holder).mCardView.setBackgroundColor(color);
                ((ViewHolderNormal) holder).mTvDeviceStatus.setText(mContext.getResources().getString(R.string.label_device_power_on));
            }
        }
    }

    @Override
    public int getItemCount() {
        return mLstDevices == null ? 0 : mLstDevices.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mLstDevices.get(position).getDeviceViewType();
    }


    public class ViewHolderNormal extends RecyclerView.ViewHolder {
        private ImageView mIvDeviceIcon;
        private TextView mTvDeviceName;
        private TextView mTvDeviceStatus;
        private MaterialCardView mCardView;

        public ViewHolderNormal(@NonNull View itemView) {
            super(itemView);

            mIvDeviceIcon = itemView.findViewById(R.id.imageView_itemControl_DeviceIcon);
            mTvDeviceName = itemView.findViewById(R.id.textView_itemControl_DeviceName);
            mTvDeviceStatus = itemView.findViewById(R.id.textView_itemControl_DeviceStatus);
            mCardView = itemView.findViewById(R.id.cardView_itemControl);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListenerClick != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListenerClick.onItemClick(position);
                        }
                    }
                }
            });

        }
    }

    public class ViewHolderSeekBar extends RecyclerView.ViewHolder {
        private ImageView mIvDeviceIcon;
        private TextView mTvDeviceName;
        private TextView mTvDeviceStatus;
        private SeekBar mSbDeviceRange;
        private MaterialCardView mCardView;

        private ViewHolderSeekBar(@NonNull View itemView) {
            super(itemView);

            mIvDeviceIcon = itemView.findViewById(R.id.imageView_itemControlSeekBar_DeviceIcon);
            mTvDeviceName = itemView.findViewById(R.id.textView_itemControlSeekBar_DeviceName);
            mTvDeviceStatus = itemView.findViewById(R.id.textView_itemControlSeekBar_DeviceStatus);
            mSbDeviceRange = itemView.findViewById(R.id.seekBar_itemControlSeekBar_DeviceRange);
            mCardView = itemView.findViewById(R.id.cardView_itemControl_SeekBar);

            mSbDeviceRange.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    int position = getAdapterPosition();
                    Device device = mLstDevices.get(position);
                    switch (device.getType().getID()) {
                        case Comm.DEVICE_TYPE_LIGHT:
                            mTvDeviceStatus.setText(mContext.getResources().getString(R.string.label_device_power_on) + "(" + device.getCurrentValue() + "%)");
                            break;
                        case Comm.DEVICE_TYPE_AC:
                            mTvDeviceStatus.setText(mContext.getResources().getString(R.string.label_device_power_on) + "(" + device.getCurrentValue() + "°C)");
                            break;
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    if (mListenerTracking != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            int progress = seekBar.getProgress();
                            mListenerTracking.onStopTrackingTouch(position, progress);
                        }
                    }
                }
            });

            mSbDeviceRange.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (mListenerTouch != null) {
                        mListenerTouch.onTouch(motionEvent);
                    }
                    view.onTouchEvent(motionEvent);
                    return true;
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListenerClick != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListenerClick.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public class ViewHolderCurtain extends RecyclerView.ViewHolder {
        private ImageView mIvDeviceIcon;
        private TextView mTvDeviceName;
        private TextView mTvDeviceStatus;
        private ImageButton mIbOpen;
        private ImageButton mIbClose;
        private ImageButton mIbStop;

        public ViewHolderCurtain(@NonNull View itemView) {
            super(itemView);

            mIvDeviceIcon = itemView.findViewById(R.id.imageView_itemControlCurtain_DeviceIcon);
            mTvDeviceName = itemView.findViewById(R.id.textView_itemControlCurtain_DeviceName);
            mTvDeviceStatus = itemView.findViewById(R.id.textView_itemControlCurtain_DeviceStatus);
            mIbOpen = itemView.findViewById(R.id.imageButton_itemControlCurtain_Open);
            mIbStop = itemView.findViewById(R.id.imageButton_itemControlCurtain_Stop);
            mIbClose = itemView.findViewById(R.id.imageButton_itemControlCurtain_Close);

            //Open = 1
            mIbOpen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        mListenerClick.onItemClick(position, 1);
                    }
                }
            });

            //Stop = 0
            mIbStop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        mListenerClick.onItemClick(position, 0);
                    }
                }
            });


            //Close = 2
            mIbClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        mListenerClick.onItemClick(position, 2);
                    }
                }
            });

        }
    }
}

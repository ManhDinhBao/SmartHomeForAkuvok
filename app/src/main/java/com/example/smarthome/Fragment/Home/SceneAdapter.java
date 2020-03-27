package com.example.smarthome.Fragment.Home;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smarthome.Model.Device.Device;
import com.example.smarthome.R;

import java.util.ArrayList;

public class SceneAdapter extends RecyclerView.Adapter<SceneAdapter.SceneViewHolder> {
    private Context mContext;
    private ArrayList<Device> mSceneDevices;
    private OnItemClickListener mListener;

    public SceneAdapter(Context context, ArrayList<Device> sceneDevices)
    {
        mContext=context;
        mSceneDevices=sceneDevices;
    }
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public SceneViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(mContext).inflate(R.layout.item_scene,parent,false);
        return new SceneViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SceneViewHolder holder, int position) {
        Device device= mSceneDevices.get(position);
        holder.mTvSceneName.setText(device.getName());
        holder.mIvSceneIcon.setImageResource(R.drawable.game_30px);

    }

    @Override
    public int getItemCount() {
        return mSceneDevices==null?0:mSceneDevices.size();
    }

    public class SceneViewHolder extends RecyclerView.ViewHolder {

        private TextView mTvSceneName;
        private ImageView mIvSceneIcon;

        public SceneViewHolder(@NonNull final View itemView) {
            super(itemView);

            mTvSceneName = itemView.findViewById(R.id.textView_itemScene_Name);
            mIvSceneIcon = itemView.findViewById(R.id.imageView_itemScene_Icon);

            //When user click scene
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick(position);

                        }

                    }
                }
            });
        }
    }
}

package com.example.smarthome.Fragment.Notification;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.smarthome.Model.Message.Message;
import com.example.smarthome.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class NotifyAdapter extends RecyclerView.Adapter<NotifyAdapter.NotifyViewHolder> {
    private Context mContext;
    private ArrayList<Message> mMessages;
    private NotifyAdapter.OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(NotifyAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public NotifyAdapter(Context context, ArrayList<Message> messages)
    {
        mContext=context;
        mMessages=messages;
    }

    @NonNull
    @Override
    public NotifyAdapter.NotifyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(mContext).inflate(R.layout.item_notification,parent,false);
        return new NotifyAdapter.NotifyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NotifyAdapter.NotifyViewHolder holder, int position) {
        Message message= mMessages.get(position);
        holder.mTvNotifyTitle.setText(message.getTitle());
        holder.mTvNotifyDetail.setText(message.getDetail());

        //Set notification send date follow format "30/12/2020"
        SimpleDateFormat formatterDate = new SimpleDateFormat("dd/MM/YYYY");
        holder.mTvNotifyDate.setText(formatterDate.format(message.getSendDate()));

        //If notification is emergency, set icon emergency
        if (message.isEmergency()){
            holder.mIvEmergencyIcon.setImageResource(R.drawable.ac_on_64px);
        }
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    public class NotifyViewHolder extends RecyclerView.ViewHolder {

        private TextView mTvNotifyTitle;
        private TextView mTvNotifyDetail;
        private TextView mTvNotifyDate;
        private ImageView mIvEmergencyIcon;

        public NotifyViewHolder(@NonNull View itemView) {
            super(itemView);

            mTvNotifyTitle = itemView.findViewById(R.id.textView_Notification_Title);
            mTvNotifyDetail = itemView.findViewById(R.id.textView_Notification_Title);
            mTvNotifyDate = itemView.findViewById(R.id.textView_Notification_Title);
            mIvEmergencyIcon = itemView.findViewById(R.id.textView_Notification_Title);

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

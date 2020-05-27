package com.lefatechs.smarthome.Controller;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.lefatechs.smarthome.Model.Message.Message;
import com.lefatechs.smarthome.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private Context mContext;
    private ArrayList<Message> mMessages;
    private MessageAdapter.OnItemClickListener mListener;
    private int rowIndex = -1;
    private int defaultIndex = 0;
    public MessageAdapter(Context context, ArrayList<Message> messages)
    {
        mContext=context;
        mMessages=messages;
    }
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(MessageAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public MessageAdapter.MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(mContext).inflate(R.layout.item_notification,parent,false);
        return new MessageAdapter.MessageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, final int position) {
        Message message = mMessages.get(position);
        holder.mTvMessageTitle.setText(message.getTitle());
        holder.mTvMessageDetail.setText(message.getDetail());
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String date = df.format(message.getSendDate());
        holder.mTvMessageDate.setText(date);


        if (rowIndex == position)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.mRlMessageItem.setBackgroundColor(ContextCompat.getColor(mContext, R.color.amber));
            } else {
                holder.mRlMessageItem.setBackgroundColor(mContext.getColor(R.color.amber));
            }
        }
        else
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.mRlMessageItem.setBackgroundColor(ContextCompat.getColor(mContext, R.color.transparent80));
            } else {
                holder.mRlMessageItem.setBackgroundColor(mContext.getColor(R.color.transparent80));
            }
        }

        if (position == defaultIndex){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.mRlMessageItem.setBackgroundColor(ContextCompat.getColor(mContext, R.color.amber));
            } else {
                holder.mRlMessageItem.setBackgroundColor(mContext.getColor(R.color.amber));
            }
            defaultIndex=-1;
        }

    }


    @Override
    public int getItemCount() {
        return mMessages==null?0:mMessages.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        private TextView mTvMessageTitle;
        private TextView mTvMessageDetail;
        private TextView mTvMessageDate;
        private RelativeLayout mRlMessageItem;
        public MessageViewHolder(@NonNull final View itemView) {
            super(itemView);
            mTvMessageTitle = itemView.findViewById(R.id.textView_Notification_Title);
            mTvMessageDetail = itemView.findViewById(R.id.textView_Notification_Detail);
            mTvMessageDate = itemView.findViewById(R.id.textView_Notification_Date);
            mRlMessageItem = itemView.findViewById(R.id.relativeLayout_Notification_Item);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();

                        mListener.onItemClick(position);
                        rowIndex = position;
                        notifyDataSetChanged();
                    }
                }
            });


        }
    }
}

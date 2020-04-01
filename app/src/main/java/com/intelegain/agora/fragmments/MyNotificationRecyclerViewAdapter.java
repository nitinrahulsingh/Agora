package com.intelegain.agora.fragmments;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.intelegain.agora.R;
import com.intelegain.agora.model.NotificationItem;

import java.util.ArrayList;
import java.util.List;


public class MyNotificationRecyclerViewAdapter extends RecyclerView.Adapter<MyNotificationRecyclerViewAdapter.ViewHolder> {

    Context mContext;
    private final List<NotificationItem> mValues;
    private final NotificationFragment.OnNotificationListener mListener;

    public MyNotificationRecyclerViewAdapter(Context context, List<NotificationItem> items, NotificationFragment.OnNotificationListener listener) {
        this.mContext = context;
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        List<String> aa = new ArrayList<>();
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).getTitle());
        holder.mContentView.setText(mValues.get(position).getMessage());
        switch (mValues.get(position).getStatus()) {
            case "Approved":
            case "approved":
                holder.mStatusImgView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_notify_approved));
                break;
            case "Rejected":
            case "rejected":
                holder.mStatusImgView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_notify_rejected));
                break;
            case "pending" :
            case "Pending" :
                holder.mStatusImgView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_notify_pending));
                break;
            default:
                holder.mStatusImgView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_notify));
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onNotification(mValues.get(holder.getAdapterPosition()).title);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView mIdView;
        final TextView mContentView;
        final ImageView mStatusImgView;
        public NotificationItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = view.findViewById(R.id.item_number);
            mContentView = view.findViewById(R.id.content);
            mStatusImgView = view.findViewById(R.id.img_notification_status);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}

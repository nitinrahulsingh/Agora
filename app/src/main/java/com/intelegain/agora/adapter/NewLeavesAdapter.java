package com.intelegain.agora.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.intelegain.agora.R;
import com.intelegain.agora.interfeces.OnItemLongClickListener;
import com.intelegain.agora.model.LeaveData;


import java.util.ArrayList;

/**
 * Created by suraj.m on 18/7/17.
 */

public class NewLeavesAdapter extends RecyclerView.Adapter<NewLeavesAdapter.LeaveViewHolder> {
    private String TAG = getClass().getSimpleName();
    private Activity activity;
    public ArrayList<LeaveData> mlstLeaveData, filteredList;
    OnItemLongClickListener onItemLongClickListener;

    public NewLeavesAdapter(Activity activity, ArrayList<LeaveData> mlstLeaveData, OnItemLongClickListener onItemLongClickListener) {
        this.activity = activity;
        this.filteredList = new ArrayList<>();
        this.mlstLeaveData = mlstLeaveData;
        // we copy the original list to the filter list and use it for setting row values
        this.filteredList.addAll(this.mlstLeaveData);
        this.onItemLongClickListener = onItemLongClickListener;
    }

    @Override
    public LeaveViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.leave_row_item, parent, false);
        return new LeaveViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LeaveViewHolder holder, final int position) {

        String strLeaveStatus = filteredList.get(position).getLeaveStatus();
        if (strLeaveStatus.equalsIgnoreCase("Pending")) {
            holder.imgBtnLeaveStatus.setImageResource(R.drawable.pending);
            holder.imgBtnLeaveStatus.setBackgroundResource(R.drawable.bg_cell_gray);
        } else if (strLeaveStatus.equalsIgnoreCase("Approved")) {
            holder.imgBtnLeaveStatus.setImageResource(R.drawable.approved);
            holder.imgBtnLeaveStatus.setBackgroundResource(R.drawable.bg_cell_green);
        } else if (strLeaveStatus.equalsIgnoreCase("Rejected")) {
            holder.imgBtnLeaveStatus.setImageResource(R.drawable.rejected);
            holder.imgBtnLeaveStatus.setBackgroundResource(R.drawable.bg_cell_gray);
        } else {
            // Do nothing
        }

        // To set leave Type
        String strLeaveType = filteredList.get(position).getLeaveType().toString();
        String strLeaveTypeShortName = "";
        if (strLeaveType.equalsIgnoreCase("Paid Leave")) {
            strLeaveTypeShortName = "PL";
        } else if (strLeaveType.equalsIgnoreCase("Comp Off")) {
            strLeaveTypeShortName = "CO";
        } else if (strLeaveType.equalsIgnoreCase("Casual Leave")) {
            strLeaveTypeShortName = "CL";
        } else if (strLeaveType.equalsIgnoreCase("Sick Leave")) {
            strLeaveTypeShortName = "SL";
        } else if (strLeaveType.equalsIgnoreCase("Leave (Without Pay)")) {
            strLeaveTypeShortName = "WL";
        }

        // Set leave Start date
        holder.tvLeaveStartDate.setText(filteredList.get(position).getLeaveFrom());
        // Set leave End date
        holder.tvLeaveEndDate.setText(filteredList.get(position).getLeaveTo());
        // Set leave reason title text
        holder.tvLeaveTitle_text.setText(filteredList.get(position).getLeaveStatus());
        // Set leave reason detail text
        //holder.tvLeaveDetail_text.setText(filteredList.get(position).getLeaveDesc());
        holder.tvLeaveDetail_text.setText(filteredList.get(position).getLeaveDesc());
        // Set leave type text
        holder.tvLeaveType.setText(strLeaveTypeShortName);

        if (filteredList.get(position).adminComments != null && !filteredList.get(position).adminComments.equals(""))
            holder.tvLeaveAdminComments.setText(filteredList.get(position).adminComments);
        else
            holder.tvLeaveAdminComments.setVisibility(View.GONE);

        if (strLeaveStatus.equalsIgnoreCase("Pending") /*|| strLeaveStatus.equalsIgnoreCase("Rejected")*/) {
//            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View view) {
//                    onItemLongClickListener.onItemLongClicked(position, filteredList.get(position).getLeaveId());
//                    return true;
//                }
//            });

            holder.tvSwipeToDelete.setVisibility(View.VISIBLE);
        }else{
            holder.tvSwipeToDelete.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return (null != filteredList ? filteredList.size() : 0);
        //return mlstLeaveData.size();
    }

    public  void deleteRow(int position){
        onItemLongClickListener.onItemLongClicked(position, filteredList.get(position).getLeaveId());
    }

    public void removeItem(int position) {
        filteredList.remove(position);
        notifyItemRemoved(position);
    }

    public class LeaveViewHolder extends RecyclerView.ViewHolder {
        private ImageButton imgBtnLeaveStatus;
        private TextView tvLeaveType, tvLeaveStartDate, tvLeaveEndDate, tvLeaveTitle_text, tvLeaveDetail_text, tvLeaveAdminComments,tvSwipeToDelete;

        public LeaveViewHolder(View itemView) {
            super(itemView);
            imgBtnLeaveStatus = itemView.findViewById(R.id.imgBtnLeaveStatus);
            tvLeaveType = itemView.findViewById(R.id.tvLeaveType);
            tvLeaveStartDate = itemView.findViewById(R.id.tvLeaveStartDate);
            tvLeaveEndDate = itemView.findViewById(R.id.tvLeaveEndDate);
            tvLeaveTitle_text = itemView.findViewById(R.id.tvLeaveTitle_text);
            tvLeaveDetail_text = itemView.findViewById(R.id.tvLeaveDetail_text);
            tvLeaveAdminComments = itemView.findViewById(R.id.tvLeaveAdminComments);
            tvSwipeToDelete = itemView.findViewById(R.id.tvSwipeToDelete);
        }
    }

    // Do Search...
    public void filter(final String text) {

        // Searching could be complex..so we will dispatch it to a different thread...
        new Thread(new Runnable() {
            @Override
            public void run() {

                // Clear the filter list
                filteredList.clear();

                // If there is no search value, then add all original list items to filter list
                if (TextUtils.isEmpty(text)) {

                    filteredList.addAll(mlstLeaveData);

                } else {
                    // Iterate in the original List and add it to filter list...
                    for (LeaveData item : mlstLeaveData) {

                        if (item.getLeaveStatus().toLowerCase().contains(text.toLowerCase()) ||
                                item.getLeaveStatus().toLowerCase().contains(text.toLowerCase())) {
                            filteredList.add(item);
                        }
                    }
                }

                // Set on UI Thread
                (activity).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Notify the List that the DataSet has changed...
                        notifyDataSetChanged();
                    }
                });

            }
        }).start();

    }

}

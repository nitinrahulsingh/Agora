package com.intelegain.agora.api.urls;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.intelegain.agora.R;
import com.intelegain.agora.interfeces.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pranay.s on 14/7/17.
 */

public class CommonRecyclerAdapter extends RecyclerView.Adapter<CommonRecyclerAdapter.CommonRecyclerViewHolder>
        implements Filterable {

    private Context mContext;
    private List<String> dataList;
    private List<String> dataListResult;
    private RecyclerItemClickListener itemClickListener;
    private ItemFilter mFilter = new ItemFilter();
    private String TAG = "CommonRecyclerAdapter";

    public CommonRecyclerAdapter(Context mContext, List<String> dataList, RecyclerItemClickListener
            itemClickListener) {
        this.mContext = mContext;
        this.dataList = dataList;
        this.dataListResult = dataList;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public CommonRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.common_recyclerview_row, null);
        CommonRecyclerViewHolder viewHolder = new CommonRecyclerViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CommonRecyclerViewHolder holder, final int position) {

        holder.itemName.setText(dataList.get(position));
        holder.itemName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.recyclerViewListClicked(position, dataList.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new ItemFilter();
        }
        return mFilter;
    }

    //******************* View Holder ***************************************

    public class CommonRecyclerViewHolder extends RecyclerView.ViewHolder {


        TextView itemName;

        public CommonRecyclerViewHolder(View itemView) {
            super(itemView);

            itemName = (TextView) itemView.findViewById(R.id.txt_recycler_item);

        }
    }


    //******************* Filterable ***************************************

    public class ItemFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults results = new FilterResults();


            Log.e(TAG, "constraint:  " + constraint);

            if ((constraint == null || constraint.length() == 0 || constraint.length() < 2)) {
                results.values = dataListResult;
                results.count = dataListResult.size();
            } else {

                List<String> filteredTask = new ArrayList();
                for (String c : dataList) {
                    if (c.toUpperCase().contains(constraint.toString().toUpperCase())) {
                        // if `contains` == true then add it
                        // to our filtered list
                        filteredTask.add(c);
                    }
                }
                results.values = filteredTask;
                results.count = filteredTask.size();

            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            dataList = (List<String>) results.values;
            notifyDataSetChanged();

        }
    }
}

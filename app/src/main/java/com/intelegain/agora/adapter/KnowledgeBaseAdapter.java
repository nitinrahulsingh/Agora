

package com.intelegain.agora.adapter;

import android.app.Activity;
import android.graphics.Paint;

import android.text.Html;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.intelegain.agora.R;
import com.intelegain.agora.interfeces.IKnlodedgeBaseAttachment;
import com.intelegain.agora.model.KnowledgeBaseData;
import com.intelegain.agora.model.KnowledgeTagList;
import com.intelegain.agora.utils.Contants2;

import java.util.ArrayList;


public class KnowledgeBaseAdapter extends RecyclerView.Adapter<KnowledgeBaseAdapter.KnowledgeBaseViewHolder> {

    public static int FILTER_WITH_TAG_LIST = 0;
    public static int FILTER_WITH_TECHNOLOGY = 1;
    private String TAG = getClass().getSimpleName();
    private Activity activity;
    private ArrayList<KnowledgeBaseData> mlstKnowledgeBaseData, filteredList;


    private int expandedPosition = -1;
    private String strToken,strEmpId;
    private IKnlodedgeBaseAttachment iKnlodedgeBaseAttachment;
    public KnowledgeBaseAdapter(Activity activity, ArrayList<KnowledgeBaseData> lstKnowledgeBaseData, String token, String empId, IKnlodedgeBaseAttachment iKnlodedgeBaseAttachment) {
        this.activity = activity;
        this.filteredList = new ArrayList<KnowledgeBaseData>();
        this.mlstKnowledgeBaseData = lstKnowledgeBaseData;
        // we copy the original list to the filter list and use it for setting row values
        this.filteredList.addAll(this.mlstKnowledgeBaseData);

        this.strToken=token;
        this.strEmpId=empId;
        this.iKnlodedgeBaseAttachment=iKnlodedgeBaseAttachment;
    }

    @Override
    public KnowledgeBaseAdapter.KnowledgeBaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_knowledge_base, parent, false);
        return new KnowledgeBaseAdapter.KnowledgeBaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final KnowledgeBaseAdapter.KnowledgeBaseViewHolder holder, int position) {
        final KnowledgeBaseData knowledgeBaseData = filteredList.get(position);

        holder.knowledge_title.setText(knowledgeBaseData.getKnowledgeTitle());
        holder.knowledge_project_name.setText(knowledgeBaseData.getKnowledgeProjectTitle());
        holder.knowledge_technology.setText(knowledgeBaseData.getKnowledgeTechnology());
        holder.knowledge_posted_by.setText(knowledgeBaseData.getKnowledgePostedBy());
        holder.knowledge_posted_date.setText(knowledgeBaseData.getKnowledgePostedDate());

        if (knowledgeBaseData.getKnowledgeDescription() != null && !knowledgeBaseData.getKnowledgeDescription().equals("")
                && !knowledgeBaseData.getKnowledgeDescription().equals("null")) {
            holder.knowledge_description.setVisibility(View.VISIBLE);
            //holder.knowledge_description.setText(knowledgeBaseData.getKnowledgeDescription());
            holder.knowledge_description.setText(Html.fromHtml(knowledgeBaseData.getKnowledgeDescription()));
        } else {
            holder.knowledge_description.setVisibility(View.GONE);
        }

        if (knowledgeBaseData.getKnowledegeFileName() != null && !knowledgeBaseData.getKnowledegeFileName().equals("null")
         && !knowledgeBaseData.getKnowledegeFileName().equals("")) {

            // holder.knowledge_file_name.setVisibility(View.VISIBLE);

            String strFileName[] = knowledgeBaseData.getKnowledegeFileName().split(",");
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            LinearLayout ll_file_tag = new LinearLayout(activity);
            ll_file_tag.setLayoutParams(params); //Layout params for Button

            for (int i = 0; i < strFileName.length; i++) {
                final TextView fileName=new TextView(activity);
                ImageView imageView=new ImageView(activity);
                imageView.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_attachment));
                imageView.setPadding(0,3,0,0);

                //fileName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_attachment,0,0,0);
                //fileName.setCompoundDrawablePadding(1);
                fileName.setId(i);//Setting Id for using in future
                fileName.setText(strFileName[i]);
                fileName.setPadding(4,0,10,0);

                fileName.setPaintFlags(fileName.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
                ll_file_tag.setOrientation(LinearLayout.HORIZONTAL);//Setting Layout orientation
                ll_file_tag.addView(imageView);
                ll_file_tag.addView(fileName);//Finally adding view
                fileName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        iKnlodedgeBaseAttachment.getAttachment(knowledgeBaseData.getKnowledgeId(),fileName.getText().toString());

                        /*
                        // create interface to do same as done and check file type
                        Toast.makeText(activity, "downloading file "+fileName.getText(), Toast.LENGTH_SHORT).show();
                        Intent startIntent = new Intent(activity,
                                AttachmentDownloderService.class);
                        startIntent.putExtra(Contants2.KBID, knowledgeBaseData.getKnowledgeId());
                        startIntent.putExtra(Contants2.KB_FILE_NAME,fileName.getText());
                        startIntent.putExtra(Contants2.EMP_ID,strEmpId);
                        startIntent.putExtra(Contants2.TOKEN,strToken);
                        activity.startService(startIntent);*/

                    }
                });
            }
            holder.ll_knowledge_file.removeAllViews();
            holder.ll_knowledge_file.addView(ll_file_tag);
//            holder.ll_knowledge_file.setVisibility(View.VISIBLE);
        } else {
            holder.ll_knowledge_file.setVisibility(View.GONE);
        }

        if (knowledgeBaseData.getKnowledgeUrl() != null && !knowledgeBaseData.getKnowledgeUrl().equals("")) {
            holder.knowledge_url.setVisibility(View.VISIBLE);
            holder.knowledge_url.setText(knowledgeBaseData.getKnowledgeUrl());
        } else {
            holder.knowledge_url.setVisibility(View.GONE);
        }

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout linearLayoutForTags = new LinearLayout(activity);
        linearLayoutForTags.setLayoutParams(params);

        ArrayList<KnowledgeTagList> knowledgeTagList = knowledgeBaseData.getKnowledgeTagList();
        if (knowledgeTagList != null) {
            int iTagListSize = knowledgeTagList.size();
            for (int iCnt = 0; iCnt < iTagListSize; iCnt++) {
                LinearLayout.LayoutParams paramForTextview = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                paramForTextview.setMargins(0, 10, 5, 10);

                TextView tvKnowledgeTags = new TextView(activity);
                tvKnowledgeTags.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                tvKnowledgeTags.setPadding(15, 5, 15, 5);
                tvKnowledgeTags.setGravity(Gravity.CENTER);
                tvKnowledgeTags.setTextSize(12);
                tvKnowledgeTags.setTextColor(ContextCompat.getColor(activity, R.color.new_dark_grey));
                tvKnowledgeTags.setBackgroundDrawable(ContextCompat.getDrawable(activity, R.drawable.rounded_button_tag));
                tvKnowledgeTags.setLayoutParams(paramForTextview);
                tvKnowledgeTags.setText(knowledgeTagList.get(iCnt).getTagName());

                linearLayoutForTags.addView(tvKnowledgeTags);
            }
            holder.linearLayoutForTags.removeAllViews();
            holder.linearLayoutForTags.addView(linearLayoutForTags);
        }


        /** expand collapse view*/
        if (position == expandedPosition) {
            holder.ll_knowledge_detail.setVisibility(View.VISIBLE);
            holder.img_expand_collapse.setImageResource(R.drawable.ic_arrow_up);
        } else {
            holder.ll_knowledge_detail.setVisibility(View.GONE);
            holder.img_expand_collapse.setImageResource(R.drawable.ic_arrow_down);
        }

        holder.knowledge_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Check for an expanded view, collapse if you find one
                if (expandedPosition >= 0) {
                    int prev = expandedPosition;
                    notifyItemChanged(prev);
                }
                if (holder.ll_knowledge_detail.getVisibility() == View.VISIBLE) {
                    expandedPosition = -1;
                } else {
                    // Set the current position to "expanded"
                    expandedPosition = holder.getPosition();
                    notifyItemChanged(expandedPosition);
                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return (null != filteredList ? filteredList.size() : 0);
        //return mlstKnowledgeBaseData.size();
    }

    // Do Search...
    public void filter(final String text, final int filterType) {

        // Searching could be complex..so we will dispatch it to a different thread...
        new Thread(new Runnable() {
            @Override
            public void run() {

                // Clear the filter list
                filteredList.clear();

                // If there is no search value, then add all original list items to filter list
                if (TextUtils.isEmpty(text)) {
                    if (mlstKnowledgeBaseData != null)
                        filteredList.addAll(mlstKnowledgeBaseData);

                } else {
                    if (mlstKnowledgeBaseData != null) {
                        // Iterate in the original List and add it to filter list...
                        for (KnowledgeBaseData item : mlstKnowledgeBaseData) {
                            if (filterType == FILTER_WITH_TAG_LIST) {
                                ArrayList<KnowledgeTagList> KnowledgeTagList = item.getKnowledgeTagList();
                                if (KnowledgeTagList != null) {
                                    int iTagListLength = KnowledgeTagList.size();
                                    for (int iCnt = 0; iCnt < iTagListLength; iCnt++) {
                                        // filtered on basis of  title, categories, tags & creator
                                        if (item.getKnowledgeTagList().get(iCnt).getTagName().toLowerCase().contains(text.toLowerCase()) ||
                                                item.getKnowledgeTitle().toLowerCase().contains(text.toLowerCase()) ||
                                                item.getKnowledgePostedBy().toLowerCase().contains(text.toLowerCase()) ||
                                                item.getKnowledgeProjectTitle().toLowerCase().contains(text.toLowerCase())) {
                                            // Adding Matched items
                                            filteredList.add(item);
                                        }
                                    }
                                }
                            } else if (filterType == FILTER_WITH_TECHNOLOGY) {
                                String strTechnology = item.getKnowledgeTechnology();
                                if (!strTechnology.isEmpty()) {
                                    if (strTechnology.toLowerCase().contains(text.toLowerCase()) ||
                                            strTechnology.toLowerCase().contains(text.toLowerCase())) {
                                        // Adding Matched items
                                        filteredList.add(item);
                                    }
                                }
                            }
                        }
                    }
                }

                // Set on UI Thread
                (activity).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Notify the List that the DataSet has changed...
                        if (filteredList.size() == 0)
                            Contants2.showToastMessageAtCenter(activity, activity.getString(R.string.no_data_found), false);
                        notifyDataSetChanged();
                    }
                });

            }
        }).start();

    }

    public class KnowledgeBaseViewHolder extends RecyclerView.ViewHolder {
        private TextView knowledge_title, knowledge_project_name, knowledge_technology,
                knowledge_posted_by, knowledge_posted_date, knowledge_description,  knowledge_url;
        private LinearLayout linearLayoutForTags, ll_knowledge_detail, knowledge_row, ll_knowledge_file;
        private ImageView img_expand_collapse;

        public KnowledgeBaseViewHolder(View itemView) {
            super(itemView);
            //horizontalScrollViewForTags = (HorizontalScrollView) itemView.findViewById(R.id.horizontal_scrollview_for_tags);
            knowledge_title = (TextView) itemView.findViewById(R.id.knowledge_title);
            knowledge_project_name = (TextView) itemView.findViewById(R.id.knowledge_project_name);
            knowledge_technology = (TextView) itemView.findViewById(R.id.knowledge_technology);
            knowledge_posted_by = (TextView) itemView.findViewById(R.id.knowledge_posted_by);
            knowledge_posted_date = (TextView) itemView.findViewById(R.id.knowledge_posted_date);
            linearLayoutForTags = (LinearLayout) itemView.findViewById(R.id.linearLayoutForTags);
            ll_knowledge_detail = (LinearLayout) itemView.findViewById(R.id.ll_knowledge_detail);
            img_expand_collapse = (ImageView) itemView.findViewById(R.id.img_expand_collapse);
            knowledge_row = (LinearLayout) itemView.findViewById(R.id.knowledge_row);
            knowledge_description = (TextView) itemView.findViewById(R.id.knowledge_description);
            //knowledge_file_name = (TextView) itemView.findViewById(R.id.knowledge_file_name);
            knowledge_url = (TextView) itemView.findViewById(R.id.knowledge_url);
            ll_knowledge_file = (LinearLayout) itemView.findViewById(R.id.ll_knowledge_file);
        }
    }

}
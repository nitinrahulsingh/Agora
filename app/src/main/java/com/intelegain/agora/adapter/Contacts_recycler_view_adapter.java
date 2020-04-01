package com.intelegain.agora.adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.intelegain.agora.R;
import com.intelegain.agora.model.Employee_contacts;
import com.intelegain.agora.utils.Contants2;

import java.util.ArrayList;
import java.util.List;

public class Contacts_recycler_view_adapter extends RecyclerView.Adapter<Contacts_recycler_view_adapter.ContactsViewHolder> implements Filterable {
    Activity mActivity;
    public ArrayList<Employee_contacts> contactsList, filteredList = new ArrayList<Employee_contacts>();
    ContactsViewHolder contactsViewHolder;
    Intent intentToCall, intentToEmail;

    private ItemFilter mFilter = new ItemFilter();


    public Contacts_recycler_view_adapter(Activity mActivity, ArrayList<Employee_contacts> contactsList) {
        this.mActivity = mActivity;
        this.contactsList = contactsList;
        this.filteredList = contactsList;
    }


    @Override
    public ContactsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.contacts_recycler_view_row, null);
        contactsViewHolder = new ContactsViewHolder(view);
        return contactsViewHolder;
    }

    @Override
    public void onBindViewHolder(ContactsViewHolder holder, int position) {

        if (!contactsList.get(position).isSwiped) {
            // view is not right swiped
            /*holder.relativeLayoutEmpDetailsContainer.setVisibility(View.VISIBLE);
            holder.relativeLayoutCallContainer.setVisibility(View.INVISIBLE);*/


        } else {
            // view is right swiped
            /*holder.relativeLayoutEmpDetailsContainer.setVisibility(View.INVISIBLE);
            holder.relativeLayoutCallContainer.setVisibility(View.VISIBLE);*/

            if (!TextUtils.isEmpty(contactsList.get(position).empContact)) {
                intentToCall = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + contactsList.get(position).empContact));
                mActivity.startActivity(intentToCall);
            }


            /*holder.relativeLayoutEmpDetailsContainer.setVisibility(View.VISIBLE);
            holder.relativeLayoutCallContainer.setVisibility(View.INVISIBLE);*/
            contactsList.get(position).isSwiped = false;

        }
        // set image of emp here
        //holder.imgProfilePic.setImageDrawable(R.id.);
        holder.tvEmpName.setText(TextUtils.isEmpty(contactsList.get(position).empName) ? "" : contactsList.get(position).empName);
        holder.tvEmpDesignation.setText(TextUtils.isEmpty(contactsList.get(position).designation) ? "" : contactsList.get(position).designation);

        holder.imgMail.setTag(position);
        holder.imgPhoneCall.setTag(position);

        holder.imgMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(contactsList.get(Integer.parseInt(v.getTag().toString())).empEmail)) {
                    intentToEmail = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + contactsList.get(Integer.parseInt(v.getTag().toString())).empEmail));
                    mActivity.startActivity(intentToEmail);
                }

            }
        });

        holder.imgPhoneCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(contactsList.get(Integer.parseInt(v.getTag().toString())).empContact)) {
                    intentToCall = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + contactsList.get(Integer.parseInt(v.getTag().toString())).empContact));
                    mActivity.startActivity(intentToCall);
                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return contactsList.size();
    }

    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new ItemFilter();
        }
        return mFilter;
    }

    public static class ContactsViewHolder extends RecyclerView.ViewHolder {


        TextView tvEmpName, tvEmpDesignation/*, tvCall*/;

        ImageView imgProfilePic, imgMail, imgPhoneCall;

        RelativeLayout relativeLayoutEmpDetailsContainer/*, relativeLayoutCallContainer*/;


        public ContactsViewHolder(View itemView) {
            super(itemView);

            relativeLayoutEmpDetailsContainer = itemView.findViewById(R.id.relative_recycler_emp_details_container);
            //relativeLayoutCallContainer = itemView.findViewById(R.id.relative_call_container);

            imgProfilePic = itemView.findViewById(R.id.imgContactsProfile);
            imgMail = itemView.findViewById(R.id.img_recycler_row_mail);
            imgPhoneCall = itemView.findViewById(R.id.img_recycler_row_call);

            tvEmpName = itemView.findViewById(R.id.tvEmpName);
            tvEmpDesignation = itemView.findViewById(R.id.tvDesignation);
            //tvCall = itemView.findViewById(R.id.tvCall);

        }

    }

    private class ItemFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults results = new FilterResults();

            if ((constraint == null || constraint.length() == 0) || constraint.length() < 3) {
                results.values = filteredList;
                results.count = filteredList.size();
            } else {

                List<Employee_contacts> filteredTask = new ArrayList();
                for (Employee_contacts c : filteredList) {
                    if (c.empName.toUpperCase().contains(constraint.toString().toUpperCase())) {
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
            contactsList = (ArrayList<Employee_contacts>) results.values;
            if (contactsList.size() == 0)
                Contants2.showToastMessageAtCenter(mActivity, mActivity.getString(R.string.no_data_found), false);
            notifyDataSetChanged();

        }
    }

}
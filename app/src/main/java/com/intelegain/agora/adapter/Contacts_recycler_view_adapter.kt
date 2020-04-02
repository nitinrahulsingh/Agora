package com.intelegain.agora.adapter

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.intelegain.agora.R
import com.intelegain.agora.adapter.Contacts_recycler_view_adapter.ContactsViewHolder
import com.intelegain.agora.model.Employee_contacts
import com.intelegain.agora.utils.Contants2
import java.util.*

class Contacts_recycler_view_adapter(var mActivity: Activity, var contactsList: ArrayList<Employee_contacts>) : RecyclerView.Adapter<ContactsViewHolder>(), Filterable {
    var filteredList = ArrayList<Employee_contacts>()
    var contactsViewHolder: ContactsViewHolder? = null
    var intentToCall: Intent? = null
    var intentToEmail: Intent? = null
    private var mFilter = ItemFilter()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {
        val view = LayoutInflater.from(mActivity).inflate(R.layout.contacts_recycler_view_row, null)
        contactsViewHolder = ContactsViewHolder(view)
        return contactsViewHolder!!
    }

    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
        if (!contactsList[position].isSwiped) { // view is not right swiped
/*holder.relativeLayoutEmpDetailsContainer.setVisibility(View.VISIBLE);
            holder.relativeLayoutCallContainer.setVisibility(View.INVISIBLE);*/
        } else { // view is right swiped
/*holder.relativeLayoutEmpDetailsContainer.setVisibility(View.INVISIBLE);
            holder.relativeLayoutCallContainer.setVisibility(View.VISIBLE);*/
            if (!TextUtils.isEmpty(contactsList[position].empContact)) {
                intentToCall = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + contactsList[position].empContact))
                mActivity.startActivity(intentToCall)
            }
            /*holder.relativeLayoutEmpDetailsContainer.setVisibility(View.VISIBLE);
            holder.relativeLayoutCallContainer.setVisibility(View.INVISIBLE);*/contactsList[position].isSwiped = false
        }
        // set image of emp here
//holder.imgProfilePic.setImageDrawable(R.id.);
        holder.tvEmpName.text = if (TextUtils.isEmpty(contactsList[position].empName)) "" else contactsList[position].empName
        holder.tvEmpDesignation.text = if (TextUtils.isEmpty(contactsList[position].designation)) "" else contactsList[position].designation
        holder.imgMail.tag = position
        holder.imgPhoneCall.tag = position
        holder.imgMail.setOnClickListener { v ->
            if (!TextUtils.isEmpty(contactsList[v.tag.toString().toInt()].empEmail)) {
                intentToEmail = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + contactsList[v.tag.toString().toInt()].empEmail))
                mActivity.startActivity(intentToEmail)
            }
        }
        holder.imgPhoneCall.setOnClickListener { v ->
            if (!TextUtils.isEmpty(contactsList[v.tag.toString().toInt()].empContact)) {
                intentToCall = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + contactsList[v.tag.toString().toInt()].empContact))
                mActivity.startActivity(intentToCall)
            }
        }
    }

    override fun getItemCount(): Int {
        return contactsList.size
    }

    override fun getFilter(): Filter {
        if (mFilter == null) {
            mFilter = ItemFilter()
        }
        return mFilter
    }

    class ContactsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvEmpName: TextView
        var tvEmpDesignation /*, tvCall*/: TextView
        var imgProfilePic: ImageView
        var imgMail: ImageView
        var imgPhoneCall: ImageView
        var relativeLayoutEmpDetailsContainer /*, relativeLayoutCallContainer*/: RelativeLayout

        init {
            relativeLayoutEmpDetailsContainer = itemView.findViewById(R.id.relative_recycler_emp_details_container)
            //relativeLayoutCallContainer = itemView.findViewById(R.id.relative_call_container);
            imgProfilePic = itemView.findViewById(R.id.imgContactsProfile)
            imgMail = itemView.findViewById(R.id.img_recycler_row_mail)
            imgPhoneCall = itemView.findViewById(R.id.img_recycler_row_call)
            tvEmpName = itemView.findViewById(R.id.tvEmpName)
            tvEmpDesignation = itemView.findViewById(R.id.tvDesignation)
            //tvCall = itemView.findViewById(R.id.tvCall);
        }
    }

    private inner class ItemFilter : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {
            val results = FilterResults()
            if (constraint == null || constraint.length == 0 || constraint.length < 3) {
                results.values = filteredList
                results.count = filteredList.size
            } else {
                val filteredTask: MutableList<Employee_contacts?> = ArrayList<Employee_contacts?>()
                for (c in filteredList) {
                    if (c.empName!!.toUpperCase().contains(constraint.toString().toUpperCase())) { // if `contains` == true then add it
// to our filtered list
                        filteredTask.add(c)
                    }
                }
                results.values = filteredTask
                results.count = filteredTask.size
            }
            return results
        }

        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            contactsList = results.values as ArrayList<Employee_contacts>
            if (contactsList.size == 0) Contants2.showToastMessageAtCenter(mActivity, mActivity.getString(R.string.no_data_found), false)
            notifyDataSetChanged()
        }
    }

    init {
        filteredList = contactsList
    }
}
package com.intelegain.agora.adapter

import android.app.Activity
import android.app.Dialog
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.intelegain.agora.R
import com.intelegain.agora.api.urls.CommonMethods
import com.intelegain.agora.interfeces.RecyclerItemClickListener
import com.intelegain.agora.model.SkillMatrixData.EmpSkill
import com.intelegain.agora.utils.Contants2
import java.util.*

/**
 * Created by maninder on 2/8/17.
 */
class SkillMatrixRecyclerAdapter(private val mActivity: Activity, employee_skillList: ArrayList<EmpSkill>?) : RecyclerView.Adapter<SkillMatrixRecyclerAdapter.MyViewHolder>(), View.OnClickListener /*, Filterable*/ {
    private val skillList: List<EmpSkill>?
    private val filteredSkillList: MutableList<EmpSkill> = ArrayList()
    private val commonMethods: CommonMethods
    private val dialog: Dialog
    private val dialogView: View? = null
    private val inflater: LayoutInflater
    private val mDialogRecyclerView: RecyclerView? = null
    private val arrSkillLevel = arrayOf("Beginner", "Intermediate", "Expert")
    private val selectSkillLevelList: List<String>
    var contants2: Contants2
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_skill_matrix_row, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.tvSkillTitle.text = filteredSkillList[position].skillName
        holder.tvSkillSubTitle.text = filteredSkillList[position].category
        holder.tvSkillYear.text = filteredSkillList[position].years
        holder.tvSkillMonth.text = filteredSkillList[position].months
        holder.tvSkillLevel.text = filteredSkillList[position].level
        holder.checkBoxSkill.isChecked = filteredSkillList[position].activeSkill!!
        holder.checkBoxSkill.tag = filteredSkillList[position].skillID
    }

    override fun getItemCount(): Int {
        return filteredSkillList.size
    }

    override fun onClick(v: View) {}
    /*@Override
    public Filter getFilter() {
        if (skillMatrixFilter == null)
            skillMatrixFilter = new SkillMatrixFilter(this, skillList);
        return skillMatrixFilter;
    }*/
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvSkillTitle: TextView
        var tvSkillSubTitle: TextView
        var tvSkillYear: TextView
        var tvSkillMonth: TextView
        var tvSkillLevel: TextView
        var spinnerSkillLevel: Spinner? = null
        var imageViewDropDownArrow: ImageView
        var imgMinusYear: ImageView
        var imgPlusYear: ImageView
        var imgMinusMonth: ImageView
        var imgPlusMonth: ImageView
        var checkBoxSkill: CheckBox
        var isSkillChecked = false

        init {
            tvSkillTitle = itemView.findViewById(R.id.tvSkillTitle)
            tvSkillSubTitle = itemView.findViewById(R.id.tvSkillSubTitle)
            checkBoxSkill = itemView.findViewById(R.id.checkboxSkillMatrix)
            tvSkillYear = itemView.findViewById(R.id.tvCountYear)
            tvSkillMonth = itemView.findViewById(R.id.tvCountMonth)
            tvSkillLevel = itemView.findViewById(R.id.tvSkillLevel)
            imgMinusYear = itemView.findViewById(R.id.tvMinusYear)
            imgPlusYear = itemView.findViewById(R.id.tvPlusYear)
            imgMinusMonth = itemView.findViewById(R.id.tvMinusMonth)
            imgPlusMonth = itemView.findViewById(R.id.tvPlusMonth)
            imageViewDropDownArrow = itemView.findViewById(R.id.imgDownArrow)
            imgMinusYear.setOnClickListener(View.OnClickListener {
                if (tvSkillYear.text.toString() == "0") {
                    return@OnClickListener
                } else {
                    if (tvSkillMonth.text.toString() == "0") checkBoxSkill.isChecked = false
                    tvSkillYear.setText((tvSkillYear.text.toString().toInt() - 1).toString() + "")
                    if (checkBoxSkill.isChecked) { //skillList.
                        skillList!![getPositionByProperty(checkBoxSkill.tag.toString())].valueHasChanged = true
                        skillList[getPositionByProperty(checkBoxSkill.tag.toString())].years = tvSkillYear.text.toString()
                        calculateExperience(tvSkillYear.text.toString(), tvSkillMonth.text.toString(), getPositionByProperty(checkBoxSkill.tag.toString()))
                    }
                }
            })
            imgPlusYear.setOnClickListener(View.OnClickListener {
                if (tvSkillYear.text.toString() == "50") {
                    return@OnClickListener
                } else {
                    tvSkillYear.setText((tvSkillYear.text.toString().toInt() + 1).toString() + "")
                    if (checkBoxSkill.isChecked) {
                        skillList!![getPositionByProperty(checkBoxSkill.tag.toString())].valueHasChanged = true
                        skillList[getPositionByProperty(checkBoxSkill.tag.toString())].years = tvSkillYear.text.toString()
                        calculateExperience(tvSkillYear.text.toString(), tvSkillMonth.text.toString(), getPositionByProperty(checkBoxSkill.tag.toString()))
                    }
                }
            })
            imgMinusMonth.setOnClickListener(View.OnClickListener {
                if (tvSkillMonth.text.toString() == "0") {
                    return@OnClickListener
                } else {
                    if (tvSkillYear.text.toString() == "0") checkBoxSkill.isChecked = false
                    tvSkillMonth.setText((tvSkillMonth.text.toString().toInt() - 1).toString() + "")
                    if (checkBoxSkill.isChecked) {
                        skillList!![getPositionByProperty(checkBoxSkill.tag.toString())].valueHasChanged = true
                        skillList[getPositionByProperty(checkBoxSkill.tag.toString())].months = tvSkillMonth.text.toString()
                        calculateExperience(tvSkillYear.text.toString(), tvSkillMonth.text.toString(), getPositionByProperty(checkBoxSkill.tag.toString()))
                    }
                }
            })
            imgPlusMonth.setOnClickListener(View.OnClickListener {
                if (tvSkillMonth.text.toString() == "11") {
                    return@OnClickListener
                } else {
                    tvSkillMonth.setText((tvSkillMonth.text.toString().toInt() + 1).toString() + "")
                    if (checkBoxSkill.isChecked) {
                        skillList!![getPositionByProperty(checkBoxSkill.tag.toString())].valueHasChanged = true
                        skillList[getPositionByProperty(checkBoxSkill.tag.toString())].months = tvSkillMonth.text.toString()
                        calculateExperience(tvSkillYear.text.toString(), tvSkillMonth.text.toString(), getPositionByProperty(checkBoxSkill.tag.toString()))
                    }
                }
            })
            tvSkillLevel.setOnClickListener {
                commonMethods.customSpinner(mActivity, "Select Level", inflater, mDialogRecyclerView, selectSkillLevelList, dialog,
                        dialogView,
                        object : RecyclerItemClickListener {
                            override fun recyclerViewListClicked(position: Int, itemClickText: String?) {
                                tvSkillLevel.text = itemClickText
                                if (checkBoxSkill.isChecked) {
                                    skillList!![getPositionByProperty(checkBoxSkill.tag.toString())].valueHasChanged = true
                                    skillList[getPositionByProperty(checkBoxSkill.tag.toString())].level = itemClickText
                                }
                                dialog.hide()
                            }
                        })
            }
            imageViewDropDownArrow.setOnClickListener {
                commonMethods.customSpinner(mActivity, "Select Level", inflater, mDialogRecyclerView, selectSkillLevelList, dialog,
                        dialogView,
                        object : RecyclerItemClickListener {
                            override fun recyclerViewListClicked(position: Int, itemClickText: String?) {
                                tvSkillLevel.text = itemClickText
                                if (checkBoxSkill.isChecked) {
                                    skillList!![getPositionByProperty(checkBoxSkill.tag.toString())].valueHasChanged = true
                                    skillList[getPositionByProperty(checkBoxSkill.tag.toString())].level = itemClickText
                                }
                                dialog.hide()
                            }
                        })
            }
            checkBoxSkill.setOnClickListener {
                if (checkBoxSkill.isChecked) {
                    if (tvSkillYear.text.toString() == "0" && tvSkillMonth.text.toString() == "0" || tvSkillLevel.text.toString() == "") {
                        Contants2.showToastMessage(mActivity, mActivity.getString(R.string.skill_validation_message), false)
                        checkBoxSkill.isChecked = false
                    } else {
                        skillList!![getPositionByProperty(checkBoxSkill.tag.toString())].valueHasChanged = true
                        skillList[getPositionByProperty(checkBoxSkill.tag.toString())].activeSkill = true
                        skillList[getPositionByProperty(checkBoxSkill.tag.toString())].years = tvSkillYear.text.toString()
                        skillList[getPositionByProperty(checkBoxSkill.tag.toString())].months = tvSkillMonth.text.toString()
                        skillList[getPositionByProperty(checkBoxSkill.tag.toString())].level = tvSkillLevel.text.toString()
                        calculateExperience(tvSkillYear.text.toString(), tvSkillMonth.text.toString(), getPositionByProperty(checkBoxSkill.tag.toString()))
                    }
                } else {
                    skillList!![getPositionByProperty(checkBoxSkill.tag.toString())].valueHasChanged = true
                    skillList[getPositionByProperty(checkBoxSkill.tag.toString())].activeSkill = false
                }
            }
            /* checkBoxSkill.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        if ((tvSkillYear.getText().toString().equals("0") && tvSkillMonth.getText().toString().equals("0")) || tvSkillLevel.getText().toString().equals("")) {
                            contants2.showToastMessage(mActivity, mActivity.getString(R.string.fill_valid_details), false);
                            buttonView.setChecked(false);
                        }
                    }
                }
            });*/
        }
    }

    /**
     * @param tvYearValue  value in years
     * @param tvMonthValue value in years
     */
    private fun calculateExperience(tvYearValue: String, tvMonthValue: String, position: Int) {
        skillList!![position].experience = (tvYearValue.toInt() * 12 + tvMonthValue.toInt()).toString() + ""
    }

    // Do Search...
    fun filter(text: String /*, final int filterType*/) { // Searching could be complex..so we will dispatch it to a different thread...
        Thread(Runnable {
            // Clear the filter list
            filteredSkillList.clear()
            // If there is no search value, then add all original list items to filter list
            if (TextUtils.isEmpty(text)) {
                if (skillList != null) filteredSkillList.addAll(skillList)
            } else {
                if (skillList != null) // Iterate in the original List and add it to filter list...
                    for (emp_skill in skillList) {
                        if (emp_skill.skillName!!.toLowerCase().contains(text.toLowerCase())) filteredSkillList.add(emp_skill)
                    }
            }
            // Set on UI Thread
            mActivity.runOnUiThread {
                if (filteredSkillList.size == 0) Contants2.showToastMessageAtCenter(mActivity, mActivity.getString(R.string.no_data_found), false)
                // Notify the List that the DataSet has changed...
                notifyDataSetChanged()
            }
        }).start()
    }

    fun getPositionByProperty(skillId: String?): Int {
        var value = -1
        for (i in skillList!!.indices) {
            if (skillList[i].skillID!!.contains(skillId!!)) {
                value = i
                break
            }
        }
        return value
    }

    init {
        skillList = employee_skillList
        filteredSkillList.addAll(employee_skillList!!)
        selectSkillLevelList = Arrays.asList(*arrSkillLevel)
        contants2 = Contants2()
        // dialog
        commonMethods = CommonMethods()
        inflater = mActivity.layoutInflater
        dialog = Dialog(mActivity)
        dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
    }
}
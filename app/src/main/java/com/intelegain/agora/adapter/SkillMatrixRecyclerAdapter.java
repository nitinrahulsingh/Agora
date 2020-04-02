package com.intelegain.agora.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.intelegain.agora.R;
import com.intelegain.agora.interfeces.RecyclerItemClickListener;
import com.intelegain.agora.model.SkillMatrixData;
import com.intelegain.agora.utils.Contants2;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by maninder on 2/8/17.
 */

public class SkillMatrixRecyclerAdapter extends RecyclerView.Adapter<SkillMatrixRecyclerAdapter.MyViewHolder> implements View.OnClickListener/*, Filterable*/ {

    private List<SkillMatrixData.EmpSkill> skillList, filteredSkillList = new ArrayList<>();
    private Activity mActivity;
    private CommonMethods commonMethods;
    private Dialog dialog;
    private View dialogView;
    private LayoutInflater inflater;
    private RecyclerView mDialogRecyclerView;
    private String[] arrSkillLevel = {"Beginner", "Intermediate", "Expert"};
    private List<String> selectSkillLevelList;
    Contants2 contants2;


    public SkillMatrixRecyclerAdapter(Activity activity, ArrayList<SkillMatrixData.EmpSkill> employee_skillList) {
        this.mActivity = activity;
        this.skillList = employee_skillList;
        this.filteredSkillList.addAll(employee_skillList);
        selectSkillLevelList = Arrays.asList(arrSkillLevel);
        contants2 = new Contants2();
        // dialog
        commonMethods = new CommonMethods();
        inflater = mActivity.getLayoutInflater();
        dialog = new Dialog(mActivity);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_skill_matrix_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        holder.tvSkillTitle.setText(filteredSkillList.get(position).skillName);
        holder.tvSkillSubTitle.setText(filteredSkillList.get(position).category);


        holder.tvSkillYear.setText(filteredSkillList.get(position).years);
        holder.tvSkillMonth.setText(filteredSkillList.get(position).months);
        holder.tvSkillLevel.setText(filteredSkillList.get(position).level);

        holder.checkBoxSkill.setChecked(filteredSkillList.get(position).activeSkill);
        holder.checkBoxSkill.setTag(filteredSkillList.get(position).skillID);


    }

    @Override
    public int getItemCount() {
        return filteredSkillList.size();
    }

    @Override
    public void onClick(View v) {


    }



    /*@Override
    public Filter getFilter() {
        if (skillMatrixFilter == null)
            skillMatrixFilter = new SkillMatrixFilter(this, skillList);
        return skillMatrixFilter;
    }*/

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvSkillTitle, tvSkillSubTitle, tvSkillYear, tvSkillMonth, tvSkillLevel;
        Spinner spinnerSkillLevel;
        ImageView imageViewDropDownArrow, imgMinusYear, imgPlusYear, imgMinusMonth, imgPlusMonth;
        CheckBox checkBoxSkill;
        boolean isSkillChecked = false;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvSkillTitle = itemView.findViewById(R.id.tvSkillTitle);
            tvSkillSubTitle = itemView.findViewById(R.id.tvSkillSubTitle);
            checkBoxSkill = itemView.findViewById(R.id.checkboxSkillMatrix);
            tvSkillYear = itemView.findViewById(R.id.tvCountYear);
            tvSkillMonth = itemView.findViewById(R.id.tvCountMonth);
            tvSkillLevel = itemView.findViewById(R.id.tvSkillLevel);

            imgMinusYear = itemView.findViewById(R.id.tvMinusYear);
            imgPlusYear = itemView.findViewById(R.id.tvPlusYear);
            imgMinusMonth = itemView.findViewById(R.id.tvMinusMonth);
            imgPlusMonth = itemView.findViewById(R.id.tvPlusMonth);
            imageViewDropDownArrow = itemView.findViewById(R.id.imgDownArrow);


            imgMinusYear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tvSkillYear.getText().toString().equals("0")) {
                        return;
                    } else {
                        if (tvSkillMonth.getText().toString().equals("0"))
                            checkBoxSkill.setChecked(false);
                        tvSkillYear.setText(Integer.parseInt(tvSkillYear.getText().toString()) - 1 + "");
                        if (checkBoxSkill.isChecked()) {
                            //skillList.

                            skillList.get(getPositionByProperty(checkBoxSkill.getTag().toString())).valueHasChanged = true;
                            skillList.get(getPositionByProperty(checkBoxSkill.getTag().toString())).years = tvSkillYear.getText().toString();
                            calculateExperience(tvSkillYear.getText().toString(), tvSkillMonth.getText().toString(), getPositionByProperty(checkBoxSkill.getTag().toString()));
                        }

                    }
                }
            });

            imgPlusYear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tvSkillYear.getText().toString().equals("50")) {
                        return;
                    } else {
                        tvSkillYear.setText(Integer.parseInt(tvSkillYear.getText().toString()) + 1 + "");
                        if (checkBoxSkill.isChecked()) {
                            skillList.get(getPositionByProperty(checkBoxSkill.getTag().toString())).valueHasChanged = true;
                            skillList.get(getPositionByProperty(checkBoxSkill.getTag().toString())).years = tvSkillYear.getText().toString();
                            calculateExperience(tvSkillYear.getText().toString(), tvSkillMonth.getText().toString(), getPositionByProperty(checkBoxSkill.getTag().toString()));
                        }
                    }
                }
            });

            imgMinusMonth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tvSkillMonth.getText().toString().equals("0")) {
                        return;
                    } else {
                        if (tvSkillYear.getText().toString().equals("0"))
                            checkBoxSkill.setChecked(false);
                        tvSkillMonth.setText(Integer.parseInt(tvSkillMonth.getText().toString()) - 1 + "");
                        if (checkBoxSkill.isChecked()) {
                            skillList.get(getPositionByProperty(checkBoxSkill.getTag().toString())).valueHasChanged = true;
                            skillList.get(getPositionByProperty(checkBoxSkill.getTag().toString())).months = tvSkillMonth.getText().toString();
                            calculateExperience(tvSkillYear.getText().toString(), tvSkillMonth.getText().toString(), getPositionByProperty(checkBoxSkill.getTag().toString()));
                        }
                    }
                }
            });

            imgPlusMonth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tvSkillMonth.getText().toString().equals("11")) {
                        return;
                    } else {
                        tvSkillMonth.setText(Integer.parseInt(tvSkillMonth.getText().toString()) + 1 + "");
                        if (checkBoxSkill.isChecked()) {
                            skillList.get(getPositionByProperty(checkBoxSkill.getTag().toString())).valueHasChanged = true;
                            skillList.get(getPositionByProperty(checkBoxSkill.getTag().toString())).months = tvSkillMonth.getText().toString();
                            calculateExperience(tvSkillYear.getText().toString(), tvSkillMonth.getText().toString(), getPositionByProperty(checkBoxSkill.getTag().toString()));
                        }
                    }
                }
            });

            tvSkillLevel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    commonMethods.customSpinner(mActivity, "Select Level", inflater, mDialogRecyclerView, selectSkillLevelList, dialog,
                            dialogView,
                            new RecyclerItemClickListener() {
                                @Override
                                public void recyclerViewListClicked(int position, String itemClickText) {
                                    tvSkillLevel.setText(itemClickText);

                                    if (checkBoxSkill.isChecked()) {
                                        skillList.get(getPositionByProperty(checkBoxSkill.getTag().toString())).valueHasChanged = true;
                                        skillList.get(getPositionByProperty(checkBoxSkill.getTag().toString())).level = itemClickText;
                                    }
                                    dialog.hide();
                                }
                            });
                }
            });

            imageViewDropDownArrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    commonMethods.customSpinner(mActivity, "Select Level", inflater, mDialogRecyclerView, selectSkillLevelList, dialog,
                            dialogView,
                            new RecyclerItemClickListener() {
                                @Override
                                public void recyclerViewListClicked(int position, String itemClickText) {

                                    tvSkillLevel.setText(itemClickText);

                                    if (checkBoxSkill.isChecked()) {
                                        skillList.get(getPositionByProperty(checkBoxSkill.getTag().toString())).valueHasChanged = true;
                                        skillList.get(getPositionByProperty(checkBoxSkill.getTag().toString())).level = itemClickText;
                                    }
                                    dialog.hide();
                                }
                            });
                }
            });

            checkBoxSkill.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkBoxSkill.isChecked()) {
                        if ((tvSkillYear.getText().toString().equals("0") && tvSkillMonth.getText().toString().equals("0")) || tvSkillLevel.getText().toString().equals("")) {
                            Contants2.showToastMessage(mActivity, mActivity.getString(R.string.skill_validation_message), false);
                            checkBoxSkill.setChecked(false);
                        } else {
                            skillList.get(getPositionByProperty(checkBoxSkill.getTag().toString())).valueHasChanged = true;
                            skillList.get(getPositionByProperty(checkBoxSkill.getTag().toString())).activeSkill = true;
                            skillList.get(getPositionByProperty(checkBoxSkill.getTag().toString())).years = tvSkillYear.getText().toString();
                            skillList.get(getPositionByProperty(checkBoxSkill.getTag().toString())).months = tvSkillMonth.getText().toString();
                            skillList.get(getPositionByProperty(checkBoxSkill.getTag().toString())).level = tvSkillLevel.getText().toString();
                            calculateExperience(tvSkillYear.getText().toString(), tvSkillMonth.getText().toString(), getPositionByProperty(checkBoxSkill.getTag().toString()));
                        }
                    } else {
                        skillList.get(getPositionByProperty(checkBoxSkill.getTag().toString())).valueHasChanged = true;
                        skillList.get(getPositionByProperty(checkBoxSkill.getTag().toString())).activeSkill = false;
                    }
                }
            });

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
    private void calculateExperience(String tvYearValue, String tvMonthValue, int position) {
        skillList.get(position).experience = (Integer.parseInt(tvYearValue) * 12) + Integer.parseInt(tvMonthValue) + "";
    }

    // Do Search...
    public void filter(final String text/*, final int filterType*/) {

        // Searching could be complex..so we will dispatch it to a different thread...
        new Thread(new Runnable() {
            @Override
            public void run() {

                // Clear the filter list
                filteredSkillList.clear();

                // If there is no search value, then add all original list items to filter list
                if (TextUtils.isEmpty(text)) {
                    if (skillList != null)
                        filteredSkillList.addAll(skillList);
                } else {
                    if (skillList != null)
                        // Iterate in the original List and add it to filter list...
                        for (SkillMatrixData.EmpSkill emp_skill : skillList) {
                            if (emp_skill.skillName.toLowerCase().contains(text.toLowerCase()))
                                filteredSkillList.add(emp_skill);
                        }
                }


                // Set on UI Thread
                (mActivity).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (filteredSkillList.size() == 0)
                            Contants2.showToastMessageAtCenter(mActivity, mActivity.getString(R.string.no_data_found), false);
                        // Notify the List that the DataSet has changed...
                        notifyDataSetChanged();
                    }
                });

            }
        }).start();

    }

    public int getPositionByProperty(String skillId) {
        int value = -1;
        for (int i = 0; i < skillList.size(); i++) {
            if (skillList.get(i).skillID.contains(skillId)) {

                value = i;
                break;
            }
        }
        return value;
    }
}

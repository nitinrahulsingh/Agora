package com.intelegain.agora.fragmments;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.intelegain.agora.R;
import com.intelegain.agora.adapter.SkillMatrixRecyclerAdapter;
import com.intelegain.agora.constants.Constants;
import com.intelegain.agora.dataFetch.RetrofitClient;
import com.intelegain.agora.interfeces.WebApiInterface;
import com.intelegain.agora.model.CommonStatusMessage;
import com.intelegain.agora.model.SaveSkillMatrixEntity;
import com.intelegain.agora.model.SkillMatrixData;
import com.intelegain.agora.utils.Contants2;
import com.intelegain.agora.utils.Sharedprefrences;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class SkillMatrix extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    View view;

    EditText etSearchBySkill;
    TextView tv_try_again;
    ImageView imageViewAddSkill, img_filter;
    public RecyclerView recyclerView_skill_matrix;
    Button btnSaveSkill;


    Contants2 contants2;
    public SkillMatrixRecyclerAdapter skillMatrixRecyclerAdapter;
    ArrayList<SkillMatrixData.EmpSkill> empSkillArrayList, saveSkillArrayList = new ArrayList<>();
    SkillMatrixData skillMatrixData;
    String currentScreen = "";

    CommonStatusMessage commonStatusMessage;
    private SwipeRefreshLayout swipeRefreshLayout;

    Retrofit retrofit;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_skill_matrix, container, false);
        retrofit = RetrofitClient.getInstance(Constants.BASE_URL);
        initialize();
        findViews();
        getAllSkillMatrix("-1");

        setListenersForViews();
        return view;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, v.getId(), 0, getResources().getString(R.string.get_All_Skill));
        menu.add(0, v.getId(), 0, getResources().getString(R.string.My_Skill));
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle().equals(getString(R.string.get_All_Skill))) {
            if (currentScreen.equals("-1"))
                return false;
            getAllSkillMatrix("-1");
        } else if (item.getTitle().equals(getString(R.string.My_Skill))) {
            if (currentScreen.equals("0"))
                return false;
            getAllSkillMatrix("0");
        } else {
            return false;
        }
        return true;
    }

    private void initialize() {
        empSkillArrayList = new ArrayList<>();
        contants2 = new Contants2();
    }


    private void findViews() {
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        img_filter = view.findViewById(R.id.img_filter);
        etSearchBySkill = view.findViewById(R.id.etSearchBySkill);
        imageViewAddSkill = view.findViewById(R.id.imgAddSkill);
        recyclerView_skill_matrix = view.findViewById(R.id.recycler_skill_matrix);
        tv_try_again = view.findViewById(R.id.text_view_try_again);
        btnSaveSkill = view.findViewById(R.id.btnSaveSkill);

        registerForContextMenu(img_filter);
    }

    private void setUpRecyclerView() {

        // recycler
        recyclerView_skill_matrix.setHasFixedSize(true);
        recyclerView_skill_matrix.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,
                false));

        recyclerView_skill_matrix.setLayoutManager(new LinearLayoutManager(getActivity()));

        skillMatrixRecyclerAdapter = new SkillMatrixRecyclerAdapter(getActivity(), empSkillArrayList);
        recyclerView_skill_matrix.setAdapter(skillMatrixRecyclerAdapter);

        /*recyclerView_contacts.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // Log.e("RecyclerView", "onScrollStateChanged");
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                // Log.e("RecyclerView", "onScrolled");
            }
        });*/


    }

    private void setListenersForViews() {
        swipeRefreshLayout.setOnRefreshListener(this);
        img_filter.setOnClickListener(this);
        imageViewAddSkill.setOnClickListener(this);
        tv_try_again.setOnClickListener(this);
        btnSaveSkill.setOnClickListener(this);

        etSearchBySkill.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                skillMatrixRecyclerAdapter.filter(etSearchBySkill.getText().toString());
            }
        });
    }

//    /**
//
//     */

    /**
     * @param viewAll --- "-1" value if landing screen or view all selected from search filter and "0"
     *                Retrofit Web Call to get employee profile details. Calls the interface through fragment to activity to update the
     *                image, name, emp id, designation received here.
     */
    private void getAllSkillMatrix(final String viewAll) {
        if (Contants2.checkInternetConnection(getActivity())) {
            contants2.showProgressDialog(getActivity());
            swipeRefreshLayout.setRefreshing(false);

            WebApiInterface apiInterface = retrofit.create(WebApiInterface.class);
            Sharedprefrences mSharedPrefs = Sharedprefrences.getInstance(getActivity());
            String strToken = mSharedPrefs.getString("Token", "");
            String strEmpId = mSharedPrefs.getString("emp_Id", "");

            Map<String, String> params = new HashMap<>();
            params.put("EmpId", strEmpId);
            params.put("ToggleSkill", viewAll);
            params.put("SkillName", "");

            Call<SkillMatrixData> call = apiInterface.getSkillMatrix(strEmpId, strToken, params);
            call.enqueue(new Callback<SkillMatrixData>() {
                @Override
                public void onResponse(Call<SkillMatrixData> call, Response<SkillMatrixData> response) {
                    recyclerView_skill_matrix.setVisibility(View.VISIBLE);
                    swipeRefreshLayout.setRefreshing(false);
                    switch (response.code()) {
                        case 200:
                            currentScreen = viewAll;
                            hideshowRecyclerSkillMatrix(false);

                            skillMatrixData = response.body();
                            if (empSkillArrayList.size() > 0) {
                                empSkillArrayList.clear();
                                skillMatrixRecyclerAdapter.notifyDataSetChanged();
                            }
                            empSkillArrayList.addAll(skillMatrixData.lstEmpSkill);

                            if (empSkillArrayList.size() > 0) {
                                setUpRecyclerView();
                            } else {
                                hideshowRecyclerSkillMatrix(true);
                                Contants2.showToastMessageAtCenter(getActivity(), getString(R.string.no_data_found), true);
                                // tv_try_again.setText(getString(R.string.no_data_found));

                            }
                            contants2.dismissProgressDialog();
                            break;
                        case 403:
                            contants2.dismissProgressDialog();
                            hideshowRecyclerSkillMatrix(false);
                            Contants2.showOkAlertDialog(getActivity(), response.message(), "", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Contants2.forceLogout(getActivity());
                                }
                            });
                            break;
                        case 500:
                            contants2.dismissProgressDialog();
                            Contants2.showToastMessage(getActivity(), getString(R.string.some_error_occurred), true);

                            hideshowRecyclerSkillMatrix(true);
                            break;
                        default:
                            contants2.dismissProgressDialog();
                            Contants2.showToastMessage(getActivity(), getString(R.string.some_error_occurred), true);
                            hideshowRecyclerSkillMatrix(true);
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    call.cancel();
                    contants2.dismissProgressDialog();
                    swipeRefreshLayout.setRefreshing(false);
                    Contants2.showToastMessage(getActivity(), getString(R.string.some_error_occurred), true);
                }
            });
        } else {
            Contants2.showToastMessage(getActivity(), getString(R.string.no_internet), true);
            hideshowRecyclerSkillMatrix(true);
        }
    }

    /**
     * @param hideRecyclerSkillMatrix boolean value if true then need to hide constraint layout and false will make it visible
     */
    public void hideshowRecyclerSkillMatrix(boolean hideRecyclerSkillMatrix) {
        if (hideRecyclerSkillMatrix) {
            recyclerView_skill_matrix.setVisibility(View.GONE);
            tv_try_again.setVisibility(View.VISIBLE);
        } else {
            tv_try_again.setVisibility(View.GONE);
            recyclerView_skill_matrix.setVisibility(View.VISIBLE);
        }

    }

    private void saveSkillMatrix() {
        if (Contants2.checkInternetConnection(getActivity())) {
            contants2.showProgressDialog(getActivity());
            WebApiInterface apiInterface = retrofit.create(WebApiInterface.class);
            Sharedprefrences mSharedPrefs = Sharedprefrences.getInstance(getActivity());

            commonStatusMessage = new CommonStatusMessage();

            String strToken = mSharedPrefs.getString("Token", "");
            String strEmpId = mSharedPrefs.getString("emp_Id", "");

            SaveSkillMatrixEntity saveSkillMatrixEntity = new SaveSkillMatrixEntity();
            saveSkillMatrixEntity.empId = strEmpId;
            saveSkillMatrixEntity.userID = strEmpId;
            saveSkillMatrixEntity.lstEmpSkill = saveSkillArrayList;

            Call<CommonStatusMessage> call = apiInterface.saveSkillMatrix(strEmpId, strToken, saveSkillMatrixEntity);
            call.enqueue(new Callback<CommonStatusMessage>() {
                @Override
                public void onResponse(Call<CommonStatusMessage> call, Response<CommonStatusMessage> response) {

                    switch (response.code()) {
                        case 200:
                            commonStatusMessage = response.body();
                            contants2.dismissProgressDialog();
                            if (commonStatusMessage.status.equals("1")) {
                                Contants2.showToastMessage(getActivity(), commonStatusMessage.message, true);
                                getAllSkillMatrix(currentScreen);
                            } else
                                Contants2.showToastMessage(getActivity(), commonStatusMessage.message, true);

                            break;
                        case 403:
                            contants2.dismissProgressDialog();

                            Contants2.showOkAlertDialog(getActivity(), response.message(), "", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Contants2.forceLogout(getActivity());
                                }
                            });
                            break;
                        case 500:
                            contants2.dismissProgressDialog();
                            Contants2.showToastMessage(getActivity(), getString(R.string.some_error_occurred), true);

                            break;
                        default:
                            contants2.dismissProgressDialog();
                            Contants2.showToastMessage(getActivity(), getString(R.string.some_error_occurred), true);

                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    call.cancel();
                    contants2.dismissProgressDialog();
                    Contants2.showToastMessage(getActivity(), getString(R.string.some_error_occurred), true);
                }
            });
        } else {
            Contants2.showToastMessage(getActivity(), getString(R.string.no_internet), true);

        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgAddSkill:
                break;
            case 2:
                break;
            case R.id.text_view_try_again:
                getAllSkillMatrix(currentScreen);
                break;
            case R.id.img_filter:
                getActivity().openContextMenu(img_filter);
                break;
            case R.id.btnSaveSkill:
                //for (SkillMatrixData.EmpSkill empSkill : empSkillArrayList) {
                if (saveSkillArrayList.size() > 0)
                    saveSkillArrayList.clear();
                int size = empSkillArrayList.size();
                for (int i = 0; i < size; i++)
                    if (empSkillArrayList.get(i).valueHasChanged) {
                        saveSkillArrayList.add(empSkillArrayList.get(i));
                    }
                if (saveSkillArrayList.size() > 0)
                    saveSkillMatrix();
                else
                    Contants2.showToastMessage(getActivity(), getString(R.string.skill_select_check), true);
                break;
            default:
                break;
        }
    }

    @Override
    public void onRefresh() {
        getAllSkillMatrix(currentScreen);
        if (etSearchBySkill != null)
            etSearchBySkill.setText("");

    }
}
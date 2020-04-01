package com.intelegain.agora.fragmments;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.github.angads25.filepicker.view.FilePickerDialog;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.intelegain.agora.R;
import com.intelegain.agora.activity.AddKnowledgebase;
import com.intelegain.agora.activity.BasicCropActivity;
import com.intelegain.agora.adapter.KnowledgeBaseAdapter;
import com.intelegain.agora.api.urls.CommonMethods;
import com.intelegain.agora.constants.Constants;
import com.intelegain.agora.dataFetch.RetrofitClient;
import com.intelegain.agora.interfeces.IKnlodedgeBaseAttachment;
import com.intelegain.agora.interfeces.RecyclerItemClickListener;
import com.intelegain.agora.interfeces.WebApiInterface;
import com.intelegain.agora.interfeces.updateDrawerImage;
import com.intelegain.agora.model.AddKnowledge;
import com.intelegain.agora.model.KnowdlegeAttachmentResponce;
import com.intelegain.agora.model.KnowledgeBaseData;
import com.intelegain.agora.model.KnowledgeMaster;
import com.intelegain.agora.model.KnowledgebaseProjectName;
import com.intelegain.agora.model.TechnologyData;
import com.intelegain.agora.utils.Contants2;
import com.intelegain.agora.utils.FileDownloader;
import com.intelegain.agora.utils.ImageSaver;
import com.intelegain.agora.utils.Sharedprefrences;
import com.intelegain.agora.utils.VolleyMultipartRequest;
import com.intelegain.agora.utils.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


public class KnowledgeBaseFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, IKnlodedgeBaseAttachment {
    private String TAG = getClass().getSimpleName();
    private Activity activity;
    private ImageView ivknowledge_filter;
    private EditText edKnowledgeSkill;
    private FloatingActionButton fabAddKnowledge;
    private RecyclerView recyler_view_for_knowledge;
    private ArrayList<KnowledgeBaseData> mlstKnowledgeBaseDataList = new ArrayList<>();
    private KnowledgeBaseAdapter knowledgeBaseAdapter;
    private BottomSheetDialog mBottomSheetDialog;
    private Retrofit retrofit = RetrofitClient.getInstance(Constants.BASE_URL);
    Contants2 contants2;


    /**
     * Add New Knowledge widget
     */
    private Button btnAddKnowledge;
    private EditText edEmployeeName, edTitle, ed_description, ed_tag;
    private TextView tvProjectName, tvTechnology, tvTag, tv_try_again;
    private ArrayList<String> mlstProjectNameList = new ArrayList<String>();
    private ArrayList<String> mlstTechnologyList = new ArrayList<String>();
    private ArrayList<String> mlstTechnologyFilterList = new ArrayList<String>();

    private ArrayList<KnowledgebaseProjectName.ProjectDatum> projectDatumArrayList = new ArrayList<>();
    private ArrayList<TechnologyData.TechnologyDatum> technologyDatumArrayList = new ArrayList<>();

    private RecyclerView dialogRecyclerView;
    private LayoutInflater inflater;
    private View dialog_view;
    private Dialog dialog;
    private ProgressDialog progressDialog;

    private SwipeRefreshLayout swipeRefreshLayout;

    WebApiInterface apiInterface = retrofit.create(WebApiInterface.class);
    Sharedprefrences mSharedPrefs = Sharedprefrences.getInstance(activity);
    String strToken = mSharedPrefs.getString("Token", "");
    String strEmpId = mSharedPrefs.getString("emp_Id", "");

    private String kbId = "", fileName = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setRetainInstance(true);    // to retain the state of fragment when activity recreates
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_knowledge_base, container, false);
        activity = getActivity();
        InitializeWidget(view);
        setEventClickListener();
//        PrepareKnowledgeBaseDataList();
//        setKnowledgeAdapterData();

        edKnowledgeSkill.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //mTaskManagerAdapter.getFilter().filter(s);
                knowledgeBaseAdapter.filter(s.toString(), KnowledgeBaseAdapter.FILTER_WITH_TAG_LIST);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Initialize();

        // Get knowledgeBase data form server
        getKnowledgeBaseData();
        getProjectNameList();
        getTechnologyNameList();
    }

    @Override
    public void onRefresh() {
        // Get knowledgeBase data form server
        if (CommonMethods.checkInternetConnection(activity)) {
            getKnowledgeBaseData();
            if (edKnowledgeSkill != null)
                edKnowledgeSkill.setText("");
        } else {
            swipeRefreshLayout.setRefreshing(false);
            Contants2.showToastMessage(activity, getString(R.string.no_internet), true);
        }
    }


    private void Initialize() {
        contants2 = new Contants2();
    }


    /* Set the event click listener for views */
    private void setEventClickListener() {
        ivknowledge_filter.setOnClickListener(this);
        edKnowledgeSkill.setOnClickListener(this);
        fabAddKnowledge.setOnClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(this);
        tv_try_again.setOnClickListener(this);
    }

    /**
     * Initialize the widget
     *
     * @param view
     */
    private void InitializeWidget(View view) {
        recyler_view_for_knowledge = view.findViewById(R.id.recyler_view_for_knowledge);
        ivknowledge_filter = view.findViewById(R.id.ivknowledge_filter);
        edKnowledgeSkill = view.findViewById(R.id.edKnowledgeSkill);
        fabAddKnowledge = view.findViewById(R.id.fabAddKnowledge);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);


        tv_try_again = view.findViewById(R.id.text_view_try_again);

        inflater = getActivity().getLayoutInflater();
        dialog = new Dialog(getActivity());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivknowledge_filter:
                showTechnologyListFilterDialog();
                break;
            case R.id.edKnowledgeSkill:
                break;
            case R.id.fabAddKnowledge:
                startActivity(new Intent(getActivity(), AddKnowledgebase.class));
                //getActivity().overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                //OpenAddNewKnowledgeSheet();
                break;
            case R.id.tvProjectName:
                showProjectListDialog();
                break;
            case R.id.tvTechnology:
                showTechnologyListDialog();
                break;
            case R.id.btnAddKnowledge:
                SaveKnowledgeData();
                break;
            case R.id.text_view_try_again:
                getKnowledgeBaseData();
                getProjectNameList();
                getTechnologyNameList();
                break;
            default:
                break;
        }
    }

    /**
     * Set knowledge adapter data
     */
    private void setKnowledgeAdapterData() {
        // Set adapter data here
        knowledgeBaseAdapter = new KnowledgeBaseAdapter(activity, mlstKnowledgeBaseDataList, strToken, strEmpId, this);
        recyler_view_for_knowledge.setAdapter(knowledgeBaseAdapter);
        recyler_view_for_knowledge.setLayoutManager(new LinearLayoutManager(activity));
    }

    /**
     * Open the Add knowledge screen for adding the new knowledge
     */
    private void OpenAddNewKnowledgeSheet() {
        mBottomSheetDialog = new BottomSheetDialog(activity);
        View sheetView = activity.getLayoutInflater().inflate(R.layout.add_knowledge, null);
        mBottomSheetDialog.setContentView(sheetView);
        InitializeAddNewViews(sheetView);

        mBottomSheetDialog.show();
    }

    /**
     * Initialize add new views
     */
    private void InitializeAddNewViews(View view) {
        btnAddKnowledge = view.findViewById(R.id.btnAddKnowledge);
        edEmployeeName = view.findViewById(R.id.edEmployeeName);
        edTitle = view.findViewById(R.id.edTitle);
        ed_description = view.findViewById(R.id.ed_description);
        ed_tag = view.findViewById(R.id.ed_tag);
        tvProjectName = view.findViewById(R.id.tvProjectName);
        tvTechnology = view.findViewById(R.id.tvTechnology);
        tvTag = view.findViewById(R.id.tvTagTitle);
        // Add click listener for addKnowledge button
        btnAddKnowledge.setOnClickListener(this);
        tvProjectName.setOnClickListener(this);
        tvTechnology.setOnClickListener(this);
    }

    /**
     * Is valid input in EditText
     */
    private boolean IsValidInputInEditText(EditText edToValid) {
        if (edToValid == null)
            return false;
        String strInput = edToValid.getText().toString();
        return !TextUtils.isEmpty(strInput);
    }

    /**
     * Save Knowledge data to server
     */
    private void SaveKnowledgeData() {
        if (IsValidInputInEditText(edEmployeeName)
                && IsValidInputInEditText(edTitle)
                && IsValidInputInEditText(ed_description)
                && IsValidInputInEditText(ed_tag)
                && (!TextUtils.isEmpty(tvProjectName.getText().toString()))
                && (!TextUtils.isEmpty(tvTechnology.getText().toString()))) {
            addKnowledgeBaseData();
            //Toast.makeText(activity, "Knowledge Added!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(activity, "Please fill all the data first!", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Prepare technology filter name list spinner
     */
   /* private void PrepareTechnologyFilterNameList() {


        if (mlstTechnologyFilterList.size() == 0) {
            mlstTechnologyFilterList.add("See All");
            mlstTechnologyFilterList.add(".Net");
            mlstTechnologyFilterList.add("Java");
            mlstTechnologyFilterList.add("PHP");
            mlstTechnologyFilterList.add("System Analysis");
            mlstTechnologyFilterList.add("Miscellaneous");
            mlstTechnologyFilterList.add("SQL");
            mlstTechnologyFilterList.add("Javascript");
        }
    }*/

    /**
     * Show Project name list dialog that to pick project name
     */
    private void showProjectListDialog() {
        new CommonMethods().customSpinner(activity, "Select Project", inflater, dialogRecyclerView,
                mlstProjectNameList, dialog, dialog_view,
                new RecyclerItemClickListener() {
                    @Override
                    public void recyclerViewListClicked(int position, String itemClickText) {
                        tvProjectName.setText(itemClickText);
                        tvProjectName.setTag(mlstProjectNameList.indexOf(itemClickText));
                        dialog.hide();
                    }
                });
    }

    /**
     * Show Project name list dialog that to pick technology name
     */
    private void showTechnologyListDialog() {
        dialogRecyclerView = null;
        new CommonMethods().customSpinner(activity, "Select Technology", inflater, dialogRecyclerView,
                mlstTechnologyList, dialog, dialog_view,
                new RecyclerItemClickListener() {
                    @Override
                    public void recyclerViewListClicked(int position, String itemClickText) {
                        tvTechnology.setText(itemClickText);
                        tvTechnology.setTag(mlstTechnologyList.indexOf(itemClickText));
                        dialog.hide();
                    }
                });
    }

    /**
     * Show Project name list filter dialog that to pick technology name
     */
    private void showTechnologyListFilterDialog() {
        dialogRecyclerView = null;
        new CommonMethods().customSpinner(activity, "Select Technology", inflater, dialogRecyclerView,
                mlstTechnologyFilterList, dialog, dialog_view,
                new RecyclerItemClickListener() {
                    @Override
                    public void recyclerViewListClicked(int position, String itemClickText) {
                        if (itemClickText.equalsIgnoreCase("See All"))
                            knowledgeBaseAdapter.filter("", KnowledgeBaseAdapter.FILTER_WITH_TECHNOLOGY);
                        else
                            knowledgeBaseAdapter.filter(itemClickText, KnowledgeBaseAdapter.FILTER_WITH_TECHNOLOGY);
                        dialog.hide();
                    }
                });
    }

    /******************************************** WEB API CALLS ***********************************/
    private void getKnowledgeBaseData() {
        if (Contants2.checkInternetConnection(getActivity())) {
            swipeRefreshLayout.setRefreshing(false);
            contants2.showProgressDialog(getActivity());
            // make list of parameter for sending the http request
            Map<String, String> params = new HashMap<String, String>();
            params.put("EmpId", strEmpId);


            Call<KnowledgeMaster> call = apiInterface.getKnowledgeBaseData(strEmpId, strToken);
            call.enqueue(new Callback<KnowledgeMaster>() {
                @Override
                public void onResponse(Call<KnowledgeMaster> call, retrofit2.Response<KnowledgeMaster> response) {
                    contants2.dismissProgressDialog();
                    switch (response.code()) {
                        case 200:


                            KnowledgeMaster objKnowledgeMaster = response.body();
                            ArrayList<KnowledgeBaseData> objKnowledgeBaseDataList = objKnowledgeMaster.getKnowledgeData();

                            if (objKnowledgeBaseDataList.size() > 0) {
                                mlstKnowledgeBaseDataList = objKnowledgeBaseDataList;
                                setKnowledgeAdapterData();
                                hideshowRecyclerView(false);
                            } else {
                                hideshowRecyclerView(true);
                                Contants2.showToastMessageAtCenter(activity, getString(R.string.no_data_found), true);
                                //tv_try_again.setText(getString(R.string.no_data_found));
                            }

                            contants2.dismissProgressDialog();
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
                            hideshowRecyclerView(true);
                            contants2.dismissProgressDialog();
                            Contants2.showToastMessage(getActivity(), getString(R.string.some_error_occurred), true);
                            break;
                        default:
                            hideshowRecyclerView(true);
                            contants2.dismissProgressDialog();
                            Contants2.showToastMessage(getActivity(), getString(R.string.some_error_occurred), true);

                    }

                    //dismissProgressDialog();
                    swipeRefreshLayout.setRefreshing(false);
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    call.cancel();

                    contants2.dismissProgressDialog();
                    Contants2.showToastMessage(getActivity(), getString(R.string.some_error_occurred), true);
                    swipeRefreshLayout.setRefreshing(false);
                    //Toast.makeText(activity, "Some error occurred! please try again later..", Toast.LENGTH_SHORT).show();
                }
            });
        } else {

            hideshowRecyclerView(true);
            Contants2.showToastMessage(getActivity(), getString(R.string.no_internet), true);
        }
    }

    private void addKnowledgeBaseData(/*final File KnowledgebaseFile*/) {
        if (Contants2.checkInternetConnection(getActivity())) {
            contants2.showProgressDialog(getActivity());
            Map headers = new HashMap<>();

            headers.put("empid", strEmpId);
            headers.put("token", strToken);

            VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Constants.BASE_URL + "addKnowledge", headers, new com.android.volley.Response.Listener<NetworkResponse>() {
                @Override
                public void onResponse(NetworkResponse networkResponse) {
                    contants2.dismissProgressDialog();
                    try {
                        String resultResponse = new String(networkResponse.data);
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(resultResponse);
                            if (jsonObject.getString("Status").equals("1")) {
                                Contants2.showToastMessage(getActivity(), jsonObject.getString("Message"), true);
                                mBottomSheetDialog.cancel();
                            } else {
                                Contants2.showToastMessage(getActivity(), jsonObject.getString("Message"), true);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    contants2.dismissProgressDialog();
                }
            }) {
                @Override
                protected Map getParams() {
                    Map params = new HashMap<>();
                    params.put("EmpId", strEmpId);
                    params.put("KbDescrptn", ed_description.getText().toString().trim());
                    params.put("KbTitle", edTitle.getText().toString().trim());
                    params.put("ProjId", projectDatumArrayList.get(Integer.parseInt(tvProjectName.getTag().toString())).projId.toString());
                    params.put("TechId", technologyDatumArrayList.get(Integer.parseInt(tvTechnology.getTag().toString())).techId.toString());
                    params.put("Url", "");
                    params.put("SubTechName", ed_tag.getText().toString());

                    return params;
                }

                @Override
                protected Map getByteData() {
                    Map params = new HashMap<>();

                    params.put("UploadedImage", null);
                /*if (BitmapDrawable.createFromPath(photoFile.getPath()) != null)
                    params.put("UploadedImage", new DataPart(photoFile.getName().toString(), getFileDataFromImagePath(photoFile.getPath()), "image"));*/
                    return params;
                }
            };
            multipartRequest.setRetryPolicy(new DefaultRetryPolicy(300000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance(getActivity()).addToRequestQueue(multipartRequest);
        } else {
            Contants2.showToastMessage(getActivity(), getString(R.string.no_internet), true);
        }
    }


    //showProgressDialog();
        /*contants2.showProgressDialog(getActivity());

        Map<String, String> params = new HashMap<String, String>();
        params.put("EmpId", strEmpId);
        params.put("KbDescrptn", ed_description.getText().toString().trim());
        params.put("KbTitle", edTitle.getText().toString().trim());
        params.put("ProjId", projectDatumArrayList.get(Integer.parseInt(tvProjectName.getTag().toString())).projId.toString());
        params.put("TechId", technologyDatumArrayList.get(Integer.parseInt(tvTechnology.getTag().toString())).techId.toString());

        Call<KnowledgeMaster> call = apiInterface.getKnowledgeBaseData(strEmpId, strToken);
        call.enqueue(new Callback<KnowledgeMaster>() {
            @Override
            public void onResponse(Call<KnowledgeMaster> call, Response<KnowledgeMaster> response) {

                switch (response.code()) {
                    case 200:
                        contants2.dismissProgressDialog();

                        //hideshowConstraintLayout(false);
                        //employeeProfile = response.body();
                        *//*new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                setValues(employeeProfile);
                                contants2.dismissProgressDialog();
                            }
                        }, 100);*//*
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
                        contants2.showToastMessage(getActivity(), getString(R.string.some_error_occurred), true);
                        break;
                    default:
                        contants2.dismissProgressDialog();
                        contants2.showToastMessage(getActivity(), getString(R.string.some_error_occurred), true);

                }

            }

            @Override
            public void onFailure(Call call, Throwable t) {
                call.cancel();
                //dismissProgressDialog();//  dismiss progress dialog
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(activity, "Some error occurred! please try again later..", Toast.LENGTH_SHORT).show();
            }
        });*/


    /**
     * web api call for getting project name list
     */
    private void getProjectNameList() {
        // make list of parameter for sending the http request
        Map<String, String> params = new HashMap<String, String>();
        params.put("EmpId", strEmpId);

        Call<KnowledgebaseProjectName> call = apiInterface.getProjectNameList(strEmpId, strToken, params);
        call.enqueue(new Callback<KnowledgebaseProjectName>() {
            @Override
            public void onResponse(Call<KnowledgebaseProjectName> call, retrofit2.Response<KnowledgebaseProjectName> response) {
                Log.d("TAG", "Status Code: " + response.code());
                int iStatusCode = response.code();

                switch (response.code()) {
                    case 200:
                        KnowledgebaseProjectName knowledgebaseProjectName = response.body();

                        projectDatumArrayList.addAll(knowledgebaseProjectName.projectData);
                        if (mlstProjectNameList.size() > 0)
                            mlstProjectNameList.clear();
                        int projectNameListSize = knowledgebaseProjectName.projectData.size();
                        for (int i = 0; i < projectNameListSize; i++) {
                            mlstProjectNameList.add(knowledgebaseProjectName.projectData.get(i).projName);
                        }
                        break;
                    case 403:
                        break;
                    case 500:
                        getProjectNameList();
                        break;

                }


            }

            @Override
            public void onFailure(Call call, Throwable t) {
                call.cancel();

            }
        });

        /*if (mlstProjectNameList.size() == 0) {
            mlstProjectNameList.add("Knowledge Base");
            mlstProjectNameList.add("Procharge Android");
            mlstProjectNameList.add("Corvi LED");
            mlstProjectNameList.add("MUrgency");
            mlstProjectNameList.add("TATA World");
            mlstProjectNameList.add("Delhop");
        }*/
    }

    /**
     * web api call for getting technology list
     */
    private void getTechnologyNameList() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("EmpId", strEmpId);

        Call<TechnologyData> call = apiInterface.getTechnologyNameList(strEmpId, strToken, params);
        call.enqueue(new Callback<TechnologyData>() {
            @Override
            public void onResponse(Call<TechnologyData> call, retrofit2.Response<TechnologyData> response) {
                Log.d("TAG", "Status Code: " + response.code());

                switch (response.code()) {
                    case 200:
                        TechnologyData technologyData = response.body();
                        if (mlstTechnologyList.size() > 0)
                            mlstTechnologyList.clear();

                        technologyDatumArrayList.addAll(technologyData.technologyData);
                        int projectNameListSize = technologyData.technologyData.size();
                        for (int i = 0; i < projectNameListSize; i++) {
                            mlstTechnologyList.add(technologyData.technologyData.get(i).techName);
                            mlstTechnologyFilterList.add(technologyData.technologyData.get(i).techName);
                        }

                        break;
                    case 403:
                        break;
                    case 500:
                        getTechnologyNameList();
                        break;

                }


            }

            @Override
            public void onFailure(Call call, Throwable t) {
                call.cancel();

            }
        });

        /*if (mlstTechnologyList.size() == 0) {
            mlstTechnologyList.add(".Net");
            mlstTechnologyList.add("Java");
            mlstTechnologyList.add("PHP");
            mlstTechnologyList.add("System Analysis");
            mlstTechnologyList.add("Miscellaneous");
            mlstTechnologyList.add("SQL");
            mlstTechnologyList.add("Javascript");
        }*/
    }


    /**
     * Apply leave to server
     */
    private void applyLeave(String strLeaveType, String strLeaveFrom, String strLeaveTo, String strReason) {
        showProgressDialog();
        WebApiInterface apiInterface = retrofit.create(WebApiInterface.class);
        Sharedprefrences mSharedPrefs = Sharedprefrences.getInstance(activity);
        String strToken = mSharedPrefs.getString("Token", "");
        String strEmpId = mSharedPrefs.getString("emp_Id", "1000");
        // make list of parameter for sending the http request
        Map<String, String> params = new HashMap<String, String>();
        params.put("EmpId", strEmpId);
        params.put("LeaveType", strLeaveType);
        params.put("LeaveFrom", strLeaveFrom);
        params.put("LeaveTo", strLeaveTo);
        params.put("Reason", strReason);

        Call<AddKnowledge> call = apiInterface.addKnowledge(strEmpId, strToken, params);
        call.enqueue(new Callback<AddKnowledge>() {
            @Override
            public void onResponse(Call<AddKnowledge> call, retrofit2.Response<AddKnowledge> response) {
                Log.d("TAG", "Status Code: " + response.code());
                int iStatusCode = response.code();
                if (iStatusCode == 200) {
                    AddKnowledge objApplyLeave = response.body();
                    String strStatus = objApplyLeave.getStatus();
                    String strMessage = objApplyLeave.getMessage();
                    Toast.makeText(activity, "Status = " + strStatus + "\nMessage = " + strMessage, Toast.LENGTH_SHORT).show();
                    mBottomSheetDialog.cancel();    // Close bottom sheet dialog
                } else {
                    Toast.makeText(activity, "Some error occurred! please try again later..", Toast.LENGTH_SHORT).show();
                }
                dismissProgressDialog();

//                {
//                    "status": "1",
//                        "message": "Knowledge added successfully"
//                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                call.cancel();
                dismissProgressDialog();
                Toast.makeText(activity, "Some error occurred! please try again later..", Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
     * @param hideshowRecyclerView boolean value if true then need to hide RecyclerView and false will make it visible
     */
    public void hideshowRecyclerView(boolean hideshowRecyclerView) {
        if (hideshowRecyclerView) {
            recyler_view_for_knowledge.setVisibility(View.GONE);
            tv_try_again.setVisibility(View.VISIBLE);
        } else {
            tv_try_again.setVisibility(View.GONE);
            recyler_view_for_knowledge.setVisibility(View.VISIBLE);
        }

    }

    /**************************************** WEB API CALLS ENDS **********************************/

    /**
     * Show progresss dialog
     */
    private void showProgressDialog() {
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Please wait ...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    /**
     * Dismiss the progress dialog
     */
    private void dismissProgressDialog() {
        if (progressDialog != null)
            if (progressDialog.isShowing())
                progressDialog.dismiss();
    }


    /**
     * Create dummy data for display
     */
    private void PrepareKnowledgeBaseDataList() {
//        KnowledgeBaseData knowledgeBaseData;
//        ArrayList<String> lstKnowledgeTagList = new ArrayList<>();
//        for(int iCnt=0; iCnt < 4; iCnt++) {
//            if(iCnt == 0)
//                lstKnowledgeTagList.add("Android");
//            else if(iCnt == 1)
//                lstKnowledgeTagList.add("Sqlite");
//            else if(iCnt == 2)
//                lstKnowledgeTagList.add("Data");
//            else if(iCnt == 3)
//                lstKnowledgeTagList.add("Compare");
//            else
//                lstKnowledgeTagList.add("Query");
//
//        }
//        for(int iCnt=0; iCnt < 10; iCnt++) {
//            knowledgeBaseData = new KnowledgeBaseData("Enable/ON device location service directly from Android App",
//                    "Knowledge Base","Miscellaneous","Suraj Magre","07 Jul 2017",lstKnowledgeTagList.);
//            mlstKnowledgeBaseDataList.add(knowledgeBaseData);
//        }
    }

    @Override
    public void getAttachment(String kbId, String fileName) {

        downloadAttachment(kbId, fileName);
    }

    private void downloadAttachment(String Id, String file) {

        contants2.showProgressDialog(activity);
        kbId = Id;
        fileName = file;
        Map<String, String> params = new HashMap<String, String>();
        params.put("KbId", kbId);
        params.put("FileName", fileName);
        Retrofit retrofit = RetrofitClient.getInstance(Constants.BASE_URL);
        WebApiInterface apiInterface = retrofit.create(WebApiInterface.class);
        Call<KnowdlegeAttachmentResponce> call = apiInterface.getKnowledgeAttachment(strEmpId, strToken, params);

        call.enqueue(new Callback<KnowdlegeAttachmentResponce>() {
            @Override
            public void onResponse(Call<KnowdlegeAttachmentResponce> call, retrofit2.Response<KnowdlegeAttachmentResponce> response) {
                switch (response.code()) {
                    case 200:

                        KnowdlegeAttachmentResponce data = response.body();
                        if (writeFile(data.Attachments.get(0).FileData, data.Attachments.get(0).FileName))
                            viewFile(data.Attachments.get(0).FileName);
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
                }
            }
            @Override
            public void onFailure(Call<KnowdlegeAttachmentResponce> call, Throwable t) {
                call.cancel();

            }
        });
    }

    private void viewFile(String strFileName) {

        try {
            contants2.viewAttachment(getActivity(), strFileName);
        } catch (ActivityNotFoundException e) {
            Contants2.showToastMessage(getActivity(), getString(R.string.no_application_found), true);
        }
    }

    /***
     *
     * @param FileData
     * @param fileName
     * @return
     */
    private boolean writeFile(String FileData, String fileName) {
        File folder = new File(getActivity().getFilesDir(), Contants2.agora_folder);
        if (!folder.exists())
            folder.mkdir(); // If directory not exist, create a new one
        File file = new File(folder, fileName);
        try {
            if (file.exists())
                file.createNewFile();
            byte[] stringBytes = Base64.decode(FileData, Base64.DEFAULT);
            FileOutputStream os = new FileOutputStream(file.getAbsoluteFile(), false);
            os.write(stringBytes);
            os.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

}

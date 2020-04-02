package com.intelegain.agora.fragmments

import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.android.volley.DefaultRetryPolicy
import com.android.volley.NetworkResponse
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.intelegain.agora.R
import com.intelegain.agora.activity.AddKnowledgebase
import com.intelegain.agora.adapter.KnowledgeBaseAdapter
import com.intelegain.agora.api.urls.CommonMethods
import com.intelegain.agora.constants.Constants
import com.intelegain.agora.dataFetch.RetrofitClient
import com.intelegain.agora.interfeces.IKnlodedgeBaseAttachment
import com.intelegain.agora.interfeces.RecyclerItemClickListener
import com.intelegain.agora.interfeces.WebApiInterface
import com.intelegain.agora.model.*
import com.intelegain.agora.model.KnowledgebaseProjectName.ProjectDatum
import com.intelegain.agora.model.TechnologyData.TechnologyDatum
import com.intelegain.agora.utils.Contants2
import com.intelegain.agora.utils.Sharedprefrences.Companion.getInstance
import com.intelegain.agora.utils.VolleyMultipartRequest
import com.intelegain.agora.utils.VolleySingleton
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class KnowledgeBaseFragment : Fragment(), View.OnClickListener, OnRefreshListener, IKnlodedgeBaseAttachment {
    private val TAG = javaClass.simpleName
    private var activity: Activity? = null
    private var ivknowledge_filter: ImageView? = null
    private var edKnowledgeSkill: EditText? = null
    private var fabAddKnowledge: FloatingActionButton? = null
    private var recyler_view_for_knowledge: RecyclerView? = null
    private var mlstKnowledgeBaseDataList = ArrayList<KnowledgeBaseData>()
    private var knowledgeBaseAdapter: KnowledgeBaseAdapter? = null
    private var mBottomSheetDialog: BottomSheetDialog? = null
    private val retrofit = RetrofitClient.getInstance(Constants.BASE_URL)
    var contants2: Contants2? = null
    /**
     * Add New Knowledge widget
     */
    private lateinit var btnAddKnowledge: Button
    private lateinit var edEmployeeName: EditText
    private var edTitle: EditText? = null
    private var ed_description: EditText? = null
    private var ed_tag: EditText? = null
    private lateinit var tvProjectName: TextView
    private lateinit var tvTechnology: TextView
    private var tvTag: TextView? = null
    private var tv_try_again: TextView? = null
    private val mlstProjectNameList = ArrayList<String>()
    private val mlstTechnologyList = ArrayList<String>()
    private val mlstTechnologyFilterList = ArrayList<String>()
    private val projectDatumArrayList = ArrayList<ProjectDatum>()
    private val technologyDatumArrayList = ArrayList<TechnologyDatum>()
    private var dialogRecyclerView: RecyclerView? = null
    private var inflater: LayoutInflater? = null
    private val dialog_view: View? = null
    private var dialog: Dialog? = null
    private var progressDialog: ProgressDialog? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    var apiInterface = retrofit.create(WebApiInterface::class.java)
    var mSharedPrefs = getInstance(activity)
    var strToken = mSharedPrefs.getString("Token", "")
    var strEmpId = mSharedPrefs.getString("emp_Id", "")
    private var kbId: String? = ""
    private var fileName: String? = ""
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        retainInstance = true // to retain the state of fragment when activity recreates
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_knowledge_base, container, false)
        activity = getActivity()
        InitializeWidget(view)
        setEventClickListener()
        //        PrepareKnowledgeBaseDataList();
//        setKnowledgeAdapterData();
        edKnowledgeSkill!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) { //mTaskManagerAdapter.getFilter().filter(s);
                knowledgeBaseAdapter!!.filter(s.toString(), KnowledgeBaseAdapter.FILTER_WITH_TAG_LIST)
            }

            override fun afterTextChanged(s: Editable) {}
        })
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Initialize()
        // Get knowledgeBase data form server
        knowledgeBaseData
        projectNameList
        technologyNameList
    }

    override fun onRefresh() { // Get knowledgeBase data form server
        if (CommonMethods.checkInternetConnection(activity!!)) {
            knowledgeBaseData
            if (edKnowledgeSkill != null) edKnowledgeSkill!!.setText("")
        } else {
            swipeRefreshLayout!!.isRefreshing = false
            Contants2.showToastMessage(activity, getString(R.string.no_internet), true)
        }
    }

    private fun Initialize() {
        contants2 = Contants2()
    }

    /* Set the event click listener for views */
    private fun setEventClickListener() {
        ivknowledge_filter!!.setOnClickListener(this)
        edKnowledgeSkill!!.setOnClickListener(this)
        fabAddKnowledge!!.setOnClickListener(this)
        swipeRefreshLayout!!.setOnRefreshListener(this)
        tv_try_again!!.setOnClickListener(this)
    }

    /**
     * Initialize the widget
     *
     * @param view
     */
    private fun InitializeWidget(view: View) {
        recyler_view_for_knowledge = view.findViewById(R.id.recyler_view_for_knowledge)
        ivknowledge_filter = view.findViewById(R.id.ivknowledge_filter)
        edKnowledgeSkill = view.findViewById(R.id.edKnowledgeSkill)
        fabAddKnowledge = view.findViewById(R.id.fabAddKnowledge)
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)
        tv_try_again = view.findViewById(R.id.text_view_try_again)
        inflater = getActivity()!!.layoutInflater
        dialog = Dialog(getActivity()!!)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.ivknowledge_filter -> showTechnologyListFilterDialog()
            R.id.edKnowledgeSkill -> {
            }
            R.id.fabAddKnowledge -> startActivity(Intent(getActivity(), AddKnowledgebase::class.java))
            R.id.tvProjectName -> showProjectListDialog()
            R.id.tvTechnology -> showTechnologyListDialog()
            R.id.btnAddKnowledge -> SaveKnowledgeData()
            R.id.text_view_try_again -> {
                knowledgeBaseData
                projectNameList
                technologyNameList
            }
            else -> {
            }
        }
    }

    /**
     * Set knowledge adapter data
     */
    private fun setKnowledgeAdapterData() { // Set adapter data here
        knowledgeBaseAdapter = KnowledgeBaseAdapter(activity!!, mlstKnowledgeBaseDataList, strToken!!, strEmpId!!, this)
        recyler_view_for_knowledge!!.adapter = knowledgeBaseAdapter
        recyler_view_for_knowledge!!.layoutManager = LinearLayoutManager(activity)
    }

    /**
     * Open the Add knowledge screen for adding the new knowledge
     */
    private fun OpenAddNewKnowledgeSheet() {
        mBottomSheetDialog = BottomSheetDialog(activity!!)
        val sheetView = activity!!.layoutInflater.inflate(R.layout.add_knowledge, null)
        mBottomSheetDialog!!.setContentView(sheetView)
        InitializeAddNewViews(sheetView)
        mBottomSheetDialog!!.show()
    }

    /**
     * Initialize add new views
     */
    private fun InitializeAddNewViews(view: View) {
        btnAddKnowledge = view.findViewById(R.id.btnAddKnowledge)
        edEmployeeName = view.findViewById(R.id.edEmployeeName)
        edTitle = view.findViewById(R.id.edTitle)
        ed_description = view.findViewById(R.id.ed_description)
        ed_tag = view.findViewById(R.id.ed_tag)
        tvProjectName = view.findViewById(R.id.tvProjectName)
        tvTechnology = view.findViewById(R.id.tvTechnology)
        tvTag = view.findViewById(R.id.tvTagTitle)
        // Add click listener for addKnowledge button
        btnAddKnowledge.setOnClickListener(this)
        tvProjectName.setOnClickListener(this)
        tvTechnology.setOnClickListener(this)
    }

    /**
     * Is valid input in EditText
     */
    private fun IsValidInputInEditText(edToValid: EditText?): Boolean {
        if (edToValid == null) return false
        val strInput = edToValid.text.toString()
        return !TextUtils.isEmpty(strInput)
    }

    /**
     * Save Knowledge data to server
     */
    private fun SaveKnowledgeData() {
        if (IsValidInputInEditText(edEmployeeName)
                && IsValidInputInEditText(edTitle)
                && IsValidInputInEditText(ed_description)
                && IsValidInputInEditText(ed_tag)
                && !TextUtils.isEmpty(tvProjectName!!.text.toString())
                && !TextUtils.isEmpty(tvTechnology!!.text.toString())) {
            addKnowledgeBaseData()
            //Toast.makeText(activity, "Knowledge Added!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(activity, "Please fill all the data first!", Toast.LENGTH_SHORT).show()
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
    private fun showProjectListDialog() {
        CommonMethods().customSpinner(activity, "Select Project", inflater!!, dialogRecyclerView!!,
                mlstProjectNameList, dialog!!, dialog_view!!,
                object : RecyclerItemClickListener {
                    override fun recyclerViewListClicked(position: Int, itemClickText: String?) {
                        tvProjectName!!.text = itemClickText
                        tvProjectName!!.tag = mlstProjectNameList.indexOf(itemClickText)
                        dialog!!.hide()
                    }
                })
    }

    /**
     * Show Project name list dialog that to pick technology name
     */
    private fun showTechnologyListDialog() {
        dialogRecyclerView = null
        CommonMethods().customSpinner(activity, "Select Technology", inflater!!, dialogRecyclerView!!,
                mlstTechnologyList, dialog!!, dialog_view!!,
                object : RecyclerItemClickListener {
                    override fun recyclerViewListClicked(position: Int, itemClickText: String?) {
                        tvTechnology.text = itemClickText
                        tvTechnology.tag = mlstTechnologyList.indexOf(itemClickText)
                        dialog!!.hide()
                    }
                })
    }

    /**
     * Show Project name list filter dialog that to pick technology name
     */
    private fun showTechnologyListFilterDialog() {
        dialogRecyclerView = null
        CommonMethods().customSpinner(activity, "Select Technology", inflater!!, dialogRecyclerView!!,
                mlstTechnologyFilterList, dialog!!, dialog_view!!,
                object : RecyclerItemClickListener {
                    override fun recyclerViewListClicked(position: Int, itemClickText: String?) {
                        if (itemClickText.equals("See All", ignoreCase = true)) knowledgeBaseAdapter!!.filter("", KnowledgeBaseAdapter.FILTER_WITH_TECHNOLOGY) else knowledgeBaseAdapter!!.filter(itemClickText!!, KnowledgeBaseAdapter.FILTER_WITH_TECHNOLOGY)
                        dialog!!.hide()
                    }
                })
    }//Toast.makeText(activity, "Some error occurred! please try again later..", Toast.LENGTH_SHORT).show();//tv_try_again.setText(getString(R.string.no_data_found));
    //dismissProgressDialog();
// make list of parameter for sending the http request

    /******************************************** WEB API CALLS  */
    private val knowledgeBaseData: Unit
        private get() {
            if (Contants2.checkInternetConnection(getActivity())) {
                swipeRefreshLayout!!.isRefreshing = false
                contants2!!.showProgressDialog(getActivity())
                // make list of parameter for sending the http request
                val params: MutableMap<String, String?> = HashMap()
                params["EmpId"] = strEmpId
                val call = apiInterface.getKnowledgeBaseData(strEmpId, strToken)
                call.enqueue(object : Callback<KnowledgeMaster?> {
                    override fun onResponse(call: Call<KnowledgeMaster?>, response: Response<KnowledgeMaster?>) {
                        contants2!!.dismissProgressDialog()
                        when (response.code()) {
                            200 -> {
                                val objKnowledgeMaster = response.body()
                                val objKnowledgeBaseDataList = objKnowledgeMaster!!.knowledgeData
                                if (objKnowledgeBaseDataList!!.size > 0) {
                                    mlstKnowledgeBaseDataList = objKnowledgeBaseDataList
                                    setKnowledgeAdapterData()
                                    hideshowRecyclerView(false)
                                } else {
                                    hideshowRecyclerView(true)
                                    Contants2.showToastMessageAtCenter(activity, getString(R.string.no_data_found), true)
                                    //tv_try_again.setText(getString(R.string.no_data_found));
                                }
                                contants2!!.dismissProgressDialog()
                            }
                            403 -> {
                                contants2!!.dismissProgressDialog()
                                Contants2.showOkAlertDialog(getActivity(), response.message(), "") { dialog, which -> Contants2.forceLogout(getActivity()) }
                            }
                            500 -> {
                                hideshowRecyclerView(true)
                                contants2!!.dismissProgressDialog()
                                Contants2.showToastMessage(getActivity(), getString(R.string.some_error_occurred), true)
                            }
                            else -> {
                                hideshowRecyclerView(true)
                                contants2!!.dismissProgressDialog()
                                Contants2.showToastMessage(getActivity(), getString(R.string.some_error_occurred), true)
                            }
                        }
                        //dismissProgressDialog();
                        swipeRefreshLayout!!.isRefreshing = false
                    }

                    override fun onFailure(call: Call<KnowledgeMaster?>, t: Throwable) {
                        call.cancel()
                        contants2!!.dismissProgressDialog()
                        Contants2.showToastMessage(getActivity(), getString(R.string.some_error_occurred), true)
                        swipeRefreshLayout!!.isRefreshing = false
                        //Toast.makeText(activity, "Some error occurred! please try again later..", Toast.LENGTH_SHORT).show();
                    }
                })
            } else {
                hideshowRecyclerView(true)
                Contants2.showToastMessage(getActivity(), getString(R.string.no_internet), true)
            }
        }

    private fun addKnowledgeBaseData( /*final File KnowledgebaseFile*/) {
        if (Contants2.checkInternetConnection(getActivity())) {
            contants2!!.showProgressDialog(getActivity())
            val headers: HashMap<String, String> = HashMap<String, String>()
            /*   headers["empid"] = strEmpId
               headers["token"] = strToken*/
            headers.put("empid", strEmpId.toString());
            headers.put("token", strToken.toString());
            val multipartRequest: VolleyMultipartRequest = object : VolleyMultipartRequest(Constants.BASE_URL + "addKnowledge", headers, com.android.volley.Response.Listener<NetworkResponse> { networkResponse ->
                contants2!!.dismissProgressDialog()
                try {
                    val resultResponse = String(networkResponse.data)
                    var jsonObject: JSONObject? = null
                    try {
                        jsonObject = JSONObject(resultResponse)
                        if (jsonObject.getString("Status") == "1") {
                            Contants2.showToastMessage(getActivity(), jsonObject.getString("Message"), true)
                            mBottomSheetDialog!!.cancel()
                        } else {
                            Contants2.showToastMessage(getActivity(), jsonObject.getString("Message"), true)
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }, com.android.volley.Response.ErrorListener { error ->
                error.printStackTrace()
                contants2!!.dismissProgressDialog()
            }) {
                override fun getParams(): MutableMap<String, String?> {
                    val params: MutableMap<String, String?> = HashMap()
                    params["EmpId"] = strEmpId
                    params["KbDescrptn"] = ed_description!!.text.toString().trim { it <= ' ' }
                    params["KbTitle"] = edTitle!!.text.toString().trim { it <= ' ' }
                    params["ProjId"] = projectDatumArrayList[tvProjectName!!.tag.toString().toInt()].projId.toString()
                    params["TechId"] = technologyDatumArrayList[tvTechnology!!.tag.toString().toInt()].techId.toString()
                    params["Url"] = ""
                    params["SubTechName"] = ed_tag!!.text.toString()
                    return params
                }

                override fun getByteData(): MutableMap<String, DataPart?>? {
                    val params: MutableMap<String, DataPart?> = HashMap<String, DataPart?>()
                    params["UploadedImage"] = null
                    /*if (BitmapDrawable.createFromPath(photoFile.getPath()) != null)
                    params.put("UploadedImage", new DataPart(photoFile.getName().toString(), getFileDataFromImagePath(photoFile.getPath()), "image"));*/return params
                }
            }
            multipartRequest.retryPolicy = DefaultRetryPolicy(300000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
            VolleySingleton.getInstance(getActivity()!!)!!.addToRequestQueue(multipartRequest)
        } else {
            Contants2.showToastMessage(getActivity(), getString(R.string.no_internet), true)
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
                        */
/*new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                setValues(employeeProfile);
                                contants2.dismissProgressDialog();
                            }
                        }, 100);*/
/*
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
        });*/// make list of parameter for sending the http request
    /*if (mlstProjectNameList.size() == 0) {
        mlstProjectNameList.add("Knowledge Base");
        mlstProjectNameList.add("Procharge Android");
        mlstProjectNameList.add("Corvi LED");
        mlstProjectNameList.add("MUrgency");
        mlstProjectNameList.add("TATA World");
        mlstProjectNameList.add("Delhop");
    }*/
    /**
     * web api call for getting project name list
     */
    private val projectNameList: Unit
        private get() { // make list of parameter for sending the http request
            val params: MutableMap<String, String?> = HashMap()
            params["EmpId"] = strEmpId
            val call = apiInterface.getProjectNameList(strEmpId, strToken, params)
            call.enqueue(object : Callback<KnowledgebaseProjectName?> {
                override fun onResponse(call: Call<KnowledgebaseProjectName?>, response: Response<KnowledgebaseProjectName?>) {
                    Log.d("TAG", "Status Code: " + response.code())
                    val iStatusCode = response.code()
                    when (response.code()) {
                        200 -> {
                            val knowledgebaseProjectName = response.body()
                            projectDatumArrayList.addAll(knowledgebaseProjectName!!.projectData!!)
                            if (mlstProjectNameList.size > 0) mlstProjectNameList.clear()
                            val projectNameListSize = knowledgebaseProjectName.projectData!!.size
                            var i = 0
                            while (i < projectNameListSize) {
                                mlstProjectNameList.add(knowledgebaseProjectName.projectData!![i].projName!!)
                                i++
                            }
                        }
                        403 -> {
                        }
                        500 -> projectNameList
                    }
                }

                override fun onFailure(call: Call<KnowledgebaseProjectName?>, t: Throwable) {
                    call.cancel()
                }
            })
            /*if (mlstProjectNameList.size() == 0) {
                mlstProjectNameList.add("Knowledge Base");
                mlstProjectNameList.add("Procharge Android");
                mlstProjectNameList.add("Corvi LED");
                mlstProjectNameList.add("MUrgency");
                mlstProjectNameList.add("TATA World");
                mlstProjectNameList.add("Delhop");
            }*/
        }/*if (mlstTechnologyList.size() == 0) {
            mlstTechnologyList.add(".Net");
            mlstTechnologyList.add("Java");
            mlstTechnologyList.add("PHP");
            mlstTechnologyList.add("System Analysis");
            mlstTechnologyList.add("Miscellaneous");
            mlstTechnologyList.add("SQL");
            mlstTechnologyList.add("Javascript");
        }*/

    /**
     * web api call for getting technology list
     */
    private val technologyNameList: Unit
        private get() {
            val params: MutableMap<String, String?> = HashMap()
            params["EmpId"] = strEmpId
            val call = apiInterface.getTechnologyNameList(strEmpId, strToken, params)
            call.enqueue(object : Callback<TechnologyData?> {
                override fun onResponse(call: Call<TechnologyData?>, response: Response<TechnologyData?>) {
                    Log.d("TAG", "Status Code: " + response.code())
                    when (response.code()) {
                        200 -> {
                            val technologyData = response.body()
                            if (mlstTechnologyList.size > 0) mlstTechnologyList.clear()
                            technologyDatumArrayList.addAll(technologyData!!.technologyData!!)
                            val projectNameListSize = technologyData.technologyData!!.size
                            var i = 0
                            while (i < projectNameListSize) {
                                mlstTechnologyList.add(technologyData.technologyData!![i].techName!!)
                                mlstTechnologyFilterList.add(technologyData.technologyData!![i].techName!!)
                                i++
                            }
                        }
                        403 -> {
                        }
                        500 -> technologyNameList
                    }
                }

                override fun onFailure(call: Call<TechnologyData?>, t: Throwable) {
                    call.cancel()
                }
            })
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
    private fun applyLeave(strLeaveType: String, strLeaveFrom: String, strLeaveTo: String, strReason: String) {
        showProgressDialog()
        val apiInterface = retrofit.create(WebApiInterface::class.java)
        val mSharedPrefs = getInstance(activity)
        val strToken = mSharedPrefs.getString("Token", "")
        val strEmpId = mSharedPrefs.getString("emp_Id", "1000")
        // make list of parameter for sending the http request
        val params: MutableMap<String, String?> = HashMap()
        params["EmpId"] = strEmpId
        params["LeaveType"] = strLeaveType
        params["LeaveFrom"] = strLeaveFrom
        params["LeaveTo"] = strLeaveTo
        params["Reason"] = strReason
        val call = apiInterface.addKnowledge(strEmpId, strToken, params)
        call.enqueue(object : Callback<AddKnowledge?> {
            override fun onResponse(call: Call<AddKnowledge?>, response: Response<AddKnowledge?>) {
                Log.d("TAG", "Status Code: " + response.code())
                val iStatusCode = response.code()
                if (iStatusCode == 200) {
                    val objApplyLeave = response.body()
                    val strStatus = objApplyLeave!!.status
                    val strMessage = objApplyLeave.message
                    Toast.makeText(activity, "Status = $strStatus\nMessage = $strMessage", Toast.LENGTH_SHORT).show()
                    mBottomSheetDialog!!.cancel() // Close bottom sheet dialog
                } else {
                    Toast.makeText(activity, "Some error occurred! please try again later..", Toast.LENGTH_SHORT).show()
                }
                dismissProgressDialog()
                //                {
//                    "status": "1",
//                        "message": "Knowledge added successfully"
//                }
            }

            override fun onFailure(call: Call<AddKnowledge?>, t: Throwable) {
                call.cancel()
                dismissProgressDialog()
                Toast.makeText(activity, "Some error occurred! please try again later..", Toast.LENGTH_SHORT).show()
            }
        })
    }

    /**
     * @param hideshowRecyclerView boolean value if true then need to hide RecyclerView and false will make it visible
     */
    fun hideshowRecyclerView(hideshowRecyclerView: Boolean) {
        if (hideshowRecyclerView) {
            recyler_view_for_knowledge!!.visibility = View.GONE
            tv_try_again!!.visibility = View.VISIBLE
        } else {
            tv_try_again!!.visibility = View.GONE
            recyler_view_for_knowledge!!.visibility = View.VISIBLE
        }
    }
    /**************************************** WEB API CALLS ENDS  */
    /**
     * Show progresss dialog
     */
    private fun showProgressDialog() {
        progressDialog = ProgressDialog(activity)
        progressDialog!!.setMessage("Please wait ...")
        progressDialog!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        progressDialog!!.setCanceledOnTouchOutside(false)
        progressDialog!!.show()
    }

    /**
     * Dismiss the progress dialog
     */
    private fun dismissProgressDialog() {
        if (progressDialog != null) if (progressDialog!!.isShowing) progressDialog!!.dismiss()
    }

    /**
     * Create dummy data for display
     */
    private fun PrepareKnowledgeBaseDataList() { //        KnowledgeBaseData knowledgeBaseData;
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

    override fun getAttachment(kbId: String?, fileName: String?) {
        downloadAttachment(kbId, fileName)
    }

    private fun downloadAttachment(Id: String?, file: String?) {
        contants2!!.showProgressDialog(activity)
        kbId = Id
        fileName = file
        val params: MutableMap<String, String?> = HashMap()
        params["KbId"] = kbId
        params["FileName"] = fileName
        val retrofit = RetrofitClient.getInstance(Constants.BASE_URL)
        val apiInterface = retrofit.create(WebApiInterface::class.java)
        val call = apiInterface.getKnowledgeAttachment(strEmpId, strToken, params)
        call.enqueue(object : Callback<KnowdlegeAttachmentResponce?> {
            override fun onResponse(call: Call<KnowdlegeAttachmentResponce?>, response: Response<KnowdlegeAttachmentResponce?>) {
                when (response.code()) {
                    200 -> {
                        val data = response.body()
                        if (writeFile(data!!.Attachments!![0].FileData!!, data.Attachments!![0].FileName!!)) viewFile(data.Attachments!![0].FileName!!)
                    }
                    403 -> {
                        contants2!!.dismissProgressDialog()
                        Contants2.showOkAlertDialog(getActivity(), response.message(), "") { dialog, which -> Contants2.forceLogout(getActivity()) }
                    }
                    500 -> {
                        contants2!!.dismissProgressDialog()
                        Contants2.showToastMessage(getActivity(), getString(R.string.some_error_occurred), true)
                    }
                    else -> {
                    }
                }
            }

            override fun onFailure(call: Call<KnowdlegeAttachmentResponce?>, t: Throwable) {
                call.cancel()
            }
        })
    }

    private fun viewFile(strFileName: String) {
        try {
            contants2!!.viewAttachment(getActivity(), strFileName)
        } catch (e: ActivityNotFoundException) {
            Contants2.showToastMessage(getActivity(), getString(R.string.no_application_found), true)
        }
    }

    /***
     *
     * @param FileData
     * @param fileName
     * @return
     */
    private fun writeFile(FileData: String, fileName: String): Boolean {
        val folder = File(getActivity()!!.filesDir, Contants2.agora_folder)
        if (!folder.exists()) folder.mkdir() // If directory not exist, create a new one
        val file = File(folder, fileName)
        return try {
            if (file.exists()) file.createNewFile()
            val stringBytes = Base64.decode(FileData, Base64.DEFAULT)
            val os = FileOutputStream(file.absoluteFile, false)
            os.write(stringBytes)
            os.close()
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }
}
package com.intelegain.agora.fragmments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.intelegain.agora.R
import com.intelegain.agora.adapter.SkillMatrixRecyclerAdapter
import com.intelegain.agora.constants.Constants
import com.intelegain.agora.dataFetch.RetrofitClient
import com.intelegain.agora.interfeces.WebApiInterface
import com.intelegain.agora.model.CommonStatusMessage
import com.intelegain.agora.model.SaveSkillMatrixEntity
import com.intelegain.agora.model.SkillMatrixData
import com.intelegain.agora.model.SkillMatrixData.EmpSkill
import com.intelegain.agora.utils.Contants2
import com.intelegain.agora.utils.Sharedprefrences.Companion.getInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.util.*

class SkillMatrixFragment : Fragment(), View.OnClickListener, OnRefreshListener {
    var viewParent: View? = null
    var etSearchBySkill: EditText? = null
    var tv_try_again: TextView? = null
    var imageViewAddSkill: ImageView? = null
    lateinit var img_filter: ImageView
    var recyclerView_skill_matrix: RecyclerView? = null
    var btnSaveSkill: Button? = null
    var contants2: Contants2? = null
    var skillMatrixRecyclerAdapter: SkillMatrixRecyclerAdapter? = null
    var empSkillArrayList: ArrayList<EmpSkill>? = null
    var saveSkillArrayList = ArrayList<EmpSkill>()
    var skillMatrixData: SkillMatrixData? = null
    var currentScreen = ""
    var commonStatusMessage: CommonStatusMessage? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    var retrofit: Retrofit? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? { // Inflate the layout for this fragment
        viewParent = inflater.inflate(R.layout.fragment_skill_matrix, container, false)
        retrofit = RetrofitClient.getInstance(Constants.BASE_URL)
        initialize()
        findViews()
        getAllSkillMatrix("-1")
        setListenersForViews()
        return view
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menu.add(0, v.id, 0, resources.getString(R.string.get_All_Skill))
        menu.add(0, v.id, 0, resources.getString(R.string.My_Skill))
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        if (item.title == getString(R.string.get_All_Skill)) {
            if (currentScreen == "-1") return false
            getAllSkillMatrix("-1")
        } else if (item.title == getString(R.string.My_Skill)) {
            if (currentScreen == "0") return false
            getAllSkillMatrix("0")
        } else {
            return false
        }
        return true
    }

    private fun initialize() {
        empSkillArrayList = ArrayList()
        contants2 = Contants2()
    }

    private fun findViews() {
        swipeRefreshLayout = view!!.findViewById(R.id.swipe_refresh_layout)
        img_filter = view!!.findViewById(R.id.img_filter)
        etSearchBySkill = view!!.findViewById(R.id.etSearchBySkill)
        imageViewAddSkill = view!!.findViewById(R.id.imgAddSkill)
        recyclerView_skill_matrix = view!!.findViewById(R.id.recycler_skill_matrix)
        tv_try_again = view!!.findViewById(R.id.text_view_try_again)
        btnSaveSkill = view!!.findViewById(R.id.btnSaveSkill)
        registerForContextMenu(img_filter)
    }

    private fun setUpRecyclerView() { // recycler
        recyclerView_skill_matrix!!.setHasFixedSize(true)
        recyclerView_skill_matrix!!.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL,
                false)
        recyclerView_skill_matrix!!.layoutManager = LinearLayoutManager(activity)
        skillMatrixRecyclerAdapter = SkillMatrixRecyclerAdapter(activity, empSkillArrayList)
        recyclerView_skill_matrix!!.adapter = skillMatrixRecyclerAdapter
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

    private fun setListenersForViews() {
        swipeRefreshLayout!!.setOnRefreshListener(this)
        img_filter!!.setOnClickListener(this)
        imageViewAddSkill!!.setOnClickListener(this)
        tv_try_again!!.setOnClickListener(this)
        btnSaveSkill!!.setOnClickListener(this)
        etSearchBySkill!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                skillMatrixRecyclerAdapter!!.filter(etSearchBySkill!!.text.toString())
            }
        })
    }
    //    /**
//
//     */
    /**
     * @param viewAll --- "-1" value if landing screen or view all selected from search filter and "0"
     * Retrofit Web Call to get employee profile details. Calls the interface through fragment to activity to update the
     * image, name, emp id, designation received here.
     */
    private fun getAllSkillMatrix(viewAll: String) {
        if (Contants2.checkInternetConnection(activity)) {
            contants2!!.showProgressDialog(activity)
            swipeRefreshLayout!!.isRefreshing = false
            val apiInterface = retrofit!!.create(WebApiInterface::class.java)
            val mSharedPrefs = getInstance(activity)
            val strToken = mSharedPrefs.getString("Token", "")
            val strEmpId = mSharedPrefs.getString("emp_Id", "")
            val params: MutableMap<String, String?> = HashMap()
            params["EmpId"] = strEmpId
            params["ToggleSkill"] = viewAll
            params["SkillName"] = ""
            val call = apiInterface.getSkillMatrix(strEmpId, strToken, params)
            call.enqueue(object : Callback<SkillMatrixData?> {
                override fun onResponse(call: Call<SkillMatrixData?>, response: Response<SkillMatrixData?>) {
                    recyclerView_skill_matrix!!.visibility = View.VISIBLE
                    swipeRefreshLayout!!.isRefreshing = false
                    when (response.code()) {
                        200 -> {
                            currentScreen = viewAll
                            hideshowRecyclerSkillMatrix(false)
                            skillMatrixData = response.body()
                            if (empSkillArrayList!!.size > 0) {
                                empSkillArrayList!!.clear()
                                skillMatrixRecyclerAdapter!!.notifyDataSetChanged()
                            }
                            empSkillArrayList!!.addAll(skillMatrixData!!.lstEmpSkill)
                            if (empSkillArrayList!!.size > 0) {
                                setUpRecyclerView()
                            } else {
                                hideshowRecyclerSkillMatrix(true)
                                Contants2.showToastMessageAtCenter(activity, getString(R.string.no_data_found), true)
                                // tv_try_again.setText(getString(R.string.no_data_found));
                            }
                            contants2!!.dismissProgressDialog()
                        }
                        403 -> {
                            contants2!!.dismissProgressDialog()
                            hideshowRecyclerSkillMatrix(false)
                            Contants2.showOkAlertDialog(activity, response.message(), "") { dialog, which -> Contants2.forceLogout(activity) }
                        }
                        500 -> {
                            contants2!!.dismissProgressDialog()
                            Contants2.showToastMessage(activity, getString(R.string.some_error_occurred), true)
                            hideshowRecyclerSkillMatrix(true)
                        }
                        else -> {
                            contants2!!.dismissProgressDialog()
                            Contants2.showToastMessage(activity, getString(R.string.some_error_occurred), true)
                            hideshowRecyclerSkillMatrix(true)
                        }
                    }
                }

                override fun onFailure(call: Call<SkillMatrixData?>, t: Throwable) {
                    call.cancel()
                    contants2!!.dismissProgressDialog()
                    swipeRefreshLayout!!.isRefreshing = false
                    Contants2.showToastMessage(activity, getString(R.string.some_error_occurred), true)
                }
            })
        } else {
            Contants2.showToastMessage(activity, getString(R.string.no_internet), true)
            hideshowRecyclerSkillMatrix(true)
        }
    }

    /**
     * @param hideRecyclerSkillMatrix boolean value if true then need to hide constraint layout and false will make it visible
     */
    fun hideshowRecyclerSkillMatrix(hideRecyclerSkillMatrix: Boolean) {
        if (hideRecyclerSkillMatrix) {
            recyclerView_skill_matrix!!.visibility = View.GONE
            tv_try_again!!.visibility = View.VISIBLE
        } else {
            tv_try_again!!.visibility = View.GONE
            recyclerView_skill_matrix!!.visibility = View.VISIBLE
        }
    }

    private fun saveSkillMatrix() {
        if (Contants2.checkInternetConnection(activity)) {
            contants2!!.showProgressDialog(activity)
            val apiInterface = retrofit!!.create(WebApiInterface::class.java)
            val mSharedPrefs = getInstance(activity)
            commonStatusMessage = CommonStatusMessage()
            val strToken = mSharedPrefs.getString("Token", "")
            val strEmpId = mSharedPrefs.getString("emp_Id", "")
            val saveSkillMatrixEntity = SaveSkillMatrixEntity()
            saveSkillMatrixEntity.empId = strEmpId
            saveSkillMatrixEntity.userID = strEmpId
            saveSkillMatrixEntity.lstEmpSkill = saveSkillArrayList
            val call = apiInterface.saveSkillMatrix(strEmpId, strToken, saveSkillMatrixEntity)
            call.enqueue(object : Callback<CommonStatusMessage?> {
                override fun onResponse(call: Call<CommonStatusMessage?>, response: Response<CommonStatusMessage?>) {
                    when (response.code()) {
                        200 -> {
                            commonStatusMessage = response.body()
                            contants2!!.dismissProgressDialog()
                            if (commonStatusMessage!!.status == "1") {
                                Contants2.showToastMessage(activity, commonStatusMessage!!.message, true)
                                getAllSkillMatrix(currentScreen)
                            } else Contants2.showToastMessage(activity, commonStatusMessage!!.message, true)
                        }
                        403 -> {
                            contants2!!.dismissProgressDialog()
                            Contants2.showOkAlertDialog(activity, response.message(), "") { dialog, which -> Contants2.forceLogout(activity) }
                        }
                        500 -> {
                            contants2!!.dismissProgressDialog()
                            Contants2.showToastMessage(activity, getString(R.string.some_error_occurred), true)
                        }
                        else -> {
                            contants2!!.dismissProgressDialog()
                            Contants2.showToastMessage(activity, getString(R.string.some_error_occurred), true)
                        }
                    }
                }

                override fun onFailure(call: Call<CommonStatusMessage?>, t: Throwable) {
                    call.cancel()
                    contants2!!.dismissProgressDialog()
                    Contants2.showToastMessage(activity, getString(R.string.some_error_occurred), true)
                }
            })
        } else {
            Contants2.showToastMessage(activity, getString(R.string.no_internet), true)
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.imgAddSkill -> {
            }
            2 -> {
            }
            R.id.text_view_try_again -> getAllSkillMatrix(currentScreen)
            R.id.img_filter -> activity!!.openContextMenu(img_filter)
            R.id.btnSaveSkill -> {
                //for (SkillMatrixData.EmpSkill empSkill : empSkillArrayList) {
                if (saveSkillArrayList.size > 0) saveSkillArrayList.clear()
                val size = empSkillArrayList!!.size
                var i = 0
                while (i < size) {
                    if (empSkillArrayList!![i].valueHasChanged) {
                        saveSkillArrayList.add(empSkillArrayList!![i])
                    }
                    i++
                }
                if (saveSkillArrayList.size > 0) saveSkillMatrix() else Contants2.showToastMessage(activity, getString(R.string.skill_select_check), true)
            }
            else -> {
            }
        }
    }

    override fun onRefresh() {
        getAllSkillMatrix(currentScreen)
        if (etSearchBySkill != null) etSearchBySkill!!.setText("")
    }
}
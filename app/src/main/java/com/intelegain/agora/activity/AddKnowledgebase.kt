package com.intelegain.agora.activity

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Paint
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.DefaultRetryPolicy
import com.android.volley.NetworkResponse
import com.intelegain.agora.R
import com.intelegain.agora.api.urls.CommonMethods
import com.intelegain.agora.constants.Constants
import com.intelegain.agora.dataFetch.RetrofitClient
import com.intelegain.agora.interfeces.RecyclerItemClickListener
import com.intelegain.agora.interfeces.WebApiInterface
import com.intelegain.agora.model.KbAttchmentResponce
import com.intelegain.agora.model.KnowledgebaseProjectName
import com.intelegain.agora.model.KnowledgebaseProjectName.ProjectDatum
import com.intelegain.agora.model.TechnologyData
import com.intelegain.agora.model.TechnologyData.TechnologyDatum
import com.intelegain.agora.parser.FilePath
import com.intelegain.agora.utils.Contants2
import com.intelegain.agora.utils.Sharedprefrences
import com.intelegain.agora.utils.VolleyMultipartRequest
import com.intelegain.agora.utils.VolleySingleton.Companion.getInstance
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.*
import java.util.*
import kotlin.collections.HashMap

class AddKnowledgebase : AppCompatActivity(), View.OnClickListener {
    private lateinit var btnAddKnowledge: Button
    private var edEmployeeName: EditText? = null
    private var edTitle: EditText? = null
    private var ed_description: EditText? = null
    private var ed_tag: EditText? = null
    private var et_url: EditText? = null
    private lateinit var tvProjectName: TextView
    private lateinit var tvTechnology: TextView
    private lateinit var imageViewCancel: ImageView
    private var inflater: LayoutInflater? = null
    private val dialog_view: View? = null
    private var dialog: Dialog? = null
    private var ll_uploaded_fileName: LinearLayout? = null
    private val mlstProjectNameList = ArrayList<String>()
    private val mlstTechnologyList = ArrayList<String>()
    private val mlstTechnologyFilterList = ArrayList<String>()
    var contants2: Contants2? = null
    private var dialogRecyclerView /*, recyler_view_for_knowledge*/: RecyclerView? = null
    var strToken: String? = null
    var strEmpId: String? = null
    lateinit var retrofit: Retrofit
    var apiInterface: WebApiInterface? = null
    private val projectDatumArrayList = ArrayList<ProjectDatum>()
    private val technologyDatumArrayList = ArrayList<TechnologyDatum>()
    lateinit var mSharedPrefs: Sharedprefrences
    private val PICK_FILE_REQUEST = 1
    private var filePath: Uri? = null
    var selectedFilePath: String? = null
    var selectedFile: File? = null
    var mimeType: String? = null
    var lstFileName = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_knowledge)
        initializeObjects()
        initializeViews()
        projectNameList
        technologyNameList
    }

    private fun initializeObjects() {
        contants2 = Contants2()
        retrofit = RetrofitClient.getInstance(Constants.BASE_URL)
        apiInterface = retrofit.create(WebApiInterface::class.java)
        inflater = this.layoutInflater
        dialog = Dialog(this)
        mSharedPrefs = Sharedprefrences.getInstance(this@AddKnowledgebase)
        strToken = mSharedPrefs.getString("Token", "")
        strEmpId = mSharedPrefs.getString("emp_Id", "")
    }

    private fun initializeViews() {
        btnAddKnowledge = findViewById(R.id.btnAddKnowledge)
        edEmployeeName = findViewById(R.id.edEmployeeName)
        edTitle = findViewById(R.id.edTitle)
        ed_description = findViewById(R.id.ed_description)
        ed_tag = findViewById(R.id.ed_tag)
        tvProjectName = findViewById(R.id.tvProjectName)
        tvTechnology = findViewById(R.id.tvTechnology)
        et_url = findViewById<View>(R.id.ed_url) as EditText
        ll_uploaded_fileName = findViewById<View>(R.id.ll_uploaded_fileName) as LinearLayout
        findViewById<View>(R.id.rl_upload_file).setOnClickListener(this)
        //recyler_view_for_knowledge = findViewById(R.id.recyler_view_for_knowledge);
        imageViewCancel = findViewById(R.id.img_icon_close)
        // Add click listener for addKnowledge button
        btnAddKnowledge.setOnClickListener(this)
        tvProjectName.setOnClickListener(this)
        tvTechnology.setOnClickListener(this)
        imageViewCancel.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.tvProjectName -> showProjectListDialog()
            R.id.tvTechnology -> showTechnologyListDialog()
            R.id.rl_upload_file -> selectFile()
            R.id.btnAddKnowledge -> SaveKnowledgeData()
            R.id.img_icon_close -> {
                overridePendingTransition(R.anim.stay, R.anim.slide_in_down)
                onBackPressed()
            }
            else -> {
            }
        }
    }

    /**
     * select file to upload
     */
    private fun selectFile() {
        val intent = Intent()
        //sets the select file to all types of files
        intent.type = "*/*"
        //allows to select data and return it
        intent.action = Intent.ACTION_GET_CONTENT
        //starts new activity to select file and return data
        startActivityForResult(Intent.createChooser(intent, "Choose File to Upload.."), PICK_FILE_REQUEST)
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_FILE_REQUEST) {
            if (resultCode == Activity.RESULT_OK && data != null && data.data != null) {
                filePath = data.data
                selectedFilePath = FilePath.getPath(this, filePath!!)
                mimeType = this.contentResolver.getType(filePath!!)
                selectedFile = File(selectedFilePath)
                //addKnowledgeBaseData(true);// isfromImageUpload
            }
        }
    }

    private fun setFileName() {
        ll_uploaded_fileName!!.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        ll_uploaded_fileName!!.orientation = LinearLayout.VERTICAL
        val ll_file_tag = LinearLayout(this)
        ll_file_tag.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        ll_uploaded_fileName!!.orientation = LinearLayout.HORIZONTAL
        //ll_file_tag.setLayoutParams(params);
        for (i in lstFileName.indices) {
            val fileName = TextView(this)
            //ImageView imageView = new ImageView(this);
//imageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_attachment));
//imageView.setPadding(0, 3, 0, 0);
            fileName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_attachment, 0, 0, 0)
            fileName.compoundDrawablePadding = 5
            fileName.id = i //Setting Id for using in future
            fileName.text = lstFileName[i]
            fileName.setPadding(4, 0, 10, 0)
            fileName.paintFlags = fileName.paintFlags or Paint.UNDERLINE_TEXT_FLAG
            //ll_file_tag.addView(imageView);
            ll_file_tag.addView(fileName) //Finally adding view
            ll_file_tag.orientation = LinearLayout.VERTICAL //Setting Layout orientation
        }
        ll_uploaded_fileName!!.removeAllViews()
        ll_uploaded_fileName!!.addView(ll_file_tag)
        //        ll_uploaded_fileName.setOrientation(LinearLayout.VERTICAL);//Setting Layout orientation
    }

    private fun uploadFileToServer() { // Parsing any Media type file
        val requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), selectedFile)
        val fileToUpload = MultipartBody.Part.createFormData("UploadedImage", selectedFile!!.name, requestBody)
        val filename = RequestBody.create(MediaType.parse("multipart/form-data"), selectedFile!!.name)
        val retrofit = RetrofitClient.getInstance(Constants.BASE_URL)
        val apiInterface = retrofit.create(WebApiInterface::class.java)
        val call = apiInterface.uploadFile(fileToUpload, filename, strEmpId, strToken)
        call.enqueue(object : Callback<KbAttchmentResponce?> {
            override fun onResponse(call: Call<KbAttchmentResponce?>, response: Response<KbAttchmentResponce?>) {
                val serverResponse = response.body()
                if (serverResponse != null) {
                    if (serverResponse.Status.equals("1", ignoreCase = true)) {
                        Toast.makeText(applicationContext, serverResponse.Message, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(applicationContext, serverResponse.Message, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    assert(serverResponse != null)
                    Log.v("Response", serverResponse.toString())
                }
                //progressDialog.dismiss();
            }

            override fun onFailure(call: Call<KbAttchmentResponce?>, t: Throwable) {
                call.cancel()
                Log.e("Upload error:", t.message)
            }
        })
    }// make list of parameter for sending the http request
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
            val call = apiInterface!!.getProjectNameList(strEmpId, strToken, params)
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

                /*override fun onFailure(call: Call<*>, t: Throwable) {
                    call.cancel()
                }*/

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
        }

    /**
     * web api call for getting technology list
     */
    private val technologyNameList: Unit
        private get() {
            val params: MutableMap<String, String?> = HashMap()
            params["EmpId"] = strEmpId
            val call = apiInterface!!.getTechnologyNameList(strEmpId, strToken, params)
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
        }

    /**
     * Show Project name list dialog that to pick project name
     */
    private fun showProjectListDialog() {

        CommonMethods().customSpinner(this, "Select Project", inflater!!, dialogRecyclerView!!, mlstProjectNameList, dialog!!,
                dialog_view!!, object : RecyclerItemClickListener {
            override fun recyclerViewListClicked(position: Int, itemClickText: String?) {
                tvProjectName.text = itemClickText
                tvProjectName.tag = mlstProjectNameList.indexOf(itemClickText)
                dialog!!.hide()
            }
        })

        /* CommonMethods().customSpinner(this, "Select Project", inflater, dialogRecyclerView,
                 mlstProjectNameList, dialog, dialog_view
         ) { position, itemClickText ->
             tvProjectName!!.text = itemClickText
             tvProjectName!!.tag = mlstProjectNameList.indexOf(itemClickText)
             dialog!!.hide()
         }*/
    }

    /**
     * Show Project name list dialog that to pick technology name
     */
    private fun showTechnologyListDialog() {
        dialogRecyclerView = null

        CommonMethods().customSpinner(this, "Select Technology", inflater!!, dialogRecyclerView!!, mlstTechnologyList, dialog!!,
                dialog_view!!, object : RecyclerItemClickListener {
            override fun recyclerViewListClicked(position: Int, itemClickText: String?) {
                tvTechnology.text = itemClickText
                tvTechnology.tag = mlstTechnologyList.indexOf(itemClickText)
                dialog!!.hide()
            }
        })

        /* CommonMethods().customSpinner(this, "Select Technology", inflater, dialogRecyclerView,
                 mlstTechnologyList, dialog, dialog_view
         ) { position, itemClickText ->
             tvTechnology!!.text = itemClickText
             tvTechnology!!.tag = mlstTechnologyList.indexOf(itemClickText)
             dialog!!.hide()
         }*/
    }

    /**
     * Save Knowledge data to server
     */
    private fun SaveKnowledgeData() {
        if (IsValidInputInEditText(edEmployeeName)
                && IsValidInputInEditText(edTitle)
                && IsValidInputInEditText(ed_description)
                && IsValidInputInEditText(ed_tag)
                && IsValidInputInEditText(et_url)
                && !TextUtils.isEmpty(tvProjectName!!.text.toString())
                && !TextUtils.isEmpty(tvTechnology!!.text.toString())) {
            addKnowledgeBaseData(false)
            //addKnowledgeBaseData(new File(this.getFilesDir() + File.separator + Contants2.agora_folder + File.separator + Contants2.emp_profile_image));
//Toast.makeText(activity, "Knowledge Added!", Toast.LENGTH_SHORT).show();
        } else { //Toast.makeText(AddKnowledgebase.this, "Please fill in the data first!", Toast.LENGTH_SHORT).show();
            Contants2.showToastMessage(this, getString(R.string.valid_details), false)
        }
    }

    /**
     * Is valid input in EditText
     */
    private fun IsValidInputInEditText(edToValid: EditText?): Boolean {
        if (edToValid == null) return false
        val strInput = edToValid.text.toString()
        return !TextUtils.isEmpty(strInput)
    }

    private fun addKnowledgeBaseData(isFromFileUpload: Boolean) {
        if (Contants2.checkInternetConnection(this@AddKnowledgebase)) {
            contants2!!.showProgressDialog(this@AddKnowledgebase)
            val headers: MutableMap<String, String> = mutableMapOf<String, String>()
            headers.put("empid", strEmpId.toString())
            headers.put("token", strToken.toString())
            /* headers["empid"] = strEmpId
             headers["token"] = strToken*/
            val multipartRequest: VolleyMultipartRequest = object : VolleyMultipartRequest(Constants.BASE_URL + "addKnowledge", headers, com.android.volley.Response.Listener<NetworkResponse> { networkResponse ->
                contants2!!.dismissProgressDialog()
                try {
                    val resultResponse = String(networkResponse.data)
                    var jsonObject: JSONObject? = null
                    try {
                        jsonObject = JSONObject(resultResponse)
                        if (jsonObject.getString("Status") == "1") {
                            Contants2.showToastMessage(this@AddKnowledgebase, jsonObject.getString("Message"), true)
                            if (isFromFileUpload) {
                                lstFileName.add(selectedFile!!.name.toString())
                                setFileName()
                            } else {
                                overridePendingTransition(R.anim.stay, R.anim.slide_in_down)
                                onBackPressed()
                            }
                        } else {
                            Contants2.showToastMessage(this@AddKnowledgebase, jsonObject.getString("Message"), true)
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
                override fun getParams(): HashMap<String, String>? {
                    /*  var params: HashMap<*, *> = HashMap<Any?, Any?>()*/
                    var params: HashMap<String, String> = HashMap<String, String>()
                    params["EmpId"] = strEmpId.toString()
                    params["KbDescrptn"] = ed_description!!.text.toString().trim { it <= ' ' }
                    params["KbTitle"] = edTitle!!.text.toString().trim { it <= ' ' }
                    params["ProjId"] = projectDatumArrayList[tvProjectName!!.tag.toString().toInt()].projId.toString()
                    params["TechId"] = technologyDatumArrayList[tvTechnology!!.tag.toString().toInt()].techId.toString()
                    params["Url"] = et_url!!.text.toString().trim { it <= ' ' }
                    params["SubTechName"] = ed_tag!!.text.toString()
                    return params
                }

                override fun getByteData(): MutableMap<String, DataPart> {
                    //var params: Map<String, String> = HashMap<String, String>()
                    val params: MutableMap<String, DataPart> = mutableMapOf()
                    //params.put("UploadedImage", );

                    // params["UploadedImage"] = null

                    //params.put("UploadedImage", new DataPart(selectedFile.getName().toString(), getBytesFromFile(selectedFile.getPath()), mimeType));
/*params.put("UploadedImage", new DataPart("IMG_20180305_153226.jpg", getBytesFromFile("/storage/emulated/0/DCIM/Camera/IMG_20180305_153226.jpg"), "image"));
                    params.put("UploadedImage", new DataPart("Mobile.pdf", getBytesFromFile("/storage/emulated/0/Pictures/Mobile.pdf"), "pdf"));*/
/* if (BitmapDrawable.createFromPath(photoFile.getPath()) != null)
                    params.put("UploadedImage", new DataPart(photoFile.getName().toString(), getFileDataFromImagePath(photoFile.getPath()), "image"));*/return params
                }
            }
            multipartRequest.retryPolicy = DefaultRetryPolicy(300000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
            getInstance(this@AddKnowledgebase)!!.addToRequestQueue(multipartRequest)
        } else {
            Contants2.showToastMessage(this@AddKnowledgebase, getString(R.string.no_internet), true)
        }
    }

    /**
     * Convert imagepath to byte array     *     * @param imagePath - Image Path     * @return - image in byte array
     */
    fun getFileDataFromImagePath(imagePath: String?): ByteArray {
        val bitmap = BitmapFactory.decodeFile(imagePath)
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream)
        return byteArrayOutputStream.toByteArray()
    }

    private fun getBytesFromFile(path: String): ByteArray {
        val file = File(path)
        val size = file.length().toInt()
        val bytes = ByteArray(size)
        try {
            val buf = BufferedInputStream(FileInputStream(file))
            buf.read(bytes, 0, bytes.size)
            buf.close()
        } catch (e: FileNotFoundException) { // TODO Auto-generated catch block
            e.printStackTrace()
        } catch (e: IOException) { // TODO Auto-generated catch block
            e.printStackTrace()
        }
        return bytes
    }
}
package com.intelegain.agora.activity

import android.app.Activity
import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.NotificationCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.drawerlayout.widget.DrawerLayout.DrawerListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.github.angads25.filepicker.view.FilePickerDialog
import com.intelegain.agora.R
import com.intelegain.agora.fragmments.*
import com.intelegain.agora.interfeces.empProfileImageUpdate
import com.intelegain.agora.interfeces.updateDrawerImage
import com.intelegain.agora.utils.Contants2
import com.intelegain.agora.utils.Sharedprefrences
import de.hdodenhof.circleimageview.CircleImageView
import java.io.File

class DrawerActivity : AppCompatActivity(), View.OnClickListener, DrawerListener, empProfileImageUpdate, updateDrawerImage, NotificationFragment.OnNotificationListener {
    var linearlayout_drawer: LinearLayout? = null
    var mSharedPref: Sharedprefrences? = null
    var drawer_header: View? = null
    var linearView: View? = null
    var tvLogout: TextView? = null
    var tvAppVersion: TextView? = null
    var tvMatrix /*, tvLeaves*/: TextView? = null
    var tvHolidayWork: TextView? = null
    var tvPerformanceReview: TextView? = null
    var tvToolbarTitle: TextView? = null
    var tvDrawerRowTitle: TextView? = null
    var tvEmpName: TextView? = null
    var tvEmpId: TextView? = null
    var imgIcon: ImageView? = null
    var imgNotification: ImageView? = null
    var image_profile_pic: CircleImageView? = null
    private var toolbar: Toolbar? = null
    private var mDrawer: DrawerLayout? = null
    private var fragmentManager: FragmentManager? = null
    private var fragmentTransaction: FragmentTransaction? = null
    var myProfileMainFragment: MyProfile_MainFragment? = null
    var title: Array<String>? = null
    var contants2: Contants2? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS)
        setContentView(R.layout.drawer_activity)
        findView()
        intialize()
        initNavigationDrawer()
        Handler().postDelayed({
            val intent = intent
            if (intent.hasExtra("menuFragment")) {
                if (intent.getStringExtra("menuFragment").equals("NotificationFragment", ignoreCase = true)) {
                    addFragment(NotificationFragment(), "notifications", title!!.get(11))
                } else { // you can add other fragment
                    Toast.makeText(this@DrawerActivity, "Other Fragment", Toast.LENGTH_SHORT).show()
                }
            } else {
                addFragment(DashboardFragment(), "dashboard", title!![0])
            }
        }, 1)
        Handler().postDelayed({
            var isImagePresent = false
            val intent = intent
            isImagePresent = if (intent.hasExtra("isRemovePhoto")) intent.getBooleanExtra("isRemovePhoto", false) else mSharedPref!!.getBoolean(getString(R.string.key_is_remove_photo), false)
            setValuesForDrawer(isImagePresent)
        }, 100)
    }

    private fun intialize() {
        contants2 = Contants2()
    }

    private fun findView() {
        title = resources.getStringArray(R.array.menu_drawer)
        toolbar = findViewById(R.id.toolbar)
        drawer_header = findViewById(R.id.drawer_header)
        image_profile_pic = drawer_header!!.findViewById(R.id.image_profile_pic)
        tvEmpName = drawer_header!!.findViewById(R.id.tvEmpName)
        tvEmpId = drawer_header!!.findViewById(R.id.tvEmpId)
        drawer_header!!.setOnClickListener(this)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        tvToolbarTitle = toolbar!!.findViewById(R.id.tv_toolbar_title)
        imgNotification = toolbar!!.findViewById(R.id.notification)
        tvLogout = findViewById(R.id.txt_logout)
        tvAppVersion = findViewById(R.id.tvAppVersion)
        // programatically setting the version name here
        try {
            tvAppVersion!!.setText(appVersionName)
        } catch (e: PackageManager.NameNotFoundException) {
            tvAppVersion!!.setVisibility(View.GONE)
            e.printStackTrace()
        }
        tvLogout!!.setOnClickListener(this)
        imgNotification!!.setOnClickListener(this)
    }

    /**
     * sets the image, emp name and id for drawer header here.
     */
    fun setValuesForDrawer(isRemovePhoto: Boolean) {
        if (isRemovePhoto) image_profile_pic!!.setImageResource(R.drawable.profile_image) else image_profile_pic!!.setImageBitmap(contants2!!.compressedBitmap(filesDir.toString() + File.separator + Contants2.agora_folder + File.separator + Contants2.emp_profile_image, 100))
        val empName = mSharedPref!!.getString(getString(R.string.key_emp_name), "")
        val empId = mSharedPref!!.getString(getString(R.string.key_emp_id), "")
        tvEmpName!!.text = empName
        tvEmpId!!.text = empId
    }

    private fun addFragment(fragment: Fragment, fragmentTag: String, toolbar_title: String) {
        mDrawer!!.closeDrawer(GravityCompat.START)
        fragmentManager = supportFragmentManager
        hideKeyboard(this)
        Handler().postDelayed({
            if (fragmentManager!!.findFragmentByTag(fragmentTag) == null) {
                tvToolbarTitle!!.text = toolbar_title
                fragmentTransaction = fragmentManager!!.beginTransaction()
                fragmentTransaction!!.replace(R.id.containerView, fragment, fragmentTag)
                fragmentTransaction!!.commit()
            }
        }, 350)
    }

    private fun initNavigationDrawer() {
        linearlayout_drawer = findViewById(R.id.linearlayout_drawer)
        mSharedPref = Sharedprefrences.getInstance(this)
        mDrawer = findViewById(R.id.drawer_layout)
        val actionBarDrawerToggle: ActionBarDrawerToggle = object : ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open, R.string.drawer_close) {
            override fun onDrawerClosed(v: View) {
                super.onDrawerClosed(v)
            }

            override fun onDrawerOpened(v: View) {
                super.onDrawerOpened(v)
                hideKeyboard(this@DrawerActivity)
            }
        }
        actionBarDrawerToggle.isDrawerIndicatorEnabled = false
        toolbar!!.setNavigationIcon(R.drawable.menu)
        toolbar!!.setNavigationOnClickListener { mDrawer!!.openDrawer(GravityCompat.START) }
        mDrawer!!.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        setDrawer()
    }

    private fun setDrawer() {
        linearlayout_drawer!!.removeAllViews()
        createDrawerItems(title!![0], R.drawable.dashboard, 0)
        createDrawerItems(title!![1], R.drawable.my_project, 1)
        createDrawerItems(title!![2], R.drawable.personal, 2)
        createDrawerItems(title!![3], R.drawable.attendance, 3)
        createDrawerItems(title!![4], R.drawable.leaves_new, 4)
        createDrawerItems(title!![5], R.drawable.contacts, 5)
        createDrawerItems(title!![6], R.drawable.knowledge, 6)
        createDrawerItems(title!![7], R.drawable.favorite_new, 7)
        createDrawerItems(title!![8], R.drawable.about_new, 8)
        createDrawerItems(title!![9], R.drawable.contact_us, 9)
        createDrawerItems(title!![10], R.drawable.careers, 10)
        createDrawerItems(title!![11], R.drawable.ic_notification, 11)
    }

    private fun createDrawerItems(text: String, iconId: Int, position: Int) { /*if (position == 2) {
            addPersonalMenuRow();
        } else {*/
        linearView = LayoutInflater.from(this).inflate(R.layout.row_drawer, linearlayout_drawer, false)
        tvDrawerRowTitle = linearView!!.findViewById(R.id.txt_drawer_title)
        imgIcon = linearView!!.findViewById(R.id.img_drawer_icon)
        tvDrawerRowTitle!!.setText(text)
        imgIcon!!.setImageResource(iconId)
        linearView!!.setOnClickListener(View.OnClickListener {
            when (position) {
                0 -> addFragment(DashboardFragment(), "dashboard", title!![position])
                1 ->  // add my project
                    //addFragment(new MyProjectFragment(), "my_project", title[position]);
                    Contants2.showToastMessage(this@DrawerActivity, getString(R.string.under_development), false)
                2 -> addFragment(SkillMatrixFragment(), "skill_matrix", title!![position])
//                    Contants2.showToastMessage(this@DrawerActivity, getString(R.string.under_development), false)
                3 -> {
//                    Contants2.showToastMessage(this@DrawerActivity, getString(R.string.under_development), false)
                    addFragment(AttendanceFragment(), "attendance", title!![position])
                }
                4 -> addFragment(LeaveFragment(), "leaves", title!![position])
                5 -> addFragment(ContactsFragment(), "contacts", title!![position])
                6 ->  // Knowledge base
                    addFragment(KnowledgeBaseFragment(), "knowledge_base", title!![position])
                7 -> Contants2.showToastMessage(this@DrawerActivity, getString(R.string.under_development), false)
                8 ->  // About us
                    addFragment(AboutUsFragment(), "about_us", title!![position])
                9 ->  // contact us
                    addFragment(ContactUsFragment(), "contact_us", title!![position])
                10 ->  // career
                    addFragment(CareersFragment(), "careers", title!![position])
                11 ->  // career
                    addFragment(NotificationFragment(), "notifications", title!![position])
            }
        })
        linearlayout_drawer!!.addView(linearView)
        //}
    }

    @get:Throws(PackageManager.NameNotFoundException::class)
    private val appVersionName: String
        private get() {
            val pinfo = packageManager.getPackageInfo(packageName, 0)
            return "version - " + pinfo.versionName
        }
    /*  private void addPersonalMenuRow() {
        View personal_menu = getLayoutInflater().inflate(R.layout.row_personal, null);
        linearlayout_drawer.addView(personal_menu);
        final ImageView img_personal_menu_arrow = personal_menu.findViewById(R.id.img_drawer_arrow);
        img_personal_menu_arrow.setOnClickListener(this);
        final RelativeLayout relative_menu_personal = personal_menu.findViewById(R.id.relative_menu_personal);
        tvMatrix = personal_menu.findViewById(R.id.txt_matrix);
        // tvLeaves = (TextView) personal_menu.findViewById(R.id.txt_leaves);
        tvHolidayWork = personal_menu.findViewById(R.id.txt_holiday_working);
        tvPerformanceReview = personal_menu.findViewById(R.id.txt_performace_review);
        tvMatrix.setOnClickListener(this);
        //tvLeaves.setOnClickListener(this);
        tvHolidayWork.setOnClickListener(this);
        tvPerformanceReview.setOnClickListener(this);
        */
    /** TODO: NOTE: Temporary we are hiding the menu,
     * 'Holiday working' and 'Self performance review' menu under Personal menu
     * later once the design will get ready will enable it again  */
/*
        tvHolidayWork.setVisibility(View.GONE);
        tvPerformanceReview.setVisibility(View.GONE);

        personal_menu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (relative_menu_personal.getVisibility() == View.VISIBLE) {
                    img_personal_menu_arrow.setImageDrawable(getResources().getDrawable(R.drawable.gray_arrow));
                    relative_menu_personal.setVisibility(View.GONE);
                } else {
                    img_personal_menu_arrow.setImageDrawable(getResources().getDrawable(R.drawable.dropdown_arrow));
                    relative_menu_personal.setVisibility(View.VISIBLE);
                }
            }
        });

    }*/
    override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}

    override fun onDrawerOpened(drawerView: View) {}
    override fun onDrawerClosed(drawerView: View) {}
    override fun onDrawerStateChanged(newState: Int) {}
    override fun onClick(view: View) {
        when (view.id) {
            R.id.notification -> Contants2.showToastMessage(this@DrawerActivity, getString(R.string.under_development), false)
            R.id.txt_logout -> Contants2.doLogout(this)
            R.id.txt_matrix -> addFragment(SkillMatrixFragment(), "Skill Matrix", "Skill Matrix")
            R.id.txt_performace_review -> {
            }
            R.id.txt_holiday_working -> {
            }
            R.id.drawer_header -> {
                myProfileMainFragment = MyProfile_MainFragment()
                addFragment(myProfileMainFragment!!, "my_profile", "My Profile")
            }
            else -> {
            }
        }
    }

    override fun onBackPressed() {
        Contants2.showAlertDialog(this, "Do you want to close ?", "", { dialog, which -> finish() }, { dialog, which -> dialog.dismiss() }, true)
    }

    // interface implemented method used to update the profile image coming from child fragment web call
    override fun updateParentFragment(empImageUrl: String?, empName: String?, empId: String?, empDesignation: String?) {
        myProfileMainFragment = fragmentManager!!.findFragmentByTag("my_profile") as MyProfile_MainFragment?
        myProfileMainFragment!!.setUpProfileImage(empImageUrl, empName, empId, empDesignation)
    }

    /**
     * result here are called in [MyProfile_MainFragment]
     * If user allows the permission then do respective action
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            FilePickerDialog.EXTERNAL_READ_PERMISSION_GRANT ->  // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (myProfileMainFragment!!.isCamera) myProfileMainFragment!!.CallCameraIntent() else myProfileMainFragment!!.fileChooser()
                }
        }
    }

    override fun updateProfileImage(isRemovePhoto: Boolean) {
        setValuesForDrawer(isRemovePhoto)
    }

    fun hideKeyboard(ctx: Context) {
        val inputManager = ctx
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        // check if no view has focus:
        val v = (ctx as Activity).currentFocus ?: return
        inputManager.hideSoftInputFromWindow(v.windowToken, 0)
    }

    override fun onNotification(item: String?) { //        Toast.makeText(this, "" + item, Toast.LENGTH_SHORT).show();
        val notificationBuilder = NotificationCompat.Builder(this, "chanel_id") //                .setSmallIcon(getNotificationIcon())
                .setSmallIcon(R.drawable.ic_notification_icon)
                .setLargeIcon(BitmapFactory.decodeResource(applicationContext.resources, R.mipmap.ic_launcher))
                .setContentTitle("title")
                .setContentText("Message")
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, notificationBuilder.build())
    }

    private val notificationIcon: Int
        private get() {
            val useWhiteIcon = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
            return if (useWhiteIcon) R.drawable.ic_notification_icon else R.mipmap.ic_launcher
        }
}
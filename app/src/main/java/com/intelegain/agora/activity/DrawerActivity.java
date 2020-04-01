package com.intelegain.agora.activity;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.intelegain.agora.R;


import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.angads25.filepicker.view.FilePickerDialog;
import com.intelegain.agora.fragmments.AboutUsFragment;
import com.intelegain.agora.fragmments.CareersFragment;
import com.intelegain.agora.fragmments.ContactUsFragment;
import com.intelegain.agora.fragmments.ContactsFragment;
import com.intelegain.agora.fragmments.DashboardFragment;
import com.intelegain.agora.fragmments.KnowledgeBaseFragment;
import com.intelegain.agora.fragmments.LeavesFragment;
import com.intelegain.agora.fragmments.MyProfile_MainFragment;
import com.intelegain.agora.fragmments.NotificationFragment;
//import com.intelegain.agora.fragmments.NotificationsFragment;
import com.intelegain.agora.fragmments.SkillMatrixFragment;
import com.intelegain.agora.interfeces.empProfileImageUpdate;
import com.intelegain.agora.interfeces.updateDrawerImage;
import com.intelegain.agora.utils.Contants2;
import com.intelegain.agora.utils.Sharedprefrences;


import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class DrawerActivity extends AppCompatActivity implements OnClickListener,
        DrawerLayout.DrawerListener, empProfileImageUpdate, updateDrawerImage, NotificationFragment.OnNotificationListener {
    LinearLayout linearlayout_drawer;
    Sharedprefrences mSharedPref;
    View drawer_header, linearView;
    TextView tvLogout, tvAppVersion, tvMatrix/*, tvLeaves*/, tvHolidayWork, tvPerformanceReview,
            tvToolbarTitle, tvDrawerRowTitle, tvEmpName, tvEmpId;
    ImageView imgIcon, imgNotification;
    CircleImageView image_profile_pic;

    private Toolbar toolbar;
    private DrawerLayout mDrawer;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    MyProfile_MainFragment myProfileMainFragment;
    String[] title;

    Contants2 contants2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.drawer_activity);


        findView();
        intialize();
        initNavigationDrawer();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = getIntent();
                if (intent.hasExtra("menuFragment")) {
                    if (intent.getStringExtra("menuFragment").equalsIgnoreCase("NotificationFragment")) {
                        addFragment(new NotificationFragment(), "notifications", title[11]);
                    } else {
                        // you can add other fragment
                        Toast.makeText(DrawerActivity.this, "Other Fragment", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    addFragment(new DashboardFragment(), "dashboard", title[0]);
                }
            }
        }, 1);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean isImagePresent = false;
                Intent intent = getIntent();
                if (intent.hasExtra("isRemovePhoto"))
                    isImagePresent = intent.getBooleanExtra("isRemovePhoto", false);
                else
                    isImagePresent = mSharedPref.getBoolean(getString(R.string.key_is_remove_photo), false);
                setValuesForDrawer(isImagePresent);
            }
        }, 100);


    }


    private void intialize() {
        contants2 = new Contants2();
    }


    private void findView() {

        title = getResources().getStringArray(R.array.menu_drawer);
        toolbar = findViewById(R.id.toolbar);
        drawer_header = findViewById(R.id.drawer_header);
        image_profile_pic = drawer_header.findViewById(R.id.image_profile_pic);
        tvEmpName = drawer_header.findViewById(R.id.tvEmpName);
        tvEmpId = drawer_header.findViewById(R.id.tvEmpId);

        drawer_header.setOnClickListener(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tvToolbarTitle = toolbar.findViewById(R.id.tv_toolbar_title);
        imgNotification = toolbar.findViewById(R.id.notification);
        tvLogout = findViewById(R.id.txt_logout);
        tvAppVersion = findViewById(R.id.tvAppVersion);

        // programatically setting the version name here
        try {
            tvAppVersion.setText(getAppVersionName());
        } catch (PackageManager.NameNotFoundException e) {
            tvAppVersion.setVisibility(View.GONE);
            e.printStackTrace();
        }
        tvLogout.setOnClickListener(this);

        imgNotification.setOnClickListener(this);

    }


    /**
     * sets the image, emp name and id for drawer header here.
     */
    public void setValuesForDrawer(boolean isRemovePhoto) {
        if (isRemovePhoto)
            image_profile_pic.setImageResource(R.drawable.profile_image);
        else
            image_profile_pic.setImageBitmap(contants2.compressedBitmap((getFilesDir() + File.separator + Contants2.agora_folder + File.separator + Contants2.emp_profile_image), 100));
        String empName = mSharedPref.getString(getString(R.string.key_emp_name), ""),
                empId = mSharedPref.getString(getString(R.string.key_emp_id), "");

        tvEmpName.setText(empName);
        tvEmpId.setText(empId);
    }

    private void addFragment(final Fragment fragment, final String fragmentTag, final String toolbar_title) {
        mDrawer.closeDrawer(GravityCompat.START);
        fragmentManager = getSupportFragmentManager();

        hideKeyboard(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (fragmentManager.findFragmentByTag(fragmentTag) == null) {
                    tvToolbarTitle.setText(toolbar_title);
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.containerView, fragment, fragmentTag);
                    fragmentTransaction.commit();
                }
            }
        }, 350);


    }

    private void initNavigationDrawer() {
        linearlayout_drawer = findViewById(R.id.linearlayout_drawer);
        mSharedPref = Sharedprefrences.getInstance(this);
        mDrawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View v) {
                super.onDrawerClosed(v);
            }

            @Override
            public void onDrawerOpened(View v) {
                super.onDrawerOpened(v);
                hideKeyboard(DrawerActivity.this);
            }
        };
        actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
        toolbar.setNavigationIcon(R.drawable.menu);
        toolbar.setNavigationOnClickListener(new OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View view) {
                mDrawer.openDrawer(Gravity.START);
            }
        });
        mDrawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        setDrawer();
    }

    private void setDrawer() {
        linearlayout_drawer.removeAllViews();
        createDrawerItems(title[0], R.drawable.dashboard, 0);
        createDrawerItems(title[1], R.drawable.my_project, 1);
        createDrawerItems(title[2], R.drawable.personal, 2);
        createDrawerItems(title[3], R.drawable.attendance, 3);
        createDrawerItems(title[4], R.drawable.leaves_new, 4);
        createDrawerItems(title[5], R.drawable.contacts, 5);
        createDrawerItems(title[6], R.drawable.knowledge, 6);
        createDrawerItems(title[7], R.drawable.favorite_new, 7);
        createDrawerItems(title[8], R.drawable.about_new, 8);
        createDrawerItems(title[9], R.drawable.contact_us, 9);
        createDrawerItems(title[10], R.drawable.careers, 10);
        createDrawerItems(title[11], R.drawable.ic_notification, 11);
    }

    private void createDrawerItems(final String text, int iconId, final int position) {
        /*if (position == 2) {
            addPersonalMenuRow();
        } else {*/
        linearView = LayoutInflater.from(this).inflate(R.layout.row_drawer, linearlayout_drawer, false);
        tvDrawerRowTitle = linearView.findViewById(R.id.txt_drawer_title);
        imgIcon = linearView.findViewById(R.id.img_drawer_icon);
        tvDrawerRowTitle.setText(text);
        imgIcon.setImageResource(iconId);
        linearView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (position) {
                    case 0:
                        addFragment(new DashboardFragment(), "dashboard", title[position]);
                        break;
                    case 1:
                        // add my project
                        //addFragment(new MyProjectFragment(), "my_project", title[position]);
                        Contants2.showToastMessage(DrawerActivity.this, getString(R.string.under_development), false);
                        break;
                    case 2:
                        Contants2.showToastMessage(DrawerActivity.this, getString(R.string.under_development), false);
                        //  addFragment(new SkillMatrix(), "Skill Matrix", "Skill Matrix");
                        break;
                    case 3:
                        Contants2.showToastMessage(DrawerActivity.this, getString(R.string.under_development), false);
                        //addFragment(new AttendanceFragment(), "attendance", title[position]);
                        break;
                    case 4:
                        addFragment(new LeavesFragment(), "leaves", title[position]);
                        break;
                    case 5:
                        addFragment(new ContactsFragment(), "contacts", title[position]);
                        break;
                    case 6:
                        // Knowledge base
                        addFragment(new KnowledgeBaseFragment(), "knowledge_base", title[position]);
                        break;
                    case 7:
                        Contants2.showToastMessage(DrawerActivity.this, getString(R.string.under_development), false);
                        break;
                    case 8:
                        // About us
                        addFragment(new AboutUsFragment(), "about_us", title[position]);
                        break;
                    case 9:
                        // contact us
                        addFragment(new ContactUsFragment(), "contact_us", title[position]);
                        break;
                    case 10:
                        // career
                        addFragment(new CareersFragment(), "careers", title[position]);
                        break;
                    case 11:
                        // career
                        addFragment(new NotificationFragment(), "notifications", title[position]);
                        break;
                }
            }
        });

        linearlayout_drawer.addView(linearView);
        //}


    }

    private String getAppVersionName() throws PackageManager.NameNotFoundException {
        PackageInfo pinfo = getPackageManager().getPackageInfo(getPackageName(), 0);

        return "version - " + pinfo.versionName;
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

    /**
     * TODO: NOTE: Temporary we are hiding the menu,
     * 'Holiday working' and 'Self performance review' menu under Personal menu
     * later once the design will get ready will enable it again
     *//*
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
    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(View drawerView) {

    }

    @Override
    public void onDrawerClosed(View drawerView) {

    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.notification:
                Contants2.showToastMessage(DrawerActivity.this, getString(R.string.under_development), false);
                break;
            case R.id.txt_logout:
                Contants2.doLogout(this);
                break;
            case R.id.txt_matrix:
                addFragment(new SkillMatrixFragment(), "Skill Matrix", "Skill Matrix");
                break;
            case R.id.txt_performace_review:
//                Toast.makeText(DrawerActivity.this, "Performance", Toast.LENGTH_SHORT).show();
                break;
           /* case R.id.txt_leaves:
                Toast.makeText(DrawerActivity.this, "Leaves", Toast.LENGTH_SHORT).show();
                break;*/
            case R.id.txt_holiday_working:
                //              Toast.makeText(DrawerActivity.this, "Holiday  working", Toast.LENGTH_SHORT).show();
                break;
            case R.id.drawer_header:
                myProfileMainFragment = new MyProfile_MainFragment();
                addFragment(myProfileMainFragment, "my_profile", "My Profile");
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Contants2.showAlertDialog(this, "Do you want to close ?", "", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }, true);
    }

    // interface implemented method used to update the profile image coming from child fragment web call
    @Override
    public void updateParentFragment(String empImageUrl, String empName, String empId, String empDesignation) {
        myProfileMainFragment = (MyProfile_MainFragment) fragmentManager.findFragmentByTag("my_profile");
        myProfileMainFragment.setUpProfileImage(empImageUrl, empName, empId, empDesignation);
    }

    /**
     * result here are called in {@link MyProfile_MainFragment}
     * If user allows the permission then do respective action
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case FilePickerDialog.EXTERNAL_READ_PERMISSION_GRANT:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (myProfileMainFragment.isCamera)
                        myProfileMainFragment.CallCameraIntent();
                    else
                        myProfileMainFragment.fileChooser();

                }
        }
    }


    @Override
    public void updateProfileImage(boolean isRemovePhoto) {
        setValuesForDrawer(isRemovePhoto);
    }

    void hideKeyboard(Context ctx) {
        InputMethodManager inputManager = (InputMethodManager) ctx
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View v = ((Activity) ctx).getCurrentFocus();
        if (v == null)
            return;

        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    @Override
    public void onNotification(String item) {
//        Toast.makeText(this, "" + item, Toast.LENGTH_SHORT).show();
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "chanel_id")
//                .setSmallIcon(getNotificationIcon())
                .setSmallIcon(R.drawable.ic_notification_icon)
                .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.mipmap.ic_launcher))
                .setContentTitle("title")
                .setContentText("Message")
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }

    private int getNotificationIcon() {
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.drawable.ic_notification_icon : R.mipmap.ic_launcher;
    }

}

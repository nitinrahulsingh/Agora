package com.intelegain.agora.fragmments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.github.angads25.filepicker.view.FilePickerDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.intelegain.agora.R;
import com.intelegain.agora.utils.Contants2;
import com.intelegain.agora.utils.Sharedprefrences;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;


public class SettingsActivity extends Fragment implements View.OnClickListener {

    /*private GridView gv_leaves_dashboard;		/Gridview for showing all the Available options
    private static final String[] list_options = new String[] { "Available Leaves","Apply Leaves", "Leave Status","Approve Leaves"}; // This Array is holds the
                                                    // name which we are showing
                                                    // on Leaves Dashboard. */
    /*TextView Settings_title_text;
    Button chng_pass_btn, personaldetails_btn;*/
    ImageView back_img_Settings, img_Settings_drawer;
    //LinearLayout lay_back_img_Settings, lay_img_Settings_drawer;
    Sharedprefrences mSharedPref;
    //for drawer
    String isSuperAdmin;
    Button webview_closebutton;
    //int value = 0;
    private WebView webView;
    DrawerLayout drawer;
    // FrameLayout fragment_view;
    ListView navList;
    //ImageView mNavigationDrawerButton;
    final String[] data = {"Dashboard", "Contacts", "Attendance", "Leaves", "TimeSheet", "Favourites", "Settings",
            "Company Website",
            "About Us", "Contact Us", "Careers", "Logout"};//

    /*int[] icons = {R.drawable.dashboard, R.drawable.contacts_dashbrd, R.drawable.timesheet, R.drawable.timesheet,
            R.drawable.leaves, R.drawable.favorite_white, R.drawable.settings, R.drawable.company_web,
            R.drawable.about_us, R.drawable.contact_us, R.drawable.careers,
            R.drawable.logout};*/

    // new screen variables
    ViewPager viewPager;
    TabLayout myprofile_tablayout;
    FloatingActionButton floatingActionButton;
    View view/*,dialog_view*/;
    private boolean isCamera = false;
    boolean isImage = false;

    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int FILE_SELECT_REQUEST_CODE = 300;
    Dialog dialog;

    ImageView imageView_camera_icon, imageView_profile_pic;

    MyProfile_Change_Password_Fragment myProfileChangePasswordFragment;
    MyProfile_Personal_Details_Fragment myProfilePersonalDetailsFragment;
    Contants2 contants2;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_myprofile_change_password, container, false);

        init();
        //setToolBar();
        findView();
        setUpMyProfileViewPager();
        setListeners();
        sharedpreferences();
        // drawer_method();


        return view;

        /*ObjectAnimator animation = ObjectAnimator.ofFloat(leaves_title_text, "rotationY", 0.0f, 360f);
        animation.setDuration(3600);

		animation.setInterpolator(new AccelerateDecelerateInterpolator());
		animation.start();*/
    }

        /*@Override
        public void onCreateContextMenu (ContextMenu menu, View v, ContextMenu.ContextMenuInfo
        menuInfo){
            super.onCreateContextMenu(menu, v, menuInfo);
            menu.setHeaderTitle(R.string.title_image_picker);
            menu.add(0, v.getId(), 0, getResources().getString(R.string.camera));
            menu.add(0, v.getId(), 0, getResources().getString(R.string.gallery));
        }

        @Override
        public boolean onContextItemSelected (MenuItem item){

            if (item.getTitle() == "Camera") {
                if (!checkIfAlreadyhavePermission()) {
                    isCamera = true;
                    requestForSpecificPermission();


                } else {

                    CallCameraIntent();
                }
            } else if (item.getTitle() == "Gallery") {
                if (!checkIfAlreadyhavePermission()) {
                    isCamera = false;
                    requestForSpecificPermission();

                } else {
                    fileChooser();
                }
            } else {
                return false;
            }
            return true;
        }*/

    private void setListeners() {
        myprofile_tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        // change fab image according to tab

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            floatingActionButton.setImageDrawable(getResources().getDrawable(R.drawable.edit, getActivity().getTheme()));
                        } else {
                            floatingActionButton.setImageDrawable(getResources().getDrawable(R.drawable.edit));
                        }
                        break;
                    case 1:
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            floatingActionButton.setImageDrawable(getResources().getDrawable(R.drawable.save, getActivity().getTheme()));
                        } else {
                            floatingActionButton.setImageDrawable(getResources().getDrawable(R.drawable.save));
                        }
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (tab.getPosition() == 1) {
                    view = getActivity().getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setUpMyProfileViewPager() {
        myProfilePersonalDetailsFragment = new MyProfile_Personal_Details_Fragment();
        myProfileChangePasswordFragment = new MyProfile_Change_Password_Fragment();


        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        viewPagerAdapter.addFragment(myProfilePersonalDetailsFragment, "Personal Details");
        viewPagerAdapter.addFragment(myProfileChangePasswordFragment, "Change Password");


        viewPager.setAdapter(viewPagerAdapter);
        myprofile_tablayout.setupWithViewPager(viewPager);
        //  viewPager.setAdapter(adapter);
    }

    /*private void setToolBar() {
        Toolbar toolbar = (Toolbar) view.view.findViewById(R.id.included_tool_bar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.menu);
        getSupportActionBar().setTitle("");

    }*/

    private void sharedpreferences() {
        mSharedPref = Sharedprefrences.getInstance(getActivity().getApplicationContext());
        isSuperAdmin = mSharedPref.getString("isSuperAdmin", null);
        System.out.println("ADMIN: " + isSuperAdmin);
    }

    /**
     * This Method is for initialization
     */

    private void init() {
        contants2 = new Contants2();
    }

    /**
     * This Method is for declareing the id.
     */

    @SuppressLint("WrongViewCast")
    private void findView() {
        //Settings_title_text = (TextView) view.findViewById(R.id.Settings_title_text);
        /*chng_pass_btn = (Button) view.findViewById(R.id.chng_pass_btn);
        personaldetails_btn = (Button) view.findViewById(R.id.personaldetails_btn);*/

        back_img_Settings = (ImageView) view.findViewById(R.id.back_img_Settings);
        img_Settings_drawer = (ImageView) view.findViewById(R.id.img_Settings_drawer);

		/*lay_back_img_Settings = (LinearLayout) view.findViewById(R.id.lay_back_img_Settings);
        lay_img_Settings_drawer = (LinearLayout) view.findViewById(R.id.lay_img_Settings_drawer);*/

        drawer = (DrawerLayout) view.findViewById(R.id.drawer_layout);
        navList = (ListView) view.findViewById(R.id.navList);

        imageView_profile_pic = (ImageView) view.findViewById(R.id.image_profile_pic);
        imageView_camera_icon = (ImageView) view.findViewById(R.id.img_camera);

        viewPager = (ViewPager) view.findViewById(R.id.my_profile_viewpager);
        myprofile_tablayout = (TabLayout) view.findViewById(R.id.my_profile_tabs);
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab);
        //  mNavigationDrawerButton = (ImageView) view.findViewById(R.id.nav_drawer);

        floatingActionButton.setOnClickListener(this);
        imageView_camera_icon.setOnClickListener(this);
        imageView_profile_pic.setOnClickListener(this);
        registerForContextMenu(imageView_camera_icon);

        //Typeface font1 = Typeface.createFromAsset(getAssets(), "fonts/segoeuil.ttf");



        /*        chng_pass_btn.setTypeface(font1);
        personaldetails_btn.setTypeface(font1);*/
        // Settings_title_text.setTypeface(font1);

        /*chng_pass_btn.setOnClickListener(this);
        personaldetails_btn.setOnClickListener(this);*/
        //back_img_leave.setOnClickListener(this);
        //img_leaves_drawer.setOnClickListener(this);
/*        back_img_Settings.setOnClickListener(this);
        lay_back_img_Settings.setOnClickListener(this);
        lay_img_Settings_drawer.setOnClickListener(this);*/
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawer.openDrawer(Gravity.LEFT);
                break;
            case R.id.notification:
                Toast.makeText(getActivity(), "notification page", Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
        return true;
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lay_img_Settings_drawer:
                drawer.openDrawer(Gravity.RIGHT);
                break;

            case R.id.notification:
                Toast.makeText(getActivity(), "notification Screen", Toast.LENGTH_SHORT).show();
                break;

            /*case R.id.image_profile_pic:
                Toast.makeText(this, "profile pic", Toast.LENGTH_SHORT).show();
                break;*/

            case R.id.img_camera:
                // openContextMenu(imageView_camera);
                //showdialog();
                break;

      /*      case R.id.nav_drawer:
                drawer.openDrawer(Gravity.LEFT);
                //showdialog();
                break;*/


            default:
                break;

            /*case R.id.chng_pass_btn:
                Intent intent = new Intent(SettingsActivity.this, ChangePasswordActivity.class);
                if (android.os.Build.VERSION.SDK_INT >= 16) {
                    Bundle bundle = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.slide_in_right, R.anim.slide_out_right).toBundle();
                    startActivity(intent, bundle);
                    finish();
                } else {
                    startActivity(intent);
                    finish();
                }
                break;
            case R.id.personaldetails_btn:


                Intent intent2 = new Intent(SettingsActivity.this, PersonalDetailsActivity.class);
                if (android.os.Build.VERSION.SDK_INT >= 16) {
                    Bundle bundle = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.slide_in_right, R.anim.slide_out_right).toBundle();
                    startActivity(intent2, bundle);
                    finish();
                } else {
                    startActivity(intent2);
                    finish();
                }
                break;

            case R.id.lay_back_img_Settings:
                //Intent intent3 = new Intent(SettingsActivity.this,DashBordActivity.class);
                finish();
            /*if (android.os.Build.VERSION.SDK_INT >= 16)
            {
				Bundle bundle = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.slide_in_right,R.anim.slide_out_right).toBundle();
				//startActivity(intent3, bundle);
			}
			else
			{
				//startActivity(intent3);
			}
                break;


            case R.id.back_img_Settings:
                finish();
                break;*/

        }
    }


    /*private void showdialog() {

        LayoutInflater inflater = SettingsActivity.this.getLayoutInflater();
        dialog_view = inflater.inflate(R.layout.dialog_image_picker, null);
        dialog = new Dialog(SettingsActivity.this);
        dialog.setTitle(R.string.title_image_picker);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(dialog_view);
        dialog.show();

        TextView txt_camera = (TextView) dialog.view.findViewById(R.id.txt_camera);
        TextView txt_gallery_pick = (TextView) dialog.view.findViewById(R.id.txt_gallery_pick);

        txt_camera.setOnClickListener(this);
        txt_gallery_pick.setOnClickListener(this);
    }*/

    private void requestForSpecificPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, FilePickerDialog.EXTERNAL_READ_PERMISSION_GRANT);
    }

    /**
     * Check if app having permissions for External storage
     *
     * @return
     */
    private boolean checkIfAlreadyhavePermission() {
        int result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private void CallCameraIntent() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    private void fileChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), FILE_SELECT_REQUEST_CODE);
    }

    public Bitmap rotateImage(int angle, Bitmap bitmapSrc) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(bitmapSrc, 0, 0,
                bitmapSrc.getWidth(), bitmapSrc.getHeight(), matrix, true);
    }

    private Bitmap compressBitmap(Bitmap getbitmap) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        getbitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);
        return BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));

    }

    /**
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
            case FilePickerDialog.EXTERNAL_READ_PERMISSION_GRANT: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (isCamera)
                        CallCameraIntent();
                    else
                        fileChooser();

                } else {

                }
                return;
            }


        }
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FILE_SELECT_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {


                Uri selectedImage = data.getData();

                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();
                int rotate = contants2.getCameraPhotoOrientation(this, selectedImage, picturePath);

                imageView_profile_pic.setImageBitmap(rotateImage(rotate, compressBitmap(BitmapFactory.decodeFile(picturePath))));

                isImage = true;
            }
        } else if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {


                Bitmap photo = (Bitmap) data.getExtras().get("data");
                imageView_profile_pic.setImageBitmap(compressBitmap(photo));
                isImage = true;
            } else if (resultCode == RESULT_CANCELED) {

                // user cancelled Image capture
*//*                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();*//*

            } else {
                // failed to capture image
         *//*       Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();*//*
            }

        }
    }*/
}

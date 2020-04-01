package com.intelegain.agora.fragmments;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
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

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.github.angads25.filepicker.view.FilePickerDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.intelegain.agora.R;
import com.intelegain.agora.activity.BasicCropActivity;
import com.intelegain.agora.constants.Constants;
import com.intelegain.agora.interfeces.updateDrawerImage;
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

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


public class MyProfile extends Fragment implements View.OnClickListener, FileDownloader.onDownloadTaskFinish {

    ImageView back_img_Settings, img_Settings_drawer;
    TextView tv_emp_name, tv_emp_desination;
    Sharedprefrences mSharedPref;
    //for drawer
    String isSuperAdmin;
    private int selectedTab;


    DrawerLayout drawer;
    // FrameLayout fragment_view;
    ListView navList;

    // new screen variables
    ViewPager viewPager;
    TabLayout myprofile_tablayout;
    FloatingActionButton floatingActionButton;
    View view;
    public boolean isCamera = false;
    public boolean isImage = false;
    updateDrawerImage UpdateDrawerImage;

    public static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100, FILE_SELECT_REQUEST_CODE = 300, IMAGE_CROP = 5;
    Activity mActivity;
    public CircleImageView imageView_profile_pic;
    ImageView imageView_camera;
    MyProfile_Change_Password_Fragment myProfileChangePasswordFragment;
    MyProfile_Personal_Details_Fragment myProfilePersonalDetailsFragment;
    Contants2 contants2;


    Uri mCapturedImageURI;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof updateDrawerImage) {
            UpdateDrawerImage = (updateDrawerImage) context;
        } else {
            /*throw new RuntimeException(context.toString()
                    + " must implement OnEditInteractionListener");*/
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        UpdateDrawerImage = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my_profile, container, false);

        init();
        findView();
        setProfileImage();
        setUpMyProfileViewPager();
        setListeners();
        sharedpreferences();

        // drawer_method();


        return view;
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo
            menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle(R.string.title_image_picker);
        menu.add(0, v.getId(), 0, getString(R.string.camera));
        menu.add(0, v.getId(), 0, getString(R.string.gallery));

        if (!imageView_profile_pic.getDrawable().getConstantState()
                .equals(ContextCompat.getDrawable(getActivity(), R.drawable.profile_image).getConstantState())) {
            menu.add(0, v.getId(), 0, getString(R.string.remove_photo));
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if (item.getTitle().equals(getString(R.string.camera))) {
            if (!checkIfAlreadyhavePermission()) {
                isCamera = true;
                requestForSpecificPermission();
            } else {
                CallCameraIntent();
            }
        } else if (item.getTitle().equals(getString(R.string.gallery))) {
            if (!checkIfAlreadyhavePermission()) {
                isCamera = false;
                requestForSpecificPermission();

            } else {
                fileChooser();
            }
        } else if (item.getTitle().equals(getString(R.string.remove_photo))) {
            imageView_profile_pic.setImageResource(R.drawable.profile_image);
            uploadAlbum(new File(getActivity().getFilesDir() + File.separator + Contants2.agora_folder + File.separator + Contants2.emp_profile_image), true);

            // call web service here
        } else {
            return false;
        }
        return true;
    }

    private void setListeners() {
        myprofile_tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                selectedTab = tab.getPosition();
                switch (selectedTab) {
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
        mActivity = getActivity();
    }

    /**
     * This Method is for declareing the id.
     */

    private void findView() {

        back_img_Settings = view.findViewById(R.id.back_img_Settings);
        img_Settings_drawer = view.findViewById(R.id.img_Settings_drawer);

		/*lay_back_img_Settings = (LinearLayout) view.findViewById(R.id.lay_back_img_Settings);
        lay_img_Settings_drawer = (LinearLayout) view.findViewById(R.id.lay_img_Settings_drawer);*/

        drawer = view.findViewById(R.id.drawer_layout);
        navList = view.findViewById(R.id.navList);

        imageView_profile_pic = view.findViewById(R.id.image_profile_pic);
        imageView_camera = view.findViewById(R.id.img_camera);

        tv_emp_name = view.findViewById(R.id.tv_emp_name);
        tv_emp_desination = view.findViewById(R.id.tv_emp_desination);

        viewPager = view.findViewById(R.id.my_profile_viewpager);
        myprofile_tablayout = view.findViewById(R.id.my_profile_tabs);
        floatingActionButton = view.findViewById(R.id.fab);
        //  mNavigationDrawerButton = (ImageView) view.findViewById(R.id.nav_drawer);

        floatingActionButton.setOnClickListener(this);
        imageView_camera.setOnClickListener(this);
        imageView_profile_pic.setOnClickListener(this);
        registerForContextMenu(imageView_camera);

    }

    private void setProfileImage() {

        File profileImage = new File(getActivity().getFilesDir() + File.separator + Contants2.agora_folder
                + File.separator + Contants2.emp_profile_image);

        if (profileImage != null && profileImage.length() > 0)
            imageView_profile_pic.setImageBitmap(contants2.compressedBitmap((getActivity().getFilesDir() + File.separator
                    + Contants2.agora_folder + File.separator + Contants2.emp_profile_image), 100));
        else
            imageView_profile_pic.setImageResource(R.drawable.profile_image);
    }

    public void setUpProfileImage(String empImageUrl, String empName, String empId, String empDesignation) {

        // need to uncomment this code for checking when url available from WS
        /*"https://api.androidhive.info/images/glide/medium/deadpool.jpg";
        Glide.with(this)
                .load(new File(getActivity().getFilesDir() + File.separator + Contants2.agora_folder + File.separator + Contants2.emp_profile_image).getPath())
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model,
                                               Target<GlideDrawable> target,
                                               boolean isFirstResource) {
                        e.printStackTrace();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        return false;
                    }
                })
                .placeholder(R.drawable.profile_image)
                //.error(R.drawable.icon_user)
                .into(imageView_profile_pic);*/
        File profileImage = new File(getActivity().getFilesDir() + File.separator + Contants2.agora_folder + File.separator + Contants2.emp_profile_image);
        if (profileImage != null && profileImage.length() > 0)
            imageView_profile_pic.setImageBitmap(contants2.compressedBitmap((getActivity().getFilesDir() + File.separator + Contants2.agora_folder + File.separator + Contants2.emp_profile_image), 100));
        else
            imageView_profile_pic.setImageResource(R.drawable.profile_image);

        //imageView_profile_pic.setImageURI(Uri.parse(getActivity().getFilesDir() + File.separator + Contants2.agora_folder + File.separator + Contants2.emp_profile_image));
        String empNameAndId = empName + " - " + empId;
        tv_emp_name.setText(empNameAndId);
        if (empDesignation != null)
            tv_emp_desination.setText(empDesignation);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawer.openDrawer(Gravity.LEFT);
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

            case R.id.img_camera:
                getActivity().openContextMenu(imageView_camera);
                //showdialog();
                break;

            case R.id.fab:
                switch (selectedTab) {
                    case 0:
                        myProfilePersonalDetailsFragment.callEditProfileActivity();
                        break;
                    case 1:
                        myProfileChangePasswordFragment.validateAndMakeApiCall();
                        break;
                    default:
                        break;
                }


                break;
            default:
                break;


        }
    }

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
        return result == PackageManager.PERMISSION_GRANTED;
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

    public void CallCameraIntent() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "camera_image.png");
        mCapturedImageURI = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent intentPicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentPicture.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
        startActivityForResult(intentPicture, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);

        /*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);*/
    }

    public void fileChooser() {
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

    public Bitmap compressBitmap(Bitmap getbitmap) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        getbitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);
        return BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));

    }

    public void uploadAlbum(final File photoFile, final boolean isRemovePhoto) {
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Constants.BASE_URL + "imageupload", getHeadersTokenAlbum(), new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse networkResponse) {
                try {
                    String resultResponse = new String(networkResponse.data);
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(resultResponse);
                        final String message = jsonObject.getString("Message");
                        if (jsonObject.getString("Status").equals("1")) {

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    File folder = new File(mActivity.getFilesDir(), Contants2.agora_folder);
                                    if (!folder.exists())
                                        folder.mkdir(); // If directory not exist, create a new one

                                    File destFile = new File(folder, Contants2.emp_profile_image);
                                    try {
                                        if (!isRemovePhoto)
                                            saveProfileImage(photoFile, destFile, message);
                                        else {
                                            Contants2.showToastMessage(getContext(), message, false);
                                            UpdateDrawerImage.updateProfileImage(true);
                                            photoFile.delete();
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    //convertSaveImageViewToBase64();

                                }
                            }, 200);

                        } else {
                            /*String strMsg;
                            if (!isRemovePhoto)
                                strMsg = "Failed to upload image. Please try again.";
                            else
                                strMsg = "Failed to remove image.";*/
                            Contants2.showToastMessage(getActivity(), message, true);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map getParams() {
                Map params = new HashMap<>();
                params.put("EmpId", mSharedPref.getString("emp_Id", ""));
                params.put("removePhoto", isRemovePhoto + "");


                return params;
            }

            @Override
            protected Map getByteData() {
                Map params = new HashMap<>();
                if (!isRemovePhoto) {
                    if (BitmapDrawable.createFromPath(photoFile.getPath()) != null)
                        params.put("UploadedImage", new VolleyMultipartRequest.DataPart(photoFile.getName().toString(), getFileDataFromImagePath(photoFile.getPath()), "image/*"));
                }
                return params;
            }
        };
        multipartRequest.setRetryPolicy(new DefaultRetryPolicy(300000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(multipartRequest);
    }


    @Override
    public void onDownloadSuccess(String strResult, String fileName) {
        UpdateDrawerImage.updateProfileImage(false);
        Contants2.showToastMessage(getActivity(), "Profile image updated sucessfully.", false);
    }

    @Override
    public void onDownloadFailed(String strResult) {

    }

    private Map getHeadersTokenAlbum() {
        Map headers = new HashMap<>();

        headers.put("empid", mSharedPref.getString("emp_Id", ""));
        headers.put("token", mSharedPref.getString("Token", ""));
        /*headers.put("p4", Constatnts.TokenSource);
        headers.put("p5", Constatnts.TokenCreatedByIPAddress);
        headers.put("p6", Constatnts.TokenDeviceID);*/
        return headers;
    }

    /**
     * Convert imagepath to byte array     *     * @param imagePath - Image Path     * @return - image in byte array
     */
    public byte[] getFileDataFromImagePath(String imagePath) {
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    /**
     * This method is used to get real path of file from from uri
     *
     * @param contentUri
     * @return String
     */
    //----------------------------------------
    public String getRealPathFromURI(Uri contentUri) {
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = getActivity().getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e) {
            return contentUri.getPath();
        }
    }


    public static Bitmap rotate(Bitmap bitmap, int degree) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        Matrix mtx = new Matrix();
        //       mtx.postRotate(degree);
        mtx.setRotate(degree);

        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*
         * need to send file path while calling camera intent
         * for gallery need to get file path as per version
         * */
        if (requestCode == FILE_SELECT_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Uri selectedImage = data.getData();

                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();
                int rotate = contants2.getCameraPhotoOrientation(getActivity(), selectedImage, picturePath);

                Glide.with(getActivity()).clear(imageView_profile_pic);
                //imageView_profile_pic.setImageBitmap(rotateImage(rotate, compressBitmap(BitmapFactory.decodeFile(picturePath))));

                Intent intent = new Intent(getActivity(), BasicCropActivity.class);
                File imageFile = new File(picturePath);
                intent.setData(Uri.fromFile(imageFile));
                startActivityForResult(intent, IMAGE_CROP);

                //uploadAlbum(new File(picturePath), false);

                isImage = true;
            }
        } else if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Glide.with(getActivity()).clear(imageView_profile_pic);
                String path = getRealPathFromURI(mCapturedImageURI);
                int rotate = contants2.getCameraPhotoOrientation(getActivity(), mCapturedImageURI, path);
                //imageView_profile_pic.setImageBitmap(rotateImage(rotate, compressBitmap(BitmapFactory.decodeFile(path))));

                Intent intent = new Intent(getActivity(), BasicCropActivity.class);
                File imageFile = new File(getRealPathFromURI(mCapturedImageURI));
                intent.setData(Uri.fromFile(imageFile));
                startActivityForResult(intent, IMAGE_CROP);

                // uploadAlbum(new File(getRealPathFromURI(mCapturedImageURI)), false);

                isImage = true;
            } else if (resultCode == RESULT_CANCELED) {

                // user cancelled Image capture
                /*Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();*/

            }
        } else if (requestCode == IMAGE_CROP) {
            // crop activity result
            try {
                if (requestCode == IMAGE_CROP) {
                    String image_name = data.getStringExtra("image");
                    File bitmap = new ImageSaver(getActivity()).
                            setFileName(image_name).
                            getFilepath();

                    imageView_profile_pic.setImageBitmap(new ImageSaver(getActivity()).
                            setFileName(image_name).
                            load());
                    uploadAlbum(bitmap, false);
                }
            } catch (Exception e) {
                e.printStackTrace();
                /*String errorMessage = "Whoops - your device doesn't support the crop action!";
                Contants2.showToastMessage(getActivity(), errorMessage, true);*/
                // toast.show();
            }
        }
    }

    private void setOrientatedImage(Uri mCapturedImageURI) {
        try {
            File f = new File(mCapturedImageURI.getPath());
            ExifInterface exif = new ExifInterface(f.getPath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            int angle = 0;

            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                angle = 90;
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                angle = 180;
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                angle = 270;
            }

            Matrix mat = new Matrix();
            mat.postRotate(angle);

            Bitmap bmp = BitmapFactory.decodeStream(new FileInputStream(f), null, null);
            Bitmap correctBmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), mat, true);
            imageView_profile_pic.setImageBitmap(correctBmp);
        } catch (IOException e) {
            Log.w("TAG", "-- Error in setting image");
        } catch (OutOfMemoryError oom) {
            Log.w("TAG", "-- OOM Error in setting image");
        }
    }

    FileDownloader fileDownloader;

    public void convertSaveImageViewToBase64() {
        //encode image to base64 string

        // imageView_profile_pic.setDrawingCacheEnabled(true);
        Bitmap bitmap = imageView_profile_pic.getDrawingCache();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        final byte[] byteArray = byteArrayOutputStream.toByteArray();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String base64Data = Base64.encodeToString(byteArray, Base64.DEFAULT);
                File folder = new File(mActivity.getFilesDir(), Contants2.agora_folder);
                if (!folder.exists())
                    folder.mkdir(); // If directory not exist, create a new one

                File file = new File(folder, Contants2.emp_profile_image);
                try {

                    if (file.exists() && file.length() > 0) {
                        //file.delete();
                        file.createNewFile();
                    }


                    byte[] stringBytes = Base64.decode(base64Data, Base64.DEFAULT);

                    //FileOutputStream os = new FileOutputStream(file.getAbsolutePath(), false);
                    FileOutputStream os = new FileOutputStream(file.getAbsolutePath(), false);
                    os.write(stringBytes);
                    os.close();


                    // UpdateDrawerImage.updateProfileImage();
                    Contants2.showToastMessage(getActivity(), "Profile image updated sucessfully.", false);

                } catch (IOException e) {
                    e.printStackTrace();
                    //Contants2.showToastMessage(getActivity(), "Profile image updated sucessfully.", false);
                }
            }
        }, 1);

        /*fileDownloader = new FileDownloader(getActivity(), this);
        fileDownloader.execute(base64Data, Contants2.emp_profile_image, Contants2.emp_profile_image);*/
    }

    public void saveProfileImage(File src, File dst, String serverMessage) throws IOException {
        InputStream in = new FileInputStream(src);
        try {
            OutputStream out = new FileOutputStream(dst);
            try {
                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                UpdateDrawerImage.updateProfileImage(false);
                Contants2.showToastMessage(getActivity(), serverMessage, false);
            } finally {
                out.close();
            }
        } finally {
            in.close();
        }
    }

}

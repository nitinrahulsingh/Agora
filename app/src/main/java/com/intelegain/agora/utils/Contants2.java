package com.intelegain.agora.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.intelegain.agora.BuildConfig;
import com.intelegain.agora.R;
import com.intelegain.agora.fragmments.New_Home_activity;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Contants2 {

    public String KEY_SUCCESS = "success", EMPLOYEE_NAME = "emp_name", EMPLOYEE_EMAIL = "emp_name",
            EMPLOYEE_PHONE_NUMBER = "emp_phone_number", EMPLOYEE_ID = "emp_id,", PASSWORD = "paswd";

    //Sirus urls
    /*public static final String BASE_URL = "http://10.0.0.2:170/AgoraMobile.api/";
    public static final String CUSTOMERBASE_URL = "http://10.0.0.2:171/AgoraMobile.api/";*/

    //Staging url
    //public static final String BASE_URL = "http://52.172.192.203/AgoraMobile.api/";
    //public static final String CUSTOMERBASE_URL = "http://www.agoracust.sirus/AgoraMobile.api/";
    ////////////Sirus New URL
    //public static final String BASE_URL = "http://168.63.218.137//AgoraMobile.api/";  ////Straiging URL

    //public static final String BASE_URL = "http://52.172.192.203/AgoraMobile.api/";
    //public static final String BASE_URL = "http://emp.intelgain.com/AgoraMobile.api/"; ///Live URL


    public static AlertDialog.Builder errorDialog = null;
    public static final String HRManualContentCode = "HRPolicy";
    public static final String MediclaimPolicyContentCode = "MediclaimPolicy";
    public static final String AntiSexualPolicyContentCode = "ASHPolicy";


    public static String Old_Password = "Please enter old password.";
    public static String logged_user_token = "";
    public static boolean logged_in = false;

    public static final String agora_folder = "agora";
    public static final String emp_profile_image = "profile.png";
    public static final String hr_manual_file_name = "hrManual.pdf";
    public static final String mediclaim_policy_file_name = "mediclaim.pdf";
    public static final String ASH_policy_file_name = "ash.pdf";
    private static final String IntelgainFileProvider = ".fileprovider";
    public static String ApplicationPDF = "application/pdf";


    public static String KBID = "kbId";
    public static String KB_FILE_NAME = "kbFileName";
    public static String EMP_ID = "empId";
    public static String TOKEN = "token";

    public static String NEWS_LINK_UNAVAILABLE = "Link not available";
    /*public static final String  = "profile.png";
    public static final String emp_profile_image = "profile.png";*/


    public static final String MINT_API_KEY = "UA-83015668-1";

    private ProgressDialog progressDialog;


    public int getCameraPhotoOrientation(Context context, Uri imageUri, String imagePath) {
        int rotate = 0;
        try {
            context.getContentResolver().notifyChange(imageUri, null);
            File imageFile = new File(imagePath);

            ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
                default:
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return rotate;
    }

    public Bitmap getAndReturnitMap(Bitmap inBtmp, String strPthh) {
        Bitmap btt = inBtmp;
        int angle = 0;
        Matrix mat = new Matrix();
        try {
            File f1 = new File(strPthh);
            ExifInterface exif = new ExifInterface(f1.getPath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            f1 = null;
            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                angle = 90;
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                angle = 180;
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                angle = 270;
            }
            if (angle > 0) {
                mat.postRotate(angle);                //int temp=maxHeight;                //maxHeight=maxWidth;                //maxWidth=temp;
                if (angle > 0) {
                    btt = Bitmap.createBitmap(inBtmp, 0, 0, inBtmp.getWidth(), inBtmp.getHeight(), mat, true);
                }
            }
        } catch (IOException e) {
            Log.w("TAG", "-- Error in setting image");
        } catch (OutOfMemoryError oom) {
            Log.w("TAG", "-- OOM Error in setting image");
        }
        return btt;
    }

    public static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }


    public static void LogWritter(String TAG, String CONTENT) {
        Log.d(TAG, CONTENT);
    }


    /**
     * @param editText : EditText where the underline color filter needs to be added
     * @param color    : color for EditText underline
     * @return Drawable to be applied to EditText
     */
    public Drawable set_EditText_With_UnderLine(EditText editText, int color) {
        Drawable background = editText.getBackground();
        background.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));
        return background;
    }


    /**
     * Calculate the Height and width of the File from given path and Compressed the file size
     *
     * @param strPath
     */
    public Bitmap compressedBitmap(String strPath, int MAXWIDTH) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        //Returns null, sizes are in the options variable
        BitmapFactory.decodeFile(strPath, options);
        float Swidth = options.outWidth;
        float Sheight = options.outHeight;
        //If you want, the MIME type will also be decoded (if possible)
        //String type = options.outMimeType;
        // ChckImgHt=(int)Sheight;
        // ChckImgWdth=(int)Swidth;

        float width1, height1;
        if (Swidth > MAXWIDTH) {

            float ratio = (Sheight / Swidth);

            if (Swidth > MAXWIDTH)

            {

                height1 = (ratio * MAXWIDTH);

                width1 = MAXWIDTH;

            } else

            {

                height1 = (ratio * Swidth);

                width1 = Swidth;

            }

        } else if (Sheight > MAXWIDTH) {

            float ratio = (Sheight / Swidth);
            height1 = (ratio * Swidth);
            width1 = Swidth;
        } else {
            width1 = Swidth;
            height1 = Sheight;
        }
        Bitmap bmm = decodeSampledBitmapFromResourceDrawable(strPath, (int) width1, (int) height1);
        return bmm;

        /*try {
            //  File fImage=new File(filePath);

            if (Swidth > 800 || Sheight > 800)//((fImage.length()/1024)>800)
            {
                FileOutputStream out;
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmm.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                // arrThumbByte=byteArray;
                out = new FileOutputStream(filePath);
                out.write(byteArray);
                out.close();


            }

        } catch (Exception e) {

        }
*/
    }

    /////new compression method
    public Bitmap decodeSampledBitmapFromResourceDrawable(String strres,
                                                          int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // BitmapFactory.decodeResource(res, resId, options);
        BitmapFactory.decodeFile(strres, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSizeNew(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        Bitmap bbbb = BitmapFactory.decodeFile(strres, options);
        bbbb = getAndReturnitMap(bbbb, strres);

        return bbbb;//(res, resId, options);
    }

            /*public Bitmap getAndReturnitMap (Bitmap inBtmp, String strPthh){
                Bitmap btt = inBtmp;
                int angle = 0;
                Matrix mat = new Matrix();
                try {
                    File f1 = new File(strPthh);
                    ExifInterface exif = new ExifInterface(f1.getPath());
                    int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

                    f1 = null;

                    if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                        angle = 90;
                    } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                        angle = 180;
                    } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                        angle = 270;
                    }


                    if (angle > 0) {
                        mat.postRotate(angle);
                        //int temp=maxHeight;
                        //maxHeight=maxWidth;
                        //maxWidth=temp;

                        if (angle > 0) {
                            btt = Bitmap.createBitmap(inBtmp, 0, 0, inBtmp.getWidth(), inBtmp.getHeight(), mat, true);
                        }
                    }

                } catch (IOException e) {
                    Log.w("TAG", "-- Error in setting image");
                } catch (OutOfMemoryError oom) {
                    Log.w("TAG", "-- OOM Error in setting image");
                }
                return btt;
            }*/

    public static void doLogout(final Activity activity) {
        final Sharedprefrences mSharedPref = Sharedprefrences.getInstance(activity.getApplicationContext());
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setMessage(R.string.msg_logout);

        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {

                Intent Logout = new Intent(activity, New_Home_activity.class);
                Logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                activity.startActivity(Logout);
                mSharedPref.putString(activity.getString(R.string.key_emp_id), "");
                mSharedPref.putString(activity.getString(R.string.key_emp_name), "");
                activity.finish();

            }
        });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    public static void forceLogout(Activity activity) {
        Sharedprefrences mSharedPref = Sharedprefrences.getInstance(activity.getApplicationContext());
        mSharedPref.putString(activity.getString(R.string.key_emp_id), "");
        mSharedPref.putString(activity.getString(R.string.key_emp_name), "");

        Intent Logout = new Intent(activity, New_Home_activity.class);
        Logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(Logout);
        mSharedPref.clear();
        activity.finish();

    }

    public static void showAlertDialog(Context context, String error_msg, String title,
                                       DialogInterface.OnClickListener
                                               positiveListener, DialogInterface.OnClickListener negativeListener, boolean isCancelable) {
        errorDialog = new AlertDialog.Builder(context, R.style.AlertDialogCustomColor);
        errorDialog.setCancelable(isCancelable);
        errorDialog.setPositiveButton("Yes", positiveListener);
        errorDialog.setNegativeButton("No", negativeListener);
        errorDialog.setMessage(error_msg);
        errorDialog.setTitle(title);
        errorDialog.show();

    }

    public static int calculateInSampleSizeNew(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    public static void showToastMessage(Context context, String message, boolean isLong) {

        Toast.makeText(context, message, isLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();

    }

    public static void showToastMessageAtCenter(Context context, String message, boolean isLong) {

        Toast toast = Toast.makeText(context, message, isLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

    }

    /**
     * This method is used to check if the entered string is null, blank, or
     * "null"
     *
     * @param str set String to check
     * @return true if null else false
     */
    public boolean isEmptyOrNull(String str) {
        return !(!TextUtils.isEmpty(str) && !str.equals("null"));
    }


    /**
     * This method is used for checking internet connection if connection
     * available return true else return false
     */

    public static boolean checkInternetConnection(Context context) {
        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable();

    }

    /**
     * @param context   context for alert dialog
     * @param error_msg error message to displayed in this alert dialog
     * @param title     title for this alert dialog
     * @param listener  listener to be set if "OK" clicked
     */
    public static void showOkAlertDialog(Context context, String error_msg, String title,
                                         DialogInterface.OnClickListener listener) {

        try {
            AlertDialog.Builder errorDialog = new AlertDialog.Builder(context, R.style.AlertDialogCustomColor);
            errorDialog.setCancelable(false);
            errorDialog.setPositiveButton("OK", listener);
            errorDialog.setMessage(error_msg);
            errorDialog.setTitle(title);
            errorDialog.show();

        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    public static void hideKeyBoard(Activity activity) {
        InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * Global progress dialog show
     */
    public void showProgressDialog(Context context) {
        // done this to show app theme color for progress dialog in OS below LOLLIPOP
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            progressDialog = new ProgressDialog(context, R.style.AlertDialogCustomColor);
        else
            progressDialog = new ProgressDialog(context, R.style.AlertDialog_Holo);

        progressDialog.setMessage("Please wait ...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    /**
     * Global progress dialog dismiss
     */
    public void dismissProgressDialog() {
        if (progressDialog != null)
            if (progressDialog.isShowing())
                progressDialog.dismiss();
    }

    public String getMonth3Letter(int month) {
        String[] arrayMonths = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

        return arrayMonths[month];
    }

    /**
     * This method retruns true if file exists and has content in it, false otherwise
     *
     * @param context  context for the method
     * @param fileName name of the file to check for existence
     * @return
     */
    public boolean pdfFileExists(Context context, String fileName) {
        File pdfFile = new File(context.getFilesDir() + File.separator + Contants2.agora_folder + File.separator + fileName);
        if (pdfFile.exists() && pdfFile.length() > 0)
            return true;
        else
            return false;
    }

    /**
     * @param context  context for the method
     * @param fileName name of pdf/image/xlsc/text  file to be viewed
     * @throws ActivityNotFoundException throws exception if no application found to open the pdf file
     */
    public void viewPdfFile(Context context, String fileName) throws ActivityNotFoundException {

        File pdfFile = new File(context.getFilesDir() + File.separator + Contants2.agora_folder + File.separator + fileName).getAbsoluteFile();
        Intent intentShow = new Intent(Intent.ACTION_VIEW);

        intentShow.setDataAndType(FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + Contants2.IntelgainFileProvider, pdfFile), Contants2.ApplicationPDF);
        intentShow.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // temp permission Uri will be only accessible until existing activity is alive.
        intentShow.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Intent intentPdf = Intent.createChooser(intentShow, "Open File");
        intentPdf.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intentPdf);

    }

    public void viewAttachment(Context context, String fileName) {
        File pdfFile = new File(context.getFilesDir() + File.separator + Contants2.agora_folder + File.separator + fileName).getAbsoluteFile();
        Intent intentShow = new Intent(Intent.ACTION_VIEW);
        MimeTypeMap myMime = MimeTypeMap.getSingleton();
        String mimeType = myMime.getMimeTypeFromExtension(fileExt(fileName).substring(1));

        intentShow.setDataAndType(FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + Contants2.IntelgainFileProvider, pdfFile), mimeType);
        intentShow.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // temp permission Uri will be only accessible until existing activity is alive.
        intentShow.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Intent intentPdf = Intent.createChooser(intentShow, "Open File");
        intentPdf.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intentPdf);
    }

    /**
     * @param url
     * @return get file name extension
     */
    public static String fileExt(String url) {
        if (url.indexOf("?") > -1) {
            url = url.substring(0, url.indexOf("?"));
        }
        if (url.lastIndexOf(".") == -1) {
            return null;
        } else {
            String ext = url.substring(url.lastIndexOf(".") + 1);
            if (ext.indexOf("%") > -1) {
                ext = ext.substring(0, ext.indexOf("%"));
            }
            if (ext.indexOf("/") > -1) {
                ext = ext.substring(0, ext.indexOf("/"));
            }
            return ext.toLowerCase();
        }
    }

    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }


    /**
     * e
     * sets the start date and end date from server recived date respectilvely
     */
    public String setStartEndDates(String currentServerDate) {
        SimpleDateFormat dfSource = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat dfDest = new SimpleDateFormat("MM/dd/yyyy");
        String strReturnDate = "";
        try {
            Date sourceDate = dfSource.parse(currentServerDate);
            Date destDate = dfDest.parse(dfDest.format(sourceDate));

            strReturnDate += dfDest.format(destDate);

            Calendar c = Calendar.getInstance();
            c.setTime(destDate);
            c.set(Calendar.DATE, c.getActualMaximum(Calendar.DATE));

            Date lastDate = c.getTime();

            strReturnDate += "--" + dfDest.format(lastDate);

            return strReturnDate;
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }


    }


    /**
     * sets the prev month start date and end date which needs to be sent to server
     *
     * @param currentDate String date here used to get start date and end date of the month
     */
    public String setPrevMonthStartEndDate(String currentDate, TextView tvMonthTitle) {
        int month, year;
        String[] dateSplit;
        String prevDate;
        if (!currentDate.equals("")) {
            dateSplit = currentDate.split("/");
            month = Integer.parseInt(dateSplit[0]);
            year = Integer.parseInt(dateSplit[2]);

            if (month == 1) {
                month = 12;
                year = year - 1;
                // tvMonthTitle.setText(getMonth3Letter(month - 1) + " " + year);
            } else {
                month = month - 1;
                //  tvMonthTitle.setText(getMonth3Letter(month - 1) + " " + year);
            }

            String formattedMonth = month + "";
            formattedMonth = formattedMonth.length() > 1 ? formattedMonth : "0" + formattedMonth;
            prevDate = dateSplit[1] + "/" + formattedMonth + "/" + year;
            return setStartEndDates(prevDate);
        }
        return "";

    }


    /**
     * sets startdate and enddate to be sent to server one month ahead of present month
     */
    public String setNextMonthStartEndDate(String startDate, TextView tvMonthTitle) {
        int month, year;
        String[] dateSplit;
        String nextDate;
        if (!startDate.equals("")) {
            dateSplit = startDate.split("/");
            month = Integer.parseInt(dateSplit[0]);
            year = Integer.parseInt(dateSplit[2]);

            if (month == 12) {
                month = 1;
                year = year + 1;
                // tvMonthTitle.setText(getMonth3Letter(month - 1) + " " + year);
            } else {
                month = month + 1;
                // tvMonthTitle.setText(getMonth3Letter(month - 1) + " " + year);
            }

            String formattedMonth = month + "";
            formattedMonth = formattedMonth.length() > 1 ? formattedMonth : "0" + formattedMonth;
            nextDate = dateSplit[1] + "/" + formattedMonth + "/" + year;
            return setStartEndDates(nextDate);
        }
        return "";
    }

    public void wrtieFileOnInternalStorage(Context mcoContext, String sFileName, String
            sBody) {
        File file = new File(mcoContext.getFilesDir(), "AgoraDir");
        if (!file.exists()) {
            file.mkdir();
        }

        try {
            File gpxfile = new File(file, sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();

        } catch (Exception e) {

        }
    }


}
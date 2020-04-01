package com.intelegain.agora.fragmments

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.RectF
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.intelegain.agora.R
import com.intelegain.agora.activity.BasicCropActivity
import com.intelegain.agora.fragmments.BasicFragment
import com.intelegain.agora.fragmments.ProgressDialogFragment.Companion.instance
import com.isseiaoki.simplecropview.CropImageView
import com.isseiaoki.simplecropview.callback.CropCallback
import com.isseiaoki.simplecropview.callback.LoadCallback
import com.isseiaoki.simplecropview.callback.SaveCallback
import com.isseiaoki.simplecropview.util.Logger
import com.isseiaoki.simplecropview.util.Utils
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.OnShowRationale
import permissions.dispatcher.PermissionRequest
import permissions.dispatcher.RuntimePermissions
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@RuntimePermissions
class BasicFragment  // Note: only the system can call this constructor by reflection.
    : Fragment() {
    // Views ///////////////////////////////////////////////////////////////////////////////////////
    private var mCropView: CropImageView? = null
    private val mCompressFormat = Bitmap.CompressFormat.JPEG
    private var mFrameRect: RectF? = null
    private var mSourceUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_crop_basic, null, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // bind Views
        bindViews(view)
        mCropView!!.setDebug(false)
        if (savedInstanceState != null) { // restore data
            mFrameRect = savedInstanceState.getParcelable(KEY_FRAME_RECT)
            mSourceUri = savedInstanceState.getParcelable(KEY_SOURCE_URI)
        }
        if (mSourceUri == null && arguments != null) { // default data
            mSourceUri = Uri.parse(arguments!!.getString("URI"))
            //  mSourceUri = getUriFromDrawableResId(getContext(), R.drawable.banner_first);
            Log.e("aoki", "mSourceUri = $mSourceUri")
        }
        // load image
        mCropView!!.load(mSourceUri)
                .initialFrameRect(mFrameRect)
                .useThumbnail(true)
                .execute(mLoadCallback)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // save data
        outState.putParcelable(KEY_FRAME_RECT, mCropView!!.actualCropRect)
        outState.putParcelable(KEY_SOURCE_URI, mCropView!!.sourceUri)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, result: Intent?) {
        super.onActivityResult(requestCode, resultCode, result)
        if (resultCode == Activity.RESULT_OK) { // reset frame rect
            mFrameRect = null
            when (requestCode) {
                REQUEST_PICK_IMAGE -> {
                    mSourceUri = result!!.data
                    mCropView!!.load(mSourceUri)
                            .initialFrameRect(mFrameRect)
                            .useThumbnail(true)
                            .execute(mLoadCallback)
                }
                REQUEST_SAF_PICK_IMAGE -> {
                    mSourceUri = Utils.ensureUriPermission(context, result)
                    mCropView!!.load(mSourceUri)
                            .initialFrameRect(mFrameRect)
                            .useThumbnail(true)
                            .execute(mLoadCallback)
                }
            }
        }
    }

    /*
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // BasicFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }
*/
// Bind views //////////////////////////////////////////////////////////////////////////////////
    private fun bindViews(view: View) {
        mCropView = view.findViewById<View>(R.id.cropImageView) as CropImageView
        view.findViewById<View>(R.id.buttonDone).setOnClickListener(btnListener)
        /* view.findViewById(R.id.buttonFitImage).setOnClickListener(btnListener);
        view.findViewById(R.id.button1_1).setOnClickListener(btnListener);
        view.findViewById(R.id.button3_4).setOnClickListener(btnListener);
        view.findViewById(R.id.button4_3).setOnClickListener(btnListener);
        view.findViewById(R.id.button9_16).setOnClickListener(btnListener);
        view.findViewById(R.id.button16_9).setOnClickListener(btnListener);
        view.findViewById(R.id.buttonFree).setOnClickListener(btnListener);
        view.findViewById(R.id.buttonPickImage).setOnClickListener(btnListener);*/view.findViewById<View>(R.id.buttonRotateLeft).setOnClickListener(btnListener)
        view.findViewById<View>(R.id.buttonRotateRight).setOnClickListener(btnListener)
        /*view.findViewById(R.id.buttonCustom).setOnClickListener(btnListener);
        view.findViewById(R.id.buttonCircle).setOnClickListener(btnListener);
        view.findViewById(R.id.buttonShowCircleButCropAsSquare).setOnClickListener(btnListener);*/
    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    fun pickImage() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            startActivityForResult(Intent(Intent.ACTION_GET_CONTENT).setType("image/*"),
                    REQUEST_PICK_IMAGE)
        } else {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_SAF_PICK_IMAGE)
        }
    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun cropImage() {
        showProgress()
        mCropView!!.crop(mSourceUri).execute(mCropCallback)
    }

    @OnShowRationale(Manifest.permission.READ_EXTERNAL_STORAGE)
    fun showRationaleForPick(request: PermissionRequest) {
        showRationaleDialog(R.string.permission_pick_rationale, request)
    }

    @OnShowRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun showRationaleForCrop(request: PermissionRequest) {
        showRationaleDialog(R.string.permission_crop_rationale, request)
    }

    fun showProgress() {
        val f = instance
        fragmentManager!!.beginTransaction().add(f, PROGRESS_DIALOG).commitAllowingStateLoss()
    }

    fun dismissProgress() {
        if (!isResumed) return
        val manager = fragmentManager ?: return
        val f = manager.findFragmentByTag(PROGRESS_DIALOG) as ProgressDialogFragment?
        if (f != null) {
            fragmentManager!!.beginTransaction().remove(f).commitAllowingStateLoss()
        }
    }

    fun createSaveUri(): Uri? {
        return createNewUri(context, mCompressFormat)
    }

    private fun showRationaleDialog(@StringRes messageResId: Int, request: PermissionRequest) {
        AlertDialog.Builder(activity).setPositiveButton(R.string.button_allow
        ) { dialog, which -> request.proceed() }.setNegativeButton(R.string.button_deny) { dialog, which -> request.cancel() }.setCancelable(false).setMessage(messageResId).show()
    }

    // Handle button event /////////////////////////////////////////////////////////////////////////
    private val btnListener = View.OnClickListener { v ->
        when (v.id) {
            R.id.buttonDone -> BasicFragmentPermissionsDispatcher.cropImageWithPermissionCheck(this@BasicFragment)
            R.id.buttonRotateLeft -> mCropView!!.rotateImage(CropImageView.RotateDegrees.ROTATE_M90D)
            R.id.buttonRotateRight -> mCropView!!.rotateImage(CropImageView.RotateDegrees.ROTATE_90D)
        }
    }
    // Callbacks ///////////////////////////////////////////////////////////////////////////////////
    private val mLoadCallback: LoadCallback = object : LoadCallback {
        override fun onSuccess() {}
        override fun onError(e: Throwable) {}
    }
    private val mCropCallback: CropCallback = object : CropCallback {
        override fun onSuccess(cropped: Bitmap) {
            mCropView!!.save(cropped)
                    .compressFormat(mCompressFormat)
                    .execute(createSaveUri(), mSaveCallback)
        }

        override fun onError(e: Throwable) {}
    }
    private val mSaveCallback: SaveCallback = object : SaveCallback {
        override fun onSuccess(outputUri: Uri) {
            dismissProgress()
            (activity as BasicCropActivity?)!!.saveBitmap(outputUri)
        }

        override fun onError(e: Throwable) {
            dismissProgress()
        }
    }

    companion object {
        private val TAG = BasicFragment::class.java.simpleName
        private const val REQUEST_PICK_IMAGE = 10011
        private const val REQUEST_SAF_PICK_IMAGE = 10012
        private const val PROGRESS_DIALOG = "ProgressDialog"
        private const val KEY_FRAME_RECT = "FrameRect"
        private const val KEY_SOURCE_URI = "SourceUri"
        @JvmStatic
        fun newInstance(Uri: String?): BasicFragment {
            val fragment = BasicFragment()
            val args = Bundle()
            args.putString("URI", Uri)
            fragment.arguments = args
            return fragment
        }

        val dirPath: String
            get() {
                var dirPath = ""
                var imageDir: File? = null
                val extStorageDir = Environment.getExternalStorageDirectory()
                if (extStorageDir.canWrite()) {
                    imageDir = File(extStorageDir.path + "/simplecropview")
                }
                if (imageDir != null) {
                    if (!imageDir.exists()) {
                        imageDir.mkdirs()
                    }
                    if (imageDir.canWrite()) {
                        dirPath = imageDir.path
                    }
                }
                return dirPath
            }

        fun getUriFromDrawableResId(context: Context, drawableResId: Int): Uri {
            val builder = StringBuilder().append(ContentResolver.SCHEME_ANDROID_RESOURCE)
                    .append("://")
                    .append(context.resources.getResourcePackageName(drawableResId))
                    .append("/")
                    .append(context.resources.getResourceTypeName(drawableResId))
                    .append("/")
                    .append(context.resources.getResourceEntryName(drawableResId))
            return Uri.parse(builder.toString())
        }

        fun createNewUri(context: Context?, format: Bitmap.CompressFormat): Uri? {
            val currentTimeMillis = System.currentTimeMillis()
            val today = Date(currentTimeMillis)
            val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss")
            val title = dateFormat.format(today)
            val dirPath = dirPath
            val fileName = "scv" + title + "." + getMimeType(format)
            val path = "$dirPath/$fileName"
            val file = File(path)
            val values = ContentValues()
            values.put(MediaStore.Images.Media.TITLE, title)
            values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/" + getMimeType(format))
            values.put(MediaStore.Images.Media.DATA, path)
            val time = currentTimeMillis / 1000
            values.put(MediaStore.MediaColumns.DATE_ADDED, time)
            values.put(MediaStore.MediaColumns.DATE_MODIFIED, time)
            if (file.exists()) {
                values.put(MediaStore.Images.Media.SIZE, file.length())
            }
            val resolver = context!!.contentResolver
            val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            Logger.i("SaveUri = $uri")
            return uri
        }

        fun getMimeType(format: Bitmap.CompressFormat): String {
            Logger.i("getMimeType CompressFormat = $format")
            when (format) {
                Bitmap.CompressFormat.JPEG -> return "jpeg"
                Bitmap.CompressFormat.PNG -> return "png"
            }
            return "png"
        }

        fun createTempUri(context: Context): Uri {
            return Uri.fromFile(File(context.cacheDir, "cropped"))
        }
    }
}
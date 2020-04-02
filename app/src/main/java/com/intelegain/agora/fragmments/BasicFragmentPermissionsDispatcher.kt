package com.intelegain.agora.fragmments

import permissions.dispatcher.PermissionRequest
import permissions.dispatcher.PermissionUtils
import java.lang.ref.WeakReference

internal object BasicFragmentPermissionsDispatcher {
    private const val REQUEST_CROPIMAGE = 0
    private val PERMISSION_CROPIMAGE = arrayOf("android.permission.WRITE_EXTERNAL_STORAGE")
    private const val REQUEST_PICKIMAGE = 1
    private val PERMISSION_PICKIMAGE = arrayOf("android.permission.READ_EXTERNAL_STORAGE")
    fun pickImageWithPermissionCheck(target: BasicFragment) {
        if (PermissionUtils.hasSelfPermissions(target.activity, *PERMISSION_PICKIMAGE)) {
            target.pickImage()
        } else {
            if (PermissionUtils.shouldShowRequestPermissionRationale(target, *PERMISSION_PICKIMAGE)) {
                target.showRationaleForPick(BasicFragmentPickImagePermissionRequest(target))
            } else {
                target.requestPermissions(PERMISSION_PICKIMAGE, REQUEST_PICKIMAGE)
            }
        }
    }

    fun cropImageWithPermissionCheck(target: BasicFragment) {
        if (PermissionUtils.hasSelfPermissions(target.activity, *PERMISSION_CROPIMAGE)) {
            target.cropImage()
        } else {
            if (PermissionUtils.shouldShowRequestPermissionRationale(target, *PERMISSION_CROPIMAGE)) {
                target.showRationaleForCrop(BasicFragmentCropImagePermissionRequest(target))
            } else {
                target.requestPermissions(PERMISSION_CROPIMAGE, REQUEST_CROPIMAGE)
            }
        }
    }

    fun onRequestPermissionsResult(target: BasicFragment, requestCode: Int,
                                   grantResults: IntArray) {
        when (requestCode) {
            REQUEST_PICKIMAGE -> if (PermissionUtils.verifyPermissions(*grantResults)) {
                target.pickImage()
            }
            REQUEST_CROPIMAGE -> if (PermissionUtils.verifyPermissions(*grantResults)) {
                target.cropImage()
            }
            else -> {
            }
        }
    }

    private class BasicFragmentPickImagePermissionRequest(target: BasicFragment) : PermissionRequest {
        private val weakTarget: WeakReference<BasicFragment>
        override fun proceed() {
            val target = weakTarget.get() ?: return
            target.requestPermissions(PERMISSION_PICKIMAGE, REQUEST_PICKIMAGE)
        }

        override fun cancel() {}

        init {
            weakTarget = WeakReference(target)
        }
    }

    private class BasicFragmentCropImagePermissionRequest(target: BasicFragment) : PermissionRequest {
        private val weakTarget: WeakReference<BasicFragment>
        override fun proceed() {
            val target = weakTarget.get() ?: return
            target.requestPermissions(PERMISSION_CROPIMAGE, REQUEST_CROPIMAGE)
        }

        override fun cancel() {}

        init {
            weakTarget = WeakReference(target)
        }
    }
}



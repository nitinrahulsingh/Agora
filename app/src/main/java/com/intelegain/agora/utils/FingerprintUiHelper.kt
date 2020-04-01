/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */
package com.intelegain.agora.utils

import android.annotation.SuppressLint
import android.hardware.fingerprint.FingerprintManager
import android.os.Build
import android.os.CancellationSignal
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.intelegain.agora.R

/**
 * Small helper class to manage text/icon around fingerprint authentication UI.
 */
@RequiresApi(api = Build.VERSION_CODES.M)
class FingerprintUiHelper
/**
 * Constructor for [FingerprintUiHelper].
 */(private val mFingerprintManager: FingerprintManager,
    private val mIcon: ImageView, private val mErrorTextView: TextView, private val mCallback: Callback) : FingerprintManager.AuthenticationCallback() {
    private var mCancellationSignal: CancellationSignal? = null
    private var mSelfCancelled = false
    // The line below prevents the false positive inspection from Android Studio
// noinspection ResourceType
    val isFingerprintAuthAvailable: Boolean
        get() =// The line below prevents the false positive inspection from Android Studio
// noinspection ResourceType
            (mFingerprintManager.isHardwareDetected
                    && mFingerprintManager.hasEnrolledFingerprints())

    @SuppressLint("MissingPermission")
    fun startListening(cryptoObject: FingerprintManager.CryptoObject?) {
        if (!isFingerprintAuthAvailable) {
            return
        }
        mCancellationSignal = CancellationSignal()
        mSelfCancelled = false
        // The line below prevents the false positive inspection from Android Studio
// noinspection ResourceType
        mFingerprintManager
                .authenticate(cryptoObject, mCancellationSignal, 0 /* flags */, this, null)
        mIcon.setImageResource(R.drawable.ic_fp_40px)
    }

    fun stopListening() {
        if (mCancellationSignal != null) {
            mSelfCancelled = true
            mCancellationSignal!!.cancel()
            mCancellationSignal = null
        }
    }

    override fun onAuthenticationError(errMsgId: Int, errString: CharSequence) {
        if (!mSelfCancelled) {
            showError(errString)
            mIcon.postDelayed({ mCallback.onError() }, ERROR_TIMEOUT_MILLIS)
        }
    }

    override fun onAuthenticationHelp(helpMsgId: Int, helpString: CharSequence) {
        showError(helpString)
    }

    override fun onAuthenticationFailed() {
        showError(mIcon.resources.getString(
                R.string.fingerprint_not_recognized))
    }

    override fun onAuthenticationSucceeded(result: FingerprintManager.AuthenticationResult) {
        mErrorTextView.removeCallbacks(mResetErrorTextRunnable)
        mIcon.setImageResource(R.drawable.ic_fingerprint_success)
        mErrorTextView.setTextColor(
                mErrorTextView.resources.getColor(R.color.success_color, null))
        mErrorTextView.text = mErrorTextView.resources.getString(R.string.fingerprint_success)
        mIcon.postDelayed({ mCallback.onAuthenticated() }, SUCCESS_DELAY_MILLIS)
    }

    private fun showError(error: CharSequence) {
        mIcon.setImageResource(R.drawable.ic_fingerprint_error)
        mErrorTextView.text = error
        mErrorTextView.setTextColor(
                mErrorTextView.resources.getColor(R.color.warning_color, null))
        mErrorTextView.removeCallbacks(mResetErrorTextRunnable)
        mErrorTextView.postDelayed(mResetErrorTextRunnable, ERROR_TIMEOUT_MILLIS)
    }

    private val mResetErrorTextRunnable = Runnable {
        mErrorTextView.setTextColor(
                mErrorTextView.resources.getColor(R.color.hint_color, null))
        mErrorTextView.text = mErrorTextView.resources.getString(R.string.fingerprint_hint)
        mIcon.setImageResource(R.drawable.ic_fp_40px)
    }

    interface Callback {
        fun onAuthenticated()
        fun onError()
    }

    companion object {
        private const val ERROR_TIMEOUT_MILLIS: Long = 1600
        private const val SUCCESS_DELAY_MILLIS: Long = 1300
    }

}
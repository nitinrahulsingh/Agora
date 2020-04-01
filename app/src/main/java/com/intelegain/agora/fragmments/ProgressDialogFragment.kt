package com.intelegain.agora.fragmments

import android.app.Dialog
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.ProgressBar
import androidx.fragment.app.DialogFragment
import com.intelegain.agora.R

class ProgressDialogFragment  // Note: only the system can call this constructor by reflection.
    : DialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_progress_dialog, null, false)
        val progressBar = view.findViewById<ProgressBar>(R.id.progress)
        progressBar.indeterminateDrawable
                .setColorFilter(context!!.resources.getColor(R.color.colorAccent),
                        PorterDuff.Mode.SRC_IN)
        return view
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setOnKeyListener { dialog, keyCode, event ->
            // Disable Back key and Search key
            when (keyCode) {
                KeyEvent.KEYCODE_BACK, KeyEvent.KEYCODE_SEARCH -> true
                else -> false
            }
        }
        return dialog
    }

    companion object {
        val TAG = ProgressDialogFragment::class.java.simpleName
        @JvmStatic
        val instance: ProgressDialogFragment
            get() {
                val fragment = ProgressDialogFragment()
                val args = Bundle()
                fragment.arguments = args
                return fragment
            }
    }
}
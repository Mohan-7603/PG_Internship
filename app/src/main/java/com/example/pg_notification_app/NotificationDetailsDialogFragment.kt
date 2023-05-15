package com.example.pg_notification_app

import android.R
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
class NotificationDetailsDialogFragment : DialogFragment() {

    companion object {
        const val TAG = "NotificationDetailsDialogFragment"
        private const val ARG_TITLE = "title"
        private const val ARG_MESSAGE = "message"

        fun newInstance(title: String, message: String): NotificationDetailsDialogFragment {
            val fragment = NotificationDetailsDialogFragment()
            val args = Bundle()
            args.putString(ARG_TITLE, title)
            args.putString(ARG_MESSAGE, message)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val title = requireArguments().getString(ARG_TITLE)
        val message = requireArguments().getString(ARG_MESSAGE)

        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK") { _, _ ->
                    // do something when OK button is clicked
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}

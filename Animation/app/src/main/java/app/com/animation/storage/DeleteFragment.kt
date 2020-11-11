package app.com.animation.storage

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import app.com.animation.R
import java.io.File

class DeleteFragment(private val file: File, private val update : () -> Unit) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(context).apply {
            setMessage(resources.getString(R.string.delete_message))
            setPositiveButton("Yes") { dialog, which ->
                run {
                    file.delete()
                    dialog.cancel()
                    update()
                }
            }
            setNegativeButton("No") { dialog, which ->
                dialog.cancel()
            }
        }
        return builder.create()
    }
}
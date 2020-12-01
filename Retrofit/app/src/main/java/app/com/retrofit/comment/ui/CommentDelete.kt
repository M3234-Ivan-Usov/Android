package app.com.retrofit.comment.ui

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import app.com.retrofit.R
import app.com.retrofit.RetrofitApp
import app.com.retrofit.post.netApi.DeleteCallback

class CommentDelete(private val comment: Comment) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(context).apply {
            setMessage(resources.getString(R.string.delete_message))
            setPositiveButton("Yes") { dialog, which ->
                val call = RetrofitApp.instance.commentRequest.delete(comment.id)
                call.enqueue(DeleteCallback())
                dialog.cancel()
            }
            setNegativeButton("No") { dialog, which ->
                dialog.cancel()
            }
        }
        return builder.create()
    }
}
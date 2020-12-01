package app.com.retrofit.post.ui

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import app.com.retrofit.R
import app.com.retrofit.RetrofitApp
import app.com.retrofit.post.dbApi.DatabaseAccess
import app.com.retrofit.post.dbApi.DatabaseModify
import app.com.retrofit.post.netApi.DeleteCallback
import java.lang.ref.WeakReference

class DatabaseDelete(private val postList: WeakReference<PostList>) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(context).apply {
            setMessage(resources.getString(R.string.delete_database))
            setPositiveButton("Yes") { dialog, which ->
                DatabaseAccess(postList, DatabaseAccess.DELETE_ALL).execute()
                postList.get()?.content?.clear()
                postList.get()?.contentRecycler?.adapter?.notifyDataSetChanged()
                dialog.cancel()
            }
            setNegativeButton("No") { dialog, which ->
                dialog.cancel()
            }
        }
        return builder.create()
    }
}
package app.com.retrofit.post.ui

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import app.com.retrofit.MainActivity
import app.com.retrofit.R
import app.com.retrofit.RetrofitApp
import app.com.retrofit.post.dbApi.DatabaseModify
import app.com.retrofit.post.netApi.DeleteCallback
import java.lang.ref.WeakReference


class PostDelete(
    private val postList: WeakReference<PostList>,
    private val post: Post
) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(context).apply {
            setMessage(resources.getString(R.string.delete_message))
            setPositiveButton("Yes") { dialog, which ->
                val call = RetrofitApp.instance.postRequest.delete(post.id)
                call.enqueue(DeleteCallback())
                DatabaseModify(DatabaseModify.DELETE).execute(post)
                postList.get()?.content?.remove(post)
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
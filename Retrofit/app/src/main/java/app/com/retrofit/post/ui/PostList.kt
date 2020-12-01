package app.com.retrofit.post.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import app.com.retrofit.MainActivity
import app.com.retrofit.R
import app.com.retrofit.RetrofitApp
import app.com.retrofit.post.dbApi.DatabaseAccess
import app.com.retrofit.post.netApi.GetCallback
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.lang.ref.WeakReference

class PostList : Fragment() {
    private lateinit var shimmer: ShimmerFrameLayout
    lateinit var refresh: FloatingActionButton
    lateinit var put: FloatingActionButton
    lateinit var remove: FloatingActionButton
    lateinit var contentView: View
    lateinit var contentRecycler: RecyclerView
    var content = ArrayList<Post>()

    private fun initialiseLayout() {
        contentRecycler = contentView.findViewById(R.id.content_recycler)
        contentRecycler.adapter = PostAdapter(WeakReference(this))
        shimmer = contentView.findViewById(R.id.shimmer)
        put = contentView.findViewById(R.id.add_content)
        put.setOnClickListener {
            switchFloatingButtons(false)
            activity?.supportFragmentManager?.beginTransaction()?.add(
                R.id.fragment_container,
                PostPut(WeakReference(this))
            )?.commit()
        }

        refresh = contentView.findViewById(R.id.refresh_content)
        refresh.setOnClickListener {
            switchView(true)
            val call = RetrofitApp.instance.postRequest.download()
            call.enqueue(GetCallback(WeakReference(this)))
        }
        remove = contentView.findViewById(R.id.remove_content)
        remove.setOnClickListener {
            val fm = MainActivity.instance.supportFragmentManager
            DatabaseDelete(WeakReference(this)).show(fm, "dialog")
        }
        switchView(false)
    }

    fun switchFloatingButtons(toShow: Boolean) {
        if (toShow) {
            put.visibility = View.VISIBLE
            refresh.visibility = View.VISIBLE
            remove.visibility = View.VISIBLE
        } else {
            put.visibility = View.INVISIBLE
            refresh.visibility = View.INVISIBLE
            remove.visibility = View.INVISIBLE
        }
    }

    fun switchView(toDownload: Boolean) {
        if (toDownload) {
            shimmer.visibility = View.VISIBLE
            shimmer.startShimmer()
            contentRecycler.visibility = View.INVISIBLE
            switchFloatingButtons(false)
        } else {
            contentRecycler.visibility = View.VISIBLE
            switchFloatingButtons(true)
            shimmer.stopShimmer()
            shimmer.visibility = View.INVISIBLE
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        contentView = inflater.inflate(R.layout.content_list_layout, container, false)
        initialiseLayout()
        DatabaseAccess(WeakReference(this), DatabaseAccess.READ_ALL).execute()
        return contentView
    }
}

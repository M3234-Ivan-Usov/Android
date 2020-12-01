package app.com.retrofit.comment.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import app.com.retrofit.MainActivity
import app.com.retrofit.R
import app.com.retrofit.RetrofitApp
import app.com.retrofit.comment.netApi.GetCallback
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import java.lang.ref.WeakReference

class CommentList : Fragment() {

    private lateinit var call: Call<List<Comment>>
    private lateinit var shimmer: ShimmerFrameLayout
    private lateinit var floatButton: FloatingActionButton
    lateinit var contentView: View
    var content: List<Comment>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        call = RetrofitApp.instance.commentRequest.download()
    }

    fun configureView() {
        contentView.findViewById<RecyclerView>(R.id.content_recycler)
            ?.adapter = CommentAdapter(content!!)
        contentView.findViewById<FloatingActionButton>(R.id.add_content)
            .setOnClickListener {

            }
        shimmer.stopShimmer()
        shimmer.visibility = View.GONE
        contentView.visibility = View.VISIBLE
        floatButton = contentView.findViewById(R.id.add_content)
        floatButton.visibility = View.VISIBLE
        floatButton.setOnClickListener {
            floatButton.visibility = View.GONE
            MainActivity.instance.supportFragmentManager.beginTransaction().add(
                R.id.fragment_container,
                CommentPut(floatButton)
            ).commit()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        contentView = inflater.inflate(R.layout.content_list_layout, container, false)
        contentView.findViewById<FloatingActionButton>(R.id.refresh_content).visibility = View.GONE
        contentView.findViewById<FloatingActionButton>(R.id.remove_content).visibility = View.GONE
        shimmer =
            contentView.findViewById<ShimmerFrameLayout>(R.id.shimmer).apply { startShimmer() }
        shimmer.visibility = View.VISIBLE
        shimmer.startShimmer()
        call.enqueue(GetCallback(WeakReference(this)))
        return contentView
    }
}

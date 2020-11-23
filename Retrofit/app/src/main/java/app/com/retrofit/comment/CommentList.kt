package app.com.retrofit.comment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import app.com.retrofit.MainActivity
import app.com.retrofit.R
import app.com.retrofit.post.PostPut
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommentList : Fragment() {

    private lateinit var call: Call<List<Comment>>
    private lateinit var shimmer: ShimmerFrameLayout
    private lateinit var floatButton: FloatingActionButton
    lateinit var contentView: View
    var content: List<Comment>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        call = MainActivity.instance.commentRequest.download()
    }

    fun configureView() {
        contentView.findViewById<RecyclerView>(R.id.content_recycler)
            ?.adapter = CommentAdapter(content!!)
        contentView.findViewById<FloatingActionButton>(R.id.put_button)
            .setOnClickListener {

            }
        shimmer.stopShimmer()
        shimmer.visibility = View.GONE
        contentView.visibility = View.VISIBLE
        floatButton = contentView.findViewById(R.id.put_button)
        floatButton.visibility = View.VISIBLE
        floatButton.setOnClickListener {
            floatButton.visibility = View.GONE
            MainActivity.instance.supportFragmentManager.beginTransaction().add(
                R.id.fragment_container, CommentPut(floatButton)
            ).commit()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        contentView = inflater.inflate(R.layout.content_list_layout, container, false)
        shimmer =
            contentView.findViewById<ShimmerFrameLayout>(R.id.shimmer).apply { startShimmer() }
        call.enqueue(object : Callback<List<Comment>> {
            override fun onFailure(call: Call<List<Comment>>, t: Throwable) {
                Snackbar.make(
                    MainActivity.instance.findViewById(R.id.main_view),
                    "Failed to download: " + t.message,
                    Snackbar.LENGTH_INDEFINITE
                ).setAction("Retry") {
                    MainActivity.instance.recreate()
                }.show()
            }

            override fun onResponse(call: Call<List<Comment>>, response: Response<List<Comment>>) {
                if (response.isSuccessful) {
                    content = response.body()
                    configureView()
                }
            }
        })
        return contentView
    }
}

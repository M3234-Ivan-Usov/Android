package app.com.retrofit.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import app.com.retrofit.MainActivity
import app.com.retrofit.R
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostList : Fragment() {

    private lateinit var call: Call<List<Post>>
    private lateinit var shimmer: ShimmerFrameLayout
    lateinit var floatButton: FloatingActionButton
    lateinit var contentView: View
    var content: List<Post>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        call = MainActivity.instance.postRequest.download()
    }

    fun configureView() {
        contentView.findViewById<RecyclerView>(R.id.content_recycler)
            ?.adapter = PostAdapter(content!!)
        shimmer.stopShimmer()
        shimmer.visibility = View.GONE
        contentView.visibility = View.VISIBLE
        floatButton = contentView.findViewById(R.id.put_button)
        floatButton.visibility = View.VISIBLE
        floatButton.setOnClickListener {
            floatButton.visibility = View.GONE
            MainActivity.instance.supportFragmentManager.beginTransaction().add(
                R.id.fragment_container, PostPut(floatButton)
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
        call.enqueue(object : Callback<List<Post>> {
            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                Snackbar.make(
                    MainActivity.instance.findViewById(R.id.main_view),
                    "Failed to download: " + t.message,
                    Snackbar.LENGTH_INDEFINITE
                ).setAction("Retry") {
                    MainActivity.instance.recreate()
                }.show()
            }

            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                if (response.isSuccessful) {
                    content = response.body()
                    configureView()
                }
            }
        })
        return contentView
    }
}

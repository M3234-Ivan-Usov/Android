package app.com.retrofit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import app.com.retrofit.comment.CommentRequest
import app.com.retrofit.post.PostRequest
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MainActivity : AppCompatActivity() {
    companion object {
        private const val BASE_URL = "https://jsonplaceholder.typicode.com"
        const val userId = 404
        lateinit var instance: MainActivity
    }

    lateinit var retrofit: Retrofit
    lateinit var postRequest: PostRequest
    lateinit var commentRequest: CommentRequest

    private lateinit var tabs: TabLayout
    private lateinit var tabPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        instance = this
        retrofit = Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create()).build()
        postRequest = retrofit.create(PostRequest::class.java)
        commentRequest = retrofit.create(CommentRequest::class.java)

        tabs = findViewById(R.id.tab_layout)
        tabPager = findViewById(R.id.tab_pager)
        tabPager.adapter = TabPagerAdapter()
        TabLayoutMediator(tabs, tabPager) { tab, position ->
            tab.text = TabPagerAdapter.titles[position]
            tabPager.setCurrentItem(tab.position, true)
        }.attach()
    }
}
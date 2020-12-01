package app.com.retrofit

import android.app.Application
import androidx.room.Room
import app.com.retrofit.comment.netApi.CommentRequest
import app.com.retrofit.post.dbApi.PostDatabase
import app.com.retrofit.post.netApi.PostRequest
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File

class RetrofitApp : Application() {
    companion object {
        private const val BASE_URL = "https://jsonplaceholder.typicode.com"
        private const val DATABASE_NAME = "posts.db"
        private const val TEMP_NAME = "posts_temp.db"
        lateinit var instance : RetrofitApp
    }

    lateinit var postDatabase: PostDatabase
    lateinit var retrofit: Retrofit
    lateinit var postRequest: PostRequest
    lateinit var commentRequest: CommentRequest


    override fun onCreate() {
        super.onCreate()
        instance = this

        retrofit = Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create()).build()
        postRequest = retrofit.create(PostRequest::class.java)
        commentRequest = retrofit.create(CommentRequest::class.java)

        val databaseLocation = File(filesDir, DATABASE_NAME)
        postDatabase = if (databaseLocation.exists()) {
            Room.databaseBuilder(this,
                PostDatabase::class.java, DATABASE_NAME)
                .createFromFile(databaseLocation).allowMainThreadQueries().build()
        } else {
            Room.databaseBuilder(this,
                PostDatabase::class.java, databaseLocation.absolutePath)
                .allowMainThreadQueries().build()
        }
    }

}
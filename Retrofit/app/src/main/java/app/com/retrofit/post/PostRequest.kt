package app.com.retrofit.post

import retrofit2.Call
import retrofit2.http.*

interface PostRequest {
    @GET("/posts")
    fun download(): Call<List<Post>>

    @DELETE("/posts/{id}")
    fun delete(
        @Path("id") id: Int,
        @Query("userId") userId: Int
    ): Call<Post>

    @POST("/posts")
    fun upload(@Body newObject: Post): Call<Post>
}
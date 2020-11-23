package app.com.retrofit.comment

import retrofit2.Call
import retrofit2.http.*

interface CommentRequest {
    @GET("/comments")
    fun download(): Call<List<Comment>>

    @DELETE("/comments/{number}")
    fun delete(
        @Path("number") number: Int,
        @Query("postId") postId: Int
    ): Call<Comment>

    @POST("/comments")
    fun upload(@Body newObject: Comment): Call<Comment>
}
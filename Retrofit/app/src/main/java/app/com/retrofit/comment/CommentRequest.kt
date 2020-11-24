package app.com.retrofit.comment

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface CommentRequest {
    @GET("/comments")
    fun download(): Call<List<Comment>>

    @DELETE("/comments/{number}")
    fun delete(
        @Path("number") number: Int
    ): Call<ResponseBody>

    @POST("/comments")
    fun upload(@Body newObject: Comment): Call<Comment>
}
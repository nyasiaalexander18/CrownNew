package games.for_fun.crownnew.activities.play_web

import retrofit2.Response
import retrofit2.http.GET

interface RetrofitGetDataApi {
    @GET("a74b25c97bd3bad3d4a071e071d4b17b/raw/crown_game")
    suspend fun getInfo(): Response<Info>
}

data class Info(
    val can: Boolean?,
    val url: String?
)
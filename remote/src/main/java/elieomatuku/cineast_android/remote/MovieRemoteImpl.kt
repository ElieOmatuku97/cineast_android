package elieomatuku.cineast_android.remote

import elieomatuku.cineast_android.remote.api.MovieApi
import elieomatuku.cineast_android.remote.model.RemoteException
import elieomatuku.cineast_android.remote.model.RemoteGenres
import elieomatuku.cineast_android.remote.model.RemoteImages
import elieomatuku.cineast_android.remote.model.RemoteMovie
import elieomatuku.cineast_android.remote.model.RemoteMovieCredits
import elieomatuku.cineast_android.remote.model.RemoteMovieFacts
import elieomatuku.cineast_android.remote.model.RemoteMovies
import elieomatuku.cineast_android.remote.model.RemotePostResult
import elieomatuku.cineast_android.remote.model.RemoteTrailers
import elieomatuku.cineast_android.remote.request.FavouritesMediaRequest
import elieomatuku.cineast_android.remote.request.WatchListMediaRequest
import okhttp3.RequestBody

/**
 * Created by elieomatuku on 2021-07-04
 */

class MovieRemoteImpl(private val movieApi: MovieApi) {

    suspend fun getPopularMovies(): RemoteMovies {
        val response = movieApi.getPopularMovies()
        if (response.isSuccessful) {
            val body = response.body()
            return body!!
        } else {
            throw RemoteException(
                response.code(),
                response.errorBody()?.toString(),
                response.message()
            )
        }
    }

    suspend fun getUpcomingMovies(): RemoteMovies {
        val response = movieApi.getUpcomingMovies()
        if (response.isSuccessful) {
            val body = response.body()
            return body!!
        } else {
            throw RemoteException(
                response.code(),
                response.errorBody()?.toString(),
                response.message()
            )
        }
    }

    suspend fun getNowPlayingMovies(): RemoteMovies {
        val response = movieApi.getNowPlayingMovies()
        if (response.isSuccessful) {
            val body = response.body()
            return body!!
        } else {
            throw RemoteException(
                response.code(),
                response.errorBody()?.toString(),
                response.message()
            )
        }
    }

    suspend fun getTopRatedMovies(): RemoteMovies {
        val response = movieApi.getTopRatedMovies()
        if (response.isSuccessful) {
            val body = response.body()
            return body!!
        } else {
            throw RemoteException(
                response.code(),
                response.errorBody()?.toString(),
                response.message()
            )
        }
    }

    suspend fun getGenres(): RemoteGenres {
        val response = movieApi.getGenre()
        if (response.isSuccessful) {
            val body = response.body()
            return body!!
        } else {
            throw RemoteException(
                response.code(),
                response.errorBody()?.toString(),
                response.message()
            )
        }
    }

    suspend fun getMovieVideos(movie: RemoteMovie): RemoteTrailers {
        val response = movieApi.getMovieVideos(movie.id)
        if (response.isSuccessful) {
            val body = response.body()
            return body!!
        } else {
            throw RemoteException(
                response.code(),
                response.errorBody()?.toString(),
                response.message()
            )
        }
    }

    suspend fun getMovieFacts(movie: RemoteMovie): RemoteMovieFacts {
        val response = movieApi.getMovieDetails(movie.id)
        if (response.isSuccessful) {
            val body = response.body()
            return body!!
        } else {
            throw RemoteException(
                response.code(),
                response.errorBody()?.toString(),
                response.message()
            )
        }
    }

    suspend fun getMovieCredits(movie: RemoteMovie): RemoteMovieCredits {
        val response = movieApi.getCredits(movie.id)
        if (response.isSuccessful) {
            val body = response.body()
            return body!!
        } else {
            throw RemoteException(
                response.code(),
                response.errorBody()?.toString(),
                response.message()
            )
        }
    }

    suspend fun getSimilarMovie(movie: RemoteMovie): RemoteMovies {
        val response = movieApi.getSimilarMovies(movie.id)
        if (response.isSuccessful) {
            val body = response.body()
            return body!!
        } else {
            throw RemoteException(
                response.code(),
                response.errorBody()?.toString(),
                response.message()
            )
        }
    }

    suspend fun getMovieImages(movieId: Int): RemoteImages {
        val response = movieApi.getMovieImages(movieId)
        if (response.isSuccessful) {
            val body = response.body()
            return body!!
        } else {
            throw RemoteException(
                response.code(),
                response.errorBody()?.toString(),
                response.message()
            )
        }
    }

    suspend fun getMovie(movieId: Int): RemoteMovie {
        val response = movieApi.getMovie(movieId)
        if (response.isSuccessful) {
            val body = response.body()
            return body!!
        } else {
            throw RemoteException(
                response.code(),
                response.errorBody()?.toString(),
                response.message()
            )
        }
    }

    suspend fun searchMovies(argQuery: String): RemoteMovies {
        val response = movieApi.getMoviesWithSearch(argQuery)
        if (response.isSuccessful) {
            val body = response.body()
            return body!!
        } else {
            throw RemoteException(
                response.code(),
                response.errorBody()?.toString(),
                response.message()
            )
        }
    }

    suspend fun updateWatchList(
        sessionId: String,
        mediaRequest: WatchListMediaRequest
    ): RemotePostResult {
        val response = movieApi.updateWatchList(sessionId, mediaRequest)
        if (response.isSuccessful) {
            val body = response.body()
            return body!!
        } else {
            throw RemoteException(
                response.code(),
                response.errorBody()?.toString(),
                response.message()
            )
        }
    }

    suspend fun getFavoriteList(sessionId: String): RemoteMovies {
        val response = movieApi.getFavoritesList(sessionId)
        if (response.isSuccessful) {
            val body = response.body()
            return body!!
        } else {
            throw RemoteException(
                response.code(),
                response.errorBody()?.toString(),
                response.message()
            )
        }
    }

    suspend fun updateFavoriteList(
        sessionId: String,
        mediaRequest: FavouritesMediaRequest
    ): RemotePostResult {
        val response = movieApi.updateFavorites(sessionId, mediaRequest)
        if (response.isSuccessful) {
            val body = response.body()
            return body!!
        } else {
            throw RemoteException(
                response.code(),
                response.errorBody()?.toString(),
                response.message()
            )
        }
    }

    suspend fun postMovieRate(
        movie: RemoteMovie,
        sessionId: String,
        requestBody: RequestBody
    ): RemotePostResult {
        val response = movieApi.postMovieRate(movie.id, sessionId, requestBody)
        if (response.isSuccessful) {
            val body = response.body()
            return body!!
        } else {
            throw RemoteException(
                response.code(),
                response.errorBody()?.toString(),
                response.message()
            )
        }
    }

    suspend fun getUserRatedMovies(sessionId: String): RemoteMovies {
        val response = movieApi.getUserRatedMovies(sessionId)
        if (response.isSuccessful) {
            val body = response.body()
            return body!!
        } else {
            throw RemoteException(
                response.code(),
                response.errorBody()?.toString(),
                response.message()
            )
        }
    }
}

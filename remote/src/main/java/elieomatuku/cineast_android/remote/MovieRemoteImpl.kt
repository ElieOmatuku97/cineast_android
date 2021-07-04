package elieomatuku.cineast_android.remote

import elieomatuku.cineast_android.data.repository.movie.MovieRemote
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

class MovieRemoteImpl(private val movieApi: MovieApi) : MovieRemote {

    override suspend fun getPopularMovies(): RemoteMovies {
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

    override suspend fun getUpcomingMovies(): RemoteMovies {
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

    override suspend fun getNowPlayingMovies(): RemoteMovies {
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

    override suspend fun getTopRatedMovies(): RemoteMovies {
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

    override suspend fun getGenres(): RemoteGenres {
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

    override suspend fun getMovieVideos(movieId: Int): RemoteTrailers {
        val response = movieApi.getMovieVideos(movieId)
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

    override suspend fun getMovieFacts(movieId: Int): RemoteMovieFacts {
        val response = movieApi.getMovieDetails(movieId)
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

    override suspend fun getMovieCredits(movieId: Int): RemoteMovieCredits {
        val response = movieApi.getCredits(movieId)
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

    override suspend fun getSimilarMovie(movieId: Int): RemoteMovies {
        val response = movieApi.getSimilarMovies(movieId)
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

    override suspend fun getMovieImages(movieId: Int): RemoteImages {
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

    override suspend fun getMovie(movieId: Int): RemoteMovie {
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

    override suspend fun searchMovies(query: String): RemoteMovies {
        val response = movieApi.getMoviesWithSearch(query)
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

    override suspend fun updateWatchList(
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

    override suspend fun getFavoriteList(sessionId: String): RemoteMovies {
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

    override suspend fun updateFavoriteList(
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

    override suspend fun postMovieRate(
        movieId: Int,
        sessionId: String,
        requestBody: RequestBody
    ): RemotePostResult {
        val response = movieApi.postMovieRate(movieId, sessionId, requestBody)
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

    override suspend fun getUserRatedMovies(sessionId: String): RemoteMovies {
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

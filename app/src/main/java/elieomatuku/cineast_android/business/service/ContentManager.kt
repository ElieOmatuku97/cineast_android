package elieomatuku.cineast_android.business.service


import elieomatuku.cineast_android.business.callback.AsyncResponse
import elieomatuku.cineast_android.business.client.TmdbContentClient
import elieomatuku.cineast_android.model.data.Movie
import elieomatuku.cineast_android.model.data.MovieDetails
import elieomatuku.cineast_android.model.data.PeopleDetails
import elieomatuku.cineast_android.model.data.Person
import elieomatuku.cineast_android.business.api.response.*
import elieomatuku.cineast_android.utils.RestUtils


class ContentManager(private val tmdbContentClient: TmdbContentClient) {
    companion object {
        val API_KEY = RestUtils.API_KEY
    }

    fun getPopularMovies(asyncResponse: AsyncResponse<MovieResponse>) {
        tmdbContentClient.getPopularMovies(asyncResponse)
    }


    fun getUpcomingMovies(asyncResponse: AsyncResponse<MovieResponse>) {
        tmdbContentClient.getUpcomingMovies(asyncResponse)
    }


    fun getNowPlayingMovies(asyncResponse: AsyncResponse<MovieResponse>) {
        tmdbContentClient.getNowPlayingMovies(asyncResponse)
    }

    fun getTopRatedMovies(asyncResponse: AsyncResponse<MovieResponse>) {
        tmdbContentClient.getTopRatedMovies(asyncResponse)
    }


    fun getPopularPeople(asyncResponse: AsyncResponse<PeopleResponse>) {
        tmdbContentClient.getPopularPeople(asyncResponse)
    }

    fun getGenres(asyncResponse: AsyncResponse<GenreResponse>) {
        tmdbContentClient.getGenres(asyncResponse)
    }

    fun getMovieVideos(movie: Movie, asyncResponse: AsyncResponse<TrailerResponse>) {
        tmdbContentClient.getMovieVideos(movie, asyncResponse)
    }

    fun getMovieDetails(movie: Movie, asyncResponse: AsyncResponse<MovieDetails>) {
        tmdbContentClient.getMovieDetails(movie, asyncResponse)
    }


    fun getMovieCredits(movie: Movie, asyncResponse: AsyncResponse<MovieCreditsResponse>) {
        tmdbContentClient.getMovieCredits(movie, asyncResponse)
    }

    fun getSimilarMovie(movie: Movie, asyncResponse: AsyncResponse<MovieResponse>) {
        tmdbContentClient.getSimilarMovie(movie, asyncResponse)
    }

    fun getMovieImages(movieId: Int, asyncResponse: AsyncResponse<ImageResponse>) {
        tmdbContentClient.getMovieImages(movieId, asyncResponse)
    }

    fun getPeopleMovies(person: Person, asyncResponse: AsyncResponse<PeopleCreditsResponse>) {
        tmdbContentClient.getPeopleMovies(person, asyncResponse)
    }

    fun getPeopleDetails(person: Person, asyncResponse: AsyncResponse<PeopleDetails>) {
        tmdbContentClient.getPeopleDetails(person, asyncResponse)
    }

    fun getPeopleImages(personId: Int, asyncResponse: AsyncResponse<ImageResponse>) {
        tmdbContentClient.getPeopleImages(personId, asyncResponse)
    }


    fun getMovie(movieId: Int, asyncResponse: AsyncResponse<Movie>) {
        tmdbContentClient.getMovie(movieId, asyncResponse)
    }

    fun searchMovies(argQuery: String, asyncResponse: AsyncResponse<MovieResponse>) {
        tmdbContentClient.searchMovies(argQuery, asyncResponse)
    }

    fun searchPeople(argQuery: String, asyncResponse: AsyncResponse<PeopleResponse>) {
        tmdbContentClient.searchPeople(argQuery, asyncResponse)
    }
}
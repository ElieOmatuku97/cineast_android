package elieomatuku.cineast_android.database.repository


import elieomatuku.cineast_android.database.ContentDatabase
import elieomatuku.cineast_android.database.entity.MovieEntity
import elieomatuku.cineast_android.database.entity.MovieType
import elieomatuku.cineast_android.database.entity.MovieTypeJoin
import elieomatuku.cineast_android.database.entity.PersonalityEntity
import elieomatuku.cineast_android.model.DiscoverContent
import elieomatuku.cineast_android.model.data.Movie
import elieomatuku.cineast_android.model.data.Personality
import io.reactivex.Flowable
import io.reactivex.functions.Function5



/**
 * Created by elieomatuku on 2019-12-07
 */


class ContentRepository(private val contentDatabase: ContentDatabase) {


    /**
     * Get the DiscoverContent.

     * @return a [Flowable] that will emit every time the db has been updated.
     */


    fun discoverContent(): Flowable<DiscoverContent> {
        return Flowable.zip(popularMovies, nowPlayingMovies, upcomingMovies, topRatedMovies, popularPeople,
                Function5<List<Movie>, List<Movie>, List<Movie>, List<Movie>, List<Personality>, DiscoverContent>
                { popularMovies, nowPlayingMovies, upcomingMovies, topRatedMovies, popularPeople ->
                    DiscoverContent(
                            popularMovies = popularMovies,
                            upcomingMovies = upcomingMovies,
                            nowPlayingMovies = nowPlayingMovies,
                            topRatedMovies = topRatedMovies,
                            popularPeople = popularPeople)
                })

    }

    val popularMovies: Flowable<List<Movie>>
        get() {
            return contentDatabase.movieTypeJoinDao().getMoviesForType(MovieType.POPULAR.id).map { MovieEntity.toMovies(it) }
        }


    val upcomingMovies: Flowable<List<Movie>>
        get() {
            return contentDatabase.movieTypeJoinDao().getMoviesForType(MovieType.UPCOMING.id)
                    .map { MovieEntity.toMovies(it) }
        }

    val nowPlayingMovies: Flowable<List<Movie>>
        get() {
            return contentDatabase.movieTypeJoinDao().getMoviesForType(MovieType.NOW_PLAYING.id)
                    .map { MovieEntity.toMovies(it) }
        }

    val topRatedMovies: Flowable<List<Movie>>
        get() {
            return contentDatabase.movieTypeJoinDao().getMoviesForType(MovieType.TOP_RATED.id)
                    .map { MovieEntity.toMovies(it) }
        }


    val popularPeople: Flowable<List<Personality>>
        get() {
            return contentDatabase.personalityDao().getAllPersonalities()
                    .map { PersonalityEntity.toPersonalities(it) }
        }


    fun getAllMovies():  Flowable<List<Movie>> {
        return contentDatabase.movieDao().getAllMovies().map{ MovieEntity.toMovies(it) }
    }

    /**
     * Below methods insert content to the database
     */

    fun insertPopularMovie(movie: Movie) {
        insertMovie(movie, MovieType.POPULAR)
    }

    fun insertUpcomingMovie(movie: Movie) {
        insertMovie(movie, MovieType.UPCOMING)
    }

    fun insertNowPlayingMovie(movie: Movie) {
        insertMovie(movie, MovieType.NOW_PLAYING)
    }

    fun insertTopRatedMovie(movie: Movie) {
        insertMovie(movie, MovieType.TOP_RATED)
    }

    fun insertMovie(movie: Movie, type: MovieType) {
        contentDatabase.movieDao().insertMovie(MovieEntity.fromMovie(movie))
        contentDatabase.movieTypeJoinDao().insert(MovieTypeJoin(movie.id, type.id))
    }

    fun insertPersonality(personality: Personality) {
        contentDatabase.personalityDao().insertPersonality(PersonalityEntity.fromPersonality(personality))
    }
}
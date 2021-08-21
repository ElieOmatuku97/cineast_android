import elieomatuku.cineast_android.domain.model.Genre
import elieomatuku.cineast_android.domain.model.Movie
import elieomatuku.cineast_android.domain.model.Person

interface ContentRepository {

    fun discoverContent()

    fun insertPopularMovie(movies: List<Movie>)

    fun insertUpcomingMovie(movies: List<Movie>)

    fun insertNowPlayingMovie(movies: List<Movie>)

    fun insertTopRatedMovie(movies: List<Movie>)

    fun deleteAllPersonalities()

    fun deletePersonality(person: Person)

    fun deleteAllMovies()

    fun deleteMovie(movie: Movie)

    fun insertGenres(genres: List<Genre>)

    fun insertMovie(movie: Movie)

    fun updateMovie(movie: Movie)

    fun insertPersonality(
        person: Person
    )

    fun insertPersonalities(people: List<Person>)

    fun updatePersonality(person: Person)
}

interface ContentRepository {

    fun discoverContent()

    fun insertPopularMovie(movies: List<Movie>)

    fun insertUpcomingMovie(movies: List<Movie>)

    fun insertNowPlayingMovie(movies: List<Movie>)

    fun insertTopRatedMovie(movies: List<Movie>)

    fun deleteAllPersonalities()

    fun deletePersonality(personality: Personality)

    fun deleteAllMovies()

    fun deleteMovie(movie: Movie)

    fun insertGenres(genres: List<Genre>)

    fun insertMovie(movie: Movie)

    fun updateMovie(movie: Movie)

    fun insertPersonality(personality: Personality)

    fun insertPersonalities(personalities: List<Personality>)

    fun updatePersonality(personality: Personality)
}
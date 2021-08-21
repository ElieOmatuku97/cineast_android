package elieomatuku.cineast_android.domain

import elieomatuku.cineast_android.domain.model.MovieFacts
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Created by elieomatuku on 2021-01-30
 */

class MovieFactsTest {
    @Test
    fun testRuntimeInHoursAndMinutes() {
        var facts = MovieFacts(budget = 200000000, releaseDate = "2020-12-16", runtime = 151, revenue = 131400000, homepage = null)
        assertEquals(facts.runtimeInHoursAndMinutes, "2 hours 31 minutes")

        facts = facts.copy(runtime = 50)
        assertEquals(facts.runtimeInHoursAndMinutes, "50 minutes")

        facts = facts.copy(runtime = 60)
        assertEquals(facts.runtimeInHoursAndMinutes, "1 hour")

        facts = facts.copy(runtime = 65)
        assertEquals(facts.runtimeInHoursAndMinutes, "1 hour 5 minutes")

        facts = facts.copy(runtime = 30)
        assertEquals(facts.runtimeInHoursAndMinutes, "30 minutes")

        facts = facts.copy(runtime = 0)
        assertEquals(facts.runtimeInHoursAndMinutes, "N/A")

        facts = facts.copy(runtime = null)
        assertEquals(facts.runtimeInHoursAndMinutes, "N/A")
    }
}

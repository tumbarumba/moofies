import org.neo4j.driver.v1.AuthTokens
import org.neo4j.driver.v1.GraphDatabase
import org.neo4j.driver.v1.Values.parameters

fun main(args: Array<String>) {
    val driver = GraphDatabase.driver("bolt://54.145.155.174:32909", AuthTokens.basic("neo4j", "buoys-thread-task"))

    driver.session().use { session ->
        val cypherQuery ="""
            MATCH (m:Movie) --> (g:Genre)
            RETURN properties(m) as movie, g.name as genre
            LIMIT ${'$'}limit
        """
        val result = session.run(cypherQuery, parameters("limit", 100))
        while (result.hasNext()) {
            val record = result.next()
            val movie = record.get("movie")
            val genre = record.get("genre")
            val title = movie.get("title")
            println("$title ($genre)")

            for (key in movie.keys().sorted()) {
                println("\t$key:\t${movie.get(key)}")
            }
        }
    }
}

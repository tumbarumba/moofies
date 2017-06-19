import org.neo4j.driver.v1.AuthTokens
import org.neo4j.driver.v1.Config
import org.neo4j.driver.v1.GraphDatabase
import org.neo4j.driver.v1.Values.parameters

fun main(args: Array<String>) {
    val noSSL = Config.build().withEncryptionLevel(Config.EncryptionLevel.NONE).toConfig()
    val driver = GraphDatabase.driver("bolt://34.239.249.221:33516", AuthTokens.basic("neo4j", "windings-hills-perforators"), noSSL) // <password>

    driver.session().use { session ->
        val cypherQuery ="""
            MATCH (n)
            RETURN id(n) AS id
            LIMIT ${'$'}limit
        """
        val result = session.run(cypherQuery, parameters("limit", 10))
        while (result.hasNext()) {
            println(result.next().get("id"))
        }
    }
}

package repository

import org.neo4j.test.ImpermanentGraphDatabase
import org.neo4j.kernel.GraphDatabaseAPI
import org.neo4j.server.WrappingNeoServerBootstrapper
import scala.collection.mutable
import collection.JavaConversions._
import java.net.ServerSocket
import java.io.IOException
import org.neo4j.server.configuration.{ServerConfigurator, Configurator}
import org.anormcypher.Neo4jREST


trait TemporaryDatabase {
    class ImpermanentGraphDatabaseAPI() extends ImpermanentGraphDatabase with GraphDatabaseAPI {
        var newConfig = mutable.Map[String, String](config.getParams.toSeq:_*)
        newConfig.put("ephemeral", "true")
        config.applyChanges(newConfig)
    }

    val port = findFreePort(Range(49152, 65535))

    private val bootstrapper = {
        val graphDb = new ImpermanentGraphDatabaseAPI()
        val config = new ServerConfigurator(graphDb)
        config.configuration().addProperty(Configurator.WEBSERVER_PORT_PROPERTY_KEY, port)
        new WrappingNeoServerBootstrapper(graphDb, config)
    }

    private def findFreePort(portRange: Range): Int = {
        portRange.find { prospectivePort: Int =>
            try {
                val socket = new ServerSocket(prospectivePort)
                socket.close()
                true
            } catch {
                case _: IOException => false
            }
        }.getOrElse( throw new IOException("No free port found for Test Database"))
    }

    def start() = {
        bootstrapper.start()
    }

    def stop() = bootstrapper.stop()
}

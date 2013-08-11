package repository

import domain.{Team, Person}
import org.anormcypher.{NeoNode, Cypher, Neo4jREST}
import play.api.libs.json.Json

case class Repository() {
    def findTeamForPerson(person: Person): Option[Team] = Cypher(
        """
          |START person = node(*)
          |MATCH person-[:TEAMMEMBER_OF]->team
          |WHERE person.id! = {personId}
          |RETURN team
        """.stripMargin).on("personId" -> person.id)().flatMap { row =>
        val nodeAsMap = row[NeoNode]("team").props
        val jsonValue = Json.toJson(nodeAsMap)(Neo4jREST.mapFormat)
        jsonValue.asOpt[Team]
    } match {
        case result if result.size > 1 => throw new IllegalStateException(s"Found more than one team for person, ${person.id}")
        case result => result.headOption
    }

    def listPersons: List[Person] = Cypher("START n = node(*) RETURN n;")().flatMap { row =>
        val nodeAsMap = row[NeoNode]("n").props
        val jsonValue = Json.toJson(nodeAsMap)(Neo4jREST.mapFormat)
        jsonValue.asOpt[Person]
    }.toList

    def listTeams: List[Team] = Cypher(
        """
          |START team = node(*)
          |MATCH person-[:TEAMMEMBER_OF]->team
          |RETURN team
        """.stripMargin
    )().flatMap { row =>
        val nodeAsMap = row[NeoNode]("team").props
        val jsonValue = Json.toJson(nodeAsMap)(Neo4jREST.mapFormat)
        jsonValue.asOpt[Team]
    }.toList

    def findPersonsForTeam(team: Team): List[Person] = Cypher(
        """
          |START person = node(*)
          |MATCH person-[:TEAMMEMBER_OF]->team
          |WHERE team.id! = {teamId}
          |RETURN person
        """.stripMargin).on("teamId" -> team.id)().flatMap { row =>
        val nodeAsMap = row[NeoNode]("person").props
        val jsonValue = Json.toJson(nodeAsMap)(Neo4jREST.mapFormat)
        jsonValue.asOpt[Person]
    }.toList
}
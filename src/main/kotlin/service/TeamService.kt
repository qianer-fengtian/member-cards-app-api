package jp.co.anyplus.anyplab.webapp.membercards.service

import jp.co.anyplus.anyplab.webapp.membercards.dao.Teams
import jp.co.anyplus.anyplab.webapp.membercards.model.Team
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.joda.time.DateTime
import java.util.*


private fun parseRow(row: ResultRow) = Team(
    id = row[Teams.id],
    name = row[Teams.name],
    leaderId = row[Teams.leaderId],
    deleted = row[Teams.deleted],
    registeredDate = row[Teams.registeredDate],
    modifiedDate = row[Teams.modifiedDate]
)

class TeamService {
    fun getAll(): List<Team> {
        var teams = listOf<Team>()
        transaction {
            teams = Teams
                .select { Teams.deleted.eq(false) }
                .map { parseRow(it) }
                .sortedByDescending { Teams.registeredDate }
        }
        return teams
    }

    fun get(id: UUID) : Team? {
        var team: Team? = null
        transaction {
            team = Teams
                .select { Teams.id.eq(id) }
                .map { parseRow(it) }[0]
        }
        return team ?: null
    }

    fun register(team: Team) : Unit {
        transaction {
            val tmp = Teams.insert {
                it[id] = UUID.randomUUID()
                it[name] = team.name
                it[leaderId] = team.leaderId
                it[bool("deleted")] = false
                it[datetime("registered_date")] = DateTime.now()
                it[datetime("modified_date")] = DateTime.now()
            }
        }
    }

    fun update(team: Team) : Unit {
        transaction {
            Teams.update({ Teams.id eq team.id }) {
                it[name] = team.name
                it[leaderId] = team.leaderId
                it[bool("deleted")] = team.deleted
                it[datetime("modified_date")] = DateTime.now()
            }
        }
    }
}
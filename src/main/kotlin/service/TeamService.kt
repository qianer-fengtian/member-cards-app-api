package jp.co.anyplus.anyplab.webapp.membercards.service

import jp.co.anyplus.anyplab.webapp.membercards.dao.teams.TeamsDao
import jp.co.anyplus.anyplab.webapp.membercards.model.teams.Team
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.joda.time.DateTime
import java.util.*

class TeamService {
    fun getAll(): List<Team> {
        var teams = listOf<Team>()
        transaction {
            teams = TeamsDao
                .select { TeamsDao.deleted.eq(false) }
                .map { Team.parseRow(it) }
                .sortedByDescending { TeamsDao.registeredDate }
        }
        return teams
    }

    fun get(id: UUID) : Team? {
        var team: Team? = null
        transaction {
            team = TeamsDao
                .select { TeamsDao.id.eq(id) }
                .map { Team.parseRow(it) }[0]
        }
        return team ?: null
    }

    fun register(team: Team) {
        transaction {
            val tmp = TeamsDao.insert {
                it[id] = UUID.randomUUID()
                it[name] = team.name
                it[leaderId] = team.leaderId
                it[bool("deleted")] = false
                it[datetime("registered_date")] = DateTime.now()
                it[datetime("modified_date")] = DateTime.now()
            }
        }
    }

    fun update(team: Team) {
        transaction {
            TeamsDao.update({ TeamsDao.id eq team.id }) {
                it[name] = team.name
                it[leaderId] = team.leaderId
                it[bool("deleted")] = team.deleted
                it[datetime("modified_date")] = DateTime.now()
            }
        }
    }
}
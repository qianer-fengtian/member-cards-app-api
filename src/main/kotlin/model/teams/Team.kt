package jp.co.anyplus.anyplab.webapp.membercards.model.teams

import jp.co.anyplus.anyplab.webapp.membercards.dao.teams.TeamsDao
import org.jetbrains.exposed.sql.ResultRow
import org.joda.time.DateTime
import java.util.*

class Team(
    val id: UUID,
    val name: String,
    val leaderId: UUID,
    val deleted: Boolean,
    val registeredDate: DateTime,
    val modifiedDate: DateTime
) {

    companion object {
        fun parseRow(row: ResultRow): Team {
            return Team(
                id = row[TeamsDao.id],
                name = row[TeamsDao.name],
                leaderId = row[TeamsDao.leaderId],
                deleted = row[TeamsDao.deleted],
                registeredDate = row[TeamsDao.registeredDate],
                modifiedDate = row[TeamsDao.modifiedDate]
            )
        }
    }
}
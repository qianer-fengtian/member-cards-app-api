package jp.co.anyplus.anyplab.webapp.membercards.dao.teams

import org.jetbrains.exposed.sql.Table

object TeamsDao : Table("teams") {
    val id = uuid("id").primaryKey()
    val name = varchar("name", 10)
    val leaderId = uuid("leader_id")
    val deleted = bool("deleted")
    val registeredDate = datetime("registered_date")
    val modifiedDate = datetime("modified_date")
}
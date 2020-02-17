package jp.co.anyplus.anyplab.webapp.membercards.dao

import org.jetbrains.exposed.sql.Table

object Teams : Table() {
    val id = uuid("id").primaryKey()
    val name = varchar("name", 10)
    val leaderId = uuid("leader_id")
    val deleted = bool("deleted")
    val registeredDate = date("registered_date")
    val modifiedDate = date("modified_date")
}
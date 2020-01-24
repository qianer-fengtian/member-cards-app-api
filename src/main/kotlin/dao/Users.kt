package jp.co.anyplus.anyplab.webapp.membercards.dao

import org.jetbrains.exposed.sql.Table

object Users: Table() {
    val id = uuid("id").primaryKey()
    val code = varchar("code", 10).uniqueIndex()
    val name = varchar("name", 10)
    val role = varchar("role", 10)
    val password = text("password")
    val deleted = bool("deleted")
    val registeredDate = date("registered_date")
    val modifiedDate = date("modified_date")
}
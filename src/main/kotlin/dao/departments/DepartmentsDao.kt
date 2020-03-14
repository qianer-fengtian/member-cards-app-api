package jp.co.anyplus.anyplab.webapp.membercards.dao.departments

import org.jetbrains.exposed.sql.Table

object DepartmentsDao : Table("departments") {
    val id = uuid("id").primaryKey()
    val name = varchar("name", 10)
    val deleted = bool("deleted")
    val registeredDate = datetime("registered_date")
    val modifiedDate = datetime("modified_date")
}
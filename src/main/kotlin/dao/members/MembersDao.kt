package jp.co.anyplus.anyplab.webapp.membercards.dao.members

import org.jetbrains.exposed.sql.Table

object MembersDao : Table("members") {
    val id = uuid("id").primaryKey()
    val name = varchar("name", 10)
    val avatar = binary("avatar", 1024*1024*1024)
    val gender = varchar("gender", 1)
    val birthDate = datetime("birth_date")
    val joinedDate = datetime("joined_date")
    val leftDate = datetime("left_date").nullable()
    val specialty = text("specialty")
    val selfAppeal = text("self_appeal")
    val departmentId = uuid("department_id")
    val teamId = uuid("team_id").nullable()
    val deleted = bool("deleted")
    val registeredDate = datetime("registered_date")
    val modifiedDate = datetime("modified_date")
}
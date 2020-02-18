package jp.co.anyplus.anyplab.webapp.membercards.dao

import org.jetbrains.exposed.sql.Table

object Members : Table("members") {
    val id = uuid("id").primaryKey()
    val name = varchar("name", 10)
    val avatar = binary("avatar", 1024*1024*1024)
    val birthDate = date("birth_date")
    val joinedDate = date("joined_date")
    val leftDate = date("left_date").nullable()
    val gender = varchar("gender", 1)
    val specialty = text("specialty")
    val selfAppeal = text("self_appeal")
    val departmentId = uuid("department_id")
    val teamId = uuid("team_id").nullable()
    val deleted = bool("deleted")
    val registeredDate = date("registered_date")
    val modifiedDate = date("modified_date")
}
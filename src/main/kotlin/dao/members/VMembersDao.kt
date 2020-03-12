package jp.co.anyplus.anyplab.webapp.membercards.dao.members

import org.jetbrains.exposed.sql.Table

object VMembersDao : Table("v_members") {
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
    val registeredDate = datetime("registered_date")
    val modifiedDate = datetime("modified_date")

    val memberJoiningId = uuid("member_joining_id")
    val joiningForm = text("joining_form")
}
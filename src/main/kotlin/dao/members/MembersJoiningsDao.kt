package jp.co.anyplus.anyplab.webapp.membercards.dao.members

import org.jetbrains.exposed.sql.Table

object MembersJoiningsDao: Table("members_joinings") {
    val id = uuid("id")
    val memberId = uuid("member_id").primaryKey()
    val joiningForm = text("joining_form")
}
package jp.co.anyplus.anyplab.webapp.membercards.service

import jp.co.anyplus.anyplab.webapp.membercards.dao.members.MembersDao
import jp.co.anyplus.anyplab.webapp.membercards.dao.members.MembersJoiningsDao
import jp.co.anyplus.anyplab.webapp.membercards.dao.members.VMembersDao
import jp.co.anyplus.anyplab.webapp.membercards.model.members.Member
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import java.util.*

class MemberService {
    fun getAll(): List<Member> {
        var members = listOf<Member>()
        transaction {
            members = VMembersDao
                .select { VMembersDao.deleted.eq(false) }
                .map { Member.parseRow(it) }
                .sortedByDescending { VMembersDao.registeredDate }
        }
        return members
    }

    fun getEmployees(): List<Member> {
        var members = listOf<Member>()
        transaction {
            members = VMembersDao
                .select { VMembersDao.deleted.eq(false).and(VMembersDao.leftDate.isNull()) }
                .map { Member.parseRow(it) }
                .sortedByDescending { VMembersDao.registeredDate }
        }
        return members
    }

    fun getRetirees(): List<Member> {
        var members = listOf<Member>()
        transaction {
            members = VMembersDao
                .select { VMembersDao.deleted.eq(false).and(VMembersDao.leftDate.isNotNull()) }
                .map { Member.parseRow(it) }
                .sortedByDescending { VMembersDao.registeredDate }
        }
        return members
    }

    fun get(id: UUID) : Member? {
        var member: Member? = null
        transaction {
            member = VMembersDao
                .select { VMembersDao.id.eq(id) }
                .map { Member.parseRow(it) }[0]
        }
        return member ?: null
    }

    fun register(member: Member) : Unit {
        transaction {
            val memberId = UUID.randomUUID()
            MembersDao.insert {
                it[id] = memberId
                it[name] = member.name
                it[avatar] = member.avatar
                it[birthDate] = member.birthDate
                it[joinedDate] = member.joinedDate
                it[leftDate] = member.leftDate
                it[gender] = member.gender
                it[specialty] = member.specialty
                it[selfAppeal] = member.selfAppeal
                it[departmentId] = member.departmentId
                it[teamId] = member.teamId
                it[bool("deleted")] = false
                it[datetime("registered_date")] = DateTime.now()
                it[datetime("modified_date")] = DateTime.now()
            }

            MembersJoiningsDao.insert {
                it[id] = UUID.randomUUID()
                it[MembersJoiningsDao.memberId] = memberId
                it[joiningForm] = member.joiningForm ?: "9"
            }
        }
    }

    fun update(member: Member) : Unit {
        transaction {
            MembersDao.update({ MembersDao.id eq member.id }) {
                it[name] = member.name
                it[avatar] = member.avatar
                it[birthDate] = member.birthDate
                it[joinedDate] = member.joinedDate
                it[leftDate] = member.leftDate
                it[gender] = member.gender
                it[specialty] = member.specialty
                it[selfAppeal] = member.selfAppeal
                it[departmentId] = member.departmentId
                it[teamId] = member.teamId
                it[bool("deleted")] = member.deleted
                it[datetime("modified_date")] = DateTime.now()
            }

            MembersJoiningsDao.update({ MembersJoiningsDao.memberId eq member.id}) {
                it[joiningForm] = member.joiningForm ?: "9"
            }
        }
    }
}
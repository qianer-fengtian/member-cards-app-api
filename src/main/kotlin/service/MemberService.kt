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

    fun get(id: UUID) : Member? {
        var member: Member? = null
        transaction {
            member = VMembersDao
                .select { VMembersDao.id.eq(id) }
                .map { Member.parseRow(it) }[0]
        }
        return member ?: null
    }

    fun getStatistics(): Map<String, Any> {
        var members = listOf<Member>()
        transaction {
            members = VMembersDao
                .select { VMembersDao.deleted.eq(false) }
                .map { Member.parseRow(it) }
        }

        val statistics = hashMapOf(
            "total" to members.count(),
            "maleTotal" to members.filter { it.gender == "1" }.count(),
            "femaleTotal" to members.filter { it.gender == "2" }.count(),
            "numberOfJoinedPerYear" to members.groupingBy { it.joinedDate.year }.eachCount(),
            "numberOfLeftPerYear" to members.filter { it.leftDate !== null }.groupingBy { it.leftDate?.year }.eachCount(),
            "populationByAge" to members.groupingBy { DateTime.now().year - it.birthDate.year }.eachCount()
        )
        return statistics
    }

    fun register(member: Member) : Unit {
        transaction {
            val memberId = UUID.randomUUID()
            MembersDao.insert {
                it[MembersDao.id] = memberId
                it[MembersDao.name] = member.name
                it[MembersDao.avatar] = member.avatar
                it[MembersDao.birthDate] = member.birthDate
                it[MembersDao.joinedDate] = member.joinedDate
                it[MembersDao.leftDate] = member.leftDate
                it[MembersDao.gender] = member.gender
                it[MembersDao.specialty] = member.specialty
                it[MembersDao.selfAppeal] = member.selfAppeal
                it[MembersDao.departmentId] = member.departmentId
                it[MembersDao.teamId] = member.teamId
                it[bool("deleted")] = false
                it[datetime("registered_date")] = DateTime.now()
                it[datetime("modified_date")] = DateTime.now()
            }

            MembersJoiningsDao.insert {
                it[MembersJoiningsDao.id] = UUID.randomUUID()
                it[MembersJoiningsDao.memberId] = memberId
                it[MembersJoiningsDao.joiningForm] = member.memberJoining?.joiningForm ?: "9"
            }
        }
    }

    fun update(member: Member) : Unit {
        transaction {
            MembersDao.update({ MembersDao.id eq member.id }) {
                it[MembersDao.name] = member.name
                it[MembersDao.avatar] = member.avatar
                it[MembersDao.birthDate] = member.birthDate
                it[MembersDao.joinedDate] = member.joinedDate
                it[MembersDao.leftDate] = member.leftDate
                it[MembersDao.gender] = member.gender
                it[MembersDao.specialty] = member.specialty
                it[MembersDao.selfAppeal] = member.selfAppeal
                it[MembersDao.departmentId] = member.departmentId
                it[MembersDao.teamId] = member.teamId
                it[bool("deleted")] = member.deleted
                it[datetime("modified_date")] = DateTime.now()
            }

            MembersJoiningsDao.update({ MembersJoiningsDao.memberId eq member.id}) {
                it[MembersJoiningsDao.joiningForm] = member.memberJoining?.joiningForm ?: "9"
            }
        }
    }
}
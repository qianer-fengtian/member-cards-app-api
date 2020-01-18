package jp.co.anyplus.anyplab.webapp.membercards.service

import jp.co.anyplus.anyplab.webapp.membercards.dao.Members
import jp.co.anyplus.anyplab.webapp.membercards.model.Member
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import java.util.*

private fun parseRow(row: ResultRow) = Member(
    id = row[Members.id],
    code = row[Members.code],
    name = row[Members.name],
    avatar = row[Members.avatar],
    birthDate = row[Members.birthDate],
    specialty = row[Members.specialty],
    selfAppeal = row[Members.selfAppeal],
    departmentId = row[Members.departmentId],
    deleted = row[Members.deleted],
    registeredDate = row[Members.registeredDate],
    modifiedDate = row[Members.modifiedDate]
)

class MemberService {
    fun getAll(): List<Member> {
        var members = listOf<Member>()
        transaction {
            members = Members
                .select { Members.deleted.eq(false) }
                .map { parseRow(it) }
                .sortedByDescending { Members.registeredDate }
                .sortedBy { Members.code }
        }
        return members
    }

    fun get(id: UUID) : Member? {
        var member: Member? = null
        transaction {
            member = Members
                .select { Members.id.eq(id) }
                .map { parseRow(it) }[0]
        }
        return member ?: null
    }

    fun register(member: Member) : Unit {
        transaction {
            val tmp = Members.insert {
                it[Members.id] = UUID.randomUUID()
                it[Members.code] = member.code
                it[Members.name] = member.name
                it[Members.avatar] = member.avatar
                it[Members.birthDate] = member.birthDate
                it[Members.specialty] = member.specialty
                it[Members.selfAppeal] = member.selfAppeal
                it[Members.departmentId] = member.departmentId
                it[bool("deleted")] = false
                it[date("registered_date")] = DateTime.now()
                it[date("modified_date")] = DateTime.now()
            }
        }
    }

    fun update(member: Member) : Unit {
        transaction {
            Members.update({ Members.id eq member.id }) {
                it[Members.code] = member.code
                it[Members.name] = member.name
                it[Members.avatar] = member.avatar
                it[Members.birthDate] = member.birthDate
                it[Members.specialty] = member.specialty
                it[Members.selfAppeal] = member.selfAppeal
                it[Members.departmentId] = member.departmentId
                it[bool("deleted")] = member.deleted
                it[date("modified_date")] = DateTime.now()
            }
        }
    }
}
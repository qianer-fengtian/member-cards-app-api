package jp.co.anyplus.anyplab.webapp.membercards.model.members

import jp.co.anyplus.anyplab.webapp.membercards.dao.members.VMembersDao
import org.jetbrains.exposed.sql.ResultRow
import org.joda.time.DateTime
import java.util.*

class Member(
    val id: UUID,
    val name: String,
    val avatar: ByteArray,
    val birthDate: DateTime,
    val joinedDate: DateTime,
    val leftDate: DateTime?,
    val gender: String,
    val specialty: String,
    val selfAppeal: String,
    val departmentId: UUID,
    val teamId: UUID?,
    val deleted: Boolean,
    val registeredDate: DateTime,
    val modifiedDate: DateTime,
    var memberJoining: MemberJoining?
) {
    companion object {
        fun parseRow(row: ResultRow): Member {
            val memberJoining = if (row[VMembersDao.memberJoiningId] != null) {
                MemberJoining(
                    id = row[VMembersDao.memberJoiningId],
                    memberId = row[VMembersDao.id],
                    joiningForm = row[VMembersDao.joiningForm]
                )
            } else {
                null
            }

            return Member(
                id = row[VMembersDao.id],
                name = row[VMembersDao.name],
                avatar = row[VMembersDao.avatar],
                birthDate = row[VMembersDao.birthDate],
                joinedDate = row[VMembersDao.joinedDate],
                leftDate = row[VMembersDao.leftDate],
                gender = row[VMembersDao.gender],
                specialty = row[VMembersDao.specialty],
                selfAppeal = row[VMembersDao.selfAppeal],
                departmentId = row[VMembersDao.departmentId],
                teamId = row[VMembersDao.teamId],
                deleted = row[VMembersDao.deleted],
                registeredDate = row[VMembersDao.registeredDate],
                modifiedDate = row[VMembersDao.modifiedDate],
                memberJoining = memberJoining
            )
        }
    }
}
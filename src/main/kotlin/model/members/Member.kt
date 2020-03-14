package jp.co.anyplus.anyplab.webapp.membercards.model.members

import jp.co.anyplus.anyplab.webapp.membercards.dao.members.VMembersDao
import org.jetbrains.exposed.sql.ResultRow
import org.joda.time.DateTime
import java.util.*

class Member(
    val id: UUID,
    val name: String,
    val avatar: ByteArray,
    val gender: String,
    val birthDate: DateTime,
    val joinedDate: DateTime,
    val leftDate: DateTime?,
    val specialty: String,
    val selfAppeal: String,
    val departmentId: UUID,
    var departmentName: String?,
    val teamId: UUID?,
    val teamName: String?,
    val joiningId: UUID?,
    var joiningForm: String?,
    val deleted: Boolean,
    val registeredDate: DateTime,
    val modifiedDate: DateTime
) {
    companion object {
        fun parseRow(row: ResultRow): Member {
            return Member(
                id = row[VMembersDao.id],
                name = row[VMembersDao.name],
                avatar = row[VMembersDao.avatar],
                gender = row[VMembersDao.gender],
                birthDate = row[VMembersDao.birthDate],
                joinedDate = row[VMembersDao.joinedDate],
                leftDate = row[VMembersDao.leftDate],
                specialty = row[VMembersDao.specialty],
                selfAppeal = row[VMembersDao.selfAppeal],
                departmentId = row[VMembersDao.departmentId],
                departmentName = row[VMembersDao.departmentName],
                teamId = row[VMembersDao.teamId],
                teamName = row[VMembersDao.teamName],
                joiningId = row[VMembersDao.joiningId],
                joiningForm = row[VMembersDao.joiningForm],
                deleted = row[VMembersDao.deleted],
                registeredDate = row[VMembersDao.registeredDate],
                modifiedDate = row[VMembersDao.modifiedDate]
            )
        }
    }
}
package jp.co.anyplus.anyplab.webapp.membercards.model.members

import jp.co.anyplus.anyplab.webapp.membercards.dao.members.VMembersDao
import org.jetbrains.exposed.sql.ResultRow
import org.joda.time.DateTime
import java.util.*

class MemberAvatar (
    val id: UUID,
    val avatar: ByteArray,
    val leftDate: DateTime?,
    val deleted: Boolean
) {
    companion object {
        fun parseRow(row: ResultRow): MemberAvatar {
            return MemberAvatar(
                id = row[VMembersDao.id],
                avatar = row[VMembersDao.avatar],
                leftDate = row[VMembersDao.leftDate],
                deleted = row[VMembersDao.deleted]
            )
        }
    }
}
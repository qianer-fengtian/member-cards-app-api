package jp.co.anyplus.anyplab.webapp.membercards.model.users

import jp.co.anyplus.anyplab.webapp.membercards.dao.users.UsersDao
import org.jetbrains.exposed.sql.ResultRow
import org.joda.time.DateTime
import java.util.*

class User(
    val id: UUID,
    val code: String,
    val name: String,
    val role: String,
    val password: String,
    val deleted: Boolean,
    val registeredDate: DateTime,
    val modifiedDate: DateTime
) {
    companion object {
        fun parseRow(row: ResultRow) = User(
            id = row[UsersDao.id],
            code = row[UsersDao.code],
            name = row[UsersDao.name],
            role = row[UsersDao.role],
            password = row[UsersDao.password],
            deleted = row[UsersDao.deleted],
            registeredDate = row[UsersDao.registeredDate],
            modifiedDate = row[UsersDao.modifiedDate]
        )
    }
}
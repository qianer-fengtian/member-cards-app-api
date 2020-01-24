package jp.co.anyplus.anyplab.webapp.membercards.service

import jp.co.anyplus.anyplab.webapp.membercards.dao.Users
import jp.co.anyplus.anyplab.webapp.membercards.model.User
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

private fun parseRow(row: ResultRow) = User(
    id = row[Users.id],
    code = row[Users.code],
    name = row[Users.name],
    role = row[Users.role],
    password = row[Users.password],
    deleted = row[Users.deleted],
    registeredDate = row[Users.registeredDate],
    modifiedDate = row[Users.modifiedDate]
)

class UserService {
    fun get(code: String) : User? {
        var user: User? = null
        transaction {
            user = Users
                .select { Users.code.eq(code) }
                .map { parseRow(it) }
                .getOrNull(0)
        }
        return user
    }
}
package jp.co.anyplus.anyplab.webapp.membercards.service

import jp.co.anyplus.anyplab.webapp.membercards.dao.users.UsersDao
import jp.co.anyplus.anyplab.webapp.membercards.model.users.User
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class UserService {
    fun get(code: String) : User? {
        var user: User? = null
        transaction {
            user = UsersDao
                .select { UsersDao.code.eq(code) }
                .map { User.parseRow(it) }
                .getOrNull(0)
        }
        return user
    }
}
package jp.co.anyplus.anyplab.webapp.membercards.model

import io.ktor.auth.Principal
import org.joda.time.DateTime
import java.util.*

data class User(
    val id: UUID,
    val code: String,
    val name: String,
    val role: String,
    val password: String,
    val deleted: Boolean,
    val registeredDate: DateTime,
    val modifiedDate: DateTime
)

data class UserPrincipal (
    val code: String,
    val username: String,
    val role: String
) : Principal {}
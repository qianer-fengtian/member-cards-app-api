package jp.co.anyplus.anyplab.webapp.membercards.model.users

import io.ktor.auth.Principal

data class UserPrincipal (
    val code: String,
    val username: String,
    val role: String
) : Principal {}
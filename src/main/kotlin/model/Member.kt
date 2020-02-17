package jp.co.anyplus.anyplab.webapp.membercards.model

import org.joda.time.DateTime
import java.util.*

data class Member(
    val id: UUID,
    val name: String,
    val avatar: ByteArray,
    val birthDate: DateTime,
    val joinedDate: DateTime,
    val gender: String,
    val specialty: String,
    val selfAppeal: String,
    val departmentId: UUID,
    val deleted: Boolean,
    val registeredDate: DateTime,
    val modifiedDate: DateTime
)
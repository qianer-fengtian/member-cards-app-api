package jp.co.anyplus.anyplab.webapp.membercards.model

import org.joda.time.DateTime
import java.util.*

data class Department(
    val id: UUID,
    val code: String,
    val name: String,
    val deleted: Boolean,
    val registeredDate: DateTime,
    val modifiedDate: DateTime
)
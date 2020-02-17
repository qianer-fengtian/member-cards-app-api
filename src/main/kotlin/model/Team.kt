package jp.co.anyplus.anyplab.webapp.membercards.model

import org.joda.time.DateTime
import java.util.*

data class Team(
    val id: UUID,
    val name: String,
    val leaderId: UUID,
    val deleted: Boolean,
    val registeredDate: DateTime,
    val modifiedDate: DateTime
)
package jp.co.anyplus.anyplab.webapp.membercards.model.members

import java.util.*

data class MemberJoining(
    val id: UUID,
    val memberId: UUID,
    val joiningForm: String
)
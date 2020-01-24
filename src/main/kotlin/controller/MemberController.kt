package jp.co.anyplus.anyplab.webapp.membercards.controller

import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*
import jp.co.anyplus.anyplab.webapp.membercards.dao.Members
import jp.co.anyplus.anyplab.webapp.membercards.model.Member
import jp.co.anyplus.anyplab.webapp.membercards.service.DepartmentService
import jp.co.anyplus.anyplab.webapp.membercards.service.MemberService
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import java.util.*

private val memberService = MemberService()
private val departmentService = DepartmentService()

fun Route.memberController() {
    route("/members") {
        get {
            val members = memberService.getAll()
            if (members.size > 0) {
                call.respond(members)
            } else {
                call.respond(404)
            }
        }

        get("/{id}") {
            val id = UUID.fromString(call.parameters["id"])
            val member = memberService.get(id)
            call.respond(member ?: 404)
        }

        post {
            val member = call.receive<Member>()
            memberService.register(member)
            call.respond(200)
        }

        put {
            val member = call.receive<Member>()
            memberService.update(member)
            call.respond(200)
        }
    }
}
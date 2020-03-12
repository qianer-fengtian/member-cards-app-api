package jp.co.anyplus.anyplab.webapp.membercards.controller

import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*
import jp.co.anyplus.anyplab.webapp.membercards.model.members.Member
import jp.co.anyplus.anyplab.webapp.membercards.service.MemberService
import java.util.*

private val memberService = MemberService()

fun Route.memberController() {
    route("/members") {
        get {
            val members = memberService.getAll()
            call.respond(members)
        }

        get("/{id}") {
            val id = UUID.fromString(call.parameters["id"])
            val member = memberService.get(id)
            call.respond(member ?: 404)
        }

        get("/statistics") {
            val statistics = memberService.getStatistics()
            call.respond(statistics)
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
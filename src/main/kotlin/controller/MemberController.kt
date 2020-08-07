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

        get("/employees") {
            val employees = memberService.getEmployees()
            call.respond(employees)
        }

        get("/employees/avatars") {
            val employeeAvatars = memberService.getEmployeeAvatars()
            call.respond(employeeAvatars)
        }

        get("/retirees") {
            val retirees = memberService.getRetirees()
            call.respond(retirees)
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
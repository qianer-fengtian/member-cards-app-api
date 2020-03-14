package jp.co.anyplus.anyplab.webapp.membercards.controller

import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*
import jp.co.anyplus.anyplab.webapp.membercards.model.departments.Department
import jp.co.anyplus.anyplab.webapp.membercards.service.DepartmentService
import java.util.*

private val departmentService = DepartmentService()

fun Route.departmentController() {
    route("/departments") {
        get {
            val departments = departmentService.getAll()
            call.respond(departments)
        }

        get("/{id}") {
            val id = UUID.fromString(call.parameters["id"])
            val department = departmentService.get(id)
            call.respond(department ?: 404)
        }

        post {
            val department = call.receive<Department>()
            departmentService.register(department)
            call.respond(200)
        }

        put {
            val department = call.receive<Department>()
            departmentService.update(department)
            call.respond(200)
        }
    }
}
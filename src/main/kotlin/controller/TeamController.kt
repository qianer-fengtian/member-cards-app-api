package jp.co.anyplus.anyplab.webapp.membercards.controller

import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*
import jp.co.anyplus.anyplab.webapp.membercards.model.teams.Team
import jp.co.anyplus.anyplab.webapp.membercards.service.TeamService
import java.util.*

private val teamService = TeamService()

fun Route.teamController() {
    route("/teams") {
        get {
            val teams = teamService.getAll()
            call.respond(teams)
        }

        get("/{id}") {
            val id = UUID.fromString(call.parameters["id"])
            val team = teamService.get(id)
            call.respond(team ?: 404)
        }

        post {
            val team = call.receive<Team>()
            teamService.register(team)
            call.respond(200)
        }

        put {
            val team = call.receive<Team>()
            teamService.update(team)
            call.respond(200)
        }
    }
}
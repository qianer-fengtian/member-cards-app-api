package jp.co.anyplus.anyplab.webapp.membercards.controller

import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.*
import jp.co.anyplus.anyplab.webapp.membercards.service.MemberService
import jp.co.anyplus.anyplab.webapp.membercards.service.StatisticsService

private val memberService = MemberService()
private val statisticsService = StatisticsService()

fun Route.statisticsController() {
    route("/statistics") {
        get {
            val numbersOfEmployments = statisticsService.getNumbersOfEmployments()
            val numbersOfRetirements = statisticsService.getNumbersOfRetirements()
            call.respond(mapOf(
                "numbers-of-employments" to numbersOfEmployments,
                "numbers-of-retirements" to numbersOfRetirements
            ))
        }
    }
}
package jp.co.anyplus.anyplab.webapp.membercards

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.features.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.auth.*
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.auth.jwt.jwt
import io.ktor.jackson.*
import io.ktor.request.httpMethod
import io.ktor.request.receive
import jp.co.anyplus.anyplab.webapp.membercards.controller.departmentController
import jp.co.anyplus.anyplab.webapp.membercards.controller.memberController
import jp.co.anyplus.anyplab.webapp.membercards.exception.InvalidAuthorizationException
import jp.co.anyplus.anyplab.webapp.membercards.exception.InvalidCredentialsException
import jp.co.anyplus.anyplab.webapp.membercards.model.User
import jp.co.anyplus.anyplab.webapp.membercards.model.UserPrincipal
import jp.co.anyplus.anyplab.webapp.membercards.service.UserService
import org.jetbrains.exposed.sql.Database
import java.lang.Exception

fun initDB() {
    val config = HikariConfig("/hikari.properties")
    val ds = HikariDataSource(config)
    Database.connect(ds)
}

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {

    val secret = environment.config.property("app.jwt.secret").getString()
    val simpleJwt = SimpleJWT(secret)

    install(CORS) {
        method(HttpMethod.Options)
        method(HttpMethod.Post)
        method(HttpMethod.Put)
        method(HttpMethod.Delete)
        method(HttpMethod.Patch)
        header(HttpHeaders.Authorization)
        header(HttpHeaders.ContentType)
        allowCredentials = true
        anyHost()
    }

    initDB()

    install(Authentication) {
        jwt {
            verifier(simpleJwt.verifier)
            validate {
                UserPrincipal(
                    it.payload.getClaim("code").asString(),
                    it.payload.getClaim("name").asString(),
                    it.payload.getClaim("role").asString()
                )
            }
        }
    }

    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
            registerModule(JodaModule())
        }
    }

    install(StatusPages) {
        exception<InvalidCredentialsException> { exception ->
            call.respond(HttpStatusCode.Unauthorized, mapOf(
                "OK" to false,
                "error" to (exception.message ?: "")
            ))
        }
    }

    routing {
        route("/login") {
            post {
                val userService = UserService()
                val user = call.receive<User>()
                userService.get(user.code)?.let {
                    if (user.password != it.password) {
                        throw InvalidCredentialsException("Invalid credentials")
                    }
                    call.respond(mapOf("token" to simpleJwt.sign(it.code, it.name, it.role)))
                }
            }
        }

        authenticate {
            intercept(ApplicationCallPipeline.Call) {
                val principal = call.principal<UserPrincipal>() ?: error("No principal")
                if (principal.role == "user" && call.request.httpMethod != HttpMethod.Get) {
                    throw InvalidAuthorizationException("Invalid authorization")
                }
                return@intercept
            }

            memberController()
            departmentController()
        }
    }
}


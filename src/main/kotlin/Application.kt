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
import jp.co.anyplus.anyplab.webapp.membercards.controller.teamController
import jp.co.anyplus.anyplab.webapp.membercards.exception.InvalidAuthorizationException
import jp.co.anyplus.anyplab.webapp.membercards.exception.InvalidCredentialsException
import jp.co.anyplus.anyplab.webapp.membercards.exception.UserNotFoundException
import jp.co.anyplus.anyplab.webapp.membercards.model.User
import jp.co.anyplus.anyplab.webapp.membercards.model.UserPrincipal
import jp.co.anyplus.anyplab.webapp.membercards.service.UserService
import org.jetbrains.exposed.sql.Database
import java.lang.Exception
import java.security.MessageDigest

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    fun initDB() {
        val host = environment.config.property("db.host").getString()
        val port = environment.config.property("db.port").getString()
        val dbname = environment.config.property("db.dbname").getString()
        val username = environment.config.property("db.username").getString()
        val password = environment.config.property("db.password").getString()
        val config = HikariConfig()
        config.jdbcUrl = "jdbc:postgresql://${host}:${port}/${dbname}"
        config.username = username
        config.password = password
        val ds = HikariDataSource(config)
        Database.connect(ds)
    }

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
        header(HttpHeaders.AccessControlAllowOrigin)
        allowCredentials = true

        val host = environment.config.property("web.host").getString()
        host(host, schemes = listOf("http", "https"))
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
                val loginUser = userService.get(user.code)
                if (loginUser == null) {
                    call.respond(HttpStatusCode.NotFound, "User not found")
                    return@post
                }

                val hashedPassword = MessageDigest.getInstance("SHA-256")
                    .digest(user.password.toByteArray())
                    .joinToString(separator = "") { "%02x".format(it) }
                if (hashedPassword != loginUser.password) {
                    call.respond(HttpStatusCode.Unauthorized, "Invalid credentials")
                    return@post
                }
                call.respond(mapOf("token" to simpleJwt.sign(loginUser.code, loginUser.name, loginUser.role)))
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

            route("/role") {
                get {
                    val principal = call.principal<UserPrincipal>() ?: error("No principal")
                    call.respondText(principal.role)
                }
            }

            memberController()
            departmentController()
            teamController()
        }
    }
}


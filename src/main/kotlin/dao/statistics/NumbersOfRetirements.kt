package jp.co.anyplus.anyplab.webapp.membercards.dao.statistics

import org.jetbrains.exposed.sql.Table

object NumbersOfRetirements : Table("numbers_of_retirements") {
    val year = integer("year")
    val total = integer("total")
}
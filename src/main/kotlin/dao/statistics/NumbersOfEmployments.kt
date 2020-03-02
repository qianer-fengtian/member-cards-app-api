package jp.co.anyplus.anyplab.webapp.membercards.dao.statistics

import org.jetbrains.exposed.sql.Table

object NumbersOfEmployments : Table("numbers_of_employments") {
    val year = integer("year")
    val total = integer("total")
}
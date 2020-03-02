package jp.co.anyplus.anyplab.webapp.membercards.service

import jp.co.anyplus.anyplab.webapp.membercards.dao.statistics.NumbersOfEmployments
import jp.co.anyplus.anyplab.webapp.membercards.dao.statistics.NumbersOfRetirements
import jp.co.anyplus.anyplab.webapp.membercards.model.statistics.NumberOfEmployment
import jp.co.anyplus.anyplab.webapp.membercards.model.statistics.NumberOfRetirement
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class StatisticsService {
    fun getNumbersOfEmployments(): List<NumberOfEmployment> {
        var totals = listOf<NumberOfEmployment>()
        transaction {
            totals = NumbersOfEmployments.selectAll().map { row ->
                NumberOfEmployment(
                    year = row[NumbersOfEmployments.year],
                    total = row[NumbersOfEmployments.total]
                )
            }
        }
        return totals
    }

    fun getNumbersOfRetirements(): List<NumberOfRetirement> {
        var totals = listOf<NumberOfRetirement>()
        transaction {
            totals = NumbersOfRetirements.selectAll().map { row ->
                NumberOfRetirement(
                    year = row[NumbersOfRetirements.year],
                    total = row[NumbersOfRetirements.total]
                )
            }
        }
        return totals
    }
}
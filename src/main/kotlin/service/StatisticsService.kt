package jp.co.anyplus.anyplab.webapp.membercards.service

import jp.co.anyplus.anyplab.webapp.membercards.dao.members.VMembersDao
import jp.co.anyplus.anyplab.webapp.membercards.dao.statistics.NumbersOfEmploymentsDao
import jp.co.anyplus.anyplab.webapp.membercards.dao.statistics.NumbersOfRetirementsDao
import jp.co.anyplus.anyplab.webapp.membercards.model.members.Member
import jp.co.anyplus.anyplab.webapp.membercards.model.statistics.NumberOfEmployment
import jp.co.anyplus.anyplab.webapp.membercards.model.statistics.NumberOfRetirement
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime

class StatisticsService {
    fun getNumbersOfEmployments(): List<NumberOfEmployment> {
        var totals = listOf<NumberOfEmployment>()
        transaction {
            totals = NumbersOfEmploymentsDao.selectAll().map { row ->
                NumberOfEmployment(
                    year = row[NumbersOfEmploymentsDao.year],
                    total = row[NumbersOfEmploymentsDao.total]
                )
            }
        }
        return totals
    }

    fun getNumbersOfRetirements(): List<NumberOfRetirement> {
        var totals = listOf<NumberOfRetirement>()
        transaction {
            totals = NumbersOfRetirementsDao.selectAll().map { row ->
                NumberOfRetirement(
                    year = row[NumbersOfRetirementsDao.year],
                    total = row[NumbersOfRetirementsDao.total]
                )
            }
        }
        return totals
    }

    fun getMembersStatistics(): Map<String, Any> {
        var members = listOf<Member>()
        transaction {
            members = VMembersDao
                .select { VMembersDao.deleted.eq(false) }
                .map { Member.parseRow(it) }
        }

        val incumbents = members.filter { it.leftDate == null }
        val retirees = members.filter { it.leftDate != null }

        val statistics = hashMapOf(
            "total" to incumbents.count(),
            "maleTotal" to incumbents.filter { it.gender == "1" }.count(),
            "femaleTotal" to incumbents.filter { it.gender == "2" }.count(),
            "populationByAge" to incumbents.groupingBy { DateTime.now().year - it.birthDate.year }.eachCount(),
            "newGraduates" to incumbents.filter { it.joiningForm == "0" }.count(),
            "midCareers" to incumbents.filter { it.joiningForm == "1" }.count()
        )

        return statistics
    }
}
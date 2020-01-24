package jp.co.anyplus.anyplab.webapp.membercards.service

import jp.co.anyplus.anyplab.webapp.membercards.dao.Departments
import jp.co.anyplus.anyplab.webapp.membercards.model.Department
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import java.util.*

private fun parseRow(row: ResultRow) = Department(
    id = row[Departments.id],
    code = row[Departments.code],
    name = row[Departments.name],
    deleted = row[Departments.deleted],
    registeredDate = row[Departments.registeredDate],
    modifiedDate = row[Departments.modifiedDate]
)

class DepartmentService {
    fun getAll(): List<Department> {
        var departments = listOf<Department>()
        transaction {
            departments = Departments
                .select { Departments.deleted.eq(false) }
                .map { parseRow(it) }
                .sortedByDescending { Departments.registeredDate }
                .sortedBy { Departments.code }
        }
        return departments
    }

    fun get(id: UUID) : Department? {
        var department: Department? = null
        transaction {
            department = Departments
                .select { Departments.id.eq(id) }
                .map { parseRow(it) }[0]
        }
        return department ?: null
    }

    fun register(department: Department) : Unit {
        transaction {
            val tmp = Departments.insert {
                it[Departments.id] = UUID.randomUUID()
                it[Departments.code] = department.code
                it[Departments.name] = department.name
                it[bool("deleted")] = false
                it[date("registered_date")] = DateTime.now()
                it[date("modified_date")] = DateTime.now()
            }
        }
    }

    fun update(department: Department) : Unit {
        transaction {
            Departments.update({ Departments.id eq department.id }) {
                it[Departments.code] = department.code
                it[Departments.name] = department.name
                it[bool("deleted")] = department.deleted
                it[date("modified_date")] = DateTime.now()
            }
        }
    }
}
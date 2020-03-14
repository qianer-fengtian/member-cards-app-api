package jp.co.anyplus.anyplab.webapp.membercards.service

import jp.co.anyplus.anyplab.webapp.membercards.dao.departments.DepartmentsDao
import jp.co.anyplus.anyplab.webapp.membercards.model.departments.Department
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import java.util.*

class DepartmentService {
    fun getAll(): List<Department> {
        var departments = listOf<Department>()
        transaction {
            departments = DepartmentsDao
                .select { DepartmentsDao.deleted.eq(false) }
                .map { Department.parseRow(it) }
                .sortedByDescending { DepartmentsDao.registeredDate }
        }
        return departments
    }

    fun get(id: UUID) : Department? {
        var department: Department? = null
        transaction {
            department = DepartmentsDao
                .select { DepartmentsDao.id.eq(id) }
                .map { Department.parseRow(it) }[0]
        }
        return department ?: null
    }

    fun register(department: Department) : Unit {
        transaction {
            val tmp = DepartmentsDao.insert {
                it[id] = UUID.randomUUID()
                it[name] = department.name
                it[bool("deleted")] = false
                it[datetime("registered_date")] = DateTime.now()
                it[datetime("modified_date")] = DateTime.now()
            }
        }
    }

    fun update(department: Department) : Unit {
        transaction {
            DepartmentsDao.update({ DepartmentsDao.id eq department.id }) {
                it[name] = department.name
                it[bool("deleted")] = department.deleted
                it[datetime("modified_date")] = DateTime.now()
            }
        }
    }
}
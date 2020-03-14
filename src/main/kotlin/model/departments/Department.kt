package jp.co.anyplus.anyplab.webapp.membercards.model.departments

import jp.co.anyplus.anyplab.webapp.membercards.dao.departments.DepartmentsDao
import org.jetbrains.exposed.sql.ResultRow
import org.joda.time.DateTime
import java.util.*

class Department(
    val id: UUID,
    val name: String,
    val deleted: Boolean,
    val registeredDate: DateTime,
    val modifiedDate: DateTime
) {
    companion object {
        fun parseRow(row: ResultRow): Department {
            return Department(
                id = row[DepartmentsDao.id],
                name = row[DepartmentsDao.name],
                deleted = row[DepartmentsDao.deleted],
                registeredDate = row[DepartmentsDao.registeredDate],
                modifiedDate = row[DepartmentsDao.modifiedDate]
            )
        }
    }
}
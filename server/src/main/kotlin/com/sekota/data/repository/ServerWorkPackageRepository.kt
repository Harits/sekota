package com.sekota.data.repository

import com.sekota.data.database.DatabaseFactory.dbQuery
import com.sekota.data.database.SprintsTable
import com.sekota.data.database.WorkPackagesTable
import com.sekota.features.workpackage.domain.model.Sprint
import com.sekota.features.workpackage.domain.model.WorkPackage
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.time.LocalDate

class ServerWorkPackageRepository {
    suspend fun addSprint(sprint: Sprint): Unit = dbQuery {
        SprintsTable.insert {
            it[sprintName] = sprint.sprintName
            it[startDate] = LocalDate.parse(sprint.startDate)
            it[endDate] = LocalDate.parse(sprint.endDate)
        }
    }

    suspend fun getSprints(): List<Sprint> = dbQuery {
        SprintsTable.selectAll().map {
            Sprint(
                sprintName = it[SprintsTable.sprintName],
                startDate = it[SprintsTable.startDate].toString(),
                endDate = it[SprintsTable.endDate].toString()
            )
        }
    }

    suspend fun addWorkPackage(wp: WorkPackage): Unit = dbQuery {
        WorkPackagesTable.insert {
            it[localId] = wp.localId
            it[opId] = wp.opId
            it[subject] = wp.subject
            it[status] = wp.status
            it[lockVersion] = wp.lockVersion
            it[assignee] = wp.assignee
            it[sprintName] = wp.sprintName
        }
    }

    suspend fun getWorkPackagesForSprint(sprintName: String): List<WorkPackage> = dbQuery {
        WorkPackagesTable.selectAll().where { WorkPackagesTable.sprintName eq sprintName }.map {
            WorkPackage(
                localId = it[WorkPackagesTable.localId],
                opId = it[WorkPackagesTable.opId],
                subject = it[WorkPackagesTable.subject],
                status = it[WorkPackagesTable.status],
                lockVersion = it[WorkPackagesTable.lockVersion],
                assignee = it[WorkPackagesTable.assignee],
                sprintName = it[WorkPackagesTable.sprintName]
            )
        }
    }
}

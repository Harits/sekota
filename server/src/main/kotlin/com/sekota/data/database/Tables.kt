package com.sekota.data.database

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.date

object SprintsTable : Table("sprints") {
    val sprintName = varchar("sprint_name", 255)
    val startDate = date("start_date")
    val endDate = date("end_date")

    override val primaryKey = PrimaryKey(sprintName)
}

object WorkPackagesTable : Table("work_packages") {
    val localId = varchar("local_id", 255)
    val opId = integer("op_id").nullable()
    val subject = varchar("subject", 255)
    val status = varchar("status", 255)
    val lockVersion = integer("lock_version").nullable()
    val assignee = varchar("assignee", 255).nullable()
    
    val sprintName = varchar("sprint_name", 255).references(SprintsTable.sprintName)

    override val primaryKey = PrimaryKey(localId)
}

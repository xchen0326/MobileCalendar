package com.kizitonwose.mobilecalendar

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "table_student")
data class Student(
//                   @PrimaryKey val id: Int = 0,
    @PrimaryKey var username: String = "",
    @ColumnInfo(name = "password") var password: String = "",
    @ColumnInfo(name = "course") var course: String = ""
)
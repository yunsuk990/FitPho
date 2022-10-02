package com.example.fitpho.Database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity
data class GuideEntity(
    @PrimaryKey var id: Int,
    var title: String,
    var img1: String,
    var img2: String
)

@Entity(tableName = "GuideDetailEntity",
    foreignKeys = [
        ForeignKey(
            entity= GuideEntity::class,
            parentColumns = ["id"],
            childColumns = ["id"],
        )
    ]
)
data class GuideDetailEntity(
    @PrimaryKey var id: Int,
    var stimulate1: String,
    var stimulate2: String,
    var text: String
)
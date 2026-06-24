package com.llsit.joinsphere.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.llsit.joinsphere.core.model.Category

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey val id: String,
    val label: String,
    val emoji: String
)

fun CategoryEntity.toExternalModel() = Category(
    id = id,
    label = label,
    emoji = emoji
)

fun Category.toEntity() = CategoryEntity(
    id = id,
    label = label,
    emoji = emoji
)

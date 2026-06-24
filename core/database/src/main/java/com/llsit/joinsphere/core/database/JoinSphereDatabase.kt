package com.llsit.joinsphere.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.llsit.joinsphere.core.database.dao.CategoryDao
import com.llsit.joinsphere.core.database.entity.CategoryEntity

@Database(
    entities = [CategoryEntity::class],
    version = 1,
    exportSchema = false
)
abstract class JoinSphereDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
}

package com.sayed.bandhan.Data
import androidx.room.Entity
import androidx.room.PrimaryKey



@Entity(tableName = "users")
data class User(
    @PrimaryKey val username: String,
    val passwordHash: String // Storing a hash is safer than a plain-text password
)

package com.example.hunrmand.data.util

import androidx.sqlite.db.SupportSQLiteDatabase
import java.util.UUID

object DatabaseSeeder {
    fun seed(db: SupportSQLiteDatabase) {
        // Pre-calculated hash for "password" (using simple hash or placeholder if SecurityUtils not available here)
        // Assuming SecurityUtils.hashPassword("password") -> "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8" (SHA-256 for 'password')
        val passwordHash = "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8" 
        
        val workers = listOf(
            Triple("Ahmed Ali", "Electrician", Pair(32.9328, 71.5365)), // Mianwali
            Triple("Bilal Khan", "Plumber", Pair(31.5204, 74.3587)), // Lahore
            Triple("Usman Gondal", "AC Repair", Pair(33.6844, 73.0479)), // Islamabad
            Triple("Hamza Shah", "Painter", Pair(31.5204, 74.3587)), // Lahore
            Triple("Zain Malik", "Electrician", Pair(32.9328, 71.5365)) // Mianwali-ish
        )

        workers.forEach { (name, profession, loc) ->
            val id = UUID.randomUUID().toString()
            val email = "${name.replace(" ", "").lowercase()}@example.com"
            // Ensure table name matches UserEntity "users"
            // Columns: id, username, email, passwordHash, role, profession, rating, hourlyRate, latitude, longitude
            db.execSQL("""
                INSERT INTO users (id, username, email, passwordHash, role, profession, rating, hourlyRate, latitude, longitude)
                VALUES ('$id', '$name', '$email', '$passwordHash', 'WORKER', '$profession', 4.5, '1000', ${loc.first}, ${loc.second})
            """.trimIndent())
        }
    }
}

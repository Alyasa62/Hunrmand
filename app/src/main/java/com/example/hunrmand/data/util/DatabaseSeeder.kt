package com.example.hunrmand.data.util

import androidx.sqlite.db.SupportSQLiteDatabase
import java.util.UUID

object DatabaseSeeder {
    fun seed(db: SupportSQLiteDatabase) {
        val passwordHash = "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8" // "password"

        // 1. Users
        // Admin
        val adminId = "admin_01"
        db.execSQL("""
            INSERT INTO users (id, username, email, passwordHash, role, profession, rating, hourlyRate, latitude, longitude)
            VALUES ('$adminId', 'Admin User', 'admin@example.com', '$passwordHash', 'ADMIN', NULL, 0.0, NULL, NULL, NULL)
        """.trimIndent())

        // Client (Ali Client)
        val clientId = "client_01"
        db.execSQL("""
            INSERT INTO users (id, username, email, passwordHash, role, profession, rating, hourlyRate, latitude, longitude)
            VALUES ('$clientId', 'Ali Client', 'ali@example.com', '$passwordHash', 'USER', NULL, 0.0, NULL, 32.5839, 71.5370)
        """.trimIndent())

        // Worker (Bilal Plumber - Mianwali)
        val workerId = "worker_01"
        db.execSQL("""
            INSERT INTO users (id, username, email, passwordHash, role, profession, rating, hourlyRate, latitude, longitude)
            VALUES ('$workerId', 'Bilal Plumber', 'bilal@example.com', '$passwordHash', 'WORKER', 'Plumber', 4.8, '1500', 32.5839, 71.5370)
        """.trimIndent())

        // 2. Jobs
        val now = System.currentTimeMillis()

        // Job 1: Plumber, Mianwali (Should match Bilal)
        db.execSQL("""
            INSERT INTO jobs (id, title, description, budget, creatorId, createdAt, userId, category, latitude, longitude, address)
            VALUES ('job_01', 'Fix Leaky Tap', 'Kitchen tap is leaking badly.', 2000.0, '$clientId', $now, '$clientId', 'Plumber', 32.5839, 71.5370, 'Mianwali, Punjab')
        """.trimIndent())

        // Job 2: Plumber, Lahore (Too far from Bilal)
        db.execSQL("""
            INSERT INTO jobs (id, title, description, budget, creatorId, createdAt, userId, category, latitude, longitude, address)
            VALUES ('job_02', 'Bathroom Pipe Burst', 'Urgent repair needed in Lahore.', 5000.0, '$clientId', $now, '$clientId', 'Plumber', 31.5204, 74.3587, 'Lahore, Punjab')
        """.trimIndent())

        // Job 3: Electrician, Mianwali (Wrong Category)
        db.execSQL("""
            INSERT INTO jobs (id, title, description, budget, creatorId, createdAt, userId, category, latitude, longitude, address)
            VALUES ('job_03', 'Install Ceiling Fan', 'Need to install a new fan.', 1000.0, '$clientId', $now, '$clientId', 'Electrician', 32.5839, 71.5370, 'Mianwali, Punjab')
        """.trimIndent())
    }
}

package com.jawabdulu.app.models

import android.graphics.drawable.Drawable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "apps"
)
data class AppModel(
    @PrimaryKey(autoGenerate = true)
    var id : Int?,
    var namaAplikasi: String,
    var iconAplikasi: Drawable,
    var locked: Boolean,
    var packageName: String
)

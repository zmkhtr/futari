package com.jawabdulu.app.models

import android.graphics.drawable.Drawable
import androidx.room.Entity
import androidx.room.PrimaryKey


data class App(
    var namaAplikasi: String,
    var iconAplikasi: Drawable,
    var locked: Boolean,
    var packageName: String,
    var isSystemPackage: Boolean
)

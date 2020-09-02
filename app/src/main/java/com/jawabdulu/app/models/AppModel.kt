package com.jawabdulu.app.models

import android.graphics.drawable.Drawable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "apps"
)
data class AppModel(
    @PrimaryKey
    var packageName: String,
    var pass : String
)

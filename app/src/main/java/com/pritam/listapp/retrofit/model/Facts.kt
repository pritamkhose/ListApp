package com.pritam.listapp.retrofit.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// Fact Data Class
data class Facts(
    var title: String?,
    var rows: MutableList<Fact>?,
    // extra parameter taken for error handling
    var error: String
)

// Fact Data Class
@Entity
data class Fact(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var title: String? = null,
    var description: String? = null,
    var imageHref: String? = null
)
package com.pritam.listapp.retrofit.model

// Fact Data Class
data class Facts(
    var title: String,
    var rows : MutableList<Fact>
)

data class Fact(
    var title: String? = null,
    var description: String? = null,
    var imageHref: String? = null
)
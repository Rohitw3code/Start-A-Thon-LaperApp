package com.lapperapp.laper.Model

data class ProjectWorkModel(
    var cat: String,
    var desc: String,
    var subCat: List<String>,
    var tags: List<String>
)


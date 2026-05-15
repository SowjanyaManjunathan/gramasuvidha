package com.gramasuvidha.portal.data

data class Project(
    val id: Int = 0,
    val firestoreId: String = "",
    val titleEn: String = "",
    val titleKn: String = "",
    val descriptionEn: String = "",
    val descriptionKn: String = "",
    val category: String = "",
    val budget: String = "",
    val progress: Int = 0,
    val status: String = "Planned",
    val expectedCompletion: String = "",
    val contractor: String = "",
    val ward: String = "",
    val beforePhotoBase64: String = "",
    val afterPhotoBase64: String = "",
    val userRating: Int = 0,
    val issueReported: Boolean = false
)

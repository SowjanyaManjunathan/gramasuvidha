package com.gramasuvidha.portal.firebase

//import android.app.DownloadManager
//import androidx.compose.foundation.text.selection.Direction
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.gramasuvidha.portal.data.MockDataSource
import com.gramasuvidha.portal.data.Project
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import com.google.firebase.firestore.firestore
import com.google.firebase.Firebase

object ProjectRepository {

    // Direct reference — no FirebaseManager needed, avoids any import issues
    private val db: FirebaseFirestore get() = FirebaseFirestore.getInstance()
    private val col get() = db.collection("projects")

    /** Real-time Flow — updates automatically when Firestore data changes */
    fun observeProjects(): Flow<List<Project>> = callbackFlow {
        val listener = col
            .orderBy("updatedAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val list = snapshot?.documents?.mapNotNull { doc ->
                    doc.toProject()
                } ?: emptyList()
                trySend(list)
            }
        awaitClose { listener.remove() }
    }

    /** Seed Firestore with 6 sample projects — tap button once on first run */
    suspend fun seedMockProjects(): Result<Unit> = runCatching {
        val batch = db.batch()
        MockDataSource.getProjects().forEach { project ->
            val ref = col.document(project.id.toString())
            batch.set(ref, project.toMap())
        }
        batch.commit().await()
    }

    /** Save Base64 photo string back to the Firestore document */
    suspend fun updatePhotoUrl(
        projectId: String,
        type: String,
        value: String
    ): Result<Unit> = runCatching {
        col.document(projectId)
            .update("${type}PhotoBase64", value)
            .await()
    }

    // ── Private mappers ───────────────────────────────────────────────────────

    private fun DocumentSnapshot.toProject(): Project? = try {
        Project(
            id                 = (getString("id") ?: id).toIntOrNull() ?: 0,
            firestoreId        = id,
            titleEn            = getString("titleEn") ?: "",
            titleKn            = getString("titleKn") ?: "",
            descriptionEn      = getString("descriptionEn") ?: "",
            descriptionKn      = getString("descriptionKn") ?: "",
            category           = getString("category") ?: "",
            budget             = getString("budget") ?: "",
            progress           = (getLong("progress") ?: 0L).toInt(),
            status             = getString("status") ?: "Planned",
            expectedCompletion = getString("expectedCompletion") ?: "",
            contractor         = getString("contractor") ?: "",
            ward               = getString("ward") ?: "",
            beforePhotoBase64  = getString("beforePhotoBase64") ?: "",
            afterPhotoBase64   = getString("afterPhotoBase64") ?: ""
        )
    } catch (e: Exception) {
        null
    }

    private fun Project.toMap(): Map<String, Any> = mapOf(
        "id"                 to id.toString(),
        "titleEn"            to titleEn,
        "titleKn"            to titleKn,
        "descriptionEn"      to descriptionEn,
        "descriptionKn"      to descriptionKn,
        "category"           to category,
        "budget"             to budget,
        "progress"           to progress,
        "status"             to status,
        "expectedCompletion" to expectedCompletion,
        "contractor"         to contractor,
        "ward"               to ward,
        "beforePhotoBase64"  to "",
        "afterPhotoBase64"   to "",
        "updatedAt"          to System.currentTimeMillis()
    )
}

package com.gramasuvidha.portal.firebase

import com.gramasuvidha.portal.firebase.FirebaseManager
import com.gramasuvidha.portal.firebase.FirebaseManager.auth
import com.gramasuvidha.portal.firebase.FirebaseManager.db
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import com.gramasuvidha.portal.firebase.FirebaseManager.Collections
import com.gramasuvidha.portal.firebase.FirebaseManager.db
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

object StorageRepository {

    suspend fun uploadProjectPhoto(
        context: Context,
        projectId: String,
        imageUri: Uri,
        type: String
    ): Result<String> = runCatching {
        val base64String = withContext(Dispatchers.IO) {
            compressToBase64(context, imageUri)
        }
        db.collection(FirebaseManager.Collections.PROJECTS)
            .document(projectId)
            .update("${type}PhotoBase64", base64String)
            .await()
        base64String
    }

    private fun compressToBase64(context: Context, uri: Uri): String {
        val stream = context.contentResolver.openInputStream(uri)
        val original = BitmapFactory.decodeStream(stream)
        stream?.close()
        val max = 400
        val scale = minOf(max.toFloat() / original.width, max.toFloat() / original.height, 1f)
        val scaled = if (scale < 1f)
            Bitmap.createScaledBitmap(
                original,
                (original.width * scale).toInt(),
                (original.height * scale).toInt(),
                true
            )
        else original
        val out = ByteArrayOutputStream()
        scaled.compress(Bitmap.CompressFormat.JPEG, 60, out)
        return Base64.encodeToString(out.toByteArray(), Base64.DEFAULT)
    }
}

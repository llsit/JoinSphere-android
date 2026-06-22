package com.llsit.joinsphere.core.domain.usecase

import android.content.Context
import android.net.Uri
import com.llsit.joinsphere.core.domain.repository.ProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UploadImageProfileUseCase(
    private val repository: ProfileRepository,
    private val context: Context
) {
    suspend operator fun invoke(uri: Uri): Result<String> = runCatching {
        val bytes = withContext(Dispatchers.IO) {
            val inputStream = context.contentResolver.openInputStream(uri)
                ?: throw Exception("ไม่สามารถเปิดไฟล์รูปภาพได้")
            val readBytes = inputStream.readBytes()
            inputStream.close()
            readBytes
        }
        
        repository.updateImageProfile(bytes).getOrThrow()
    }
}
package com.llsit.joinsphere.core.domain.usecase

import android.content.Context
import android.net.Uri
import com.llsit.joinsphere.core.domain.repository.ProfileRepository

class UploadImageProfileUseCase(
    private val repository: ProfileRepository,
    private val context: Context
) {
    suspend operator fun invoke(uri: Uri): Result<String> = runCatching {
        val inputStream = context.contentResolver.openInputStream(uri)
            ?: throw Exception("ไม่สามารถเปิดไฟล์รูปภาพได้")
        val bytes = inputStream.readBytes()
        inputStream.close()
        
        repository.updateImageProfile(bytes).getOrThrow()
    }
}
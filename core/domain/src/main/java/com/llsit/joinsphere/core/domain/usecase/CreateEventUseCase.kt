package com.llsit.joinsphere.core.domain.usecase

import android.content.Context
import android.net.Uri
import com.llsit.joinsphere.core.domain.repository.EventRepository
import com.llsit.joinsphere.core.domain.repository.UserDataRepository
import com.llsit.joinsphere.core.model.EventDto
import kotlinx.coroutines.flow.first
import java.util.UUID

class CreateEventUseCase(
    private val eventRepository: EventRepository,
    private val userDataRepository: UserDataRepository,
    private val context: Context
) {
    suspend operator fun invoke(eventData: EventDto, localImageUri: Uri?): Result<Unit> =
        runCatching {
            val currentUserId =
                userDataRepository.authToken.first() ?: throw Exception("ไม่พบสิทธิ์ผู้ใช้งาน")

            val eventId = UUID.randomUUID().toString()

            val onlineImageUrl = if (localImageUri != null) {
                val inputStream = context.contentResolver.openInputStream(localImageUri)
                    ?: throw Exception("ไม่สามารถอ่านไฟล์รูปภาพได้")
                val bytes = inputStream.readBytes()
                inputStream.close()

                eventRepository.uploadCoverImage(currentUserId, eventId, bytes).getOrThrow()
            } else {
                "https://images.unsplash.com/photo-1552674605-db6ffd4facb5?w=600&h=260&fit=crop&auto=format"
            }

            val finalEvent = eventData.copy(
                creatorId = currentUserId,
                coverImageUrl = onlineImageUrl
            )

            eventRepository.createEvent(finalEvent).getOrThrow()
        }
}
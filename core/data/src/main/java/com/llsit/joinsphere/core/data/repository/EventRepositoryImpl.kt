package com.llsit.joinsphere.core.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.llsit.joinsphere.core.domain.repository.EventRepository
import com.llsit.joinsphere.core.model.EventDto
import kotlinx.coroutines.tasks.await
import java.util.UUID

class EventRepositoryImpl(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) : EventRepository {

    override suspend fun createEvent(event: EventDto): Result<Unit> = runCatching {
        val documentRef = firestore.collection("events").document()
        val finalEvent = event.copy(id = documentRef.id)
        documentRef.set(finalEvent).await()
    }

    override suspend fun uploadCoverImage(imageByteArray: ByteArray): Result<String> = runCatching {
        val fileName = UUID.randomUUID().toString()
        val storageRef = storage.reference.child("event_covers/$fileName.jpg")
        
        storageRef.putBytes(imageByteArray).await()
        storageRef.downloadUrl.await().toString()
    }
}
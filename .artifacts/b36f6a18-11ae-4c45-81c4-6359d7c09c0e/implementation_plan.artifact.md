# Implementation Plan - Past Events Feature

Implement the backend-to-frontend flow for fetching and displaying past events in the "My Events" screen.

## Proposed Changes

### core:domain

#### [MODIFY] [EventRepository.kt](file:///C:/Users/Nuttapong/AndroidStudioProjects/JoinSphere/core/domain/src/main/java/com/llsit/joinsphere/core/domain/repository/EventRepository.kt)
- Add `suspend fun getPastEvents(userId: String): Result<List<AttendingEvent>>` to the interface.

#### [NEW] [GetPastEventsUseCase.kt](file:///C:/Users/Nuttapong/AndroidStudioProjects/JoinSphere/core/domain/src/main/java/com/llsit/joinsphere/core/domain/usecase/GetPastEventsUseCase.kt)
- Create a use case that takes `EventRepository` and `UserDataRepository` to fetch past events for the current user.

#### [MODIFY] [DomainModule.kt](file:///C:/Users/Nuttapong/AndroidStudioProjects/JoinSphere/core/domain/src/main/java/com/llsit/joinsphere/core/domain/di/DomainModule.kt)
- Register `GetPastEventsUseCase` in the Koin module.

### core:data

#### [MODIFY] [EventRepositoryImpl.kt](file:///C:/Users/Nuttapong/AndroidStudioProjects/JoinSphere/core/data/src/main/java/com/llsit/joinsphere/core/data/repository/EventRepositoryImpl.kt)
- Implement `getPastEvents` using Supabase RPC `get_past_events`.

### feature:myevents

#### [MODIFY] [MyEventsViewModel.kt](file:///C:/Users/Nuttapong/AndroidStudioProjects/JoinSphere/feature/myevents/src/main/java/com/llsit/joinsphere/feature/myevents/MyEventsViewModel.kt)
- Inject `GetPastEventsUseCase`.
- Add `loadPastEvents()` method and call it in `init`.

#### [MODIFY] [MyEventsScreen.kt](file:///C:/Users/Nuttapong/AndroidStudioProjects/JoinSphere/feature/myevents/src/main/java/com/llsit/joinsphere/feature/myevents/MyEventsScreen.kt)
- Update "Past" tab to use `uiState.pastEvents`.
- Update `PastEventCard` to accept `AttendingEvent` and use real data.
- Remove `MOCK_PAST` and the local `PastEvent` data class.

## Verification Plan

### Automated Tests
- Run `:feature:myevents:compileDebugKotlin` to ensure no regression.

### Manual Verification
- Deploy the app.
- Navigate to "My Events" -> "Past" tab.
- Verify that past events are loaded from the repository and displayed correctly.

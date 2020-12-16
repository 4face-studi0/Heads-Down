package studio.forface.headsdown.data

import arrow.core.Either
import kotlinx.coroutines.flow.Flow
import studio.forface.headsdown.model.App
import studio.forface.headsdown.model.AppSettings
import studio.forface.headsdown.model.AppWithSettings

interface AppRepository {

    fun allApps(): Flow<List<AppWithSettings>>
    suspend fun updateAppSettings(app: App, settings: AppSettings)
}

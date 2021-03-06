package studio.forface.headsdown

import android.content.Context
import androidx.datastore.preferences.createDataStore
import co.touchlab.kermit.LogcatLogger
import co.touchlab.kermit.Logger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import studio.forface.headsdown.data.AndroidAppSource
import studio.forface.headsdown.data.AppRepository
import studio.forface.headsdown.data.AppRepositoryImpl
import studio.forface.headsdown.data.SettingsSource
import studio.forface.headsdown.notifications.NotificationAccessVerifier
import studio.forface.headsdown.usagestats.UsageStatsAccessVerifier
import studio.forface.headsdown.usecase.*
import studio.forface.headsdown.viewmodel.AppViewModel

val dataModule = module {

    factory<AppRepository> { AppRepositoryImpl(androidAppSource = get(), settingsSource = get()) }

    // Apps
    single { AndroidAppSource(packageManager = get()) }
    single { get<Context>().packageManager }

    // Notifications
    single { NotificationAccessVerifier(context = get()) }

    // UsageStats
    single { UsageStatsAccessVerifier(context = get()) }

    // Settings
    single { SettingsSource(dataStore = get()) }
    single { get<Context>().createDataStore("settings") }
}

val useCaseModule = module {

    factory { AddToShouldBlockHeadsUp(repo = get()) }
    factory { GetAllApps(repo = get()) }
    factory { GetAllBlockingHeadsUpApps(repo = get()) }
    factory { GetAllNonSystemApps(repo = get()) }
    factory { RemoveFromShouldBlockHeadsUp(repo = get()) }
}

val appModule = module {

    viewModel {
        AppViewModel(
            logger = get(),
            getAllNonSystemApps = get(),
            addToShouldBlockHeadsUp = get(),
            removeFromShouldBlockHeadsUp = get(),
            notificationAccessVerifier = get(),
            usageStatsAccessVerifier = get()
        )
    }

    single<Logger> { LogcatLogger() }

} + useCaseModule + dataModule

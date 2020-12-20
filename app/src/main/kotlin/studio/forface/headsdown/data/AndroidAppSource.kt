package studio.forface.headsdown.data

import android.annotation.SuppressLint
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import studio.forface.headsdown.model.App
import studio.forface.headsdown.model.PackageName
import java.util.*


class AndroidAppSource(
    private val packageManager: PackageManager
) {

    fun allApps(): List<App> {
        @SuppressLint("QueryPermissionsNeeded")
        val packages = packageManager.getInstalledPackages(PackageManager.GET_META_DATA)

        return packages.filterNot(::isSystemPackage).map { packageInfo ->
            val appName = packageInfo.applicationInfo.loadLabel(packageManager)
            val packageName = packageInfo.applicationInfo.packageName
            val icon = packageInfo.applicationInfo.loadIcon(packageManager)
            App(appName.toString(), PackageName(packageName), icon)
        }.sortedBy { it.appName.toLowerCase(Locale.getDefault()) }
    }

    private fun isSystemPackage(packageInfo: PackageInfo): Boolean =
        packageInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
}

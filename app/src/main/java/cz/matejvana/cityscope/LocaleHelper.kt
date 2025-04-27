package cz.matejvana.cityscope

import android.content.Context

fun getCurrentLocale(context: Context): String {
    return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
        context.resources.configuration.locales[0].language
    } else {
        context.resources.configuration.locale.language
    }
}
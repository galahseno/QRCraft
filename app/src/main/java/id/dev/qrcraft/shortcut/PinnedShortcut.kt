package id.dev.qrcraft.shortcut

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.os.Build
import androidx.core.content.getSystemService
import id.dev.home.presentation.model.QrTypeIdentifier
import id.dev.qrcraft.MainActivity
import id.dev.qrcraft.R

fun addPinnedShortcut(context: Context) {
    if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
        return
    }

    val shortcutManager = context.getSystemService<ShortcutManager>()!!
    if(shortcutManager.isRequestPinShortcutSupported) {
        val shortcut = ShortcutInfo.Builder(context, "pinned_text_qr")
            .setShortLabel("Text shortcut")
            .setLongLabel("This text shortcut is about navigating user to text qr code generating screen")
            .setIcon(Icon.createWithResource(
                context, R.drawable.outline_title_24
            ))
            .setIntent(
                Intent(context, MainActivity::class.java).apply {
                    action = Intent.ACTION_VIEW
                    putExtra("qr_type", QrTypeIdentifier.TEXT.name)
                }
            )
            .build()

        val callbackIntent = shortcutManager.createShortcutResultIntent(shortcut)
        val successPendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            callbackIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        shortcutManager.requestPinShortcut(shortcut, successPendingIntent.intentSender)
    }
}
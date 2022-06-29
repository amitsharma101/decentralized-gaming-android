package land.kho.meta.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat

object EasyPermission {
    fun requestPermission(
        context: Context,
        permissions: Array<String>,
        permissionRequest: ActivityResultLauncher<Array<String>>,
        callback: (isGranted: Boolean) -> Unit
    ) {
        var granted = true
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                granted = false
                break
            }
        }
        if (granted) {
            callback(true)
        } else {
            permissionRequest.launch(
                permissions
            )
        }

    }

    fun resultLauncher(
        result: ActivityResultLauncher<Intent>,
        intent: Intent
    ) {
        result.launch(intent)
    }


}
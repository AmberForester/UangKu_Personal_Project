package com.example.uangku.feature.settings.presentation.component

import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import com.example.uangku.feature.backup.presentation.BackupEvent
import com.example.uangku.feature.backup.presentation.BackupViewModel
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.common.api.ApiException

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun exportLauncher (
    backupViewModel: BackupViewModel
): ManagedActivityResultLauncher<String, Uri?> {
    val exportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument(
            "application/json"
        )
    ) { uri ->
        Log.d("BACK UP JSON: ", "launcher activated")
        if (uri != null) {
            backupViewModel.onEvent(
                BackupEvent.ExportFile(uri)
            )
        }
    }
    return exportLauncher
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun importLauncher (
    backupViewModel: BackupViewModel
): ManagedActivityResultLauncher<Array<String>, Uri?> {
    val importLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        if(uri != null){
            backupViewModel.onEvent(
                BackupEvent.ImportFile(uri)
            )
        }
    }
    return importLauncher
}

@Composable
fun googleDriveAuthLauncher(
    context: Context
): ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult> {
    val googleDriveAuthorizationLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartIntentSenderForResult()
        ) { activityResult ->
            try {
                val authorizationResult =
                    Identity.getAuthorizationClient(context)
                        .getAuthorizationResultFromIntent(activityResult.data)

                Toast.makeText(
                    context,
                    "Google Drive berhasil terhubung!",
                    Toast.LENGTH_LONG
                ).show()
            } catch (e: ApiException) {
                Toast.makeText(
                    context,
                    "Gagal menghubungkan Google Drive",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    return googleDriveAuthorizationLauncher
}
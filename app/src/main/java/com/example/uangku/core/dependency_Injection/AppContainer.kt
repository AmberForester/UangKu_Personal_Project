package com.example.uangku.core.dependency_Injection

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Room
import com.example.uangku.core.database.AppDatabase
import com.example.uangku.core.database.DatabaseCallback
import com.example.uangku.feature.backup.data.automatic.AutoBackupPreferences
import com.example.uangku.feature.backup.data.automatic.AutoBackupScheduler
import com.example.uangku.feature.backup.data.automatic.AutoBackupWorkerFactory
import com.example.uangku.feature.google_drive.data.GoogleDriveAuth
import com.example.uangku.feature.google_drive.data.GoogleDriveRepositoryImpl
import com.example.uangku.feature.backup.data.manual.BackupRepositoryImpl
import com.example.uangku.feature.backup.data.manual.FileManagerImpl
import com.example.uangku.feature.backup.data.manual.JsonSerializerImpl
import com.example.uangku.feature.backup.domain.automatic.AutoBackupSettingsRepositoryImpl
import com.example.uangku.feature.backup.domain.automatic.AutoBackupSettingsUseCase
import com.example.uangku.feature.backup.domain.automatic.AutoBackupUseCase
import com.example.uangku.feature.backup.domain.manual.BackupUseCase
import com.example.uangku.feature.budget.data.BudgetRepoImpl
import com.example.uangku.feature.budget.domain.BudgetUseCase
import com.example.uangku.feature.category.data.CategoryRepoImpl
import com.example.uangku.feature.category.domain.CategoryUseCase
import com.example.uangku.feature.google_drive.domain.GoogleDriveUseCase
import com.example.uangku.feature.period.data.PeriodDataStore
import com.example.uangku.feature.period.data.PeriodRepositoryImpl
import com.example.uangku.feature.period.domain.PeriodUseCase
import com.example.uangku.feature.transaction.data.TransactionRepoImpl
import com.example.uangku.feature.transaction.domain.TransactionUseCase

@RequiresApi(Build.VERSION_CODES.O)
class AppContainer (
    context: Context
) {
    private val database: AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "uangku_database"
        )
        .addCallback(DatabaseCallback())
        .build()

    //period
    private val periodDataStore = PeriodDataStore(context)
    private val periodRepository = PeriodRepositoryImpl(periodDataStore)
    val periodUseCase = PeriodUseCase(periodRepository)

    // category
    private val categoryDao = database.categoryDao()
    private val categoryRepository = CategoryRepoImpl(categoryDao)
    val categoryUseCase = CategoryUseCase(categoryRepository)

    //transaction
    private val transactionDao = database.transactionDao()
    private val transactionRepository = TransactionRepoImpl(transactionDao)
    val transactionUseCase = TransactionUseCase(transactionRepository, periodUseCase)

    //budget
    private val budgetDao = database.budgetDao()
    private val budgetRepository = BudgetRepoImpl(budgetDao)
    val budgetUseCase = BudgetUseCase(budgetRepository, transactionRepository, categoryRepository, periodUseCase)

    // manual back up and restore
    private val backupFileManager = FileManagerImpl(
        contentResolver = context.contentResolver
    )
    private val backUpRepository = BackupRepositoryImpl(database)
    private val jsonSerializer = JsonSerializerImpl()
    val backupUseCase = BackupUseCase(backUpRepository, jsonSerializer, backupFileManager )

    // google drive
    val googleDriveAuth = GoogleDriveAuth()
    private val googleDriveRepository = GoogleDriveRepositoryImpl(
        context = context,
        googleDriveAuth = googleDriveAuth,
    )
    val googleDriveUseCase = GoogleDriveUseCase(
        googleDriveRepository = googleDriveRepository
    )

    // automatic back up
    private val autoBackupDataStore = AutoBackupPreferences(context)
    private val autoBackupUseCase = AutoBackupUseCase(
        backupUseCase = backupUseCase,
        googleDriveRepository = googleDriveRepository
    )
    private val autoBackupScheduler = AutoBackupScheduler(context)
    val autoBackupWorkerFactory = AutoBackupWorkerFactory(
        autoBackupUseCase = autoBackupUseCase,
        autoBackupPreferences = autoBackupDataStore,
        autoBackupScheduler = autoBackupScheduler
    )
    private val autoBackupSettingsRepository = AutoBackupSettingsRepositoryImpl(
        preferences = autoBackupDataStore
    )
    val autoBackupSettingsUseCase = AutoBackupSettingsUseCase(
        repository = autoBackupSettingsRepository,
        scheduler = autoBackupScheduler
    )
}
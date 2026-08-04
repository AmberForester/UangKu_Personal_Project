package com.example.uangku.feature.transaction.data

import com.example.uangku.feature.transaction.domain.Transaction

fun TransactionView.toDomain(): Transaction {
    return Transaction(
        id = id,
        type = type,
        description = description,
        amount = amount,
        categoryId = categoryId,
        categoryName = categoryName,
        date = date,
    )
}

fun Transaction.toEntity(): TransactionEntity {
    return TransactionEntity(
        id = id ?: 0,
        type = type,
        description = description,
        amount = amount,
        categoryId = categoryId,
        date = date,
    )
}
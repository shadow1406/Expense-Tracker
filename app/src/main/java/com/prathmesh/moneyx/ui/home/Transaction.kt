package com.prathmesh.moneyx.ui.home

import android.os.Parcelable
import com.google.firebase.firestore.DocumentId
import kotlinx.parcelize.Parcelize

@Parcelize
data class Transaction(
    @DocumentId
    val documentId: String = "",
    val categoryId: Int = 0,
    val amount: String = "",
    val time: String = "",
    val date: String = "",
    val description: String = "",
    val type: String = "",
) : Parcelable

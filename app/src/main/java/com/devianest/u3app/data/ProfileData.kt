// ProfileData.kt - Data class untuk menyimpan data profile
package com.devianest.u3app.data

import android.net.Uri
import android.provider.ContactsContract.CommonDataKinds.Phone

data class ProfileData(
    val nama: String = "",
    val email: String = "",
    val nim: String = "",
    val prodi: String = "",
    val semester: String = "5",
    val profileImage: Uri? = null,
    val phone: String = ""
)


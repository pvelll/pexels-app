package com.pvelll.newpexelsapp.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.io.Serializable


@Parcelize
@Entity(tableName = "photos")
data class Photo(
    val alt: String,
    val avgColor: String,
    val height: Int,
    @PrimaryKey val id: Int,
    val liked: Boolean,
    val photographer: String,
    val photographerId: Int,
    val photographerUrl: String,
    val photoFormat: PhotoFormat,
    val url: String,
    val width: Int
): Serializable, Parcelable

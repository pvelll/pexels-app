package com.pvelll.newpexelsapp.domain.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.io.Serializable


@Parcelize
@Entity(tableName = "photos")
data class Photo(
    val alt: String,
    val avg_color: String,
    val height: Int,
    @PrimaryKey val id: Int,
    val liked: Boolean,
    val photographer: String,
    val photographer_id: Int,
    val photographer_url: String,
    val src: Src,
    val url: String,
    val width: Int
): Serializable, Parcelable

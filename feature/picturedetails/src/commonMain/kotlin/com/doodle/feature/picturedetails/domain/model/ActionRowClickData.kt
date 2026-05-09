package com.doodle.feature.picturedetails.domain.model

import com.doodle.core.domain.enums.ActionType
import com.doodle.core.domain.model.remote.RemoteImage

data class ActionRowClickData(
    val type: ActionType,
    val image: RemoteImage.Hit,
    val diskCacheKey: String?
)

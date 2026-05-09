package com.doodle.core.domain.enums

enum class RemoveAdsStatus {
    PURCHASED, NOT_PURCHASED
}

fun RemoveAdsStatus.isNotPurchased() = this != RemoveAdsStatus.PURCHASED

fun RemoveAdsStatus.isPurchased() = this == RemoveAdsStatus.PURCHASED

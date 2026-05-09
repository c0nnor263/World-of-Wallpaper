package com.doodle.core.billing.domain.enums

import com.android.billingclient.api.BillingClient

enum class BillingProductType(val id: String, val productType: String) {
    REMOVE_ADS("remove_ads", BillingClient.ProductType.INAPP)
}

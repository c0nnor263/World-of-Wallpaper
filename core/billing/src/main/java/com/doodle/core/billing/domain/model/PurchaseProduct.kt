package com.doodle.core.billing.domain.model

import com.doodle.core.billing.domain.enums.BillingProductType
import com.doodle.core.billing.domain.enums.VerifyResult

data class PurchaseProduct(
    val type: BillingProductType? = null,
    val result: VerifyResult? = null
)

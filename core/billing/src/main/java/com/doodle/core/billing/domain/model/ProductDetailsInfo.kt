package com.doodle.core.billing.domain.model

import com.android.billingclient.api.ProductDetails
import kotlinx.collections.immutable.ImmutableList

data class ProductDetailsInfo(
    val inAppDetails: ImmutableList<ProductDetails>? = null
)

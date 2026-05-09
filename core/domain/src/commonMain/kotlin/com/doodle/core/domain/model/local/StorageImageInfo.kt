package com.doodle.core.domain.model.local

data class StorageImageInfo(
    val id: Int?,
    val userId: Int?,
    val mimeType: String?,
    val byteArray: ByteArray?
) {
    val extension: String
        get() = byteArray?.detectImageExtension()
            ?: mimeType.toImageExtension()
            ?: "jpg"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is StorageImageInfo) return false

        return id == other.id &&
                userId == other.userId &&
                mimeType == other.mimeType &&
                byteArray.contentEqualsNullable(other.byteArray)
    }

    override fun hashCode(): Int {
        var result = id ?: 0
        result = 31 * result + (userId ?: 0)
        result = 31 * result + (mimeType?.hashCode() ?: 0)
        result = 31 * result + (byteArray?.contentHashCode() ?: 0)
        result = 31 * result + extension.hashCode()
        return result
    }
}

private fun String?.toImageExtension(): String? {
    return when (this?.lowercase()) {
        "image/jpeg", "image/jpg" -> "jpg"
        "image/png" -> "png"
        "image/webp" -> "webp"
        "image/gif" -> "gif"
        "image/heic" -> "heic"
        else -> null
    }
}

private fun ByteArray.detectImageExtension(): String? {
    if (size < 12) return null

    return when {
        this[0] == 0xFF.toByte() &&
                this[1] == 0xD8.toByte() &&
                this[2] == 0xFF.toByte() -> "jpg"

        this[0] == 0x89.toByte() &&
                this[1] == 0x50.toByte() &&
                this[2] == 0x4E.toByte() &&
                this[3] == 0x47.toByte() -> "png"

        this[0] == 0x52.toByte() &&
                this[1] == 0x49.toByte() &&
                this[2] == 0x46.toByte() &&
                this[3] == 0x46.toByte() &&
                this[8] == 0x57.toByte() &&
                this[9] == 0x45.toByte() &&
                this[10] == 0x42.toByte() &&
                this[11] == 0x50.toByte() -> "webp"

        this[0] == 0x47.toByte() &&
                this[1] == 0x49.toByte() &&
                this[2] == 0x46.toByte() -> "gif"

        else -> null
    }
}

private fun ByteArray?.contentEqualsNullable(other: ByteArray?): Boolean {
    return when {
        this == null && other == null -> true
        this == null || other == null -> false
        else -> this.contentEquals(other)
    }
}

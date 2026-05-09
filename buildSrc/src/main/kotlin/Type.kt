enum class Type(val get: String) {
    DEFAULT("implementation"),
    PLATFORM("implementation"),
    TEST("testImplementation"),
    ANNOTATION_PROCESSOR("annotationProcessor"),
    ANDROID_TEST("androidTestImplementation"),
    DEBUG("debugImplementation"),
    KSP("ksp")
}
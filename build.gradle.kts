// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.jetbrains.kotlin.android) apply false

    alias(libs.plugins.android.application) apply false
    //Ksp migration
    alias(libs.plugins.android.kotlin.ksp) apply false
    //Hilt
    alias(libs.plugins.android.hilt) apply false
    //Serialization
    alias(libs.plugins.serialization) apply false
    //Firebase
    alias(libs.plugins.google.services) apply false

}
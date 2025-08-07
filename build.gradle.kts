// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
}

/**
 * Required to make all modules use JUnit 5 instead of JUnit 4
 * */
subprojects {
    tasks {
        withType<Test>().configureEach {
            useJUnitPlatform()
        }
    }
}

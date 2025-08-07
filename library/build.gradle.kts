import com.vanniktech.maven.publish.SonatypeHost

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.vanniktech.maven.publish") version "0.32.0"
}

kotlin {
    jvmToolchain(libs.versions.jvm.get().toInt())
}

android {
    namespace = "io.github.aungthiha.snackbar"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.material3)

    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockk)
    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.platform.launcher)
}

mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

    signAllPublications()

    coordinates(
        groupId = "io.github.aungthiha",
        artifactId = "android-snackbar-stateflow-handle",
        version = "1.0.0"
    )

    pom {
        name.set("AndroidSnackbarStateFlowHandle")
        description.set("A lifecycle-aware Snackbar library that eliminates boilerplate and prevents missed/duplicated snackbars in Jetpack Compose.")
        url.set("https://github.com/AungThiha/AndroidSnackbarStateFlowHandle")

        licenses {
            license {
                name.set("MIT License")
                url.set("https://github.com/AungThiha/AndroidSnackbarStateFlowHandle/blob/main/LICENSE")
            }
        }

        developers {
            developer {
                id.set("AungThiha")
                name.set("Aung Thiha")
                email.set("mr.aungthiha@gmail.com")
            }
        }

        scm {
            connection.set("scm:git:github.com/AungThiha/AndroidSnackbarStateFlowHandle.git")
            developerConnection.set("scm:git:ssh://github.com/AungThiha/AndroidSnackbarStateFlowHandle.git")
            url.set("https://github.com/AungThiha/AndroidSnackbarStateFlowHandle")
        }
    }
}

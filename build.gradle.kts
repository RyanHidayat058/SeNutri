// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.google.gms.google.services) apply false
}

subprojects {
    configurations.all {
        resolutionStrategy {
            eachDependency {
                if (requested.group == "com.google.firebase" && requested.name == "firebase-auth") {
                    useVersion("22.3.1")
                }
                if (requested.group == "com.google.firebase" && requested.name == "firebase-auth-ktx") {
                    useVersion("22.3.1")
                }
            }
        }
    }
}
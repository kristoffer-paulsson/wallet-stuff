plugins {
    kotlin("multiplatform") version "1.9.0"
    kotlin("plugin.serialization") version "1.9.0"
}

group = "io.bipcrypto"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
        withJava()
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }
    js(IR) {
        binaries.executable()
        browser()
        nodejs()
        /*@Suppress("OPT_IN_USAGE")
        wasm {
            browser()
            nodejs()
            d8()
        }*/
        generateTypeScriptDefinitions()
    }
    val hostOs = System.getProperty("os.name")
    val isMingwX64 = hostOs.startsWith("Windows")
    val nativeTarget = when {
        hostOs == "Mac OS X" -> macosX64("native")
        hostOs == "Linux" -> linuxX64("native")
        isMingwX64 -> mingwX64("native")
        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }

    
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.angproj.aux.util:angelos-project-aux:0.0.1")
                implementation("org.angproj.crypt:angelos-project-crypt:0.0.1")
                implementation("com.doist.x:normalize:1.0.5")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
            }
        }
        val jvmMain by getting{
        }
        val jvmTest by getting
        val jsMain by getting
        val jsTest by getting
        val nativeMain by getting
        val nativeTest by getting
    }
}

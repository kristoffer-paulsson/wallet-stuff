plugins {
    kotlin("multiplatform") version "1.8.22"
    kotlin("plugin.serialization") version "1.8.22"
    `maven-publish`
}

group = "io.bipcrypto"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
}

kotlin {
    explicitApi()
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
        testRuns["test"].executionTask.configure {
            useJUnit()
        }
    }
    js(IR) {
        browser()
        nodejs()
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
                implementation("org.angproj.aux:angelos-project-aux:0.6.2")
                implementation("org.angproj.crypt:angelos-project-crypt:0.2.3")
                implementation("com.doist.x:normalize:1.0.5")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
            }
        }
        val jvmMain by getting
        val jvmTest by getting
        val jsMain by getting
        val jsTest by getting
        val nativeMain by getting
        val nativeTest by getting

        val baseBackboneTest by creating {
            description = "Backbone Test Suite"
            dependsOn(commonMain)
        }

        val jvmVectorTest by creating {
            description = "Vector Test Suite"
            dependsOn(jvmMain)
            dependsOn(baseBackboneTest)
            dependencies {
                implementation(kotlin("test"))
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
            }
        }

        jvm { compilations["test"].source(jvmVectorTest) }
    }
}

publishing {
    repositories {
        maven {}
    }
    publications {
        getByName<MavenPublication>("kotlinMultiplatform") {
            artifactId = rootProject.name
        }
    }
}
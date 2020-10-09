import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jlleitschuh.gradle.ktlint.KtlintExtension

plugins {
    idea
    kotlin("jvm") version Ver.kotlin
    kotlin("kapt") version Ver.kotlin
    kotlin("plugin.spring") version Ver.kotlin
    id(Plugin.ktLint) version Ver.ktlintPlugin
    id(Plugin.springDependencyManagement) version Ver.springDependencyManagement
    id(Plugin.springBoot) version Ver.springBoot
}

group = "com.whiskey"
version = "1.0.0-SNAPSHOT"

repositories {
    jcenter()
    mavenCentral()
}

dependencies {
    implementation(springBoot("starter-web"))
    implementation(kotlin("stdlib-jdk8", Ver.kotlin))
    implementation(kotlin("reflect", Ver.kotlin))
    implementation(flyway("core"))

    testImplementation(springBoot("starter-test")) {
        exclude(group = "junit", module = "junit")
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testImplementation(mockitoKotlin2())
    testImplementation(mockk())
    testRuntimeOnly(junit5("engine"))
}

configure<JavaPluginExtension> {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

configure<KtlintExtension> {
    version.set(Ver.ktlint)
    disabledRules.add("import-ordering")
}

tasks {
    compileKotlin {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
        }
    }

    jar {
        dependsOn(bootJar)
    }

    bootJar {
        archiveFileName.value("meetube-api-server.jar")
    }

    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }
}
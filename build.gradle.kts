plugins {
    id("org.jetbrains.intellij") version "0.7.2"
    java
    kotlin("jvm") version "1.5.0"
}

group = "ru.lardis.meta"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

intellij {
    version = "202.7660.26"  // Same IntelliJ IDEA version (2019.1.4) as target 3.5 Android Studio
    type = "IC"              // Use IntelliJ IDEA CE because it's the basis of the IntelliJ Platform
    // Require the Android plugin, Gradle will match the plugin version to intellij.version
    setPlugins("android","org.jetbrains.kotlin:202-1.5.10-release-894-AS8194.7")
}
tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
tasks.getByName<org.jetbrains.intellij.tasks.PatchPluginXmlTask>("patchPluginXml") {
    changeNotes(
        """
      Add change notes here.<br>
      <em>most HTML tags may be used</em>"""
    )
}


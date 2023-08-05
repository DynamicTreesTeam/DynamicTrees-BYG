import com.google.gson.Gson
import com.google.gson.JsonObject
import com.matthewprenger.cursegradle.*
import java.io.InputStreamReader
import java.time.Instant
import java.time.format.DateTimeFormatter

fun property(key: String) = project.findProperty(key).toString()

plugins {
    id("java")
    id("net.minecraftforge.gradle")
    id("org.parchmentmc.librarian.forgegradle")
    id("idea")
    id("maven-publish")
    //id("com.harleyoconnor.translationsheet") version "0.1.1"
    id("com.matthewprenger.cursegradle") version "1.4.0"
    id("com.harleyoconnor.autoupdatetool") version "1.0.0"
}

repositories {
    maven("https://ldtteam.jfrog.io/ldtteam/modding/")
    maven("https://www.cursemaven.com") {
        content {
            includeGroup("curse.maven")
        }
    }
    maven("https://harleyoconnor.com/maven")
    maven("https://squiddev.cc/maven/")
    mavenLocal()
}

val modName = property("modName")
val modId = property("modId")
val modVersion = property("modVersion")
val mcVersion = property("mcVersion")

version = "$mcVersion-$modVersion"
group = property("group")

minecraft {
    mappings("parchment", "${property("mappingsVersion")}-$mcVersion")

    runs {
        create("client") {
            applyDefaultConfiguration()

            if (project.hasProperty("mcUuid")) {
                args("--uuid", property("mcUuid"))
            }
            if (project.hasProperty("mcUsername")) {
                args("--username", property("mcUsername"))
            }
            if (project.hasProperty("mcAccessToken")) {
                args("--accessToken", property("mcAccessToken"))
            }
        }

        create("server") {
            applyDefaultConfiguration()
        }

        create("data") {
            applyDefaultConfiguration()

            args(
                "--mod", modId,
                "--all",
                "--output", file("src/generated/resources/"),
                "--existing", file("src/main/resources"),
                "--existing-mod", "dynamictrees",
                "--existing-mod", "dynamictreesplus",
                "--existing-mod", "byg"
            )
        }
    }
}

sourceSets.main.get().resources {
    srcDir("src/generated/resources")
}

dependencies {
    // Not sure if we need this one, what is a "forge" anyway?
    minecraft("net.minecraftforge:forge:$mcVersion-${property("forgeVersion")}")
    // BYG requires this
    runtimeOnly(fg.deobf("curse.maven:terrablender-563928:3957976"))
    // Compile BYG and DT, of course.
    implementation(fg.deobf("curse.maven:BYG-247560:4036050"))
    //implementation(fg.deobf("curse.maven:dynamictrees-252818:4458396"))
    implementation(fg.deobf("com.ferreusveritas.dynamictrees:DynamicTrees-$mcVersion:${property("dynamicTreesVersion")}"))

    // At runtime, use DT+ for BYG's cacti.
    //implementation(fg.deobf("curse.maven:dynamictreesplus-478155:4458646"))
    implementation(fg.deobf("com.ferreusveritas.dynamictreesplus:DynamicTreesPlus-$mcVersion:${property("dynamicTreesPlusVersion")}"))

    /////////////////////////////////////////
    /// Runtime Dependencies (optional)
    /////////////////////////////////////////

    // At runtime, use the full Hwyla mod.
    runtimeOnly(fg.deobf("curse.maven:jade-324717:3970956"))

    // At runtime, use the full JEI mod.
    runtimeOnly(fg.deobf("mezz.jei:jei-$mcVersion:${property("jeiVersion")}"))

    // At runtime, use CC for creating growth chambers.
    runtimeOnly(fg.deobf("org.squiddev:cc-tweaked-$mcVersion:${property("ccVersion")}"))

    // At runtime, get rid of experimental settings warning screen.
    runtimeOnly(fg.deobf("curse.maven:ShutUpExperimentalSettings-407174:3188120"))

    // At runtime, use suggestion provider fix mod.
    runtimeOnly(fg.deobf("com.harleyoconnor.suggestionproviderfix:SuggestionProviderFix-1.18.1:${property("suggestionProviderFixVersion")}"))

    // At runtime, use snow coated to allow snow and vines on leaves
    runtimeOnly(fg.deobf("curse.maven:snow-coated-843893:4626429"))
}

tasks.jar {
    manifest.attributes(
        "Specification-Title" to project.name,
        "Specification-Vendor" to "Max Hyper",
        "Specification-Version" to "1",
        "Implementation-Title" to project.name,
        "Implementation-Version" to project.version,
        "Implementation-Vendor" to "Max Hyper",
        "Implementation-Timestamp" to DateTimeFormatter.ISO_INSTANT.format(Instant.now())
    )

    archiveBaseName.set(modName)
    finalizedBy("reobfJar")
}

java {
    withSourcesJar()

    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

fun enablePublishing() =
    project.hasProperty("curseApiKey") && project.hasProperty("curseFileType") && project.hasProperty("projectId")

curseforge {
    if (project.hasProperty("curseApiKey") && project.hasProperty("curseFileType")) {
        apiKey = property("curseApiKey")

        project {
            id = "562143"

            addGameVersion(mcVersion)

            changelog = file("changelog.txt")
            changelogType = "markdown"
            releaseType = property("curseFileType")

            addArtifact(tasks.findByName("sourcesJar"))

            mainArtifact(tasks.findByName("jar")) {
                relations {
                    optionalDependency("dynamictreesplus")
                    optionalDependency("chunk-saving-fix")
                }
            }
        }
    } else {
        project.logger.log(
            LogLevel.WARN,
            "API Key and file type for CurseForge not detected; uploading will be disabled."
        )
    }
}

tasks.withType<GenerateModuleMetadata> {
    enabled = false
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = "$modName-$mcVersion"
            version = modVersion

            from(components["java"])

            pom {
                name.set(modName)
                url.set("https://github.com/supermassimo/$modName")
                licenses {
                    license {
                        name.set("MIT")
                        url.set("https://mit-license.org")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/supermassimo/$modName.git")
                    developerConnection.set("scm:git:ssh://github.com/supermassimo/$modName.git")
                    url.set("https://github.com/supermassimo/$modName")
                }
            }

            pom.withXml {
                val element = asElement()

                // Clear dependencies.
                for (i in 0 until element.childNodes.length) {
                    val node = element.childNodes.item(i)
                    if (node?.nodeName == "dependencies") {
                        element.removeChild(node)
                    }
                }
            }
        }
    }
    repositories {
        maven("file:///${project.projectDir}/mcmodsrepo")
        if (hasProperty("harleyOConnorMavenUsername") && hasProperty("harleyOConnorMavenPassword")) {
            maven("https://harleyoconnor.com/maven") {
                name = "HarleyOConnor"
                credentials {
                    username = property("harleyOConnorMavenUsername")
                    password = property("harleyOConnorMavenPassword")
                }
            }
        } else {
            logger.log(LogLevel.WARN, "Credentials for maven not detected; it will be disabled.")
        }
    }
}

//translationSheet {
//    this.sheetId.set("1xjxEh2NdbeV_tQc6fDHPgcRmtchqCZJKt--6oifq1qc")
//    this.sectionColour.set(0xF9CB9C)
//    this.sectionPattern.set("Dynamic Trees")
//    this.outputDir("src/localization/resources/assets/dynamictrees/lang/")
//
//    this.useJson()
//}

fun net.minecraftforge.gradle.common.util.RunConfig.applyDefaultConfiguration(runDirectory: String = "run") {
    workingDirectory = file(runDirectory).absolutePath

    property("forge.logging.markers", "SCAN,REGISTRIES,REGISTRYDUMP")
    property("forge.logging.console.level", "debug")

    property("mixin.env.remapRefMap", "true")
    property("mixin.env.refMapRemappingFile", "${buildDir}/createSrgToMcp/output.srg")

    mods {
        create(modId) {
            source(sourceSets.main.get())
        }
    }
}

fun com.matthewprenger.cursegradle.CurseExtension.project(action: CurseProject.() -> Unit) {
    this.project(closureOf(action))
}

fun CurseProject.mainArtifact(artifact: Task?, action: CurseArtifact.() -> Unit) {
    this.mainArtifact(artifact, closureOf(action))
}

fun CurseArtifact.relations(action: CurseRelation.() -> Unit) {
    this.relations(closureOf(action))
}
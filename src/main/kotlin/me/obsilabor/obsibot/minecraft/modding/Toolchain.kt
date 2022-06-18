package me.obsilabor.obsibot.minecraft.modding

enum class Toolchain(
    val capitalizedName: String,
    val apiGroupId: String,
    val apiUrl: String,
    val loaderGroupId: String,
    val loaderUrl: String,
    val gradlePlugins: List<String>,
    val repositories: List<String>,
    val defaultMappings: Mappings,
) {

    QUILT(
        "Quilt",
        "org.quiltmc.quilted-fabric-api:quilted-fabric-api",
        "https://api.modrinth.com/v2/project/qvIfYCYJ/version",
        "org.quiltmc:quilt-loader",
        "https://meta.quiltmc.org/v3/versions/loader",
        listOf(
            "fabric-loom:0.12-SNAPSHOT",
            "io.github.juuxel.loom-quiltflower:1.7.3"
        ),
        listOf(
            "https://maven.fabricmc.net/",
            "https://server.bbkr.space/artifactory/libs-release/"
        ),
        Mappings.LAYERED_MOJANG_AND_QUILT
    ),
    FABRIC(
        "Fabric",
        "net.fabricmc.fabric-api:fabric-api",
        "https://api.modrinth.com/v2/project/P7dR8mSH/version",
        "net.fabricmc:fabric-loader",
        "https://meta.fabricmc.net/v1/versions/loader",
        listOf(
            "fabric-loom:0.12-SNAPSHOT"
        ),
        listOf(
            "https://maven.fabricmc.net/"
        ),
        Mappings.YARN
    )

}
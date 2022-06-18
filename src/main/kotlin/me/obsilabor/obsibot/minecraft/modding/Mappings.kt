package me.obsilabor.obsibot.minecraft.modding

enum class Mappings(
    val capitalizedName: String,
    val url: String?,
    val gradlePlugins: List<String> = emptyList(),
    val gradleRepositories: List<String> = emptyList(),
    val isLayered: Boolean = false
) {

    OFFICIAL_MOJANG(
        "Official Mojang Mappings",
        null
    ),
    YARN(
        "Fabric Yarn Mappings",
        "https://meta.fabricmc.net/v1/versions/mappings"
    ),
    QUILT(
        "Quilt Mappings",
        "https://meta.quiltmc.org/v3/versions/quilt-mappings",
        listOf(
            "org.quiltmc.quilt-mappings-on-loom:4.2.0"
        ),
        listOf(
            "https://maven.quiltmc.org/repository/release",
            "https://server.bbkr.space/artifactory/libs-release/"
        )
    ),
    LAYERED_MOJANG_AND_QUILT(
        "Layered Mojang and Quilt Mappings",
        "https://meta.quiltmc.org/v3/versions/quilt-mappings",
        listOf(
            "org.quiltmc.quilt-mappings-on-loom:4.2.0"
        ),
        listOf(
            "https://maven.quiltmc.org/repository/release",
            "https://server.bbkr.space/artifactory/libs-release/"
        ),
        true
    );

}
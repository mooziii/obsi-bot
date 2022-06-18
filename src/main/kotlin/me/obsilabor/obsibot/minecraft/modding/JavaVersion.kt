package me.obsilabor.obsibot.minecraft.modding

class JavaVersion(val intVersion: Int, val stringVersion: String, val longTermSupport: Boolean, val officialVersionName: String = "Java $intVersion") {

    companion object {
        val VERSIONS = listOf(
            JavaVersion(7, "1.7", true),
            JavaVersion(8, "1.8", true),
            JavaVersion(9, "1.9", false),
            JavaVersion(10, "10", false),
            JavaVersion(11, "11", true),
            JavaVersion(12, "12", false),
            JavaVersion(13, "13", false),
            JavaVersion(14, "14", false),
            JavaVersion(15, "15", false),
            JavaVersion(16, "16", false),
            JavaVersion(17, "17", true),
            JavaVersion(18, "18", false),
        )
    }

}
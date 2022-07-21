package me.obsilabor.obsibot.features

object Blacklist {
    fun containsWord(string: String, blacklistedWord: String, checkVariations: Boolean): Boolean {
        if (string.contains(blacklistedWord, true)) {
            return true
        }
        if (checkVariations) {
            if (string.contains(blacklistedWord.replace(".", " dot "), true)) {
                return true
            }
            if (string.contains(blacklistedWord.replace(".", " . "), true)) {
                return true
            }
            if (string.contains(blacklistedWord.replace(".", " , "), true)) {
                return true
            }
            if (string.contains(blacklistedWord.replace(".", " punkt "), true)) {
                return true
            }
            if (string.contains(blacklistedWord.replace(".", " "), true)) {
                return true
            }
            if (string.contains(blacklistedWord.replace(".", ","), true)) {
                return true
            }
            if (string.replace(" ", "").contains(blacklistedWord)) {
                return true
            }
            val numbersReplaced = string
                .replace("4", "a")
                .replace("1", "i")
                .replace("5", "s")
                .replace("4", "a")
                .replace("3", "e")
            if (numbersReplaced.contains(blacklistedWord, true)) {
                return true
            }
        }
        return false
    }
}
package me.obsilabor.obsibot.utils

import java.io.File

fun getOrCreateFile(file: File): File {
    if(!file.exists()) {
        file.createNewFile()
    }
    return file
}

fun getOrCreateDirectory(dir: File): File {
    if(!dir.exists()) {
        dir.createNewFile()
    }
    return dir
}

fun File.createParent() {
    if(!parentFile.exists()) {
        parentFile.mkdir()
    }
}

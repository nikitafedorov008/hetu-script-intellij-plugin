package com.hetu.hetuscript

import com.intellij.openapi.fileTypes.LanguageFileType
import javax.swing.Icon

class HetuFileType : LanguageFileType(HetuLanguage) {
    override fun getName() = "Hetu Script File"
    override fun getDescription() = "Hetu Script language file"
    override fun getDefaultExtension() = "ht"
    override fun getIcon(): Icon? = HetuIcons.FILE

    companion object {
        val INSTANCE = HetuFileType()
    }
}
package com.hetu.hetuscript

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider
import com.hetu.hetuscript.HetuFileType

class HetuFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, HetuLanguage) {
    override fun getFileType(): FileType = HetuFileType.INSTANCE

    override fun toString(): String = "Hetu Script File"
}
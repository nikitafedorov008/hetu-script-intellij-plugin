package com.hetu.hetuscript

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.patterns.PlatformPatterns
import com.intellij.util.ProcessingContext

class HetuCompletionContributor : CompletionContributor() {
    init {
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement().withLanguage(HetuLanguage),
            HetuCompletionProvider()
        )
    }
}

class HetuCompletionProvider : CompletionProvider<CompletionParameters>() {
    override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
        listOf(
            "class", "func", "var", "final", "if", "else", "for", "while",
            "break", "continue", "return", "import", "export", "assert",
            "true", "false", "null", "this", "super"
        ).forEach { keyword ->
            result.addElement(LookupElementBuilder.create(keyword))
        }
    }
}
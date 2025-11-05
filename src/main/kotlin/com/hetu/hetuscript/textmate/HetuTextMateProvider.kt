package com.hetu.hetuscript.textmate

import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.editor.colors.EditorColorsScheme
import com.intellij.openapi.editor.highlighter.EditorHighlighter

// This is an attempt to create a proper TextMate-based syntax highlighter
class HetuTextMateProvider : SyntaxHighlighterFactory() {
    
    override fun getSyntaxHighlighter(project: Project?, virtualFile: VirtualFile?): SyntaxHighlighter {
        // Try to create a proper TextMate-based syntax highlighter
        try {
            // Load the TextMate grammar directly
            val textMateServiceClass = Class.forName("org.jetbrains.plugins.textmate.TextMateService")
            val serviceInstance = textMateServiceClass.getMethod("getInstance").invoke(null)
            
            // Get the loadGrammar method
            val loadGrammarMethod = textMateServiceClass.getMethod(
                "loadGrammar", 
                String::class.java, 
                String::class.java, 
                java.io.Reader::class.java, 
                Any::class.java
            )
            
            val grammarStream = javaClass.classLoader.getResourceAsStream("textmate/hetuscript.json")
            if (grammarStream != null) {
                val grammarReader = java.io.InputStreamReader(grammarStream, "UTF-8")
                val grammar = loadGrammarMethod.invoke(
                    serviceInstance, 
                    "source.hetu", 
                    "hetuscript.json", 
                    grammarReader, 
                    null
                )
                
                // Try to create the TextMate syntax highlighter
                val textMateSyntaxHighlighterClass = Class.forName("org.jetbrains.plugins.textmate.language.TextMateBasedSyntaxHighlighter")
                val textMateSyntaxHighlighterConstructor = textMateSyntaxHighlighterClass.getConstructor(
                    Class.forName("org.jetbrains.plugins.textmate.language.TextMateGrammarBase"),
                    Any::class.java,
                    EditorColorsScheme::class.java
                )
                
                return textMateSyntaxHighlighterConstructor.newInstance(grammar, null, null) as SyntaxHighlighter
            }
        } catch (e: Exception) {
            // If TextMate integration fails, fallback to the original syntax highlighter
            // This could happen if TextMate plugin is not available
        }
        
        // Fallback to existing syntax highlighter
        return com.hetu.hetuscript.HetuSyntaxHighlighter()
    }
}
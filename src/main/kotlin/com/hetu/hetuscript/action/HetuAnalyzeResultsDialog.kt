package com.hetu.hetuscript.action

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.components.JBTextArea
import java.awt.BorderLayout
import javax.swing.JComponent
import javax.swing.JPanel

class HetuAnalyzeResultsDialog(project: Project?, private val resultsText: String, private val resultsList: List<String>) : DialogWrapper(project) {
    
    private lateinit var textArea: JBTextArea
    
    init {
        title = "Hetu Analysis Results"
        init()
    }
    
    override fun createCenterPanel(): JComponent? {
        val panel = JPanel(BorderLayout())
        
        textArea = JBTextArea(resultsText)
        textArea.isEditable = false
        textArea.lineWrap = true
        textArea.wrapStyleWord = true
        
        val scrollPane = JBScrollPane(textArea)
        
        panel.add(scrollPane, BorderLayout.CENTER)
        
        return panel
    }
    
    override fun getPreferredFocusedComponent(): JComponent? {
        return textArea
    }
}
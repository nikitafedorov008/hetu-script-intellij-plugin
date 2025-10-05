package com.hetu.hetuscript.run

import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import java.awt.BorderLayout
import javax.swing.JComponent
import javax.swing.JPanel

class HetuRunConfigurationEditor : SettingsEditor<HetuRunConfiguration>() {
    private lateinit var mainPanel: JPanel
    private lateinit var scriptPathField: TextFieldWithBrowseButton

    override fun createEditor(): JComponent {
        mainPanel = JPanel(BorderLayout())

        scriptPathField = TextFieldWithBrowseButton().apply {
            addBrowseFolderListener(
                "Select Hetu Script",
                "Choose a Hetu script file to run",
                null, // project is available in the configuration
                FileChooserDescriptor(false, true, false, false, false, false)
            )
        }

        mainPanel.add(scriptPathField, BorderLayout.CENTER)
        return mainPanel
    }

    override fun resetEditorFrom(configuration: HetuRunConfiguration) {
        scriptPathField.text = configuration.getScriptPath()
    }

    override fun applyEditorTo(configuration: HetuRunConfiguration) {
        configuration.setScriptPath(scriptPathField.text)
    }
}
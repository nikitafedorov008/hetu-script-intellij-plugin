package com.hetu.hetuscript.run

import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.openapi.ui.VerticalFlowLayout
import com.intellij.ui.components.JBLabel
import java.awt.BorderLayout
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.Insets
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel

class HetuCompileConfigurationEditor : SettingsEditor<HetuCompileConfiguration>() {
    private lateinit var sourceScriptField: TextFieldWithBrowseButton
    private lateinit var outputField: TextFieldWithBrowseButton
    private lateinit var programParametersField: TextFieldWithBrowseButton

    override fun createEditor(): JComponent {
        val panel = JPanel(BorderLayout())

        // Create source script field
        sourceScriptField = TextFieldWithBrowseButton().apply {
            addBrowseFolderListener(
                "Select Hetu Script",
                "Choose a Hetu script file to compile",
                null, // project will be null here, will be passed via dialog
                FileChooserDescriptor(true, false, false, false, false, false)
                    .withFileFilter { it.extension == "ht" }
                    .withTitle("Select Hetu Script")
            )
        }

        // Create output field
        outputField = TextFieldWithBrowseButton().apply {
            addBrowseFolderListener(
                "Select Output Directory",
                "Choose output directory for compiled files",
                null,
                FileChooserDescriptor(false, true, false, false, false, false)
                    .withTitle("Select Output Directory")
            )
        }

        // Create program parameters field
        programParametersField = TextFieldWithBrowseButton().apply {
            addBrowseFolderListener(
                "Program Parameters",
                "Additional parameters for the compile command",
                null,
                FileChooserDescriptor(true, true, false, false, false, false)
                    .withTitle("Additional Parameters")
            )
        }

        // Create main panel with GridBagLayout
        val mainPanel = JPanel(GridBagLayout())
        val gbc = GridBagConstraints().apply {
            fill = GridBagConstraints.HORIZONTAL
            insets = Insets(2, 2, 2, 2)
            weightx = 1.0
        }

        gbc.gridx = 0
        gbc.gridy = 0
        gbc.anchor = GridBagConstraints.WEST
        mainPanel.add(JLabel("Source Script:"), gbc)

        gbc.gridx = 1
        gbc.weightx = 1.0
        mainPanel.add(sourceScriptField, gbc)

        gbc.gridx = 0
        gbc.gridy = 1
        gbc.weightx = 0.0
        gbc.anchor = GridBagConstraints.WEST
        mainPanel.add(JLabel("Output Path:"), gbc)

        gbc.gridx = 1
        gbc.weightx = 1.0
        mainPanel.add(outputField, gbc)

        // helper hint: when a pubspec.yaml with `version` exists in the project of the .ht file,
        // the output file will be named `<scriptName>-<version>.out` (otherwise legacy behaviour)
        gbc.gridx = 1
        gbc.gridy = 2
        gbc.weightx = 1.0
        val hint = JBLabel("If a pubspec.yaml with 'version' exists, output will be '<name>-<version>.out'")
        hint.font = hint.font.deriveFont((hint.font.style))
        mainPanel.add(hint, gbc)

        gbc.gridx = 0
        gbc.gridy = 2
        gbc.weightx = 0.0
        gbc.anchor = GridBagConstraints.WEST
        mainPanel.add(JLabel("Program Parameters:"), gbc)

        gbc.gridx = 1
        gbc.weightx = 1.0
        mainPanel.add(programParametersField, gbc)

        panel.add(mainPanel, BorderLayout.CENTER)
        return panel
    }

    override fun resetEditorFrom(configuration: HetuCompileConfiguration) {
        // Set default values if they are empty or null
        val sourcePath = if (configuration.getSourceScriptPath().isEmpty()) "ht-lib/" else configuration.getSourceScriptPath()
        val outputPath = if (configuration.getOutputPath().isEmpty()) "build/hetu/output.out" else configuration.getOutputPath()
        
        sourceScriptField.text = sourcePath
        outputField.text = outputPath
        programParametersField.text = configuration.getProgramParameters()
    }

    override fun applyEditorTo(configuration: HetuCompileConfiguration) {
        configuration.setSourceScriptPath(sourceScriptField.text)
        configuration.setOutputPath(outputField.text)
        configuration.setProgramParameters(programParametersField.text)
    }
}
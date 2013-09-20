package com.seaniscool.whatsapp2pdf.ui

import javafx.fxml.{FXML, FXMLLoader}
import javafx.scene.Group
import javafx.scene.control.{Label, ListView, TableView}
import java.io.File
import javafx.stage.{DirectoryChooser, Stage, FileChooser}
import com.seaniscool.whatsapp2pdf.{CommandLineArgs, Log}
import javafx.beans.value.ObservableValue
import javafx.beans.property.SimpleStringProperty
import com.seaniscool.whatsapp2pdf.parser.{PDFWriter, WhatsAppParser}

/** The controller for the WhatsApp2PDF JavaFX UI.
  *
  * @author Sean Connolly
  */
class Controller(primaryStage: Stage) extends Group {

  private val resource = getClass.getResource("/fxml/whatsapp2pdf.fxml")
  private val loader = new FXMLLoader(resource)
  loader.setRoot(this)
  loader.setController(this)
  loader.load()

  private var targetDirectory: File = _
  @FXML
  private var targetDirectoryLabel: Label = _
  @FXML
  private var sourceListView: ListView[WhatsAppFile] = _
  @FXML
  private var targetListView: ListView[WhatsAppFile] = _

  def initialize() = {
    targetDirectory = new File(System.getProperty("user.home"))
    refreshTargetDirectoryLabel()
  }

  @FXML
  protected def addSourceFile() = {
    val fileChooser = new FileChooser()
    val extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt")
    fileChooser.getExtensionFilters.add(extFilter)
    val file = fileChooser.showOpenDialog(primaryStage)
    println(sourceListView)
    println(sourceListView.getItems)
    println(file)
    sourceListView.getItems.add(new WhatsAppFile(file))
  }

  @FXML
  protected def convertFiles() = {
    val parser = new WhatsAppParser(targetDirectory)
    val writer = new PDFWriter(targetDirectory)
    val sourceFiles = sourceListView.getItems.iterator
    while (sourceFiles.hasNext) {
      val sourceFile = sourceFiles.next().getFile
      convertFile(sourceFile, parser, writer)
      sourceFiles.remove()
    }
  }

  private def convertFile(sourceFile: File, parser: WhatsAppParser, writer: PDFWriter) = {
    val conversation = parser.parse(sourceFile)
    val targetFile = writer.write(sourceFile, conversation)
    targetListView.getItems.add(new WhatsAppFile(targetFile))
  }

  @FXML
  protected def changeTargetDirectory() {
    val chooser = new DirectoryChooser
    chooser.setTitle("Output Directory")
    chooser.setInitialDirectory(targetDirectory)
    targetDirectory = chooser.showDialog(primaryStage)
    refreshTargetDirectoryLabel()
  }

  private def refreshTargetDirectoryLabel() = {
    targetDirectoryLabel.setText(targetDirectory.getAbsolutePath)
  }

}

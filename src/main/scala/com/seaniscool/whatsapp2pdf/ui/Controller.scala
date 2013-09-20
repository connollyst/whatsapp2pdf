package com.seaniscool.whatsapp2pdf.ui

import javafx.fxml.{FXML, FXMLLoader}
import javafx.scene.Group
import javafx.scene.control.{Label, ListView, TableView}
import java.io.File
import javafx.stage.{DirectoryChooser, Stage, FileChooser}
import com.seaniscool.whatsapp2pdf.Log
import javafx.beans.value.ObservableValue
import javafx.beans.property.SimpleStringProperty

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

  private var targetDirectory: SimpleFileProperty = _
  @FXML
  private var targetDirectoryLabel: Label = _
  @FXML
  private var sourceListView: ListView[WhatsAppFile] = _
  @FXML
  private var targetListView: ListView[WhatsAppFile] = _

  def initialize() = {
    targetDirectory = new SimpleFileProperty()
    println("Target Directory: " + targetDirectory)
    targetDirectory.setFile(System.getProperty("user.dir"))
    targetDirectoryLabel.textProperty().bind(targetDirectory)
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
    val sourceFiles = sourceListView.getItems.iterator
    while (sourceFiles.hasNext) {
      val sourceFile = sourceFiles.next()
      println("Converting " + sourceFile)
      sourceFiles.remove()
      targetListView.getItems.add(sourceFile)
    }
  }

  @FXML
  protected def changeTargetDirectory() {
    val chooser = new DirectoryChooser
    chooser.setTitle("Output Directory")
    chooser.setInitialDirectory(targetDirectory.getFile)
    targetDirectory.setFile(chooser.showDialog(primaryStage))
  }

}

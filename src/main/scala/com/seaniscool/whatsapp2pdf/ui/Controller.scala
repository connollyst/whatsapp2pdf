package com.seaniscool.whatsapp2pdf.ui

import collection.JavaConversions._
import com.seaniscool.whatsapp2pdf.parser.{PDFWriter, WhatsAppParser}
import java.io.{IOException, File}
import javafx.fxml.{FXML, FXMLLoader}
import javafx.scene.Group
import javafx.scene.control.{Label, ListView}
import javafx.stage.{DirectoryChooser, Stage, FileChooser}
import java.awt.Desktop
import javafx.event.EventHandler
import javafx.scene.input.{TransferMode, MouseDragEvent, MouseEvent, DragEvent}
import javafx.scene.paint.Color
import com.google.common.io.Files
import com.seaniscool.whatsapp2pdf.Directory

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

  var targetDirectory: File = _
  @FXML
  var targetDirectoryLabel: Label = _
  @FXML
  var sourceListView: ListView[WhatsAppFile] = _
  @FXML
  var targetListView: ListView[WhatsAppFile] = _


  def initialize() = {
    targetDirectory = Directory.desktop()
    refreshTargetDirectoryLabel()
    sourceListView.setOnDragDropped(new EventHandler[DragEvent] {
      override def handle(event: DragEvent) = {
        for (file <- event.getDragboard.getFiles) {
          val extension = Files.getFileExtension(file.getName)
          if ("txt".equalsIgnoreCase(extension)) {
            sourceListView.getItems.add(new WhatsAppFile(file))
          }
        }
      }
    })
    sourceListView.setOnDragEntered(new EventHandler[DragEvent] {
      override def handle(event: DragEvent) = {
        sourceListView.setStyle("-fx-border-color: blue;")
      }
    })
    sourceListView.setOnDragExited(new EventHandler[DragEvent] {
      override def handle(event: DragEvent) = {
        sourceListView.setStyle("-fx-border-color: black;")
      }
    })
    sourceListView.setOnDragOver(new EventHandler[DragEvent] {
      override def handle(event: DragEvent) = {
        event.acceptTransferModes(TransferMode.COPY)
      }
    })
  }

  @FXML
  protected def addSourceFile() = {
    val chooser = new FileChooser()
    val filter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt")
    chooser.getExtensionFilters.add(filter)
    chooser.setTitle("Choose Text Files")
    val files = chooser.showOpenMultipleDialog(primaryStage)
    if (files != null) {
      files.foreach(
        file => sourceListView.getItems.add(new WhatsAppFile(file))
      )
    }
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
    val directory = chooser.showDialog(primaryStage)
    if (directory != null) {
      targetDirectory = directory
      refreshTargetDirectoryLabel()
    }
  }

  private def refreshTargetDirectoryLabel() = {
    targetDirectoryLabel.setText(targetDirectory.getAbsolutePath)
  }

  private def openFile(file: File) = {
    if (Desktop.isDesktopSupported) {
      //      try {
      Desktop.getDesktop.open(file)
      //      } catch (IOException ex) {
      // no application registered for PDFs
      //      }
    }
  }

}

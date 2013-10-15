package com.seaniscool.whatsapp2pdf.ui

import collection.JavaConversions._
import com.seaniscool.whatsapp2pdf.parser.{WhatsAppParser}
import java.io.{FileOutputStream, IOException, File}
import javafx.fxml.{FXML, FXMLLoader}
import javafx.scene.Group
import javafx.scene.control.{Label, ListView}
import javafx.stage.{DirectoryChooser, Stage, FileChooser}
import java.awt.Desktop
import javafx.event.EventHandler
import javafx.scene.input.{TransferMode, MouseDragEvent, MouseEvent, DragEvent}
import javafx.scene.paint.Color
import com.google.common.io.Files
import com.seaniscool.whatsapp2pdf.{Log, Directory}
import com.seaniscool.whatsapp2pdf.writer.PDFWriter
import com.seaniscool.whatsapp2pdf.cmd.CommandLineArgs

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
    val logFile = new File(Directory.temp(), "whatsapp2pdf.log")
    Log.register(new FileOutputStream(logFile))
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
        file => {
          Log.debug("Adding source file: " + file.getAbsolutePath)
          sourceListView.getItems.add(new WhatsAppFile(file))
        }
      )
    }
  }

  @FXML
  protected def convertFiles() = {
    Log.debug("Converting files.")
    val parser = new WhatsAppParser()
    val writer = new PDFWriter(targetDirectory)
    val sourceFiles = sourceListView.getItems.iterator
    while (sourceFiles.hasNext) {
      try {
        val sourceFile = sourceFiles.next().getFile
        convertFile(sourceFile, parser, writer)
        sourceFiles.remove()
      } catch {
        case t: Throwable => {
          Log.error("Failed to convert file!", t)
          // TODO display an error dialog
        }
      }
    }
  }

  private def convertFile(sourceFile: File, parser: WhatsAppParser, writer: PDFWriter) = {
    Log.debug("Converting " + sourceFile)
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
      Log.debug("Setting target directory to " + directory)
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

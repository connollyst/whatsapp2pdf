package com.seaniscool.whatsapp2pdf

import javafx.fxml.FXMLLoader
import scalafx.application.JFXApp
import scalafx.scene.Scene

object WhatsApp2PDF extends JFXApp {

  val root = FXMLLoader.load(getClass.getResource("whatsapp2pdf.fxml"))
  val scene2 = new Scene(root, 300, 275)

  stage = new JFXApp.PrimaryStage {
    title = "WhatsApp2PDF"
    width = 600
    height = 450
    scene = scene2
  }
}
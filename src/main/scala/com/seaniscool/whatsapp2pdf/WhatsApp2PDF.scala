package com.seaniscool.whatsapp2pdf

import com.seaniscool.whatsapp2pdf.ui.Controller
import javafx.application.Application
import javafx.scene.Scene
import javafx.stage.Stage

class WhatsApp2PDF extends Application {

  override def start(primaryStage: Stage) {
    val ide = new Controller(primaryStage)
    primaryStage.setTitle("WhatsApp2PDF")
    primaryStage.setWidth(800)
    primaryStage.setHeight(600)
    primaryStage.setScene(new Scene(ide))
    primaryStage.show()
  }

}

object WhatsApp2PDF {

  def main(args: Array[String]) {
    Application.launch(classOf[WhatsApp2PDF], args: _*)
  }

}
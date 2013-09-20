package com.seaniscool.whatsapp2pdf.ui

import javafx.fxml.FXMLLoader
import javafx.scene.Group

/** The controller for the WhatsApp2PDF JavaFX UI.
  *
  * @author Sean Connolly
  */
class Controller extends Group {

  private val resource = getClass.getResource("/fxml/whatsapp2pdf.fxml")
  private val loader = new FXMLLoader(resource)
  loader.setRoot(this)
  loader.setController(this)
  loader.load()

  def initialize() {
    println("Initializing " + getClass.getSimpleName)
  }

}

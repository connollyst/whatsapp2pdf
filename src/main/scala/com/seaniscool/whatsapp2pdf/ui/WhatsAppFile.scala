package com.seaniscool.whatsapp2pdf.ui

import java.io.File
import javafx.beans.property.SimpleStringProperty

/**
 *
 * @author Sean Connolly
 */
class WhatsAppFile(path: SimpleStringProperty) {

  def this(file: File) = {
    this(new SimpleStringProperty(file.getAbsolutePath))
  }

  def this() = {
    this(new SimpleStringProperty())
  }

  def getPath = path.get()

  def setPath(path: String) = this.path.setValue(path)

  override def toString: String = path.get()
}

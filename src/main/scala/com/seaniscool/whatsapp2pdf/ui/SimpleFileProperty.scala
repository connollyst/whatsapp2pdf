package com.seaniscool.whatsapp2pdf.ui

import javafx.beans.property.SimpleStringProperty
import java.io.File

/**
 *
 *
 * @author Sean Connolly
 */
class SimpleFileProperty extends SimpleStringProperty {

  println("Creating " + getClass.getSimpleName)
  private var file: File = _

  def getFile: File = file

  def setFile(file: File) = {
    this.file = file
  }

  def setFile(path: String) = {
    this.file = new File(path)
  }

}

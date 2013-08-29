package com.seaniscool

import java.io.File

/**
 *
 *
 * @author Sean Connolly
 */
class WhatsAppParser(outputDirectory: File) {

  def parse(file: File) {
    println("Parsing " + file + " to " + outputDirectory)
  }

}

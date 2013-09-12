package com.seaniscool.whatsapp2pdf.parser

import com.google.common.base.Charsets
import com.google.common.io.Files
import java.io.{BufferedReader, File}

/** A buffered reader to read one line of raw text at a time.
  *
  * @author Sean Connolly
  */
class LineReader(reader: BufferedReader) {

  private var count = 0
  private var line: Option[String] = None

  def ready(): Boolean = {
    line = Option(reader.readLine)
    line.isDefined
  }

  def next(): Line = {
    count += 1
    new Line(line.get, count)
  }

}

object LineReader {

  def newReader(file: File): LineReader = {
    val reader = Files.newReader(file, Charsets.UTF_8)
    new LineReader(reader)
  }

}
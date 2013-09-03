package com.seaniscool

import com.lowagie.text.{Phrase, Paragraph, Document}
import com.lowagie.text.pdf.PdfWriter
import java.io.{File, FileOutputStream}
import com.google.common.io.Files

/**
 * Writes a [[com.seaniscool.Conversation]] to PDF.
 *
 * @author Sean Connolly
 */
class PDFWriter(directory: File) {

  /**
   * Write the [[com.seaniscool.Conversation]] to a PDF file with the suggested
   * file name in the directory specified for this writer.
   *
   * Note: the name is used as a suggestion and may be altered if it clashes
   * with an existing file.
   *
   * @param name suggested file name (without directory)
   * @param conversation the conversation to write
   */
  def write(name: String, conversation: Conversation) {
    val document = new Document()
    val outStream = new FileOutputStream(getFile(name))
    PdfWriter.getInstance(document, outStream)
    document.open()
    for (message <- conversation.messages) {
      val paragraph = new Paragraph()
      for (line <- message.lines)
        paragraph.add(new Phrase(line.text))
      document.add(paragraph)
    }
    document.close()
  }

  /**
   * Get the PDF file using the given name as a suggestion.
   *
   * Resolves conflicts if the suggested name is taken, appends `(1)`, `(2)`,
   * etc. until a unique file name is found.
   *
   * Note: Disregards the file extension on the suggested file name, if any, so
   * this function always returns a `.pdf` file.
   *
   * @param filename the suggested file name
   * @return the file
   */
  private def getFile(filename: String): File = {
    val name = Files.getNameWithoutExtension(filename)
    var file = new File(directory, name + ".pdf")
    var counter = 0
    while (file.exists()) {
      counter += 1
      file = new File(directory, name + " (" + counter + ").pdf")
    }
    file
  }

}

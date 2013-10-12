package com.seaniscool.whatsapp2pdf.parser

import com.google.common.io.Files
import com.lowagie.text._
import com.lowagie.text.pdf.PdfWriter
import java.io.{File, FileOutputStream}
import java.text.SimpleDateFormat
import java.util.{Calendar, Date}
import scala.List
import scala.Some


/** Writes a [[com.seaniscool.whatsapp2pdf.parser.Conversation]] to PDF.
  *
  * @author Sean Connolly
  */
class PDFWriter(outputDirectory: File) {

  /** Write the [[com.seaniscool.whatsapp2pdf.parser.Conversation]] to a PDF file with the suggested
    * file name in the directory specified for this writer.
    *
    * Note: the name is used as a suggestion and may be altered if it clashes
    * with an existing file.
    *
    * @param sourceFile the source file, this is used to name the PDF and
    *                   resolve attachments
    * @param conversation the conversation to write
    */
  def write(sourceFile: File, conversation: Conversation): File = {
    val sourceDirectory = sourceFile.getParentFile
    val name = sourceFile.getName
    val targetFile = getFile(name)
    val document = createDocument(targetFile)
    val pdf = new PDF(document, sourceDirectory)
    var lastDate: Option[Date] = None
    for (message <- conversation.clean.messages) {
      val nextDate = message.date
      if (!lastDate.isDefined || isNewDay(lastDate.get, nextDate)) {
        lastDate = Some(nextDate)
        pdf.addDate(document, nextDate)
      }
      pdf.addMessage(document, message)
    }
    pdf.close()
    targetFile
  }

  def createDocument(file: File): Document = {
    val name = Files.getNameWithoutExtension(file.getName)
    val document = new Document(PDFStyles.PAGE_SIZE,
      PDFStyles.MARGIN_LEFT, PDFStyles.MARGIN_RIGHT,
      PDFStyles.MARGIN_TOP, PDFStyles.MARGIN_BOTTOM)
    val writer = PdfWriter.getInstance(document, new FileOutputStream(file))
    writer.setPageEvent(new PDFHeader(name))
    writer.setPageEvent(new PDFBackground())
    writer.setStrictImageSequence(true)
    document
  }

  /** Get the PDF file using the given name as a suggestion.
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
    var file = new File(outputDirectory, name + ".pdf")
    var counter = 0
    while (file.exists()) {
      counter += 1
      file = new File(outputDirectory, name + " (" + counter + ").pdf")
    }
    file
  }

  /** Determine if the `nextDate` is a different '''day''' than the `lastDate`.
    * This will indicate that we need to print a new day header.
    *
    * @param lastDate the last date
    * @param nextDate the next date
    */
  private def isNewDay(lastDate: Date, nextDate: Date): Boolean = {
    // TODO can we recycle calendars?
    val lastCalendar = Calendar.getInstance()
    val nextCalendar = Calendar.getInstance()
    lastCalendar.setTime(lastDate)
    nextCalendar.setTime(nextDate)
    val lastDay = lastCalendar.get(Calendar.DAY_OF_YEAR)
    val nextDay = nextCalendar.get(Calendar.DAY_OF_YEAR)
    val lastYear = lastCalendar.get(Calendar.YEAR)
    val nextYear = nextCalendar.get(Calendar.YEAR)
    lastDay != nextDay && lastYear != nextYear
  }


}

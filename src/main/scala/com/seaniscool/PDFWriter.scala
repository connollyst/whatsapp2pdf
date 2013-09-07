package com.seaniscool

import com.google.common.io.Files
import com.lowagie.text._
import com.lowagie.text.pdf.PdfWriter
import java.awt.Color
import java.io.{File, FileOutputStream}
import java.text.SimpleDateFormat
import java.util.{Calendar, Date}
import scala.List
import scala.Some
import scala.collection.mutable


/** Writes a [[com.seaniscool.Conversation]] to PDF.
  *
  * @author Sean Connolly
  */
class PDFWriter(outputDirectory: File) {

  /** Write the [[com.seaniscool.Conversation]] to a PDF file with the suggested
    * file name in the directory specified for this writer.
    *
    * Note: the name is used as a suggestion and may be altered if it clashes
    * with an existing file.
    *
    * @param sourceFile the source file, this is used to name the PDF and
    *                   resolve attachments
    * @param conversation the conversation to write
    */
  def write(sourceFile: File, conversation: Conversation) {
    val sourceDirectory = sourceFile.getParentFile
    val name = sourceFile.getName
    val document = createDocument(name)
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
  }

  def createDocument(name: String): Document = {
    val document = new Document(PageSize.A5,
      PDFWriter.MARGIN, PDFWriter.MARGIN, PDFWriter.MARGIN, PDFWriter.MARGIN)
    val writer = PdfWriter.getInstance(document, new FileOutputStream(getFile(name)))
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

object PDFWriter {

  val DATE = new SimpleDateFormat("dd/ww/yy")
  val TIME = new SimpleDateFormat("kk'h'mm")
  val MARGIN = margin(1)

  private def margin(inches: Int): Int = {
    // val inches = cm / 2.54
    val points = inches * 72
    points.toInt
  }

  // TODO this font is shite
  //  private val FONT_FILE = "OriginalGaramond.ttf"
  //  private val FONT_NAME = "OriginalGaramond"
  //  private val FONT_RESOURCE = getClass.getClassLoader.getResource(FONT_FILE)
  //  Log.debug("Registering custom font: " + FONT_RESOURCE)
  //  FontFactory.register(FONT_RESOURCE.getFile, FONT_NAME)
  //  private val FONT = FontFactory.getFont(FONT_NAME)
  //  FONT.setSize(6)
  val FONT = new Font(Font.HELVETICA)
  val COLORS = List(
    "#FF0000",
    "#00C000",
    "#0000FF",
    "#CC0000",
    "#FF6600",
    "#FF0033",
    "#CC0099",
    "#99CC00",
    "#9900CC",
    "#9900FF",
    "#993300",
    "#990033",
    "#660033",
    "#333366",
    "#330000",
    "#000033",
    "#000000",
    "#3366FF",
    "#66FF00",
    "#663366",
    "#66CC99"
  )

}

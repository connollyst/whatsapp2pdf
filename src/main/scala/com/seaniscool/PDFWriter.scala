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
class PDFWriter(directory: File) {

  private val colorMap = new mutable.HashMap[String, Font]

  /** Write the [[com.seaniscool.Conversation]] to a PDF file with the suggested
    * file name in the directory specified for this writer.
    *
    * Note: the name is used as a suggestion and may be altered if it clashes
    * with an existing file.
    *
    * @param name suggested file name (without directory)
    * @param conversation the conversation to write
    */
  def write(name: String, conversation: Conversation) {
    colorMap.clear()
    val document = createDocument(name)
    document.open()
    var lastDate: Option[Date] = None
    for (message <- conversation.clean.messages) {
      val nextDate = message.date
      if (!lastDate.isDefined || isNewDay(lastDate.get, nextDate)) {
        lastDate = Some(nextDate)
        addDate(document, nextDate)
      }
      addMessage(document, message)
    }
    document.close()
  }

  def createDocument(name: String): Document = {
    val document = new Document(PageSize.A5,
      PDFWriter.MARGIN, PDFWriter.MARGIN, PDFWriter.MARGIN, PDFWriter.MARGIN)
    PdfWriter.getInstance(document, new FileOutputStream(getFile(name)))
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
    var file = new File(directory, name + ".pdf")
    var counter = 0
    while (file.exists()) {
      counter += 1
      file = new File(directory, name + " (" + counter + ").pdf")
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

  private def addDate(document: Document, date: Date) {
    val paragraph = new Paragraph()
    paragraph.setAlignment(Element.ALIGN_CENTER)
    add(paragraph, PDFWriter.DATE.format(date))
    document.add(paragraph)
  }

  private def addMessage(document: Document, message: Message) {
    val paragraph = new Paragraph()
    addUser(paragraph, message)
    addTime(paragraph, message)
    add(paragraph, ":")
    addBody(paragraph, message)
    document.add(paragraph)
  }


  private def addUser(paragraph: Paragraph, message: Message) {
    val user = message.user
    val style = userStyle(user)
    if (!user.isEmpty) {
      add(paragraph, user + " ", style)
    }
  }

  private def addTime(paragraph: Paragraph, message: Message) {
    add(paragraph, PDFWriter.TIME.format(message.date))
  }

  private def addBody(paragraph: Paragraph, message: Message) {
    for (line <- message.lines)
      add(paragraph, line.body)
  }

  private def add(paragraph: Paragraph, text: String) {
    add(paragraph, text, PDFWriter.FONT)
  }

  private def add(paragraph: Paragraph, text: String, font: Font) {
    Log.debug("Printing " + text)
    val phrase = new Chunk(text)
    phrase.setFont(font)
    paragraph.add(phrase)
  }

  private def userStyle(user: String): Font = {
    if (!colorMap.contains(user)) {
      colorMap.put(user, createUserStyle(user))
    }
    colorMap.get(user).get
  }

  private def createUserStyle(user: String): Font = {
    val users = colorMap.size
    val colorIndex = (users + 1) % PDFWriter.COLORS.size
    val color = PDFWriter.COLORS(colorIndex)
    val font = new Font(PDFWriter.FONT)
    font.setColor(Color.decode(color))
    Log.debug("Created user style for " + user + ": " + color)
    font
  }

}

object PDFWriter {

  private val DATE = new SimpleDateFormat("dd/ww/yy")
  private val TIME = new SimpleDateFormat("kk'h'mm")
  private val MARGIN = margin(1)

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
  private val FONT = new Font(Font.HELVETICA)
  private val COLORS = List(
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

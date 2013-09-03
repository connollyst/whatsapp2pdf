package com.seaniscool

import com.google.common.io.Files
import com.lowagie.text.pdf.PdfWriter
import com.lowagie.text.{Element, Phrase, Paragraph, Document}
import java.io.{File, FileOutputStream}
import java.text.SimpleDateFormat
import java.util.{Calendar, Date}

/** Writes a [[com.seaniscool.Conversation]] to PDF.
  *
  * @author Sean Connolly
  */
class PDFWriter(directory: File) {

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
    val document = new Document()
    val outStream = new FileOutputStream(getFile(name))
    var lastDate: Option[Date] = None
    PdfWriter.getInstance(document, outStream)
    document.open()
    for (message <- conversation.messages) {
      val nextDate = message.date
      if (!lastDate.isDefined || isNewDay(lastDate.get, nextDate)) {
        lastDate = Some(nextDate)
        addDate(document, nextDate)
      }
      addMessage(document, message)
    }
    document.close()
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
    if (!user.isEmpty) {
      add(paragraph, user)
      add(paragraph, " ")
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
    paragraph.add(new Phrase(text))
  }

}

object PDFWriter {

  private val DATE = new SimpleDateFormat("dd/ww/yy")
  private val TIME = new SimpleDateFormat("kk'h'mm")

}

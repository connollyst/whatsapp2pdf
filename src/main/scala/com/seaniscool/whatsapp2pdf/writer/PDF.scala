package com.seaniscool.whatsapp2pdf.writer

import com.lowagie.text._
import com.seaniscool.whatsapp2pdf.Log
import com.seaniscool.whatsapp2pdf.parser.{Line, Message}
import java.awt.Color
import java.io.File
import java.util.Date
import scala.collection.mutable

/** A PDF of the WhatsApp
  *
  * @author Sean Connolly
  */
class PDF(document: Document, imagesDirectory: File) {

  private val colorMap = new mutable.HashMap[String, Font]
  private var paragraph: Paragraph = null
  // Automatically open the document to start writing
  document.open()


  private def newParagraph() = {
    if (paragraph != null) {
      document.add(paragraph)
    }
    paragraph = new Paragraph()
  }

  /**
   * Add a date to the PDF.
   *
   * @param document the document to add the message to
   * @param date the message to add to the document
   */
  def addDate(document: Document, date: Date) {
    newParagraph()
    paragraph.setAlignment(Element.ALIGN_CENTER)
    paragraph.setSpacingBefore(PDFStyles.MARGIN_DATE_TOP)
    paragraph.setSpacingAfter(PDFStyles.MARGIN_DATE_BOTTOM)
    add(paragraph, PDFStyles.DATE.format(date), PDFStyles.FONT_DATE)
  }

  /** Add a message to the PDF.
    *
    * @param document the document to add the message to
    * @param message the message to add to the document
    */
  def addMessage(document: Document, message: Message) {
    newParagraph()
    paragraph.setSpacingBefore(PDFStyles.MARGIN_MESSAGE_TOP)
    paragraph.setSpacingAfter(PDFStyles.MARGIN_MESSAGE_BOTTOM)
    addUser(paragraph, message)
    addBody(paragraph, message)
    addTime(paragraph, message)
  }

  private def addUser(paragraph: Paragraph, message: Message) {
    val user = message.user
    if (!user.isEmpty) {
      val style = userStyle(user)
      add(paragraph, user + ": ", style)
    }
  }

  private def addTime(paragraph: Paragraph, message: Message) {
    add(paragraph, PDFStyles.TIME.format(message.date), PDFStyles.FONT_TIME)
  }

  private def addBody(paragraph: Paragraph, message: Message) {
    for (line <- message.lines)
      if (line.isSupportedImage) {
        addImage(line)
      } else {
        add(paragraph, line.body.getOrElse(""))
      }
  }

  private def add(paragraph: Paragraph, text: String) {
    add(paragraph, text, PDFStyles.FONT_MAIN)
  }

  private def add(paragraph: Paragraph, text: String, font: Font) {
    Log.debug("Printing: " + text)
    val phrase = new Chunk(text)
    phrase.setFont(font)
    paragraph.add(phrase)
  }

  private def addImage(line: Line) {
    newParagraph()
    val fileName = line.image
    Log.debug("Printing image: " + fileName)
    val file = new File(imagesDirectory, fileName)
    val image = Image.getInstance(file.toURI.toURL)
    image.setAlignment(Image.MIDDLE | Image.TEXTWRAP)
    adjustImageDimensions(image)
    paragraph.add(image)
  }

  /**
   * Resize the image if it is larger than will fit on a page.
   * @param image the image to resize
   */
  private def adjustImageDimensions(image: Image) {
    val buffer: Float = 10
    val pageWidth: Float = document.getPageSize.getWidth
    val pageHeight: Float = document.getPageSize.getHeight
    val maxWidth: Float = pageWidth - document.leftMargin - document.rightMargin - buffer
    val maxHeight: Float = pageHeight - document.topMargin - document.bottomMargin - buffer
    val width: Float = image.getWidth
    val height: Float = image.getHeight
    val widthPercent: Float = (maxWidth / width) * 100
    val heightPercent: Float = (maxHeight / height) * 100
    var percentToScale: Float = Math.min(widthPercent, heightPercent)
    percentToScale = Math.floor(percentToScale).asInstanceOf[Float]
    if (percentToScale < 100) {
      Log.debug("Scaling image to " + percentToScale + "%.")
      image.scalePercent(percentToScale)
    }
  }

  private def userStyle(user: String): Font = {
    if (!colorMap.contains(user)) {
      colorMap.put(user, createUserStyle(user))
    }
    colorMap.get(user).get
  }

  private def createUserStyle(user: String): Font = {
    val users = colorMap.size
    val colorIndex = (users + 1) % PDFStyles.COLORS.size
    val color = PDFStyles.COLORS(colorIndex)
    val font = new Font(PDFStyles.FONT_MAIN)
    font.setColor(Color.decode(color))
    Log.debug("Created user style for " + user + ": " + color)
    font
  }


  def close() = {
    if (paragraph != null) {
      document.add(paragraph)
    }
    document.close()
  }

}

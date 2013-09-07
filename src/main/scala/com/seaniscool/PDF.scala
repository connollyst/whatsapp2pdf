package com.seaniscool

import java.io.File
import com.lowagie.text._
import java.util.Date
import scala.collection.mutable
import java.awt.Color

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

  def addDate(document: Document, date: Date) {
    newParagraph()
    paragraph.setAlignment(Element.ALIGN_CENTER)
    add(paragraph, PDFWriter.DATE.format(date))
  }

  def addMessage(document: Document, message: Message) {
    newParagraph()
    addUser(paragraph, message)
    addTime(paragraph, message)
    add(paragraph, ":")
    addBody(paragraph, message)
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
      if (line.isSupportedImage) {
        addImage(line)
      } else {
        add(paragraph, line.body)
      }
  }

  private def add(paragraph: Paragraph, text: String) {
    add(paragraph, text, PDFWriter.FONT)
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
    val colorIndex = (users + 1) % PDFWriter.COLORS.size
    val color = PDFWriter.COLORS(colorIndex)
    val font = new Font(PDFWriter.FONT)
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

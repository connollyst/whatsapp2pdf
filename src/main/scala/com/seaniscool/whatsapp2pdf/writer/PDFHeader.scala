package com.seaniscool.whatsapp2pdf.writer

import com.lowagie.text.pdf.{ColumnText, PdfWriter, PdfPageEventHelper}
import com.lowagie.text.{Element, Phrase, Image, Document}

/** Stamps some header text onto every page.
  *
  * @author Sean Connolly
  */
class PDFHeader(title: String) extends PdfPageEventHelper {

  private val header = new Phrase("- " + title + " -", PDFStyles.FONT_HEAD)
  private val yOffset = 550

  override def onEndPage(writer: PdfWriter, document: Document) = {
    val width = document.getPageSize.getWidth
    val xOffset = width / 2
    ColumnText.showTextAligned(writer.getDirectContent,
      Element.ALIGN_CENTER, header, xOffset, yOffset, 0)
  }

}

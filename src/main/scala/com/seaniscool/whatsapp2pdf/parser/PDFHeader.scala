package com.seaniscool.whatsapp2pdf.parser

import com.lowagie.text.pdf.{ColumnText, PdfWriter, PdfPageEventHelper}
import com.lowagie.text.{Element, Phrase, Image, Document}

/** Stamps some header text onto every page.
  *
  * @author Sean Connolly
  */
class PDFHeader(title: String) extends PdfPageEventHelper {

  private val header = new Phrase("- " + title + " -", PDFStyles.FONT_HEAD)

  override def onEndPage(writer: PdfWriter, document: Document) = {
    val width = document.getPageSize.getWidth
    ColumnText.showTextAligned(writer.getDirectContent,
      Element.ALIGN_CENTER, header, width / 2, 540, 0)
  }

}

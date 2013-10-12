package com.seaniscool.whatsapp2pdf.writer

import com.lowagie.text.pdf.{PdfWriter, PdfPageEventHelper}
import com.lowagie.text.{Image, Document}

/** Stamps a background image under every page.
  *
  * @author Sean Connolly
  */
class PDFBackground extends PdfPageEventHelper {

  private val background = Image.getInstance(PDFStyles.BACKGROUND_IMAGE)

  override def onEndPage(writer: PdfWriter, document: Document) = {
    val width = document.getPageSize.getWidth
    val height = document.getPageSize.getHeight
    writer.getDirectContentUnder.addImage(background, width, 0, 0, height, 0, 0)
  }

}

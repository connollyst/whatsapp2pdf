package com.seaniscool.whatsapp2pdf.parser

import java.text.SimpleDateFormat
import com.lowagie.text.{FontFactory, PageSize, Font}
import com.seaniscool.whatsapp2pdf.Log
import com.lowagie.text.pdf.BaseFont

/**
 *
 *
 * @author Sean Connolly
 */
object PDFStyles {

  val PAGE_SIZE = PageSize.A5
  val DATE = new SimpleDateFormat("dd/ww/yy")
  val TIME = new SimpleDateFormat("(kk:mm)")
  val MARGIN = margin(1)

  private def margin(inches: Int): Int = {
    // val inches = cm / 2.54
    val points = inches * 72
    points.toInt
  }

  private val FONT_FILE = "font/AppleGaramond.ttf"
  private val FONT_RESOURCE = getClass.getClassLoader.getResource(FONT_FILE)
  Log.debug("Registering custom font: " + FONT_RESOURCE)
  private val FONT_BASE = BaseFont.createFont(FONT_RESOURCE.getFile,
    BaseFont.CP1252, BaseFont.EMBEDDED)
  val FONT = new Font(FONT_BASE, 14)

  val COLORS = List(
    "#ba7f22",
    "#6fa96c",
    "#d71f4a",
    "#79021e",
    "#251c6c",
    "#06717b",
    "#1e61d0",
    "#e60000",
    "#45b0ba",
    "#03884e"
  )

}

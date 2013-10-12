package com.seaniscool.whatsapp2pdf.parser

import java.text.SimpleDateFormat
import com.lowagie.text.{FontFactory, PageSize, Font}
import com.seaniscool.whatsapp2pdf.Log
import com.lowagie.text.pdf.BaseFont
import java.awt.Color

/**
 *
 *
 * @author Sean Connolly
 */
object PDFStyles {

  val PAGE_SIZE = PageSize.A5
  val DATE = new SimpleDateFormat("dd/ww/yy")
  val TIME = new SimpleDateFormat(" (kk:mm)")
  val MARGIN = inToPoints(1)

  def inToPoints(inches: Double): Float = {
    val points = inches * 72
    points.toFloat
  }

  def cmToPoints(centimeters: Double): Float = {
    val inches = centimeters / 2.54
    inToPoints(inches)
  }

  // Background
  private val BACKGROUND_FILE = "background.jpg"
  private val BACKGROUND_RESOURCE = getClass.getClassLoader.getResource(BACKGROUND_FILE)
  Log.debug("Custom background: " + BACKGROUND_RESOURCE)
  val BACKGROUND_IMAGE = BACKGROUND_RESOURCE.getFile

  // Font
  private val FONT_FILE = "font/AppleGaramond.ttf"
  private val FONT_RESOURCE = getClass.getClassLoader.getResource(FONT_FILE)
  Log.debug("Custom font: " + FONT_RESOURCE)
  private val FONT_BASE = BaseFont.createFont(FONT_RESOURCE.getFile,
    BaseFont.CP1252, BaseFont.EMBEDDED)

  val FONT_MAIN = new Font(FONT_BASE, 14)
  val FONT_TIME = new Font(FONT_BASE, 11, Font.NORMAL, Color.decode("#c7c7c7"))

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

package com.seaniscool.whatsapp2pdf.parser

import com.lowagie.text.pdf.BaseFont
import com.lowagie.text.{PageSize, Font}
import com.seaniscool.whatsapp2pdf.Log
import java.awt.Color
import java.text.SimpleDateFormat

/** Fonts, colors, images, and the like used to style the PDF.
  *
  * @author Sean Connolly
  */
object PDFStyles {

  val PAGE_SIZE = PageSize.A5
  val DATE = new SimpleDateFormat("dd/ww/yyyy")
  val TIME = new SimpleDateFormat(" (kk:mm)")
  val MARGIN_TOP = cmToPoints(1.8)
  val MARGIN_BOTTOM = inToPoints(1)
  val MARGIN_LEFT = cmToPoints(3)
  val MARGIN_RIGHT = cmToPoints(3)

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
  private val FONT_FILE = "font/GaramondPremrPro.otf"
  private val FONT_RESOURCE = getClass.getClassLoader.getResource(FONT_FILE)
  Log.debug("Custom font: " + FONT_RESOURCE)
  private val FONT_BASE = BaseFont.createFont(FONT_RESOURCE.getFile,
    BaseFont.CP1252, BaseFont.EMBEDDED)

  val FONT_HEAD = new Font(FONT_BASE, 10)
  val FONT_MAIN = new Font(FONT_BASE, 14)
  val FONT_DATE = new Font(FONT_BASE, 11)
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

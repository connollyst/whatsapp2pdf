package com.seaniscool.whatsapp2pdf.writer

import com.lowagie.text.pdf.BaseFont
import com.lowagie.text.{PageSize, Font}
import com.seaniscool.whatsapp2pdf.{Directory, Log}
import java.awt.Color
import java.text.SimpleDateFormat
import java.io.{FileOutputStream, IOException, FileNotFoundException, File}
import com.google.common.io.{ByteStreams, Files}

/** Fonts, colors, images, and the like used to style the PDF.
  *
  * @author Sean Connolly
  */
object PDFStyles {

  val PAGE_SIZE = PageSize.A5
  val DATE = new SimpleDateFormat("dd/ww/yyyy")
  val TIME = new SimpleDateFormat(" (kk:mm)")

  // Margins
  val MARGIN_PAGE_TOP = cmToPoints(2.5)
  val MARGIN_PAGE_BOTTOM = inToPoints(1)
  val MARGIN_PAGE_LEFT = cmToPoints(3)
  val MARGIN_PAGE_RIGHT = cmToPoints(3)
  val MARGIN_DATE_TOP = cmToPoints(0.7)
  val MARGIN_DATE_BOTTOM = cmToPoints(0.5)
  val MARGIN_MESSAGE_TOP = cmToPoints(0.25)
  val MARGIN_MESSAGE_BOTTOM = cmToPoints(0.25)

  def inToPoints(inches: Double): Float = {
    val points = inches * 72
    points.toFloat
  }

  def cmToPoints(centimeters: Double): Float = {
    val inches = centimeters / 2.54
    inToPoints(inches)
  }

  // Background
  private val BACKGROUND_RESOURCE = "background.jpg"
  val BACKGROUND_IMAGE = writeResourceToTempFile(BACKGROUND_RESOURCE)

  // Font
  private val FONT_RESOURCE = "font/GaramondPremrPro.otf"
  private val FONT_FILE = writeResourceToTempFile(FONT_RESOURCE)
  private val FONT = BaseFont.createFont(FONT_FILE, BaseFont.CP1252, BaseFont.EMBEDDED)
  val FONT_HEAD = new Font(FONT, 10)
  val FONT_MAIN = new Font(FONT, 14)
  val FONT_DATE = new Font(FONT, 11)
  val FONT_TIME = new Font(FONT, 11, Font.NORMAL, Color.decode("#c7c7c7"))

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

  /** Writes a bundled resource to a temporary directory to be
    *
    * @param resourcePath the path of the resource relative to the application root
    * @return the absolute path of the temporary file
    */
  private def writeResourceToTempFile(resourcePath: String): String = {
    val resource = getClass.getClassLoader.getResource(resourcePath)
    if (resource == null) {
      throw new IOException("No resource found at " + resourcePath)
    }
    val resourceFile = new File(resource.getFile)
    val fileName = resourceFile.getName
    val tempDir = Directory.temp()
    val file = new File(tempDir, fileName)
    Log.debug("Copying resource: " + resource + " to " + file)
    if (file.exists()) {
      throw new IOException("Temporary resource already exists: " + file)
    }
    val inputStream = resource.openStream()
    val outputStream = new FileOutputStream(file)
    ByteStreams.copy(inputStream, outputStream)
    file.deleteOnExit()
    file.getAbsolutePath
  }

}

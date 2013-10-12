package com.seaniscool.whatsapp2pdf.parser

import com.seaniscool.whatsapp2pdf.writer.PDFWriter
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{PrivateMethodTester, Matchers, FlatSpec}

/** Test cases for [[com.seaniscool.whatsapp2pdf.writer.PDFWriter]].
  *
  * @author Sean Connolly
  */
@RunWith(classOf[JUnitRunner])
class TestPDFWriter extends FlatSpec with Matchers with PrivateMethodTester {

  val isNewDay = PrivateMethod[Boolean]('isNewDay)

  /* Conversation with 'You changed the subject..' line  */

  "PDFWriter" should "not detect a new day on the same day" in {
    val writer = new PDFWriter(new File(""))
    val newDay = writer invokePrivate isNewDay(date("12/30/2000"), date("12/30/2000"))
    newDay should be(false)
  }

  it should "detect a new day between days" in {
    val writer = new PDFWriter(new File(""))
    val newDay = writer invokePrivate isNewDay(date("12/30/2000"), date("12/31/2000"))
    newDay should be(true)
  }

  it should "detect a new day across new year" in {
    val writer = new PDFWriter(new File(""))
    val newDay = writer invokePrivate isNewDay(date("12/31/2000"), date("01/01/2001"))
    newDay should be(true)
  }


  private def date(date: String): Date = {
    new SimpleDateFormat("dd/MM/yyyy").parse(date)
  }

}

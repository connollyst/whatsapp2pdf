package com.seaniscool.whatsapp2pdf.parser

import java.io.File
import java.util.{GregorianCalendar, Date}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{Matchers, FlatSpec}

/** Test cases for [[com.seaniscool.whatsapp2pdf.parser.Line]] parsing.
  *
  * @author Sean Connolly
  */
@RunWith(classOf[JUnitRunner])
class TestLineParsing extends FlatSpec with Matchers {

  private val expectedDate = new GregorianCalendar(2013, 11, 30, 23, 59, 00).getTime
  private val parser = new WhatsAppParser()

  /* Conversation with 'You changed the subject..' line  */

  "WhatsAppParser" should "produce a one message conversation for the 'renamed' announcement" in {
    assertMessageCount("/conversations/renamed.txt", 1)
  }

  it should "correctly parse the 'renamed' announcement date" in {
    assertDate("/conversations/renamed.txt", 0, 0, expectedDate)
  }

  it should "correctly parse the 'renamed' announcement user" in {
    assertNoUser("/conversations/renamed.txt", 0, 0)
  }

  it should "correctly parse the 'renamed' announcement body" in {
    assertBody("/conversations/renamed.txt", 0, 0, "You changed the subject to “:O”")
  }

  /* Conversation with a few simple messages */

  it should "correctly parse simple conversation messages" in {
    assertMessageCount("/conversations/simple.txt", 5)
  }

  it should "correctly parse simple conversation users" in {
    assertUser("/conversations/simple.txt", 0, 0, "UserA")
    assertUser("/conversations/simple.txt", 1, 0, "UserB")
    assertUser("/conversations/simple.txt", 2, 0, "UserC")
    assertUser("/conversations/simple.txt", 3, 0, "UserD")
    assertUser("/conversations/simple.txt", 4, 0, "UserE")
  }

  it should "correctly parse simple conversation body" in {
    assertBody("/conversations/simple.txt", 0, 0, "Message 1")
    assertBody("/conversations/simple.txt", 1, 0, "Message 2")
    assertBody("/conversations/simple.txt", 2, 0, "Message 3")
    assertBody("/conversations/simple.txt", 3, 0, "Message 4")
    assertBody("/conversations/simple.txt", 4, 0, "Message 5")
  }

  /* Conversation with a message with multiple lines in it */

  it should "correctly parse multiline conversation messages" in {
    assertMessageCount("/conversations/multiline.txt", 5)
  }

  it should "correctly parse multiline conversation users" in {
    assertUser("/conversations/multiline.txt", 0, 0, "UserA")
    assertUser("/conversations/multiline.txt", 1, 0, "UserB")
    assertUser("/conversations/multiline.txt", 2, 0, "UserC")
    assertNoUser("/conversations/multiline.txt", 2, 1)
    assertNoUser("/conversations/multiline.txt", 2, 2)
    assertNoUser("/conversations/multiline.txt", 2, 3)
    assertNoUser("/conversations/multiline.txt", 2, 4)
    assertNoUser("/conversations/multiline.txt", 2, 5)
    assertUser("/conversations/multiline.txt", 3, 0, "UserD")
    assertUser("/conversations/multiline.txt", 4, 0, "UserE")
  }

  it should "correctly parse multiline conversation body" in {
    assertBody("/conversations/multiline.txt", 0, 0, "Message 1")
    assertBody("/conversations/multiline.txt", 1, 0, "Message 2")
    assertBody("/conversations/multiline.txt", 2, 0, "Message 3")
    assertBody("/conversations/multiline.txt", 2, 1, "Message 4")
    assertBody("/conversations/multiline.txt", 2, 2, "Message 5")
    assertBody("/conversations/multiline.txt", 2, 3, "Message 6")
    assertBody("/conversations/multiline.txt", 2, 4, "Message 7")
    assertBody("/conversations/multiline.txt", 2, 5, "Message 8")
    assertBody("/conversations/multiline.txt", 3, 0, "Message 9")
    assertBody("/conversations/multiline.txt", 4, 0, "Message 10")
  }

  /* Utility test functions */

  private def assertDate(fileName: String, messageIndex: Int, lineIndex: Int, expectedDate: Date) = {
    val line = getLine(fileName, messageIndex, lineIndex)
    line.date.get should be(expectedDate)
  }

  private def assertNoDate(fileName: String, messageIndex: Int, lineIndex: Int, expectedDate: Date) = {
    val line = getLine(fileName, messageIndex, lineIndex)
    line.date should be(None)
  }

  private def assertUser(fileName: String, messageIndex: Int, lineIndex: Int, expectedUser: String) = {
    val line = getLine(fileName, messageIndex, lineIndex)
    line.user.isDefined should be(true)
    line.user.get should be(expectedUser)
  }

  private def assertNoUser(fileName: String, messageIndex: Int, lineIndex: Int) = {
    val line = getLine(fileName, messageIndex, lineIndex)
    line.user should be(None)
  }

  private def assertBody(fileName: String, messageIndex: Int, lineIndex: Int, expectedBody: String) = {
    val line = getLine(fileName, messageIndex, lineIndex)
    line.body.isDefined should be(true)
    line.body.get should be(expectedBody)
  }

  private def assertNoBody(fileName: String, messageIndex: Int, lineIndex: Int, expectedBody: String) = {
    val line = getLine(fileName, messageIndex, lineIndex)
    line.body should be(None)
  }

  private def assertMessageCount(fileName: String, expectedMessages: Int) = {
    val file = getResourceFile(fileName)
    val conversation = parser.parse(file)
    val actualMessages = conversation.messages.size
    actualMessages should be(expectedMessages)
  }

  private def getLine(fileName: String, messageIndex: Int, lineIndex: Int) = {
    val message = getMessage(fileName, messageIndex)
    message.linesPrintable(lineIndex)
  }

  private def getMessage(fileName: String, messageIndex: Int) = {
    val conversation = getConversation(fileName)
    conversation.messages(messageIndex)
  }

  private def getConversation(fileName: String) = {
    val file = getResourceFile(fileName)
    parser.parse(file)
  }

  private def getResourceFile(fileName: String) = {
    val resource = getClass.getResource(fileName)
    new File(resource.getFile)
  }

}

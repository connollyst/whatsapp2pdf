import com.seaniscool.whatsapp2pdf.parser.WhatsAppParser
import java.io.File
import java.util.{GregorianCalendar, Calendar, Date}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{Matchers, FlatSpec}

/**
 *
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
    assertLineDate("/conversations/renamed.txt", 0, 0, expectedDate)
  }

  it should "correctly parse the 'renamed' announcement user" in {
    assertNoLineUser("/conversations/renamed.txt", 0, 0)
  }

  it should "correctly parse the 'renamed' announcement body" in {
    assertLineBody("/conversations/renamed.txt", 0, 0, "You changed the subject to “:O”")
  }

  /* Conversation with 'You changed the subject..' line  */

  it should "correctly parse simple conversation messages" in {
    assertMessageCount("/conversations/simple.txt", 10)
  }
  it should "correctly parse simple conversation users" in {
    assertLineUser("/conversations/simple.txt", 0, 0, "UserA")
    assertLineUser("/conversations/simple.txt", 1, 0, "UserA")
    assertLineUser("/conversations/simple.txt", 2, 0, "UserB")
    assertLineUser("/conversations/simple.txt", 3, 0, "UserA")
    assertLineUser("/conversations/simple.txt", 4, 0, "UserB")
    assertLineUser("/conversations/simple.txt", 5, 0, "UserA")
    assertLineUser("/conversations/simple.txt", 6, 0, "UserA")
    assertLineUser("/conversations/simple.txt", 7, 0, "UserC")
    assertLineUser("/conversations/simple.txt", 8, 0, "UserD")
    assertLineUser("/conversations/simple.txt", 9, 0, "UserB")
  }

  /* Utility functions */

  private def assertLineDate(fileName: String, messageIndex: Int, lineIndex: Int, expectedDate: Date) = {
    val line = getMessageLine(fileName, messageIndex, lineIndex)
    line.containsDate should be(true)
    line.date.get should be(expectedDate)
  }

  private def assertNoLineUser(fileName: String, messageIndex: Int, lineIndex: Int) = {
    val line = getMessageLine(fileName, messageIndex, lineIndex)
    line.containsUser should be(false)
    line.user should be(None)
  }

  private def assertLineUser(fileName: String, messageIndex: Int, lineIndex: Int, expectedUser: String) = {
    val line = getMessageLine(fileName, messageIndex, lineIndex)
    line.user.get should be(expectedUser)
  }

  private def assertLineBody(fileName: String, messageIndex: Int, lineIndex: Int, expectedBody: String) = {
    val line = getMessageLine(fileName, messageIndex, lineIndex)
    line.body.get should be(expectedBody)
  }

  private def assertMessageCount(fileName: String, expectedMessages: Int) = {
    val file = getResourceFile(fileName)
    val conversation = parser.parse(file)
    conversation.messages.size should be(expectedMessages)
  }

  private def getMessageLine(fileName: String, messageIndex: Int, lineIndex: Int) = {
    val message = getMessage(fileName, messageIndex)
    message.lines(lineIndex)
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

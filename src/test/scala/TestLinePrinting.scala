import com.seaniscool.whatsapp2pdf.parser.Line
import org.junit.runner.RunWith
import org.scalatest._
import org.scalatest.junit.JUnitRunner

/** Test how well the [[com.seaniscool.whatsapp2pdf.parser.Line]] determines if
  * it should be printed or not.
  *
  * Note: vCards aren't omitted as media on Android
  */
@RunWith(classOf[JUnitRunner])
class TestLinePrinting extends FlatSpec with Matchers {

  val headerPrefix = "01/01/13 01:01:01: UserName: "
  val normalText = "Hello World"
  val locationHttp = "location: http://maps.google.com/?q=41.38046,2.16724"
  val locationHttps = "location: http://maps.google.com/?q=41.38046,2.16724"
  val omittedMediaIOS = "<media omitted>"
  val omittedMediaAndroid = "<Media omitted>"
  val omittedVCardIOS = "<vCard omitted>"
  val attachedVCardIOS = "vCard attached: DOSBCN.vcf"
  val attachedVCardAndroid = "DOSBCN.vcf (file attached)"

  def attachmentIOS(extension: String): String = {
    "cceea549d0f70fe40e80fe6b1da2b2fd." + extension + " <attached>"
  }

  def attachmentAndroid(extension: String): String = {
    "IMG-20130906-WA0001." + extension + " (file attached)"
  }

  "A Line" should "not be printed if it is a normal header" in {
    assertPrintable(headerPrefix + normalText)
  }

  it should "be printed if it is a normal line" in {
    assertPrintable(normalText)
  }

  /* IOS ATTACHMENTS */

  it should "not be printed if it contains omitted media in header (IOS)" in {
    assertNotPrintable(headerPrefix + omittedMediaIOS)
  }

  it should "not be printed if it contains omitted media (IOS)" in {
    assertNotPrintable(omittedMediaIOS)
  }

  it should "not be printed if it contains omitted vCard in header (IOS)" in {
    assertNotPrintable(headerPrefix + omittedVCardIOS)
  }
  it should "not be printed if it contains omitted vCard (IOS)" in {
    assertNotPrintable(omittedVCardIOS)
  }

  it should "not be printed if it contains attached vCard in header (IOS)" in {
    assertNotPrintable(headerPrefix + attachedVCardIOS)
  }

  it should "not be printed if it contains attached vCard (IOS)" in {
    assertNotPrintable(attachedVCardIOS)
  }

  it should "not be printed if it contains omitted location in header (IOS)" in {
    assertNotPrintable(headerPrefix + locationHttp)
  }

  it should "not be printed if it contains location (IOS)" in {
    assertNotPrintable(locationHttp)
  }

  it should "not be printed if it contains location in header (https) (IOS)" in {
    assertNotPrintable(headerPrefix + locationHttps)
  }

  it should "not be printed if it contains location (https) (IOS)" in {
    assertNotPrintable(locationHttps)
  }

  it should "not be printed if it contains attachment in header (IOS)" in {
    assertNotPrintable(headerPrefix + attachmentIOS("aac"))
  }

  it should "not be printed if it contains attachment (IOS)" in {
    assertNotPrintable(attachmentIOS("aac"))
  }

  it should "be printed if it contains JPEG in header (IOS)" in {
    assertPrintable(headerPrefix + attachmentIOS("JPEG"))
  }

  it should "be printed if it contains JPEG (IOS)" in {
    assertPrintable(attachmentIOS("JPEG"))
  }

  it should "be printed if it contains JPG in header (IOS)" in {
    assertPrintable(headerPrefix + attachmentIOS("JPG"))
  }

  it should "be printed if it contains JPG (IOS)" in {
    assertPrintable(attachmentIOS("JPG"))
  }

  it should "be printed if it contains PNG in header (IOS)" in {
    assertPrintable(headerPrefix + attachmentIOS("PNG"))
  }

  it should "be printed if it contains PNG (IOS)" in {
    assertPrintable(attachmentIOS("PNG"))
  }

  it should "be printed if it contains GIF in header (IOS)" in {
    assertPrintable(headerPrefix + attachmentIOS("GIF"))
  }

  it should "be printed if it contains GIF (IOS)" in {
    assertPrintable(attachmentIOS("GIF"))
  }

  it should "be printed if it contains jpeg in header (IOS)" in {
    assertPrintable(headerPrefix + attachmentIOS("jpeg"))
  }

  it should "be printed if it contains jpeg (IOS)" in {
    assertPrintable(attachmentIOS("jpeg"))
  }

  it should "be printed if it contains jpg in header (IOS)" in {
    assertPrintable(headerPrefix + attachmentIOS("jpg"))
  }

  it should "be printed if it contains jpg (IOS)" in {
    assertPrintable(attachmentIOS("jpg"))
  }

  it should "be printed if it contains png in header (IOS)" in {
    assertPrintable(headerPrefix + attachmentIOS("png"))
  }

  it should "be printed if it contains png (IOS)" in {
    assertPrintable(attachmentIOS("png"))
  }

  it should "be printed if it contains gif in header (IOS)" in {
    assertPrintable(headerPrefix + attachmentIOS("gif"))
  }

  it should "be printed if it contains gif (IOS)" in {
    assertPrintable(attachmentIOS("gif"))
  }

  /* ANDROID ATTACHMENTS */

  it should "not be printed if it contains omitted media in header (Android)" in {
    assertNotPrintable(headerPrefix + omittedMediaAndroid)
  }

  it should "not be printed if it contains omitted media (Android)" in {
    assertNotPrintable(omittedMediaAndroid)
  }

  it should "not be printed if it contains attached vCard in header (Android)" in {
    assertNotPrintable(headerPrefix + attachedVCardAndroid)
  }

  it should "not be printed if it contains attached vCard (Android)" in {
    assertNotPrintable(attachedVCardAndroid)
  }

  it should "not be printed if it contains omitted location in header (Android)" in {
    assertNotPrintable(headerPrefix + locationHttp)
  }

  it should "not be printed if it contains location (Android)" in {
    assertNotPrintable(locationHttp)
  }

  it should "not be printed if it contains location in header (https) (Android)" in {
    assertNotPrintable(headerPrefix + locationHttps)
  }

  it should "not be printed if it contains location (https) (Android)" in {
    assertNotPrintable(locationHttps)
  }

  it should "not be printed if it contains attachment in header (Android)" in {
    assertNotPrintable(headerPrefix + attachmentAndroid("aac"))
  }

  it should "not be printed if it contains attachment (Android)" in {
    assertNotPrintable(attachmentAndroid("aac"))
  }

  it should "be printed if it contains JPEG in header (Android)" in {
    assertPrintable(headerPrefix + attachmentAndroid("JPEG"))
  }

  it should "be printed if it contains JPEG (Android)" in {
    assertPrintable(attachmentAndroid("JPEG"))
  }

  it should "be printed if it contains JPG in header (Android)" in {
    assertPrintable(headerPrefix + attachmentAndroid("JPG"))
  }

  it should "be printed if it contains JPG (Android)" in {
    assertPrintable(attachmentAndroid("JPG"))
  }

  it should "be printed if it contains PNG in header (Android)" in {
    assertPrintable(headerPrefix + attachmentAndroid("PNG"))
  }

  it should "be printed if it contains PNG (Android)" in {
    assertPrintable(attachmentAndroid("PNG"))
  }

  it should "be printed if it contains GIF in header (Android)" in {
    assertPrintable(headerPrefix + attachmentAndroid("GIF"))
  }

  it should "be printed if it contains GIF (Android)" in {
    assertPrintable(attachmentAndroid("GIF"))
  }

  it should "be printed if it contains jpeg in header (Android)" in {
    assertPrintable(headerPrefix + attachmentAndroid("jpeg"))
  }

  it should "be printed if it contains jpeg (Android)" in {
    assertPrintable(attachmentAndroid("jpeg"))
  }

  it should "be printed if it contains jpg in header (Android)" in {
    assertPrintable(headerPrefix + attachmentAndroid("jpg"))
  }

  it should "be printed if it contains jpg (Android)" in {
    assertPrintable(attachmentAndroid("jpg"))
  }

  it should "be printed if it contains png in header (Android)" in {
    assertPrintable(headerPrefix + attachmentAndroid("png"))
  }

  it should "be printed if it contains png (Android)" in {
    assertPrintable(attachmentAndroid("png"))
  }

  it should "be printed if it contains gif in header (Android)" in {
    assertPrintable(headerPrefix + attachmentAndroid("gif"))
  }

  it should "be printed if it contains gif (Android)" in {
    assertPrintable(attachmentAndroid("gif"))
  }

  private def assertPrintable(text: String) {
    assertPrintable(text, true)
  }

  private def assertNotPrintable(text: String) {
    assertPrintable(text, false)
  }

  private def assertPrintable(text: String, expected: Boolean) {
    new Line(text, 1).isPrintable should be(expected)
  }

}
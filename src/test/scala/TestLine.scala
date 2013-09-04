import com.seaniscool.Line
import org.junit.runner.RunWith
import org.scalatest._
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class TestLine extends FlatSpec with Matchers {

  val headerPrefix = "01/01/13 01:01:01: UserName: "
  val normalText = "Hello World"
  val omittedMedia = "<media omitted>"
  val omittedVCard = "<vCard omitted>"
  val attachedVCard = "vCard attached: Lau.vcf"
  val attachment = "cceea549d0f70fe40e80fe6b1da2b2fd.aac <attached>"
  val locationHttp = "location: http://maps.google.com/?q=41.38046,2.16724"
  val locationHttps = "location: http://maps.google.com/?q=41.38046,2.16724"

  "A Line" should "not be excluded if it is a normal header" in {
    val text = headerPrefix + normalText
    val line = new Line(text, 1)
    line.isExcludable should be(false)
  }

  it should "not be excluded if it is a normal line" in {
    val text = normalText
    val line = new Line(text, 1)
    line.isExcludable should be(false)
  }

  it should "be excluded if it contains omitted media in header" in {
    val text = headerPrefix + omittedMedia
    val line = new Line(text, 1)
    line.isExcludable should be(true)
  }

  it should "be excluded if it contains omitted media" in {
    val text = omittedMedia
    val line = new Line(text, 1)
    line.isExcludable should be(true)
  }

  it should "be excluded if it contains omitted vCard in header" in {
    val text = headerPrefix + omittedVCard
    val line = new Line(text, 1)
    line.isExcludable should be(true)
  }
  it should "be excluded if it contains omitted vCard" in {
    val text = omittedVCard
    val line = new Line(text, 1)
    line.isExcludable should be(true)
  }

  it should "be excluded if it contains attached vCard in header" in {
    val text = headerPrefix + attachedVCard
    val line = new Line(text, 1)
    line.isExcludable should be(true)
  }

  it should "be excluded if it contains attached vCard" in {
    val text = attachedVCard
    val line = new Line(text, 1)
    line.isExcludable should be(true)
  }

  it should "be excluded if it contains omitted attachment in header" in {
    val text = headerPrefix + attachment
    val line = new Line(text, 1)
    line.isExcludable should be(true)
  }

  it should "be excluded if it contains omitted attachment" in {
    val text = attachment
    val line = new Line(text, 1)
    line.isExcludable should be(true)
  }

  it should "be excluded if it contains omitted location in header" in {
    val text = headerPrefix + locationHttp
    val line = new Line(text, 1)
    line.isExcludable should be(true)
  }

  it should "be excluded if it contains omitted location" in {
    val text = locationHttp
    val line = new Line(text, 1)
    line.isExcludable should be(true)
  }

  it should "be excluded if it contains omitted location in header (https)" in {
    val text = headerPrefix + locationHttps
    val line = new Line(text, 1)
    line.isExcludable should be(true)
  }

  it should "be excluded if it contains omitted location (https)" in {
    val text = locationHttps
    val line = new Line(text, 1)
    line.isExcludable should be(true)
  }


}
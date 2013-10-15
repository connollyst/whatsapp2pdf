package com.seaniscool.whatsapp2pdf

import com.seaniscool.whatsapp2pdf.cmd.CommandLineArgs
import java.io.{OutputStream, PrintWriter, StringWriter}

/** We don't do any real logging in this application.
  * This logger lets us print debug statements to standard out if debugging is
  * enabled in the command line arguments.
  *
  * @author Sean Connolly
  */
object Log {

  // Logs default to System.out but can be redirected if needed
  private var out: OutputStream = System.out

  def register(stream: OutputStream) = {
    this.out = stream
  }

  def debug(message: String) = {
    if (CommandLineArgs.debugMode) {
      printLine(message)
    }
  }

  def info(message: String) = {
    printLine(message)
  }

  def error(message: String) = {
    printLine("ERROR: " + message)
  }

  def error(message: String, cause: Throwable) = {
    cause.printStackTrace()
    printLine("ERROR: " + message)
    // print stack trace
    val errors = new StringWriter()
    cause.printStackTrace(new PrintWriter(errors))
    printLine(errors.toString)
  }

  private def printLine(message: String) = {
    out.write((message + '\n').getBytes)
  }

}

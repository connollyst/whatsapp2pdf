package com.seaniscool.whatsapp2pdf

import com.seaniscool.whatsapp2pdf.cmd.CommandLineArgs

/** We don't do any real logging in this application.
  * This logger lets us print debug statements to standard out if debugging is
  * enabled in the command line arguments.
  *
  * @author Sean Connolly
  */
object Log {

  def debug(message: String) = {
    if (CommandLineArgs.debugMode) {
      println(message)
    }
  }

  def info(message: String) = {
    println(message)
  }

}

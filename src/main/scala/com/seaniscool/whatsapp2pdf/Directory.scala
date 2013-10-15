package com.seaniscool.whatsapp2pdf

import java.io.File
import com.google.common.io.Files

/** A utility for resolving user directories.
  *
  * @author Sean Connolly
  */
object Directory {

  private var TEMP_DIR: Option[File] = None

  /** Get the temporary directory used by the application.
    *
    * @return the temporary directory
    */
  def temp(): File = {
    if (!TEMP_DIR.isDefined) {
      TEMP_DIR = Some(Files.createTempDir())
      TEMP_DIR.get.deleteOnExit()
    }
    TEMP_DIR.get
  }

  /** Get the user's current directory, as defined by the system.
    *
    * @return the user's current directory
    */
  def current(): File = {
    new File(System.getProperty("user.dir"))
  }

  /** Get the user's home directory, as defined by the system.
    *
    * @return the user's home directory
    */
  def home(): File = {
    new File(System.getProperty("user.home"))
  }

  /** Get the user's desktop directory if possible. If not, their home directory,
    * as defined by the system, is returned.
    *
    * @return the user's desktop directory, or home directory
    */
  def desktop(): File = {
    val homeDirectory = home()
    val desktopDirectory = new File(homeDirectory, "Desktop")
    if (desktopDirectory.exists()) {
      desktopDirectory
    } else {
      homeDirectory
    }
  }

}

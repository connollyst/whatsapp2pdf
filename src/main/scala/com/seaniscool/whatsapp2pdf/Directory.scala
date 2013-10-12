package com.seaniscool.whatsapp2pdf

import java.io.File

/** A utility for resolving user directories.
  *
  * @author Sean Connolly
  */
object Directory {

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

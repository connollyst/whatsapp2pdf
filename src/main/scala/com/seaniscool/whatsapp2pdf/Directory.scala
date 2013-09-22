package com.seaniscool.whatsapp2pdf

import java.io.File

/**
 *
 *
 * @author Sean Connolly
 */
object Directory {

  def current(): File = {
    new File(System.getProperty("user.dir"))
  }

  def home(): File = {
    new File(System.getProperty("user.home"))
  }

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

WhatsApp2PDF
============

WhatsApp2PDF is a command line utility, written in Scala, to process WhatsApp
conversation dumps to formatted PDF.

Use
---

The simplest use case is to run the tool on a single file:

    whatsapp2pdf file.txt

This will produce `file.pdf` in your current directory.

You can hand _whatsapp2pdf_ any number of files:

    whatsapp2pdf file_1.txt file_2.txt file_3.txt

Which will result in `file_1.pdf`, `file_2.pdf`, and `file_3.pdf` in your
current directory.

You can specify the output directory with the `-o` flag:

    whatsapp2pdf file.txt -o pdfs/

Input Format
------------

**The only accepted input format for the text files to be converted to PDF, is
a direct export of a WhatsApp conversation.**

To export a conversation from WhatsApp:
* Open WhatsApp
* Navigate to the conversation
* Tap the `...` button in the top right to open the conversation menu.
* Select `More`, the last option in the menu.
* Choose `Email Conversation`
* If you want to include images in the resulting PDF, select `Attach media`.
  Note that most email providers have a limit on the size an email can be.

Notes
-----

    // TODO

Build
-----
To build WhatsApp2PDF from source, you will need a
[Java Development Kit (JDK)](http://www.oracle.com/technetwork/java/javase/downloads),
[Maven](http://maven.apache.org/), and an internet connection. All dependencies
(including the Scala compiler) will be downloaded by Maven. With your
environment all set up, the following commands will download the source code and
build WhatsApp2PDF..

    git clone git@github.com:connollyst/whatsapp2pdf
    cd whatsapp2pdf
    mvn clean package

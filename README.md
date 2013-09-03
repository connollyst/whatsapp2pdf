WhatsApp2PDF
============

WhatsApp2PDF is a command line utility, written in Scala, to process WhatsApp
conversation dumps to formatted PDF.

Use
---

The simplest use case is to run the tool on a single file:

    whatsapp2pdf file.txt

This will produce `file.pdf` in your current directory.

You can hand `whatsapp2pdf` any number of files:

    whatsapp2pdf file_1.txt file_2.txt file_3.txt

Which will result in `file_1.pdf`, `file_2.pdf`, and `file_3.pdf` in your
current directory.

You can specify the output directory with the `-o` flag:

    whatsapp2pdf file.txt -o pdfs/

Input Format
------------

    // TODO

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

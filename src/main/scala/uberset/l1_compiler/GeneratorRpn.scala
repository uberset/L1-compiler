/*
  Author: uberset
  Date: 2015-12-26
  Licence: GPL v2
*/

package uberset.l1_compiler

import java.io.PrintWriter

class GeneratorRpn(
    val out: PrintWriter
) extends Generator {

    override def begin(): Unit = out.print("Program(")
    override def end(): Unit = out.print(")")
    override def library(): Unit = ()

    override def printStr(): Unit = out.print("printStr ")
    override def printInt(): Unit = out.print("printInt ")
    override def printChr(): Unit = out.print("printChr ")
    override def printBoo(): Unit = out.print("printBoo ")
    override def printLn(): Unit = out.print("printLn ")

    override def pushStr(v: String): Unit = out.print(s"Str($v) ")
    override def pushInt(v: String): Unit = out.print(s"Int($v) ")
    override def pushChr(v: Char): Unit = out.print(s"Chr(${v.toInt}) ")
    override def pushBoo(v: Boolean): Unit = out.print(s"Boo($v) ")

    override def negI(): Unit = out.print("negI ")
    override def mulI(): Unit = out.print("mulI ")
    override def divI(): Unit = out.print("divI ")
    override def modI(): Unit = out.print("modI ")
    //override def addI(): Unit = out.print("addI ")
    //override def subI(): Unit = out.print("subI ")
}

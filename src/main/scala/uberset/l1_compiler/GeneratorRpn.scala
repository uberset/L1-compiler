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

    override def end(): Unit = out.print(")")

    override def printStr(): Unit = out.print("printStr ")

    override def printLn(): Unit = out.print("printLn ")

    override def begin(): Unit = out.print("Program(")

    override def pushBoo(v: Boolean): Unit = out.print(s"Boo($v) ")

    override def pushChr(v: Char): Unit = out.print(s"Chr(${v.toInt}) ")

    override def negI(): Unit = out.print("negI ")

    override def printBoo(): Unit = out.print("printBoo ")

    override def pushInt(v: String): Unit = out.print(s"Int($v) ")

    override def printChr(): Unit = out.print("printChr ")

    override def pushStr(v: String): Unit = out.print(s"Str($v) ")

    override def library(): Unit = ()

    override def printInt(): Unit = out.print("printInt ")

}

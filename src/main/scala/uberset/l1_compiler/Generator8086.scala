/*
  Author: uberset
  Date: 2015-12-20
  Licence: GPL v2
*/

package uberset.l1_compiler

import java.io.PrintWriter

class Generator8086(
    val out: PrintWriter
) extends Generator {

    var dataCount: Int = 0

    override def begin() {
        out.println("org 100h")
        out.println("mov ax, -1")
    }

    override def end() {
        out.println("mov ax,0x4c00")
        out.println("int 0x21")
    }

    override def library(): Unit = {
        Library8086.library(out)
    }

    override def printLn() {
        out.println("call println")
    }

    override def pushBoo(v: Boolean) {
        val i = if(v) 1 else 0
        out.println( "push ax")
        out.println(s"mov ax, $i")
    }

    override def printBoo() {
        out.println("call printb")
    }

    override def pushChr(v: Char) {
        val s = v.toShort // shall be in 0..255
        out.println( "push ax")
        out.println(s"mov ax, $s")
    }

    override def printChr() {
        out.println("call printc")
    }

    override def pushInt(v: String) {
        val i = v.toInt // shall be in 0..65535
        out.println( "push ax")
        out.println(s"mov ax, $i")
    }

    override def printInt() {
        out.println("call printi")
    }

    override def pushStr(str: String) {
        val nr = dataCount; dataCount += 1
        val lbl = s"DATA_$nr"
        val size = str.length
        out.println(   "section .data")
        out.println(  s"dw $size")
        out.println(s"""$lbl: db "$str"""")
        out.println(   "section .text")
        out.println(   "push ax")
        out.println(  s"mov ax, $lbl")
    }

    override def negI(): Unit = {
        // ( a -> - a )
        out.println("neg ax")
    }

    override def mulI(): Unit = {
        // ( a b -> a * b )
        out.println("mov bx, ax")
        out.println("pop ax")
        out.println("imul ax, bx")
    }

    override def divI(): Unit = {
        // ( a b -> a / b )
        out.println("mov bx, ax")
        out.println("pop ax")
        out.println("cwd")
        out.println("idiv bx")
    }

    override def modI(): Unit = {
        // ( a b -> a % b )
        out.println("mov bx, ax")
        out.println("pop ax")
        out.println("cwd")
        out.println("idiv bx")
        out.println("mov ax, dx")
    }
/*
    override def addI(): Unit = {
        // ( a b -> a + b )
        out.println("mov bx, ax")
        out.println("pop ax")
        out.println("add ax, bx")
    }

    override def subI(): Unit = {
        // ( a b -> a - b )
        out.println("mov bx, ax")
        out.println("pop ax")
        out.println("sub ax, bx")
    }
*/

    override def printStr() {
        out.println("call prints")
    }

}

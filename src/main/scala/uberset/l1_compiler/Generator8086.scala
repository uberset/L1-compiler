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

    override def pushInt(v: String) {
        val i = v.toInt // 0..65535
        out.println( "push ax")
        out.println(s"mov ax, $i")
    }

    override def printInt() {
        out.println("call printi")
    }

}

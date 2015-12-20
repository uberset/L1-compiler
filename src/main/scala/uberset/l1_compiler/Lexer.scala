/*
  Author: uberset
  Date: 2015-12-20
  Licence: GPL v2
*/

package uberset.l1_compiler

import java.io.BufferedReader

class Lexer(
    val in: BufferedReader
) {

    var char: Char = 0
    nextChar()

    def nextChar(): Unit = {
        val i = in.read()
        //char =
            if(i<0) char = 0   // EOF
            else char = i.toChar
    }

    def getToken(): Token = {
        if(char==0) EOF()
        else if(char.isDigit) {
            var s = ""+char
            nextChar()
            while(char.isDigit) {
                s = s + char
                nextChar()
            }
            IntLiteral(s)
        } else {
            nextChar()
            Unknown(char)
        }
    }

}

abstract sealed class Token
case class EOF() extends Token
case class Unknown(c: Char) extends Token
case class IntLiteral(v: String) extends Token

/*
  Author: uberset
  Date: 2015-12-20
  Licence: GPL v2
*/

package uberset.l1_compiler

import java.io.BufferedReader

abstract sealed class Token
case class EOF() extends Token
case class Unknown(c: Char) extends Token
case class IntLiteral(v: String) extends Token
case class Identifier(v: String) extends Token

class Lexer(
    val in: BufferedReader
) {

    var char: Char = 0
    nextChar()

    def nextChar(): Unit = {
        val i = in.read()
        char =
            if(i<0) 0       // EOF
            else i.toChar   // character
    }

    def getToken(): Token = {
        while(char.isWhitespace) nextChar()
        if(char==0) EOF()
        else if(char.isDigit) {
            var s = "" + char
            nextChar()
            while (char.isDigit) {
                s = s + char
                nextChar()
            }
            IntLiteral(s)
        } else if(char.isLetter) {
            var s = "" + char
            nextChar()
            while (char.isLetter || char.isDigit) {
                s = s + char
                nextChar()
            }
            Identifier(s)
        } else {
            val t = Unknown(char)
            nextChar()
            t
        }
    }

}

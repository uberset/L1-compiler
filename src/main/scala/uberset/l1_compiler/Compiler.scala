/*
  Author: uberset
  Date: 2015-12-20
  Licence: GPL v2
*/

package uberset.l1_compiler

import java.io.{BufferedReader, StringReader}

class Compiler(
    val lexer: Lexer,
    val generator: Generator
) {

    var token: Token = _

    def nextToken() {
        token = lexer.getToken()
    }

    def parse() {
        nextToken()
        program()
    }

    def program() {
        generator.begin()
        expression()
        generator.printInt()
        generator.end()
        generator.library()
    }

    def expression() {
        intLiteral()
    }

    def intLiteral(): Type = {
        token match {
            case IntLiteral(v) => generator.pushInt(v); nextToken(); Int()
            case other => fail(s"Expecting integer literal. Found: ${other}")
        }
    }

    def fail(msg: String): Nothing = {
        throw new Exception(msg)
    }

}

abstract sealed class Type
case class Int() extends Type

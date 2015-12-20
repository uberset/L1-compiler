/*
  Author: uberset
  Date: 2015-12-20
  Licence: GPL v2
*/

package uberset.l1_compiler

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
        statement()
        if(token!=EOF()) fail(s"Unexpected token: $token at end of program")
        generator.end()
        generator.library()
    }

    def statement() {
        token match {
            case Identifier("print") => stmPrint()
            case other => fail(s"Expecting print statement. Found token: $other")
        }
    }

    def stmPrint() {
        nextToken()
        intLiteral()
        generator.printInt()
    }

    def expression() {
        intLiteral()
    }

    def intLiteral(): Type = {
        token match {
            case IntLiteral(v) => generator.pushInt(v); nextToken(); Int()
            case other => fail(s"Expecting integer literal. Found token: $other")
        }
    }

    def fail(msg: String): Nothing = {
        throw new Exception(msg)
    }

}

abstract sealed class Type
case class Int() extends Type

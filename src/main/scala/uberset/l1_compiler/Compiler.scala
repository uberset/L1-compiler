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
        while(token!=EOF()) {
            statement()
        }
        generator.end()
        generator.library()
    }

    def statement() {
        token match {
            case Identifier("print")   => stmPrint()
            case Identifier("println") => generator.printLn(); nextToken()
            case other => fail(s"Expecting print statement. Found token: $other")
        }
    }

    def stmPrint() {
        nextToken()
        val t = expression()
        t match {
            case TBoo() => generator.printBoo()
            case TChr() => generator.printChr()
            case TInt() => generator.printInt()
            case TStr() => generator.printStr()
            case other => fail(s"Unknown argument type $t for function print.")
        }
    }

    def expression(): Type = {
        term()
    }

    // term = factor { mulop factor }* ;
    def term(): Type = {
        val t = factor()
        while(token.isInstanceOf[MulOp]) {
            val op = token.asInstanceOf[MulOp]
            nextToken()
            val t2 = factor()
            if(t==TInt() && t2== TInt()) {
                op match {
                    case MulOp('*') => generator.mulI()
                    case MulOp('/') => generator.divI()
                    case MulOp('%') => generator.modI()
                }
            } else {
                fail(s"Operator ${op.c} is not defined for types $t and $t2.")
            }
        }
        t
    }

    // factor = '-' factor | literal ;
    def factor(): Type = {
        if(token==AddOp('-')) {
            nextToken()
            val t = factor()
            if(t==TInt()) {
                generator.negI()
                t
            } else {
                fail("Type TInt() expected. Found: $t")
            }
        } else {
            literal()
        }
    }

    def literal(): Type = {
        token match {
            case Identifier("true" ) => generator.pushBoo(true ); nextToken(); TBoo()
            case Identifier("false") => generator.pushBoo(false); nextToken(); TBoo()
            case ChrLiteral(v) => generator.pushChr(v); nextToken(); TChr()
            case IntLiteral(v) => generator.pushInt(v); nextToken(); TInt()
            case StrLiteral(v) => generator.pushStr(v); nextToken(); TStr()
            case other => fail(s"Expecting literal. Found token: $other")
        }
    }

    def fail(msg: String): Nothing = {
        throw new Exception(msg)
    }

}

abstract sealed class Type
case class TBoo() extends Type
case class TChr() extends Type
case class TInt() extends Type
case class TStr() extends Type

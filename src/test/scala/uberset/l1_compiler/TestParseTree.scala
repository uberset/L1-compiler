/*
  Author: uberset
  Date: 2015-12-26
  Licence: GPL v2
*/

package uberset.l1_compiler

import java.io.{StringWriter, BufferedReader, PrintWriter, StringReader}

object TestParseTree {

    def main(args: Array[String]): Unit = {
        println(this.getClass.getSimpleName)
        val results = Seq(
            test("int1", Program(Seq(PrintInt(IntLit(1))))),
            test("int32767", Program(Seq(PrintInt(IntLit(32767))))),
            test("int32768", Program(Seq(PrintInt(IntLit(32768))))),
            test("int65535", Program(Seq(PrintInt(IntLit(65535))))),
            test("int65536", Program(Seq(PrintInt(IntLit(65536))))),
            test("hello", Program(Seq(PrintStr(StrLit("Hello World!"))))),
            test("char", Program(Seq(
                PrintChr(ChrLit('H')),
                PrintChr(ChrLit('\'')),
                PrintChr(ChrLit('\\')),
                PrintChr(ChrLit(10.toChar)),
                PrintChr(ChrLit('\r')),
                PrintChr(ChrLit('/'))
            ))),
            test("empty", Program(Seq())),
            test("boolean", Program(Seq(
                PrintBoo(BooLit(true)),
                PrintChr(ChrLit(' ')),
                PrintBoo(BooLit(false))
            ))),
            test("negInt", Program(Seq(
                PrintStr(StrLit("-1=")), PrintInt(NegI(IntLit(1))), PrintLn(),
                PrintStr(StrLit("--2=")), PrintInt(NegI(NegI(IntLit(2)))), PrintLn(),
                PrintStr(StrLit("---3=")), PrintInt(NegI(NegI(NegI(IntLit(3))))), PrintLn()
            ))),
            test("mulOpsInt", Program(Seq(
                PrintStr(StrLit("2*3=")), PrintInt(MulI(IntLit(2), IntLit(3))), PrintLn(),
                PrintStr(StrLit("2*-3=")), PrintInt(MulI(IntLit(2), NegI(IntLit(3)))), PrintLn(),
                PrintStr(StrLit("-2*-3=")), PrintInt(MulI(NegI(IntLit(2)), NegI(IntLit(3)))), PrintLn(),
                PrintStr(StrLit("-2*3=")), PrintInt(MulI(NegI(IntLit(2)), IntLit(3))), PrintLn(),
                PrintLn(),
                PrintStr(StrLit("5/2=")), PrintInt(DivI(IntLit(5), IntLit(2))), PrintLn(),
                PrintStr(StrLit("5/-2=")), PrintInt(DivI(IntLit(5), NegI(IntLit(2)))), PrintLn(),
                PrintStr(StrLit("-5/-2=")), PrintInt(DivI(NegI(IntLit(5)), NegI(IntLit(2)))), PrintLn(),
                PrintStr(StrLit("-5/2=")), PrintInt(DivI(NegI(IntLit(5)), IntLit(2))), PrintLn(),
                PrintLn(),
                PrintStr(StrLit("5%2=")), PrintInt(ModI(IntLit(5), IntLit(2))), PrintLn(),
                PrintStr(StrLit("5%-2=")), PrintInt(ModI(IntLit(5), NegI(IntLit(2)))), PrintLn(),
                PrintStr(StrLit("-5%-2=")), PrintInt(ModI(NegI(IntLit(5)), NegI(IntLit(2)))), PrintLn(),
                PrintStr(StrLit("-5%2=")), PrintInt(ModI(NegI(IntLit(5)), IntLit(2))), PrintLn()
            )))
        )
        val tests = results.size
        val passed = results.filter(identity).size
        val failed = tests - passed
        if(failed>0)
            println(s"$failed of $tests tests failed.")
        else
            println(s"All $tests tests passed.")
    }

    def test(mainName: String, tree: Program): Boolean = {
        try {
            val text: String = scala.io.Source.fromFile("input/"+mainName+".l1").getLines.mkString("\n")
            println(mainName);println(text); println("--------------------")
            val lexer = new Lexer(new BufferedReader(new StringReader(text)))
            val sw = new StringWriter()
            new PrintWriter(sw) {
                writer =>
                val generator = new GeneratorRpn(writer)
                new Compiler(lexer, generator).parse()
                writer.close
            }
            val expected = toRpn(tree)
            val actual = sw.toString()
            if(expected!=actual) {
                println(s"expecting: $expected")
                println(s"got      : $actual")
                false
            } else {
                true
            }
        } catch {
            case e: Exception =>
                println(e.toString)
                e.printStackTrace()
                false
        }
    }

    def toRpn(node: AnyRef): String = {
        node match {
            case Program(stms) => {
                var s = "Program("
                for(stm <- stms) s = s + toRpn(stm)
                s + ")"
            }
            case PrintInt(arg) => toRpn(arg) + "printInt "
            case PrintStr(arg) => toRpn(arg) + "printStr "
            case PrintChr(arg) => toRpn(arg) + "printChr "
            case PrintBoo(arg) => toRpn(arg) + "printBoo "
            case PrintLn() => "printLn "
            case IntLit(v) => s"Int($v) "
            case StrLit(v) => s"Str($v) "
            case ChrLit(v) => s"Chr(${v.toInt}) "
            case BooLit(v) => s"Boo($v) "
            case NegI(x) => toRpn(x) + "negI "
            case MulI(x, y) => toRpn(x) + toRpn(y) + "mulI "
            case DivI(x, y) => toRpn(x) + toRpn(y) + "divI "
            case ModI(x, y) => toRpn(x) + toRpn(y) + "modI "
        }
    }

}

case class Program(stms: Seq[Stm])

abstract class Stm
case class PrintInt(arg: IntExpression) extends Stm
case class PrintStr(arg: StrLit) extends Stm
case class PrintChr(arg: ChrLit) extends Stm
case class PrintBoo(arg: BooLit) extends Stm
case class PrintLn() extends Stm

abstract class IntExpression
case class IntLit(v: Int) extends IntExpression
case class NegI(v: IntExpression) extends IntExpression
case class MulI(v: IntExpression, w: IntExpression) extends IntExpression
case class DivI(v: IntExpression, w: IntExpression) extends IntExpression
case class ModI(v: IntExpression, w: IntExpression) extends IntExpression

case class StrLit(v: String)

case class ChrLit(v: Char)

case class BooLit(v: Boolean)

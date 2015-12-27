/*
  Author: uberset
  Date: 2015-12-20
  Licence: GPL v2
*/

package uberset.l1_compiler

import java.io.{PrintWriter, StringReader, BufferedReader}

object TestCompiler {

    def main(args: Array[String]): Unit = {
        println(this.getClass.getSimpleName)
        val results = Seq(
            test("int1"),
            test("int32767"),
            test("int32768"),
            test("int65535"),
            test("int65536"),
            test("hello"),
            test("char"),
            test("empty"),
            test("boolean"),
            test("negInt"),
            test("mulOpsInt")
        )
        val tests = results.size
        val passed = results.filter(identity).size
        val failed = tests - passed
        if(failed>0)
            println(s"$failed of $tests tests failed.")
        else
            println(s"All $tests tests passed.")
    }

    def test(mainName: String, dummy: String = ""): Boolean = {
        try {
            val text: String = scala.io.Source.fromFile("input/"+mainName+".l1").getLines.mkString("\n")
            println(mainName);println(text); println("--------------------")
            val lexer = new Lexer(new BufferedReader(new StringReader(text)))
            new PrintWriter("output/generator8086/"+mainName+".asm") {
                writer =>
                val generator = new Generator8086(writer)
                new Compiler(lexer, generator).parse()
                writer.close
            }
            true
        } catch {
            case e: Exception =>
                println(e.toString)
                e.printStackTrace()
                false
        }
    }

}

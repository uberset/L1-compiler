/*
  Author: uberset
  Date: 2015-12-20
  Licence: GPL v2
*/

package uberset.l1_compiler

trait Generator {

    def end(): Unit
    def begin(): Unit
    def library(): Unit

    def pushInt(v: String): Unit
    def printInt(): Unit

    def pushStr(v: String): Unit
    def printStr(): Unit

}

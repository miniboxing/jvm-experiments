package miniboxing.test

import miniboxing.runtime.MiniboxConversions._
import miniboxing.benchmarks.hardcoded._

object HardcodedMiniboxingBenchTest extends App {

  def testSize = 1000000

  private[this] object TestArray {
    def array_insert(): MBResizableArray[Int] = {
      val a: MBResizableArray[Int] = new MBResizableArray_J[Int](5)
      var i = 0
      while (i < testSize) {
        a.add_J(IntToMinibox(i))
        i += 1
      }
      a
    }

    def array_reverse(a: MBResizableArray[Int]): MBResizableArray[Int] = {
      a.reverse_J
      a
    }

    def array_insert_LONG(): MBResizableArray[Long] = {
      val a: MBResizableArray[Long] = new MBResizableArray_J[Long](6)
      var i = 0
      while (i < testSize) {
        a.add_J(LongToMinibox(i))
        i += 1
      }
      a
    }

    def array_reverse_LONG(a: MBResizableArray[Long]): MBResizableArray[Long] = {
      a.reverse_J
      a
    }

    def array_insert_DOUBLE(): MBResizableArray[Double] = {
      val a: MBResizableArray[Double] = new MBResizableArray_J[Double](8)
      var i = 0
      while (i < testSize) {
        a.add_J(DoubleToMinibox(i))
        i += 1
      }
      a
    }

    def array_reverse_DOUBLE(a: MBResizableArray[Double]): MBResizableArray[Double] = {
      a.reverse_J
      a
    }
  }

  def testHardcodedMiniboxing(megamorphic: Boolean) = {
    import TestArray._

    val transformation = "miniboxed standard " + (if (megamorphic) "mega" else "mono")

      def forceMegamorphicCallSites(): Unit =
      if (megamorphic) {
        //array_reverse(array_insert())
        array_reverse_LONG(array_insert_LONG())
        array_reverse_DOUBLE(array_insert_DOUBLE())
      }

    var a: MBResizableArray[Int] = null
    var b: Boolean = true
    TestJVM.benchmark(transformation+" array.reverse", () => { forceMegamorphicCallSites(); a = array_insert() }, () => { a = array_reverse(a) }, () => { assert(a.length == testSize); a = null })
  }
}

package miniboxing.test

import miniboxing.runtime.MiniboxConversions._
import miniboxing.benchmarks.hardcoded._
import miniboxing.benchmarks.ideal._

trait Benchmark extends App {

  def benchmark(desc: String, setup: () => Unit, f: () => Unit, tear: () => Unit) = {
    var avg = 0D
    var n = 0
    var M2 = 0D
    var sd = 0D
    val n0 = 50

    setup.apply()

    while (((sd >= avg / 10) || (n < n0 + 10)) && (n < n0 + 100)) {
      System.gc
      var time = System.currentTimeMillis()
      f.apply()
      time = System.currentTimeMillis() - time
      if (n > n0) {
        val delta = time - avg
        avg += delta / (n - n0)
        M2 += delta * (time - avg)
        sd = if(n == n0 + 1) avg else math.sqrt(M2 / (n - n0 - 1))
        //println(f"measured ${desc}%s with time:  avg=${avg}%8.3fms  sd=${sd}%8.3fms")
      }
      n += 1
    }

    tear.apply()

    println(f"measured ${desc}%s final time: avg=${avg}%8.3fms  sd=${sd}%8.3fms  n=${n}%d")
  }

    def testSize = 1000000

  private[this] object TestMiniboxedArray {
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

  private[this] object TestIdealArray {
    def array_insert(): ResizableArray = {
      val a: ResizableArray = new ResizableArray()
      var i = 0
      while (i < testSize) {
        a.add(i)
        i += 1
      }
      a
    }

    def array_reverse(a: ResizableArray): ResizableArray = {
      a.reverse
      a
    }
  }

  def testIdealInsert(megamorphic: Boolean) = {
    import TestIdealArray._

    val transformation = "ideal "

    var a: ResizableArray = null
    benchmark(transformation+" array.insert", () => (), () => { a = array_insert() }, () => { assert(a.length == testSize); a = null })
  }


  def testMiniboxingInsert(megamorphic: Boolean) = {
    import TestMiniboxedArray._

    val transformation = "miniboxed standard " + (if (megamorphic) "mega" else "mono")

      def forceMegamorphicCallSites(): Unit =
      if (megamorphic) {
        array_insert_LONG()
        array_insert_DOUBLE()
      }

    var a: MBResizableArray[Int] = null
    benchmark(transformation+" array.insert", () => { forceMegamorphicCallSites() }, () => { a = array_insert() }, () => { assert(a.length == testSize); a = null })
  }


  def testMiniboxingReverse(megamorphic: Boolean) = {
    import TestMiniboxedArray._

    val transformation = "miniboxed standard " + (if (megamorphic) "mega" else "mono")

      def forceMegamorphicCallSites(): Unit =
      if (megamorphic) {
        array_reverse_LONG(array_insert_LONG())
        array_reverse_DOUBLE(array_insert_DOUBLE())
      }

    var a: MBResizableArray[Int] = null
    benchmark(transformation+" array.reverse", () => { forceMegamorphicCallSites(); a = array_insert() }, () => { a = array_reverse(a) }, () => { assert(a.length == testSize); a = null })
  }
}

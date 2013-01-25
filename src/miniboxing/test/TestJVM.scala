package miniboxing.test;

object TestJVM extends App {

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

  println("**************************************************************")
  HardcodedMiniboxingBenchTest.testHardcodedMiniboxing(false)
  println("**************************************************************")
  HardcodedMiniboxingBenchTest.testHardcodedMiniboxing(true)
  println("**************************************************************")
  HardcodedMiniboxingBenchTest.testHardcodedMiniboxing(false)
  println("**************************************************************")
}

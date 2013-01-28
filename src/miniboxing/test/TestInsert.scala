package miniboxing.test

object TestInsert extends Benchmark {
  println("**************************************************************")
  testMiniboxingInsert(false)
  println("**************************************************************")
  testIdealInsert(true)
  println("**************************************************************")
}

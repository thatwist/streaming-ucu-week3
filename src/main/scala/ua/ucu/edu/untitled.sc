val l = 1 to 10

l.par.foreach(_ => Thread.sleep(100))
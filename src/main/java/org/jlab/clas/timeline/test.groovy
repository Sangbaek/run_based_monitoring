package org.jlab.clas.timeline

import org.jlab.groot.data.TDirectory
import org.jlab.clas.timeline.timeline.*//ftof.ftof_tdcadc_p1a_test

def engines = [ 
  out_TOF: [    new  org.jlab.clas.timeline.timeline.ftof.ftof_tdcadc_p1a_test()
]
]

if(args.any{it=="--timelines"}) {
  engines.values().flatten().each{
    println(it.getClass().getSimpleName())
  }
  System.exit(0)
}

def eng = engines.collectMany{key,engs->engs.collect{[key,it]}}
  .find{name,eng->eng.getClass().getSimpleName()==args[0]}

if(eng) {
  def (name,engine) = eng 
  def input = new File(args[1])
  println([name,args[0],engine.getClass().getSimpleName(),input])
  def fnames = []
  input.traverse {
    if(it.name.endsWith('.hipo') && it.name.contains(name))
      fnames.add(it.absolutePath)
  }

  fnames.sort().each{arg->
    TDirectory dir = new TDirectory()
    dir.readFile(arg)
    def fname = arg.split('/')[-1]
    def m = fname =~ /\d{4,7}/
    def run = m[0].toInteger()

    println("debug: "+engine.getClass().getSimpleName()+" processes $arg")
    engine.processDirectory(dir, run)
  }
  engine.close()
  println("debug: "+engine.getClass().getSimpleName()+" ended")
} else {
  println("debug: "+args[0]+" not found")
}

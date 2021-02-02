package org.jlab.clas.timeline.timeline.cvt
import java.util.concurrent.ConcurrentHashMap
import org.jlab.groot.data.TDirectory
import org.jlab.groot.data.GraphErrors

class cvt_chi2norm {

def data = new ConcurrentHashMap()

def processDirectory(dir, run) {
  def h1 = dir.getObject('/cvt/hchi2norm')
  h1.setTitle("CVT track chi2norm");
  h1.setTitleX("CVT track chi2/ndf");

  data[run] = [run:run, h1:h1]
}



def close() {

  TDirectory out = new TDirectory()

  def grtl = new GraphErrors('CVT chi2 per ndf')
  grtl.setTitle("Average CVT chi2/ndf")
  grtl.setTitleY("Average CVT chi2/ndf")
  grtl.setTitleX("run number")

  data.sort{it.key}.each{run,it->
    out.mkdir('/'+it.run)
    out.cd('/'+it.run)
    out.addDataSet(it.h1)
    grtl.addPoint(it.run, it.h1.getMean(), 0, 0)
  }

  out.mkdir('/timelines')
  out.cd('/timelines')
  grtl.each{ out.addDataSet(it) }
  out.writeFile('cvt_chi2norm.hipo')
}
}

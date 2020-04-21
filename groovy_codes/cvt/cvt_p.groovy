import org.jlab.groot.data.TDirectory
import org.jlab.groot.data.GraphErrors

class cvt_p.groovy {

def data = []

def processDirectory(dir, run) {
  def h1 = dir.getObject('/cvt/hp')
  h1.setTitle("CVT track momentum");
  h1.setTitleX("CVT track momentum (GeV/c)");

  data.add([run:run, h1:h1])
}



def close() {

  TDirectory out = new TDirectory()

  def grtl = new GraphErrors('CVT momentum')
  grtl.setTitle("Average CVT momentum")
  grtl.setTitleY("Average CVT momentum (GeV/c)")
  grtl.setTitleX("run number")

  data.each{
    out.mkdir('/'+it.run)
    out.cd('/'+it.run)
    out.addDataSet(it.h1)
    grtl.addPoint(it.run, it.h1.getDataX(it.h1.getMaximumBin()), 0, 0)
  }

  out.mkdir('/timelines')
  out.cd('/timelines')
  grtl.each{ out.addDataSet(it) }
  out.writeFile('cvt_p.hipo')
}
}

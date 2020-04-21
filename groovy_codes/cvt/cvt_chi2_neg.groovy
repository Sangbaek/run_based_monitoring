import org.jlab.groot.data.TDirectory
import org.jlab.groot.data.GraphErrors

class cvt_chi2_neg.groovy {

def data = []

def processDirectory(dir, run) {
  def h1 = dir.getObject('/cvt/H_CVT_chi2_neg')

  data.add([run:run, h1:h1])
}



def close() {

  TDirectory out = new TDirectory()

  def grtl = new GraphErrors('cvt_chi2_neg')
  grtl.setTitle("Average CVT chi2 for negatives")
  grtl.setTitleY("Average CVT chi2 for negatives")
  grtl.setTitleX("run number")

  data.each{
    out.mkdir('/'+it.run)
    out.cd('/'+it.run)
    out.addDataSet(it.h1)
    grtl.addPoint(it.run, it.h1.getMean(), 0, 0)
  }

  out.mkdir('/timelines')
  out.cd('/timelines')
  grtl.each{ out.addDataSet(it) }
  out.writeFile('cvt_chi2_neg.hipo')
}
}

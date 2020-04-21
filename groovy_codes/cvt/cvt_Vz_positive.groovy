import org.jlab.groot.data.TDirectory
import org.jlab.groot.data.GraphErrors

class cvt_Vz_positive.groovy {

def data = []

def processDirectory(dir, run) {
  def h1 = dir.getObject('/cvt/H_CVT_z_pos')

  data.add([run:run, h1:h1])
}



def close() {

  TDirectory out = new TDirectory()

  def grtl = new GraphErrors('cvt_z_pos')
  grtl.setTitle("Average VZ for positives")
  grtl.setTitleY("Average VZ for positives (cm)")
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
  out.writeFile('cvt_Vz_pos.hipo')
}
}

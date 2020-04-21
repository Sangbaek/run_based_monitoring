import org.jlab.groot.data.TDirectory
import org.jlab.groot.data.GraphErrors

class cvt_trks_pos_rat.groovy {

def data = []

def processDirectory(dir, run) {
  def h1 = dir.getObject('/cvt/hpostrks_rat')
  h1.setTitle("CVT Positive Tracks/ trigger");
  h1.setTitleX("CVT Positive Tracks/ trigger");

  data.add([run:run, h1:h1])
}



def close() {

  TDirectory out = new TDirectory()

  def grtl = new GraphErrors('CVT positive tracks per trigger')
  grtl.setTitle("Average CVT positive track multiplicity per trigger")
  grtl.setTitleY("Average CVT positive track multiplicity per trigger")
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
  out.writeFile('cvt_pos_tracks_ratio.hipo')
}
}

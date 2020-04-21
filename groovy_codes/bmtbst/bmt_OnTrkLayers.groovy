import org.jlab.groot.data.TDirectory
import org.jlab.groot.data.GraphErrors

class bmt_OnTrkLayers.groovy {

def data = []

def processDirectory(dir, run) {
  def h1 = dir.getObject('/cvt/hbmtOnTrkLayers')
  h1.setTitle("BMT Layers per Track");
  h1.setTitleX("BMT Layers per Track");

  data.add([run:run, h1:h1])
}



def close() {

  TDirectory out = new TDirectory()

  def grtl = new GraphErrors('BMT layers per track')
  grtl.setTitle("Average BMT layers per track")
  grtl.setTitleY("Average BMT layers per track")
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
  out.writeFile('bmt_OnTrkLayers.hipo')
}
}

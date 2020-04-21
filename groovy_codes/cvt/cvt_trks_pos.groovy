package cvt
import java.util.concurrent.ConcurrentHashMap
import org.jlab.groot.data.TDirectory
import org.jlab.groot.data.GraphErrors

class cvt_trks_pos {

def data = new ConcurrentHashMap()

def processDirectory(dir, run) {
  def h1 = dir.getObject('/cvt/hpostrks')
  h1.setTitle("CVT Positive Tracks");
  h1.setTitleX("CVT Positive Tracks");

  data[run] = [run:run, h1:h1]
}



def close() {

  TDirectory out = new TDirectory()

  def grtl = new GraphErrors('CVT positive tracks')
  grtl.setTitle("Average CVT positive track multiplicity")
  grtl.setTitleY("Average CVT positive track multiplicity")
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
  out.writeFile('cvt_pos_tracks.hipo')
}
}

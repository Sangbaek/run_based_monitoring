package bmtbst
import java.util.concurrent.ConcurrentHashMap
import org.jlab.groot.data.TDirectory
import org.jlab.groot.data.GraphErrors

class bmt_Occupancy {

def data = new ConcurrentHashMap()

def processDirectory(dir, run) {
  def h1 = dir.getObject('/cvt/hbmtOccupancy')
  h1.setTitle("BMT Occupancy");
  h1.setTitleX("BMT Occupancy (%)");

  data[run] = [run:run, h1:h1]
}



def close() {

  TDirectory out = new TDirectory()

  def grtl = new GraphErrors('BMT Occupancy')
  grtl.setTitle("Average BMT Occupancy")
  grtl.setTitleY("Average BMT Occupancy (%)")
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
  out.writeFile('bmt_Occupancy.hipo')
}
}

package rich
import java.util.concurrent.ConcurrentHashMap
import org.jlab.groot.data.TDirectory
import org.jlab.groot.data.GraphErrors
import fitter.RICHFitter;

class rich_timediff {


def data = new ConcurrentHashMap()

def processDirectory(dir, run) {
  def h1 = dir.getObject('/RICH/H_RICH_FWHM')
  h1.setName(h1.getName + ", max at "+h1.getAxis().getBinCenter(h1.getMaximumBin()))
  data[run] = [run:run, h1:h1, fwhm_max:h1.getBinContent(h1.getMaximumBin())]
}



def close() {


  ['fwhm_max'].each{name->
    def grtl = new GraphErrors(name)
    grtl.setTitle("RICH FWHM of T_meas-T_calc per pmt photons ("+name+")")
    grtl.setTitleY("RICH FWHM of T_meas-T_calc per pmt photons ("+name+") (ns)")
    grtl.setTitleX("run number")

    TDirectory out = new TDirectory()

    data.sort{it.key}.each{run,it->
      out.mkdir('/'+it.run)
      out.cd('/'+it.run)
      out.addDataSet(it.h1)
      grtl.addPoint(it.run, it[name], 0, 0)
    }

    out.mkdir('/timelines')
    out.cd('/timelines')
    out.addDataSet(grtl)
    out.writeFile('rich_time_'+name+'.hipo')
  }

}
}

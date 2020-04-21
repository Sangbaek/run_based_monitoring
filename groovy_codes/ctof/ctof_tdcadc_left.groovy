package ctof
import java.util.concurrent.ConcurrentHashMap
import org.jlab.groot.data.TDirectory
import org.jlab.groot.data.GraphErrors
import fitter.CTOFFitter;

class ctof_tdcadc_left {


def data = new ConcurrentHashMap()

def processDirectory(dir, run) {
  def h1 = dir.getObject('/ctof/CTOF TDC-ADC Time Difference')
  def f1 = CTOFFitter.tdcadcdifffit_left(h1)

  data[run] = [run:run, h1:h1, f1:f1, mean:f1.getParameter(1), sigma:f1.getParameter(2).abs, chi2:f1.getChiSquare()]
}



def close() {


  ['mean', 'sigma'].each{name->
    def grtl = new GraphErrors(name)
    grtl.setTitle("TDC time - FADC time averaged over CTOF counters, left peak (" + name +")")
    grtl.setTitleY("TDC time - FADC time averaged over CTOF counters, left peak  (" + name + ") (ns)")
    grtl.setTitleX("run number")

    TDirectory out = new TDirectory()

    data.sort{it.key}.each{run,it->
      out.mkdir('/'+it.run)
      out.cd('/'+it.run)
      out.addDataSet(it.h1)
      out.addDataSet(it.f1)
      grtl.addPoint(it.run, it[name], 0, 0)
    }

    out.mkdir('/timelines')
    out.cd('/timelines')
    out.addDataSet(grtl)
    out.writeFile('ctof_tdcadc_time_left_'+name+'.hipo')
  }
}
}

import org.jlab.groot.data.TDirectory
import org.jlab.groot.data.GraphErrors
import fitter.ECFitter

class ec_pim_time.groovy {

def data = []

def processDirectory(dir, run) {
  def h1 = dir.getObject('/tof/H_pim_vtd')
  def f1 = ECFitter.timefit(h1)

  data.add([run:run, h1:h1, f1:f1, mean:f1.getParameter(1), sigma:f1.getParameter(2).abs(), chi2:f1.getChiSquare()])
}



def close() {


  ['mean', 'sigma'].each{name->
    def grtl = new GraphErrors(name)
    grtl.setTitle("#pi^- time - start time, " + name)
    grtl.setTitleY("#pi^- time - start time, "+ name +" (ns)")
    grtl.setTitleX("run number")

    TDirectory out = new TDirectory()

    data.each{
      out.mkdir('/'+it.run)
      out.cd('/'+it.run)
      out.addDataSet(it.h1)
      out.addDataSet(it.f1)
      grtl.addPoint(it.run, it[name], 0, 0)
    }

    out.mkdir('/timelines')
    out.cd('/timelines')
    out.addDataSet(grtl)
    out.writeFile('ec_pim_time_'+name+'.hipo')
  }
}
}

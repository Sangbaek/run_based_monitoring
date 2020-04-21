import org.jlab.groot.data.TDirectory
import org.jlab.groot.data.GraphErrors
import fitter.ECFitter

class ec_gg_m.groovy {

def data = []

def processDirectory(dir, run) {
  def h1 = dir.getObject('/gg/H_gg_m')
  def f1 = ECFitter.ggmfit(h1)

  data.add([run:run, h1:h1, f1:f1, mean:f1.getParameter(1), sigma:f1.getParameter(2).abs(), chi2:f1.getChiSquare()])
}



def close() {


  ['mean', 'sigma'].each{name->
    def grtl = new GraphErrors(name)
    grtl.setTitle("#gamma #gamma invariant mass ECAL, " + name)
    grtl.setTitleY("#gamma #gamma invariant mass ECAL, " + name + " (GeV)")
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
    out.writeFile('ec_gg_m_'+name+'.hipo')
  }
}
}

package ft
import java.util.concurrent.ConcurrentHashMap
import org.jlab.groot.data.TDirectory
import org.jlab.groot.data.GraphErrors
import fitter.FTFitter

class fth_MIPS_energy {

def data = new ConcurrentHashMap()

def processDirectory(dir, run) {
  def funclist = []
  def meanlist = []
  def sigmalist = []
  def chi2list = []

  def h1
  def h2

  def histlist = [h1, h2].withIndex().collect{hist, it ->
    hist = dir.getObject('/ft/hi_hodo_ematch_l'+(it+1))
    funclist.add(FTFitter.fthedepfit(hist, it))
    meanlist.add(funclist[it].getParameter(1))
    sigmalist.add(funclist[it].getParameter(2).abs())
    chi2list.add(funclist[it].getChiSquare())
    hist
  }

  data[run] = [run:run, hlist:histlist, flist:funclist, mean:meanlist, sigma:sigmalist, clist:chi2list]
}



def close() {


  TDirectory out = new TDirectory()
  out.mkdir('/timelines')
  ['layer1','layer2'].eachWithIndex{layer, lindex ->
    def grtl = new GraphErrors(layer)
    grtl.setTitle("FTH MIPS energy per layer (mean value)")
    grtl.setTitleY("FTH MIPS energy per layer (mean value) (MeV)")
    grtl.setTitleX("run number")

    data.sort{it.key}.each{run,it->
      out.mkdir('/'+it.run)
      out.cd('/'+it.run)

      out.addDataSet(it.hlist[lindex])
      out.addDataSet(it.flist[lindex])
      grtl.addPoint(it.run, it.mean[lindex], 0, 0)
    }
    out.cd('/timelines')
    out.addDataSet(grtl)
  }
  out.writeFile('fth_MIPS_energy.hipo')
}
}

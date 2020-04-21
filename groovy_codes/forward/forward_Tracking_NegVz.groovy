import org.jlab.groot.data.TDirectory
import org.jlab.groot.data.GraphErrors
import fitter.ForwardFitter

class forward_Tracking_NegVz.groovy {

def data = []

def processDirectory(dir, run) {
  def funclist = []
  def meanlist = []
  def sigmalist = []
  def chi2list = []
  def histlist =   (0..<6).collect{
    def h1 = dir.getObject('/dc/H_dcm_vz_s'+(it+1))
    h1.setTitle("VZ of negatives")
    h1.setTitleX("VZ of negatives (cm)")

    def f1 = ForwardFitter.fit(h1)

    funclist.add(f1)
    meanlist.add(f1.getParameter(1))
    sigmalist.add(f1.getParameter(2).abs())
    chi2list.add(f1.getChiSquare())
    return h1
  }
  data.add([run:run, hlist:histlist, flist:funclist, mean:meanlist, sigma:sigmalist, clist:chi2list])
}



def close() {

  TDirectory out = new TDirectory()
  out.mkdir('/timelines')
  (0..<6).each{ sec->
    def grtl = new GraphErrors('sec'+(sec+1))
    grtl.setTitle("VZ (peak value) for negatives per sector")
    grtl.setTitleY("VZ (peak value) for negatives per sector (cm)")
    grtl.setTitleX("run number")

    data.each{
      if (sec==0){
        out.mkdir('/'+it.run)
      }
      out.cd('/'+it.run)
      out.addDataSet(it.hlist[sec])
      out.addDataSet(it.flist[sec])
      grtl.addPoint(it.run, it.mean[sec], 0, 0)
    }
    out.cd('/timelines')
    out.addDataSet(grtl)
  }

  out.writeFile('forward_negative_VZ.hipo')
}
}

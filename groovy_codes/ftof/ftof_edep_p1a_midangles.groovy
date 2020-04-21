import org.jlab.groot.data.TDirectory
import org.jlab.groot.data.GraphErrors
import fitter.FTOFFitter

data = []

for(arg in args) {
  TDirectory dir = new TDirectory()
  dir.readFile(arg)

  def name = arg.split('/')[-1]
  def m = name =~ /\d{4,5}/
  def run = m[0].toInteger()

  def funclist = []
  def meanlist = []
  def sigmalist = []
  def chi2list = []
  def histlist =   (0..<6).collect{
    def h1 = dir.getObject('/tof/p1a_edep_midangles_S'+(it+1))
    def f1 = FTOFFitter.edepfit(h1)

    funclist.add(f1)
    meanlist.add(f1.getParameter(1))
    sigmalist.add(f1.getParameter(2).abs())
    chi2list.add(f1.getChiSquare())
    return h1
  }
  data.add([run:run, hlist:histlist, flist:funclist, mean:meanlist, sigma:sigmalist, clist:chi2list])
}

TDirectory out = new TDirectory()
out.mkdir('/timelines')
(0..<6).each{ sec->
  def grtl = new GraphErrors('sec'+(sec+1))
  grtl.setTitle("p1a Path-length Corrected Edep for negative tracks, mid. angles (11, 23 deg)")
  grtl.setTitleY("p1a Path-length Corrected Edep for negative tracks, mid. angles (11, 23 deg) (MeV)")
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

out.writeFile('ftof_edep_p1a_midangles.hipo')  
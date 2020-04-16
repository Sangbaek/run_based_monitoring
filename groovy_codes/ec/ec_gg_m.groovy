import org.jlab.groot.data.TDirectory
import org.jlab.groot.data.GraphErrors
import fitter.ECFitter

def grtl = new GraphErrors('Mean')
grtl.setTitle("#gamma #gamma invariant mass ECAL")
grtl.setTitleY("#gamma #gamma invariant mass ECAL (GeV)")
grtl.setTitleX("run number")

def grtl2 = new GraphErrors('Sigma')
grtl.setTitle("#gamma #gamma invariant mass ECAL")
grtl.setTitleY("#gamma #gamma invariant mass ECAL (GeV)")
grtl.setTitleX("run number")

TDirectory out = new TDirectory()
TDirectory out2 = new TDirectory()

for(arg in args) {
  TDirectory dir = new TDirectory()
  dir.readFile(arg)

  def name = arg.split('/')[-1]
  def m = name =~ /\d{4,5}/
  def run = m[0].toInteger()

  def h1 = dir.getObject('/gg/H_gg_m')
  // def f1 = ROOTFitter.fit(h1)
  def f1 = ECFitter.ggmfit(h1)
  grtl.addPoint(run, f1.getParameter(1), 0, 0)
  grtl2.addPoint(run, f1.getParameter(2), 0, 0)

  // grtl.addPoint(run, h1.getRMS(), 0, 0)

  out.mkdir('/'+run)
  out.cd('/'+run)
  out.addDataSet(h1)
  out.addDataSet(f1)
  out2.mkdir('/'+run)
  out2.cd('/'+run)
  out2.addDataSet(h1)
  out2.addDataSet(f1)

}


out.mkdir('/timelines')
out.cd('/timelines')
grtl.each{ out.addDataSet(it) }
out.writeFile('ec_gg_m_mean.hipo')
out2.mkdir('/timelines')
out2.cd('/timelines')
grtl2.each{ out2.addDataSet(it) }
out2.writeFile('ec_gg_m_sigma.hipo')
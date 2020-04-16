import org.jlab.groot.data.TDirectory
import org.jlab.groot.data.GraphErrors
import fitter.ECFitter

def grtl = new GraphErrors('Mean')
grtl.setTitle("#pi^+ time - start time")
grtl.setTitleY("#pi^+ time - start time (ns)")
grtl.setTitleX("run number")

def grtl2 = new GraphErrors('Sigma')
grtl2.setTitle("#pi^+ time - start time")
grtl2.setTitleY("#pi^+ time - start time (ns)")
grtl2.setTitleX("run number")

TDirectory out = new TDirectory()
TDirectory out2 = new TDirectory()

for(arg in args) {
  TDirectory dir = new TDirectory()
  dir.readFile(arg)

  def name = arg.split('/')[-1]
  def m = name =~ /\d{4,5}/
  def run = m[0].toInteger()

  def h1 = dir.getObject('/tof/H_pip_vtd')
  def f1 = ECFitter.timefit(h1)

  grtl.addPoint(run, f1.getParameter(1), 0, 0)
  grtl2.addPoint(run, f1.getParameter(2), 0, 0)
  // grtl.addPoint(run, h1.getMean(), 0, 0)
  // grtl2.addPoint(run, h1.getRMS(), 0, 0)

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
out.writeFile('ec_pip_time_mean.hipo')
out2.mkdir('/timelines')
out2.cd('/timelines')
grtl2.each{ out2.addDataSet(it) }
out2.writeFile('ec_pip_time_sigma.hipo')
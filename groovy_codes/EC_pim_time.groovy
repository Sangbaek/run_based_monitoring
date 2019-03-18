import org.jlab.groot.data.TDirectory
import org.jlab.groot.data.GraphErrors
// import ROOTFitter


def grtl = new GraphErrors('Mean')
grtl.setTitle("#pi^- time - start time")
grtl.setTitleY("#pi^- time - start time")
grtl.setTitleX("run number")

def grtl2 = new GraphErrors('Sigma')
grtl2.setTitle("#pi^- time - start time")
grtl2.setTitleY("#pi^- time - start time")
grtl2.setTitleX("run number")

TDirectory out = new TDirectory()

for(arg in args) {
  TDirectory dir = new TDirectory()
  dir.readFile(arg)

  def name = arg.split('/')[-1]
  def m = name =~ /\d\d\d\d/
  def run = m[0].toInteger()

  def h1 = dir.getObject('/tof/H_pim_vtd')
  // def f1 = ROOTFitter.fit(h1)

  // grtl.addPoint(run, f1.getParameter(2), 0, 0)
  grtl.addPoint(run, h1.getMean(), 0, 0)
  grtl2.addPoint(run, h1.getRMS(), 0, 0)

  out.mkdir('/'+run)
  out.cd('/'+run)
  out.addDataSet(h1)
}


out.mkdir('/timelines')
out.cd('/timelines')
grtl.each{ out.addDataSet(it) }
out.writeFile('EC_pim_time.hipo')

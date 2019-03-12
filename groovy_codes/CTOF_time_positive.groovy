import org.jlab.groot.data.TDirectory
import org.jlab.groot.data.GraphErrors
// import ROOTFitter

def grtl = new GraphErrors('ctof_time')
grtl.setTitle("Average CTOF time, positive")
grtl.setTitleY("Average CTOF time, positive")
grtl.setTitleX("run number")

def grtl2 = new GraphErrors('ctof_time_sigma')
grtl2.setTitle("Sigma CTOF time, positive")
grtl2.setTitleY("Sigma CTOF time, positive")
grtl2.setTitleX("run number")

TDirectory out = new TDirectory()

for(arg in args) {
  TDirectory dir = new TDirectory()
  dir.readFile(arg)

  def name = arg.split('/')[-1]
  def m = name =~ /\d\d\d\d/
  def run = m[0].toInteger()

  // def f1 = ROOTFitter.fit(h1)

  //grtl[it].addPoint(run, h1.getDataX(h1.getMaximumBin()), 0, 0)
  // grtl[it].addPoint(run, f1.getParameter(1), 0, 0)
  // grtl2[it].addPoint(run, f1.getParameter(2), 0, 0)
  grtl[it].addPoint(run, h1.GetMean(), 0, 0)
  grtl2[it].addPoint(run, h1.GetStdDev(2), 0, 0)
  out.addDataSet(h1)
  // out.addDataSet(f1)

  out.mkdir('/'+run)
  out.cd('/'+run)
  out.addDataSet(h1)
}


out.mkdir('/timelines')
out.cd('/timelines')
grtl.each{ out.addDataSet(it) }
grtl2.each{ out.addDataSet(it) }
out.writeFile('out_CTOF_time_pos.hipo')

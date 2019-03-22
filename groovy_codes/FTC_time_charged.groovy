import org.jlab.groot.data.TDirectory
import org.jlab.groot.data.GraphErrors
// import ROOTFitter

  def grtl = new GraphErrors('Mean')
  grtl.setTitle("FTC time - RF time, charged")
  grtl.setTitleY("FTC time - RF time, charged (ns)")
  grtl.setTitleX("run number")

  def grtl2 = new GraphErrors('Sigma')
  grtl.setTitle("FTC time - RF time, charged")
  grtl.setTitleY("FTC time - RF time, charged (ns)")
  grtl.setTitleX("run number")


TDirectory out = new TDirectory()
for(arg in args) {
  TDirectory dir = new TDirectory()
  dir.readFile(arg)

  def name = arg.split('/')[-1]
  def m = name =~ /\d\d\d\d/
  def run = m[0].toInteger()

  out.mkdir('/'+run)
  out.cd('/'+run)

  def h1 = dir.getObject('/ft/hi_cal_time_ch')
  // def f1 = ROOTFitter.fit(h1)

  //grtl[it].addPoint(run, h1.getDataX(h1.getMaximumBin()), 0, 0)
  // grtl.addPoint(run, f1.getParameter(1), 0, 0)
  // grtl2.addPoint(run, f1.getParameter(2), 0, 0)
  grtl.addPoint(run, h1.getMean(), 0, 0)
  grtl2.addPoint(run, h1.getRMS(), 0, 0)
  out.addDataSet(h1)
  // out.addDataSet(f1)

}


out.mkdir('/timelines')
out.cd('/timelines')
grtl.each{ out.addDataSet(it) }
grtl2.each{ out.addDataSet(it) }
out.writeFile('FTC_time_charged.hipo')

import org.jlab.groot.data.TDirectory
import org.jlab.groot.data.GraphErrors
import fitter.FTFitter


  def grtl = new GraphErrors('Mean')
  grtl.setTitle("FTC time - start time, charged (peak value)")
  grtl.setTitleY("FTC time - start time, charged (peak value) (ns)")
  grtl.setTitleX("run number")

  def grtl2 = new GraphErrors('Sigma')
  grtl2.setTitle("FTC time - start time, charged (sigma)")
  grtl2.setTitleY("FTC time - start time, charged (sigma) (ns)")
  grtl2.setTitleX("run number")


TDirectory out = new TDirectory()
TDirectory out2 = new TDirectory()
for(arg in args) {
  TDirectory dir = new TDirectory()
  dir.readFile(arg)

  def name = arg.split('/')[-1]
  def m = name =~ /\d{4,5}/
  def run = m[0].toInteger()

  out.mkdir('/'+run)
  out.cd('/'+run)
  out2.mkdir('/'+run)
  out2.cd('/'+run)

  def h1 = dir.getObject('/ft/hi_cal_time_cut_ch')
  // def f1 = ROOTFitter.fit(h1)
  def ftime_ch = FTFitter.ftctimefit(h1)
  //grtl[it].addPoint(run, h1.getDataX(h1.getMaximumBin()), 0, 0)
  grtl.addPoint(run, ftime_ch.getParameter(1), 0, 0)
  grtl2.addPoint(run, ftime_ch.getParameter(2), 0, 0)
  // grtl.addPoint(run, h1.getMean(), 0, 0)
  // grtl2.addPoint(run, h1.getRMS(), 0, 0)
  out.addDataSet(h1)
  out.addDataSet(ftime_ch)
  out2.addDataSet(h1)
  out2.addDataSet(ftime_ch)

}

out.mkdir('/timelines')
out.cd('/timelines')
out2.mkdir('/timelines')
out2.cd('/timelines')
grtl.each{ out.addDataSet(it) }
grtl2.each{ out2.addDataSet(it) }
out.writeFile('ftc_time_ch_mean.hipo')
out2.writeFile('ftc_time_ch_sigma.hipo')
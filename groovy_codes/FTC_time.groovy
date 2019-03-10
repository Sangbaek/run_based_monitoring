import org.jlab.groot.data.TDirectory
import org.jlab.groot.data.GraphErrors
import ROOTFitter

  def gr = new GraphErrors('FTH MIPS time')
  gr.setTitle("FTC time - start time (peak value)")
  gr.setTitleY("FTC time - start time (peak value)")
  gr.setTitleX("run number")
  return gr


TDirectory out = new TDirectory()
for(arg in args.drop(1)) {
  TDirectory dir = new TDirectory()
  dir.readFile(arg)

  def name = arg.split('/')[-1]
  def m = name =~ /\d\d\d\d/
  def run = m[0].toInteger()

  out.mkdir('/'+run)
  out.cd('/'+run)

  def h1 = dir.getObject('/ft/hi_cal_time_ch')
  def h2 = dir.getObject('/ft/hi_cal_time_neu')
  h1.Add(h2)
  def f1 = ROOTFitter.fit(h1)

  //grtl[it].addPoint(run, h1.getDataX(h1.getMaximumBin()), 0, 0)
  grtl[it].addPoint(run, f1.getParameter(1), 0, 0)
  out.addDataSet(h1)
  out.addDataSet(f1)

}


out.mkdir('/timelines')
out.cd('/timelines')
grtl.each{ out.addDataSet(it) }
out.writeFile('out_FTC_time.hipo')

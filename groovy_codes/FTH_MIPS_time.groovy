import org.jlab.groot.data.TDirectory
import org.jlab.groot.data.GraphErrors
// import ROOTFitter

  def grtl = new GraphErrors('FTH MIPS time')
  grtl.setTitle("FTH MIPS time - start time (peak value)")
  grtl.setTitleY("FTH MIPS time - start time (peak value)")
  grtl.setTitleX("run number")

  def grtl2 = new GraphErrors('FTH MIPS time, sigma')
  grtl2.setTitle("FTH MIPS time - start time (sigma)")
  grtl2.setTitleY("FTH MIPS time - start time (sigma)")
  grtl2.setTitleX("run number")


TDirectory out = new TDirectory()
for(arg in args.drop(1)) {
  TDirectory dir = new TDirectory()
  dir.readFile(arg)

  def name = arg.split('/')[-1]
  def m = name =~ /\d\d\d\d/
  def run = m[0].toInteger()

  out.mkdir('/'+run)
  out.cd('/'+run)

  def h1 = dir.getObject('/ft/hi_hodo_tmatch_l1')
  def h2 = dir.getObject('/ft/hi_hodo_tmatch_l2')
  h1.Add(h2)
  // def f1 = ROOTFitter.fit(h1)

  //grtl[it].addPoint(run, h1.getDataX(h1.getMaximumBin()), 0, 0)
  // grtl.addPoint(run, f1.getParameter(1), 0, 0)
  // grtl2.addPoint(run, f1.getParameter(2), 0, 0)
  grtl.addPoint(run, h1.getMean(), 0, 0)
  grtl2.addPoint(run, f1.getStdDev(), 0, 0)
  out.addDataSet(h1)
  // out.addDataSet(f1)

}


out.mkdir('/timelines')
out.cd('/timelines')
grtl.each{ out.addDataSet(it) }
grtl2.each{ out.addDataSet(it) }
out.writeFile('out_FTH_MIPS_time.hipo')

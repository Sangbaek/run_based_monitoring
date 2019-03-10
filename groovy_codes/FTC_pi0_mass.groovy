import org.jlab.groot.data.TDirectory
import org.jlab.groot.data.GraphErrors
import ROOTFitter

  def grtl = new GraphErrors('FTH MIPS time')
  grtl.setTitle("FTC pi0 mass (peak value)")
  grtl.setTitleY("FTC pi0 mass (peak value)")
  grtl.setTitleX("run number")
  return grtl


TDirectory out = new TDirectory()
for(arg in args.drop(1)) {
  TDirectory dir = new TDirectory()
  dir.readFile(arg)

  def name = arg.split('/')[-1]
  def m = name =~ /\d\d\d\d/
  def run = m[0].toInteger()

  out.mkdir('/'+run)
  out.cd('/'+run)

  def h1 = dir.getObject('/ft/hpi0sum')
  def f1 = ROOTFitter.fit(h1)

  //grtl[it].addPoint(run, h1.getDataX(h1.getMaximumBin()), 0, 0)
  grtl.addPoint(run, f1.getParameter(1), 0, 0)
  out.addDataSet(h1)
  out.addDataSet(f1)

}


out.mkdir('/timelines')
out.cd('/timelines')
grtl.each{ out.addDataSet(it) }
out.writeFile('out_FTC_pi0_mass.hipo')

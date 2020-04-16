import org.jlab.groot.data.TDirectory
import org.jlab.groot.data.GraphErrors
import fitter.FTFitter

def grtl = (1..2).collect{
  def gr = new GraphErrors('layer'+it)
  gr.setTitle("FTH MIPS time per layer (peak value)")
  gr.setTitleY("FTH MIPS time per layer (peak value) (ns)")
  gr.setTitleX("run number")
  return gr
}

def grtl2 = (1..2).collect{
  def gr2 = new GraphErrors('layer'+it)
  gr2.setTitle("FTH MIPS time per layer (sigma)")
  gr2.setTitleY("FTH MIPS time per layer (sigma) (ns)")
  gr2.setTitleX("run number")
  return gr2
}

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
  (0..<2).each{
    def h1 = dir.getObject('/ft/hi_hodo_tmatch_l'+(it+1))
    def f1 = FTFitter.fthtimefit(h1)
   // def h1 = h2.projectionY()
    // h1.setName("layer"+(it+1))
    // h1.setTitle("FTH_MIPS_energy")
    // h1.setTitleX("E (MeV)")

    // def f1 = ROOTFitter.fit(h1)

    //grtl[it].addPoint(run, h1.getDataX(h1.getMaximumBin()), 0, 0)
    grtl[it].addPoint(run, f1.getParameter(1), 0, 0)
    grtl2[it].addPoint(run, f1.getParameter(2), 0, 0)
    // grtl[it].addPoint(run, h1.getMean(), 0, 0)
    out.addDataSet(h1)
    out.addDataSet(f1)
    out2.addDataSet(h1)
    out2.addDataSet(f1)
  }
}
out.mkdir('/timelines')
out.cd('/timelines')
out2.mkdir('/timelines')
out2.cd('/timelines')
grtl.each{ out.addDataSet(it) }
grtl2.each{ out2.addDataSet(it) }
out.writeFile('fth_MIPS_time_mean.hipo')
out2.writeFile('fth_MIPS_time_sigma.hipo')
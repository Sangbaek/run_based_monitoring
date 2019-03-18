import org.jlab.groot.data.TDirectory
import org.jlab.groot.data.GraphErrors
// import ROOTFitter

def grtl = (1..2).collect{
  def gr = new GraphErrors('layer'+it)
  gr.setTitle("FTH MIPS energy per layer (Mean)")
  gr.setTitleY("FTH MIPS energy per layer (Mean)")
  gr.setTitleX("run number")
  return gr
}

TDirectory out = new TDirectory()

for(arg in args.drop(1)) {
  TDirectory dir = new TDirectory()
  dir.readFile(arg)

  def name = arg.split('/')[-1]
  def m = name =~ /\d\d\d\d/
  def run = m[0].toInteger()

  out.mkdir('/'+run)
  out.cd('/'+run)

  (0..<2).each{
    def h1 = dir.getObject('/ft/hi_hodo_ematch_l'+(it+1))
    // def h1 = h2.projectionY()
    // h1.setName("layer"+(it+1))
    // h1.setTitle("FTH_MIPS_energy")
    // h1.setTitleX("E (MeV)")

    // def f1 = ROOTFitter.fit(h1)

    //grtl[it].addPoint(run, h1.getDataX(h1.getMaximumBin()), 0, 0)
    // grtl[it].addPoint(run, f1.getParameter(1), 0, 0)
    grtl[it].addPoint(run, h1.getMean(), 0, 0)
    out.addDataSet(h1)
    // out.addDataSet(f1)
  }
}


out.mkdir('/timelines')
out.cd('/timelines')
grtl.each{ out.addDataSet(it) }
out.writeFile('FTH_MIPS_energy.hipo')

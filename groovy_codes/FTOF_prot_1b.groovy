import org.jlab.groot.data.TDirectory
import org.jlab.groot.data.GraphErrors
// import ROOTFitter

def grtl = (1..6).collect{
  def gr = new GraphErrors('sec'+it)
  gr.setTitle("FTOF p peak")
  gr.setTitleY("FTOF p peak (GeV)")
  gr.setTitleX("run number")
  return gr
}

TDirectory out = new TDirectory()

for(arg in args) {
  TDirectory dir = new TDirectory()
  dir.readFile(arg)

  def name = arg.split('/')[-1]
  def m = name =~ /\d\d\d\d/
  def run = m[0].toInteger()

  out.mkdir('/'+run)
  out.cd('/'+run)

  (0..<6).each{
    def h2 = dir.getObject(String.format("H_FTOF_pos_mass_mom_pad1b_%d",it+1))
    def h1 = h2.projectionY()
    h1.setName("sec"+(it+1))
    h1.setTitle("FTOF prot mass")
    h1.setTitleX("FTOF prot mass (GeV)")

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
out.writeFile('FTOF1b_protm.hipo')

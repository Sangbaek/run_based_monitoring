import org.jlab.groot.data.TDirectory
import org.jlab.groot.data.GraphErrors
// import ROOTFitter

def grtl = (1..6).collect{
  def gr = new GraphErrors('sec'+it)
  gr.setTitle("Electron RFtime1 per sector")
  gr.setTitleY("Electron RFtime1 per sector")
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
    def h1 = dir.getObject('/RF/H_pip_RFtime1_S'+(it+1))
    // def h1 = h2.projectionY()
    // h1.setName("sec"+(it+1))
    // h1.setTitle("VZ for electrons")
    // h1.setTitleX("VZ for electrons")

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
out.writeFile('RFtime_pip.hipo')
import org.jlab.groot.data.TDirectory
import org.jlab.groot.data.GraphErrors
// import ROOTFitter

def grtl = (1..3).collect{
  def gr = new GraphErrors('layer'+it+' Mean')
  gr.setTitle("CVT z - CVT z per layer")
  gr.setTitleY("CVT z - CVT z per layer (cm)")
  gr.setTitleX("run number")
  return gr
}

def grtl2 = (1..3).collect{
  def gr2 = new GraphErrors('layer'+it+' Sigma')
  gr2.setTitle("CVT z - CVT z per layer")
  gr2.setTitleY("CVT z - CVT z per layer (cm)")
  gr2.setTitleX("run number")
  return gr2
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

  (0..<3).each{
    def h2 = dir.getObject(String.format("/cnd/Diff Z CVT_L%d",it+1))
    def h1 = h2.projectionY()
    iL=it+1
    h1.setName("layer"+iL)
    h1.setTitle("CVT z - CND z")
    h1.setTitleX("CVT z - CND z (cm)")

    // def f1 = ROOTFitter.fit(h1)

    //grtl[it].addPoint(run, h1.getDataX(h1.getMaximumBin()), 0, 0)
    // grtl[it].addPoint(run, f1.getParameter(1), 0, 0)
    // grtl2[it].addPoint(run, f1.getParameter(2), 0, 0)
    grtl[it].addPoint(run, h1.getMean(), 0, 0)
    grtl2[it].addPoint(run, h1.getRMS(), 0, 0)
    out.addDataSet(h1)
    // out.addDataSet(f1)
  }
}


out.mkdir('/timelines')
out.cd('/timelines')
grtl.each{ out.addDataSet(it) }
grtl2.each{ out.addDataSet(it) }
out.writeFile('CND_zdiff.hipo')

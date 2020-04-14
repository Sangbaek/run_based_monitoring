import org.jlab.groot.data.TDirectory
import org.jlab.groot.data.GraphErrors
import org.jlab.groot.data.H1F
import org.jlab.groot.group.DataGroup;
import org.jlab.groot.math.F1D;
import org.jlab.groot.fitter.DataFitter;
import org.jlab.groot.graphics.EmbeddedCanvas;
import fitter.CNDFitter

def grtl = (1..3).collect{
  def gr = new GraphErrors('layer'+it+' Mean')
  gr.setTitle("CVT z - CND z per layer")
  gr.setTitleY("CVT z - CND z per layer (cm)")
  gr.setTitleX("run number")
  return gr
}

def grtl2 = (1..3).collect{
  def gr2 = new GraphErrors('layer'+it+' Sigma')
  gr2.setTitle("CVT z - CND z per layer")
  gr2.setTitleY("CVT z - CND z per layer (cm)")
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

  (0..<3).each{
    def h2 = dir.getObject(String.format("/cnd/Diff Z CND_L%d",it+1))
    def h1 = h2.projectionY()
    iL=it+1
    h1.setName("layer"+iL)
    h1.setTitle("CVT z - CND z")
    h1.setTitleX("CVT z - CND z (cm)")

    def f1 = CNDFitter.zdifffit(h1)

    //grtl[it].addPoint(run, h1.getDataX(h1.getMaximumBin()), 0, 0)
    grtl[it].addPoint(run, f1.getParameter(1), 0, 0)
    grtl2[it].addPoint(run, f1.getParameter(2), 0, 0)

    out.addDataSet(h1)
    out.addDataSet(f1)
    out2.addDataSet(h1)
    out2.addDataSet(f1)


  }
}


out.mkdir('/timelines')
out.cd('/timelines')
grtl.each{ out.addDataSet(it) }
out.writeFile('cnd_zdiff_mean.hipo')

out2.mkdir('/timelines')
out2.cd('/timelines')
grtl2.each{ out2.addDataSet(it) }
out2.writeFile('cnd_zdiff_sigma.hipo')


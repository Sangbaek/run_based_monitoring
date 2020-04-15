import org.jlab.groot.data.TDirectory
import org.jlab.groot.data.GraphErrors
import org.jlab.groot.data.H1F
import org.jlab.groot.group.DataGroup;
import org.jlab.groot.math.F1D;
import org.jlab.groot.fitter.DataFitter;
import org.jlab.groot.graphics.EmbeddedCanvas;
import fitter.ForwardFitter

def grtl = (1..6).collect{
  def gr = new GraphErrors('sec'+it)
  gr.setTitle("VZ (peak value) per sector")
  gr.setTitleY("VZ (peak value) per sector")
  gr.setTitleX("run number")
  return gr
}

TDirectory out = new TDirectory()

for(arg in args) {
  TDirectory dir = new TDirectory()
  dir.readFile(arg)

  def name = arg.split('/')[-1]
  def m = name =~ /\d{4,5}/
  def run = m[0].toInteger()

  out.mkdir('/'+run)
  out.cd('/'+run)

  (0..<6).each{
    def h1 = dir.getObject('/dc/H_dcp_vz_s'+(it+1))
    h1.setTitleX("VZ for positive (cm)")

    def f1 = ForwardFitter.fit(h1)

    grtl[it].addPoint(run, f1.getParameter(1), 0, 0)

    //grtl[it].addPoint(run, h1.getDataX(h1.getMaximumBin()), 0, 0)
    // grtl[it].addPoint(run, h1.getMean(), 0, 0)
    out.addDataSet(h1)
    out.addDataSet(f1)
  }
}

out.mkdir('/timelines')
out.cd('/timelines')
grtl.each{ out.addDataSet(it) }
out.writeFile('forward_positive_VZ.hipo')
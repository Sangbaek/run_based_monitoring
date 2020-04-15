import org.jlab.groot.data.TDirectory
import org.jlab.groot.data.GraphErrors
import org.jlab.groot.data.H1F
import org.jlab.groot.group.DataGroup;
import org.jlab.groot.math.F1D;
import org.jlab.groot.fitter.DataFitter;
import org.jlab.groot.graphics.EmbeddedCanvas;
import fitter.FTOFFitter_mass

def grtl = (1..6).collect{
  def gr = new GraphErrors('sec'+it)
  gr.setTitle("FTOF #pi^- mass^2 peak")
  gr.setTitleY("FTOF #pi^- mass^2 peak (GeV^2)")
  gr.setTitleX("run number")
  return gr
}

def grtl2 = (1..6).collect{
  def gr = new GraphErrors('sec'+it)
  gr.setTitle("FTOF #pi^- mass^2 sigma")
  gr.setTitleY("FTOF #pi^- mass^2 sigma (GeV^2)")
  gr.setTitleX("run number")
  return gr
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

  (0..<6).each{
    def h2 = dir.getObject(String.format("/FTOF/H_FTOF_neg_mass_mom_pad1a_%d",it+1))
    def h1 = h2.projectionY()
    h1.setName("sec"+(it+1))
    h1.setTitle("FTOF p1a negative, mass^2")
    h1.setTitleX("FTOF p1a negative, mass^2 (GeV^2)")

    def f1 = FTOFFitter_mass.fit(h1)

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
out.writeFile('ftof_m2_p1a_pim_mean.hipo')
out2.mkdir('/timelines')
out2.cd('/timelines')
grtl2.each{ out2.addDataSet(it) }
out2.writeFile('ftof_m2_p1a_pim_sigma.hipo')
import org.jlab.groot.data.TDirectory
import org.jlab.groot.data.GraphErrors
import org.jlab.groot.data.H1F
import org.jlab.groot.group.DataGroup;
import org.jlab.groot.math.F1D;
import org.jlab.groot.fitter.DataFitter;
import org.jlab.groot.graphics.EmbeddedCanvas;
import fitter.FTFitter

def grtl = (1..2).collect{
  def gr = new GraphErrors('layer'+it)
  gr.setTitle("FTH MIPS energy per layer (peak value)")
  gr.setTitleY("FTH MIPS energy per layer (peak value) (MeV)")
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

  (0..<2).each{
    def h1 = dir.getObject('/ft/hi_hodo_ematch_l'+(it+1))
    def f_charge_landau = FTFitter.fthedepfit(h1, it)
    grtl[it].addPoint(run, f_charge_landau.getParameter(1), 0, 0)
    out.addDataSet(h1)
    out.addDataSet(f_charge_landau)
  }
}


out.mkdir('/timelines')
out.cd('/timelines')
grtl.each{ out.addDataSet(it) }
out.writeFile('fth_MIPS_energy.hipo')
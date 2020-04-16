import org.jlab.groot.data.TDirectory
import org.jlab.groot.data.GraphErrors
import fitter.FTOFFitter

def grtl = (1..6).collect{
  def gr = new GraphErrors('sec'+it)
  gr.setTitle("p1b Pathlength Corrected Edep for negative tracks, small angles (<= 11 deg)")
  gr.setTitleY("p1b Pathlength Corrected Edep for negative tracks, small angles (<= 11 deg) (MeV)")
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
    def h1 = dir.getObject('/tof/p1b_edep_smallangles_S'+(it+1))
    def f1 = FTOFFitter.edepfit(h1)

    grtl[it].addPoint(run, f1.getParameter(1), 0, 0)
    out.addDataSet(h1)
    out.addDataSet(f1)
  }
}


out.mkdir('/timelines')
out.cd('/timelines')
grtl.each{ out.addDataSet(it) }
out.writeFile('ftof_edep_p1b_smallangles.hipo')
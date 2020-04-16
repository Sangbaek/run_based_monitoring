import org.jlab.groot.data.TDirectory
import org.jlab.groot.data.GraphErrors
import fitter.FTOFFitter

def grtl = (1..6).collect{
  def gr = new GraphErrors('Mean, sec'+it)
  gr.setTitle("p1b Vertex-time difference FTOF_vtime-RFT for pions and electrons (mean)")
  gr.setTitleY("p1b Vertex-time difference FTOF_vtime-RFT for pions and electrons (mean) (ns)")
  gr.setTitleX("run number")
  return gr
}
def grtl2 = (1..6).collect{
  def gr = new GraphErrors('Sigma,'+it)
  gr.setTitle("p1b Vertex-time difference FTOF_vtime-RFT for pions and electrons (sigma)")
  gr.setTitleY("p1b Vertex-time difference FTOF_vtime-RFT for pions and electrons (sigma) (ns)")
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
    def h1 = dir.getObject('/tof/p1b_dt_S'+(it+1))
    def f1 = FTOFFitter.timefit_p1(h1)

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
out.writeFile('ftof_time_p1b_mean.hipo')

out2.mkdir('/timelines')
out2.cd('/timelines')
grtl2.each{ out2.addDataSet(it) }
out2.writeFile('ftof_time_p1b_sigma.hipo')
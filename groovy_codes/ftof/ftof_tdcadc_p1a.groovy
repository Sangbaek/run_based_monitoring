import org.jlab.groot.data.TDirectory
import org.jlab.groot.data.GraphErrors
import fitter.FTOFFitter

def grtl = (1..6).collect{
  def gr = new GraphErrors('Mean, sec'+it)
  gr.setTitle("p1a t_tdc-t_fadc (mean)")
  gr.setTitleY("p1a t_tdc-t_fadc (mean) (ns)")
  gr.setTitleX("run number")
  return gr
}
def grtl2 = (1..6).collect{
  def gr = new GraphErrors('Sigma,'+it)
  gr.setTitle("p1a t_tdc-t_fadc (sigma)")
  gr.setTitleY("p1a t_tdc-t_fadc (sigma) (ns)")
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
    def h1 = dir.getObject('/tof/p1a_tdcadc_dt_S'+(it+1))
    def f1 = FTOFFitter.tdcadcdifffit_p1a(h1)

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
out.writeFile('ftof_tdcadc_time_p1a_mean.hipo')

out2.mkdir('/timelines')
out2.cd('/timelines')
grtl2.each{ out2.addDataSet(it) }
out2.writeFile('ftof_tdcadc_time_p1a_sigma.hipo')
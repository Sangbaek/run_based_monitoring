import org.jlab.groot.data.TDirectory
import org.jlab.groot.data.GraphErrors
// import ROOTFitter

def grtl = (1..6).collect{
  def gr = new GraphErrors('sec'+it)
  gr.setTitle("Electrons per trigger per sector")
  gr.setTitleY("Electrons per trigger per sector")
  gr.setTitleX("run number")
  return gr
}


TDirectory out = new TDirectory()

for(arg in args) {
  TDirectory dir = new TDirectory()
  dir.readFile(arg)

  def name = arg.split('/')[-1]
  def m = name =~ /d{4,5}/
  def run = m[0].toInteger()


    // def h2 = dir.getObject('/elec/H_trig_vz_mom_S'+(it+1))
    // def h1 = h2.projectionY()
    def h1 = dir.getObject('/trig/H_trig_sector_elec_rat')
    // grtl1.addPoint(run, h1.getBinContent(1), 0, 0)
    (0..<6).each{
      grtl[it].addPoint(run, h1.getBinContent(it), 0, 0)
    }
    out.mkdir('/'+run)
    out.cd('/'+run)
    out.addDataSet(h1)
    // out.addDataSet(f1)
}


out.mkdir('/timelines')
out.cd('/timelines')
grtl.each{ out.addDataSet(it) }

out.writeFile('rat_electron.hipo')

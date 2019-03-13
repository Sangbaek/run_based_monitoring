import org.jlab.groot.data.TDirectory
import org.jlab.groot.data.GraphErrors
// import ROOTFitter

def grtl = new GraphErrors('trig_sector_elec_rat')
grtl.setTitle("Electrons per trigger per sector")
grtl.setTitleY("Electrons per trigger per sector")
grtl.setTitleX("run number")


TDirectory out = new TDirectory()

for(arg in args.drop(1)) {
  TDirectory dir = new TDirectory()
  dir.readFile(arg)

  def name = arg.split('/')[-1]
  def m = name =~ /\d\d\d\d/
  def run = m[0].toInteger()

  out.mkdir('/'+run)
  out.cd('/'+run)

    // def h2 = dir.getObject('/elec/H_trig_vz_mom_S'+(it+1))
    // def h1 = h2.projectionY()
    def h1 = dir.getObject('/trig/H_trig_sector_elec_rat')
    grtl.addPoint(run, h1.getBinContent(1), 0, 0)
    grtl.addPoint(run, h1.getBinContent(2), 0, 0)
    grtl.addPoint(run, h1.getBinContent(3), 0, 0)
    grtl.addPoint(run, h1.getBinContent(4), 0, 0)
    grtl.addPoint(run, h1.getBinContent(5), 0, 0)
    grtl.addPoint(run, h1.getBinContent(6), 0, 0)
    // grtl[it].addPoint(run, f1.getParameter(1), 0, 0)
    out.addDataSet(h1)
    // out.addDataSet(f1)
}


out.mkdir('/timelines')
out.cd('/timelines')
grtl.each{ out.addDataSet(it) }
out.writeFile('out_elec_num.hipo')

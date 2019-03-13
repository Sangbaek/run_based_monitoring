import org.jlab.groot.data.TDirectory
import org.jlab.groot.data.GraphErrors
// import ROOTFitter

def grtl1 = new GraphErrors('sec 1')
grtl1.setTitle("Electrons per trigger per sector")
grtl1.setTitleY("Electrons per trigger per sector")
grtl1.setTitleX("run number")

def grtl2 = new GraphErrors('sec 2')
grtl2.setTitle("Electrons per trigger per sector")
grtl2.setTitleY("Electrons per trigger per sector")
grtl2.setTitleX("run number")

def grtl3 = new GraphErrors('sec 3')
grtl3.setTitle("Electrons per trigger per sector")
grtl3.setTitleY("Electrons per trigger per sector")
grtl3.setTitleX("run number")

def grtl4 = new GraphErrors('sec 4')
grtl4.setTitle("Electrons per trigger per sector")
grtl4.setTitleY("Electrons per trigger per sector")
grtl4.setTitleX("run number")

def grtl5 = new GraphErrors('sec 5')
grtl5.setTitle("Electrons per trigger per sector")
grtl5.setTitleY("Electrons per trigger per sector")
grtl5.setTitleX("run number")

def grtl6 = new GraphErrors('sec 6')
grtl6.setTitle("Electrons per trigger per sector")
grtl6.setTitleY("Electrons per trigger per sector")
grtl6.setTitleX("run number")


TDirectory out = new TDirectory()

for(arg in args.drop(1)) {
  TDirectory dir = new TDirectory()
  dir.readFile(arg)

  def name = arg.split('/')[-1]
  def m = name =~ /\d\d\d\d/
  def run = m[0].toInteger()


    // def h2 = dir.getObject('/elec/H_trig_vz_mom_S'+(it+1))
    // def h1 = h2.projectionY()
    def h1 = dir.getObject('/trig/H_trig_sector_elec_rat')
    grtl1.addPoint(run, h1.getBinContent(1), 0, 0)
    grtl2.addPoint(run, h1.getBinContent(2), 0, 0)
    grtl3.addPoint(run, h1.getBinContent(3), 0, 0)
    grtl4.addPoint(run, h1.getBinContent(4), 0, 0)
    grtl5.addPoint(run, h1.getBinContent(5), 0, 0)
    grtl6.addPoint(run, h1.getBinContent(6), 0, 0)
    // grtl[it].addPoint(run, f1.getParameter(1), 0, 0)
    out.mkdir('/'+run)
    out.cd('/'+run)
    out.addDataSet(h1)
    // out.addDataSet(f1)
}


out.mkdir('/timelines')
out.cd('/timelines')
grtl1.each{ out.addDataSet(it) }
grtl2.each{ out.addDataSet(it) }
grtl3.each{ out.addDataSet(it) }
grtl4.each{ out.addDataSet(it) }
grtl5.each{ out.addDataSet(it) }
grtl6.each{ out.addDataSet(it) }

out.writeFile('out_elec_num.hipo')

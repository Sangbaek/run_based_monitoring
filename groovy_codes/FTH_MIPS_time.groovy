import org.jlab.groot.data.TDirectory
import org.jlab.groot.data.GraphErrors
// import ROOTFitter

def grtl1 = new GraphErrors('layer 1, Mean')
grtl1.setTitle("FTH MIPS time, neutral")
grtl1.setTitleY("FTH MIPS time, neutral (ns)")
grtl1.setTitleX("run number")

def grtl2 = new GraphErrors('layer 1, Sigma')
grtl2.setTitle("FTH MIPS time, neutral")
grtl2.setTitleY("FTH MIPS time, neutral (ns)")
grtl2.setTitleX("run number")

def grtl3 = new GraphErrors('layer 2, Mean')
grtl3.setTitle("FTH MIPS time, neutral")
grtl3.setTitleY("FTH MIPS time, neutral (ns)")
grtl3.setTitleX("run number")

def grtl4 = new GraphErrors('layer 2, Sigma')
grtl4.setTitle("FTH MIPS time, neutral")
grtl4.setTitleY("FTH MIPS time, neutral (ns)")
grtl4.setTitleX("run number")

TDirectory out = new TDirectory()
for(arg in args) {
  TDirectory dir = new TDirectory()
  dir.readFile(arg)

  def name = arg.split('/')[-1]
  def m = name =~ /\d\d\d\d/
  def run = m[0].toInteger()

  out.mkdir('/'+run)
  out.cd('/'+run)

  def h1 = dir.getObject('/ft/hi_hodo_tmatch_l1')
  def h2 = dir.getObject('/ft/hi_hodo_tmatch_l2')
  // h1.add(h2)
  // def f1 = ROOTFitter.fit(h1)

  //grtl[it].addPoint(run, h1.getDataX(h1.getMaximumBin()), 0, 0)
  // grtl.addPoint(run, f1.getParameter(1), 0, 0)
  // grtl2.addPoint(run, f1.getParameter(2), 0, 0)
  grtl1.addPoint(run, h1.getMean(), 0, 0)
  grtl2.addPoint(run, h1.getRMS(), 0, 0)
  grtl3.addPoint(run, h2.getMean(), 0, 0)
  grtl4.addPoint(run, h2.getRMS(), 0, 0)
  out.addDataSet(h1)
  out.addDataSet(h2)
  // out.addDataSet(f1)

}


out.mkdir('/timelines')
out.cd('/timelines')
grtl1.each{ out.addDataSet(it) }
grtl2.each{ out.addDataSet(it) }
grtl3.each{ out.addDataSet(it) }
grtl4.each{ out.addDataSet(it) }
out.writeFile('FTH_MIPS_time.hipo')

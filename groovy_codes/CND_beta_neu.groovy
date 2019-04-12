import org.jlab.groot.data.TDirectory
import org.jlab.groot.data.GraphErrors
// import ROOTFitter

def grtl = new GraphErrors('Mean')
grtl.setTitle("CND beta, neutral")
grtl.setTitleY("CND beta, neutral")
grtl.setTitleX("run number")

def grtl2 = new GraphErrors('Sigma')
grtl2.setTitle("CND beta, neutral")
grtl2.setTitleY("CND beta, neutral")
grtl2.setTitleX("run number")

TDirectory out = new TDirectory()

for(arg in args) {
  TDirectory dir = new TDirectory()
  dir.readFile(arg)

  def name = arg.split('/')[-1]
  def m = name =~ /\d\d\d\d/
  def run = m[0].toInteger()

  out.mkdir('/'+run)
  out.cd('/'+run)

  // (0..<3).each{
    // def h2 = dir.getObject('/elec/H_trig_vz_mom_S'+(it+1))
    // def h1 = h2.projectionY()
    // iL=it+1
    def h2 = dir.getObject(String.format("/cnd/H_CND_beta_e_neutral"))
    def h1 = h2.projectionY()
    h1.setTitle("beta for neutral")
    h1.setTitleX("beta for neutral")

    // def f1 = ROOTFitter.fit(h1)

    //grtl[it].addPoint(run, h1.getDataX(h1.getMaximumBin()), 0, 0)
    // grtl[it].addPoint(run, f1.getParameter(1), 0, 0)
    // grtl2[it].addPoint(run, f1.getParameter(2), 0, 0)
    grtl.addPoint(run, h1.getMean(), 0, 0)
    grtl2.addPoint(run, h1.getRMS(), 0, 0)
    out.addDataSet(h1)
    // out.addDataSet(f1)
  }
// }


out.mkdir('/timelines')
out.cd('/timelines')
grtl.each{ out.addDataSet(it) }
grtl2.each{ out.addDataSet(it) }
out.writeFile('CND_beta_neu.hipo')

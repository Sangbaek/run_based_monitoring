import org.jlab.groot.data.TDirectory
import org.jlab.groot.data.GraphErrors
import ROOTFitter

def grtl = (1..6).collect{
  def gr = new GraphErrors('sec'+it)
  gr.setTitle("ECAL Sampling Fraction per sector")
  gr.setTitleY("ECAL Sampling Fraction per sector")
  gr.setTitleX("run number")
  return gr
}

TDirectory out = new TDirectory()

for(arg in args.drop(1)) {
  TDirectory dir = new TDirectory()
  dir.readFile(arg)

  def name = arg.split('/')[-1]
  def m = name =~ /\d\d\d\d/
  def run = m[0].toInteger()

  out.mkdir('/'+run)
  out.cd('/'+run)

  (0..<6).each{
    // def h2 = dir.getObject('/elec/H_trig_vz_mom_S'+(it+1))
    // def h1 = h2.projectionY()
    def h1 = dir.getObject('/elec/H_trig_ECALsampl_S'+(it+1))
    h1.setName("sec"+(it+1))
    h1.setTitle("ECAL Sampling Fraction")
    h1.setTitleX("ECAL Sampling Fraction")

    // def f1 = ROOTFitter.fit(h1)

    grtl[it].addPoint(run, h1.getDataX(h1.getMaximumBin()), 0, 0)
    // grtl[it].addPoint(run, f1.getParameter(1), 0, 0)
    out.addDataSet(h1)
    // out.addDataSet(f1)
  }
}


out.mkdir('/timelines')
out.cd('/timelines')
grtl.each{ out.addDataSet(it) }
out.writeFile('ECAL_Sampl.hipo')

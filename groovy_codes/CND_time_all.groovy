import org.jlab.groot.data.TDirectory
import org.jlab.groot.data.GraphErrors
import ROOTFitter

def grtl = (1..3).collect{
  def gr = new GraphErrors('layer'+sl_num)
  gr.setTitle("CND time per layer")
  gr.setTitleY("CND time per layer")
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

  (0..<3).each{
    // def h2 = dir.getObject('/elec/H_trig_vz_mom_S'+(it+1))
    // def h1 = h2.projectionY()
    iL=it+1
    def h1 = dir.getObject(String.format("/cnd/H_CND_time_all%d",iL)
    h1.setName("all, layer"+iL)
    h1.setTitle("CND time - start time")
    h1.setTitleX("CND time - start time")

    def f1 = ROOTFitter.fit(h1)

    //grtl[it].addPoint(run, h1.getDataX(h1.getMaximumBin()), 0, 0)
    grtl[it].addPoint(run, f1.getParameter(1), 0, 0)
    out.addDataSet(h1)
    out.addDataSet(f1)
  }
}


out.mkdir('/timelines')
out.cd('/timelines')
grtl.each{ out.addDataSet(it) }
out.writeFile('out_CND_time_all.hipo')

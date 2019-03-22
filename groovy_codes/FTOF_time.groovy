import org.jlab.groot.data.TDirectory
import org.jlab.groot.data.GraphErrors
// import ROOTFitter

def grtl = (1..6).collect{
  def gr = new GraphErrors('sec'+it)
  gr.setTitle("Mean FTOF time per sector")
  gr.setTitleY("Mean FTOF time per sector (ns)")
  gr.setTitleX("run number")
  return gr
}

TDirectory out = new TDirectory()

for(arg in args) {
  TDirectory dir = new TDirectory()
  dir.readFile(arg)

  def name = arg.split('/')[-1]
  def m = name =~ /\d\d\d\d/
  def run = m[0].toInteger()

  out.mkdir('/'+run)
  out.cd('/'+run)

  (0..<6).each{
    def h2 = dir.getObject('/tof/p1a_pad_vt_S'+(it+1))
    def h1 = h2.projectionY()
    def h4 = dir.getObject('/tof/p1b_pad_vt_S'+(it+1))
    def h3 = h4.projectionY()
    def h6 = dir.getObject('/tof/p2_pad_vt_S'+(it+1))
    def h5 = h6.projectionY()
    h1.add(h3)
    h1.add(h5)
    h1.setName("sec"+(it+1))
    h1.setTitle("FTOF StartTime - RFtime (ns)")
    h1.setTitleX("FTOF StartTime - RFtime")

    // def f1 = ROOTFitter.fit(h1)

    //grtl[it].addPoint(run, h1.getDataX(h1.getMaximumBin()), 0, 0)
    // grtl[it].addPoint(run, f1.getParameter(1), 0, 0)
    grtl[it].addPoint(run, h1.getMean(), 0, 0)
    out.addDataSet(h1)
    // out.addDataSet(f1)
  }
}


out.mkdir('/timelines')
out.cd('/timelines')
grtl.each{ out.addDataSet(it) }
out.writeFile('FTOF_time.hipo')

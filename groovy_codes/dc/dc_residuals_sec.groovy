import org.jlab.groot.data.TDirectory
import org.jlab.groot.data.GraphErrors
import fitter.DCFitter

def grtl = (1..6).collect{
  sec_num = it
  def gr = new GraphErrors('sec'+sec_num)
  gr.setTitle("DC residuals (peak value) per sector")
  gr.setTitleY("DC residuals (peak value) per sector (cm)")
  gr.setTitleX("run number")
  return gr
}

def grtl2 = (1..6).collect{
  sec_num = it
  def gr = new GraphErrors('sec'+sec_num)
  gr.setTitle("DC residuals (sigma) per sector")
  gr.setTitleY("DC residuals (sigma) per sector (cm)")
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
    sec_num = (it+1)
    def h21 = dir.getObject(String.format('/dc/DC_residuals_trkDoca_%d_1',sec_num))
    def h11 = h21.projectionY()
    def h22 = dir.getObject(String.format('/dc/DC_residuals_trkDoca_%d_2',sec_num))
    def h12 = h22.projectionY()
    def h23 = dir.getObject(String.format('/dc/DC_residuals_trkDoca_%d_3',sec_num))
    def h13 = h23.projectionY()
    def h24 = dir.getObject(String.format('/dc/DC_residuals_trkDoca_%d_4',sec_num))
    def h14 = h24.projectionY()
    def h25 = dir.getObject(String.format('/dc/DC_residuals_trkDoca_%d_5',sec_num))
    def h15 = h25.projectionY()
    def h26 = dir.getObject(String.format('/dc/DC_residuals_trkDoca_%d_6',sec_num))
    def h16 = h26.projectionY()
    h11.add(h12)
    h11.add(h13)
    h11.add(h14)
    h11.add(h15)
    h11.add(h16)
    h11.setName("sec"+sec_num)
    h11.setTitle("DC residuals per sector")
    h11.setTitleX("DC residuals per sector (cm)")

    def f1 = DCFitter.fit(h11)

    //grtl[it].addPoint(run, h1.getDataX(h1.getMaximumBin()), 0, 0)
    // grtl[it].addPoint(run, h11.getMean(), 0, 0)
    grtl[it].addPoint(run, f1.getParameter(1), 0, 0)
    grtl2[it].addPoint(run, f1.getParameter(2), 0, 0)
    out.addDataSet(h11)
    out.addDataSet(f1)
    out2.addDataSet(h11)
    out2.addDataSet(f1)
  }
}


out.mkdir('/timelines')
out.cd('/timelines')
grtl.each{ out.addDataSet(it) }
out.writeFile('dc_residuals_sec_peak.hipo')

out2.mkdir('/timelines')
out2.cd('/timelines')
grtl2.each{ out2.addDataSet(it) }
out2.writeFile('dc_residuals_sec_sigma.hipo')
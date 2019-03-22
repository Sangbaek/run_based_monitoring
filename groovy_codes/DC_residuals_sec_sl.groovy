import org.jlab.groot.data.TDirectory
import org.jlab.groot.data.GraphErrors
// import ROOTFitter

def grtl1 = (1..6).collect{
  sec_num = it
  def gr1 = new GraphErrors('sec'+sec_num+' sl'+1)
  gr1.setTitle("DC residuals (peak value) per sector per superlayer")
  gr1.setTitleY("DC residuals (peak value) per sector per superlayer (cm)")
  gr1.setTitleX("run number")
  return gr1
}
def grtl2 = (1..6).collect{
  sec_num = it
  def gr2 = new GraphErrors('sec'+sec_num+' sl'+2)
  gr2.setTitle("DC residuals (peak value) per sector per superlayer")
  gr2.setTitleY("DC residuals (peak value) per sector per superlayer (cm)")
  gr2.setTitleX("run number")
  return gr2
}
def grtl3 = (1..6).collect{
  sec_num = it
  def gr3 = new GraphErrors('sec'+sec_num+' sl'+3)
  gr3.setTitle("DC residuals (peak value) per sector per superlayer")
  gr3.setTitleY("DC residuals (peak value) per sector per superlayer (cm)")
  gr3.setTitleX("run number")
  return gr3
}
def grtl4 = (1..6).collect{
  sec_num = it
  def gr4 = new GraphErrors('sec'+sec_num+' sl'+4)
  gr4.setTitle("DC residuals (peak value) per sector per superlayer")
  gr4.setTitleY("DC residuals (peak value) per sector per superlayer (cm)")
  gr4.setTitleX("run number")
  return gr4
}
def grtl5 = (1..6).collect{
  sec_num = it
  def gr5 = new GraphErrors('sec'+sec_num+' sl'+5)
  gr5.setTitle("DC residuals (peak value) per sector per superlayer")
  gr5.setTitleY("DC residuals (peak value) per sector per superlayer (cm)")
  gr5.setTitleX("run number")
  return gr5
}
def grtl6 = (1..6).collect{
  sec_num = it
  def gr6 = new GraphErrors('sec'+sec_num+' sl'+6)
  gr6.setTitle("DC residuals (peak value) per sector per superlayer")
  gr6.setTitleY("DC residuals (peak value) per sector per superlayer (cm)")
  gr6.setTitleX("run number")
  return gr6
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
    h11.setName("sec"+sec_num+"sl"+1)
    h11.setTitle("DC residuals per sector per superlayer")
    h11.setTitleX("DC residuals per sector per superlayer (cm)")
    h12.setName("sec"+sec_num+"sl"+2)
    h12.setTitle("DC residuals per sector per superlayer")
    h12.setTitleX("DC residuals per sector per superlayer (cm)")
    h13.setName("sec"+sec_num+"sl"+3)
    h13.setTitle("DC residuals per sector per superlayer")
    h13.setTitleX("DC residuals per sector per superlayer (cm)")
    h14.setName("sec"+sec_num+"sl"+4)
    h14.setTitle("DC residuals per sector per superlayer")
    h14.setTitleX("DC residuals per sector per superlayer (cm)")
    h15.setName("sec"+sec_num+"sl"+5)
    h15.setTitle("DC residuals per sector per superlayer")
    h15.setTitleX("DC residuals per sector per superlayer (cm)")
    h16.setName("sec"+sec_num+"sl"+6)
    h16.setTitle("DC residuals per sector per superlayer")
    h16.setTitleX("DC residuals per sector per superlayer (cm)")


    // def f1 = ROOTFitter.fit(h1)

    //grtl[it].addPoint(run, h1.getDataX(h1.getMaximumBin()), 0, 0)
    grtl1[it].addPoint(run, h11.getMean(), 0, 0)
    out.addDataSet(h11)
    grtl2[it].addPoint(run, h12.getMean(), 0, 0)
    out.addDataSet(h12)
    grtl3[it].addPoint(run, h13.getMean(), 0, 0)
    out.addDataSet(h13)
    grtl4[it].addPoint(run, h14.getMean(), 0, 0)
    out.addDataSet(h14)
    grtl5[it].addPoint(run, h15.getMean(), 0, 0)
    out.addDataSet(h15)
    grtl6[it].addPoint(run, h16.getMean(), 0, 0)
    out.addDataSet(h16)
    // out.addDataSet(f1)
  }
}


out.mkdir('/timelines')
out.cd('/timelines')
grtl1.each{ out.addDataSet(it) }
grtl2.each{ out.addDataSet(it) }
grtl3.each{ out.addDataSet(it) }
grtl4.each{ out.addDataSet(it) }
grtl5.each{ out.addDataSet(it) }
grtl6.each{ out.addDataSet(it) }
out.writeFile('DC_residuals_sec_sl.hipo')

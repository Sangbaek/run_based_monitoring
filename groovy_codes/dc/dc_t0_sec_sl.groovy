import org.jlab.groot.data.TDirectory
import org.jlab.groot.data.GraphErrors
import org.jlab.groot.data.H1F
import org.jlab.groot.group.DataGroup;
import org.jlab.groot.math.F1D;
import org.jlab.groot.fitter.DataFitter;
import org.jlab.groot.graphics.EmbeddedCanvas;
import fitter.DCFitter

def grtl1 = (1..6).collect{
  sec_num = it
  def gr1 = new GraphErrors('sec'+sec_num+' sl'+1)
  gr1.setTitle("t0 per sector per superlayer")
  gr1.setTitleY("t0 per sector per superlayer (ns)")
  gr1.setTitleX("run number")
  return gr1
}
def grtl2 = (1..6).collect{
  sec_num = it
  def gr2 = new GraphErrors('sec'+sec_num+' sl'+2)
  gr2.setTitle("t0 per sector per superlayer")
  gr2.setTitleY("t0 per sector per superlayer (ns)")
  gr2.setTitleX("run number")
  return gr2
}
def grtl3 = (1..6).collect{
  sec_num = it
  def gr3 = new GraphErrors('sec'+sec_num+' sl'+3)
  gr3.setTitle("t0 per sector per superlayer")
  gr3.setTitleY("t0 per sector per superlayer (ns)")
  gr3.setTitleX("run number")
  return gr3
}
def grtl4 = (1..6).collect{
  sec_num = it
  def gr4 = new GraphErrors('sec'+sec_num+' sl'+4)
  gr4.setTitle("t0 per sector per superlayer")
  gr4.setTitleY("t0 per sector per superlayer (ns)")
  gr4.setTitleX("run number")
  return gr4
}
def grtl5 = (1..6).collect{
  sec_num = it
  def gr5 = new GraphErrors('sec'+sec_num+' sl'+5)
  gr5.setTitle("t0 per sector per superlayer")
  gr5.setTitleY("t0 per sector per superlayer (ns)")
  gr5.setTitleX("run number")
  return gr5
}
def grtl6 = (1..6).collect{
  sec_num = it
  def gr6 = new GraphErrors('sec'+sec_num+' sl'+6)
  gr6.setTitle("t max per sector per superlayer")
  gr6.setTitleY("t max per sector per superlayer (ns)")
  gr6.setTitleX("run number")
  return gr6
}



TDirectory out = new TDirectory()

for(arg in args) {
  TDirectory dir = new TDirectory()
  dir.readFile(arg)

  def name = arg.split('/')[-1]
  def m = name =~ /\d{4,5}/
  def run = m[0].toInteger()

  out.mkdir('/'+run)
  out.cd('/'+run)

  (0..<6).each{
    sec_num = (it+1)
    def h11 = dir.getObject(String.format('/dc/DC_Time_%d_1',sec_num))
    def h12 = dir.getObject(String.format('/dc/DC_Time_%d_2',sec_num))
    def h13 = dir.getObject(String.format('/dc/DC_Time_%d_3',sec_num))
    def h14 = dir.getObject(String.format('/dc/DC_Time_%d_4',sec_num))
    def h15 = dir.getObject(String.format('/dc/DC_Time_%d_5',sec_num))
    def h16 = dir.getObject(String.format('/dc/DC_Time_%d_6',sec_num))
    h11.setName("sec"+sec_num+"sl"+1)
    h12.setName("sec"+sec_num+"sl"+2)
    h13.setName("sec"+sec_num+"sl"+3)
    h14.setName("sec"+sec_num+"sl"+4)
    h15.setName("sec"+sec_num+"sl"+5)
    h16.setName("sec"+sec_num+"sl"+6)

    def f11 = DCFitter.t0fit(h11, 1)
    def f12 = DCFitter.t0fit(h12, 2)
    def f13 = DCFitter.t0fit(h13, 3)
    def f14 = DCFitter.t0fit(h14, 4)
    def f15 = DCFitter.t0fit(h15, 5)
    def f16 = DCFitter.t0fit(h16, 6)

    //t_max = p2-(2/p1)
    grtl1[it].addPoint(run, f11.getParameter(2)-(2/f11.getParameter(1)), 0, 0)
    out.addDataSet(h11)
    out.addDataSet(f11)
    grtl2[it].addPoint(run, f12.getParameter(2)-(2/f12.getParameter(1)), 0, 0)
    out.addDataSet(h12)
    out.addDataSet(f12)
    grtl3[it].addPoint(run, f13.getParameter(2)-(2/f13.getParameter(1)), 0, 0)
    out.addDataSet(h13)
    out.addDataSet(f13)
    grtl4[it].addPoint(run, f14.getParameter(2)-(2/f14.getParameter(1)), 0, 0)
    out.addDataSet(h14)
    out.addDataSet(f14)
    grtl5[it].addPoint(run, f15.getParameter(2)-(2/f15.getParameter(1)), 0, 0)
    out.addDataSet(h15)
    out.addDataSet(f15)
    grtl6[it].addPoint(run, f16.getParameter(2)-(2/f16.getParameter(1)), 0, 0)
    out.addDataSet(h16)
    out.addDataSet(f16)
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
out.writeFile('dc_t0_sec_sl.hipo')

public void initInvertedSFitPar(int slayer, F1D function, H1F histo) {
  // double min = 0;
  // double max = 220.0;

  test = 0.05
  cut_test = test* histo.getMax()
  T_test=-10000
  test2 = 0.85
  cut_test2 = test2* histo.getMax()
  cut10=0.1*histo.getMax()
  cut90=0.9*histo.getMax()
  for(int bin =0; bin < histo.getMaximumBin(); bin++){
      if (histo.getBinContent(bin)<cut_test && histo.getBinContent(bin+1)>cut_test) T_test=histo.getDataX(bin)
      if (histo.getBinContent(bin)<cut_test2 && histo.getBinContent(bin+1)>cut_test2) T_test2=histo.getDataX(bin+1)
      if (histo.getBinContent(bin)<cut10 && histo.getBinContent(bin+1)>cut10) T10=histo.getDataX(bin)
      if (histo.getBinContent(bin)<cut90 && histo.getBinContent(bin+1)>cut90) T90=histo.getDataX(bin+1)
  }
  if (T_test==-10000) T_test=T10
  P0 = histo.getMax()
  P1= 4/(T90-T10)
  P2 = (T10+T90)/2
  if (slayer == 1) {
    // min = 100.0; max = 240.0;
    function.setParameter(0,P0); function.setParLimits(1,2*cut10,2*cut90);
    function.setParameter(1,P1); function.setParLimits(1,P1*0.1,P1*2);
    function.setParameter(2,P2); function.setParLimits(2,T10,T90);
  }
  if (slayer == 2) {
    // min = 120.0; max = 240.0;
    function.setParameter(0,P0); function.setParLimits(1,2*cut10,2*cut90);
    function.setParameter(1,P1); function.setParLimits(1,P1*0.1,P1*2);
    function.setParameter(2,P2); function.setParLimits(2,T10,T90);
  }
  if (slayer == 3) {
    // min = 200.0; max = 450.0;
    function.setParameter(0,P0); function.setParLimits(1,2*cut10,2*cut90);
    function.setParameter(1,P1);function.setParLimits(1,P1*0.1,P1*2);
    function.setParameter(2,P2); function.setParLimits(2,T10-50,T90+50);
  }
  if (slayer == 4) {
    // min = 200.0; max = 500.0;
    function.setParameter(0,P0); function.setParLimits(1,2*cut_test,2*cut90);
    function.setParameter(1,P1); function.setParLimits(1,P1*0.1,P1*2);
    function.setParameter(2,P2); function.setParLimits(2,T10-50,T90+50);
  }
  if (slayer == 5) {
    // min = 400.0; max = 700.0;
    function.setParameter(0,P0); function.setParLimits(1,2*cut10,2*cut90);
    function.setParameter(1,P1);function.setParLimits(1,P1*0.1,P1*2);
    function.setParameter(2,P2); function.setParLimits(2,T10-50,T90+50);
  }
  if (slayer == 6) {
    // min = 480.0; max = 700.0;
    function.setParameter(0,P0); function.setParLimits(1,2*cut10,2*cut90);
    function.setParameter(1,P1); function.setParLimits(1,P1*0.1,P1*2);
    function.setParameter(2,P2); function.setParLimits(2,T10-50,T90+50);
  }
  function.setRange(T_test,T_test2);
  function.setLineColor(2);
  function.setLineWidth(4);
}

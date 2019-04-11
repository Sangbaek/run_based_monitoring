import org.jlab.groot.data.TDirectory
import org.jlab.groot.data.GraphErrors
import org.jlab.groot.data.H1F
import org.jlab.groot.group.DataGroup;
import org.jlab.groot.math.F1D;
import org.jlab.groot.fitter.DataFitter;
import org.jlab.groot.graphics.EmbeddedCanvas;
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
  def f1 = new F1D("f1", "[amp]*gaus(x,[mean],[sigma])", -10.0, 10.0);
  f1.setParameter(0, 0.0);
  f1.setParameter(1, 0.0);
  f1.setParameter(2, 2.0);
  f1.setLineWidth(2);
  f1.setOptStat("1111");
  initTimeGaussFitPar(f1,h1);
  DataFitter.fit(f1,h1,"LQ");

  def f2 = new F1D("f2", "[amp]*gaus(x,[mean],[sigma])", -10.0, 10.0);
  f2.setParameter(0, 0.0);
  f2.setParameter(1, 0.0);
  f2.setParameter(2, 2.0);
  f2.setLineWidth(2);
  f2.setOptStat("1111");
  initTimeGaussFitPar(f2,h1);
  DataFitter.fit(f2,h1,"L");

  grtl1.addPoint(run, f1.getParameter(1), 0, 0)
  grtl2.addPoint(run, f1.getParameter(2), 0, 0)
  grtl3.addPoint(run, f2.getParameter(1), 0, 0)
  grtl4.addPoint(run, f2.getParameter(2), 0, 0)
  out.addDataSet(h1)
  out.addDataSet(h2)
  out.addDataSet(f1)
  out.addDataSet(f2)

}


out.mkdir('/timelines')
out.cd('/timelines')
grtl1.each{ out.addDataSet(it) }
grtl2.each{ out.addDataSet(it) }
grtl3.each{ out.addDataSet(it) }
grtl4.each{ out.addDataSet(it) }
out.writeFile('FTH_MIPS_time.hipo')

private void initTimeGaussFitPar(F1D ftime, H1F htime) {
        double hAmp  = htime.getBinContent(htime.getMaximumBin());
        double hMean = htime.getAxis().getBinCenter(htime.getMaximumBin());
        double hRMS  = htime.getRMS(); //ns
        double rangeMin = (hMean - (3*hRMS));
        double rangeMax = (hMean + (3*hRMS));
        double pm = hRMS*3;
        ftime.setRange(rangeMin, rangeMax);
        ftime.setParameter(0, hAmp);
        ftime.setParLimits(0, hAmp*0.8, hAmp*1.2);
        ftime.setParameter(1, hMean);
        ftime.setParLimits(1, hMean-pm, hMean+(pm));
        ftime.setParameter(2, 0.2);
        ftime.setParLimits(2, 0.1*hRMS, 0.8*hRMS);
}

import org.jlab.groot.data.TDirectory
import org.jlab.groot.data.GraphErrors
import org.jlab.groot.data.H1F
import org.jlab.groot.group.DataGroup;
import org.jlab.groot.math.F1D;
import org.jlab.groot.fitter.DataFitter;
import org.jlab.groot.graphics.EmbeddedCanvas;

  def grtl = new GraphErrors('Mean')
  grtl.setTitle("FTC time - start time, charged")
  grtl.setTitleY("FTC time - start time, charged (ns)")
  grtl.setTitleX("run number")

  def grtl2 = new GraphErrors('Sigma')
  grtl.setTitle("FTC time - start time, charged")
  grtl.setTitleY("FTC time - start time, charged (ns)")
  grtl.setTitleX("run number")


TDirectory out = new TDirectory()
for(arg in args) {
  TDirectory dir = new TDirectory()
  dir.readFile(arg)

  def name = arg.split('/')[-1]
  def m = name =~ /\d\d\d\d/
  def run = m[0].toInteger()

  out.mkdir('/'+run)
  out.cd('/'+run)

  def h1 = dir.getObject('/ft/hi_cal_time_cut_ch')
  // def f1 = ROOTFitter.fit(h1)
  def ftime_ch = new F1D("ftime_ch", "[amp]*gaus(x,[mean],[sigma])", -1.0, 1.0);
  ftime_ch.setParameter(0, 0.0);
  ftime_ch.setParameter(1, 0.0);
  ftime_ch.setParameter(2, 2.0);
  ftime_ch.setLineWidth(2);
  ftime_ch.setOptStat("1111");
  initTimeGaussFitPar(ftime_ch,h1);
  DataFitter.fit(ftime_ch,h1,"L");

  //grtl[it].addPoint(run, h1.getDataX(h1.getMaximumBin()), 0, 0)
  grtl.addPoint(run, ftime_ch.getParameter(1), 0, 0)
  grtl2.addPoint(run, ftime_ch.getParameter(2), 0, 0)
  // grtl.addPoint(run, h1.getMean(), 0, 0)
  // grtl2.addPoint(run, h1.getRMS(), 0, 0)
  out.addDataSet(h1)
  // out.addDataSet(ftime_ch)

}


out.mkdir('/timelines')
out.cd('/timelines')
grtl.each{ out.addDataSet(it) }
grtl2.each{ out.addDataSet(it) }
out.writeFile('FTC_time_ch.hipo')


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

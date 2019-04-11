import org.jlab.groot.data.TDirectory
import org.jlab.groot.data.GraphErrors
import org.jlab.groot.group.DataGroup;
import org.jlab.groot.math.F1D;
import org.jlab.groot.fitter.DataFitter;
import org.jlab.groot.graphics.EmbeddedCanvas;

// import ROOTFitter

  def grtl = new GraphErrors('Mean')
  grtl.setTitle("FTC pi0 mass")
  grtl.setTitleY("FTC pi0 mass (MeV)")
  grtl.setTitleX("run number")

  def grtl2 = new GraphErrors('Sigma')
  grtl.setTitle("FTC pi0 mass")
  grtl.setTitleY("FTC pi0 mass (MeV)")
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

  def h1 = dir.getObject('/ft/hpi0sum')
  def fpi0 = new F1D("fpi0", "[amp]*gaus(x,[mean],[sigma])+[p0]+[p1]*x", 80 ,200 );
  fpi0.setParameter(0, 0.0);
  fpi0.setParameter(1, 140.0);
  fpi0.setParameter(2, 2.0);
  fpi0.setParameter(3, 0.0);
  fpi0.setParameter(4, 0.0);
  fpi0.setLineWidth(2);
  fpi0.setOptStat("1111111");
  double hAmp  = h1.getBinContent(h1.getMaximumBin());
  double hMean = h1.getAxis().getBinCenter(h1.getMaximumBin());
  double hRMS  = 10; //ns
  fpi0.setParameter(0, hAmp);
  fpi0.setParLimits(0, hAmp*0.8, hAmp*1.2);
  fpi0.setParameter(1, hMean);
  fpi0.setParLimits(1, hMean-hRMS, hMean+hRMS);
  DataFitter.fit(fpi0,h1,"LQ");
  h1.setFunction(null);

  // h1.add(h2)
  // def f1 = ROOTFitter.fit(h1)

  //grtl[it].addPoint(run, h1.getDataX(h1.getMaximumBin()), 0, 0)
  grtl.addPoint(run, fpi0.getParameter(1), 0, 0)
  grtl2.addPoint(run, fpi0.getParameter(2), 0, 0)
  out.addDataSet(h1)
  out.addDataSet(fpi0)

}


out.mkdir('/timelines')
out.cd('/timelines')
grtl.each{ out.addDataSet(it) }
grtl2.each{ out.addDataSet(it) }
out.writeFile('FTC_pi0_mass.hipo')

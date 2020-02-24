import org.jlab.groot.data.TDirectory
import org.jlab.groot.data.GraphErrors
import org.jlab.groot.data.H1F
import org.jlab.groot.group.DataGroup;
import org.jlab.groot.math.F1D;
import org.jlab.groot.fitter.DataFitter;
import org.jlab.groot.graphics.EmbeddedCanvas;

def grtl = (1..3).collect{
  def gr = new GraphErrors('layer'+it+' Mean')
  gr.setTitle("MIPS dE/dz")
  gr.setTitleY("MIPS dE/dz (GeV/cm)")
  gr.setTitleX("run number")
  return gr
}

def grtl2 = (1..3).collect{
  def gr2 = new GraphErrors('layer'+it+' Sigma')
  gr2.setTitle("MIPS dE/dz")
  gr2.setTitleY("MIPS dE/dz (GeV/cm)")
  gr2.setTitleX("run number")
  return gr2
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

  (0..<3).each{
      def h1 = dir.getObject(String.format("/cnd/CND_alignE_L%d_S%d_C%d",it+1,0+1,0+1))
      for(int sector=0;sector<24;sector++){
        for(int comp=0;comp<2;comp++){
               if(sector!=0||comp!=0){
                 def h2 = dir.getObject(String.format("/cnd/CND_alignE_L%d_S%d_C%d",it+1,sector+1,comp+1))
                 h1.add(h2)
               }
            }
        }
    h1.setName("layer"+(it+1));
    h1.setTitle("dE/dz (GeV/cm)")
    double maxE = h1.getBinContent(h1.getMaximumBin());
    f1=new F1D("fit:"+h1.getName(),"[amp]*landau(x,[mean],[sigma])+[p0]+[p1]*x", 1.5, 3.5);
    // initLandauFitPar(h1, f1)
    // f1.setLineColor(33);
    // f1.setLineWidth(10);
    // f1.setRange(1.5,5);
    f1.setParameter(1,2.0);
    f1.setParameter(0,maxE);
    f1.setParLimits(0,maxE*0.9,maxE*1.1);
    f1.setParameter(2,1.0);
    f1.setParameter(3,0.0);
    DataFitter.fit(f1,h1,"LQ")
    // recursive_Gaussian_fitting(f1,h1)

    //grtl[it].addPoint(run, h1.getDataX(h1.getMaximumBin()), 0, 0)
    grtl[it].addPoint(run, f1.getParameter(1), 0, 0)
    grtl2[it].addPoint(run, f1.getParameter(2), 0, 0)

    out.addDataSet(h1)
    out.addDataSet(f1)
    out2.addDataSet(h1)
    out2.addDataSet(f1)


  }
}


out.mkdir('/timelines')
out.cd('/timelines')
grtl.each{ out.addDataSet(it) }
out.writeFile('cnd_dEdz_mean.hipo')
out2.mkdir('/timelines')
out2.cd('/timelines')
grtl2.each{ out2.addDataSet(it) }
out2.writeFile('cnd_dEdz_sigma.hipo')

private void initLandauFitPar(H1F hcharge, F1D fcharge) {
        double hAmp  = hcharge.getBinContent(hcharge.getMaximumBin());
        double hMean = hcharge.getAxis().getBinCenter(hcharge.getMaximumBin());
        double hRMS  = hcharge.getRMS(); //ns
        fcharge.setRange(0.5*hMean, 1.5* hMean);
        fcharge.setParameter(0, hAmp);
        fcharge.setParLimits(0, 0.8*hAmp, 1.2*hAmp);
        fcharge.setParameter(1, hMean);
        fcharge.setParLimits(1, 0.8*hMean, 1.2*hMean);//Changed from 5-30
        fcharge.setParameter(2, 1.);//Changed from 2
        fcharge.setParLimits(2, 0.1, 2);//Changed from 0.5-10
        fcharge.setParameter(3, 0.);
}

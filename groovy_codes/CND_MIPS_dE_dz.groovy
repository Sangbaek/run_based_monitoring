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

for(arg in args) {
  TDirectory dir = new TDirectory()
  dir.readFile(arg)

  def name = arg.split('/')[-1]
  def m = name =~ /\d\d\d\d/
  def run = m[0].toInteger()

  out.mkdir('/'+run)
  out.cd('/'+run)

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
    // def f1 = ROOTFitter.fit(h1)
    f1=new F1D("E resolution layer"+(it+1),"[amp]*gaus(x,[mean],[sigma])+[cst]+[a]*x", 0.0, 6.0);
    f1.setLineColor(33);
    f1.setLineWidth(10);
    f1.setRange(1.5,5);
    f1.setParameter(1,2.0);
    f1.setParameter(0,maxE);
    f1.setParLimits(0,maxE*0.9,maxE*1.1);
    f1.setParameter(2,1.0);
    f1.setParameter(3,0.0);
    f1.setParameter(4,0.0);
    f1.setName("fit layer"+(it+1))
    DataFitter.fit(f1,h1,"LQ")

    // def f1 = ROOTFitter.fit(h1)

    //grtl[it].addPoint(run, h1.getDataX(h1.getMaximumBin()), 0, 0)
    grtl[it].addPoint(run, f1.getParameter(1), 0, 0)
    grtl2[it].addPoint(run, f1.getParameter(2), 0, 0)

    out.addDataSet(h1)
    out.addDataSet(f1)


  }
}


out.mkdir('/timelines')
out.cd('/timelines')
grtl.each{ out.addDataSet(it) }
grtl2.each{ out.addDataSet(it) }
out.writeFile('CND_dEdz.hipo')

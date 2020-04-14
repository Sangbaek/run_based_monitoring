import org.jlab.groot.data.TDirectory
import org.jlab.groot.data.GraphErrors
import org.jlab.groot.data.H1F
import org.jlab.groot.group.DataGroup;
import org.jlab.groot.math.F1D;
import org.jlab.groot.fitter.DataFitter;
import org.jlab.groot.graphics.EmbeddedCanvas;
import fitter.CNDFitter

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
    f1 = CNDFitter.MIPSfit(h1)
    // recursive_Gaussian_fitting(f1,h1)

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
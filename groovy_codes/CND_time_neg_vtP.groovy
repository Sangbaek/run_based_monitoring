import org.jlab.groot.data.TDirectory
import org.jlab.groot.data.GraphErrors
import org.jlab.groot.data.H1F
import org.jlab.groot.group.DataGroup;
import org.jlab.groot.math.F1D;
import org.jlab.groot.fitter.DataFitter;
import org.jlab.groot.graphics.EmbeddedCanvas;

def grtl = (1..3).collect{
  def gr = new GraphErrors('layer'+it + ' Mean')
  gr.setTitle("CND time per layer")
  gr.setTitleY("CND time per layer (ns)")
  gr.setTitleX("run number")
  return gr
}

def grtl2 = (1..3).collect{
  def gr2 = new GraphErrors('layer'+it+' Sigma')
  gr2.setTitle("CND time per layer")
  gr2.setTitleY("CND time per layer (ns)")
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
    iL=it+1
    def h2 = dir.getObject(String.format("/cnd/H_CND_time_z_charged_L%d",iL))
    def h1 = h2.projectionY()
    h1.setName("negative, layer"+iL)
    h1.setTitle("CND vtP")
    h1.setTitleX("CND vtP (ns)")

    def f1 =new F1D("t resolution","[amp]*gaus(x,[mean],[sigma])", -1.0, 1.0);
    f1.setLineColor(33);
    f1.setLineWidth(10);
    f1.setName("fit layer"+iL)
    f1.setOptStat("1111");
    double maxz = h1.getBinContent(h1.getMaximumBin());
    fitz[(comp*3)+layer+(sector*6)].setRange(-5,5);
    fitz[(comp*3)+layer+(sector*6)].setParameter(1,0.0);
    fitz[(comp*3)+layer+(sector*6)].setParameter(0,maxz);
    fitz[(comp*3)+layer+(sector*6)].setParLimits(0,maxz*0.9,maxz*1.1);
    fitz[(comp*3)+layer+(sector*6)].setParameter(2,3.0);
    fitz[(comp*3)+layer+(sector*6)].setParameter(3,10.0);
    DataFitter.fit(f1, h1, "");

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
out.writeFile('CND_time_neg_vtP.hipo')

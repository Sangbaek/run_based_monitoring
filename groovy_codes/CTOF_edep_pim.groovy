import org.jlab.groot.data.TDirectory
import org.jlab.groot.data.GraphErrors
import org.jlab.groot.data.H1F
import org.jlab.groot.group.DataGroup;
import org.jlab.groot.math.F1D;
import org.jlab.groot.fitter.DataFitter;
import org.jlab.groot.graphics.EmbeddedCanvas;

def grtl = new GraphErrors('Mean')
grtl.setTitle("CTOF Edep, #pi^-")
grtl.setTitleY("CTOF Edep, #pi^- (MeV)")
grtl.setTitleX("run number")

def grtl2 = new GraphErrors('Sigma')
grtl2.setTitle("CTOF Edep, #pi^-")
grtl2.setTitleY("CTOF Edep, #pi^- (MeV)")
grtl2.setTitleX("run number")

TDirectory out = new TDirectory()

for(arg in args) {
  TDirectory dir = new TDirectory()
  dir.readFile(arg)

  def name = arg.split('/')[-1]
  def m = name =~ /\d\d\d\d/
  def run = m[0].toInteger()
  def h2 = dir.getObject('/ctof/H_CTOF_edep_pim')
  def h1 = h2.projectionX()
  h1.setTitle(h2.getTitle());
  h1.setTitleX(h2.getTitleX());

  grtl.addPoint(run, h1.getMean(), 0, 0)
  grtl2.addPoint(run, h1.getRMS(), 0, 0)
  out.mkdir('/'+run)
  out.cd('/'+run)
  out.addDataSet(h1)
}


out.mkdir('/timelines')
out.cd('/timelines')
grtl.each{ out.addDataSet(it) }
grtl2.each{ out.addDataSet(it) }
out.writeFile('CTOF_edep_pim.hipo')

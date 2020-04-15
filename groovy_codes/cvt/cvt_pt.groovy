import org.jlab.groot.data.TDirectory
import org.jlab.groot.data.GraphErrors

def data = []

for(arg in args) {
  TDirectory dir = new TDirectory()
  dir.readFile(arg)

  def name = arg.split('/')[-1]
  def m = name =~ /\d{4,5}/
  def run = m[0].toInteger()

  def h1 = dir.getObject('/cvt/hpt')
  h1.setTitle("CVT track transverse momentum");
  h1.setTitleX("CVT track transverse momentum (GeV/c)");

  data.add([run:run, h1:h1])
}

TDirectory out = new TDirectory()

def grtl = new GraphErrors('CVT transverse momentum')
grtl.setTitle("CVT transverse momentum")
grtl.setTitleY("CVT transverse momentum (GeV/c)")
grtl.setTitleX("run number")

data.each{
  out.mkdir('/'+it.run)
  out.cd('/'+it.run)
  out.addDataSet(it.h1)
  grtl.addPoint(it.run, it.h1.getDataX(it.h1.getMaximumBin()), 0, 0)
}

out.mkdir('/timelines')
out.cd('/timelines')
grtl.each{ out.addDataSet(it) }
out.writeFile('cvt_pt.hipo')
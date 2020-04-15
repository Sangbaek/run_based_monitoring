import org.jlab.groot.data.TDirectory
import org.jlab.groot.data.GraphErrors

def data = []

for(arg in args) {
  TDirectory dir = new TDirectory()
  dir.readFile(arg)

  def name = arg.split('/')[-1]
  def m = name =~ /\d{4,5}/
  def run = m[0].toInteger()

  def h1 = dir.getObject('/cvt/H_CVT_z_neg')

  data.add([run:run, h1:h1])
}

TDirectory out = new TDirectory()

def grtl = new GraphErrors('cvt_z_neg')
grtl.setTitle("VZ (Average), negatives")
grtl.setTitleY("VZ (Average), negatives (cm)")
grtl.setTitleX("run number")

data.each{
  out.mkdir('/'+it.run)
  out.cd('/'+it.run)
  out.addDataSet(it.h1)
  grtl.addPoint(it.run, it.h1.getMean(), 0, 0)
}

out.mkdir('/timelines')
out.cd('/timelines')
grtl.each{ out.addDataSet(it) }
out.writeFile('cvt_Vz_neg.hipo')
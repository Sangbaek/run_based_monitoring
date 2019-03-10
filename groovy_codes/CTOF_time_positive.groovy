import org.jlab.groot.data.TDirectory
import org.jlab.groot.data.GraphErrors
import ROOTFitter

def grtl = new GraphErrors('ctof_time')
grtl.setTitle("peak CTOF time, positive")
grtl.setTitleY("peak CTOF time, positive")
grtl.setTitleX("run number")

TDirectory out = new TDirectory()

for(arg in args) {
  TDirectory dir = new TDirectory()
  dir.readFile(arg)

  def name = arg.split('/')[-1]
  def m = name =~ /\d\d\d\d/
  def run = m[0].toInteger()

  def h1 = dir.getObject('/ctof/H_CVT_t_pos')

  def f1 = ROOTFitter.fit(h1)

  //grtl[it].addPoint(run, h1.getDataX(h1.getMaximumBin()), 0, 0)
  grtl[it].addPoint(run, f1.getParameter(1), 0, 0)
  out.addDataSet(h1)
  out.addDataSet(f1)

  out.mkdir('/'+run)
  out.cd('/'+run)
  out.addDataSet(h1)
}


out.mkdir('/timelines')
out.cd('/timelines')
grtl.each{ out.addDataSet(it) }
out.writeFile('out_CTOF_time_pos.hipo')

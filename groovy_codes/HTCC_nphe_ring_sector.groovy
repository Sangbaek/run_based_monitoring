import org.jlab.groot.data.TDirectory
import org.jlab.groot.data.GraphErrors
// import ROOTFitter

def grtl = (1..24).collect{
  def gr = new GraphErrors('sec'+it)
  gr.setTitle("HTCC Number of Photoelectrons")
  gr.setTitleY("HTCC Number of Photoelectrons per sector per ring")
  gr.setTitleX("run number")
  return gr
}

TDirectory out = new TDirectory()

for(arg in args.drop(1)) {
  TDirectory dir = new TDirectory()
  dir.readFile(arg)

  def name = arg.split('/')[-1]
  def m = name =~ /\d\d\d\d/
  def run = m[0].toInteger()

  out.mkdir('/'+run)
  out.cd('/'+run)

for (s = 0; i <6; s++) {
  for (r = 0; r <4; r++) {

    int counter = r + 4*s ;

    def h1 = dir.getObject(String.format('/HTCC/H_HTCC_nphe_s%d_r%d_side0',s,r)) //left
    def h2 = dir.getObject(String.format('/HTCC/H_HTCC_nphe_s%d_r%d_side1',s,r)) //right
    h1.Add(h2)
    h1.setName("sec"+s +"ring"+r)
    h1.setTitle("HTCC Number of Photoelectrons")
    h1.setTitleX("HTCC Number of Photoelectrons")

    // def f1 = ROOTFitter.fit(h1)

    // grtl[it].addPoint(run, h1.getDataX(h1.getMaximumBin()), 0, 0)
    // grtl[counter].addPoint(run, f1.getParameter(1), 0, 0)
    grtl[counter].addPoint(run, h1.getMean(), 0, 0)
    grtl[counter].addPoint(run, h1.getStdDev(), 0, 0)
    out.addDataSet(h1)
    // out.addDataSet(f1)
  }
}


out.mkdir('/timelines')
out.cd('/timelines')
grtl.each{ out.addDataSet(it) }
out.writeFile('HTCC_nphe_sector_ring.hipo')

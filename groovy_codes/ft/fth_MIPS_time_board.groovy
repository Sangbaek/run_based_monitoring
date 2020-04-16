import org.jlab.groot.data.TDirectory
import org.jlab.groot.data.GraphErrors
import fitter.FTFitter

def grtl = (1..30).collect{
  board=(it-1)%15+1
  layer=(it-1).intdiv(15)+1
  def gr = new GraphErrors('layer'+layer+'board'+board)
  gr.setTitle("FTH MIPS time per layer (peak value)")
  gr.setTitleY("FTH MIPS time per layer (peak value) (ns)")
  gr.setTitleX("run number")
  return gr
}

def grtl2 = (1..30).collect{
  board=(it-1)%15+1
  layer=(it-1).intdiv(15)+1
  def gr2 = new GraphErrors('layer'+layer+'board'+board)
  gr2.setTitle("FTH MIPS time per layer (sigma)")
  gr2.setTitleY("FTH MIPS time per layer (sigma) (ns)")
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
  for (l = 0; l <2; l++) {
    for (b = 0; b <15; b++) {
    counter=l*15+b
    layer = l+1
    board = b+1
    def h1 = dir.getObject('/ft/hi_hodo_tmatch_l'+(layer)+'_b'+(board))
    def f1 = FTFitter.fthtimefit(h1)

    grtl[counter].addPoint(run, f1.getParameter(1), 0, 0)
    grtl2[counter].addPoint(run, f1.getParameter(2), 0, 0)
    out.addDataSet(h1)
    out.addDataSet(f1)
    out2.addDataSet(h1)
    out2.addDataSet(f1)
  }
}
}
out.mkdir('/timelines')
out.cd('/timelines')
out2.mkdir('/timelines')
out2.cd('/timelines')
grtl.each{ out.addDataSet(it) }
grtl2.each{ out2.addDataSet(it) }
out.writeFile('fth_MIPS_time_board_mean.hipo')
out2.writeFile('fth_MIPS_time_board_sigma.hipo')
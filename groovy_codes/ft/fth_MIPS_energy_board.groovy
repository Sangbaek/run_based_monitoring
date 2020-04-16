import org.jlab.groot.data.TDirectory
import org.jlab.groot.data.GraphErrors
import fitter.FTFitter

def grtl = (1..30).collect{
  board=(it-1)%15+1
  layer=(it-1).intdiv(15)+1
  def gr = new GraphErrors('layer'+layer+'board'+board)
  gr.setTitle("FTH MIPS energy per layer per board (peak value)")
  gr.setTitleY("FTH MIPS energy per layer per board (peak value) (MeV)")
  gr.setTitleX("run number")
  return gr
}

TDirectory out = new TDirectory()

for(arg in args) {
  TDirectory dir = new TDirectory()
  dir.readFile(arg)

  def name = arg.split('/')[-1]
  def m = name =~ /\d{4,5}/
  def run = m[0].toInteger()

  out.mkdir('/'+run)
  out.cd('/'+run)

for (l = 0; l <2; l++) {
  for (b = 0; b <15; b++) {
    counter=l*15+b
    layer = l+1
    board = b+1
    def h1 = dir.getObject('/ft/hi_hodo_ematch_l'+(layer)+'_b'+(board))
    def f_charge_landau = FTFitter.fthedepfit(h1, l)
    grtl[counter].addPoint(run, f_charge_landau.getParameter(1), 0, 0)
    out.addDataSet(h1)
    out.addDataSet(f_charge_landau)
  }
}
}


out.mkdir('/timelines')
out.cd('/timelines')
grtl.each{ out.addDataSet(it) }
out.writeFile('fth_MIPS_energy_board.hipo')
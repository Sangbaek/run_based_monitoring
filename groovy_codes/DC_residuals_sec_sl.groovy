import org.jlab.groot.data.TDirectory
import org.jlab.groot.data.GraphErrors
// import ROOTFitter

def grtl = (1..36).collect{
  sec_num = (it-1).intdiv(6)+1
  sl_num = it%6
  if (sl_num==0) sl_num=6
  def gr = new GraphErrors('sec'+sec_num +'sl'+sl_num)
  gr.setTitle("DC residuals (peak value) per sector per superlayer")
  gr.setTitleY("DC residuals (peak value) per sector per superlayer")
  gr.setTitleX("run number")
  return gr
}

def grtl2 = (1..36).collect{
  sec_num = (it-1).intdiv(6)
  sl_num = it%6
  if (sl_num==0) sl_num=6
  def gr2 = new GraphErrors('sec'+sec_num +'sl'+sl_num+'sigma')
  gr2.setTitle("DC residuals (peak value) per sector per superlayer")
  gr2.setTitleY("DC residuals (peak value) per sector per superlayer")
  gr2.setTitleX("run number")
  return gr2
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

//   (0..<36).each{
//     sec_num = it.intdiv(6)+1
//     sl_num = (it+1)%6
//     if (sl_num==0) sl_num=6
//     def h2 = dir.getObject(String.format('/dc/DC_residuals_trkDoca_%d_%d',sec_num,sl_num))
//     def h1 = h2.projectionY()
//     h1.setName("sec"+sec_num+"sl"+sl_num)
//     h1.setTitle("DC residuals per sector per superlayer")
//     h1.setTitleX("DC residuals per sector per superlayer")
//
//     // def f1 = ROOTFitter.fit(h1)
//
//     //grtl[it].addPoint(run, h1.getDataX(h1.getMaximumBin()), 0, 0)
//     // grtl[it].addPoint(run, f1.getParameter(1)), 0, 0)
//     // grtl2[it].addPoint(run, f1.getParameter(2)), 0, 0)
//     grtl[it].addPoint(run, h1.getMean(), 0, 0)
//     grtl2[it].addPoint(run, h1.getRMS(), 0, 0)
//     out.addDataSet(h1)
//     // out.addDataSet(f1)
//   }
// }

(0..<6).each{
  sec_num = (it+1)
  def h21 = dir.getObject(String.format('/dc/DC_residuals_trkDoca_%d_1',sec_num))
  def h11 = h21.projectionY()
  def h22 = dir.getObject(String.format('/dc/DC_residuals_trkDoca_%d_2',sec_num))
  def h12 = h22.projectionY()
  def h23 = dir.getObject(String.format('/dc/DC_residuals_trkDoca_%d_3',sec_num))
  def h13 = h23.projectionY()
  def h24 = dir.getObject(String.format('/dc/DC_residuals_trkDoca_%d_4',sec_num))
  def h14 = h24.projectionY()
  def h25 = dir.getObject(String.format('/dc/DC_residuals_trkDoca_%d_5',sec_num))
  def h15 = h25.projectionY()
  def h26 = dir.getObject(String.format('/dc/DC_residuals_trkDoca_%d_6',sec_num))
  def h16 = h26.projectionY()
  h11.setName("sec"+sec_num+"sl"+1)
  h11.setTitle("DC residuals per sector per superlayer")
  h11.setTitleX("DC residuals per sector per superlayer")

  // def f1 = ROOTFitter.fit(h1)

  //grtl[it].addPoint(run, h1.getDataX(h1.getMaximumBin()), 0, 0)
  grtl[it].addPoint(run, h11.getMean(), 0, 0)
  out.addDataSet(h11)
  // out.addDataSet(f1)
}
}


out.mkdir('/timelines')
out.cd('/timelines')
grtl.each{ out.addDataSet(it) }
grtl2.each{ out.addDataSet(it) }
out.writeFile('out_DC_residuals_sec_sl.hipo')

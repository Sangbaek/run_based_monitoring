import org.jlab.groot.data.TDirectory
import org.jlab.groot.data.GraphErrors
import fitter.FTFitter

data = []

for(arg in args) {
  TDirectory dir = new TDirectory()
  dir.readFile(arg)

  def name = arg.split('/')[-1]
  def m = name =~ /\d{4,5}/
  def run = m[0].toInteger()

  def funclist = [[],[]]
  def meanlist = [[],[]]
  def sigmalist = [[],[]]
  def chi2list = [[],[]]

  def histlist = [[], []].withIndex().collect{list, layer ->
    (0..<15).each{board ->
      def hist = dir.getObject('/ft/hi_hodo_tmatch_l'+(layer+1)+'_b'+(board+1))
      funclist[layer].add(FTFitter.fthtimefit(hist))
      meanlist[layer].add(funclist[layer][board].getParameter(1))
      sigmalist[layer].add(funclist[layer][board].getParameter(2).abs())
      chi2list[layer].add(funclist[layer][board].getChiSquare())
      list.add(hist)
    }
    return list
  }

  data.add([run:run, hlist:histlist, flist:funclist, mean:meanlist, sigma:sigmalist, clist:chi2list])
}

['mean', 'sigma'].each{name->
  TDirectory out = new TDirectory()
  out.mkdir('/timelines')
  ['layer1','layer2'].eachWithIndex{layer, lindex ->
    (1..15).each{board->
      def grtl = new GraphErrors(layer+'board'+board)
      grtl.setTitle("FTH MIPS time per layer (" + name + ")")
      grtl.setTitleY("FTH MIPS time per layer (" + name + ") (ns)")
      grtl.setTitleX("run number")

      data.each{
        out.mkdir('/'+it.run)
        out.cd('/'+it.run)
        grtl.addPoint(it.run, it.mean[lindex][board-1], 0, 0)
        out.addDataSet(it.hlist[lindex][board-1])
        out.addDataSet(it.flist[lindex][board-1])
      }
      out.cd('/timelines')
      out.addDataSet(grtl)
    }
  }

  out.writeFile('fth_MIPS_time_board_' + name + '.hipo')
}
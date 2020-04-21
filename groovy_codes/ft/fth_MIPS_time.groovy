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

  def funclist = []
  def meanlist = []
  def sigmalist = []
  def chi2list = []

  def histlist = [def h1, def h2].withIndex().collect{hist, it ->
    hist = dir.getObject('/ft/hi_hodo_tmatch_l'+(it+1))
    funclist.add(FTFitter.fthtimefit(hist))
    meanlist.add(funclist[it].getParameter(1))
    sigmalist.add(funclist[it].getParameter(2).abs())
    chi2list.add(funclist[it].getChiSquare())
    hist
  }

  data.add([run:run, hlist:histlist, flist:funclist, mean:meanlist, sigma:sigmalist, clist:chi2list])
}

['mean', 'sigma'].each{name->
  TDirectory out = new TDirectory()
  out.mkdir('/timelines')
  ['layer1','layer2'].eachWithIndex{layer, lindex ->
    def grtl = new GraphErrors(layer)
    grtl.setTitle("FTH MIPS time per layer (" + name + ")")
    grtl.setTitleY("FTH MIPS time per layer (" + name + ") (ns)")
    grtl.setTitleX("run number")

    data.each{
      out.mkdir('/'+it.run)
      out.cd('/'+it.run)

      out.addDataSet(it.hlist[lindex])
      out.addDataSet(it.flist[lindex])
      grtl.addPoint(it.run, it[name][lindex], 0, 0)
    }
    out.cd('/timelines')
    out.addDataSet(grtl)
  }
  out.writeFile('fth_MIPS_time_' + name + '.hipo')
}
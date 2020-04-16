import org.jlab.groot.data.TDirectory
import org.jlab.groot.data.GraphErrors
import fitter.CNDFitter

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

  def histlist = [def h1, def h2, def h3].withIndex().collect{hist, lindex ->
    for(int sector=0;sector<24;sector++){
      for(int comp=0;comp<2;comp++){
        if (!hist) hist = dir.getObject(String.format("/cnd/CND_alignE_L%d_S%d_C%d",lindex+1,sector+1,comp+1))
        else hist.add(dir.getObject(String.format("/cnd/CND_alignE_L%d_S%d_C%d",lindex+1,sector+1,comp+1)))
      }
    }
    hist.setName("layer"+(lindex+1))
    hist.setTitle("dE/dz (GeV/cm)")
    funclist.add(CNDFitter.edepfit(hist))
    meanlist.add(funclist[lindex].getParameter(1))
    sigmalist.add(funclist[lindex].getParameter(2))
    chi2list.add(funclist[lindex].getChiSquare())
    hist
  }

  data.add([run:run, hlist:histlist, flist:funclist, mean:meanlist, sigma:sigmalist, clist:chi2list])
}


['mean','sigma'].each{name ->
  TDirectory out = new TDirectory()
  out.mkdir('/timelines')
  ['layer1','layer2','layer3'].eachWithIndex{layer, lindex ->
    def grtl = new GraphErrors(layer+' '+name)
    grtl.setTitle("MIPS dE/dz")
    grtl.setTitleY("MIPS dE/dz (GeV/cm)")
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
  out.writeFile('cnd_dEdz_'+name+'.hipo')
}
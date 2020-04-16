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

  def histlist = [1,2,3].collect{iL ->
    def h2 = dir.getObject(String.format("/cnd/Diff Z CND_L%d",iL))
    def h1 = h2.projectionY()
    h1.setName("layer"+iL)
    h1.setTitle("CVT z - CND z")
    h1.setTitleX("CVT z - CND z (cm)")

    def f1 = CNDFitter.zdifffit(h1)
    funclist.add(f1)
    meanlist.add(f1.getParameter(1))
    sigmalist.add(f1.getParameter(2).abs())
    chi2list.add(f1.getChiSquare())
    return h1
  }

  data.add([run:run, hlist:histlist, flist:funclist, mean:meanlist, sigma:sigmalist, clist:chi2list])
}


['mean','sigma'].each{name ->
  TDirectory out = new TDirectory()
  out.mkdir('/timelines')
  ['layer1','layer2','layer3'].eachWithIndex{layer, lindex ->
    def grtl = new GraphErrors(layer+' '+name)
    grtl.setTitle("CVT z - CND z per layer")
    grtl.setTitleY("CVT z - CND z per layer (cm)")
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
  out.writeFile('cnd_zdiff_'+name+'.hipo')
}
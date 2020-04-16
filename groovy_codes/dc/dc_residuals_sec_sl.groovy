import org.jlab.groot.data.TDirectory
import org.jlab.groot.data.GraphErrors
import fitter.DCFitter

data = []

for(arg in args) {
  TDirectory dir = new TDirectory()
  dir.readFile(arg)

  def name = arg.split('/')[-1]
  def m = name =~ /\d{4,5}/
  def run = m[0].toInteger()

  def funclist = [[],[],[],[],[],[]]
  def meanlist = [[],[],[],[],[],[]]
  def sigmalist = [[],[],[],[],[],[]]
  def chi2list = [[],[],[],[],[],[]]
  def histlist =   (0..<6).collect{sec-> (0..<6).collect{sl ->
      def h1 = dir.getObject(String.format('/dc/DC_residuals_trkDoca_%d_%d',(sec+1),(sl+1))).projectionY()
      h1.setName("sec"+(sec+1)+"sl"+(sl+1))
      h1.setTitle("DC residuals per sector per superlayer")
      h1.setTitleX("DC residuals per sector per superlayer (cm)")
      def f1 = DCFitter.fit(h1)
      funclist[sec].add(f1)
      meanlist[sec].add(f1.getParameter(1))
      sigmalist[sec].add(f1.getParameter(2))
      chi2list[sec].add(f1.getChiSquare())
      return h1
    }
  }

  data.add([run:run, hlist:histlist, flist:funclist, mean:meanlist, sigma:sigmalist, clist:chi2list])
}

['mean', 'sigma'].each{ name ->
  TDirectory out = new TDirectory()
  out.mkdir('/timelines')
  (0..<6).each{ sec->
    (0..<6).each{sl->
      def grtl = new GraphErrors('sec'+(sec+1)+' sl'+(sl+1))
      grtl.setTitle("DC residuals (" + name + " value) per sector per superlayer")
      grtl.setTitleY("DC residuals (" + name + " value) per sector per superlayer (cm)")
      grtl.setTitleX("run number")
      
      data.each{
        if (sec==0 && sl==0) out.mkdir('/'+it.run)
        out.cd('/'+it.run) 
        out.addDataSet(it.hlist[sec][sl])
        out.addDataSet(it.flist[sec][sl])
        grtl.addPoint(it.run, it[name][sec][sl], 0, 0)
      }
      out.cd('/timelines')
      out.addDataSet(grtl)
    }
  }

  out.writeFile('dc_residuals_sec_sl_'+name+'.hipo')
}
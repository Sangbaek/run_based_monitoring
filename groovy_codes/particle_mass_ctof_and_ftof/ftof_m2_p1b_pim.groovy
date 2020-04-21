import org.jlab.groot.data.TDirectory
import org.jlab.groot.data.GraphErrors
import fitter.FTOFFitter_mass

class ftof_m2_p1b_pim.groovy {

def data = []

def processDirectory(dir, run) {
  def funclist = []
  def meanlist = []
  def sigmalist = []
  def chi2list = []
  def histlist =   (0..<6).collect{
    def h1 = dir.getObject(String.format("/FTOF/H_FTOF_neg_mass_mom_pad1b_%d",it+1)).projectionY()
    h1.setName("sec"+(it+1))
    h1.setTitle("FTOF p1b mass^2 for negatives")
    h1.setTitleX("FTOF p1b mass^2 for negatives (GeV^2)")
    def f1 = FTOFFitter_mass.fit(h1)
    funclist.add(f1)
    meanlist.add(f1.getParameter(1))
    sigmalist.add(f1.getParameter(2).abs())
    chi2list.add(f1.getChiSquare())
    return h1
  }
  data.add([run:run, hlist:histlist, flist:funclist, mean:meanlist, sigma:sigmalist, clist:chi2list])
}



def close() {

  ['mean', 'sigma'].each{ name ->
    TDirectory out = new TDirectory()
    out.mkdir('/timelines')
    (0..<6).each{ sec->
      def grtl = new GraphErrors('sec'+(sec+1))
      grtl.setTitle("FTOF p1b mass^2 for #pi^- (" + name + ")")
      grtl.setTitleY("FTOF p1b mass^2 for #pi^- (" + name + ")" + " (GeV^2)")
      grtl.setTitleX("run number")

      data.each{
        if (sec==0){
          out.mkdir('/'+it.run)
        }
        out.cd('/'+it.run)
        out.addDataSet(it.hlist[sec])
        out.addDataSet(it.flist[sec])
        grtl.addPoint(it.run, it[name][sec], 0, 0)
      }
      out.cd('/timelines')
      out.addDataSet(grtl)
    }

    out.writeFile('ftof_m2_p1b_pim_'+name+'.hipo')
  }
}
}

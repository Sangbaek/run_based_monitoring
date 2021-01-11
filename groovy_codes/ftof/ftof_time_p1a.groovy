package ftof
import java.util.concurrent.ConcurrentHashMap
import org.jlab.groot.data.TDirectory
import org.jlab.groot.data.GraphErrors
import fitter.FTOFFitter

class ftof_time_p1a {

def data = new ConcurrentHashMap()

def processDirectory(dir, run) {
  def funclist = []
  def meanlist = []
  def sigmalist = []
  def chi2list = []
  def meanerrorlist = []
  def sigmaerrorlist = []
  def histlist =   (0..<6).collect{
    def h1 = dir.getObject('/tof/p1a_dt_S'+(it+1))
    def f1 = FTOFFitter.timefit_p1a(h1)

    funclist.add(f1)
    meanlist.add(f1.getParameter(1))
    sigmalist.add(f1.getParameter(2).abs())
    chi2list.add(f1.getChiSquare())
    meanerrorlist.add(f1.parameter(1).error())
    sigmaerrorlist.add(f1.parameter(2).error())
    return h1
  }
  data[run] = [run:run, hlist:histlist, flist:funclist, mean:meanlist, sigma:sigmalist, clist:chi2list, meanerrorlist:meanerrorlist, sigmaerrorlist:sigmaerrorlist]
}



def close() {

  ['mean', 'sigma'].each{ name ->
    TDirectory out = new TDirectory()
    out.mkdir('/timelines')
    (0..<6).each{ sec->
      def grtl = new GraphErrors('sec'+(sec+1))
      grtl.setTitle("p1a Vertex-time difference FTOF_vtime-RFT for e-, e+, pi-, and pi+ (" + name + ")")
      grtl.setTitleY("p1a Vertex-time difference FTOF_vtime-RFT for e-, e+, pi-, and pi+ (" + name + ") (ns)")
      grtl.setTitleX("run number")

      data.sort{it.key}.each{run,it->
        if (sec==0){
          out.mkdir('/'+it.run)
        }
        out.cd('/'+it.run)
        out.addDataSet(it.hlist[sec])
        out.addDataSet(it.flist[sec])
        grtl.addPoint(it.run, it[name][sec], 0, it[name+'errorlist'][sec])
      }
      out.cd('/timelines')
      out.addDataSet(grtl)
    }

    out.writeFile('ftof_time_p1a_' + name + '.hipo')
  }
}
}

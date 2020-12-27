package ftof
import java.util.concurrent.ConcurrentHashMap
import org.jlab.groot.data.TDirectory
import org.jlab.groot.data.GraphErrors
import fitter.FTOFFitter

class ftof_time_p1b {

def data = new ConcurrentHashMap()

def processDirectory(dir, run) {
  def funclist = []
  def meanlist = []
  def sigmalist = []
  def chi2list = []
  def histlist =   (0..<6).collect{
    def h1 = dir.getObject('/tof/p1b_dt_S'+(it+1))
    def f1 = FTOFFitter.timefit_p1b(h1)

    funclist.add(f1)
    meanlist.add(f1.getParameter(1))
    sigmalist.add(f1.getParameter(2).abs())
    chi2list.add(f1.getChiSquare())
    return h1
  }
  data[run] = [run:run, hlist:histlist, flist:funclist, mean:meanlist, sigma:sigmalist, clist:chi2list]
}



def close() {

  ['mean', 'sigma'].each{ name ->
    TDirectory out = new TDirectory()
    out.mkdir('/timelines')
    (0..<6).each{ sec->
      def grtl = new GraphErrors('sec'+(sec+1))
      grtl.setTitle("p1b Vertex-time difference FTOF_vtime-RFT for pions and electrons (" + name + ")")
      grtl.setTitleY("p1b Vertex-time difference FTOF_vtime-RFT for pions and electrons (" + name + ") (ns)")
      grtl.setTitleX("run number")

      data.sort{it.key}.each{run,it->
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

    out.writeFile('ftof_time_p1b_' + name + '.hipo')
  }
}
}

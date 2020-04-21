import org.jlab.groot.data.TDirectory
import org.jlab.groot.data.GraphErrors

class forward_Tracking_Poschi2.groovy {

def data = []

def processDirectory(dir, run) {
  def histlist =   (0..<6).collect{
    def h1 = dir.getObject('/dc/H_dcm_chi2_S'+(it+1))
    return h1
  }
  data.add([run:run, hlist:histlist])
}



def close() {

  TDirectory out = new TDirectory()
  out.mkdir('/timelines')
  (0..<6).each{ sec->
    def grtl = new GraphErrors('sec'+(sec+1))
    grtl.setTitle("Average Forward Reconstruction chi2 for positives")
    grtl.setTitleY("Average Forward Reconstruction chi2 for positives")
    grtl.setTitleX("run number")

    data.each{
      if (sec==0){
        out.mkdir('/'+it.run)
      }
      out.cd('/'+it.run)
      out.addDataSet(it.hlist[sec])
      grtl.addPoint(it.run, it.hlist[sec].getMean(), 0, 0)
    }
    out.cd('/timelines')
    out.addDataSet(grtl)
  }

  out.writeFile('forward_positive_chi2.hipo')
}
}

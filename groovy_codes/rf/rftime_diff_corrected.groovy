import org.jlab.groot.data.TDirectory
import org.jlab.groot.data.GraphErrors
import fitter.RFFitter;

class rftime_diff_corrected.groovy {

def data = []

def processDirectory(dir, run) {
  def h1 = dir.getObject('/RF/H_RFtimediff_corrected')

  f1 = RFFitter.fit(h1)

  data.add([run:run, mean:f1.getParameter(1), sigma:f1.getParameter(2).abs(), h1:h1, f1:f1])
}



def close() {


  ['mean', 'sigma'].each{name ->
    TDirectory out = new TDirectory()

    def grtl = new GraphErrors('RFtime_diff_'+name)
    grtl.setTitle("Average rftime difference ("+name+")")
    grtl.setTitleY("Average rftime difference ("+name+") (ns)")
    grtl.setTitleX("run number")

    data.each{
      grtl.addPoint(it.run, it[name], 0, 0)
      out.mkdir('/'+it.run)
      out.cd('/'+it.run)
      out.addDataSet(it.h1)
      out.addDataSet(it.f1)
    }

    out.mkdir('/timelines')
    out.cd('/timelines')
    grtl.each{ out.addDataSet(it) }
    out.writeFile('rftime_12_diff_corrected_'+name+'.hipo')
  }
}
}

import org.jlab.groot.data.TDirectory
import org.jlab.groot.data.GraphErrors
import fitter.CTOFFitter_mass;

class ctof_m2_pim.groovy {

def data = []

def processDirectory(dir, run) {
  def h1 = dir.getObject('/ctof/H_CTOF_neg_mass')
  def f1 = CTOFFitter_mass.fit(h1)

  data.add([run:run, mean:f1.getParameter(1), sigma:f1.getParameter(2).abs(), h1:h1, f1:f1])
}



def close() {

  ['mean', 'sigma'].each{name ->
    TDirectory out = new TDirectory()

    def grtl = new GraphErrors(name)
    grtl.setTitle("CTOF mass^2 for #pi^- ("+name+")")
    grtl.setTitleY("CTOF mass^2 for #pi^- ("+name+") (GeV^2)")
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
    out.addDataSet(grtl)
    out.writeFile('ctof_m2_pim_'+name+'.hipo')
  }
}
}

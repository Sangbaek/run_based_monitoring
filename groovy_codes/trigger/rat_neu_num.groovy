import org.jlab.groot.data.TDirectory
import org.jlab.groot.data.GraphErrors

class rat_neu_num.groovy {

def data = []

def processDirectory(dir, run) {
  def h1 = dir.getObject('/trig/H_trig_sector_neutral_rat')
  data.add([run:run, h1:h1])
}



def close() {


  TDirectory out = new TDirectory()
  out.mkdir('/timelines')

  (0..<6).each{ sec->
    def grtl = new GraphErrors('sec'+(sec+1))
    grtl.setTitle("Neutrals per trigger per sector")
    grtl.setTitleY("Neutrals per trigger per sector")
    grtl.setTitleX("run number")

    data.each{
      if (sec==0){
        out.mkdir('/'+it.run)
        out.cd('/'+it.run)
        out.addDataSet(it.h1)
      }
      grtl.addPoint(it.run, it.h1.getBinContent(sec),0,0)
    }
    out.cd('/timelines')
    out.addDataSet(grtl)
  }

  out.writeFile('rat_neutral.hipo')
}
}

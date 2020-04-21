import org.jlab.groot.data.TDirectory
import org.jlab.groot.data.GraphErrors

class band_adccor.groovy {

def data = []

def processDirectory(dir, run) {
  def h1 = dir.getObject('/BAND/H_BAND_ADC_LR_SectorCombination1')
  def h2 = dir.getObject('/BAND/H_BAND_ADC_LR_SectorCombination2')

  data.add([run:run, Comb1:h1, Comb2:h2])
}



def close() {


  TDirectory out = new TDirectory()
  out.mkdir('/timelines')

  ["Comb1", "Comb2"].each{ name ->
    def gr = new GraphErrors(name)
    gr.setTitle("BAND adc LR Sector Combination")
    gr.setTitleY("Mean Value of sqrt( adcLcorr * adcRcorr )")
    gr.setTitleX("run number")

    data.each{
      out.mkdir('/'+it.run)
      out.cd('/'+it.run)
      out.addDataSet(it[name])
      gr.addPoint(it.run, it[name].getMean(), 0, 0)
    }
    out.cd('/timelines')
    out.addDataSet(gr)
  }

  out.writeFile('band_adcCorr.hipo')
}
}

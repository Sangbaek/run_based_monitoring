import org.jlab.groot.data.TDirectory
import org.jlab.groot.data.GraphErrors

data = []

for(arg in args) {
  TDirectory dir = new TDirectory()
  dir.readFile(arg)

  def name = arg.split('/')[-1]
  def m = name =~ /\d{4,5}/
  def run = m[0].toInteger()

  def histlist =   (0..<6).collect{
    def h1 = dir.getObject('/trig/H_trig_S'+(it+1)+'_HTCC_n')
    return h1
  }
  data.add([run:run, hlist:histlist])
}

TDirectory out = new TDirectory()
out.mkdir('/timelines')
(0..<6).each{ sec->
  def grtl = new GraphErrors('sec'+(sec+1))
  grtl.setTitle("Average HTCC Number of Photoelectrons per sector")
  grtl.setTitleY("Average HTCC Number of Photoelectrons per sector")
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

out.writeFile('htcc_nphe_sec.hipo')
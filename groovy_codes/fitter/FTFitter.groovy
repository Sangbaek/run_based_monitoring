package fitter

import org.jlab.groot.fitter.DataFitter
import org.jlab.groot.data.H1F
import org.jlab.groot.math.F1D


class FTFitter {
  static F1D pi0fit(H1F h1) {
    def f1 = new F1D("fit:"+h1.getName(), "[amp]*gaus(x,[mean],[sigma])+[p0]+[p1]*x+[p2]*x*x", -1.0, 1.0)
    def hMean=h1.getAxis().getBinCenter(h1.getMaximumBin())
    double hRMS  = h1.getRMS()//5

    f1.setParameter(1, hMean)
    f1.setParLimits(1, hMean-2.5*hRMS, hMean+2.5*hRMS)
    f1.setParameter(2, hRMS)
    f1.setParLimits(2, 0, 20)
    f1.setParameter(3, 0)
    f1.setParLimits(3, 0, h1.getMax()*0.5)
    f1.setRange(hMean-25,hMean+25)

    def makefit = {func->
      hMean = func.getParameter(1)
      hRMS = func.getParameter(2).abs()
      func.setRange(hMean-2.5*hRMS,hMean+2.5*hRMS)
      DataFitter.fit(func,h1,"Q")
      return [func.getChiSquare(), (0..<func.getNPars()).collect{func.getParameter(it)}]
    }

    def fits1 = (0..20).collect{makefit(f1)}
    def bestfit = fits1.sort()[0]
    f1.setParameters(*bestfit[1])
    //makefit(f1)
    return f1
  }
}

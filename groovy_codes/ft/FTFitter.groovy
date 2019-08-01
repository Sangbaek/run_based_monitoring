import org.jlab.groot.fitter.DataFitter
import org.jlab.groot.data.H1F
import org.jlab.groot.math.F1D


class FTFitter {
  static F1D pi0fit(H1F h1) {
    def f1 = new F1D("fit:"+h1.getName(), "[amp]*gaus(x,[mean],[sigma])+[p0]+[p1]*x+[p2]*x*x", -1.0, 1.0)
    double hMean = h1.getAxis().getBinCenter(h1.getMaximumBin())
    double hRMS  = 5

    f1.setParameter(1, hMean)
    f1.setParameter(2, hRMS)
    f1.setParameter(3, 0)
    f1.setParLimits(3, 0, h1.getMax()*0.2)

    def fits1 = (0..20).collect{
      hRMS = f1.getParameter(2).abs()
      f1.setRange(hMean-2.5*hRMS, hMean+2.5*hRMS)
      DataFitter.fit(f1,h1,"Q")
      return [f1.getChiSquare(), (0..<f1.getNPars()).collect{f1.getParameter(it)}]
    }

    def bestfit = fits1.sort()[0]
    f1.setParameters(*bestfit[1])

    return f1
  }
}

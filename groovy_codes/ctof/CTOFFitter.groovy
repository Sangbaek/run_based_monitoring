import org.jlab.groot.fitter.DataFitter
import org.jlab.groot.data.H1F
import org.jlab.groot.math.F1D


class CTOFFitter {
  static F1D timefit(H1F h1) {
    def f1 = new F1D("fit:"+h1.getName(), "[amp]*gaus(x,[mean],[sigma])", -5.0, 5.0)
    double hAmp  = h1.getBinContent(h1.getMaximumBin());
    double hMean = h1.getAxis().getBinCenter(h1.getMaximumBin())
    double hRMS  = h1.getRMS();
    double rangeMin = (hMean - 2*hRMS);
    double rangeMax = (hMean + hRMS);
    f1.setRange(rangeMin, rangeMax);
    f1.setParameter(0, hAmp);
    // f1.setParLimits(0, hAmp*0.8, hAmp*1.2);
    f1.setParameter(1, hMean);
    // f1.setParLimits(1, 0, 0.5);
    f1.setParameter(2, hRMS);
    // f1.setParLimits(2, 0.5*hRMS, 1.5*hRMS);


    def makefit = {func->
      hMean = func.getParameter(1)
      hRMS = func.getParameter(2).abs()
      func.setRange(hMean-2*hRMS,hMean+hRMS)
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

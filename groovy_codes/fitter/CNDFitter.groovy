/**
*
* Fitter package for CND
*
* Writer: Sangbaek Lee, Andrey Kim
*
**/
package fitter

import org.jlab.groot.fitter.DataFitter
import org.jlab.groot.data.H1F
import org.jlab.groot.math.F1D


class CNDFitter{
	static F1D MIPSfit(H1F h1) {
		double maxE = h1.getBinContent(h1.getMaximumBin());
	    def f1=new F1D("fit:"+h1.getName(),"[amp]*landau(x,[mean],[sigma])+[p0]+[p1]*x", 1.5, 3.5);
	    f1.setParameter(1,2.0);
	    f1.setParameter(0,maxE);
	    f1.setParLimits(0,maxE*0.9,maxE*1.1);
	    f1.setParameter(2,1.0);
	    f1.setParameter(3,0.0);
	    DataFitter.fit(f1,h1,"LQ")

	    double hMean, hRMS

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

 	static F1D timefit(H1F h1){
 		def f1 =new F1D("fit:"+h1.getName(),"[amp]*gaus(x,[mean],[sigma])", -1.0, 1.0);
		f1.setLineColor(33);
		f1.setLineWidth(10);
		f1.setOptStat("1111");
		double maxt = h1.getBinContent(h1.getMaximumBin());
		double hMean = h1.getAxis().getBinCenter(h1.getMaximumBin());
		double hRMS = h1.getRMS()
		f1.setParameter(1,hMean);
		f1.setParLimits(1,hMean-0.5,hMean+1);
		f1.setRange(hMean-0.5,hMean+0.5);
		f1.setParameter(0,maxt);
		f1.setParLimits(0,maxt*0.95,maxt*1.1);
		f1.setParameter(2,0.2);
		DataFitter.fit(f1, h1, "");

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

	static F1D zdifffit(H1F h1){
		def f1 =new F1D("fit:"+h1.getName(),"[amp]*gaus(x,[mean],[sigma])+[cst]", -5.0, 5.0);
		f1.setLineColor(33);
		f1.setLineWidth(10);
		f1.setOptStat("1111");
		double maxz = h1.getBinContent(h1.getMaximumBin());
		f1.setRange(-7,7);
		f1.setParameter(1,0.0);
		f1.setParameter(0,maxz);
		f1.setParLimits(0,maxz*0.9,maxz*1.1);
		f1.setParameter(2,3.0);

		double hMean, hRMS

		DataFitter.fit(f1, h1, "");

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
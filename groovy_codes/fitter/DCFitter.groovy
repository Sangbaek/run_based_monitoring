/**
*
* Fitter package for DC
*
* Writer: Sangbaek Lee, Andrey Kim
*
**/
package fitter

import org.jlab.groot.fitter.DataFitter
import org.jlab.groot.data.H1F
import org.jlab.groot.math.F1D


class DCFitter{
	static F1D fit(H1F h1) {
        def f1 = new F1D("fit:"+h1.getName(), "[amp]*gaus(x,[mean],[sigma])+[const]", -0.5, 0.5);

        double hAmp  = h1.getBinContent(h1.getMaximumBin());
        double hMean = h1.getAxis().getBinCenter(h1.getMaximumBin());
        double hRMS  = h1.getRMS(); //ns
        f1.setRange(hMean-1, hMean+1);
        f1.setParameter(0, hAmp);
        f1.setParameter(1, hMean);
        f1.setParLimits(1, hMean-0.5, hMean+0.5);
        f1.setParameter(2, hRMS);
        f1.setParameter(3,0);

	    DataFitter.fit(f1,h1,"LQ")


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

 	static F1D t0fit(H1F h1, int slayer){

    	def f1 = new F1D("fit:"+h1.getName(),"[p0]/(1+exp(-[p1]*(x-[p2])))",-100,1000);

		def test = 0.05
		def cut_test = test* h1.getMax()
		def T_test=-10000
		def T_test2, T10, T90
		def test2 = 0.85
		def cut_test2 = test2* h1.getMax()
		def cut10=0.1*h1.getMax()
		def cut90=0.9*h1.getMax()
		for(int bin =0; bin < h1.getMaximumBin(); bin++){
		  if (h1.getBinContent(bin)<cut_test && h1.getBinContent(bin+1)>cut_test) T_test=h1.getDataX(bin)
		  if (h1.getBinContent(bin)<cut_test2 && h1.getBinContent(bin+1)>cut_test2) T_test2=h1.getDataX(bin+1)
		  if (h1.getBinContent(bin)<cut10 && h1.getBinContent(bin+1)>cut10) T10=h1.getDataX(bin)
		  if (h1.getBinContent(bin)<cut90 && h1.getBinContent(bin+1)>cut90) T90=h1.getDataX(bin+1)
		}
		if (T_test==-10000) T_test=T10
		def P0 = h1.getMax()
		def P1= 4/(T90-T10)
		def P2 = (T10+T90)/2
		if (slayer == 1) {
			// min = 100.0; max = 240.0;
			f1.setParameter(0,P0); f1.setParLimits(1,2*cut10,2*cut90);
			f1.setParameter(1,P1); f1.setParLimits(1,P1*0.1,P1*2);
			f1.setParameter(2,P2); f1.setParLimits(2,T10,T90);
		}
		if (slayer == 2) {
			// min = 120.0; max = 240.0;
			f1.setParameter(0,P0); f1.setParLimits(1,2*cut10,2*cut90);
			f1.setParameter(1,P1); f1.setParLimits(1,P1*0.1,P1*2);
			f1.setParameter(2,P2); f1.setParLimits(2,T10,T90);
		}
		if (slayer == 3) {
			// min = 200.0; max = 450.0;
			f1.setParameter(0,P0); f1.setParLimits(1,2*cut10,2*cut90);
			f1.setParameter(1,P1);f1.setParLimits(1,P1*0.1,P1*2);
			f1.setParameter(2,P2); f1.setParLimits(2,T10-50,T90+50);
		}
		if (slayer == 4) {
			// min = 200.0; max = 500.0;
			f1.setParameter(0,P0); f1.setParLimits(1,2*cut_test,2*cut90);
			f1.setParameter(1,P1); f1.setParLimits(1,P1*0.1,P1*2);
			f1.setParameter(2,P2); f1.setParLimits(2,T10-50,T90+50);
		}
		if (slayer == 5) {
			// min = 400.0; max = 700.0;
			f1.setParameter(0,P0); f1.setParLimits(1,2*cut10,2*cut90);
			f1.setParameter(1,P1);f1.setParLimits(1,P1*0.1,P1*2);
			f1.setParameter(2,P2); f1.setParLimits(2,T10-50,T90+50);
		}
		if (slayer == 6) {
			// min = 480.0; max = 700.0;
			f1.setParameter(0,P0); f1.setParLimits(1,2*cut10,2*cut90);
			f1.setParameter(1,P1); f1.setParLimits(1,P1*0.1,P1*2);
			f1.setParameter(2,P2); f1.setParLimits(2,T10-50,T90+50);
		}
		f1.setRange(T_test,T_test2);
		DataFitter.fit(f1,h1,"LQ");

		return f1
	}

	static F1D tmaxfit(H1F h1, int slayer){

    	def f1 = new F1D("fit:"+h1.getName(),"[p0]/(1+exp(-[p1]*(x-[p2])))",-100,1000);

		double min = 100.0;
		double max = 220.0;
		def cut=0.1*h1.getMax()
		def T_cut, T10
		for(int bin = h1.getXaxis().getNBins(); bin >0; bin--){
		  if (h1.getBinContent(bin)<cut && h1.getBinContent(bin-1)>cut){
		    T_cut=h1.getDataX(bin)-100
		    T10=h1.getDataX(bin)
		  }
		}
		def bin_cut = h1.getXaxis().getBin(T_cut)
		def y_cut = h1.getBinContent(bin_cut)
		def y10 = cut
		def P0 = y_cut
		def P1= -4/(T10-T_cut)
		def P2 = (T10+T_cut)/2
		if (slayer == 1) {
			// min = 100.0; max = 240.0;
			f1.setParameter(0,P0); f1.setParLimits(1,2*y10,2*y_cut);
			f1.setParameter(1,P1); f1.setParLimits(1,P1*0.1,P1*2);
			f1.setParameter(2,P2); f1.setParLimits(2,T_cut,T10);
		}
		if (slayer == 2) {
			// min = 120.0; max = 240.0;
			f1.setParameter(0,P0); f1.setParLimits(1,2*y10,2*y_cut);
			f1.setParameter(1,P1); f1.setParLimits(1,P1*0.1,P1*2);
			f1.setParameter(2,P2); f1.setParLimits(2,T_cut,T10);
		}
		if (slayer == 3) {
			// min = 200.0; max = 450.0;
			f1.setParameter(0,P0); f1.setParLimits(1,2*y10,2*y_cut);
			f1.setParameter(1,P1);f1.setParLimits(1,P1*0.1,P1*2);
			f1.setParameter(2,P2); f1.setParLimits(2,T_cut-50,T10+50);
		}
		if (slayer == 4) {
			// min = 200.0; max = 500.0;
			f1.setParameter(0,P0); f1.setParLimits(1,2*y10,2*y_cut);
			f1.setParameter(1,P1); f1.setParLimits(1,P1*0.1,P1*2);
			f1.setParameter(2,P2); f1.setParLimits(2,T_cut-50,T10+50);
		}
		if (slayer == 5) {
			// min = 400.0; max = 700.0;
			f1.setParameter(0,P0); f1.setParLimits(1,2*y10,2*y_cut);
			f1.setParameter(1,P1);f1.setParLimits(1,P1*0.1,P1*2);
			f1.setParameter(2,P2); f1.setParLimits(2,T_cut-50,T10+50);
		}
		if (slayer == 6) {
			// min = 480.0; max = 700.0;
			f1.setParameter(0,P0); f1.setParLimits(1,2*y10,2*y_cut);
			f1.setParameter(1,P1); f1.setParLimits(1,P1*0.1,P1*2);
			f1.setParameter(2,P2); f1.setParLimits(2,T_cut-50,T10+50);
		}
		f1.setRange(T_cut,T10);
		DataFitter.fit(f1,h1,"LQ");

		return f1


	}
} 
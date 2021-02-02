package org.jlab.clas.timeline

import org.jlab.groot.data.TDirectory
import groovyx.gpars.GParsPool

def engines = [
  out_monitor: [new org.jlab.clas.timeline.timeline.bmtbst.bmt_Occupancy(),
    new org.jlab.clas.timeline.timeline.bmtbst.bmt_OnTrkLayers(),
    new org.jlab.clas.timeline.timeline.bmtbst.bst_Occupancy(),
    new org.jlab.clas.timeline.timeline.bmtbst.bst_OnTrkLayers(),
    new org.jlab.clas.timeline.timeline.central.central_Km_num(),
    new org.jlab.clas.timeline.timeline.central.central_pim_num(),
    new org.jlab.clas.timeline.timeline.central.central_prot_num(),
    new org.jlab.clas.timeline.timeline.central.central_Kp_num(),
    new org.jlab.clas.timeline.timeline.central.central_pip_num(),
    new org.jlab.clas.timeline.timeline.cvt.cvt_Vz_negative(),
    new org.jlab.clas.timeline.timeline.cvt.cvt_Vz_positive(),
    new org.jlab.clas.timeline.timeline.cvt.cvt_chi2_elec(),
    new org.jlab.clas.timeline.timeline.cvt.cvt_chi2_neg(),
    new org.jlab.clas.timeline.timeline.cvt.cvt_chi2_pos(),
    new org.jlab.clas.timeline.timeline.cvt.cvt_chi2norm(),
    new org.jlab.clas.timeline.timeline.cvt.cvt_ndf(),
    new org.jlab.clas.timeline.timeline.cvt.cvt_p(),
    new org.jlab.clas.timeline.timeline.cvt.cvt_pathlen(),
    new org.jlab.clas.timeline.timeline.cvt.cvt_pt(),
    new org.jlab.clas.timeline.timeline.cvt.cvt_trks(),
    new org.jlab.clas.timeline.timeline.cvt.cvt_trks_neg(),
    new org.jlab.clas.timeline.timeline.cvt.cvt_trks_neg_rat(),
    new org.jlab.clas.timeline.timeline.cvt.cvt_trks_pos(),
    new org.jlab.clas.timeline.timeline.cvt.cvt_trks_pos_rat(),
    new org.jlab.clas.timeline.timeline.trigger.rat_Km_num(),
    new org.jlab.clas.timeline.timeline.trigger.rat_neg_num(),
    new org.jlab.clas.timeline.timeline.trigger.rat_pos_num(),
    new org.jlab.clas.timeline.timeline.trigger.rat_Kp_num(),
    new org.jlab.clas.timeline.timeline.trigger.rat_neu_num(),
    new org.jlab.clas.timeline.timeline.trigger.rat_prot_num(),
    new org.jlab.clas.timeline.timeline.trigger.rat_elec_num(),
    new org.jlab.clas.timeline.timeline.trigger.rat_pim_num(),
    new org.jlab.clas.timeline.timeline.trigger.rat_muon_num(),
    new org.jlab.clas.timeline.timeline.trigger.rat_pip_num(),
    new org.jlab.clas.timeline.timeline.forward.forward_Tracking_Elechi2(),
    new org.jlab.clas.timeline.timeline.forward.forward_Tracking_EleVz(),
    new org.jlab.clas.timeline.timeline.forward.forward_Tracking_Poschi2(),
    new org.jlab.clas.timeline.timeline.forward.forward_Tracking_PosVz(),
    new org.jlab.clas.timeline.timeline.forward.forward_Tracking_Negchi2(),
    new org.jlab.clas.timeline.timeline.forward.forward_Tracking_NegVz(),
    new org.jlab.clas.timeline.timeline.ec.ec_Sampl(),
    new org.jlab.clas.timeline.timeline.ec.ec_gg_m(),
    new org.jlab.clas.timeline.timeline.ec.ec_pip_time(),
    new org.jlab.clas.timeline.timeline.ec.ec_pim_time(),
    new org.jlab.clas.timeline.timeline.htcc.htcc_nphe_sector(),
    new org.jlab.clas.timeline.timeline.ltcc.ltcc_nphe_sector(),
    new org.jlab.clas.timeline.timeline.rf.rftime_diff(),
    new org.jlab.clas.timeline.timeline.rf.rftime_pim_FD(),
    new org.jlab.clas.timeline.timeline.rf.rftime_pim_CD(),
    new org.jlab.clas.timeline.timeline.rf.rftime_pip_FD(),
    new org.jlab.clas.timeline.timeline.rf.rftime_pip_CD(),
    new org.jlab.clas.timeline.timeline.rf.rftime_elec_FD(),
    new org.jlab.clas.timeline.timeline.rf.rftime_diff_corrected(),
    new org.jlab.clas.timeline.timeline.rf.rftime_prot_FD(),
    new org.jlab.clas.timeline.timeline.rf.rftime_prot_CD()],
  out_CND: [new org.jlab.clas.timeline.timeline.cnd.cnd_MIPS_dE_dz(),
    new org.jlab.clas.timeline.timeline.cnd.cnd_time_neg_vtP(),
    new org.jlab.clas.timeline.timeline.cnd.cnd_zdiff()],
  out_CTOF: [new org.jlab.clas.timeline.timeline.ctof.ctof_edep(),
    new org.jlab.clas.timeline.timeline.ctof.ctof_time(),
    // new org.jlab.clas.timeline.timeline.ctof.ctof_tdcadc(),
    new org.jlab.clas.timeline.timeline.ctof.ctof_tdcadc_left(),
    new org.jlab.clas.timeline.timeline.ctof.ctof_tdcadc_right()],
  out_FT: [new org.jlab.clas.timeline.timeline.ft.ftc_pi0_mass(),
    new org.jlab.clas.timeline.timeline.ft.ftc_time_charged(),
    new org.jlab.clas.timeline.timeline.ft.ftc_time_neutral(),
    new org.jlab.clas.timeline.timeline.ft.fth_MIPS_energy(),
    new org.jlab.clas.timeline.timeline.ft.fth_MIPS_time(),
    new org.jlab.clas.timeline.timeline.ft.fth_MIPS_energy_board(),
    new org.jlab.clas.timeline.timeline.ft.fth_MIPS_time_board()],
  out_HTCC: [new org.jlab.clas.timeline.timeline.htcc.htcc_nphe_ring_sector(),
    new org.jlab.clas.timeline.timeline.htcc.htcc_vtimediff()],
  out_LTCC: [new org.jlab.clas.timeline.timeline.ltcc.ltcc_had_nphe_sector()],
  out_TOF: [new org.jlab.clas.timeline.timeline.ftof.ftof_edep_p1a_smallangles(),
    new org.jlab.clas.timeline.timeline.ftof.ftof_edep_p1a_midangles(),
    new org.jlab.clas.timeline.timeline.ftof.ftof_edep_p1a_largeangles(),
    new org.jlab.clas.timeline.timeline.ftof.ftof_edep_p1b_smallangles(),
    new org.jlab.clas.timeline.timeline.ftof.ftof_edep_p1b_midangles(),
    new org.jlab.clas.timeline.timeline.ftof.ftof_edep_p1b_largeangles(),
    new org.jlab.clas.timeline.timeline.ftof.ftof_edep_p2(),
    new org.jlab.clas.timeline.timeline.ftof.ftof_time_p1a(),
    new org.jlab.clas.timeline.timeline.ftof.ftof_time_p1b(),
    new org.jlab.clas.timeline.timeline.ftof.ftof_time_noTriggers_p1a(),
    new org.jlab.clas.timeline.timeline.ftof.ftof_time_noTriggers_p1b(),
    new org.jlab.clas.timeline.timeline.ftof.ftof_time_p2(),
    new org.jlab.clas.timeline.timeline.ftof.ftof_tdcadc_p1a(),
    new org.jlab.clas.timeline.timeline.ftof.ftof_tdcadc_p1b(),
    new org.jlab.clas.timeline.timeline.ftof.ftof_tdcadc_p2(),
    new org.jlab.clas.timeline.timeline.dc.dc_residuals_sec(),
    new org.jlab.clas.timeline.timeline.dc.dc_residuals_sec_sl(),
    new org.jlab.clas.timeline.timeline.dc.dc_t0_sec_sl(),
    new org.jlab.clas.timeline.timeline.dc.dc_tmax_sec_sl()],
  out_RICH: [new org.jlab.clas.timeline.timeline.rich.rich_timediff(),
    new org.jlab.clas.timeline.timeline.rich.rich_fwhm_pmt()]
]



engines.collectMany{key,engs->engs.collect{[key,it]}}
  .find{name,eng->eng.getClass().getSimpleName()==args[0].split("/")[-1].replace('.groovy','')}
  ?.with{name,engine->
    println([name,args[0],engine.getClass().getSimpleName()])
    def fnames = args.findAll{it.contains(name)}

    GParsPool.withPool 6, {
      fnames.eachParallel{arg->
        TDirectory dir = new TDirectory()
        dir.readFile(arg)
        def fname = arg.split('/')[-1]
        def m = fname =~ /\d{4,7}/
        def run = m[0].toInteger()

        println("debug: "+engine.getClass().getSimpleName()+" processes $arg")
        engine.processDirectory(dir, run)
      }
   }
   engine.close()
 }


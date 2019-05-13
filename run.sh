#!/usr/bin/bash

#Environment Initialization
#for output coordinating
export pdir=`pwd`

#currently we use hipo3 for offline and timeline monitoring, coatjava-5.7.4
#export coatjava='~/.groovy/coatjava/'
#export groovy=$coatjava"bin/run-groovy"
export pdir=`pwd`
export groovy=$pdir"/../.groovy/coatjava/bin/run-groovy"


#Where are monitoring codes by monitoring experts?
export monplots='/work/clas12/rg-b/offline_monitoring/pass0/v2/plots*'
#directory names
#bmtbst central cnd ctof cvt dc ec forward ft ftof htcc ltcc rf trigger

#hipo used for each detectors.
#bmtbst - out_monitor
#central - out_monitor
#cnd - out_CND
#ctof - out_CTOF
#cvt - out_monitor
#dc - out_TOF
#ec - out_monitor
#ft - out_FTOF
#forward - out_monitor
#rf - out_monitor
#trigger - out_monitor
#ftof - out_TOF, dst_mon
#htcc - out_HTCC, out_monitor
#ltcc - out_monitor, out_LTCC

out_monitor=(bmtbst/bmt_Occupancy bmtbst/bmt_OnTrkLayers bmtbst/bst_Occupancy bmtbst/bst_OnTrkLayers \
central/central_Km_num central/central_pim_num central/central_prot_num central/central_Kp_num central/central_pip_num \
cvt/cvt_Vz_negative cvt/cvt_Vz_positive cvt/cvt_chi2_elec cvt/cvt_chi2_neg cvt/cvt_chi2_pos cvt/cvt_chi2norm \
cvt/cvt_ndf cvt/cvt_p cvt/cvt_pathlen cvt/cvt_pt cvt/cvt_trks cvt/cvt_trks_neg cvt/cvt_trks_neg_rat cvt/cvt_trks_pos cvt/cvt_trks_pos_rat 
trigger/rat_Km_num trigger/rat_neg_num trigger/rat_pos_numtrigger/rat_Kp_num \
trigger/rat_neu_num trigger/rat_prot_num trigger/rat_elec_num trigger/rat_pim_num trigger/rat_muon_num trigger/rat_pip_num \
forward/forward_Tracking_Elechi2 forward/forward_Tracking_EleVz forward/forward_Tracking_Poschi2 forward/forward_Tracking_PosVz \
forward/forward_Tracking_Negchi2 forward/forward_Tracking_NegVz \
ec/ec_Sampl ec/ec_gg_m ec/ec_pip_time ec/ec_pim_time \
htcc/htcc_nphe_sector \
ltcc/ltcc_nphe_sector)
out_monitor=(ec/ec_pip_time ec/ec_pim_time)
#rf/rftime_diff rf/rftime_pim rf/rftime_elec rf/rftime_pip \
out_TOF=(dc/dc_residuals_sec dc/dc_residuals_sec_sl dc/dc_tmax_sec_sl ftof/ftof_time)
out_CND=(cnd/cnd_MIPS_dE_dz cnd/cnd_time_neg_vtP cnd/cnd_zdiff)
out_CTOF=(ctof/ctof_edep_pim ctof/ctof_m2_pim ctof/ctof_time_pim ctof/ctof_m2_pip ctof/ctof_time_positive ctof/ctof_time_negative)
out_FT=(ft/ftc_pi0_mass ft/ftc_time_charged ft/ftc_time_neutral ft/fth_MIPS_energy ft/fth_MIPS_time ft/fth_MIPS_energy_board ft/fth_MIPS_time_board)
dst_mon=(ftof/ftof_time_p1a_elec ftof/ftof_time_p1a_pion ftof/ftof_time_p1b_elec ftof/ftof_time_p1b_pion ftof/ftof_time_p2 \
ftof/ftof_edep_p1a_elec ftof/ftof_edep_p1a_pion ftof/ftof_edep_p1b_elec ftof/ftof_edep_p1b_pion ftof/ftof_edep_p2 \
ftof/ftof_m2_p1a_pim ftof/ftof_m2_p1a_pip ftof/ftof_m2_p1a_prot ftof/ftof_m2_p1b_pim ftof/ftof_m2_p1b_pip ftof/ftof_m2_p1b_prot)
out_HTCC=(htcc/htcc_nphe_ring_sector)
#out_LTCC=(ltcc/ltcc_had_nphe_sector)

cd groovy_output

for name in out_monitor
#out_monitor out_CND out_TOF out_CTOF out_FT out_HTCC dst_mon
do 
	var=$name[@]
	for script in ${!var}
	do
		# echo $script
        	$groovy ../groovy_codes/$script.groovy `find $monplots -name "$name*"`
	done
done

out_dir=(bmtbst central cnd ctof cvt dc ec forward ft ftof htcc ltcc rf trigger)
dir_array=out_dir[@]
for dir in ${!dir_array}
do
	mkdir -p "$dir"
done

mv bmt_*.hipo bmtbst/
mv bst_*.hipo bmtbst/
mv cen_*.hipo central/
mv cnd_*.hipo cnd/
mv ctof_*.hipo ctof/
mv cvt_*.hipo cvt/
mv dc_*.hipo dc/
mv ec_*.hipo ec/
mv forward_*.hipo forward/
mv ftc_*.hipo ft/
mv fth_*.hipo ft/
mv ftof_*.hipo ftof/
mv htcc_*.hipo htcc/
mv ltcc_*.hipo ltcc/
mv rf_*.hipo rf/
mv rat_* trigger/

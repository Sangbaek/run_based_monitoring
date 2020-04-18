#!/usr/bin/bash

#Environment Initialization
#for output coordinating
#Change the location of groovy accordingly.
export pdir=`pwd`
export groovy="$COATJAVA/bin/run-groovy"

#Output directory names
rungroup="rga"
cookver="pass0v2.2.10"
out_dir=$rungroup"_"$cookver
mkdir -p $out_dir
cd $out_dir

#subdirectory names
#bmtbst central cnd ctof cvt dc ec forward ft ftof htcc ltcc rf trigger
out_dir=(band bmtbst central cnd ctof cvt dc ec forward ft ftof htcc ltcc rf trigger particle_mass_ctof_and_ftof rich)
dir_array=out_dir[@]
for dir in ${!dir_array}
do
        mkdir -p "$dir"
done

JYPATH=$JYPATH:../groovy_codes

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
trigger/rat_Km_num trigger/rat_neg_num trigger/rat_pos_num trigger/rat_Kp_num \
trigger/rat_neu_num trigger/rat_prot_num trigger/rat_elec_num trigger/rat_pim_num trigger/rat_muon_num trigger/rat_pip_num \
forward/forward_Tracking_Elechi2 forward/forward_Tracking_EleVz forward/forward_Tracking_Poschi2 forward/forward_Tracking_PosVz \
forward/forward_Tracking_Negchi2 forward/forward_Tracking_NegVz \
ec/ec_Sampl ec/ec_gg_m ec/ec_pip_time ec/ec_pim_time \
htcc/htcc_nphe_sector \
ltcc/ltcc_nphe_sector \
rf/rftime_diff rf/rftime_pim_FD rf/rftime_pim_CD rf/rftime_pip_FD rf/rftime_pip_CD \
rf/rftime_elec_FD rf/rftime_diff_corrected rf/rftime_prot_FD rf/rftime_prot_CD)
out_CND=(cnd/cnd_MIPS_dE_dz cnd/cnd_time_neg_vtP cnd/cnd_zdiff)
out_CTOF=(ctof/ctof_edep ctof/ctof_time ctof/ctof_tdcadc)
out_FT=(ft/ftc_pi0_mass ft/ftc_time_charged ft/ftc_time_neutral ft/fth_MIPS_energy ft/fth_MIPS_time ft/fth_MIPS_energy_board ft/fth_MIPS_time_board)
out_HTCC=(htcc/htcc_nphe_ring_sector htcc/htcc_vtimediff)
out_LTCC=(ltcc/ltcc_had_nphe_sector)
out_TOF=(ftof/ftof_edep_p1a_smallangles ftof/ftof_edep_p1a_midangles ftof/ftof_edep_p1a_largeangles ftof/ftof_edep_p1b_smallangles ftof/ftof_edep_p1b_midangles ftof/ftof_edep_p1b_largeangles ftof/ftof_edep_p2 \
ftof/ftof_time_p1a ftof/ftof_time_p1b ftof/ftof_time_p2 \
ftof/ftof_tdcadc_p1a ftof/ftof_tdcadc_p1b ftof/ftof_tdcadc_p2 \
dc/dc_residuals_sec dc/dc_residuals_sec_sl dc/dc_t0_sec_sl  dc/dc_tmax_sec_sl)
out_RICH=(rich/rich_timediff)

# uncomment following for rg-b
#out_CTOF=(ctof/ctof_edep ctof/ctof_time ctof/ctof_tdcadc particle_mass_ctof_and_ftof/ctof_m2_pim particle_mass_ctof_and_ftof/ctof_m2_pip)
#dst_mon=(particle_mass_ctof_and_ftof/ftof_m2_p1a_pim particle_mass_ctof_and_ftof/ftof_m2_p1a_pip particle_mass_ctof_and_ftof/ftof_m2_p1a_prot particle_mass_ctof_and_ftof/ftof_m2_p1b_pim particle_mass_ctof_and_ftof/ftof_m2_p1b_pip particle_mass_ctof_and_ftof/ftof_m2_p1b_prot)
#out_BAND=(band/band_adccor band/band_meantimeadc band/band_meantimetdc)

# use following line to process all histograms
for name in out_RICH out_monitor out_CND out_TOF out_CTOF out_FT out_HTCC out_LTCC #dst_mon out_BAND


# otherwise, select some files and process histograms only from such files
# e.g.,
# out_TOF=(ftof/ftof_edep_p1a_smallangles ftof/ftof_edep_p1a_midangles ftof/ftof_edep_p1a_largeangles ftof/ftof_edep_p1b_smallangles ftof/ftof_edep_p1b_midangles ftof/ftof_edep_p1b_largeangles ftof/ftof_edep_p2)
# for name in out_TOF
do 
	var=$name[@]
	for script in ${!var}
	do
		# prints script name for debugging purpose
		# echo $script
		# if some monitoring histogram has problems at some run numbers, skip such run numbers
        	#if [ "$script" == "forward/forward_Tracking_PosVz" ];then
		#	echo $script
		#	JYPATH=$JYPATH $groovy ../groovy_codes/$script.groovy `find /volatile/clas12/rg-b/offline_monitoring/pass0/v21.3.1/plots* -name "$name*" ! -name "*6265*" ! -name "*11415*"`
		#else
		JYPATH=$JYPATH $groovy ../groovy_codes/$script.groovy `find /w/hallb-scifs17exp/clas12/rg-a/software/clas12_monitoring/plots* -name "$name*"`

		#fi
	done
done

out_dir=(band bmtbst central cnd ctof cvt dc ec forward ft ftof htcc ltcc rf trigger particle_mass_ctof_and_ftof)
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
mv rftime_*.hipo rf/
mv rat_* trigger/
mv ctof/*m2* particle_mass_ctof_and_ftof/
mv ftof/*m2* particle_mass_ctof_and_ftof
mv band_* band/
mv rich_* rich/
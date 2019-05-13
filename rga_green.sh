#!/usr/bin/bash


#environment
export pdir=`pwd`
export groovy=$pdir"/../.groovy/coatjava/bin/run-groovy"

#Green Cells
out_monitor=(trig_Km_num trig_Kp_num trig_elec_num trig_pim_num trig_pip_num trig_prot_num trig_muon_num \
Forward_Tracking_EleVz Forward_Tracking_Negchi2 Forward_Tracking_NegVz \
Forward_Tracking_Poschi2 Forward_Tracking_PosVz CVT_chi2_elec EC_Sampl EC_gg_m \
HTCC_nphe_sector LTCC_nphe_sector)
out_TOF=(FTOF_time DC_residuals_sec DC_residuals_sec_sl)
#out_CTOF=(CTOF_time)
out_FT=(FTC_pi0_mass FTC_time_charged FTC_time_neutral FTH_MIPS_energy FTH_MIPS_time)
out_FT=(FTH_MIPS_time_board FTH_MIPS_energy_board)
out_monitor=(Forward_Tracking_PosVz)
#out_TOF=(FTOF_time)
cd rga_output

#out_monitor=(trig_muon_num EC_gg_m HTCC_nphe_sector)
#for name in out_monitor out_TOF out_FT
for name in out_FT
do
	var=$name[@]
	for script in ${!var}
	do
		#$groovy ../groovy_codes/$script.groovy `find /volatile/clas12/rga/pass0/monitoring/plots* -name "$name*"` #rga
	$groovy ../groovy_codes/$script.groovy `find /work/clas12/rg-a/data/monplots/pass0/v0/plots* -name "$name*" ! -name "*5590*"` #rga
	done
done

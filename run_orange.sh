#!/usr/bin/bash


#environment
export pdir=`pwd`
export groovy=$pdir"/../.groovy/coatjava/bin/run-groovy"

#Orange Cells
out_monitor=(trig_pos_num trig_neg_num trig_neu_num \
BMT_Occupancy BMT_OnTrkLayers BST_Occupancy BST_OnTrkLayers CVT_chi2norm CVT_ndf CVT_trks_neg_rat CVT_trks_neg CVT_trks_pos_rat CVT_trks_pos CVT_trks CVT_p CVT_pt CVT_pathlen \
central_Km_num central_Kp_num central_pim_num central_pip_num central_prot_num \
Forward_Tracking_Elechi2 \
CVT_Vz_negative CVT_Vz_positive CVT_chi2_neg CVT_chi2_pos \
RFtime_diff RFtime_elec RFtime_pim RFtime_pip EC_pim_time EC_pip_time)
dst_mon=(FTOF_time_p1a_elec FTOF_time_p1a_pion FTOF_time_p1b_elec FTOF_time_p1b_pion FTOF_time_p2 \
FTOF_edep_p1a_elec FTOF_edep_p1a_pion FTOF_edep_p1b_elec FTOF_edep_p1b_pion FTOF_edep_p2 \
FTOF_m2_p1a_pim FTOF_m2_p1a_pip FTOF_m2_p1a_prot FTOF_m2_p1b_pim FTOF_m2_p1b_pip FTOF_m2_p1b_prot)
out_CND=(CND_beta_neu CND_zdiff CND_time_neg_vtP CND_MIPS_dE_dz) 
out_CTOF=(CTOF_time_negative CTOF_time_positive \
CTOF_m2_pim CTOF_m2_pip CTOF_m2_prot CTOF_time_pim CTOF_edep_pim)

dst_mon=(FTOF_m2_p1a_pim FTOF_m2_p1a_pip FTOF_m2_p1a_prot FTOF_m2_p1b_pim FTOF_m2_p1b_pip FTOF_m2_p1b_prot)

out_HTCC=(HTCC_nphe_ring_sector)
out_TOF=(DC_tmax_sec_sl)

cd groovy_output
#for name in out_monitor dst_mon out_CND out_HTCC out_TOF #out_CTOF
for name in dst_mon
do
	var=$name[@]
	for script in ${!var}
	do
		#$groovy ../groovy_codes/$script.groovy `find /volatile/clas12/rga/pass0/monitoring/plots* -name "$name*"` #rga
		 $groovy ../groovy_codes/$script.groovy `find /work/clas12/rg-b/offline_monitoring/plots{6296,6302,6335,6363,6383,6426,6445,6476,6501,6523,6524} -name "$name*"` #rgb
#		echo `find ~/run_based_monitoring/ana_output/plots* -name "$name*"`
	done
done

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
FTOF_edep_p1a_elec FTOF_edep_p1a_pion FTOF_edep_p1b_elec FTOF_edep_p1b_pion FTOF_edep_p2)
#\
#FTOF_m2_p1a_pim FTOF_m2_p1a_pip FTOF_m2_p1a_prot FTOF_m2_p1b_pim FTOF_m2_p1b_pip FTOF_m2_p1b_prot)
out_CND=(CND_zdiff CND_time_neg_vtP CND_MIPS_dE_dz) 
#out_CTOF=(CTOF_time_negative CTOF_time_positive \
out_CTOF=(CTOF_m2_pim CTOF_m2_pip CTOF_m2_prot CTOF_time_pim CTOF_edep_pim)
#dst_mon=(FTOF_m2_p1a_pim FTOF_m2_p1a_pip FTOF_m2_p1a_prot FTOF_m2_p1b_pim FTOF_m2_p1b_pip FTOF_m2_p1b_prot)
out_HTCC=(HTCC_nphe_ring_sector)
out_TOF=(DC_tmax_sec_sl)
#out_CTOF=(CTOF_time_negative CTOF_time_positive)
cd rga_output

out_monitor=(RFtime_pip)

#for name in out_monitor dst_mon out_CND out_HTCC out_TOF out_CTOF
for name in out_monitor 
do
	var=$name[@]
	for script in ${!var}
	do
		#$groovy ../groovy_codes/$script.groovy `find /volatile/clas12/rga/pass0/monitoring/plots* -name "$name*"` #rga 
		if [ "$script"=="RFtime_pip" ];then
		        $groovy ../groovy_codes/$script.groovy `find /work/clas12/rg-a/data/monplots/pass0/v0/plots* -name "$name*" ! -name "*4989*" ! -name "*5235*" ! -name "*5346*" ! -name "*5349*" ! -name "*5482*" ! -name "*5548*" ! -name "*6654*" ! -name "*6683*" ! -name "*6694*" ! -name "*6695*" ! -name "*6730*" ! -name "*6748*" ! -name "*6769*"` #rga

		else
		        $groovy ../groovy_codes/$script.groovy `find /work/clas12/rg-a/data/monplots/pass0/v0/plots* -name "$name*"` #rga
		fi
	done
done

#!/usr/bin/bash


#environment
export pdir=`pwd`
export groovy=$pdir"/../.groovy/coatjava/bin/run-groovy"

#Orange Cells

out_CTOF=(CTOF_m2_pim CTOF_m2_pip CTOF_time_pim CTOF_edep_pim)

out_CTOF=(CTOF_time_negative CTOF_time_positive \
CTOF_m2_pim CTOF_m2_pip CTOF_time_pim CTOF_edep_pim)

dst_mon=(FTOF_time_p1a_elec FTOF_time_p1a_pion FTOF_time_p1b_elec FTOF_time_p1b_pion FTOF_time_p2) 
# \
#FTOF_edep_p1a_elec FTOF_edep_p1a_pion FTOF_edep_p1b_elec FTOF_edep_p1b_pion FTOF_edep_p2)
#out_TOF=(DC_tmax_sec_sl)
cd groovy_output
#for name in out_monitor out_TOF out_FT
#out_CTOF=(CTOF_edep_pim)
out_FT=(FTH_MIPS_energy_board FTH_MIPS_time_board)
#dst_mon=(FTOF_time_p1b_pion)
for name in out_CTOF 
do
	var=$name[@]
	for script in ${!var}
	do
		#$groovy ../groovy_codes/$script.groovy `find /volatile/clas12/rga/pass0/monitoring/plots* -name "$name*"` #rga
		 $groovy ../groovy_codes/$script.groovy `find ~/run_based_monitoring/plots{6296,6302,6335,6363,6383,6426,6445,6476,6489,6501,6523,6524} -name "$name*"` #rg
#		echo `find ~/run_based_monitoring/ana_output/plots* -name "$name*"`
	done
done

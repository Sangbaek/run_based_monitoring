#!/usr/bin/bash


#environment
export pdir=`pwd`
export groovy=$pdir"/../.groovy/coatjava/bin/run-groovy"

#Orange Cells
out_CTOF=(CTOF_time_negative CTOF_time_positive)
#out_TOF=(DC_tmax_sec_sl)
cd groovy_output
#for name in out_monitor out_TOF out_FT
for name in out_CTOF #out_TOF
do
	var=$name[@]
	for script in ${!var}
	do
		#$groovy ../groovy_codes/$script.groovy `find /volatile/clas12/rga/pass0/monitoring/plots* -name "$name*"` #rga
		 $groovy ../groovy_codes/$script.groovy `find ~/run_based_monitoring/plots{6296,6302,6335,6363,6383,6426,6445,6476,6501,6523,6524} -name "$name*"` #rg
#		echo `find ~/run_based_monitoring/ana_output/plots* -name "$name*"`
	done
done

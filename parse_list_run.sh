#!/bin/bash

#set environment
# export groovy_name="trig_elec_num"
# export hipo="out_monitor"
export pdir=`pwd`
export groovy_input=""
export groovy_output=$pdir"/groovy_output"
# export groovypath=$pdir"/../clas12-offline-software/coatjava/bin/run-groovy" #for my local
export groovypath=$pdir"/../.groovy/coatjava/bin/run-groovy"
export listpath=$pdir"/filelists"
export javapath=$pdir
export ana_out=$pdir"/ana_output"
export run_count=0

export a=true
echo "compiling java? y or n"
while $a
do
	read -r compile_java
	if [ "$compile_java" = "y" ]
	then
		echo "compiling java.."
		rm *.class
		javac -cp "$COATJAVA/lib/clas/*:$COATJAVA/lib/utils/*:." ana_2p2.java
		export a=false
	elif [ "$compile_java" = "n" ]
	then
		echo "skipping compiling java.."
		export a=false
	else
		echo "please type y or n."
	fi
done


cd $ana_out

echo -e "\nrun ana_2p2?  y or n"
export a=true
while $a
do
	read -r run_ana
	if [ "$run_ana" = "y" ]
	then
	        echo "executing ana_2p2.."
	        export a=false
	elif [ "$run_ana" = "n" ]
	then
	        echo "skipping compiling java.."
					export a=false
	else
	        echo "please type y or n."
	fi
done

echo "test"
# while IFS="|" read run_num Eb;do
#     #echo $run_num $Eb #for debugging
# 	mkdir -p plots$run_num
# 	if [ "$run_ana" = "y" ]
# 	then
# 		if [ ! -f plots$run_num/out_CND_$run_num.hipo ] || [ ! -f plots$run_num/out_CTOF_$run_num.hipo ] || [ ! -f plots$run_num/out_HTCC_$run_num.hipo ] || [ ! -f plots$run_num/out_monitor_$run_num.hipo ];then
# 			java -DCLAS12DIR="$COATJAVA" -cp "$COATJAVA/lib/clas/*:$COATJAVA/lib/utils/*:.":"$javapath" ana_2p2 $run_num $listpath/list$run_num.txt 100000000 $Eb
# 		else
# 			echo "hipo file exists.. skipping monitoring for run $run_num"
# 		fi
# 	fi
# 	export groovy_input="$groovy_input $ana_out/plots$run_num/out_hiponame_$run_num.hipo"
# 	run_count=$((run_count+1))
# #done < $filename
# #done < $listpath/list_run2.txt
# done < $listpath/list_run.txt
#
# if [ "$run_count" == "0" ]
# then
# 	echo "list of run numbers is empty"
# 	exit 1
# fi
#
# echo -e "\nnumber of runs: $run_count"
# cd $pdir
#
# mkdir -p groovy_output
# cd groovy_output
#
# echo -e "\nfrom hipo to timeline..\n"
#
# while IFS="|" read groovy_name	hipo;do
# 	export run_groovy="$groovypath $pdir/groovy_codes/$groovy_name.groovy $groovy_input"
# 	export loop_count="0"
# 	while [ "$loop_count" -lt "$run_count" ]
# 	do
# 		export run_groovy="${run_groovy/hiponame/$hipo}"
# 		loop_count=$((loop_count+1))
# 	done
# 	#echo $run_groovy
# 	$run_groovy
# done < $listpath/list_groovy.txt
#
# echo -e "\ncheck $groovy_output"

#!/bin/bash

#set environment
export pdir=`pwd`
export groovy_output=$pdir"/groovy_output"
# export groovypath=$pdir"/../clas12-offline-software/coatjava/bin/run-groovy" #for my local
export groovypath=$pdir"/../.groovy/coatjava/bin/run-groovy"
export listpath=$pdir"/filelists"
export javapath=$pdir
export ana_out=$pdir"/ana_output"
export offline_monitoring="/work/clas12/rg-b/offline_monitoring"
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
	        echo "skipping analysis.."
					export a=false
	else
	        echo "please type y or n."
	fi
done

while IFS="|" read run_num Eb;do
    #echo $run_num $Eb #for debugging
	if [ "$run_ana" = "y" ]
	then
		mkdir -p plots$run_num
		if [ ! -f $listpath/list$run_num.txt ]
		then
			ls /work/clas12/rg-b/production/recon/pass0/v1/mon/00$run_num/* > $listpath/list$run_num.txt
		fi
		if [ ! -f plots$run_num/out_CND_$run_num.hipo ] || [ ! -f plots$run_num/out_CTOF_$run_num.hipo ] || [ ! -f plots$run_num/out_HTCC_$run_num.hipo ] || [ ! -f plots$run_num/out_monitor_$run_num.hipo ];then
			java -DCLAS12DIR="$COATJAVA" -cp "$COATJAVA/lib/clas/*:$COATJAVA/lib/utils/*:$javapath" ana_2p2 $run_num $listpath/list$run_num.txt 100000000 $Eb
		else
			echo "hipo file exists.. skipping monitoring for run $run_num"
		fi
	fi
	export groovy_input="$groovy_input $ana_out/plots$run_num/out_hiponame_$run_num.hipo"
	export groovy_input2="$groovy_input2 $offline_monitoring/plots$run_num/out_hiponame_$run_num.hipo"
	run_count=$((run_count+1))
#done < $listpath/list_run2.txt
done < $listpath/list_run.txt

if [ "$run_count" == "0" ]
then
	echo "list of run numbers is empty"
	exit 1
fi

echo -e "\nnumber of runs: $run_count"
cd $pdir

echo "make timeline? y or n\n"

export a=true
while $a
do
	read -r run_timeline
	if [ "$run_timeline" = "y" ]
	then
	        echo "executing timeline codes.."
	        export a=false
	elif [ "$run_timeline" = "n" ]
	then
	        echo "skipping timeline.."
					export a=false
	else
	        echo "please type y or n."
	fi
done

if [ "$run_timeline" == "y" ]
then
	mkdir -p groovy_output
	cd groovy_output

	echo -e "\nfrom hipo to timeline..\n"
	while IFS="|" read groovy_name	hipo;do
		export run_groovy="$groovypath $pdir/groovy_codes/$groovy_name.groovy $groovy_input"
		export run_groovy2="$groovypath $pdir/groovy_codes/$groovy_name.groovy $groovy_input2"
		export loop_count="0"
		while [ "$loop_count" -lt "$run_count" ]
		do
			export run_groovy="${run_groovy/hiponame/$hipo}"
			export run_groovy2="${run_groovy2/hiponame/$hipo}"
			loop_count=$((loop_count+1))
		done
		#echo $run_groovy
		if [ "$hipo" == "FT" ] || [  "$hipo" == "TOF" ]
		then
			$run_groovy2
			#echo $run_groovy2
		else
			$run_groovy
		fi
	done < $listpath/list_groovy.txt

	echo -e "\ncheck $groovy_output"
fi

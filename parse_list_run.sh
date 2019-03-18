#!/bin/bash


#set environment
# export groovy_name="trig_elec_num"
# export hipo="out_monitor"
export groovy_input=""
export groovy_output=`pwd`"/groovy_output"
# export groovypath="../../clas12-offline-software/coatjava/bin/run-groovy" //for my local
export groovypath="~/.groovy/coatjava/bin/run-groovy"
export listpath=`pwd`"/filelists"
export javapath=`pwd`

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

echo "run ana_2p2?  y or n"
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

while IFS="	" read run_num Eb;do
    #echo $run_num $Eb #for debugging
	if [ ! -f plots$run_num/out_CND_$run_num.hipo ] || [ ! -f plots$run_num/out_CTOF_$run_num.hipo ] || [ ! -f plots$run_num/out_HTCC_$run_num.hipo ] || [ ! -f plots$run_num/out_monitor_$run_num.hipo ];then
		java -DCLAS12DIR="$COATJAVA" -cp "$COATJAVA/lib/clas/*:$COATJAVA/lib/utils/*:.":"$javapath" ana_2p2 $run_num $listpath/list$run_num.txt 100000000 $Eb
	else
		echo "hipo file exists.. skipping monitoring for run $run_num"
	fi
	export groovy_input="$groovy_input ../plots$run_num/out_hiponame_$run_num.hipo"
#done < $filename
done < list_run2.txt
# done < $listpath/list_run.txt

mkdir -p groovy_output
cd groovy_output

echo "from hipo to timeline..\n"

while IFS="|" read groovy_name	hipo;do
	# export run_groovy="~/.groovy/coatjava/bin/run-groovy groovy_codes/$groovy_name.groovy ${groovy_input/hiponame/$hipo}"
	$groovypath ../groovy_codes/$groovy_name.groovy ${groovy_input/hiponame/$hipo}
done < $listpath/list_groovy.txt

#!/bin/bash

#echo "Enter filename"
#read -r filename
export groovy_input=""
export hipo="out_monitor"
while IFS="	" read run_num Eb;do
    #echo $run_num $Eb #for debugging
	export groovy_input="$b plots$run_num/$hipo""_$run_num.hipo"
#done < $filename
done < list_run.txt

echo "compiling java? y or n"
read -r compile_java
if [ $compile_java = "y" ]
then
	echo "compiling java.."
	rm *.class
	javac -cp "$COATJAVA/lib/clas/*:$COATJAVA/lib/utils/*:." ana_2p2.java
else [ $compile_java = "n" ]
then
	echo "skipping compiling java.."
else
	echo "please say yes or no, skipping compiling java.."
fi

echo "run ana_2p2?  y or n"
read -r run_ana
if [ $compile_java = "y" ]
then
        echo "executing ana_2p2.."
        rm *.class
        javac -cp "$COATJAVA/lib/clas/*:$COATJAVA/lib/utils/*:." ana_2p2.java
else [ $compile_java = "n" ]
then
        echo "skipping compiling java.."
else
        echo "please say yes or no, skipping compiling java.."
fi


echo $b

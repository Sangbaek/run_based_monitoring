#!/bin/sh

#Output directory names
rungroup="rgb"
cookver="pass0v25.18"
out_dir=$rungroup"_"$cookver
binDir=`dirname $0`
export binDir=`realpath $binDir`
inputdir=`realpath $1`
mkdir $out_dir
cd $out_dir

#subdirectory names
#bmtbst central cnd ctof cvt dc ec forward ft ftof htcc ltcc rf trigger
for dir in log band bmtbst central cnd ctof cvt dc ec forward ft ftof htcc ltcc rf trigger particle_mass_ctof_and_ftof rich epics
do
  mkdir -p "$dir"
done

#JAVA_OPTS="-Dsun.java2d.pmoffscreen=false -Xms1024m -Xmx12288m"; export JAVA_OPTS

find $binDir/../src/main/java/org/jlab/clas/timeline/timeline -name "*.groovy" -print0 | xargs -I{} -0 -n1 --max-procs 4 bash -c '
java -cp $binDir/../target/timelineMon-1.0-SNAPSHOT.jar org.jlab.clas.timeline.run_rgb $@ >& log/`basename $1`.log' -- {} $inputdir


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
mv epics_* epics/

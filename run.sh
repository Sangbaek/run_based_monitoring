#!/usr/bin/bash

#Environment Initialization
#for output coordinating
#Change the location of groovy accordingly.
export groovy="$COATJAVA/bin/run-groovy"

#Output directory names
rungroup="rga"
cookver="pass0v2.2.10"
out_dir=$rungroup"_"$cookver
mkdir $out_dir
cd $out_dir

#subdirectory names
#bmtbst central cnd ctof cvt dc ec forward ft ftof htcc ltcc rf trigger
for dir in band bmtbst central cnd ctof cvt dc ec forward ft ftof htcc ltcc rf trigger particle_mass_ctof_and_ftof rich
do
  mkdir -p "$dir"
done

JYPATH=$JYPATH:../groovy_codes $groovy ../run.groovy $@

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


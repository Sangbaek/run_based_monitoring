#!/bin/bash

if [[ $# -lt 1 ]]
then
	echo "USAGE: $0 -log Title name"
	exit 111
fi

[[ $1 == "-log" && $# -eq 1 ]] && echo "Specify your title!" && exit 111
[[ $1 == "-log" && $# -gt 1 ]] && shift && echo "Your title line is: Offline: $@" || exit 111

echo "Writing to Logbook, you have 2 seconds to press Ctrl-C if you want to stop it:"

for i in `seq 2 -1 1`
do
	echo "$i second[s] left..."
	sleep 1
done &&


#/site/ace/certified/apps/bin/logentry -l HBLOG -l CLAS12ANA -t "Offline: $*" \

/site/ace/certified/apps/bin/logentry -l CLAS12ANA -t "Offline: $*" \
                                      -c "electron overview" -a plots/e_rec_mon.png \
				      -c "physics kinematical coverage" -a plots/e_phys.png \
				      -c "electron trigger per sector" -a plots/trig_sect.png \
				      -c "electron per sector" -a plots/e_sects.png \
				      -c "electron phi distributions in theta bins per sector" -a plots/e_phi_sects.png \
				      -c "electron per sector projections" -a plots/e_sects_proj.png \
				      -c "electron detector positions" -a plots/e_pos_sects.png \
				      -c "electron detector acceptance" -a plots/e_ratio_sects.png \
				      -c "photon pairs in ECAL" -a plots/gg.png \
				      -c "DC tracks overview" -a plots/dc_rec_mon.png \
				      -c "DC positive tracks" -a plots/dc_p_vz_phi.png \
				      -c "DC negative tracks" -a plots/dc_m_vz_phi.png \
				      -c "DC residuals vs DOCA" -a plots/DC_resd_trkDoca.png \
				      -c "DC residuals" -a plots/DC_resd.png \
				      -c "DC Time" -a plots/DC_time.png \

/site/ace/certified/apps/bin/logentry -l CLAS12ANA -t "Offline: $*" \
				      -c "TOF vertex times per charge per sector" -a plots/TOF.png \
				      -c "TOF calibration" -a plots/TOF_cal.png \
				      -c "DST FTOF 1A Mass Distributions" -a plots/dst_FTOF1A_mass.png \
				      -c "DST FTOF 1B Mass distributions" -a plots/dst_FTOF1B_mass.png \
				      -c "HTCC electron selection" -a plots/HTCC_e.png \
				      -c "HTCC electron nphe" -a plots/HTCC_nphe.png \
				      -c "HTCC electron ADC spectra" -a plots/HTCC_adc.png \
				      -c "overview of CVT tracks" -a plots/cvt.png \
				      -c "Forward Tagger" -a plots/FT.png \
				      -c "CTOF timing with SVT matching" -a plots/central.png \
				      -c "CND timing with SVT matching" -a plots/cnd.png \
				      -c "BST occupancies" -a plots/bst_occ.png \
				      -c "BMT occupancies" -a plots/bmt_occ.png \
				      -c "Barrel crosses" -a plots/barrel_crosses.png \
				      -c "e pip coincidence" -a plots/e_pip.png \
				      -c "e pip pim coincidence" -a plots/two_pions.png \

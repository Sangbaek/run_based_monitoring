# timeline monitoring

A set of codes that read output files of [offline monitoring](https://github.com/JeffersonLab/clas12_monitoring) to create timeline monitoring.

## code structures

Individual processes are sorted in src/main/java/org/jlab/clas/timeline/timeline directory as groovy classes.
Fitting procedures are stored in ../fitter separately.

## How to compile them
```
mvn clean package
```
or,
```
/apps/maven/PRO/bin/mvn clean package   
```
in ifarm.

## How to run them

After compile, run timeline monitoring with

```
./bin/run.sh /path/to/monitoring/files
```

## For rg-b

Add following lines inside engines of run.groovy for rg-b.

1. add this inside out_CTOF

```groovy
    , new particle_mass_ctof_and_ftof.ctof_m2_pim()
    , new particle_mass_ctof_and_ftof.ctof_m2_pim()
```

2. add these lines after out_RICH

```groovy
  , out_BAND: [new band.band_adccor(),
    new band.band_lasertime(),
    new band.band_meantimeadc(),
    new band.band_meantimetdc()],

  dst_mon: [new particle_mass_ctof_and_ftof.ftof_m2_p1a_pim(),
    new particle_mass_ctof_and_ftof.ftof_m2_p1a_pip(),
    new particle_mass_ctof_and_ftof.ftof_m2_p1a_prot(),
    new particle_mass_ctof_and_ftof.ftof_m2_p1b_pim(),
    new particle_mass_ctof_and_ftof.ftof_m2_p1b_pip(),
    new particle_mass_ctof_and_ftof.ftof_m2_p1b_prot()],
```

## Contact

Sangbaek Lee, sangbaek@mit.edu
# timeline monitoring

Some groovy scripts maintained by Sangbaek Lee (sangbaek@mit.edu) for CLAS12 timeline monitoring. Scripts are based on initial works of Andrey Kim.

These groovy scripts will read offline monitoring codes that was initiated by FX, and later developed by Yordanka Illieva, Andrey Kim, and Sangbaek Lee.

## code structures

Individual processes are sorted in groovy_codes directory as groovy classes.
Fitting procedures are stored in groovy_codes/fitter separately.
[run.groovy](run.groovy) will run all scripts.

## How to run them

```bash
./run.sh `find /path/to/offline/monitoring/hipo/files -name "*.hipo"`
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

## For offline use (Mac OS X)

At [run.sh](run.sh), switch existing shebang line to
```bash
#!/bin/bash
```
. Also use gtimeout at [run.sh](run.sh) instead of timeout.
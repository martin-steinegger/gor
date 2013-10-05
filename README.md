# GOR I, III, IV, V java implementation   #

Prediction of secondary structure. 

## Parameter ##
Prediction 
Option                                  Description                            
------                                  -----------                            
--format                                txt|html                               
--maf <File>                            path to directory with .aln files (GOR 
                                          V)                                   
--model <File>                          path to fileoutput                     
--probabilities                         include the probabilities in the       
                                          output (0-9, coloring in html)       
--seq [File]                            path to dssp training file             

Training
Option                                  Description                            
------                                  -----------                            
--db <File>                             path to dssp training file             
--method                                <gor1|gor3|gor4>                       
--model <File>                          path to fileoutput                     


## Example ## 
GorTrainRunner --db data/GOR/CB513DSSP.db  --model testoutput/gor4_jar.txt --method gor4
GorPredictRunner --format txt --model testoutput/gor4_jar.txt --maf data/GOR/CB513MultipleAlignments/ --probabilities

# City bike clustering Application

## Description
This application enable to cluster CityBike‘s stations basing on geographical coordinated

## Project management
### User stories
#### Description
As a bicylce station manager, I need to have a clustering view of a set of station. 
 * At any time I can define the number of group I need in my cluster.
 * The clustering is based on geograhical coordinated
#### acceptance criteria
    To fill with some  acceptance test case by the PO
    
#### Task
 * **Task 1** : Application design : The goal of this task is to design the application basing on the platform 
                we have in company IT. If need, we can write some text and draw some diagram ( layer, sequence...).             
 * **Task 2** : Implémentation : développing the application. If we have many members in the team 
 we can divide this task as small as possible
   * **Task 2-1** GIT repository initialisation with Maven project => done 
   * **Task 2-2** Model and interface implementation and unit test => done ( without unit test ) 
   * **Task 2-3** Service implementation => done 
   * **Task 2-4** Configuration loader implementation => done 
   * **Task 2-5** Project packaging configuration => done 
   * **Task 2-6** Automatic Integration test implementation => to do
   * **Task 2-7** Job schedulong configuration (with oozie or crontab) => to do
    
 
 * **Task 3** : QA test 


## Functionnal description

The application get as input 
 * **data source**: containing for each station coordinated (latitude, longitude) and other informations related to the bicyle station.
 * **Number of cluster** : The number of cluster that we need to do
 * **Output file** : The file containing the input data clustered. For each station the ***cluster*** field indicates the id of the cluster in in which it's. The  cluster Ids is from 0 to number of cluster.
 

## Technical description 
 
The application is implemented with spark framework with scala API. Here we use two spark component :
 * spark-sql : to load data, some data processing, saving data
 * spark-mlib : to clustering task
 
In the application we have three services :
 * ***InputReader*** : This service is responsible of reading input data. We have as much implementation as the input format
 we need to deal with. We just have Json format inplemenantion ( see : JsonInputReader)
 * ***OuputWriter*** : This service is responsible of writing the result of clustering using output format. We have as much implementation as the input format
 we need to deal with. We just have Json format inplemenantion ( see : JsonInputReader)
 * ***ModelService*** : This service is responsible of loading existing model, generating a new one, apply input data to a model.

## Package description

In the package we have the following files or directories :
 
1. jars
This directory contains the application jars with all dependencies ( it's an uber jar)

2. model
This directory contains the serialized java model object which we have built with the sample. This model contains 6 groups. 

3. sample 
This directory contains the input file to generate the model in/Brisbane_CityBike.json and the clustered data in file out/Brisbane_CityBike_clustered.json
In this file, for each record you will find a **cluster field** containing the id of the group.

4. conf 
Contains configuration file : application-prod.properties.
In this file we have the following properties 

properties | description | example 
--- | --- | ---
sodexo.clustering.inputPah | The input path or table name of input data | sample/in/Brisbane_CityBike.json
sodexo.clustering.outputPah | The output path or table name of output data | sample/in/Brisbane_CityBike_clustered.json
sodexo.clustering.outputFormat | The output format of input data | JSON
sodexo.clustering.outputFormat | The output format of output data | JSON
sodexo.clustering.percentageRetrain | If over 0  the model is retained with the subset input data. If equal to 0 the last generated model is used to cluster input data. | 0
sodexo.clustering.modelDirPath | The directory which contains model serialized object | model
sodexo.clustering.numCluster | The number of cluster used if the model is retained | 6



## Installation and running
### Package downloading
  You can find the package [here](https://www.dropbox.com/s/fmpqb6umkpr55pj/city-bike-clustering-1.0-SNAPSHOT.zip?dl=0)
  
### Installation  
* 1 - Deploy the package in the platform (file system) where we need to run the application. For example if in hadoop deploy to hdfs
* 2 - Update the configuration basing on the location of the package in the platform

### Running
* 3 - launch the application by running the following command from the package directory:
```shell
spark-submit --class com.sodexo.clustering.Application   --files conf/application-prod.properties  --master  <master-url> --deploy-mode cluster jars/city-bike-clustering-with-dependencies.jar -e prod
```

### Job scheduling
The job can be scheduled using OOZIE tool in hadoop platform. The task will be to a oozie coordinator linked to a coordinator qith an spark action.




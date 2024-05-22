# Hint generator
This project generates runtimeHints for spring native application.
The generated hint class *needs to be* refined by hand. 
But it is a good starting point for figuring out which classes still missing for the native application.

##  Mode hint generator diff
The mode uses the different between reflection json meta infos. 
Which are collected by a jdk agent run of an application and generated json from spring aot.

- Write hintsgenerator.execute=diff to application.properties

##  Mode hint generator singleFile
The mode generates from the defined reflection json a sprint hint class.

- Write hintsgenerator.execute=singleFile to application.properties


##  Mode hint generator folder
The mode generate a sprint hint class from defined folder names and the path to the source code. 

- Write hintsgenerator.execute=folder to application.properties



## Get Started
- Select the type of generator in the application.properties
- Define the right path and names in the application.properties

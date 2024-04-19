# Hint generator
This project generates runtimeHints for spring native application.

The program uses the different between reflection json meta infos. Which are collected by a jdk agent run of an application and generated json from spring aot.
The generated hint class *needs to be* refined by hand. But it is a good starting point for figuring out which classes still missing for the native application. 

## Get Started
Define the right Path of the agent META-INFO folder and spring META-INFO.

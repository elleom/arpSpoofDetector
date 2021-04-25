# arpSpoofDetector
## Proyecto final Desarrollo de Aplicaciones Muliplataforma (D.A.M.) / Final Project Soft-development AP
<img src="https://github.com/elleom/arpSpoofDetector/blob/main/ARP.jpg" width="150" height="150" alt="ARP Spoof Detector logo"/>


### **Standalone JavaFX Application**

#### Technologies used:
  * Java 8
  * FavaFX
  * SceneBuilder
  * Wix Tool Set (Build)

#### Dependencies
  * Linux
    * net-tools `apt install net-tools`
  * MAC LookUp: https://maclookup.app/api_doc  
  

### Instructions:

1. Build
  * Install Wix ToolsSet
  * configure build.xml
    * content
    * '<target name="-post-jfx-deploy">
       <fx:deploy width="${javafx.run.width}" height="${javafx.run.height}"
        nativeBundles="all"
        outdir="${basedir}/${dist.dir}" outfile="${application.title}">
        <fx:application name="${application.title}"
        mainClass="${javafx.main.class}"/>
       <fx:resources>
      <fx:fileset dir="${basedir}/${dist.dir}"
      includes="*.jar"/> 
      <fx:fileset dir="${basedir}/${dist.dir}" />
      </fx:resources>
    <fx:info title="${application.title}"
    vendor="${application.vendor}"/>
    </fx:deploy>
    </target>'
 * buld on IDE 


##### Process Documentation (Spanish Only)

https://github.com/elleom/arpSpoofDetector/blob/main/DAM_M13_Memoria_ProyectoFinCiclo_LeonardoMartins_ARP_SpoofDetector.pdf

##### Main
![screen 1](https://github.com/elleom/arpSpoofDetector/blob/main/screenshot1.png "ScreenShot 1")

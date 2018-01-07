# FinVision
Financial Modeling GUI project that aims to use Java FX, R, and MySQL. The goal is to combine the statistical capabilities of the R programming language with the UI power of the Java programming language, while also incorporating MySQL functionality.

Download the project and follow the configuration steps as stated below:

Configuration:
This project connects Java and R using rJava/JRI. Development tools used: Windows, Java, Eclipse IDE, MySQL, R/RStudio. Download these tools in order to run project properly.

rJava must first be downloaded using the R workbench. This project uses 64-bit installations. On the R workbench use the following command to install the rJava package:
install.packages("rJava") 
*Note: keep track of where these files are installed on your machine, it will be needed for Eclipse plugin configuration

The Eclipse IDE must be configured to use the rJava plugin as well as the JDBC plugin for MySQL. First install the rJava plugin:

*Follow the installation procedure at http://www.studytrails.com/rjava-eclipse-plugin/rjava-eclipse-plugin/ 
Assuming the above steps were followed correctly, rJava/JRI should be configured and ready to use.

Refer to https://www.mysql.com/ for specifics regarding MySQL installation on your respective machine. Make sure the JDBC plugin is also installed as this needs to be added to the build path of the project in order for it to run. Again, refer to the official MySQL documentation for specifics.

One last important note is that for the purposes of database security, the "Credentials.txt" file used in the DBConnector class is not included in this repository. If you want to run this project, create a file with the same name and save to src/application of the project and have its content exactly as follows:

jdbc:mysql://YOUR EXACT MYSQL/DATABASE URL (e.g jdbc:mysql://localhost/finvision) '\n'
YOUR USERNAME FOR ABOVE DATABASE '\n'
YOUR PASSWORD FOR ABOVE DATABASE  

*Take note of the newline characters and translate accordingly for the Credentials file (there should be 3 lines total in the file). 

Once this is all done you can run the project on Eclipse or export the project as an executable JAR



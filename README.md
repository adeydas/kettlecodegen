KETTLE CODE GENERATOR
=====================

Author: Abhishek Dey Das

Contact: http://abhi.am

License: LGPL

Description:

The code generator is for generating boiler plate code for a Pentaho Kettle Step. This is especially handy if you are writing several plugins and just want to concentrate on the business logic 
and not the boilerplate code that is required to make a Kettle plugin work. Below are a few screenshots:

![App GUI](http://abhi.am/wp-content/uploads/2014/11/Screen-Shot-2014-11-09-at-5.16.58-PM.png)

![Auto generated Code](http://abhi.am/wp-content/uploads/2014/11/Screen-Shot-2014-11-09-at-5.18.34-PM.png)

How to use it
=============

Clone the code.

Open up pom.xml and replace the SWT library for your architecture.

Run the package goal.

If you are on OSX, make sure to run the jar with the -XstartOnFirstThread flag to avoid "Invalid thread access" error.

Managing dependencies using S3
==============================

The generator now has a feature where you can upload your dependencies to two S3 buckets (for libext and libswt) and it can download and 
copy the artifacts while generating code. To do that put a file called config.json in the same directory as the jar, containing the AWS configuration in the following format:

{
	"accessKey" : "xxxx",
	"secretKey" : "xxxx",
	"libext" : "bucketname",
	"libswt" : "bucketname"
}

This step is optional and if the program can't find the configuration file, it will ignore that step.

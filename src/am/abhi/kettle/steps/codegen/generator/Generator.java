package am.abhi.kettle.steps.codegen.generator;

import java.io.File;
import java.io.PrintWriter;

public class Generator {
	private String path;
	private GenDatatype dt;
	
	public void startGenerator(GenDatatype dt) throws Exception {
		String path = dt.getSaveto();
		if (path.isEmpty()) {
			throw new Exception("Save path cannot be empty");
		}
		this.path = path;
		this.dt = dt;
		
		createDirs();
		createBuildXml();
	}
	
	//Create build.xml
	private void createBuildXml() throws Exception {
		String tempPath = this.path + File.separator + "build.xml";
		PrintWriter out = new PrintWriter(new File(tempPath));
		
		out.println("<project name=\"" + dt.getStepname().trim() + "\" default=\"default\" basedir=\".\">");
		
		out.println();
		
		out.println("\t<description>");
		out.println("\t\t" + dt.getDescription());
		out.println("\t</description>");
		
		out.println();
		
		out.println("<tstamp prefix=\"start\"/>");
		
		out.println();
		
		out.println("<!-- system properties to use -->");
		out.println("<property name= \"cr\" value=\"${line.separator}\"/>");
		
		out.println();
		
		out.println("<!-- set global properties for this build -->");
		out.println("<property name=\"src\"        			location=\"src\"/>");
		out.println("<property name=\"classes\"        		location=\"classes\"/>");
		out.println("<property name=\"lib\"        			location=\"lib\"/>");
		out.println("<property name=\"distrib\"        		location=\"distrib\"/>");
		out.println("<property name=\"libswt\"     			location=\"libswt\"/>");
		out.println("<property name=\"deploydir\"     		location=\"Deploy\"/>");
		out.println("<!-- These properties can be used for debugging with Kettle -->");
		out.println("<property name=\"kettledir\"				location=\"kettle\"/>");
		out.println("<property name=\"kettleexecutable\"		location=\"kettle/Spoon.bat\"/>");
		
		out.println();
		
		out.println("<target name=\"init\" description=\"create timestamp and directories\">");
		out.println("\t<echo>Init...</echo>");
		out.println("\t<tstamp/>");
		out.println("\t<mkdir dir=\"${classes}\"/>");
		out.println("\t<mkdir dir=\"${lib}\"/>");
		out.println("\t<mkdir dir=\"${distrib}\"/>");
		out.println("\t<mkdir dir=\"${kettledir}/plugins/steps/"+dt.getStepname().trim()+"\"/>");
		out.println("</target>");
		
		out.println("<!-- Compile the code -->");
		
		out.println("<target name=\"compile\" depends=\"init\" description=\"compile the source \" >");
		out.println("\t<echo>Compiling...</echo>");
		out.println("\t<javac srcdir=\"${src}\"");
		out.println("\t\tdestdir=\"${classes}\" source=\"1.7\" target=\"1.7\" debug=\"true\" >");
		out.println("\t<classpath id=\"cpath\">");
		out.println("\t\t<fileset dir=\"${libext}\" 		includes=\"*.jar\"/>");
		out.println("\t\t<fileset dir=\"${libswt}\" 		includes=\"*.jar\"/>");
		out.println("\t\t<fileset dir=\"${libswt}/win32/\" includes=\"*.jar\"/>");
		out.println("\t\t<fileset dir=\"${libswt}/linux/\" includes=\"*.jar\"/>");
		out.println("\t\t<fileset dir=\"${libswt}/osx64/\" includes=\"*.jar\"/>");
		out.println("\t\t<fileset dir=\"${libswt}/win64/\" includes=\"*.jar\"/>");
		out.println("\t</classpath>");
		out.println("\t</javac>");
		out.println("</target>");
		
		out.println();
		
		out.println("<!-- Copy additional files -->");
		out.println("<target name=\"copy\" depends=\"compile\" description=\"copy images etc to classes directory\" >");
		out.println("\t<echo>Copying images etc to classes directory...</echo>");
		out.println("\t<copy>");
		out.println("\t\t<fileset dir=\"${src}\" includes=\"**/*.png,**/*.xml,**/*.properties\"/>");
		out.println("\t</copy>");
		out.println("</target>");
		
		out.println();
		
		out.println("<target name=\"libcreate\" depends=\"compile, copy\" description=\"generate the library jar\">");
		out.println("\t<echo>Generating the Kettle library " + dt.getStepname().trim() + ".jar ...</echo>");
		out.println("\t<jar jarfile=\"${lib}/" + dt.getStepname().trim() + "\" basedir=\"${classes}\" includes=\"**/*\"/>");
		out.println("</target>");
		
		out.println();
		
		out.println("<target name=\"distrib\" depends=\"libcreate\" description=\"Create the distribution package...\" >");
		out.println("\t<echo>Copying libraries to distrib directory...</echo>");
		out.println("\t<copy todir=\"${distrib}\">");
		out.println("\t\t<fileset dir=\"${lib}\" includes=\"**/*.jar\"/>");
		out.println("\t</copy>");
		out.println("</target>");
		
		out.println();
		
		out.println("<!-- Deploy to kettle -->");
		
		out.println("<target name=\"deploy\" depends=\"distrib\" description=\"Deploy distribution...\" >");
		out.println("\t<echo>deploying plugin...</echo>");
		out.println("\t<copy todir=\"${deploydir}\">");
		out.println("\t\t<fileset dir=\"${distrib}\" includes=\"**/*.*\"/>");
		out.println("\t</copy>");
		out.println("</target>");
		
		out.println();
		
		out.println("<target name=\"default\" depends=\"deploy\" description=\"default = build all\"/>");
		
		out.println();
		
		out.println("<target name=\"clean\"  description=\"clean up generated files\" >");
		out.println("\t<delete dir=\"${classes}\"/>");
		out.println("\t<delete dir=\"${lib}\"/>");
		out.println("</target>");
		
		out.println();
		
		out.println("<!-- Copy to Kettle steps directory and fork Kettle to test -->");
		out.println("<target name=\"test\" depends=\"clean,default\" description=\"test on kettle\" >");
		out.println("\t<copy todir=\"${kettledir}/plugins/steps/"+dt.getStepname().trim()+"\">");
		out.println("\t\t<fileset dir=\"${deploydir}\" includes=\"**/*.*\" />");
		out.println("\t</copy>");
		out.println("\t<exec executable=\"${kettleexecutable}\" />");
		out.println("</target>");
		
		out.println("</project>");
		
		out.close();
	}
	
	//Create outer dirs
	private void createDirs() throws Exception {
		String tempPath = this.path;
		tempPath += File.separator;
		
		//Project dir
		tempPath += dt.getStepname().trim() + File.separator;
		
		this.path = tempPath;
		
		//Lib dir
		String libPath = tempPath + "lib";
		
		//libext dir
		String libextPath = tempPath + "libext";
		
		//libswt dir
		String libswtPath = tempPath + "libswt";
		String libswtPathwin32 = libswtPath + File.separator + "win32";
		String libswtPathwin64 = libswtPath + File.separator + "win64";
		String libswtPathlinux = libswtPath + File.separator + "linux";
		String libswtPathosx64 = libswtPath + File.separator + "osx64";
		
		//distrib dir
		String distribPath = tempPath + "distrib";
		
		//Deploy dir
		String deployPath = tempPath + "Deploy";
		
		//Src dir
		String srcPath = tempPath + "src";
		
		//Package dirs
		String packageString = dt.getPackagename();
		packageString = packageString.replaceAll("\\.", File.separator);
		packageString = srcPath + File.separator + packageString;
		
		//Create all dirs
		File file = new File(libPath);
		if (!file.mkdirs()) {
			throw new Exception("Failed to make lib dir");
		}
		
		file = new File(libextPath);
		if (!file.mkdirs()) {
			throw new Exception("Failed to make libext dir");
		}
		
		file = new File(libswtPath);
		if (!file.mkdirs()) {
			throw new Exception("Failed to make libswt dir");
		}
		
		file = new File(libswtPathwin32);
		if (!file.mkdirs()) {
			throw new Exception("Failed to make win32 dir");
		}
		
		file = new File(libswtPathwin64);
		if (!file.mkdirs()) {
			throw new Exception("Failed to make win64 dir");
		}
		
		file = new File(libswtPathlinux);
		if (!file.mkdirs()) {
			throw new Exception("Failed to make linux dir");
		}
		
		file = new File(libswtPathosx64);
		if (!file.mkdirs()) {
			throw new Exception("Failed to make osx64 dir");
		}
		
		file = new File(distribPath);
		if (!file.mkdirs()) {
			throw new Exception("Failed to make distrib dir");
		}
		
		file = new File(deployPath);
		if (!file.mkdirs()) {
			throw new Exception("Failed to make deploy dir");
		}
		
		file = new File(packageString);
		if (!file.mkdirs()) {
			throw new Exception("Failed to make directories for package");
		}
	}
}

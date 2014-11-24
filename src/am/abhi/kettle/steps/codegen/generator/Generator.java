package am.abhi.kettle.steps.codegen.generator;

import java.io.File;
import java.io.PrintWriter;

public class Generator {
	private String path;
	private GenDatatype dt;
	private String packagePath;

	public void startGenerator(GenDatatype dt) throws Exception {
		String path = dt.getSaveto();
		if (path.isEmpty()) {
			throw new Exception("Save path cannot be empty");
		}
		this.path = path;
		this.dt = dt;

		createDirs();
		createBuildXml();
		createStepAttributes();
		createPluginXml();
		createDialogClass();
		createMessageClass();
		createMessageProperties();
		createMetaClass();
		createDataClass();
		createMainClass();
	}
	
	//Create Main class
	private void createMainClass() throws Exception {
		PrintWriter out = new PrintWriter(new File(this.packagePath
				+ File.separator + dt.getLibraryname() + ".java"));

		out.println("package " + dt.getPackagename() + ";");
		
		out.println("import org.pentaho.di.core.Const;");
		out.println("import org.pentaho.di.core.exception.KettleException;");
		out.println("import org.pentaho.di.trans.Trans;");
		out.println("import org.pentaho.di.trans.TransMeta;");
		out.println("import org.pentaho.di.trans.step.BaseStep;");
		out.println("import org.pentaho.di.trans.step.StepDataInterface;");
		out.println("import org.pentaho.di.trans.step.StepInterface;");
		out.println("import org.pentaho.di.trans.step.StepMeta;");
		out.println("import org.pentaho.di.trans.step.StepMetaInterface;");
		
		out.println("public class "+dt.getLibraryname()+" extends BaseStep implements StepInterface {");
		out.println("private "+dt.getLibraryname()+"Data data;");
		out.println("private "+dt.getLibraryname()+"Meta meta;");
		
		out.println();
		
		out.println("public "+dt.getLibraryname()+"(StepMeta stepMeta, StepDataInterface stepDataInterface, int copyNr, TransMeta transMeta, Trans trans) {");
		out.println("\tsuper(stepMeta, stepDataInterface, copyNr, transMeta, trans);");
		out.println("}");
		
		out.println();
		
		out.println("public synchronized boolean processRow(StepMetaInterface smi, StepDataInterface sdi) throws KettleException {");
		
		out.println("\tmeta = ("+dt.getLibraryname()+"Meta) smi;");
		out.println("\tdata = ("+dt.getLibraryname()+"Data) sdi;");
		out.println();
		out.println("\tObject[] r = getRow();");
		out.println("\tif (r == null) {");
		out.println("\t\t//No more rows left");
		out.println("\t\tsetOutputDone();");
		out.println("\t\treturn false;");
		out.println("\t}");
		out.println();
		out.println("\tif (first) {");
		out.println("\t\tfirst = false;");
		out.println("\t\t//Good place to set the meta fields");
		out.println("\t}");
		out.println("\treturn true;");
		out.println("}");
		
		out.println();
		
		out.println("public boolean init(StepMetaInterface smi, StepDataInterface sdi) {");
		out.println("\tmeta = ("+dt.getLibraryname()+"Meta) smi;");
		out.println("\tdata = ("+dt.getLibraryname()+"Data) sdi;");
		out.println("\t//All initialization code goes here");
		out.println("return super.init(smi, sdi);");
		out.println("}");
		
		out.println();
		
		out.println("public void dispose(StepMetaInterface smi, StepDataInterface sdi) {");
		out.println("\tmeta = ("+dt.getLibraryname()+"Meta) smi;");
		out.println("\tdata = ("+dt.getLibraryname()+"Data) sdi;");
		out.println("\tsuper.dispose(smi, sdi);");
		out.println("}");
		
		out.println();
		
		out.println("public void run() {");
		out.println("\ttry {");
		out.println("\twhile (processRow(meta, data) && !isStopped());");
		out.println("\t} catch (Exception e) {");
		out.println("\tlogError(Const.getStackTracker(e));");
		out.println("\tsetErrors(1);");
		out.println("\tstopAll();");
		out.println("\t} finally {");
		out.println("\tdispose(meta, data);");
		out.println("\tmarkStop();");
		out.println("\t}");
		out.println("}");
		
		
		
		out.println("}");
		
		out.close();
	}
	
	//Create Data class
	private void createDataClass() throws Exception {
		PrintWriter out = new PrintWriter(new File(this.packagePath
				+ File.separator + dt.getLibraryname() + "Data.java"));

		out.println("package " + dt.getPackagename() + ";");
		
		
		out.println("import org.pentaho.di.trans.step.BaseStepData;");
		out.println("import org.pentaho.di.trans.step.StepDataInterface;");
		
		
		out.println("public class "+dt.getLibraryname()+"Data extends BaseStepData implements StepDataInterface {");
		out.println("//All intermitent data for the step goes here");
		
		out.println("public "+dt.getLibraryname()+"Data() {");
		out.println("\tsuper();");
		out.println("}");
		
		
		out.println("}");
		
		
		out.close();
	}

	// Create Meta class
	private void createMetaClass() throws Exception {
		PrintWriter out = new PrintWriter(new File(this.packagePath
				+ File.separator + dt.getLibraryname() + "Meta.java"));

		out.println("package " + dt.getPackagename() + ";");
		
		out.println("import java.util.List;");
		out.println("import java.util.Map;");
		out.println("import org.eclipse.swt.widgets.Shell;");
		out.println("import org.pentaho.di.core.CheckResult;");
		out.println("import org.pentaho.di.core.CheckResultInterface;");
		out.println("import org.pentaho.di.core.Counter;");
		out.println("import org.pentaho.di.core.database.DatabaseMeta;");
		out.println("import org.pentaho.di.core.exception.KettleException;");
		out.println("import org.pentaho.di.core.row.RowMetaInterface;");
		out.println("import org.pentaho.di.core.row.ValueMetaInterface;");
		out.println("import org.pentaho.di.core.variables.VariableSpace;");
		out.println("import org.pentaho.di.repository.Repository;");
		out.println("import org.pentaho.di.trans.Trans;");
		out.println("import org.pentaho.di.trans.TransMeta;");
		out.println("import org.pentaho.di.trans.step.BaseStepMeta;");
		out.println("import org.pentaho.di.trans.step.StepDataInterface;");
		out.println("import org.pentaho.di.trans.step.StepDialogInterface;");
		out.println("import org.pentaho.di.trans.step.StepInterface;");
		out.println("import org.pentaho.di.trans.step.StepMeta;");
		out.println("import org.pentaho.di.trans.step.StepMetaInterface;");
		out.println("import org.w3c.dom.Node;");
		
		

		out.println("public class " + dt.getLibraryname()
				+ "Meta extends BaseStepMeta implements StepMetaInterface {");
		out.println("//Define the variable to store values from the UI. Define getters and setters.");
		
		out.println();
		
		String vars = dt.getDatastructures();
		String[] va = vars.split(";");
		for (String v : va) {
			String[] vrs = v.split(",");
			String attributeId = vrs[0];
			String dataType = vrs[4];
			
			out.println("private " + dataType + " " + attributeId + ";");
			out.println("public void set" + attributeId + "(" + dataType + " var) {");
			out.println("\tthis." + attributeId + " = " + "var;");
			out.println("}");
			out.println("public " + dataType + " get" + attributeId + "() {");
			out.println("\treturn this." + attributeId + ";");
			out.println("}");
		}

		out.println();

		out.println("@Override");
		out.println("public void check(List<CheckResultInterface> remarks, TransMeta transmeta, StepMeta stepMeta, RowMetaInterface prev, String[] input, String[] output, RowMetaInterface info) {");
		out.println("\tCheckResult cr;");
		out.println("\tif (prev == null || prev.size() == 0) {");
		out.println("\t\tcr = new CheckResult(CheckResult.TYPE_RESULT_WARNING,\"Not receiving any fields from previous steps!\", stepMeta);");
		out.println("\t\tremarks.add(cr);");
		out.println("\t} else {");
		out.println("\t\tcr = new CheckResult(CheckResult.TYPE_RESULT_OK, \"Step is connected to previous one, receiving \" + prev.size() + \" fields\", stepMeta);");
		out.println("\t\tremarks.add(cr);");
		out.println("\t}");
		out.println("\t// See if we have input streams leading to this step!");
		out.println("\tif (input.length > 0) {");
		out.println("\t\tcr = new CheckResult(CheckResult.TYPE_RESULT_OK, \"Step is receiving info from other steps.\", stepMeta);");
		out.println("\t\tremarks.add(cr);");
		out.println("\t} else {");
		out.println("\t\tcr = new CheckResult(CheckResult.TYPE_RESULT_ERROR, \"No input received from other steps!\", stepMeta);");
		out.println("\t\tremarks.add(cr);");
		out.println("\t}");
		out.println("}");
		
		out.println();
		
		out.println("@Override");
		out.println("public String getXML() {");
		out.println("\tStringBuffer retval = new StringBuffer();");
		out.println("\t//Return the Xml to store the UI properties and data. Commented example below.");
		out.println("\t/*");
		out.println("\tretval.append(\"    \").append(XMLHandler.addTagValue(getXmlCode(\"ID\"),Integer.toString(CorrespondingUIElement)));");
		out.println("\t*/");
		out.println("\treturn retval.toString();");
		out.println("}");
		
		out.println();
		
		out.println("@Override");
		out.println("public Object clone() {");
		out.println("\tObject retval = super.clone();");
		out.println("\treturn retval;");
		out.println("}");
		
		out.println();
		
		out.println("@Override");
		out.println("public void getFields(RowMetaInterface r, String origin, RowMetaInterface[] info, StepMeta nextStep, VariableSpace space) {");
		out.println("\tValueMetaInterface v;");
		out.println("\t//Set the output fields if this is a transformation step. Commented example below");
		out.println("\t/*");
		out.println("\tv = ValueMetaFactory.createValueMeta(\"SomeVal\", ValueMeta.TYPE_NUMBER);");
		out.println("\tv.setLength(15);");
		out.println("\tv.setPrecision(6);");
		out.println("\tv.setOrigin(origin);");
		out.println("\tr.addValueMeta(v);");
		out.println("\t*/");
		out.println("}");
		
		out.println();
		
		out.println("@Override");
		out.println("public StepInterface getStep(StepMeta stepMeta, StepDataInterface stepDataInterface, int cnr, TransMeta transMeta, Trans disp) {");
		out.println("\treturn new "+dt.getLibraryname()+"(stepMeta, stepDataInterface, cnr, transMeta, disp);");
		out.println("}");
		
		out.println();
		
		out.println("@Override");
		out.println("public StepDataInterface getStepData() {");
		out.println("\treturn new "+dt.getLibraryname()+"Data();");
		out.println("}");
		
		out.println();
		
		out.println("@Override");
		out.println("public void loadXML(Node stepnode, List<DatabaseMeta> databases, Map<String, Counter> counters) {");
		out.println("\t//Get data from Xml and populate GUI elements");
		out.println("\t/*");
		out.println("\tGUIElementVar = Integer.parseInt(XMLHandler.getTagValue(stepnode,getXmlCode(\"XmlAttr\")));");
		out.println("\t*/");
		out.println("}");
		
		out.println();
		
		out.println("public void readRep(Repository arg0, long arg1, List<DatabaseMeta> arg2, Map<String, Counter> arg3) throws KettleException {");
		out.println("\t//Code to read step state from a repository");
		out.println("}");
		
		out.println();
		
		out.println("public void saveRep(Repository arg0, long arg1, long arg2) throws KettleException {");
		out.println("\r//Code to save step state to a repository");
		out.println("}");
		
		out.println();
		
		out.println("@Override");
		out.println("public void setDefault() {");
		out.println("\t//Default values for GUI elements");
		out.println("}");
		
		out.println();
		
		out.println("public StepDialogInterface getDialog(Shell shell, StepMetaInterface meta, TransMeta transMeta, String name) {");
		out.println("\treturn new "+dt.getLibraryname()+"Dialog(shell, meta, transMeta, name);");
		out.println("}");
		
		out.println("}");

		out.close();
	}

	// Create Message class
	private void createMessageClass() throws Exception {
		PrintWriter out = new PrintWriter(new File(this.packagePath
				+ File.separator + "Messages.java"));

		out.println("package " + dt.getPackagename() + ";");

		out.println("import org.pentaho.di.i18n.BaseMessages;");
		out.println("public class Messages {");
		out.println("\tpublic static final String packageName = Messages.class.getPackage().getName();");

		out.println("\tpublic static String getString(String key) {");
		out.println("\t\treturn BaseMessages.getString(packageName, key);");
		out.println("\t}");

		out.println("\tpublic static String getString(String key, String param1) {");
		out.println("\t\treturn BaseMessages.getString(packageName, key, param1);");
		out.println("\t}");

		out.println("\tpublic static String getString(String key, String param1, String param2) {");
		out.println("\t\treturn BaseMessages.getString(packageName, key, param1, param2);");
		out.println("\t}");

		out.println("\tpublic static String getString(String key, String param1, String param2, String param3) {");
		out.println("\t\treturn BaseMessages.getString(packageName, key, param1, param2, param3);");
		out.println("\t}");

		out.println("\tpublic static String getString(String key, String param1, String param2, String param3, String param4) {");
		out.println("\t\treturn BaseMessages.getString(packageName, key, param1, param2, param3, param4);");
		out.println("\t}");

		out.println("\tpublic static String getString(String key, String param1, String param2, String param3, String param4, String param5) {");
		out.println("\t\treturn BaseMessages.getString(packageName, key, param1, param2, param3, param4, param5);");
		out.println("\t}");

		out.println("\tpublic static String getString(String key, String param1, String param2, String param3, String param4, String param5, String param6) {");
		out.println("\t\treturn BaseMessages.getString(packageName, key, param1, param2, param3, param4, param5, param6);");
		out.println("\t}");

		out.println("}");
		out.close();
	}

	// Create Message properties
	private void createMessageProperties() throws Exception {
		File messagesDir = new File(this.packagePath + File.separator
				+ "messages");
		if (!messagesDir.mkdir()) {
			throw new Exception("Could not create messages dir");
		}

		PrintWriter out = new PrintWriter(new File(this.packagePath
				+ File.separator + "messages" + File.separator
				+ "messages_en_US.properties"));
		out.println(dt.getStepname().trim() + ".Shell.Title="
				+ dt.getStepname());
		out.println(dt.getStepname().trim() + ".Stepname.Title= Stepname");

		String dialogElements = dt.getDialogelements();
		if (!dialogElements.contains(",")) {
			throw new Exception("Invalid format of dialog elements");
		}

		String[] de = dialogElements.split(";");

		for (String d : de) {
			String[] ds = d.split(",");
			String variableName = ds[0];
			String labelText = ds[1];
			String widgetType = ds[2];

			out.println(dt.getStepname().trim() + "." + variableName
					+ ".Title = " + labelText);

		}
		out.close();
	}

	// Create Dialog class
	private void createDialogClass() throws Exception {
		PrintWriter out = new PrintWriter(new File(this.packagePath
				+ File.separator + dt.getLibraryname() + "Dialog.java"));

		out.println("package " + dt.getPackagename() + ";");

		//Imports
		out.println("import org.eclipse.swt.SWT;");
		out.println("import org.eclipse.swt.events.ModifyEvent;");
		out.println("import org.eclipse.swt.events.ModifyListener;");
		out.println("import org.eclipse.swt.events.SelectionAdapter;");
		out.println("import org.eclipse.swt.events.SelectionEvent;");
		out.println("import org.eclipse.swt.events.ShellAdapter;");
		out.println("import org.eclipse.swt.events.ShellEvent;");
		out.println("import org.eclipse.swt.layout.FormAttachment;");
		out.println("import org.eclipse.swt.layout.FormData;");
		out.println("import org.eclipse.swt.layout.FormLayout;");
		out.println("import org.eclipse.swt.widgets.Button;");
		out.println("import org.eclipse.swt.widgets.Display;");
		out.println("import org.eclipse.swt.widgets.Event;");
		out.println("import org.eclipse.swt.widgets.Label;");
		out.println("import org.eclipse.swt.widgets.Listener;");
		out.println("import org.eclipse.swt.widgets.Shell;");
		out.println("import org.eclipse.swt.widgets.Text;");
		out.println("import org.pentaho.di.core.Const;");
		out.println("import org.pentaho.di.trans.TransMeta;");
		out.println("import org.pentaho.di.trans.step.StepDialogInterface;");
		out.println("import org.pentaho.di.trans.step.StepMetaInterface;");
		out.println("import org.pentaho.di.ui.trans.step.BaseStepDialog;");

		out.println("public class " + dt.getLibraryname() + "Dialog"
				+ " extends BaseStepDialog implements StepDialogInterface {");
		
		out.println("private " + dt.getLibraryname() + "Meta input;");

		String dialogElements = dt.getDialogelements();
		if (!dialogElements.contains(",")) {
			throw new Exception("Invalid format of dialog elements");
		}

		String[] de = dialogElements.split(";");

		for (String d : de) {
			String[] ds = d.split(",");
			String variableName = ds[0];
			String labelText = ds[1];
			String widgetType = ds[2];

			out.println("private Label wl" + variableName + ";");
			out.println("private " + widgetType + " w" + variableName + ";");
			out.println("private FormData fdl" + variableName + ", fd"
					+ variableName + ";");
		}

		// Generate the constructor
		out.println("\tpublic "
				+ dt.getLibraryname()
				+ "Dialog(Shell parent, Object baseStepMeta, TransMeta transMeta, String stepname) {");
		out.println("\t\tsuper(parent, (StepMetaInterface) baseStepMeta, transMeta, stepname);");
		out.println("\t\tinput = (" + dt.getLibraryname()
				+ "Meta) baseStepMeta;");
		out.println("\t}");

		// Generate open()
		out.println("\t@Override");
		out.println("\tpublic String open() {");
		out.println("\t\tShell parent = getParent();");
		out.println("\t\tDisplay display = parent.getDisplay();");
		out.println("\t\tshell = new Shell(parent, SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MIN | SWT.MAX);");
		out.println("\t\tprops.setLook(shell);");
		out.println("\t\tsetShellImage(shell, input);");
		out.println();
		out.println("\t\tModifyListener lsMod = new ModifyListener() {");
		out.println("\t\t\tpublic void modifyText(ModifyEvent e) {");
		out.println("\t\t\t\tinput.setChanged();");
		out.println("\t\t\t}");
		out.println("\t\t};");
		out.println("\t\tchanged = input.hasChanged();");
		out.println("\t\tFormLayout formLayout = new FormLayout();");
		out.println("\t\tformLayout.marginWidth = Const.FORM_MARGIN;");
		out.println("\t\tformLayout.marginHeight = Const.FORM_MARGIN;");
		out.println("\t\tshell.setLayout(formLayout);");
		out.println("\t\tshell.setText(Messages.getString(\""
				+ dt.getStepname().trim() + "Shell.Title\"));");
		out.println("\t\tint middle = props.getMiddlePct();");
		out.println("\t\tint margin = Const.MARGIN;");

		out.println();
		out.println("\t\t// Stepname line");
		out.println("\t\twlStepname = new Label(shell, SWT.LEFT);");
		out.println("\t\twlStepname.setText(Messages.getString(\""
				+ dt.getStepname().trim() + "Stepname.Title\""
				+ ")); //$NON-NLS-1$");
		out.println("\t\tprops.setLook(wlStepname);");
		out.println("\t\tfdlStepname = new FormData();");
		out.println("\t\tfdlStepname.left = new FormAttachment(0, 0);");
		out.println("\t\tfdlStepname.right = new FormAttachment(middle, -margin);");
		out.println("\t\tfdlStepname.top = new FormAttachment(0, margin);");
		out.println("\t\twlStepname.setLayoutData(fdlStepname);");
		out.println("\t\twStepname = new Text(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);");
		out.println("\t\twStepname.setText(stepname);");
		out.println("\t\tprops.setLook(wStepname);");
		out.println("\t\twStepname.addModifyListener(lsMod);");
		out.println("\t\tfdStepname = new FormData();");
		out.println("\t\tfdStepname.left = new FormAttachment(middle, 0);");
		out.println("\t\tfdStepname.top = new FormAttachment(0, margin);");
		out.println("\t\tfdStepname.right = new FormAttachment(100, 0);");
		out.println("\t\twStepname.setLayoutData(fdStepname);");

		out.println();

		String lastWidget = "wStepname";

		for (String d : de) {
			String[] ds = d.split(",");
			String variableName = ds[0];
			String labelText = ds[1];
			String widgetType = ds[2];

			out.println("\t\twl" + variableName
					+ " = new Label(shell, SWT.LEFT);");
			out.println("\t\twl" + variableName
					+ ".setText(Messages.getString(\""
					+ dt.getStepname().trim() + "." + variableName + ".Title"
					+ "\"));");
			out.println("\t\tprops.setLook(wl" + variableName + ");");
			out.println("\t\tfdl" + variableName + " = new FormData();");
			out.println("\t\tfdl" + variableName
					+ ".left = new FormAttachment(0, 0);");
			out.println("\t\tfdl" + variableName
					+ ".right = new FormAttachment(middle, -margin);");
			out.println("\t\tfdl" + variableName + ".top = new FormAttachment("
					+ lastWidget + ", margin);");
			out.println("\t\twl" + variableName + ".setLayoutData(fdl"
					+ variableName + ");");
			out.println("\t\tw" + variableName + " = new " + widgetType
					+ "(shell, SWT.LEFT);");
			out.println("\t\tprops.setLook(w" + variableName + ");");
			out.println("\t\tw" + variableName + ".addModifyListener(lsMod);");
			out.println("\t\tfd" + variableName + " = new FormData();");
			out.println("\t\tfd" + variableName
					+ ".left = new FormAttachment(middle, 0);");
			out.println("\t\tfd" + variableName
					+ ".right = new FormAttachment(100, 0);");
			out.println("\t\tfd" + variableName + ".top = new FormAttachment("
					+ lastWidget + ", margin);");
			out.println("\t\tw" + variableName + ".setLayoutData(fd"
					+ variableName + ");");

			lastWidget = "w" + variableName;
		}

		out.println("\t\t//Buttons");
		out.println("\t\twOK = new Button(shell, SWT.PUSH);");
		out.println("\t\twOK.setText(Messages.getString(\"System.Button.OK\"));");
		out.println("\t\twCancel = new Button(shell, SWT.PUSH);");
		out.println("\t\twCancel.setText(Messages.getString(\"System.Button.Cancel\"));");
		out.println("\t\tBaseStepDialog.positionBottomButtons(shell, new Button[] { wOK, wCancel }, margin, "
				+ lastWidget + ");");

		out.println("\t\t//Listeners");
		out.println("\t\tlsCancel = new Listener() {");
		out.println("\t\t\tpublic void handleEvent(Event e) {");
		out.println("\t\t\t\tcancel();");
		out.println("\t\t\t}");
		out.println("\t\t};");

		out.println("\t\tlsOK = new Listener() {");
		out.println("\t\t\tpublic void handleEvent(Event e) {");
		out.println("\t\t\t\tok();");
		out.println("\t\t\t}");
		out.println("\t\t};");

		out.println("\t\twCancel.addListener(SWT.Selection, lsCancel);");
		out.println("\t\twOK.addListener(SWT.Selection, lsOK);");

		out.println("\t\tlsDef = new SelectionAdapter() {");
		out.println("\t\t\tpublic void widgetDefaultSelected(SelectionEvent e) {");
		out.println("\t\t\t\tok();");
		out.println("\t\t\t}");
		out.println("\t\t};");

		out.println("\t\twStepname.addSelectionListener(lsDef);");

		out.println("\t\tshell.addShellListener(new ShellAdapter() {");
		out.println("\t\t\tpublic void shellClosed(ShellEvent e) {");
		out.println("\t\t\t\tcancel();");
		out.println("\t\t\t}");
		out.println("\t\t});");

		out.println("\t\tsetSize();");
		out.println("\t\tgetData();");
		out.println("\t\tinput.setChanged(changed);");
		out.println("\t\tshell.open();");

		out.println("\t\twhile (!shell.isDisposed()) {");
		out.println("\t\t\tif (!display.readAndDispatch())");
		out.println("\t\t\t\tdisplay.sleep();");
		out.println("\t\t}");
		out.println("\t\treturn stepname;");

		out.println("\t}");

		// Get Data method
		out.println();

		out.println("\t// Read data from input (TextFileInputInfo)");
		out.println("\tpublic void getData() {");
		out.println("\t\twStepname.selectAll();");
		out.println("\t\t//Set data to UI elements here");
		out.println("\t}");

		out.println();

		// Cancel method
		out.println("\tprivate void cancel() {");
		out.println("\t\tstepname = null;");
		out.println("\t\tinput.setChanged(changed);");
		out.println("\t\tdispose();");
		out.println("\t}");

		out.println();

		// isEmpty method
		out.println("\tprivate boolean isEmpty(String s) {");
		out.println("\t\treturn s.isEmpty();");
		out.println("\t}");

		// ok method
		out.println("\tprivate void ok() {");
		out.println("\t\tstepname = wStepname.getText(); // return value");
		out.println("\t\tif (Const.isEmpty(stepname)) return;");
		out.println("\t\t//Populate UI variables in meta here");
		out.println("}");
		
		out.println("}");

		out.close();
	}

	// Create plugin.xml
	private void createPluginXml() throws Exception {
		PrintWriter out = new PrintWriter(new File(this.path + "Deploy"
				+ File.separator + "plugin.xml"));

		out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		out.println("<plugin");
		out.println("\tid=\"" + dt.getStepname() + "\"");
		out.println("\ticonfile=\"" + dt.getIconfilename() + "\"");
		out.println("\tdescription=\"" + dt.getDescription() + "\"");
		out.println("\ttooltip=\"" + dt.getDescription() + "\"");
		out.println("\tcategory=\"" + dt.getCategory() + "\"");
		out.println("\tclassname=\"" + dt.getPackagename() + "."
				+ dt.getLibraryname() + "Meta" + "\">");
		out.println("\t<libraries>");
		out.println("\t\t<library name=\"" + dt.getLibraryname() + ".jar\"/>");
		out.println("\t</libraries>");
		out.println("<localized_category>");
		out.println("\t\t<category locale=\"en_US\">" + dt.getCategory()
				+ "</category>");
		out.println("</localized_category>");
		out.println("<localized_description>");
		out.println("\t\t<description locale=\"en_US\">" + dt.getDescription()
				+ "</description>");
		out.println("</localized_description>");
		out.println("<localized_tooltip>");
		out.println("\t\t<tooltip locale=\"en_US\">" + dt.getDescription()
				+ "</tooltip>");
		out.println("</localized_tooltip>");
		out.println("</plugin>");

		out.close();
	}

	// Create step-attributes.xml
	private void createStepAttributes() throws Exception {
		String tempPath = this.packagePath + File.separator
				+ "step-attributes.xml";
		PrintWriter out = new PrintWriter(new File(tempPath));

		out.println("<attributes>");
		String datastructures = dt.getDatastructures();

		if (!datastructures.contains(",")) {
			throw new Exception("Datastructures entered in incorrect format");
		}

		String[] allDS = datastructures.split(";");
		for (int i = 0; i < allDS.length; i++) {
			String[] dsDetails = allDS[i].split(",");
			if (dsDetails.length < 5) {
				throw new Exception(
						"Incomplete set of parameters. There should be 5 parameters per datastructure");
			}
			out.println("\t<attribute id=\"" + dsDetails[0] + "\"> <xmlcode>"
					+ dsDetails[1] + "</xmlcode>" + " <repcode>" + dsDetails[2]
					+ "</repcode>" + " <description>" + dsDetails[3]
					+ "</description>" + " <parentid>" + dsDetails[4]
					+ "</parentid> </attribute>");
		}
		out.println("</attributes>");

		out.close();
	}

	// Create build.xml
	private void createBuildXml() throws Exception {
		String tempPath = this.path + File.separator + "build.xml";
		PrintWriter out = new PrintWriter(new File(tempPath));

		out.println("<project name=\"" + dt.getStepname().trim()
				+ "\" default=\"default\" basedir=\".\">");

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
		out.println("\t<mkdir dir=\"${kettledir}/plugins/steps/"
				+ dt.getLibraryname().trim() + "\"/>");
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
		out.println("\t<echo>Generating the Kettle library "
				+ dt.getLibraryname().trim() + ".jar ...</echo>");
		out.println("\t<jar jarfile=\"${lib}/" + dt.getLibraryname().trim()
				+ ".jar\" basedir=\"${classes}\" includes=\"**/*\"/>");
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
		out.println("\t<copy todir=\"${kettledir}/plugins/steps/"
				+ dt.getLibraryname().trim() + "\">");
		out.println("\t\t<fileset dir=\"${deploydir}\" includes=\"**/*.*\" />");
		out.println("\t</copy>");
		out.println("\t<exec executable=\"${kettleexecutable}\" />");
		out.println("</target>");

		out.println("</project>");

		out.close();
	}

	// Create outer dirs
	private void createDirs() throws Exception {
		String tempPath = this.path;
		tempPath += File.separator;

		// Project dir
		tempPath += dt.getStepname().trim() + File.separator;

		this.path = tempPath;

		// Lib dir
		String libPath = tempPath + "lib";

		// libext dir
		String libextPath = tempPath + "libext";

		// libswt dir
		String libswtPath = tempPath + "libswt";
		String libswtPathwin32 = libswtPath + File.separator + "win32";
		String libswtPathwin64 = libswtPath + File.separator + "win64";
		String libswtPathlinux = libswtPath + File.separator + "linux";
		String libswtPathosx64 = libswtPath + File.separator + "osx64";

		// distrib dir
		String distribPath = tempPath + "distrib";

		// Deploy dir
		String deployPath = tempPath + "Deploy";

		// Src dir
		String srcPath = tempPath + "src";

		// Package dirs
		String packageString = dt.getPackagename();
		packageString = packageString.replaceAll("\\.", File.separator);
		packageString = srcPath + File.separator + packageString;
		this.packagePath = packageString;

		// Create all dirs
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

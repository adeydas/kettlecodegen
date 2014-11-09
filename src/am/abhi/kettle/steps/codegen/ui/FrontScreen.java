package am.abhi.kettle.steps.codegen.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import am.abhi.kettle.steps.codegen.generator.GenDatatype;
import am.abhi.kettle.steps.codegen.generator.Generator;

public class FrontScreen {
	private Shell shell;
	private Display display;

	// GUI elements
	private Text tstepname;
	private Text tdescription;
	private Text tpluginid;
	private Text ticonfilename;
	private Text tcategory;
	private Text tPackage;
	private Text tLibrary;
	private Text tDataStructures;
	private Text tSave;
	private Text tDialogUiElements;
	
	private MessageBox msgBox;

	public void createScreen() {
		Display display = new Display();
		Shell shell = new Shell(display);

		GridLayout layout = new GridLayout(2, false);
		shell.setLayout(layout);
		shell.setText("Kettle step boiler code generator");

		this.shell = shell;
		this.display = display;

		createWidgets();

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

		dispose();

	}

	private void createWidgets() {
		// Stepname
		Label lstepname = new Label(shell, SWT.LEFT | SWT.BORDER);
		lstepname.setText("Stepname");
		lstepname
				.setToolTipText("Stepname is the name of the step aka the project");
		GridData data = new GridData(SWT.FILL, SWT.TOP, true, false);
		lstepname.setLayoutData(data);

		tstepname = new Text(shell, SWT.LEFT | SWT.BORDER);
		data = new GridData(SWT.FILL, SWT.TOP, true, false);
		tstepname
				.setToolTipText("Stepname is the name of the step aka the project");
		tstepname.setLayoutData(data);

		// Description
		Label ldescription = new Label(shell, SWT.LEFT | SWT.BORDER);
		ldescription.setText("Step description");
		ldescription.setToolTipText("Define the step");
		data = new GridData(SWT.FILL, SWT.TOP, true, false);
		ldescription.setLayoutData(data);

		tdescription = new Text(shell, SWT.LEFT | SWT.BORDER);
		data = new GridData(SWT.FILL, SWT.TOP, true, false);
		tdescription.setToolTipText("Define the step");
		tdescription.setLayoutData(data);

		// Plugin config details
		Label lpluginconfigheader = new Label(shell, SWT.LEFT | SWT.BORDER
				| SWT.BORDER_SOLID);
		lpluginconfigheader
				.setText("Plugin configuration details (shown to user on Kettle panel)");

		lpluginconfigheader
				.setToolTipText("The details here will go to plugin.xml and will describe how your plugin appears to your end user");
		data = new GridData(SWT.FILL, SWT.TOP, true, false, 2, 1);
		lpluginconfigheader.setLayoutData(data);

		// Plugin id
		Label lpluginid = new Label(shell, SWT.LEFT | SWT.BORDER);
		lpluginid.setText("Plugin ID");
		data = new GridData(SWT.FILL, SWT.TOP, true, false);
		lpluginid.setLayoutData(data);

		tpluginid = new Text(shell, SWT.LEFT | SWT.BORDER);
		tpluginid.setLayoutData(data);

		// Icon file
		Label liconfile = new Label(shell, SWT.LEFT | SWT.BORDER);
		liconfile.setText("Icon file name");
		liconfile.setLayoutData(data);

		ticonfilename = new Text(shell, SWT.LEFT | SWT.BORDER);
		ticonfilename.setLayoutData(data);

		// Category
		Label lcategory = new Label(shell, SWT.LEFT | SWT.BORDER);
		lcategory.setText("Category");
		lcategory.setLayoutData(data);

		tcategory = new Text(shell, SWT.LEFT | SWT.BORDER);
		tcategory.setLayoutData(data);

		// Package
		Label lpackage = new Label(shell, SWT.LEFT | SWT.BORDER);
		lpackage.setText("Package");
		lpackage.setLayoutData(data);

		tPackage = new Text(shell, SWT.LEFT | SWT.BORDER);
		tPackage.setLayoutData(data);

		// Library name
		Label llibrary = new Label(shell, SWT.LEFT | SWT.BORDER);
		llibrary.setText("Library name");
		llibrary.setLayoutData(data);

		tLibrary = new Text(shell, SWT.LEFT | SWT.BORDER);
		tLibrary.setLayoutData(data);

		// Data structures to be used
		data = new GridData(SWT.FILL, SWT.TOP, true, false, 2, 1);
		data.heightHint = 50;
		Label lDatastructures = new Label(shell, SWT.LEFT | SWT.BORDER);
		lDatastructures
				.setText("Data structures used in the plugin in the format <Attribute_ID>,<Attribute_ID_XML>,<RepCode_ID>,<Description>,<Datatype>,<Parent_Node>;<Attribute_ID>,<Attribute_ID_XML>,<RepCode_ID>,<Description>,<Datatype>,<Parent_Node>;....");
		lDatastructures.setLayoutData(data);
		
		tDataStructures = new Text(shell, SWT.LEFT | SWT.BORDER | SWT.MULTI | SWT.RESIZE);
		
		tDataStructures.setLayoutData(data);
		
		//Step GUI elements
		data = new GridData(SWT.FILL, SWT.TOP, true, false, 2, 1);
		Label lDialogElement = new Label(shell, SWT.LEFT | SWT.BORDER);
		lDialogElement.setText("Dialog UI elements for the plugin dialog in the format <Label_Text>,<SWT_WidgetType>;<Label_Text>,<SWT_WidgetType>....");
		lDialogElement.setLayoutData(data);
		data.heightHint = 50;
		
		tDialogUiElements = new Text(shell, SWT.LEFT | SWT.BORDER | SWT.MULTI | SWT.RESIZE);
		tDialogUiElements.setLayoutData(data);
		
		
		
		data = new GridData(SWT.FILL, SWT.TOP, true, false);
		tSave = new Text(shell, SWT.LEFT | SWT.BORDER);
		tSave.setLayoutData(data);
		
		Button bSaveDialog = new Button(shell,SWT.PUSH);
		bSaveDialog.setText("Save To....");
		bSaveDialog.setLayoutData(data);
		
		Button bGenerate = new Button(shell, SWT.PUSH);
		bGenerate.setText("Generate");
		bGenerate.setLayoutData(data);
		
		//Listeners
		Listener lsSaveTo = new Listener() {

			@Override
			public void handleEvent(Event arg0) {
				saveto();
				
			}
			
		};
		bSaveDialog.addListener(SWT.Selection, lsSaveTo);
		
		Listener lsGenerate = new Listener() {

			@Override
			public void handleEvent(Event arg0) {
				generate();
				
			}
			
		};
		
		bGenerate.addListener(SWT.Selection, lsGenerate);
	}
	
	private void saveto() {
		DirectoryDialog dialog = new DirectoryDialog(shell);
		dialog.setFilterPath(".");
		String result = dialog.open();
		tSave.setText(result);
	}
	
	private void generate() {
		GenDatatype dt = new GenDatatype();
		dt.setCategory(tcategory.getText());
		dt.setDatastructures(tDataStructures.getText().trim());
		dt.setDescription(tdescription.getText());
		dt.setIconfilename(ticonfilename.getText().trim());
		dt.setLibraryname(tLibrary.getText().trim());
		dt.setPackagename(tPackage.getText().trim());
		dt.setPluginid(tpluginid.getText());
		dt.setSaveto(tSave.getText());
		dt.setStepname(tstepname.getText());
		dt.setDialogelements(tDialogUiElements.getText());
		
		try {
			new Generator().startGenerator(dt);
			msgBox = new MessageBox(shell);
			msgBox.setMessage("Generation complete");
			msgBox.open();
		} catch (Exception e) {
			msgBox = new MessageBox(shell, SWT.ERROR);
			msgBox.setMessage(e.getMessage());
			msgBox.open();
		}
	}

	private void dispose() {
		display.dispose();
		shell.dispose();
	}
}
;
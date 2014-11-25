package am.abhi.kettle.steps.codegen.generator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.transfer.Download;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.fasterxml.jackson.databind.ObjectMapper;

public class S3Downloader {

	private S3DT s3;

	// Reads configuration from file. File should be in the same directory with
	// the name config.json
	private void readConfigurationFromFile() throws Exception {
		String jarPath = S3Downloader.class.getProtectionDomain()
				.getCodeSource().getLocation().getPath();

		String[] splitPath = jarPath.split(File.separator);
		StringBuffer totalPath = new StringBuffer();
		for (int i = 0; i < splitPath.length - 1; i++) {
			totalPath.append(splitPath[i] + File.separator);
		}
		String tPath = totalPath.toString() + "config.json";

		File f = new File(tPath);
		System.out.println(f.getAbsolutePath());
		if (!f.exists()) {
			throw new Exception("Config file does not exist. Skip S3 download.");
		}

		ObjectMapper mapper = new ObjectMapper();
		S3DT s3 = mapper.readValue(new File("config.json"), S3DT.class);
		this.s3 = s3;
	}

	public List<String> downloadArtifacts(String path) {
		List<String> allDepsPath = new ArrayList<String>();
		try {
			readConfigurationFromFile();

			AWSCredentials credentials = new BasicAWSCredentials(
					s3.getAccessKey(), s3.getSecretKey());
			AmazonS3 conn = new AmazonS3Client(credentials);

			TransferManager tx = new TransferManager(credentials);

			// for libext
			ObjectListing objects = conn.listObjects(s3.getLibext());
			do {
				if (Thread.interrupted()) {
					System.exit(0);
				}

				for (S3ObjectSummary objectSummary : objects
						.getObjectSummaries()) {
					if (Thread.interrupted()) {
						System.exit(0);
					}


					String p = path + "libext"
							+ File.separator + objectSummary.getKey();
					
					String[] pp = p.split(File.separator);
					
					ProgressMessage.message = "Downloading " + pp[pp.length-1];
					
					try {
						Download download = tx.download(
								new GetObjectRequest(s3.getLibext(),
										objectSummary.getKey()), new File(p));

						while (!download.isDone()) {
							// wait
						}
					} catch (Exception e1) {
					}

					allDepsPath.add(p);
				}
				objects = conn.listNextBatchOfObjects(objects);
			} while (objects.isTruncated());

			// for libswt
			objects = conn.listObjects(s3.getLibswt());
			do {
				if (Thread.interrupted()) {
					System.exit(0);
				}

				for (S3ObjectSummary objectSummary : objects
						.getObjectSummaries()) {
					if (Thread.interrupted()) {
						System.exit(0);
					}

					String p = path + "libswt"
							+ File.separator + objectSummary.getKey();
					
					
					
					String[] pp = p.split(File.separator);
					
					ProgressMessage.message = "Downloading " + pp[pp.length-1];

					Download download = tx.download(
							new GetObjectRequest(s3.getLibext(), objectSummary
									.getKey()), new File(p));
					while (!download.isDone()) {
						// wait
					}

					allDepsPath.add(p);
				}
				objects = conn.listNextBatchOfObjects(objects);
			} while (objects.isTruncated());

			return allDepsPath;
		} catch (Exception e) {
			// skipping download logic
			System.out.println(e.getMessage());
			return null;
		}
	}
}

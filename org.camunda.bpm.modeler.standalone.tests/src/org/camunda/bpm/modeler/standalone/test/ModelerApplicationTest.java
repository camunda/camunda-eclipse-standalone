package org.camunda.bpm.modeler.standalone.test;

import static org.fest.assertions.api.Assertions.assertThat;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.camunda.bpm.modeler.standalone.ModelerApplication;
import org.junit.Test;

public class ModelerApplicationTest {

	@Test
	public void shouldLocateDataDirectoryOnWindows() throws IOException, URISyntaxException {

		if (!isWindows()) {
			return;
		}
		
		URL platformLocation = ModelerApplication.initializePlatformLocation("camunda Modeler Test");
		
		assertThat(platformLocation.toString())
			.doesNotContain("%20")
			.endsWith("camunda Modeler Test/workspace/");
	}

	private boolean isWindows() {
		return System.getProperty("os.name").toLowerCase().contains("win");
	}
}

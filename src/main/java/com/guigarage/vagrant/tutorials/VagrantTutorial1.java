package com.guigarage.vagrant.tutorials;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.guigarage.vagrant.VagrantEnvironmentFactory;
import com.guigarage.vagrant.configuration.VagrantEnvironmentConfig;
import com.guigarage.vagrant.configuration.VagrantVmConfig;
import com.guigarage.vagrant.model.VagrantEnvironment;

/**
 * Creates a simple VM that is based on lucid32 box. The VM will be created,
 * started and destroyed. This class is a first tutorial for Vagrant-Binding.
 * 
 * @author hendrikebbers
 */
public class VagrantTutorial1 {

	public static void main(String[] args) throws IOException {
		VagrantVmConfig vmConfig = VagrantVmConfig.builder().withName("demoVm")
				.withLucid32Box().build();
		VagrantEnvironmentConfig environmentConfig = VagrantEnvironmentConfig
				.builder().withVagrantVmConfig(vmConfig).build();
		VagrantEnvironment vagrantEnvironmet = VagrantEnvironmentFactory
				.builder()
				.withDebug(true)
				.build()
				.createEnvironment(
						new File(FileUtils.getTempDirectory(), "myVagrantPath"
								+ System.currentTimeMillis()),
						environmentConfig);
		vagrantEnvironmet.up();
		vagrantEnvironmet.destroy();
	}

}

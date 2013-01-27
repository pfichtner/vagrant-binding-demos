package com.guigarage.vagrant.tutorials;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

import org.apache.commons.io.FileUtils;

import com.guigarage.vagrant.Vagrant;
import com.guigarage.vagrant.configuration.PuppetProvisionerConfig;
import com.guigarage.vagrant.configuration.VagrantEnvironmentConfig;
import com.guigarage.vagrant.configuration.VagrantFileTemplateConfigurationURL;
import com.guigarage.vagrant.configuration.VagrantVmConfig;
import com.guigarage.vagrant.model.VagrantEnvironment;
import com.guigarage.vagrant.util.VagrantUtils;

public class PuppetTutorial1 {

	public static void main(String[] args) throws IOException {
		PuppetProvisionerConfig puppetConfig = PuppetProvisionerConfig
				.builder().withDebug(true)
				.withManifestFile("myPuppetManifest.pp")
				.withManifestPath("manifests").build();
		VagrantVmConfig vmConfig = VagrantVmConfig.builder().withName("demoVm")
				.withLucid32Box().withPuppetProvisionerConfig(puppetConfig)
				.build();
		VagrantEnvironmentConfig environmentConfig = VagrantEnvironmentConfig
				.builder().withVagrantVmConfig(vmConfig).build();
		VagrantFileTemplateConfigurationURL fileConfig = VagrantFileTemplateConfigurationURL
				.builder()
				.withUrlTemplate(
						VagrantUtils
								.getInstance()
								.load("/com/guigarage/vagrant/tutorials/myPuppetManifest.pp"))
				.withPathInVagrantFolder("manifests/myPuppetManifest.pp")
				.build();
		VagrantEnvironment vagrantEnvironmet = new Vagrant(true)
				.createEnvironment(new File(FileUtils.getTempDirectory(),
						"myVagrantPath" + System.currentTimeMillis()),
						environmentConfig, Collections.singleton(fileConfig));
		vagrantEnvironmet.up();
		vagrantEnvironmet.destroy();
	}
}

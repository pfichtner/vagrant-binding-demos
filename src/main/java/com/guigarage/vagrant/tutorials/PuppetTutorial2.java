package com.guigarage.vagrant.tutorials;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

import org.apache.commons.io.FileUtils;

import com.guigarage.puppet.forge.PuppetForgeClient;
import com.guigarage.vagrant.Vagrant;
import com.guigarage.vagrant.configuration.PuppetProvisionerConfig;
import com.guigarage.vagrant.configuration.VagrantEnvironmentConfig;
import com.guigarage.vagrant.configuration.VagrantFileTemplateConfiguration;
import com.guigarage.vagrant.configuration.VagrantFolderTemplateConfiguration;
import com.guigarage.vagrant.configuration.VagrantVmConfig;
import com.guigarage.vagrant.model.VagrantEnvironment;
import com.guigarage.vagrant.util.VagrantUtils;

public class PuppetTutorial2 {

	public static void main(String[] args) throws IOException {
		PuppetProvisionerConfig puppetConfig = PuppetProvisionerConfig.Builder
				.create().withDebug(true)
				.withManifestFile("myPuppetManifest.pp")
				.withManifestPath("puppet/manifests")
				.withModulesPath("puppet/modules").build();

		File tempFolder = new File(FileUtils.getTempDirectoryPath(),
				"puppet-modules");
		tempFolder.mkdirs();
		PuppetForgeClient forgeClient = new PuppetForgeClient();
		forgeClient.installToModulesDir(tempFolder,
				forgeClient.findModule("puppetlabs", "mongodb"));
		VagrantFolderTemplateConfiguration folderConfig = Builder.create()
				.withLocalFolder(tempFolder)
				.withPathInVagrantFolder("puppet/modules").build();
		VagrantFileTemplateConfiguration fileConfig = VagrantFileTemplateConfiguration.Builder
				.create()
				.withUrlTemplate(
						VagrantUtils
								.getInstance()
								.load("/com/guigarage/vagrant/tutorials/myPuppetMongoManifest.pp"))
				.withPathInVagrantFolder("puppet/manifests/myPuppetManifest.pp")
				.build();

		VagrantVmConfig vmConfig = VagrantVmConfig.Builder.create()
				.withName("demoVm").withLucid32Box()
				.withPuppetProvisionerConfig(puppetConfig).build();
		VagrantEnvironmentConfig environmentConfig = VagrantEnvironmentConfig.Builder
				.create().withVagrantVmConfig(vmConfig).build();
		VagrantEnvironment vagrantEnvironmet = new Vagrant(true)
				.createEnvironment(new File(FileUtils.getTempDirectory(),
						"myVagrantPath" + System.currentTimeMillis()),
						environmentConfig, Collections.singleton(fileConfig),
						Collections.singleton(folderConfig));
		vagrantEnvironmet.up();
		vagrantEnvironmet.destroy();
	}
}

package com.guigarage.vagrant.tutorials;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.guigarage.vagrant.Vagrant;
import com.guigarage.vagrant.configuration.VagrantEnvironmentConfig;
import com.guigarage.vagrant.configuration.VagrantVmConfig;
import com.guigarage.vagrant.model.VagrantEnvironment;
import com.guigarage.vagrant.model.VagrantVm;

public class VagrantTutorial3 {

	public static void main(String[] args) throws IOException {
		VagrantVmConfig vmConfig = VagrantVmConfig.builder().withName("demoVm")
				.withLucid32Box().build();
		VagrantEnvironmentConfig environmentConfig = VagrantEnvironmentConfig
				.builder().withVagrantVmConfig(vmConfig).build();
		VagrantEnvironment vagrantEnvironmet = new Vagrant(true)
				.createEnvironment(new File(FileUtils.getTempDirectory(),
						"myVagrantPath" + System.currentTimeMillis()),
						environmentConfig);
		vagrantEnvironmet.up();
		VagrantVm vm0 = vagrantEnvironmet.getVm(0);
		vm0.createConnection().execute("touch /tmp/newFile.tmp", false);
		vm0.createConnection().upload("path/to/local/file", "path/on/vm");
		vagrantEnvironmet.destroy();
	}

}

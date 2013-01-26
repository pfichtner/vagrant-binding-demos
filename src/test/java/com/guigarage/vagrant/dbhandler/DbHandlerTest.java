package com.guigarage.vagrant.dbhandler;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;

import com.guigarage.vagrant.configuration.PuppetProvisionerConfig;
import com.guigarage.vagrant.configuration.VagrantConfiguration;
import com.guigarage.vagrant.configuration.VagrantEnvironmentConfig;
import com.guigarage.vagrant.configuration.VagrantFileTemplateConfigurationURL;
import com.guigarage.vagrant.configuration.VagrantVmConfig;
import com.guigarage.vagrant.junit.VagrantTestRule;
import com.guigarage.vagrant.util.VagrantUtils;

/**
 * @author hendrikebbers
 * 
 *         UnitTests that uses the
 *         {@link com.guigarage.vagrant.junit.VagrantTestRule}. The test is
 *         wrapped in the lifecycle of a vm created by Vagrant-Binding.
 */
public class DbHandlerTest {

	/**
	 * IP adress for the dbserver vm
	 */
	private static final String VM_IP = "192.168.50.4";

	/**
	 * JUnit rule that syncs the lifecycle of all tests with the livecycle of a
	 * dbserver vm
	 */
	@Rule
	public VagrantTestRule testRule = new VagrantTestRule(createConfig());

	/**
	 * Creates a VagrantConfiguration for a dbserver vm. Th vm is configured by
	 * puppet and is running a MySQL server. The configuration is used by the
	 * VagrantTestRule
	 * 
	 * @return the VagrantConfiguration
	 */
	public static VagrantConfiguration createConfig() {
		try {
			PuppetProvisionerConfig puppetConfig = PuppetProvisionerConfig.Builder
					.create().withManifestPath("puppet/manifests")
					.withManifestFile("dbserver.pp").withDebug(true).build();

			VagrantVmConfig vmConfig = VagrantVmConfig.Builder.create()
					.withName("mysqlvm").withHostOnlyIp(VM_IP).withLucid32Box()
					.withPuppetProvisionerConfig(puppetConfig).build();

			VagrantEnvironmentConfig environmentConfig = VagrantEnvironmentConfig.Builder
					.create().withVagrantVmConfig(vmConfig).build();

			VagrantFileTemplateConfigurationURL fileTemplateConfiguration1 = VagrantFileTemplateConfigurationURL.Builder
					.create()
					.withUrlTemplate(
							VagrantUtils.getInstance().load(
									"com/guigarage/vagrant/dbhandler/my.cnf"))
					.withPathInVagrantFolder("files/my.cnf").build();

			VagrantFileTemplateConfigurationURL fileTemplateConfiguration2 = VagrantFileTemplateConfigurationURL.Builder
					.create()
					.withUrlTemplate(
							VagrantUtils
									.getInstance()
									.load("com/guigarage/vagrant/dbhandler/dbserver.pp"))
					.withPathInVagrantFolder("puppet/manifests/dbserver.pp")
					.build();

			VagrantConfiguration configuration = VagrantConfiguration.Builder
					.create()
					.withVagrantEnvironmentConfig(environmentConfig)
					.withVagrantFileTemplateConfiguration(
							fileTemplateConfiguration1)
					.withVagrantFileTemplateConfiguration(
							fileTemplateConfiguration2).build();

			return configuration;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Simple UnitTests that connects to the MySQL server on the vm and creates
	 * some data
	 */
	@Test
	public void testJdbc() {
		DbHandler dbHandler = new DbHandler(VM_IP, "testapp", "dbuser",
				"dbuser");
		dbHandler.connect();
		dbHandler.createMyTable();
		for (int i = 0; i < 100; i++) {
			dbHandler.insertRow();
		}
		assertEquals(100, dbHandler.getRowCount());
		dbHandler.close();
	}
}

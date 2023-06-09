package net.cabezudo.sofia.creator;

import net.cabezudo.sofia.accounts.AccountManager;
import net.cabezudo.sofia.accounts.mappers.EntityToBusinessAccountMapper;
import net.cabezudo.sofia.accounts.persistence.AccountRepository;
import net.cabezudo.sofia.config.ConfigurationException;
import net.cabezudo.sofia.config.H2TestDataSourceConfig;
import net.cabezudo.sofia.core.SofiaEnvironment;
import net.cabezudo.sofia.persistence.DatabaseManager;
import net.cabezudo.sofia.security.PermissionManager;
import net.cabezudo.sofia.sites.HostNotFoundException;
import net.cabezudo.sofia.sites.PathManager;
import net.cabezudo.sofia.sites.Site;
import net.cabezudo.sofia.sites.SiteManager;
import net.cabezudo.sofia.sites.SiteNotFoundException;
import net.cabezudo.sofia.sites.SiteRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author <a href="http://cabezudo.net">Esteban Cabezudo</a>
 * @version 0.01.00, 2022.02.25
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
    AccountManager.class, AccountRepository.class, ContentManager.class, DatabaseManager.class, EntityToBusinessAccountMapper.class, H2TestDataSourceConfig.class, PermissionManager.class, SiteManager.class,
    SofiaEnvironment.class, SiteRepository.class, PathManager.class, TemplateVariables.class
})
@ActiveProfiles("test")
@JdbcTest
@Sql({"/test_schema.sql"})
public class SofiaFileTest {

  private @Autowired SofiaEnvironment sofiaEnvironment;
  private @Autowired SiteManager siteManager;
  private @Autowired PathManager pathManager;
  private @Autowired TemplateVariables templateVariables;
  private @Autowired PermissionManager permissionManager;

  @Test
  public void testSofiaFile() throws ConfigurationException, SiteCreationException, IOException, SiteNotFoundException, HostNotFoundException {
    sofiaEnvironment.loadConfiguration();

    String name = "example.com";

    Site site = new Site(1, name, "localhost");
    SofiaFile sofiaFile = new SofiaFile(site, "1", "/index.html", sofiaEnvironment, siteManager, pathManager, templateVariables, permissionManager);

    sofiaFile.loadRootFile();
    sofiaFile.save();
    String code = sofiaFile.toCode();

    System.out.println(code);

    Assertions.assertTrue(code.contains("<!DOCTYPE html>"));
    Assertions.assertTrue(code.contains("<title>This is the page title</title>"));

    // Check all css are imported
    Assertions.assertTrue(code.contains("/* CSS import from /cssInHead.css called by file attribute on style tag on index.html:7:6 */"));
    Assertions.assertTrue(code.contains("/* CSS import from /fileCalledInSection.html called by style tag on fileCalledInSection.html:7:4 */"));
    Assertions.assertTrue(code.contains("/* CSS import from /fileWithoutConfiguration.css called by file attribute on section on fileWithoutConfiguration.html:58:6 */"));
    Assertions.assertTrue(code.contains("/* CSS import from /index.html called by style tag on index.html:13:6 */"));
    Assertions.assertTrue(code.contains("/* CSS import from /index.html called by style tag on index.html:23:6 */"));
    Assertions.assertTrue(code.contains("/* CSS code from /cssInHead.css called by file attribute on style tag on index.html:7:6 */"));
    Assertions.assertTrue(code.contains("/* CSS code from /fileCalledInSection.html called by style tag on fileCalledInSection.html:7:4 */"));
    Assertions.assertTrue(code.contains("/* CSS code from /fileWithoutConfiguration.css called by file attribute on section on fileWithoutConfiguration.html:58:6 */"));
    Assertions.assertTrue(code.contains("/* CSS code from /index.html called by style tag on index.html:13:6 */"));
    Assertions.assertTrue(code.contains("/* CSS code from /index.html called by style tag on index.html:23:6 */"));

    // Check the template variables
    Assertions.assertTrue(code.contains("const templateVariables = {"));
    Assertions.assertTrue(code.contains("\"textFromConfiguration\": \"Text from the configuration\""));
    Assertions.assertTrue(code.contains("\"soloIndex\": \"index\""));
    Assertions.assertTrue(code.contains("\"fileCalledInSectionDataInThisFile\": \"data only in this file\""));
    Assertions.assertTrue(code.contains("\"dataFromConfigurationFile\": \"ok\" };"));

    // Check that scripts are loaded
    Assertions.assertTrue(code.contains("// Code from script tag on fileCalledInSection.html:2:4 called by file attribute on section on fileCalledInSection.html:56:6"));
    Assertions.assertTrue(code.contains("scriptInFileCalledInSection = () => {"));
    Assertions.assertTrue(code.contains("// Code from script tag on index.html:18:6"));
    Assertions.assertTrue(code.contains("secondScript = () => {"));
    Assertions.assertTrue(code.contains("// Code from script tag on index.html:8:6"));
    Assertions.assertTrue(code.contains("firstScript = () => {"));
    Assertions.assertTrue(code.contains("commons = () => {"));
    Assertions.assertTrue(code.contains("index = () => {"));
    Assertions.assertTrue(code.contains("// Code from file attribute in script tag on index.html:6:6"));

    // Check the added code
    Assertions.assertTrue(code.contains("<div>Div with custom content template</div"));
    Assertions.assertTrue(code.contains("Div with custom content template</div"));
    Assertions.assertTrue(code.contains("<!-- File without configuration file -->"));
  }

  public void testSofiaFileDocumentElementInFile() throws ConfigurationException, SiteCreationException, IOException, SiteNotFoundException, HostNotFoundException {
    sofiaEnvironment.loadConfiguration();

    String name = "example.com";

    Site site = new Site(1, name, "localhost");
    SofiaFile sofiaFile = new SofiaFile(site, "1", "/documentElementInFile.html", sofiaEnvironment, siteManager, pathManager, templateVariables, permissionManager);
    sofiaFile.loadRootFile();
    sofiaFile.save();

    String code = sofiaFile.toCode();

    Assertions.assertTrue(code.contains("<!DOCTYPE html>"));
    Assertions.assertTrue(code.contains("body"));
    Assertions.assertTrue(code.contains("html"));
    Assertions.assertTrue(code.contains("margin: 0;"));
    Assertions.assertTrue(code.contains("padding: 0;"));
    Assertions.assertTrue(code.contains("}"));
    Assertions.assertTrue(code.contains("const templateVariables = { \"documentElementInFile\": \"documentElementInFile\" };"));
    Assertions.assertTrue(code.contains("// Code from :commons.js: called by system"));
    Assertions.assertTrue(code.contains("const commons = () => {"));
  }

  @Test
  public void testSourceAlreadyAddedException() throws ConfigurationException {


    sofiaEnvironment.loadConfiguration();

    String name = "example.com";

    Site site = new Site(1, name, "localhost");
    SofiaFile sofiaFile = new SofiaFile(site, "1", "/sourceAlreadyAddedException.html", sofiaEnvironment, siteManager, pathManager, templateVariables, permissionManager);

    FileNotFoundException fileNotFoundException = Assertions.assertThrows(FileNotFoundException.class, sofiaFile::loadRootFile);
    Assertions.assertNotNull(fileNotFoundException);

  }

  @Test
  public void testCommonCSSFileNotFound() throws ConfigurationException, SiteCreationException, HostNotFoundException, IOException {


    sofiaEnvironment.loadConfiguration();

    String name = "example.com";
    Site site = new Site(1, name, "localhost");
    SofiaFile sofiaFile = new SofiaFile(site, "2", "/commonsCSSFileNotFound.html", sofiaEnvironment, siteManager, pathManager, templateVariables, permissionManager);
    sofiaFile.loadRootFile();
  }

  @Test
  public void testJSONParseExceptionInCommonsConfigurationFile() throws ConfigurationException {
    sofiaEnvironment.loadConfiguration();

    String name = "example.com";
    Site site = new Site(1, name, "localhost");
    SofiaFile sofiaFile = new SofiaFile(site, "4", "/jsonParseExceptionInCommonsConfigurationFile.html", sofiaEnvironment, siteManager, pathManager, templateVariables, permissionManager);

    SiteCreationException siteCreationException = Assertions.assertThrows(SiteCreationException.class, sofiaFile::loadRootFile);
    Assertions.assertNotNull(siteCreationException);
    Assertions.assertEquals("net.cabezudo.json.exceptions.UnexpectedElementException: Unexpected element: Esteban", siteCreationException.getMessage());

    sofiaEnvironment.setDevelopment(true);
    siteCreationException = Assertions.assertThrows(SiteCreationException.class, sofiaFile::loadRootFile);
    Assertions.assertNotNull(siteCreationException);
    Assertions.assertEquals("net.cabezudo.json.exceptions.UnexpectedElementException: Unexpected element: Esteban", siteCreationException.getMessage());
  }

  @Test
  public void testJSONParseExceptionInTextsFile() throws ConfigurationException {


    sofiaEnvironment.loadConfiguration();

    String name = "example.com";

    Site site = new Site(1, name, "localhost");

    SofiaFile sofiaFile = new SofiaFile(site, "4", "/jsonParseExceptionInTextsFile.html", sofiaEnvironment, siteManager, pathManager, templateVariables, permissionManager);

    SiteCreationException siteCreationException = Assertions.assertThrows(SiteCreationException.class, sofiaFile::loadRootFile);
    Assertions.assertNotNull(siteCreationException);
    Assertions.assertEquals("net.cabezudo.json.exceptions.UnexpectedElementException: Unexpected element: Esteban", siteCreationException.getMessage());
  }

  @Test
  public void testUndefinedLiteralException() throws ConfigurationException {


    sofiaEnvironment.loadConfiguration();

    String name = "example.com";
    Site site = new Site(1, name, "localhost");
    SofiaFile sofiaFile = new SofiaFile(site, "2", "/testUndefinedLiteralException.html", sofiaEnvironment, siteManager, pathManager, templateVariables, permissionManager);

    SiteCreationException siteCreationException = Assertions.assertThrows(SiteCreationException.class, sofiaFile::loadRootFile);
    Assertions.assertNotNull(siteCreationException);
    Assertions.assertEquals("Undefined literal: undefined.literal in 4:33", siteCreationException.getMessage());
  }

  @Test
  public void testHTMLParseException() throws ConfigurationException {

    sofiaEnvironment.loadConfiguration();

    String name = "example.com";

    Site site = new Site(1, name, "localhost");

    SofiaFile sofiaFile = new SofiaFile(site, "1", "/invalidHTML.html", sofiaEnvironment, siteManager, pathManager, templateVariables, permissionManager);

    SiteCreationException siteCreationException = Assertions.assertThrows(SiteCreationException.class, sofiaFile::loadRootFile);
    Assertions.assertNotNull(siteCreationException);
    Assertions.assertEquals("net.cabezudo.html.ParseException: Invalid HTML", siteCreationException.getMessage());
  }

  @Test
  public void testDuplicateIdException() throws ConfigurationException {

    sofiaEnvironment.loadConfiguration();

    String name = "example.com";

    Site site = new Site(1, name, "localhost");

    SofiaFile sofiaFile = new SofiaFile(site, "1", "/duplicateId.html", sofiaEnvironment, siteManager, pathManager, templateVariables, permissionManager);

    SiteCreationException siteCreationException = Assertions.assertThrows(SiteCreationException.class, sofiaFile::loadRootFile);
    Assertions.assertNotNull(siteCreationException);
    Assertions.assertEquals("net.cabezudo.sofia.creator.DuplicateIdException: The id 'application' already exist in element in 1/duplicateId.html:8:6", siteCreationException.getMessage());
  }

  @Test
  public void testDuplicateIdOnTextException() throws ConfigurationException {

    sofiaEnvironment.loadConfiguration();

    String name = "example.com";

    Site site = new Site(1, name, "localhost");

    SofiaFile sofiaFile = new SofiaFile(site, "1", "/duplicateIdOnText.html", sofiaEnvironment, siteManager, pathManager, templateVariables, permissionManager);

    SiteCreationException siteCreationException = Assertions.assertThrows(SiteCreationException.class, sofiaFile::loadRootFile);
    Assertions.assertNotNull(siteCreationException);
    Assertions.assertEquals("net.cabezudo.sofia.creator.DuplicateIdException: The id 'a1' already exist in element in 1/duplicateIdOnText.html:9:13", siteCreationException.getMessage());
  }

  @Test
  public void testEmptyLanguage() throws ConfigurationException, SiteCreationException, HostNotFoundException, IOException {

    sofiaEnvironment.loadConfiguration();

    String name = "example.com";

    Site site = new Site(1, name, "localhost");

    SofiaFile sofiaFile = new SofiaFile(site, "1", "/emptyLanguage.html", sofiaEnvironment, siteManager, pathManager, templateVariables, permissionManager);

    sofiaFile.loadRootFile();
  }

  @Test
  public void testRootDocumentWithoutHeadException() throws ConfigurationException {

    sofiaEnvironment.loadConfiguration();

    String name = "example.com";

    Site site = new Site(1, name, "localhost");

    SofiaFile sofiaFile = new SofiaFile(site, "1", "/rootFileWithoutHead.html", sofiaEnvironment, siteManager, pathManager, templateVariables, permissionManager);

    SiteCreationException siteCreationException = Assertions.assertThrows(SiteCreationException.class, sofiaFile::loadRootFile);
    Assertions.assertNotNull(siteCreationException);
    Assertions.assertEquals("The root file must have an head tag", siteCreationException.getMessage());
  }

  @Test
  public void testDuplicateIdOnText() throws ConfigurationException {

    sofiaEnvironment.loadConfiguration();

    String name = "example.com";

    Site site = new Site(1, name, "localhost");

    SofiaFile sofiaFile = new SofiaFile(site, "1", "/duplicateIdOnText.html", sofiaEnvironment, siteManager, pathManager, templateVariables, permissionManager);

    SiteCreationException siteCreationException = Assertions.assertThrows(SiteCreationException.class, sofiaFile::loadRootFile);
    Assertions.assertNotNull(siteCreationException);
    Assertions.assertEquals("net.cabezudo.sofia.creator.DuplicateIdException: The id 'a1' already exist in element in 1/duplicateIdOnText.html:9:13", siteCreationException.getMessage());
  }

  @Test
  public void testScriptPathOutsideSourcePath() throws ConfigurationException {

    sofiaEnvironment.loadConfiguration();

    String name = "example.com";

    Site site = new Site(1, name, "localhost");

    SofiaFile sofiaFile = new SofiaFile(site, "1", "/scriptPathOutsideSourcePath.html", sofiaEnvironment, siteManager, pathManager, templateVariables, permissionManager);

    SiteCreationException siteCreationException = Assertions.assertThrows(SiteCreationException.class, sofiaFile::loadRootFile);
    Assertions.assertNotNull(siteCreationException);
    Assertions.assertEquals("net.cabezudo.sofia.creator.FilePathOutsideSourcePath: The file ../emptyFile.js defined in the attribute file on the source file 1/scriptPathOutsideSourcePath.html:3:11 is outside the source path.", siteCreationException.getMessage());
  }

  @Test
  public void testScriptPathDoNotExists() throws ConfigurationException {

    sofiaEnvironment.loadConfiguration();

    String name = "example.com";

    Site site = new Site(1, name, "localhost");

    SofiaFile sofiaFile = new SofiaFile(site, "1", "/scriptPathDoNotExists.html", sofiaEnvironment, siteManager, pathManager, templateVariables, permissionManager);

    SiteCreationException siteCreationException = Assertions.assertThrows(SiteCreationException.class, sofiaFile::loadRootFile);
    Assertions.assertNotNull(siteCreationException);
    Assertions.assertEquals("net.cabezudo.sofia.creator.SofiaFileNotFoundException: The file FileDoNotExists.js defined in the attribute file on the source file 1/scriptPathDoNotExists.html:3:11 do not exists.", siteCreationException.getMessage());
  }

  @Test
  public void testStylePathOutsideSourcePath() throws ConfigurationException {

    sofiaEnvironment.loadConfiguration();

    String name = "example.com";

    Site site = new Site(1, name, "localhost");

    SofiaFile sofiaFile = new SofiaFile(site, "1", "/stylePathOutsideSourcePath.html", sofiaEnvironment, siteManager, pathManager, templateVariables, permissionManager);

    SiteCreationException siteCreationException = Assertions.assertThrows(SiteCreationException.class, sofiaFile::loadRootFile);
    Assertions.assertNotNull(siteCreationException);
    Assertions.assertEquals("net.cabezudo.sofia.creator.FilePathOutsideSourcePath: The file ../emptyFile.css defined in the attribute file on the source file 1/stylePathOutsideSourcePath.html:3:10 is outside the source path.", siteCreationException.getMessage());
  }

  @Test
  public void testStylePathDoNotExists() throws ConfigurationException {

    sofiaEnvironment.loadConfiguration();

    String name = "example.com";

    Site site = new Site(1, name, "localhost");

    SofiaFile sofiaFile = new SofiaFile(site, "1", "/stylePathDoNotExists.html", sofiaEnvironment, siteManager, pathManager, templateVariables, permissionManager);

    SiteCreationException siteCreationException = Assertions.assertThrows(SiteCreationException.class, sofiaFile::loadRootFile);
    Assertions.assertNotNull(siteCreationException);
    Assertions.assertEquals("net.cabezudo.sofia.creator.SofiaFileNotFoundException: The file FileDoNotExists.css defined in the attribute file on the source file 1/stylePathDoNotExists.html:3:10 do not exists.", siteCreationException.getMessage());
  }

  @Test
  public void testFileContainerWithChildren() throws ConfigurationException {

    sofiaEnvironment.loadConfiguration();

    String name = "example.com";

    Site site = new Site(1, name, "localhost");

    SofiaFile sofiaFile = new SofiaFile(site, "1", "/fileContainerWithChildren.html", sofiaEnvironment, siteManager, pathManager, templateVariables, permissionManager);

    SiteCreationException siteCreationException = Assertions.assertThrows(SiteCreationException.class, sofiaFile::loadRootFile);
    Assertions.assertNotNull(siteCreationException);
    Assertions.assertEquals("A file container can't have children.", siteCreationException.getMessage());
  }

  @Test
  public void testUndefinedLiteralInCalledFile() throws ConfigurationException {

    sofiaEnvironment.loadConfiguration();

    String name = "example.com";

    Site site = new Site(1, name, "localhost");

    SofiaFile sofiaFile = new SofiaFile(site, "1", "/undefinedLiteralInCalledFile.html", sofiaEnvironment, siteManager, pathManager, templateVariables, permissionManager);

    SiteCreationException siteCreationException = Assertions.assertThrows(SiteCreationException.class, sofiaFile::loadRootFile);
    Assertions.assertNotNull(siteCreationException);
    Assertions.assertEquals("The property undefined.literal found in calledFileWithoutLiteral.html:4:16 is not defined on the JSON configuration files", siteCreationException.getMessage());
  }

  @Test
  public void testCallFileWithoutBody() throws ConfigurationException {

    sofiaEnvironment.loadConfiguration();

    String name = "example.com";

    Site site = new Site(1, name, "localhost");

    SofiaFile sofiaFile = new SofiaFile(site, "1", "/callFileWithoutBody.html", sofiaEnvironment, siteManager, pathManager, templateVariables, permissionManager);

    SiteCreationException siteCreationException = Assertions.assertThrows(SiteCreationException.class, sofiaFile::loadRootFile);
    Assertions.assertNotNull(siteCreationException);
    Assertions.assertEquals("The file fileWithoutBody.html must have an body element", siteCreationException.getMessage());
  }

  @Test
  public void testStyleFileAlreadyAdded() throws ConfigurationException {

    sofiaEnvironment.loadConfiguration();

    String name = "example.com";

    Site site = new Site(1, name, "localhost");

    new SofiaFile(site, "1", "/styleFileAlreadyAdded.html", sofiaEnvironment, siteManager, pathManager, templateVariables, permissionManager);
  }

  @Test
  public void testScriptFileAlreadyAdded() throws ConfigurationException {

    sofiaEnvironment.loadConfiguration();

    String name = "example.com";

    Site site = new Site(1, name, "localhost");

    new SofiaFile(site, "1", "/scriptFileAlreadyAdded.html", sofiaEnvironment, siteManager, pathManager, templateVariables, permissionManager);
  }

  @Test
  public void testReadTextsFileJSONParseException() throws ConfigurationException {

    sofiaEnvironment.loadConfiguration();

    String name = "example.com";

    Site site = new Site(1, name, "localhost");

    SofiaFile sofiaFile = new SofiaFile(site, "1", "/textFileJSONParseException.html", sofiaEnvironment, siteManager, pathManager, templateVariables, permissionManager);

    SiteCreationException siteCreationException = Assertions.assertThrows(SiteCreationException.class, sofiaFile::loadRootFile);
    Assertions.assertNotNull(siteCreationException);
    Assertions.assertEquals("net.cabezudo.json.exceptions.UnexpectedElementException: Unexpected element: es\": \"", siteCreationException.getMessage());
  }

  @Test
  public void testLoadConfigurationFileFromAttributeOutsidePath() throws ConfigurationException {

    sofiaEnvironment.loadConfiguration();

    String name = "example.com";

    Site site = new Site(1, name, "localhost");

    SofiaFile sofiaFile = new SofiaFile(site, "1", "../loadConfigurationFileFromAttributeOutsidePath.html", sofiaEnvironment, siteManager, pathManager, templateVariables, permissionManager);

    SiteCreationException siteCreationException = Assertions.assertThrows(SiteCreationException.class, sofiaFile::loadRootFile);
    Assertions.assertNotNull(siteCreationException);
    Assertions.assertEquals("net.cabezudo.sofia.creator.FilePathOutsideSourcePath: The file configurationFileInAttribute.json defined in 1/loadConfigurationFileFromAttributeOutsidePath.html:4:14 is outside the source path.", siteCreationException.getMessage());
  }

  @Test
  public void testConfigurationFileFromAttributeDoNotExists() throws ConfigurationException {

    sofiaEnvironment.loadConfiguration();

    String name = "example.com";

    Site site = new Site(1, name, "localhost");

    SofiaFile sofiaFile = new SofiaFile(site, "1", "../configurationFileFromAttributeDoNotExists.html", sofiaEnvironment, siteManager, pathManager, templateVariables, permissionManager);

    SiteCreationException siteCreationException = Assertions.assertThrows(SiteCreationException.class, sofiaFile::loadRootFile);
    Assertions.assertNotNull(siteCreationException);
    Assertions.assertEquals("net.cabezudo.sofia.creator.SofiaFileNotFoundException: The file 1/configurationFileFromAttributeDoNotExists.json defined in 1/configurationFileFromAttributeDoNotExists.html:4:14 do not exists.", siteCreationException.getMessage());
  }

  @Test
  public void testLoadConfigurationFileFromAttribute() throws ConfigurationException, SiteCreationException, HostNotFoundException, IOException {

    sofiaEnvironment.loadConfiguration();

    String name = "example.com";

    Site site = new Site(1, name, "localhost");

    SofiaFile sofiaFile = new SofiaFile(site, "1", "../loadConfigurationFileFromAttribute.html", sofiaEnvironment, siteManager, pathManager, templateVariables, permissionManager);

    sofiaFile.loadRootFile();
  }

  @Test
  public void testLoadFileFromAttributeWithFullPath() throws ConfigurationException, SiteCreationException, HostNotFoundException, IOException {

    sofiaEnvironment.loadConfiguration();

    String name = "example.com";

    Site site = new Site(1, name, "localhost");
    SofiaFile sofiaFile = new SofiaFile(site, "1", "../loadFileFromAttributeWithFullPath.html", sofiaEnvironment, siteManager, pathManager, templateVariables, permissionManager);

    sofiaFile.loadRootFile();
  }
}

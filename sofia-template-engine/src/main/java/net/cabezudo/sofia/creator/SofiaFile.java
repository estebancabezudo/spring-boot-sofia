package net.cabezudo.sofia.creator;

import net.cabezudo.html.HTMLParser;
import net.cabezudo.html.ParseException;
import net.cabezudo.html.nodes.Attribute;
import net.cabezudo.html.nodes.Attributes;
import net.cabezudo.html.nodes.DoctypeNode;
import net.cabezudo.html.nodes.Document;
import net.cabezudo.html.nodes.Element;
import net.cabezudo.html.nodes.FilePosition;
import net.cabezudo.html.nodes.Node;
import net.cabezudo.html.nodes.Nodes;
import net.cabezudo.html.nodes.Position;
import net.cabezudo.html.nodes.TagName;
import net.cabezudo.html.nodes.TextNode;
import net.cabezudo.json.JSON;
import net.cabezudo.json.exceptions.DuplicateKeyException;
import net.cabezudo.json.exceptions.JSONParseException;
import net.cabezudo.json.values.JSONObject;
import net.cabezudo.sofia.core.SofiaEnvironment;
import net.cabezudo.sofia.core.SofiaRuntimeException;
import net.cabezudo.sofia.files.FileHelper;
import net.cabezudo.sofia.security.Permission;
import net.cabezudo.sofia.security.PermissionManager;
import net.cabezudo.sofia.sites.Host;
import net.cabezudo.sofia.sites.HostNotFoundException;
import net.cabezudo.sofia.sites.Site;
import net.cabezudo.sofia.sites.SiteNotFoundException;
import net.cabezudo.sofia.sites.SourceNotFoundException;
import net.cabezudo.sofia.sites.service.PathManager;
import net.cabezudo.sofia.sites.service.SiteManager;
import net.cabezudo.sofia.users.service.Group;
import net.cabezudo.sofia.users.service.Groups;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * @author <a href="http://cabezudo.net">Esteban Cabezudo</a>
 * @version 0.01.00, 2022.02.25
 */
public class SofiaFile {
  private static final Logger log = LoggerFactory.getLogger(SofiaFile.class);

  private final Site site;
  private final Path requestFilePath;
  private final Path voidRootFilePath;
  private final CSSCode cssCode = new CSSCode();
  private final TextsFile textsFile;
  private final String version;
  private final JSCode jsCode;
  private final IdStorage idStorage;
  private final PathManager pathManager;
  private final TemplateVariables templateVariables;
  private final PermissionManager permissionManager;
  SofiaEnvironment sofiaEnvironment;
  private boolean hasDocType = false;
  private Document document;
  private Element head;

  public SofiaFile(HttpServletRequest request, SofiaEnvironment sofiaEnvironment, SiteManager siteManager, PathManager pathManager, TemplateVariables templateVariables, PermissionManager permissionManager) throws SiteNotFoundException, HostNotFoundException {
    this.sofiaEnvironment = sofiaEnvironment;
    this.pathManager = pathManager;
    this.idStorage = new IdStorage(pathManager);
    this.templateVariables = templateVariables;
    this.permissionManager = permissionManager;
    jsCode = new JSCode(sofiaEnvironment);
    textsFile = new TextsFile(sofiaEnvironment);
    site = siteManager.get(request);
    String serverName = request.getServerName();
    Host host = siteManager.getHostByName(serverName);
    this.version = host.getVersion();
    String requestURI = request.getRequestURI().substring(1);
    requestFilePath = Paths.get(requestURI);
    voidRootFilePath = FileHelper.removeExtension(requestURI);
  }

  private FilePosition getSourcePathRelativePosition(FilePosition position) {
    Path sourcesPath;
    try {
      sourcesPath = pathManager.getSourcesPath(site);
    } catch (SourceNotFoundException e) {
      throw new SofiaRuntimeException(e);
    }
    return new FilePosition(sourcesPath.relativize(position.getPath()), position.getLine(), position.getRow());
  }

  private String getSourcePathRelative(Path fullPath) {
    Path sourcesPath;
    try {
      sourcesPath = pathManager.getSourcesPath(site);
    } catch (SourceNotFoundException e) {
      throw new SofiaRuntimeException(e);
    }
    return sourcesPath.relativize(fullPath).toString();
  }

  private String getArrayOfSiteLanguages() {
    StringBuilder arrayOfSiteLanguages = new StringBuilder("[");
    Set<String> siteLanguages = textsFile.getLanguages();
    if (siteLanguages.isEmpty()) {
      return "[]";
    }
    for (String language : siteLanguages) {
      arrayOfSiteLanguages.append("'").append(language).append("', ");
    }
    return arrayOfSiteLanguages.substring(0, arrayOfSiteLanguages.length() - 2) + "]";
  }

  private void loadCommonsCSSFile() throws IOException, SourceAlreadyAdded, UndefinedLiteralException {
    try {
      cssCode.add(pathManager.getVersionedSourcesPath(site, version), Paths.get(Site.COMMONS_CSS_FILE_NAME), null, templateVariables, null);
    } catch (FileNotFoundException e) {
      log.info("File NOT FOUND " + Site.COMMONS_CSS_FILE_NAME);
    }
  }

  private void loadCommonsScriptFile() throws SiteCreationException, IOException, SourceAlreadyAdded {
    try {
      jsCode.add(pathManager.getVersionedSourcesPath(site, version), Paths.get(Site.COMMONS_SCRIPT_FILE_NAME), null, templateVariables, null);
    } catch (FileNotFoundException e) {
      log.info("File NOT FOUND " + Site.COMMONS_SCRIPT_FILE_NAME);
    }
  }

  private void loadCommonsConfigurationFile() throws IOException, JSONParseException, UndefinedLiteralException, DuplicateKeyException {
    templateVariables.add(pathManager.getVersionedSourcesPath(site, version), Paths.get(Site.COMMONS_CONFIGURATION_FILE_NAME));
  }

  private void loadCommonsTextFile() throws SiteCreationException {
    Path commonsFileTextsPath = pathManager.getVersionedSourcesPath(site, version).resolve(Site.TEXTS_FILE_NAME);
    loadTextFile(commonsFileTextsPath);
  }

  private void loadTextFile(Path file) throws SiteCreationException {
    try {
      JSONObject jsonTexts = JSON.parse(file, sofiaEnvironment.getCharset()).toJSONObject();
      log.info("Add texts file " + file + " to texts page.");
      textsFile.add(jsonTexts);
    } catch (JSONParseException e) {
      throw new SiteCreationException(e);
    } catch (IOException e) {
      log.debug("Cant read the common texts file " + Site.TEXTS_FILE_NAME);
    }
  }

  // Load the JSON configuration file for the file requested
  private void loadConfigurationFile() throws IOException, SiteCreationException, JSONParseException {
    Path configurationSourceFilePath = FileHelper.getConfigurationFile(requestFilePath);
    Path fullConfigurationSourceFilePath = pathManager.getVersionedSourcesPath(site, version).resolve(configurationSourceFilePath);

    // Search for a configuration file using the name of the page
    JSONConfigurationFile jsonSourceConfiguration = new JSONConfigurationFile();
    JSONObject jsonConfiguration = jsonSourceConfiguration.load(site, fullConfigurationSourceFilePath, null, templateVariables, sofiaEnvironment, pathManager);

    if (jsonConfiguration != null) {
      templateVariables.merge(jsonConfiguration);
    }
  }

  private void readHTMLFile() throws SiteCreationException, IOException {
    Path fullFilePath = pathManager.getVersionedSourcesPath(site, version).resolve(requestFilePath);
    if (!Files.exists(fullFilePath)) {
      log.debug("File not found " + fullFilePath);
      throw new FileNotFoundException(requestFilePath + " do not exists");
    }

    byte[] data;
    log.info("Read file " + getSourcePathRelative(fullFilePath));
    data = Files.readAllBytes(fullFilePath);
    String htmlCode = new String(data, sofiaEnvironment.getCharset());
    StringBuilder sb = new StringBuilder(htmlCode.length());

    int lineNumber = 0;
    try (Scanner scanner = new Scanner(htmlCode)) {
      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        lineNumber++;
        try {
          sb.append(templateVariables.replace(null, lineNumber, line)).append("\n");
        } catch (UndefinedLiteralException e) {
          throw new SiteCreationException(e.getMessage(), e);
        }
      }
    }

    try {
      HTMLParser htmlParser = new HTMLParser();
      document = htmlParser.parse(fullFilePath, sb.toString());
    } catch (ParseException e) {
      throw new SiteCreationException(e);
    }
  }

  private void checkIdsOnTree(Node node, IdStorage idStorage) throws SiteCreationException {
    try {
      if (node.isElement()) {
        Element element = (Element) node;
        Attribute id = element.getAttribute("id");
        if (id != null) {
          idStorage.add(site, element);
        }
      }
      Nodes nodes = node.getChilds();
      for (Node n : nodes) {
        checkIdsOnTree(n, idStorage);
      }
    } catch (DuplicateIdException e) {
      throw new SiteCreationException(e);
    }
  }

  private void searchForTextsOnTree(Node node) throws DuplicateIdException, SiteCreationException {
    if (node.isElement() && TagName.TEXT.equals(node.getTagName())) {
      Element element = (Element) node;
      Attribute languageAttribute = element.getAttribute("language");
      String languageFromAttribute = languageAttribute == null ? null : languageAttribute.getValue().trim();

      String id;
      Element parent = (Element) element.getParent();
      if (TagName.TEXTS.equals(parent.getTagName())) {
        id = processId(parent);
      } else {
        id = processId(element);
      }

      String text = element.getInnerText();
      element.removeChilds();
      String language;
      if (languageFromAttribute == null || languageFromAttribute.isBlank()) {
        language = "all";
      } else {
        language = languageFromAttribute;
      }
      try {
        textsFile.add(id, text, language);
      } catch (DuplicateKeyException e) {
        throw new SiteCreationException("The text defined in " + node.getPosition() + " for the language with code '" + languageFromAttribute + "' has already been defined.");
      }
      if (TagName.TEXTS.equals(parent.getTagName())) {
        node.remove();
      }
    }
    Nodes nodes = node.getChilds();
    for (Node n : nodes) {
      searchForTextsOnTree(n);
    }
  }

  private void renameTextsTags(Node node) {
    if (node.isElement() && (TagName.TEXT.equals(node.getTagName()) || "texts".equals(node.getTagName()))) {
      Element element = (Element) node;
      element.removeAttribute("language");
      element.getParent().replace(node, new Element(TagName.SPAN, element));
    }
    Nodes nodes = node.getChilds();
    for (Node n : nodes) {
      renameTextsTags(n);
    }
  }

  private String processId(Element element) throws DuplicateIdException {
    Attribute attributeId = element.getAttribute(Attribute.ID);
    String id;
    if (attributeId == null) {
      id = idStorage.nextId();
      element.setAttribute(Attribute.ID, id);
      idStorage.add(site, element);
    } else {
      id = attributeId.getValue();
      if (id.isBlank()) {
        id = idStorage.nextId();
        element.setAttribute(Attribute.ID, id);
        idStorage.add(site, element);
      }
    }
    return id;
  }

  private void processDocumentNode(Path requestFilePath, Node document) throws SiteCreationException, HostNotFoundException {
    log.info("Process document using " + requestFilePath + " like first processed document.");
    processNode(requestFilePath, document, null);
  }

  private void packCodeInFile(Element scriptNode) throws SiteCreationException {
    log.info("Pack the sources in the main file.");
    if (head == null) {
      throw new SiteCreationException("The root file must have an head tag");
    }
    head.appendChild(new TextNode("\n", null));

    Element styleNode = new Element(TagName.STYLE, false, document.getPosition());

    styleNode.appendChild(new TextNode(cssCode.getImports(), null));
    styleNode.appendChild(new TextNode(cssCode.getCode(), null));
    head.appendChild(styleNode);

    head.appendChild(new TextNode("\n", null));

    scriptNode.appendChild(new TextNode("\nconst templateVariables = " + templateVariables.toJSON() + ";\n", null));

    scriptNode.appendChild(new TextNode(jsCode.toCode(), null));
    head.appendChild(scriptNode);
    head.appendChild(new TextNode("\n", null));
  }

  private void processNode(Path requestFilePath, Node root, Caller caller) throws SiteCreationException, HostNotFoundException {
    Nodes nodes = root.getChilds();
    for (Node node : nodes) {
      if (Node.DOCTYPE_TYPE.equals(node.getType())) {
        hasDocType = true;
        log.info("doctype is in the document");
        continue;
      }
      if (Node.TEXT_TYPE.equals(node.getType())) {
        continue;
      }
      if (Node.COMMENT_TYPE.equals(node.getType())) {
        continue;
      }
      processElement(requestFilePath, node, caller);
      processNode(requestFilePath, node, caller);
    }
  }

  private void processElement(Path requestFilePath, Node node, Caller caller) throws SiteCreationException, HostNotFoundException {
    if (node instanceof Element) {
      Element element = (Element) node;
      try {
        switch (node.getTagName()) {
          case TagName.TITLE:
            break;
          case TagName.HEAD:
            if (head == null) {
              head = element;
            }
            break;
          case TagName.LINK:
            processLink(requestFilePath, element, caller);
            break;
          case TagName.SCRIPT:
            processScript(requestFilePath, element, caller);
            break;
          case TagName.STYLE:
            processStyle(requestFilePath, element, caller);
            break;
          default:
            // Check for references to a file.
            Nodes elementContent = processContainerElement(element, caller);
            if (elementContent != null) {
              node.add(elementContent);
            }
            break;
        }
      } catch (
        // TODO check if some of the exceptions are programming error to throw SofiaRuntimeException
          IOException | JSONParseException | SourceAlreadyAdded | SofiaFileNotFoundException | ParseException |
          FilePathOutsideSourcePath | UndefinedLiteralException | DuplicateKeyException | SiteNotFoundException e) {
        throw new SiteCreationException(e);
      }
      if (TagName.BODY.equals(element.getTagName())) {
        processBodyTag(site, requestFilePath, element, voidRootFilePath);
      }
    }
  }

  private void processBodyTag(Site site, Path requestFilePath, Element body, Path voidRootFilePath) {
    Attribute attribute;
    if ((attribute = body.getAttribute(Attribute.GROUPS)) != null) {
      body.removeAttribute(Attribute.GROUPS);
      Groups groups = new Groups(attribute.getValue());
      if (groups.isEmpty()) {
        log.debug("No groups found in tag");
      } else {
        for (Group group : groups) {
          log.debug("Found group: " + group.getName());
          String fileResource = '/' + requestFilePath.toString();
          Permission filePermission = new Permission(group.getName(), Permission.USER_ALL, Permission.ACCESS_GRANT, fileResource);
          permissionManager.add(site, filePermission);
          // Add to the list of permissions the page
          String textsResource = "/texts/" + voidRootFilePath.toString() + "/**";
          Permission textsPermission = new Permission(group.getName(), Permission.USER_ALL, Permission.ACCESS_GRANT, textsResource);
          permissionManager.add(site, textsPermission);
        }
      }
    } else {
      log.debug("No groups tag found");
    }
  }

  private void processLink(Path requestFilePath, Element link, Caller caller) throws SiteCreationException, IOException, JSONParseException, SourceAlreadyAdded, SofiaFileNotFoundException, SiteNotFoundException, HostNotFoundException {
    Attribute attribute;
    if ((attribute = link.getAttribute(Attribute.LIB)) != null) {
      processLinkToLibrary(requestFilePath, attribute.getValue(), attribute.getPosition(), caller);
      link.remove();
    }
  }

  private void processLinkToLibrary(Path requestFilePath, String libraryName, FilePosition position, Caller caller) throws SiteCreationException, IOException, SiteNotFoundException, HostNotFoundException {
    List<Path> files = new ArrayList<>();
    Path fullLibraryFilePath = getLibraryPath(libraryName, position);
    log.debug("Effective library path: " + fullLibraryFilePath);
    try (Stream<Path> stream = Files.walk(Paths.get(fullLibraryFilePath.toUri()))) {
      stream.filter(Files::isRegularFile)
          .forEach(files::add);
    }
    // TODO Try to avoid this bucle without use a RuntimeException for the SiteCreationException from processFile().
    for (Path filePath : files) {
      processFile(requestFilePath, libraryName, fullLibraryFilePath, filePath, caller);
    }
    // Check for image folder
    Path libraryImagesPath = fullLibraryFilePath.resolve(SofiaEnvironment.IMAGES_FOLDER_NAME);
    String partialPath = getLibraryPartialPath(libraryName, position);
    Path targetPath = pathManager.getVersionedSiteImagesPath(site, version).resolve(partialPath);
    if (Files.exists(libraryImagesPath) && Files.isDirectory(libraryImagesPath)) {
      log.debug("Copy images from " + libraryImagesPath + " to " + targetPath);
      FileHelper.copyFolder(libraryImagesPath, targetPath, StandardCopyOption.REPLACE_EXISTING);
    }
  }

  private void processFile(Path requestFilePath, String libraryName, Path basePath, Path fullFilePath, Caller caller) throws SiteCreationException {
    log.debug("Processing file from library: " + fullFilePath);
    String fileName = fullFilePath.toString();
    log.debug("fileName: " + fileName);
    if (fileName.endsWith(".js")) {
      try {
        Path filePath = basePath.relativize(fullFilePath);
        jsCode.add(basePath, filePath, null, templateVariables, new Caller("library " + libraryName, requestFilePath, null, caller));
      } catch (IOException e) {
        throw new SofiaRuntimeException(e);
      } catch (SourceAlreadyAdded | UndefinedLiteralException e) {
        throw new SiteCreationException(e);
      }
    }
    if (fileName.endsWith(".css")) {
      Path filePath = basePath.relativize(fullFilePath);
      try {
        cssCode.add(basePath, filePath, null, templateVariables, new Caller("file library" + libraryName, requestFilePath, null, caller));
      } catch (IOException e) {
        throw new SofiaRuntimeException(e);
      } catch (SourceAlreadyAdded e) {
        throw new SiteCreationException(e);
      }
    }
    if (fileName.endsWith("texts.json")) {
      loadTextFile(fullFilePath);
    }
  }

  private String getLibraryPartialPath(String libraryName, FilePosition position) throws InvalidAttributeValueException {
    if (!Pattern.compile("^([A-Z]|[a-z]|-|:)+:\\d+\\.\\d+$").matcher(libraryName).find()) {
      throw new InvalidAttributeValueException("The library name is not valid: " + libraryName, position);
    }
    return libraryName.replace(':', File.separatorChar);
  }

  private Path getLibraryPath(String libraryName, FilePosition position) throws LibraryNotFoundException, InvalidAttributeValueException, SourceNotFoundException {
    String partialPath = getLibraryPartialPath(libraryName, position);
    // Check the site library path first and return if exists
    Path libsPath = pathManager.getVersionedSourcesLibraryPath(site, version);
    Path siteLibBasePath = libsPath.resolve(partialPath);
    log.debug("Library path for site: " + siteLibBasePath);
    if (Files.exists(siteLibBasePath) && Files.isDirectory(siteLibBasePath)) {
      return siteLibBasePath;
    }
    // Check the system library path to return if exists or throw an error
    Path systemLibsPath = sofiaEnvironment.getSystemLibraryPath().resolve(partialPath);
    log.debug("Library path for system: " + systemLibsPath);
    if (Files.exists(systemLibsPath) && Files.isDirectory(systemLibsPath)) {
      return systemLibsPath;
    }
    throw new LibraryNotFoundException("The path for the library " + partialPath + " defined at " + position + " do not exists ", position);
  }

  private void processScript(Path filePath, Element script, Caller caller) throws SiteCreationException, IOException, JSONParseException, SourceAlreadyAdded, SofiaFileNotFoundException {
    Attribute attribute;
    if ((attribute = script.getAttribute(Attribute.FILE)) != null) {
      String value = attribute.getValue();
      Path fullFilePath = getPath(script, value);
      if (!fullFilePath.startsWith(pathManager.getVersionedSourcesPath(site, version))) {
        throw new FilePathOutsideSourcePath("The file " + value + " defined in the attribute file on the source file " + getSourcePathRelativePosition(attribute.getPosition()) + " is outside the source path.", attribute.getPosition());
      }
      if (!Files.exists(fullFilePath)) {
        log.warn("File not found: " + fullFilePath);
        throw new SofiaFileNotFoundException("The file " + value + " defined in the attribute file on the source file " + getSourcePathRelativePosition(attribute.getPosition()) + " do not exists.", attribute.getPosition());
      }

      Path versionedSourcesBasePath = pathManager.getVersionedSourcesPath(site, version);
      jsCode.add(versionedSourcesBasePath, Paths.get(value), null, templateVariables, new Caller("file attribute in " + script.getTagName() + " tag", filePath, new Position(script.getPosition()), caller));
      script.remove();
      return;
    }
    jsCode.add(filePath, script, null, templateVariables, new Caller("script tag", filePath, new Position(script.getPosition()), caller));
    script.remove();
  }

  private void processStyle(Path filePath, Element styleElement, Caller caller) throws IOException, SiteCreationException, JSONParseException, SourceAlreadyAdded, SofiaFileNotFoundException {
    Attribute attribute;
    if ((attribute = styleElement.getAttribute(Attribute.FILE)) != null) {
      String value = attribute.getValue();
      Path fullFilePath = getPath(styleElement, value);
      if (!fullFilePath.startsWith(pathManager.getVersionedSourcesPath(site, version))) {
        throw new FilePathOutsideSourcePath("The file " + value + " defined in the attribute file on the source file " + getSourcePathRelativePosition(attribute.getPosition()) + " is outside the source path.", attribute.getPosition());
      }
      if (!Files.exists(fullFilePath)) {
        log.warn("File not found: " + fullFilePath);
        throw new SofiaFileNotFoundException("The file " + value + " defined in the attribute file on the source file " + getSourcePathRelativePosition(attribute.getPosition()) + " do not exists.", attribute.getPosition());
      }

      Path versionedSourcesBasePath = pathManager.getVersionedSourcesPath(site, version);
      Path fileBasePath = versionedSourcesBasePath.resolve(filePath).getParent();
      Path filePathFromAttribute = getCheckedFilePathFromAttribute(fileBasePath, value, styleElement.getPosition());

      cssCode.add(versionedSourcesBasePath, filePathFromAttribute, null, templateVariables, new Caller("file attribute on style tag", filePath, new Position(styleElement.getPosition()), caller));
      styleElement.remove();
      return;
    }
    cssCode.add(filePath, styleElement, null, templateVariables, new Caller("style tag", filePath, new Position(styleElement.getPosition()), caller));
    styleElement.remove();
  }

  private Nodes processContainerElement(Element element, Caller caller) throws SofiaFileNotFoundException, IOException, JSONParseException, ParseException, SiteCreationException, SourceAlreadyAdded, DuplicateKeyException, HostNotFoundException {
    Attribute attributeConfigurationFile;
    Attribute attributeFile;

    Attribute idAttribute = element.getAttribute(Attribute.ID);
    String id = idAttribute == null ? null : idAttribute.getValue();

    if ((attributeFile = element.getAttribute(Attribute.FILE)) != null) {
      if (element.hasChilds()) {
        throw new SiteCreationException("A file container can't have children.");
      }
      if ((attributeConfigurationFile = element.getAttribute(Attribute.CONFIGURATION_FILE)) != null) {
        loadConfigurationFile(attributeConfigurationFile);
        element.removeAttribute(Attribute.CONFIGURATION_FILE);
      }
      element.removeAttribute(Attribute.FILE);
      return readHTMLFile(element, attributeFile, id, caller);
    }
    return null;
  }

  private Nodes readHTMLFile(Element element, Attribute fileAttribute, String configurationPrefix, Caller caller)
      throws SofiaFileNotFoundException, ParseException, IOException, SiteCreationException, DuplicateKeyException, HostNotFoundException {
    String value = fileAttribute.getValue();
    Path basePath;
    if (value.startsWith("/")) {
      basePath = pathManager.getVersionedSourcesPath(site, version);
    } else {
      basePath = element.getPosition().getPath().getParent();
    }
    Path relativeFilePath = getCheckedFilePathFromAttribute(basePath, value, fileAttribute.getPosition());
    Element body = readHTMLFile(pathManager.getVersionedSourcesPath(site, version), relativeFilePath, configurationPrefix, new Caller("file attribute on " + element.getTagName(), relativeFilePath, new Position(element.getPosition()), caller));
    Attributes attributes = body.getAttributes();
    for (Attribute attribute : attributes) {
      element.setAttribute(attribute.getName(), attribute.getValue());
    }
    return body.getChilds();
  }

  private Element readHTMLFile(Path basePath, Path filePath, String prefixForConfiguration, Caller caller)
      throws ParseException, IOException, SiteCreationException, DuplicateKeyException, HostNotFoundException {
    Path fullFilePath = basePath.resolve(filePath);

    Path fullConfigurationFilePath = Paths.get(FileHelper.removeExtension(fullFilePath) + ".json");

    JSONConfigurationFile jsonSourceConfiguration = new JSONConfigurationFile();
    // TODO check whatever the id must be use in html
    JSONObject jsonConfiguration = jsonSourceConfiguration.load(site, fullConfigurationFilePath, prefixForConfiguration, templateVariables, sofiaEnvironment, pathManager);

    if (jsonConfiguration != null) {
      templateVariables.merge(jsonConfiguration, prefixForConfiguration);
    }
    Path voidFilePath = FileHelper.removeExtension(filePath);

    readStyleFile(basePath, voidFilePath, prefixForConfiguration, caller);
    readScriptFile(basePath, voidFilePath, prefixForConfiguration, caller);
    readTextsFile(basePath, voidFilePath);

    byte[] data;
    log.info("Read file " + fullFilePath + " called from " + caller);
    data = Files.readAllBytes(fullFilePath);
    String htmlCode = new String(data, sofiaEnvironment.getCharset());
    StringBuilder sb = new StringBuilder(htmlCode.length());

    int lineNumber = 0;
    try (Scanner scanner = new Scanner(htmlCode)) {
      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        lineNumber++;
        try {
          sb.append(templateVariables.replace(prefixForConfiguration, lineNumber, line)).append("\n");
        } catch (UndefinedLiteralException e) {
          throw new SiteCreationException("The property " + e.getUndefinedLiteral() + " found in " + filePath + ":" + lineNumber + ":" + e.getPosition().getRow() + " is not defined on the JSON configuration files", e);
        }
      }
    }

    HTMLParser htmlParser = new HTMLParser();
    Document doc = htmlParser.parse(fullFilePath, sb.toString());

    processNode(filePath, doc, caller);

    Element body = (Element) doc.getNodeByTag(TagName.BODY);
    if (body == null) {
      throw new SiteCreationException("The file " + filePath + " must have an body element");
    }
    return body;
  }

  private void readStyleFile(Path basePath, Path voidFilePath, String id, Caller caller) throws SiteCreationException, IOException {
    Path cssFilePath = Paths.get(voidFilePath + ".css");
    try {
      log.info("Add CSS file from style in HTML file: " + cssFilePath);
      cssCode.add(basePath, cssFilePath, id, templateVariables, caller);
    } catch (FileNotFoundException e) {
      log.info("File NOT FOUND " + cssFilePath);
    } catch (SourceAlreadyAdded e) {
      throw new SiteCreationException(e);
    }
  }

  private void readScriptFile(Path basePath, Path voidFilePath, String id, Caller caller) throws SiteCreationException {
    Path jsFilePath = Paths.get(voidFilePath + ".js");
    try {
      log.info("Add JS file from script in HTML file: " + jsFilePath);
      jsCode.add(basePath, jsFilePath, id, templateVariables, caller);
    } catch (IOException e) {
      log.info("File NOT FOUND " + getSourcePathRelative(basePath.resolve(jsFilePath)));
    } catch (SourceAlreadyAdded e) {
      throw new SiteCreationException(e);
    }
  }

  private void readTextsFile(Path basePath, Path voidFilePath) throws SiteCreationException, IOException {
    Path textsFilePath = Paths.get(voidFilePath + ".texts.json");
    Path fullTextsFilePath = basePath.resolve(textsFilePath);
    try {
      JSONObject jsonTexts = JSON.parse(fullTextsFilePath, sofiaEnvironment.getCharset()).toJSONObject();
      log.info("Load the page text file " + textsFilePath + ".");
      textsFile.add(jsonTexts);
    } catch (NoSuchFileException e) {
      log.info("Page text file " + getSourcePathRelative(fullTextsFilePath) + " NOT FOUND.");
    } catch (JSONParseException e) {
      throw new SiteCreationException(e);
    }
  }

  private Path getCheckedFilePathFromAttribute(Path baseFilePath, String value, FilePosition position) throws FilePathOutsideSourcePath, SofiaFileNotFoundException {
    Path fullHTMLFilePath;
    if (value.startsWith("/")) {
      fullHTMLFilePath = pathManager.getVersionedSourcesPath(site, version).resolve(value.substring(1)).normalize();
    } else {
      fullHTMLFilePath = baseFilePath.resolve(value).normalize();
    }
    if (!fullHTMLFilePath.startsWith(pathManager.getVersionedSourcesPath(site, version))) {
      throw new FilePathOutsideSourcePath("The file " + getSourcePathRelative(fullHTMLFilePath) + " defined in " + getSourcePathRelativePosition(position) + " is outside the source path.", position);
    }
    if (!Files.exists(fullHTMLFilePath)) {
      throw new SofiaFileNotFoundException("The file " + getSourcePathRelative(fullHTMLFilePath) + " defined in " + getSourcePathRelativePosition(position) + " do not exists.", position);
    }
    return pathManager.getVersionedSourcesPath(site, version).relativize(fullHTMLFilePath);
  }

  private void loadConfigurationFile(Attribute attribute) throws FilePathOutsideSourcePath, SofiaFileNotFoundException, IOException, JSONParseException, UndefinedLiteralException, DuplicateKeyException {
    String value = attribute.getValue();
    Path templateVariablesBasePath = pathManager.getVersionedSourcesPath(site, version);
    Path filePath = getCheckedFilePathFromAttribute(templateVariablesBasePath, value, attribute.getPosition());
    templateVariables.add(templateVariablesBasePath, filePath);
  }

  private Path getPath(Node node, String value) {
    if (value.startsWith("/")) {
      return pathManager.getVersionedSourcesPath(site, version).resolve(value.substring(1));
    }
    Path parent = node.getPosition().getPath().getParent();
    Path fullParent = pathManager.getVersionedSourcesPath(site, version).resolve(parent);
    Path path = fullParent.resolve(value).normalize();
    log.debug("SofiaFile:getPath:path: " + path);
    return path;
  }

  public void loadRootFile() throws SiteCreationException, HostNotFoundException, IOException {
    try {
      loadCommonsConfigurationFile();
      loadCommonsTextFile();
      loadCommonsCSSFile();

      loadConfigurationFile();
      readStyleFile(pathManager.getVersionedSourcesPath(site, version), voidRootFilePath, null, null);
      readScriptFile(pathManager.getVersionedSourcesPath(site, version), voidRootFilePath, null, null);
      readTextsFile(pathManager.getVersionedSourcesPath(site, version), voidRootFilePath);
      readHTMLFile();

      processDocumentNode(requestFilePath, document);

      loadCommonsLibrariesFile();

      loadCommonsScriptFile();

      Element scriptNode = new Element(TagName.SCRIPT, false, document.getPosition());
      packCodeInFile(scriptNode);

      checkIdsOnTree(document, idStorage);

      searchForTextsOnTree(document);

      String arrayOfSiteLanguages = getArrayOfSiteLanguages();

      scriptNode.insertChild(0, new TextNode("const siteLanguages = " + arrayOfSiteLanguages + ";\n", null));

      renameTextsTags(document);

    } catch (SourceAlreadyAdded | DuplicateIdException | JSONParseException | DuplicateKeyException e) {
      if (sofiaEnvironment.isDevelopment()) {
        e.printStackTrace();
      }
      throw new SiteCreationException(e);
    }

    // DOC If the doctype is not present in the html source file the creator add one for us
    if (!hasDocType) {
      DoctypeNode doctype = new DoctypeNode("html");
      document.getChilds().insert(0, doctype);
    }
  }

  private void loadCommonsLibrariesFile() throws SiteCreationException {
    Path commonLibrariesPath = pathManager.getVersionedSourcesPath(site, version).resolve(Site.COMMONS_LIBRARIES_FILE_NAME);

    try (BufferedReader reader = new BufferedReader(new FileReader(commonLibrariesPath.toFile()))) {
      String line;
      int lineNumber = 0;
      while ((line = reader.readLine()) != null) {
        lineNumber++;
        if (line.startsWith(";")) {
          continue;
        }
        FilePosition position = new FilePosition(commonLibrariesPath, lineNumber, 1);
        Caller caller = new Caller("Commons libraries file", commonLibrariesPath, position, null);
        processLinkToLibrary(commonLibrariesPath, line, position, caller);
      }
    } catch (IOException e) {
      e.printStackTrace();
    } catch (HostNotFoundException | SiteNotFoundException e) {
      throw new RuntimeException(e);
    }

  }

  public void save() throws IOException, SiteNotFoundException, HostNotFoundException {
    Path targetFile = requestFilePath;
    log.debug("targetFile: " + targetFile);
    Path fullTargetFilePath = pathManager.getVersionedSitePath(site, version).resolve(targetFile);
    log.debug("fullTargetFilePath: " + fullTargetFilePath);
    Path parent = fullTargetFilePath.getParent();
    Files.createDirectories(parent);

    // TODO JavaScriptMinifier javaScriptMinifier = new JavaScriptMinifier();
    String code = document.toHTML();

    try (BufferedWriter writer = new BufferedWriter(new FileWriter(fullTargetFilePath.toFile()))) {
      writer.write(code);
    }
    textsFile.save(pathManager.getVersionedSiteTextsPath(site, version), voidRootFilePath);
  }
}
